package com.cft.kuplays.currency;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApplicationLevelRequestHandler {
    private static ApplicationLevelRequestHandler instance;
    private RequestQueue requestQueue;
    private static Context context;

    private ApplicationLevelRequestHandler(Context ctx) {
        context = ctx;
        requestQueue = getRequestQueue();
    }

    public static synchronized ApplicationLevelRequestHandler getInstance(Context ctx) {
        if (instance == null) {
            instance = new ApplicationLevelRequestHandler(ctx);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void addToRequestQueue(Request request) {
        getRequestQueue().add(request);
    }
}
