package cashflow.sixdcash.com.cashflow.Utility;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

import cashflow.sixdcash.com.cashflow.HostActivty;

/**
 * Created by opj1 on 13/01/2017.
 */
public class CustomBarDataSet extends BarDataSet {


    public CustomBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {

        if (getEntryForXIndex(index).getVal() < 0) { // less than 0 RED
            return /*HostActivty.EXPEND_COLORS[0]*/ /*Color.rgb(255, 0, 0)*/ mColors.get(0);

        } else if(getEntryForXIndex(index).getVal() >0) // greater than 0 Green
        {

            return /*HostActivty.INCOME_COLORS[0]*/ /*Color.rgb(76, 153, 0)*/ mColors.get(1);
        }
        else // = 0
            return mColors.get(0);
    }

}