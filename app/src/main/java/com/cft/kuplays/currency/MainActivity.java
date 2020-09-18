package com.cft.kuplays.currency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "https://www.cbr-xml-daily.ru/daily_json.js";
    private static final String NET_TAG = "TAG_RESPONSE";
    private static final String ONSTOP = "TAG_ONSTOP";
    private static final String ON_DB_CALL = "TAG_DB";
    private static final String REQUEST_TAG = "request_tag";

    AdapterCurrencyList adapter;
    AdapterValutePicker valuteAdapter;
    private ListView listView;
    private Spinner valuteSpinner;
    private Button btnRefresh;
    private String dataJSON = null;

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private Handler autoUpdateHandler = null;
    Runnable thread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        listView = findViewById(R.id.lvMain);
        valuteSpinner = findViewById(R.id.spinner_valutePicker);

        final StringRequest request = new StringRequest(Request.Method.GET,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataJSON = response;
                new AsyncParseJSON(MainActivity.this).execute();
                Toast.makeText(MainActivity.this, "ДАННЫЕ ЗАГРУЖЕНЫ С СЕТИ", Toast.LENGTH_SHORT).show();
                Log.v(NET_TAG, "DATA FETCHED FROM NETWORK");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(NET_TAG, "Error");
            }
        });

        request.setTag(REQUEST_TAG);

        if (isTableEmpty()) {
            ApplicationLevelRequestHandler.getInstance(this).addToRequestQueue(request);
        } else {
            database = databaseHelper.getReadableDatabase();
            Cursor cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.CurrencyEntry.TABLE_NAME, null);
            cursor.moveToFirst();
            dataJSON = cursor.getString(cursor.getColumnIndex(DatabaseContract.CurrencyEntry.JSON_STRING_FIELD));
            cursor.close();
            new AsyncParseJSON(MainActivity.this).execute();
            Toast.makeText(MainActivity.this, "ДАННЫЕ ЗАГРУЖЕНЫ С БД", Toast.LENGTH_SHORT).show();
            Log.v(ON_DB_CALL, "DATA FETCHED FROM DATABASE");
        }

        btnRefresh = findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationLevelRequestHandler.getInstance(getApplicationContext()).addToRequestQueue(request);
            }
        });

        autoUpdateHandler = new Handler();
        thread = new Runnable() {
            @Override
            public void run() {
                ApplicationLevelRequestHandler.getInstance(getApplicationContext()).addToRequestQueue(request);
                Toast.makeText(getApplicationContext(), "UPDATE CALLED", Toast.LENGTH_SHORT).show();
                autoUpdateHandler.postDelayed(this, 600000);
            }
        };
        autoUpdateHandler.postDelayed(thread, 600000);
    }

    private class AsyncParseJSON extends AsyncTask<Void, Void, Void> {
        public Context context;
        List<CurrencyItem> currencyItemList;

        public AsyncParseJSON(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                if (dataJSON != null) {
                    JSONArray jsonArray = null;
                    try {
                        JSONObject wholeJSON = new JSONObject(dataJSON);
                        JSONObject valuteData = wholeJSON.getJSONObject("Valute");
                        CurrencyItem currencyItem;
                        currencyItemList = new ArrayList<CurrencyItem>();
                        Iterator<?> valuteKeys = valuteData.keys();

                        while (valuteKeys.hasNext()) {
                            String currentKey = (String)valuteKeys.next();
                            JSONObject JSONcurrentEntry = new JSONObject(valuteData.get(currentKey).toString());
                            String id = JSONcurrentEntry.getString("ID");
                            String numCode = JSONcurrentEntry.getString("NumCode");
                            String charCode = JSONcurrentEntry.getString("CharCode");
                            int nominal = JSONcurrentEntry.getInt("Nominal");
                            String name = JSONcurrentEntry.getString("Name");
                            double value = JSONcurrentEntry.getDouble("Value");
                            double previousValue = JSONcurrentEntry.getDouble("Previous");
                            currencyItem = new CurrencyItem(id,
                                    numCode, charCode, nominal, name, value, previousValue);
                            currencyItemList.add(currencyItem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter = new AdapterCurrencyList(context, currencyItemList);
            valuteAdapter = new AdapterValutePicker(context, currencyItemList);
            listView.setAdapter(adapter);
            valuteSpinner.setAdapter(valuteAdapter);

            valuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    CurrencyItem selected = (CurrencyItem)valuteAdapter.getItem(position);

                    for (CurrencyItem item : currencyItemList) {
                        item.setDisplayValue(calculateValuteDisplay(item.getValue(),
                                selected.getValue(), selected.getNominal()));
                        item.setDisplayPreviousValue(calculateValuteDisplay(item.getPreviousValue(),
                                selected.getPreviousValue(), selected.getNominal()));
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    private double calculateValuteDisplay(double initialRate, double convertRate, int convertNominal) {
        return initialRate / convertRate * convertNominal;
    }

    private boolean isTableEmpty() {
        boolean isEmpty;
        database = databaseHelper.getReadableDatabase();
        final String SQL = "SELECT COUNT(*) FROM " + DatabaseContract.CurrencyEntry.TABLE_NAME;
        Cursor cursor = database.rawQuery(SQL, null);
        cursor.moveToFirst();
        if (cursor.getInt(0) == 0) {
            isEmpty = true;
        } else {
            isEmpty = false;
        }

        cursor.close();

        return isEmpty;
    }

    @Override
    protected void onPause() {
        Cursor cursor = null;
        if (isTableEmpty()) {
            database = databaseHelper.getWritableDatabase();
            ContentValues value = new ContentValues();
            value.put(DatabaseContract.CurrencyEntry.JSON_STRING_FIELD, dataJSON);
            database.insert(DatabaseContract.CurrencyEntry.TABLE_NAME, null, value);
        } else {
            database = databaseHelper.getReadableDatabase();
            cursor = database.rawQuery("SELECT * FROM " + DatabaseContract.CurrencyEntry.TABLE_NAME, null);
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.CurrencyEntry._ID));
            cursor.close();
            ContentValues value = new ContentValues();
            String selection = DatabaseContract.CurrencyEntry._ID + " LIKE ?";
            value.put(DatabaseContract.CurrencyEntry.JSON_STRING_FIELD, dataJSON);
            database = databaseHelper.getWritableDatabase();
            database.update(DatabaseContract.CurrencyEntry.TABLE_NAME, value, selection, new String[] {String.valueOf(id)});
        }
        super.onPause();
    }

    @Override
    protected void onStop () {
        super.onStop();
        autoUpdateHandler.removeCallbacks(thread);
        if (ApplicationLevelRequestHandler.getInstance(getApplicationContext()).getRequestQueue() != null) {
            ApplicationLevelRequestHandler.getInstance(getApplicationContext()).getRequestQueue().cancelAll(REQUEST_TAG);
        }
    }

    @Override
    protected void onDestroy() {
        database.close();
        databaseHelper.close();
        super.onDestroy();
    }
}
