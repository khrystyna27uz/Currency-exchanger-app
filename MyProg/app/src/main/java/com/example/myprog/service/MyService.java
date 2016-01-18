package com.example.myprog.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.myprog.ParseTask;


public class MyService extends Service {
    final String LOG_TAG = "my_log";

    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreateService");
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        someTask();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroyService");
    }

    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "onBind");
        return null;
    }

    void someTask() {
        Log.d(LOG_TAG, "someTask");
        new ParseTask(getApplicationContext()).execute();
    }

}
