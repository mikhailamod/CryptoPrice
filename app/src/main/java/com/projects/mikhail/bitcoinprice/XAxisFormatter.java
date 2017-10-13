package com.projects.mikhail.bitcoinprice;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Mikhail on 2017/10/13.
 * Simple class for representing an array of strings as an axis
 * Taken from example from MPAndroidChart wiki
 */

public class XAxisFormatter implements IAxisValueFormatter {

    //Attributes
    private String[] mValues;

    public XAxisFormatter(String[] values)
    {
        System.out.println("DEBUG------------------ " + values.length);
        mValues = new String[values.length];
        for(int i = 0;i<values.length;i++)
        {
            mValues[i] = values[i];
        }
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mValues[(int)value];
    }

    public int getDecimalDigits(){ return 0;}
}
