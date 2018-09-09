package cashflow.sixdcash.com.cashflow.Utility;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

/**
 * Created by Praveen on 9/15/2016.
 */
public class ChartValueFormatter implements YAxisValueFormatter {


    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        return convetUnicode("u00A3") + Math.round(value);
    }

    private String convetUnicode(String myString) {
        String str = myString.split(" ")[0];
        str = str.replace("\\", "");
        String[] arr = str.split("u");
        String text = "";
        for (int i = 1; i < arr.length; i++) {
            int hexVal = Integer.parseInt(arr[i], 16);
            text += (char) hexVal;
        }

        return text;
    }
}
