package com.projects.mikhail.bitcoinprice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mikhail on 2017/10/09.
 */

public class CryptoPriceHistory
{


    //Attributes
    private Cryptocurrency cryptocurrency;
    private float[] sixtyDayPrice;
    private long[] timestamps;


    private JSONObject jsonObject;

    public CryptoPriceHistory(String input, Cryptocurrency cryptocurrency, int length)
    {
        this.cryptocurrency = cryptocurrency;

        sixtyDayPrice = new float[60];

        timestamps = new long[length];
        try{
            jsonObject = new JSONObject(input);
            System.out.println(jsonObject.toString());
            JSONArray data = jsonObject.getJSONArray("Data");
            for(int i=0;i<length;i++)
            {
                JSONObject dayData = data.getJSONObject(i);
                float price = (float)dayData.getDouble("close");
                long timestamp = dayData.getLong("time");
                sixtyDayPrice[i] = price;
                timestamps[i] = timestamp;
            }
        }
        catch(JSONException e)
        {
            System.out.println(e.getStackTrace());
        }//end catch
    }//end constructor

    public float[] getPrices(int length)
    {
        if(length==sixtyDayPrice.length)
        {
            return sixtyDayPrice;
        }
        else
        {
            float[] output = new float[length];
            for (int i = 0; i < length; i++) {
                output[length-i-1] = sixtyDayPrice[sixtyDayPrice.length-i-1];
            }
            return output;
        }
    }

    public float[] getSixtyDayPrice() {
        return sixtyDayPrice;
    }

    public long[] getTimestamps() {
        return getTimestamps(60);
    }
    public long[] getTimestamps(int length)
    {
        if(length==timestamps.length)
        {
            return timestamps;
        }
        else {//return timestamps from current day to number of days required
            long[] output = new long[length];
            for (int i = 0;i<length;i++) {
                output[length-i-1] = timestamps[timestamps.length-i-1];
            }
            return output;
        }//end else
    }

    public Cryptocurrency getCryptocurrency(){ return cryptocurrency;}
}
