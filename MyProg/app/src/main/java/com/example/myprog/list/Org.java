package com.example.myprog.list;

import com.example.myprog.Currencies;


public class Org {
    String nameBank;
    String region;
    String city;
    private Currencies currencies;
    String mobil;
    String adress;

    public Org(String nameBank, String region, String city, String mobil, String adress) {
        this.nameBank = nameBank;
        this.region = region;
        this.city = city;
        this.mobil = mobil;
        this.adress = adress;
    }

    public String getName() {
        return nameBank;
    }

    public void setName(String _titleName) {
        nameBank = _titleName;
    }

    public String getR() {
        return region;
    }

    public Currencies getCurrencies() {
        return currencies;
    }

    public void setR(String _region) {
        region = _region;
    }

    public String getC() {
        return city;
    }

    public void setC(String _city) {
        city = _city;
    }

    public String getM() {
        return mobil;
    }

    public void setM(String _mobil) {
        mobil = _mobil;
    }

    public String getA() {
        return adress;
    }

    public void setA(String _adress) {
        adress = _adress;
    }
}
