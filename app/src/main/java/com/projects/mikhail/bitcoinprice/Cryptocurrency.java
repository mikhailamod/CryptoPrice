package com.projects.mikhail.bitcoinprice;

/**
 * Created by Mikhail on 2017/10/10.
 */

import android.view.View;

public class Cryptocurrency
{
    //Attributes
    String name;
    String id;
    String symbol;
    float currentPrice;
    int logo_id;

    public Cryptocurrency(String name, String id, String symbol, float currentPrice) {
        this.name = name;
        this.id = id;
        this.symbol = symbol;
        this.currentPrice = currentPrice;
    }

    public Cryptocurrency(String name, String id, String symbol) {
        this.name = name;
        this.id = id;
        this.symbol = symbol;
        this.currentPrice = 0;
    }

    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setCurrentPrice(float currentPrice) {
        this.currentPrice = currentPrice;
    }

    //Getters
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public float getCurrentPrice() {
        return currentPrice;
    }
}
