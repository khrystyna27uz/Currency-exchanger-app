package com.example.myprog.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.myprog.PhoneLinkMapDetail;
import com.example.myprog.list.Org;
import com.example.myprog.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder> implements Filterable {
    private final Context mContext;

    private List<Org> mData;
    private List<Org> mArrayList;

    public RecyclerViewAdapter(Context _context, List<Org> _data) {
        mContext = _context;
        mData = _data;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup _viewGroup, int _viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.cardview, _viewGroup, false);
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

    @Override
    public Filter getFilter() {
        return new Filter();
    }

    public class Filter extends android.widget.Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults oReturn = new FilterResults();
            List<Org> results = new ArrayList<>();
            if (mArrayList == null)
                mArrayList = mData;
            if (constraint != null) {
                if (mArrayList != null & mArrayList.size() > 0) {
                    for (final Org organization : mArrayList) {
                        if (organization.getName().toLowerCase().contains(constraint.toString().toLowerCase()))
                            results.add(organization);
                        else if (organization.getC().toLowerCase().contains(constraint.toString().toLowerCase()))
                            results.add(organization);
                        else if (organization.getR().toLowerCase().contains(constraint.toString().toLowerCase()))
                            results.add(organization);
                    }
                }
                oReturn.values = results;
            }
            return oReturn;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mData = (ArrayList<Org>) results.values;
            notifyDataSetChanged();
        }
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        private TextView mNameBank;
        private TextView mRegion;
        private TextView mCity;
        private TextView mMobil;
        private TextView mAddress;

        public CustomViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            mNameBank = (TextView) itemView.findViewById(R.id.nameBank_CV);
            mRegion = (TextView) itemView.findViewById(R.id.region_CV);
            mCity = (TextView) itemView.findViewById(R.id.city_CV);
            mMobil = (TextView) itemView.findViewById(R.id.mobil_CV);
            mAddress = (TextView) itemView.findViewById(R.id.address_CV);
            Button buttonWeb = (Button) itemView.findViewById(R.id.button_web_CV);
            buttonWeb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhoneLinkMapDetail phoneLinkMap = new PhoneLinkMapDetail();
                    phoneLinkMap.Link(mContext, mNameBank);
                }

            });

            Button btnTelephone = (Button) itemView.findViewById(R.id.btn_telephone_CV);
            btnTelephone.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    PhoneLinkMapDetail phoneLinkMap = new PhoneLinkMapDetail();
                    phoneLinkMap.Phone(mContext, mMobil);
                }
            });
            Button btnMap = (Button) itemView.findViewById(R.id.button_map_CV);
            btnMap.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    PhoneLinkMapDetail phoneLinkMap = new PhoneLinkMapDetail();
                    phoneLinkMap.Map(mContext, mRegion, mCity, mAddress);

                }
            });
            Button btnDetail = (Button) itemView.findViewById(R.id.btn_details_CV);
            btnDetail.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    PhoneLinkMapDetail phoneLinkMap = new PhoneLinkMapDetail();
                    phoneLinkMap.Detail(mContext, mNameBank, mRegion, mCity, mAddress, mMobil);
                }
            });
        }

        public void onBind() {
            Org item = mData.get(getPosition());
            mNameBank.setText(item.getName());
            mRegion.setText(item.getR());
            mCity.setText(item.getC());
            mMobil.setText(item.getM());
            mAddress.setText(item.getA());

        }
    }
}
