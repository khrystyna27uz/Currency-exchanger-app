package com.example.myprog.adapters;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myprog.list.Cur;
import com.example.myprog.R;
import com.example.myprog.db.DBHelper;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.CustomViewHolder> {
    private final Context mContext;
    private final List<Cur> mData;

    public DetailAdapter(Context _context, List<Cur> _data) {
        mContext = _context;
        mData = _data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup _viewGroup, int _viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.currencies, _viewGroup, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder _customViewHolder, int i) {
        _customViewHolder.onBind();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        private TextView mNameVal;
        private TextView mAsk;
        private TextView mBid;
        private TextView mtextViewIconNearAsk;
        private TextView mtextViewIconNearBid;

        public CustomViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv_D);
            mNameVal = (TextView) itemView.findViewById(R.id.TextVal_C);
            mAsk = (TextView) itemView.findViewById(R.id.TextAsk_C);
            mBid = (TextView) itemView.findViewById(R.id.TextBid_C);
            mtextViewIconNearAsk = (TextView) itemView.findViewById(R.id.textView_ic_green_C);
            mtextViewIconNearBid = (TextView) itemView.findViewById(R.id.textView_ic_green2_C);


        }

        public void onBind() {
            Cur item = mData.get(getPosition());
            mNameVal.setText(item.getNameVal());
            mAsk.setText(item.getAsk());
            mBid.setText(item.getBid());
            mtextViewIconNearAsk.setBackgroundResource(item.geticonNearAsk());
            mtextViewIconNearBid.setBackgroundResource(item.geticonNearBid());

        }
    }
}
