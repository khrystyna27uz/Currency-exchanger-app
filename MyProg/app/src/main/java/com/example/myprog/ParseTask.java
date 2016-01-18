package com.example.myprog;

import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.myprog.db.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;


public class ParseTask extends AsyncTask<Void, Void, String> {

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String resultJson = "";
    DBHelper dbHelper;
    public static String LOG_TAG = "my_log";
    private final Context context;

    public ParseTask(Context _context) {
        context = _context;
    }


    @Override
    protected String doInBackground(Void... params) {
        try {
            URL url = new URL("http://resources.finance.ua/ua/public/currency-cash.json");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            resultJson = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson;
    }

    public void displayMessage() {
        if (PhoneLinkMapDetail.isStateInternet(context)) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .setTicker("Дані завантажуються")
                    .setContentTitle("Дані завантажуються")
                    .setContentText("Завантаження даних");
            builder.setOngoing(true);

            Notification notification = builder.build();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(1, notification);
        }
    }

    @Override
    protected void onPostExecute(String strJson) {
        super.onPostExecute(strJson);
        Log.d(LOG_TAG, strJson);

        JSONObject dataJsonObj = null;


        try {
            displayMessage();
            dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            ContentValues cv1 = new ContentValues();
            ContentValues cv3 = new ContentValues();
            dataJsonObj = new JSONObject(strJson);
            JSONArray organizations = dataJsonObj.getJSONArray("organizations");
            Cursor tablesNames = db.rawQuery("SELECT name FROM sqlite_master WHERE type = ?", new String[]{"table"});
            tablesNames.moveToFirst();
            while (!tablesNames.isAfterLast()) {
                String cutTableName = tablesNames.getString(0);
                if (!cutTableName.equals("android_metadata")) {
                    if (cutTableName.contains("old")) {
                        db.execSQL("DROP TABLE " + cutTableName);
                    }
                }
                tablesNames.moveToNext();
            }
            tablesNames.close();
            tablesNames = db.rawQuery("SELECT name FROM sqlite_master WHERE type = ?", new String[]{"table"});
            tablesNames.moveToFirst();
            while (!tablesNames.isAfterLast()) {
                String cutTableName = tablesNames.getString(0);
                if (!cutTableName.equals("android_metadata")) {
                    if (!cutTableName.contains("old")) {
                        db.execSQL("ALTER TABLE " + cutTableName + " RENAME TO old" + cutTableName);
                    }
                }
                tablesNames.moveToNext();
            }
            tablesNames.close();
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
            Log.d(LOG_TAG, "--- onMain database 1 ---");
            db.execSQL("create table cur ("
                    + "title text ,"
                    + "currency text,"
                    + "ask real,"
                    + "bid real" + ");");
            db.execSQL("create table orgtypes ("
                    + "one text,"
                    + "two text" + ");");
            Log.d(LOG_TAG, "--- onMain database 3 ---");
            for (int i = 0; i < organizations.length(); i++) {
                JSONObject org = organizations.getJSONObject(i);
                String id = org.getString("id");
                cv.put("id1", id);
                String oldId = org.getString("oldId");
                cv.put("oldId", oldId);
                String orgType = org.getString("orgType");
                cv.put("orgType", orgType);
                String title = org.getString("title");
                cv.put("title", title);
                String regionId = org.getString("regionId");
                JSONObject regions = dataJsonObj.getJSONObject("regions");
                String idr = regions.getString(regionId);
                cv.put("regionId", idr);
                String cityId = org.getString("cityId");
                JSONObject Mcities = dataJsonObj.getJSONObject("cities");
                String cities = Mcities.getString(cityId);
                cv.put("cityId", cities);
                String phone = org.getString("phone");
                cv.put("phone", phone);
                String address = org.getString("address");
                cv.put("address", address);
                String link = org.getString("link");
                cv.put("link", link);
                long rowID = db.insert("org", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                JSONObject currencies = org.getJSONObject("currencies");
                Iterator<String> currencies_names = currencies.keys();
                for (int k = 0; k < currencies.length(); k++) {
                    String currency = currencies_names.next();
                    cv1.put("title", title);
                    JSONObject curN = dataJsonObj.getJSONObject("currencies");
                    String curNames = curN.getString(currency);
                    cv1.put("currency", curNames);
                    String ask = currencies.getJSONObject(currency).getString("ask");
                    cv1.put("ask", ask);
                    String bid = currencies.getJSONObject(currency).getString("bid");
                    cv1.put("bid", bid);
                    long rowID3 = db.insert("cur", null, cv1);
                    Log.d(LOG_TAG, "row inserted333, ID = " + rowID3);

                }

            }

            JSONObject orgTypes = dataJsonObj.getJSONObject("orgTypes");
            String id = orgTypes.getString("1");
            cv3.put("one", id);
            String title = orgTypes.getString("2");
            cv3.put("two", title);
            long rowID = db.insert("orgtypes", null, cv3);
            Log.d(LOG_TAG, "row inserted3, ID = " + rowID);

            dbHelper.close();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}