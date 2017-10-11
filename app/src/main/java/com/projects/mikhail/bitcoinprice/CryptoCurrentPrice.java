package com.projects.mikhail.bitcoinprice;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mikhail on 2017/10/08.
 */

public class CryptoCurrentPrice
{
    //Attributes
    private String id;
    private String name;
    private String symbol;

    private float price;
    private float priceZAR;

    private JSONObject jsonObject;
    private JSONArray jsonArray;

    public CryptoCurrentPrice(String jsonInput)
    {
        try{
            jsonArray = new JSONArray(jsonInput);
            jsonObject = jsonArray.getJSONObject(0);
            id = jsonObject.getString("id");
            name = jsonObject.getString("name");
            symbol = jsonObject.getString("symbol");
            price = Float.parseFloat(jsonObject.getString("price_usd"));
            priceZAR = Float.parseFloat(jsonObject.getString("price_zar"));
        }
        catch(JSONException e)
        {
            System.out.println(e.getStackTrace());
        }//end catch
    }//end constructor

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public float getPrice() {
        return price;
    }

    public float getPriceZAR() {
        return priceZAR;
    }

}
