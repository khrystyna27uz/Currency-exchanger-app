package com.example.myprog.list;

import android.widget.ImageView;
import android.widget.TextView;

public class Cur {
    String nameVal;
    String askVal;
    String bidVal;
    int iconNearAsk;
    int iconNearBid;

    public Cur(String nameVal, String askVal, String bidVal, int iconNearAsk, int iconNearBid) {
        this.nameVal = nameVal;
        this.askVal = askVal;
        this.bidVal = bidVal;
        this.iconNearAsk = iconNearAsk;
        this.iconNearBid = iconNearBid;

    }

    public String getNameVal() {
        return nameVal;
    }

    public void setNameVal(String _nameVal) {
        nameVal = _nameVal;
    }

    public String getAsk() {
        return askVal;
    }

    public void setAsk(String _askVal) {
        askVal = _askVal;
    }

    public String getBid() {
        return bidVal;
    }

    public void setBid(String _bidVal) {
        bidVal = _bidVal;
    }


    public int geticonNearAsk() {
        return iconNearAsk;
    }

    public void seticonNearAsk(int _iconNearAsk) {
        iconNearAsk = _iconNearAsk;
    }

    public int geticonNearBid() {
        return iconNearBid;
    }

    public void seticonNearBid(int _iconNearBid) {
        iconNearBid = _iconNearBid;
    }


}