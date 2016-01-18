package com.example.myprog.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.myprog.ParseTask;
import com.example.myprog.PhoneLinkMapDetail;
import com.example.myprog.R;
import com.example.myprog.adapters.RecyclerViewAdapter;
import com.example.myprog.db.DBHelper;
import com.example.myprog.list.Org;
import com.example.myprog.service.MyService;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static String LOG_TAG = "my_log";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Org> mData = new ArrayList<>();
    RecyclerViewAdapter mRecyclerViewAdapter;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);
        Log.d(LOG_TAG, "onCreate ");
        if(PhoneLinkMapDetail.isStateInternet(getApplicationContext())) {

             Intent intent = new Intent(this, MyService.class);
             startService(intent);
            mData = new ArrayList<>();
            mRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), mData);
        }
        else {
            mData = getDummyData();
            mRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), mData);

        }
            if (PhoneLinkMapDetail.doesDatabaseExist(this,DBHelper.DATABASE_NAME)) {
                mData = getDummyData();
                mRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), mData);

        }
        initUI();
        mLayoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mData = getDummyData();
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), mData);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Loading", Toast.LENGTH_LONG);
                toast.show();
                mSwipeRefreshLayout.setRefreshing(false);
                new ParseTask(getApplicationContext()).execute();
            }
        });


    }
    private void startAlarmManager() {
        startService(new Intent(this, MyService.class));
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this, MyService.class);
        PendingIntent pintent = PendingIntent
                .getService(this, 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                1800 * 1000, pintent);
        stopService(new Intent(this, MyService.class));

    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new DBHelper(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

    }

    public List<Org> getDummyData() {
        List<Org> organizations = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query("org", new String[]{DBHelper.ID_COLUMN, DBHelper.OLD_COLUMN, DBHelper.ORG_COLUMN, DBHelper.TITLE_COLUMN,
                        DBHelper.REG_COLUMN, DBHelper.CITY_COLUMN, DBHelper.PHONE_COLUMN, DBHelper.A_COLUMN, DBHelper.L_COLUMN},
                null, null,
                null, null, null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(DBHelper.TITLE_COLUMN));
            String region = cursor.getString(cursor.getColumnIndex(DBHelper.REG_COLUMN));
            String city = cursor.getString(cursor.getColumnIndex(DBHelper.CITY_COLUMN));
            String mob = cursor.getString(cursor.getColumnIndex(DBHelper.PHONE_COLUMN));
            String address = cursor.getString(cursor.getColumnIndex(DBHelper.A_COLUMN));
            organizations.add(new Org(title, region, city, mob, address));

        }
        cursor.close();
        return organizations;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchView search =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setQueryHint("Enter text");
        search.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop ");
    }

    @Override
    public void onDestroy() {
       startAlarmManager();

        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy ");

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mRecyclerViewAdapter.getFilter().filter("");
        } else {
            mRecyclerViewAdapter.getFilter().filter(newText);
        }
        return true;
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }




    }
}
