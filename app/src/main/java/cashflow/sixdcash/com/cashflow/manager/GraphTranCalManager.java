package cashflow.sixdcash.com.cashflow.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import cashflow.sixdcash.com.cashflow.Utility.DateUtility;
import cashflow.sixdcash.com.cashflow.Utility.RepetTansactionManager;
import cashflowdb.Category;
import cashflowdb.CategoryDao;
import cashflowdb.DaoSession;
import cashflowdb.FinancialYear;
import cashflowdb.InOutTransaction;
import cashflowdb.InOutTransactionDao;
import cashflowdb.RepeatTransaction;
import cashflowdb.RepeatTransactionDao;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by Praveen on 10/12/2016.
 */

public class GraphTranCalManager {

    static String totalIncome;
    static Double totalPounds;
static int i=0;
    public static List<String> getMonthlyIcome(Context context, String inORout) {
        Date temp_StartDate = null;
        Date temp_EndDate = null;

        List<String> monthluincomelist = new ArrayList<String>();
        temp_StartDate = getBugetYearStartDate(context);
        temp_EndDate = getBugetYearENDDate(context);

        //for (int z = 1; z <= 12; z++) {

        if (temp_StartDate != null && !temp_StartDate.equals("")){
            while (temp_StartDate.before(temp_EndDate)) {
                Log.i("366", "" + (i++));
                totalIncome = "";
                totalPounds = 0.0;
                String monIncome = getTotalByMonthly(context, temp_StartDate, inORout, "month");
                monthluincomelist.add(monIncome);
                //temp_StartDate = RepetTansactionManager.getNextDate(temp_StartDate, 1, "months");

                temp_StartDate = getStartDateOfMonth(temp_StartDate);
            }
    }
        return monthluincomelist;
    }



    public static List<String> getWeeklyIcome(Context context, String inORout,int intervals) {
        Date temp_StartDate = null;
        Date temp_EndDate = null;

       List<String> monthluincomelist = new ArrayList<String>();
       /*  DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date todayDate=new Date();
        temp_StartDate=DateUtility.getDateBYStringDate(format.format(todayDate));*/

        temp_StartDate = getBugetYearStartDate(context);
        Calendar cal = null;
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        // after interval
        try {
            Date date = format.parse(format.format(temp_StartDate));
            cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE,intervals);
            temp_EndDate = cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i("8159",""+temp_StartDate+" "+temp_EndDate);
        if (temp_StartDate != null && !temp_StartDate.equals("")) {
            while (temp_StartDate.before(temp_EndDate)) {
                Log.i("366", "" + (i++));
                totalIncome = "";
                totalPounds = 0.0;
                String monIncome = getTotalByMonthly(context, temp_StartDate, inORout, "week");
                monthluincomelist.add(monIncome);
                temp_StartDate = getNextDate(temp_StartDate);
                Log.i("tt 8159", "" + temp_StartDate);
            }
        }
        return monthluincomelist;
    }

