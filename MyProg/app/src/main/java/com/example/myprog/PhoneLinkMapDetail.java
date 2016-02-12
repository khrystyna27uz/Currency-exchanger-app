package com.example.myprog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.example.myprog.Activity.DetailActivity;
import com.example.myprog.Activity.GoogleMap;
import com.example.myprog.Activity.MapActivity;
import com.example.myprog.db.DBHelper;

import java.io.File;


public class PhoneLinkMapDetail {

    public static void Phone(Context context, TextView textView) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(String.format("tel:%s", textView.getText())));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }

    public static void Link(Context context, TextView textView) {
        DBHelper dbh = new DBHelper(context);
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT link FROM org WHERE title = ?", new String[]{textView.getText().toString()});
        cr.moveToFirst();
        Uri address = Uri.parse(cr.getString(0));
        Intent intent = new Intent(Intent.ACTION_VIEW, address);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        cr.close();
    }

    public static void Map(Context context, TextView region, TextView city, TextView address,TextView nameBank) {
        Intent intent = new Intent(context, GoogleMap.class);
        intent.putExtra("region", region.getText().toString());
        intent.putExtra("city", city.getText().toString());
        intent.putExtra("address", address.getText().toString());
        intent.putExtra("nameBank", nameBank.getText().toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void Detail(Context context, TextView nameBank, TextView region, TextView city, TextView address, TextView mobil) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("nameBank", nameBank.getText().toString());
        intent.putExtra("region", region.getText().toString());
        intent.putExtra("city", city.getText().toString());
        intent.putExtra("address", address.getText().toString());
        intent.putExtra("phone", mobil.getText().toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    public static boolean isStateInternet(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();


    }

    public static boolean doesDatabaseExist(Context context, String databName) {
        File dbFile = context.getDatabasePath(databName);

        return dbFile.exists();
    }

}

