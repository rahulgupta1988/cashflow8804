package cashflow.sixdcash.com.cashflow.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Praveen on 9/26/2016.
 */
public class DateUtility {

    public static Date getTodayDate(){
        Date date=null;
        String todaydate  = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(new Date());
        try {
            date  =new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(todaydate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


   public static Date getDateBYStringDate(String todayDate){
       Date date=null;
       SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
       try {
            date=simpleDateFormat.parse(todayDate);
       } catch (ParseException e) {
           e.printStackTrace();
       }

       return date;
   }


    public static String getStringBYDate(Date todayDate){
        String date=null;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            date=simpleDateFormat.format(todayDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }
}
