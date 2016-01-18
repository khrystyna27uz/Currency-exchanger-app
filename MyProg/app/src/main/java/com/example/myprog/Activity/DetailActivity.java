package com.example.myprog.Activity;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.myprog.Fragment.MyDialogFragment;
import com.example.myprog.PhoneLinkMapDetail;
import com.example.myprog.R;
import com.example.myprog.adapters.DetailAdapter;
import com.example.myprog.db.DBHelper;
import com.example.myprog.list.Cur;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;


public class DetailActivity extends AppCompatActivity implements FloatingActionsMenu.OnFloatingActionsMenuUpdateListener, View.OnClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mNameBank;
    private TextView mRegion;
    private TextView mCity;
    private TextView mAddress;
    private TextView mMobil;
    private List<Cur> mData = new ArrayList<>();
    private Toolbar mToolbar;
    private boolean is_Expanded;
    private FrameLayout mFrameLayout;
    private FloatingActionButton mPhone;
    private FloatingActionButton mSite;
    private FloatingActionButton mMap;
    private FloatingActionsMenu mFloatingActionsMenu;
    DialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        initUI();
        getDatafromAdapter();
        mLayoutManager = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mData = getDummyData();
        mAdapter = new DetailAdapter(getApplicationContext(), mData);
        mRecyclerView.setAdapter(mAdapter);
        settingToolbar();
        if (savedInstanceState != null)
            is_Expanded = savedInstanceState.getBoolean("is_Expanded");


        if (is_Expanded)
            mFrameLayout.setVisibility(View.VISIBLE);


        mMap.setOnClickListener(this);
        mPhone.setOnClickListener(this);
        mSite.setOnClickListener(this);
        mFloatingActionsMenu.setOnFloatingActionsMenuUpdateListener(this);
    }

    private void initUI() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv1);
        mFloatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        mNameBank = (TextView) findViewById(R.id.nameBank_D);
        mFrameLayout = (FrameLayout) findViewById(R.id.flVisibility);
        mRegion = (TextView) findViewById(R.id.Reg_D);
        mCity = (TextView) findViewById(R.id.City_D);
        mAddress = (TextView) findViewById(R.id.address_D);
        mMobil = (TextView) findViewById(R.id.mobil_D);
        mPhone = (FloatingActionButton) findViewById(R.id.phone_AD);
        mMap = (FloatingActionButton) findViewById(R.id.map_AD);
        mSite = (FloatingActionButton) findViewById(R.id.site_AD);

    }
    private void getDatafromAdapter()
    {
        Intent intent = getIntent();
        String nameBank = intent.getStringExtra("nameBank");
        String region = intent.getStringExtra("region");
        String city = intent.getStringExtra("city");
        String address = intent.getStringExtra("address");
        String phone = intent.getStringExtra("phone");
        mNameBank.setText(nameBank);
        mRegion.setText(region);
        mCity.setText(city);
        mAddress.setText(address);
        mMobil.setText(phone);
    }

    private void settingToolbar() {
        mToolbar.setNavigationIcon(R.mipmap.ic_arrow1);
        mToolbar.setTitle(mNameBank.getText().toString());
        mToolbar.setSubtitle(mCity.getText().toString());
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public List<Cur> getDummyData() {
        List<Cur> currencies = new ArrayList<>();
        DBHelper dbh = new DBHelper(this);
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor cr = db.rawQuery("SELECT currency, ask, bid FROM cur WHERE title = ?", new String[]{mNameBank.getText().toString()});
        while (cr.moveToNext()) {
            String currency = cr.getString(0);
            Double ask = Double.parseDouble(cr.getString(1));
            Double bid = Double.parseDouble(cr.getString(2));
            Cursor crOld = db.rawQuery("SELECT ask, bid FROM oldcur WHERE title = ? AND currency = ?", new String[]{mNameBank.getText().toString(), currency});
            Double askOld = 0.0;
            Double bidOld = 0.0;
            if(crOld.getCount()>0)
            {
                crOld.moveToFirst();
                askOld = Double.parseDouble(crOld.getString(0));
                bidOld = Double.parseDouble(crOld.getString(1));
            }
            int AskImage=R.mipmap.ic_green;
            int BidImage=R.mipmap.ic_green;

            currencies.add(new Cur(currency, ask.toString(), bid.toString(),ask>=askOld? AskImage:BidImage,bid>=bidOld?AskImage:BidImage));
        }
        cr.close();
        return currencies;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        dialogFragment = new MyDialogFragment();
        dialogFragment.show(getFragmentManager(), "dlg1");
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_Expanded", is_Expanded);
    }

    @Override
    public void onMenuExpanded() {
        is_Expanded = true;
        mFrameLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMenuCollapsed() {
        is_Expanded = false;
        mFrameLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.phone_AD:
                PhoneLinkMapDetail phone = new PhoneLinkMapDetail();
                phone.Phone(this, mMobil);
                break;
            case R.id.map_AD:
                PhoneLinkMapDetail map = new PhoneLinkMapDetail();
               map.Map(this, mRegion, mCity, mAddress);
                break;
            case R.id.site_AD:
                PhoneLinkMapDetail link = new PhoneLinkMapDetail();
                link.Link(this, mNameBank);
                break;
        }
    }
}
