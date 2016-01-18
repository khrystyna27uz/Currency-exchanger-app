package com.example.myprog;

/**
 * Created by Христинка on 22.12.2015.
 */
import com.example.myprog.list.Cur;

import java.util.List;


public class Currencies {
    private List<Cur> currencyModels;

    public List<Cur> getCurrencyModels() {
        return currencyModels;
    }

    public void setCurrencyModels(List<Cur> currencyModels) {
        this.currencyModels = currencyModels;
    }
}
