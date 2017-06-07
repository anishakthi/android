package com.example.user.travel.db.config;

import android.provider.BaseColumns;

/**
 * Created by User on 6/8/2017.
 */

public class TravelDestination {
    public static final String DB_NAME = "com.aziflaj.travellist.db";
    public static final int DB_VERSION = 1;

    public class TravelEntry implements BaseColumns {
        public static final String TABLE = "travels";

        public static final String COL_TRAVEL_TITLE = "title";
    }
}
