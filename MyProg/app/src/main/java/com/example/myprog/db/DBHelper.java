package com.example.myprog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.provider.BaseColumns;

/**
 * Created by Христинка on 02.12.2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    final String LOG_TAG = "myLog1001";
    public static final String DATABASE_NAME = "DBconverter.db";
    public static final int DATABASE_VERSION = 1;
    public static final String ID_COLUMN = "id1";
    public static final String OLD_COLUMN = "oldId";
    public static final String ORG_COLUMN = "orgType";
    public static final String TITLE_COLUMN = "title";
    public static final String REG_COLUMN = "regionId";
    public static final String CITY_COLUMN = "cityId";
    public static final String PHONE_COLUMN = "phone";
    public static final String A_COLUMN = "address";
    public static final String L_COLUMN = "link";
    public static final String TITLECUR = "title";
    public static final String CURRENCYCUR = "currency";
    public static final String ASKCUR = "ask";
    public static final String BIDCUR = "bid";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table org ("
                + "id1 text primary key,"
                + "oldId text,"
                + "orgType text,"
                + "title text,"
                + "regionId text,"
                + "cityId text,"
                + "phone text,"
                + "address text,"
                + "link text" + ");");
        Log.d(LOG_TAG, "--- onCreate database 1 ---");
        db.execSQL("create table cur ("
                + "title text,"
                + "currency text,"
                + "ask real,"
                + "bid real" + ");");
        Log.d(LOG_TAG, "--- onCreate database 2 ---");
        db.execSQL("create table orgtypes ("
                + "one text,"
                + "two text" + ");");
        Log.d(LOG_TAG, "--- onCreate database 3 ---");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