    public static String getTotalByMonthly(Context context, Date calstartdate, String inORout,String weekormonth) {

        try {
            DaoSession daoSession = BaseManager.getDBSessoin(context);
            CategoryDao categoryDao = daoSession.getCategoryDao();
            List<Category> categories = categoryDao.loadAll();

            Category category = null;
            for (int x = 0; x < categories.size(); x++) {
                if (categories.get(x).getInorexp().equals(inORout)) {
                    category = categories.get(x);
                    if (category != null) {
                        calculateInOut(context, category.getId(), calstartdate,category,weekormonth);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("AJIT-getTotalByMonthly", calstartdate+", "+inORout+",  "+weekormonth+", "+ totalIncome);
        return totalIncome;
    }


    public static void calculateInOut(Context context, Long catID, Date calstartdate,Category category,String weekormonth) {

        List<InOutTransaction> transactions=category.getInOutTransactionList();
       // List<Transaction> transactions = getTrasaction(context, catID, calstartdate);

        if (transactions != null) {
            for (int y = 0; y < transactions.size(); y++) {

                InOutTransaction transaction = transactions.get(y);
                if (transaction.getIsrepetitive()) {

                    List<RepeatTransaction> repeatTransactions = getRepeatativeTrasaction
                            (context, transaction.getId(), calstartdate,weekormonth);

                    Log.i("7391",""+ DateUtility.getStringBYDate(calstartdate));
                    Log.i("re tran 7351",""+repeatTransactions.size());

                    for (int z = 0; z < repeatTransactions.size(); z++) {
                        RepeatTransaction repeatTransaction = repeatTransactions.get(z);
                        totalPounds = totalPounds + repeatTransaction.getPound();
                    }

                } else {
                    //Date temp_endDate = RepetTansactionManager.getNextDate(calstartdate, 1, "months");
                    Date temp_endDate=null;
                    if(weekormonth.equals("week")){
                        temp_endDate=getNextDate(calstartdate);

                    }
                    else  if(weekormonth.equals("month")){
                        temp_endDate= getLastDateOfMonth(calstartdate);
                    }


                    if(transaction.getTransactiondate().equals(calstartdate)
                            ||(transaction.getTransactiondate().after(calstartdate)
                    && transaction.getTransactiondate().before(temp_endDate) )){
                        totalPounds = totalPounds + transaction.getPound();
                    }
                }
            }

            totalIncome = Double.toString(totalPounds);
        }
    }

    public static List<InOutTransaction> getTrasaction(Context context, Long catID, Date calstartdate) {

        Date temp_endDate = RepetTansactionManager.getNextDate(calstartdate, 1, "months");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(temp_endDate);
        calendar.set(Calendar.DATE, -1);
        Date endDate = calendar.getTime();

        Log.i("end date 1050", "" + endDate.toString());

        DaoSession daoSession = BaseManager.getDBSessoin(context);
        InOutTransactionDao transactionDao = daoSession.getInOutTransactionDao();
        QueryBuilder qb = transactionDao.queryBuilder();
        WhereCondition whereCondition = qb.and(InOutTransactionDao.Properties.Categoryid.eq(catID),
                InOutTransactionDao.Properties.Transactiondate.between(calstartdate, endDate));
        qb.where(whereCondition);
        List<InOutTransaction> transactions = qb.list();
        return transactions;
    }

    public static List<RepeatTransaction> getRepeatativeTrasaction(Context context, Long transcationID, Date calstartdate,String weekormonth) {

       /* Date temp_endDate = RepetTansactionManager.getNextDate(calstartdate, 1, "months");
        Log.i("4567321",""+DateUtility.getStringBYDate(temp_endDate));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(temp_endDate);
        calendar.set(Calendar.DATE,-1);
        Date endDate = calendar.getTime();*/


        Date endDate=null;
        if(weekormonth.equals("week")){
            endDate=getNextDate(calstartdate);

        }
        else  if(weekormonth.equals("month")){
            endDate= getLastDateOfMonth(calstartdate);
        }


        Log.i("4567320",""+DateUtility.getStringBYDate(endDate));
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        RepeatTransactionDao repeatTransactionDao = daoSession.getRepeatTransactionDao();

        QueryBuilder qb = repeatTransactionDao.queryBuilder();
        WhereCondition whereCondition=null;

        if(weekormonth.equals("week")){
            endDate=getNextDate(calstartdate);

            whereCondition = qb.and(RepeatTransactionDao.Properties.ActualtrasactionID.eq(transcationID),
                    RepeatTransactionDao.Properties.Transactiondate.eq(calstartdate));
        }
        else  if(weekormonth.equals("month")){
            endDate= getLastDateOfMonth(calstartdate);
            whereCondition = qb.and(RepeatTransactionDao.Properties.ActualtrasactionID.eq(transcationID),
                    RepeatTransactionDao.Properties.Transactiondate.between(calstartdate, endDate));
        }

        qb.where(whereCondition);
        List<RepeatTransaction> transactions = qb.list();

        return transactions;

    }


    public static Date getBugetYearStartDate(Context context) {
        FinancialYear financialYear = null;
        Date startDate=null;
        List<FinancialYear> financialYears = FinancialYearManager.getFinancialYear(context);
        if (financialYears != null) {
            for (int a = 0; a < financialYears.size(); a++) {

                if (financialYears.get(a).getCompeted().equals("0")) {
                    financialYear = financialYears.get(a);
                    startDate = financialYear.getStartdate();
                    break;
                }
            }
        }

        return startDate;
    }

    public static Date getBugetYearENDDate(Context context) {
        FinancialYear financialYear = null;
        Date endDate=null;
        List<FinancialYear> financialYears = FinancialYearManager.getFinancialYear(context);
        if (financialYears != null) {
            for (int a = 0; a < financialYears.size(); a++) {

                if (financialYears.get(a).getCompeted().equals("0")) {
                    financialYear = financialYears.get(a);
                    endDate = financialYear.getEnddate();
                    break;
                }
            }
        }

        return endDate;
    }


    public static Date getLastDateOfMonth(Date currentDate) {
        Calendar calendar = null;/* new GregorianCalendar(year, month,
                Calendar.DAY_OF_MONTH);*/
        try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String temp_date=DateUtility.getStringBYDate(currentDate);
            Date date = format.parse(temp_date);

            calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i("end date 2067",""+DateUtility.getStringBYDate(calendar.getTime()));

        return calendar.getTime();
    }




    public static Date getLastDateOfYear(Date currentDate) {
        Calendar calendar = null;/* new GregorianCalendar(year, month,
                Calendar.DAY_OF_MONTH);*/
        try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String temp_date=DateUtility.getStringBYDate(currentDate);
            Date date = format.parse(temp_date);

            calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendar.set(Calendar.DAY_OF_YEAR,
                    calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.i("N: end date Year",""+DateUtility.getStringBYDate(calendar.getTime()));

        return calendar.getTime();
    }

    public static Date getStartDateOfMonth(Date currentDate) {
        Calendar calendar = null;/* new GregorianCalendar(year, month,
                Calendar.DAY_OF_MONTH);*/
        try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String temp_date=DateUtility.getStringBYDate(currentDate);
            Date date = format.parse(temp_date);

            calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH,
                    calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("start date 2067",""+DateUtility.getStringBYDate(calendar.getTime()));
        return calendar.getTime();
    }

    public static Date getNextDate(Date currentDate) {
        Calendar calendar = null;/* new GregorianCalendar(year, month,
                Calendar.DAY_OF_MONTH);*/
        try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String temp_date=DateUtility.getStringBYDate(currentDate);
            Date date = format.parse(temp_date);

            calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendar.add(Calendar.DATE, 1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("start date 2067",""+DateUtility.getStringBYDate(calendar.getTime()));
        return calendar.getTime();
    }


    public static  LinkedHashMap<String, Double> getCategoryList(Context mContext, String expend) {
        LinkedHashMap<String, Double> catExpHashMap=new LinkedHashMap<>();
        try {
            DaoSession daoSession = BaseManager.getDBSessoin(mContext);
            SQLiteDatabase db = daoSession.getDatabase();
            Cursor cursor = db.rawQuery("SELECT c.CATNAME as catname, sum(rt.pound) as expense FROM CATEGORY c INNER JOIN" +
                    " REPEAT_TRANSACTION " +"rt ON c._id=rt.CAT_ID  WHERE c.INOREXP ='expend'  GROUP BY c.CATNAME;", null);
            /*Cursor cursor = db.rawQuery("SELECT c.CATNAME as catname, sum(iot.pound) as expense" +
                    "     FROM CATEGORY c LEFT OUTER JOIN REPEAT_TRANSACTION rt ON c._id=rt.CAT_ID" +
                    "     INNER JOIN IN_OUT_TRANSACTION iot ON iot.CATEGORYID= c._id" +
                    "     WHERE c.INOREXP !='income' AND c.INOREXP !='notInOut'" +
                    "    GROUP BY c.CATNAME;", null);*/
/*
 Cursor cursor = db.rawQuery("SELECT c.CATNAME as catname, sum(pound) as expense" +
                    "     FROM CATEGORY c LEFT OUTER JOIN REPEAT_TRANSACTION rt ON c._id=rt.CAT_ID" +
                    "     WHERE c.INOREXP !='income' AND c.INOREXP !='notInOut'" +"    GROUP BY c.CATNAME", null);
*/

            if (cursor != null && cursor.moveToFirst()) {

                do {
                    try {
                        String cateValue=cursor.getString(cursor.getColumnIndex("catname"));
                        double poundsValue=cursor.getDouble(cursor.getColumnIndex("expense"));
                        Log.d("nehal: expense", cateValue+" "+poundsValue);

                        catExpHashMap.put(cateValue,poundsValue);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());

                cursor.close();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return catExpHashMap;
    }


    public static  double getTotal(Context mContext) {
        double total=0;
        try {
            DaoSession daoSession = BaseManager.getDBSessoin(mContext);
            SQLiteDatabase db = daoSession.getDatabase();
            Cursor cursor = db.rawQuery("SELECT sum(iot.pound) as total" +
                    "     FROM CATEGORY c LEFT OUTER JOIN REPEAT_TRANSACTION rt ON c._id=rt.CAT_ID" +
                    "     INNER JOIN IN_OUT_TRANSACTION iot ON iot.CATEGORYID= c._id" +
                    "     WHERE c.INOREXP !='income' AND c.INOREXP !='notInOut';", null);
/*
 Cursor cursor = db.rawQuery("SELECT c.CATNAME as catname, sum(pound) as expense" +
                    "     FROM CATEGORY c LEFT OUTER JOIN REPEAT_TRANSACTION rt ON c._id=rt.CAT_ID" +
                    "     WHERE c.INOREXP !='income' AND c.INOREXP !='notInOut'" +"    GROUP BY c.CATNAME", null);
*/

            if (cursor != null && cursor.moveToFirst()) {

                do {
                    try {
                        total=cursor.getDouble(cursor.getColumnIndex("total"));
                        Log.d("total: ", total+"");


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());

                cursor.close();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return total;
    }

}
