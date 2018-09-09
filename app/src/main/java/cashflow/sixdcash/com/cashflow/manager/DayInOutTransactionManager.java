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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import cashflow.sixdcash.com.cashflow.Utility.DateUtility;
import cashflow.sixdcash.com.cashflow.Utility.pojo.DayInOutPOJO;
import cashflowdb.DaoSession;
import cashflowdb.DayInOutSummery;
import cashflowdb.DayInOutSummeryDao;
import cashflowdb.FinancialYear;
import cashflowdb.InitialBudget;

/**
 * Created by opj1 on 20/01/2017.
 */
public class DayInOutTransactionManager {

    Context context;
    private Date temp_StartDate;
    private Date temp_EndDate;

    public DayInOutTransactionManager(Context context) {
        this.context = context;
    }

    public LinkedHashMap<String,DayInOutPOJO> cumulativeDataForDay(){
        LinkedHashMap<String,DayInOutPOJO> dateDayInOutPOJOHashMap=new LinkedHashMap<>();
        try{
            DaoSession daoSession = BaseManager.getDBSessoin(this.context);
            SQLiteDatabase db = daoSession.getDatabase();
            String DAILY_REPETITIVE_DATA="SELECT date(rt.transactiondate/1000, 'unixepoch', 'localtime') as trans_date, c.inorexp as inorexp,\n" +
                    "    sum(CASE \"inorexp\" WHEN \"expend\" THEN pound WHEN \"income\" THEN 0 END) as expense,\n" +
                    "    sum(CASE \"inorexp\" WHEN \"expend\" THEN 0 WHEN \"income\" THEN pound END) as income\n" +
                    "     FROM REPEAT_TRANSACTION rt INNER JOIN\n" +
                    "    CATEGORY c ON c._id=rt.CAT_ID\n" +
                    "    GROUP BY date(rt.transactiondate/1000, 'unixepoch', 'localtime')";
            Cursor cursor = db.rawQuery(DAILY_REPETITIVE_DATA, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    try {
                        DayInOutPOJO dayInOutPOJO=new DayInOutPOJO();
                        DateFormat formatter =  new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        String temp_date= cursor.getString(cursor.getColumnIndex("trans_date")); //DateUtility.getStringBYDate(new Date(cursor.getLong(cursor.getColumnIndex("trans_date"))));
                        Date keyDate = formatter.parse(temp_date);



                        dayInOutPOJO.setDate(keyDate);
                        dayInOutPOJO.setIncome(cursor.getDouble(cursor.getColumnIndex("income")));
                        dayInOutPOJO.setExpense(cursor.getDouble(cursor.getColumnIndex("expense")));
                        dayInOutPOJO.setInorex(cursor.getString(cursor.getColumnIndex("inorexp")));

                        Log.d("nehal: date: ",keyDate+" "+dayInOutPOJO.toString());

                        DateFormat format =  new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                        String calKeyDate= format.format(keyDate);
                        Log.d("Key: ",cursor.getDouble(cursor.getColumnIndex("income"))+"");
                        dateDayInOutPOJOHashMap.put(calKeyDate,dayInOutPOJO);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());

                cursor.close();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return dateDayInOutPOJOHashMap;
    }

    public double getInitialMoney() {
        Double initial_money = 0.0;
        List<InitialBudget> initialBudgets = InitialBudgetManager.getInitialBudget(this.context);
        if (initialBudgets != null && initialBudgets.size() > 0) {
            InitialBudget startMoneyBuget = initialBudgets.get(0);
            initial_money = startMoneyBuget.getPound();
        }
        return initial_money;
    }


    public void insertDayInOut(){
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        DayInOutSummeryDao summeryDao=daoSession.getDayInOutSummeryDao();
        summeryDao.deleteAll();
        double balInHand=getInitialMoney();
        Log.d("nehal: balInHand",balInHand+"");
        temp_StartDate = getBugetYearStartDate(context);
        Calendar cal = null;
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        // after interval
        try {
            if(null!=temp_StartDate  && !temp_StartDate.equals("")) {
                Date date = format.parse(format.format(temp_StartDate));
                cal = Calendar.getInstance();
                cal.setTime(date);
                if (cal.get(Calendar.YEAR)%4==0){
                    Log.d("calDate:", "Leap year");
                    cal.add(Calendar.DATE, 366);
                }else{
                    cal.add(Calendar.DATE, 365);
                    Log.d("calDate:", "Not Leap year");
                }
                temp_EndDate = cal.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LinkedHashMap<String,DayInOutPOJO> dateDayInOutPOJOHashMap=this.cumulativeDataForDay();


        Log.i("8159",""+temp_StartDate+" "+temp_EndDate);
        if (temp_StartDate != null && !temp_StartDate.equals("")) {
            ArrayList <DayInOutSummery> newsArticles = new ArrayList<DayInOutSummery>();
            while (temp_StartDate.before(temp_EndDate)) {
                DayInOutSummery dayInOutSummery =new DayInOutSummery();
                DateFormat frmt =  new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                String calKeyDate= frmt.format(temp_StartDate);
                if(null!=dateDayInOutPOJOHashMap.get(calKeyDate)){
                    DayInOutPOJO dayInOutPOJO=dateDayInOutPOJOHashMap.get(calKeyDate);
                    dayInOutSummery.setDate(temp_StartDate);
                    dayInOutSummery.setIncome_pound(dayInOutPOJO.getIncome());
                    dayInOutSummery.setExpense_pound(dayInOutPOJO.getExpense());

                    balInHand=((balInHand+dayInOutSummery.getIncome_pound())-dayInOutSummery.getExpense_pound());
                    dayInOutSummery.setBalance_in_hand(balInHand);
                    //Log.d("nehal: DB : balInHand: ","date: "+dayInOutSummery.getDate()+" in:"+dayInOutSummery.getIncome_pound()
                           // +" exp: "+ dayInOutSummery.getExpense_pound()+ " balInHand: "+dayInOutSummery.getBalance_in_hand());
                    //summeryDao.insertOrReplace(dayInOutSummery);
                    newsArticles.add(dayInOutSummery);
                }else {
                    dayInOutSummery.setDate(temp_StartDate);
                    dayInOutSummery.setIncome_pound(0.0);
                    dayInOutSummery.setExpense_pound(0.0);
                    balInHand=((balInHand+dayInOutSummery.getIncome_pound())-dayInOutSummery.getExpense_pound());
                    dayInOutSummery.setBalance_in_hand(balInHand);
                    //Log.d("nehal: DB : balInHand: ","date: "+dayInOutSummery.getDate()+"balInHand: "+dayInOutSummery.getBalance_in_hand());
                    //summeryDao.insertOrReplace(dayInOutSummery);
                    newsArticles.add(dayInOutSummery);
                }
                temp_StartDate = getNextDate(temp_StartDate);
            }
            summeryDao.insertInTx(newsArticles);
        }

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
    public static Date getNextDate(Date currentDate) {
        Calendar calendar = null;/* new GregorianCalendar(year, month,
                Calendar.DAY_OF_MONTH);*/
        try {
            DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String temp_date= DateUtility.getStringBYDate(currentDate);
            Date date = format.parse(temp_date);

            calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendar.add(Calendar.DATE, 1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
//        Log.i("start date 2067",""+DateUtility.getStringBYDate(calendar.getTime()));
        return calendar.getTime();
    }


    public List<DayInOutSummery> getDayInOutSummery() {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        DayInOutSummeryDao summeryDao=daoSession.getDayInOutSummeryDao();
        List<DayInOutSummery> dayInOutSummeries=summeryDao.loadAll();
        return  dayInOutSummeries;
    }

    public DayInOutSummery getWorstDayInOut() {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        DayInOutSummeryDao summeryDao=daoSession.getDayInOutSummeryDao();
        List<DayInOutSummery> dayInOutSummeries=summeryDao.queryBuilder().orderAsc(DayInOutSummeryDao.Properties.Balance_in_hand).orderAsc(DayInOutSummeryDao.Properties.Date).list();
        if (null!=dayInOutSummeries && dayInOutSummeries.size()>0){
            Log.d("nehal: lowest day",dayInOutSummeries.get(0).getDate()+" "+dayInOutSummeries.get(0).getBalance_in_hand() );
        }
        if (null!=dayInOutSummeries && dayInOutSummeries.size()>0)
        return  dayInOutSummeries.get(0);
        else
            return null;
    }

    public DayInOutSummery getYearEdnMoney() {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        DayInOutSummeryDao summeryDao=daoSession.getDayInOutSummeryDao();
        List<DayInOutSummery> dayInOutSummeries=summeryDao.queryBuilder().orderDesc(DayInOutSummeryDao.Properties.Date).list();
        if (null!=dayInOutSummeries && dayInOutSummeries.size()>0){
            Log.d("nehal: year end",dayInOutSummeries.get(0).getDate()+" "+dayInOutSummeries.get(0).getBalance_in_hand() );
        }
        if (null!=dayInOutSummeries && dayInOutSummeries.size()>0)
            return  dayInOutSummeries.get(0);
        else
            return null;
    }
    public DayInOutSummery getFirstDebt() {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        DayInOutSummeryDao summeryDao=daoSession.getDayInOutSummeryDao();
        List<DayInOutSummery> dayInOutSummeries=summeryDao.queryBuilder().where(DayInOutSummeryDao.Properties.Balance_in_hand.lt(0.0)).orderAsc(DayInOutSummeryDao.Properties.Date).limit(1).list();
        if (null!=dayInOutSummeries && dayInOutSummeries.size()>0){
            Log.d("nehal: lowest day",dayInOutSummeries.get(0).getDate()+" "+dayInOutSummeries.get(0).getBalance_in_hand() );
        }
        if (null!=dayInOutSummeries && dayInOutSummeries.size()>0)
            return  dayInOutSummeries.get(0);
        else
            return null;
    }
}
