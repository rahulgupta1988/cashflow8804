package cashflow.sixdcash.com.cashflow.Utility;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cashflow.sixdcash.com.cashflow.manager.BaseManager;
import cashflowdb.DaoSession;
import cashflowdb.FinancialYear;
import cashflowdb.FinancialYearDao;
import cashflowdb.RepeatTransaction;
import cashflowdb.RepeatTransactionDao;

/**
 * Created by Praveen on 10/3/2016.
 */
public class RepetTansactionManager {

    static Date  startDateofYear=null;
    static Date endDateofYear=null;
    public static Date startDate=null;

    public static Date transtartDate=null;

    public static void repeatTrasaction(Context context,Double pound, Long actualTRID, Date nextTransactioDate,
                                    int intervals, int count, String weekORMonth,Long catID,String comments) {

        try {
            transtartDate=nextTransactioDate;
            DaoSession daoSession = BaseManager.getDBSessoin(context);
            RepeatTransactionDao repeatTransactionDao=daoSession.getRepeatTransactionDao();
            FinancialYearDao financialYearDao=daoSession.getFinancialYearDao();
            List<FinancialYear> financialYears=financialYearDao.loadAll();

            if(financialYears!=null){

                for(int i=0;i<financialYears.size();i++){
                    FinancialYear financialYear=financialYears.get(i);

                    if(financialYear!=null && financialYear.getCompeted().equals("0")){
                         startDateofYear=financialYear.getStartdate();
                         endDateofYear=financialYear.getEnddate();
                    }
                }

                DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                ArrayList<RepeatTransaction> repeatTransactionList = new ArrayList<RepeatTransaction>();

               // for (int w = 1; w <=count; w++) {
                int w=1;
                startDate=nextTransactioDate;
                if(count==10){

                    int x=1;
                    while (startDate.before(endDateofYear) && x<=12){
                        x++;
                        //Log.i("count re 01910",""+w);

                        if(w==1){
                            //startDate=DateUtility.getTodayDate();
                            startDate=nextTransactioDate;
                            // Log.i("currentDate 009911", "" + startDate);
                        }

                  /*  if(w==2){
                        startDate=nextTransactioDate;
                    }*/

                        if(w>=2){
                            getNextDate(startDate, intervals, weekORMonth);
                        }


                        Date date1= null;
                        Date date2= null;
                        try {
                      /*  date1 = format.parse(startDate);
                        date2 = format.parse(endDateofYear);*/
                            date1 = startDate;
                            date2 = endDateofYear;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.i("s1033",""+DateUtility.getStringBYDate(date1));
                        Log.i("e1033",""+DateUtility.getStringBYDate(date2));

                        if(!date1.after(date2)){
                            RepeatTransaction repeatTransaction=new RepeatTransaction();
                            repeatTransaction.setPound(pound);
                            repeatTransaction.setTransactiondate(startDate);
                            repeatTransaction.setRecomments(comments);
                            repeatTransaction.setActualtrasactionID(actualTRID);
                            repeatTransaction.setCatID(catID);
                            if(w==1)
                                repeatTransaction.setConfirm(true);
                            else
                                repeatTransaction.setConfirm(false);


                            int temp_month=getMonth(startDate);
                          if(temp_month!=1 && temp_month!=2){
//                              repeatTransactionDao.insertOrReplace(repeatTransaction);
                              repeatTransactionList.add(repeatTransaction);

                          }

                            w++;

                        }
                        else{
                            Log.i("year end134325","year end");
                            break;
                        }
                    }
                }

                else {
                    while (startDate.before(endDateofYear)) {
                        //Log.i("count re 01910",""+w);

                        if (w == 1) {
                            //startDate=DateUtility.getTodayDate();
                            startDate = nextTransactioDate;
                            // Log.i("currentDate 009911", "" + startDate);
                        }

                  /*  if(w==2){
                        startDate=nextTransactioDate;
                    }*/

                        if (w >= 2) {
                            getNextDate(startDate, intervals, weekORMonth);
                        }


                        Date date1 = null;
                        Date date2 = null;
                        try {
                      /*  date1 = format.parse(startDate);
                        date2 = format.parse(endDateofYear);*/
                            date1 = startDate;
                            date2 = endDateofYear;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Log.i("s1033", "" + DateUtility.getStringBYDate(date1));
                        Log.i("e1033", "" + DateUtility.getStringBYDate(date2));

                        if (!date1.after(date2)) {

                            RepeatTransaction repeatTransaction = new RepeatTransaction();
                            repeatTransaction.setPound(pound);
                            repeatTransaction.setTransactiondate(startDate);
                            repeatTransaction.setRecomments(comments);
                            repeatTransaction.setActualtrasactionID(actualTRID);
                            repeatTransaction.setCatID(catID);
                            if (w == 1)
                                repeatTransaction.setConfirm(true);
                            else
                                repeatTransaction.setConfirm(false);

//                            repeatTransactionDao.insertOrReplace(repeatTransaction);
                            repeatTransactionList.add(repeatTransaction);
                            w++;

                        } else {
                            Log.i("year end134325", "year end");
                            break;
                        }
                    }
                }
                if(null!=repeatTransactionList && repeatTransactionList.size()>0) {
                    repeatTransactionDao.insertInTx(repeatTransactionList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // calendar
    public static Date getNextDate(Date currentDate, int intervals, String weekORMonth) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Calendar cal = null;
        try {

            String temp_date=DateUtility.getStringBYDate(currentDate);

            Date date = format.parse(temp_date);
            cal = Calendar.getInstance();
            cal.setTime(date);

            if (weekORMonth.equals("weeks"))
                cal.add(Calendar.DATE, intervals);
            else if (weekORMonth.equals("months"))
                //cal.add(Calendar.DATE, intervals);
                cal.add(Calendar.MONTH, intervals);


        } catch (Exception e) {
            e.printStackTrace();
        }

        int temp_start_date=getStartingDate();
        if(((cal.get(Calendar.MONTH))==2) &&(temp_start_date==29 ||temp_start_date==30) && weekORMonth.equals("months")){
            cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),temp_start_date);
            startDate = cal.getTime();
        }
        else if(temp_start_date==31 && (cal.getActualMaximum(Calendar.DAY_OF_MONTH))==31 && weekORMonth.equals("months")){
            cal.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),temp_start_date);
            startDate = cal.getTime();
        }

       else{
            startDate = cal.getTime();
        }

        return  startDate;
    }



    public static int getStartingDate(){
        int startDate=0;
        Calendar cal = null;
        try {
            cal = Calendar.getInstance();
            cal.setTime(transtartDate);
            startDate=cal.get(Calendar.DATE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return startDate;
    }


    public static int getMonth(Date date){
        int month=0;
        Calendar cal = null;
        try {
            cal = Calendar.getInstance();
            cal.setTime(date);
            month=cal.get(Calendar.MONTH);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return month;
    }


}
