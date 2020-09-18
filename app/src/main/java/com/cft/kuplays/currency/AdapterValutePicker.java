package com.cft.kuplays.currency;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AdapterValutePicker extends BaseAdapter {
    private Context context;
    private List<CurrencyItem> valuteItems;

    public AdapterValutePicker(Context context, List<CurrencyItem> items) {
        this.context = context;
        this.valuteItems = new ArrayList<>(items);
        this.valuteItems.add(0, new CurrencyItem("RUS",
                "001", "RUS", 1, "Российский рубль", 1, 1));
    }

    @Override
    public int getCount() {
        return this.valuteItems.size();
    }

    @Override
    public Object getItem(int position) {
        return this.valuteItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomSpinnerItem customSpinnerItem = null;

        if (convertView != null) {
            customSpinnerItem = (CustomSpinnerItem)convertView.getTag();
        } else {
            customSpinnerItem = new CustomSpinnerItem();
            LayoutInflater layoutInfiater =
                    (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInfiater.inflate(R.layout.layout_spinner_item, null);
            customSpinnerItem.setTvCharCode((TextView)convertView.findViewById(R.id.tvSpinnerCharCode));
            customSpinnerItem.setTvName((TextView)convertView.findViewById(R.id.tvSpinnerName));
            convertView.setTag(customSpinnerItem);
        }

        customSpinnerItem.setCharCodeText(valuteItems.get(position).getCharCode());
        customSpinnerItem.setNameText(valuteItems.get(position).getName());

        return convertView;
    }
}

class CustomSpinnerItem {
    private TextView tvCharCode;
    private TextView tvName;

    public TextView getTvCharCode() {
        return tvCharCode;
    }

    public void setTvCharCode(TextView tvCharCode) {
        this.tvCharCode = tvCharCode;
    }

    public TextView getTvName() {
        return tvName;
    }

    public void setTvName(TextView tvName) {
        this.tvName = tvName;
    }

    public void setCharCodeText(String text) {
        this.tvCharCode.setText(text);
    }

    public void setNameText(String text) {
        this.tvName.setText(text);
    }
}
