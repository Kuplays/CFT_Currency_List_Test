package com.cft.kuplays.currency;

import android.provider.BaseColumns;

public final class DatabaseContract {
    private DatabaseContract() {}

    public static class CurrencyEntry implements BaseColumns {
        public static final String TABLE_NAME = "json_holder";
        public static final String JSON_STRING_FIELD = "json_string_field";
    }
}
