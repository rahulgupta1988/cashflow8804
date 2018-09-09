package cashflow.sixdcash.com.cashflow.manager;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
 * Created by Praveen on 10/7/2016.
 */

public class BugetManager {

    static  String totalIncome;
    static Double  totalPounds;


    public static String getTotalOFIncome(Context context,int month,String inORout){
        totalIncome="";
        totalPounds=0.0;

        try {
            DaoSession daoSession=BaseManager.getDBSessoin(context);
            CategoryDao categoryDao=daoSession.getCategoryDao();
            List<Category> categories=categoryDao.loadAll();


            Category category=null;
            for(int x=0;x<categories.size();x++){
                if(categories.get(x).getInorexp().equals(inORout)){
                    category=categories.get(x);
                    if(category!=null){
                        calculateInOut(context,category.getId(),month);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalIncome;
    }



    public static void calculateInOut(Context context,Long catID,int month){
        List<InOutTransaction> transactions = getTrasaction(context,catID,month);

        if(transactions!=null){

            for(int y=0;y<transactions.size();y++){

                InOutTransaction transaction=transactions.get(y);
                if(transaction.getIsrepetitive()){

                    List<RepeatTransaction> repeatTransactions=getRepeatativeTrasaction
                            (context, transaction.getId(),month);

                    for(int z=0;z<repeatTransactions.size();z++){

                        RepeatTransaction repeatTransaction=repeatTransactions.get(z);
                        Log.i("tran date 9171",""+ DateUtility.getStringBYDate(repeatTransaction.getTransactiondate()));

                        totalPounds=totalPounds+repeatTransaction.getPound();
                        Log.i("07653",""+totalPounds);
                    }

                }

                else{
                    totalPounds=totalPounds+transaction.getPound();
                    Log.i("tran date 9171",""+ DateUtility.getStringBYDate(transaction.getTransactiondate()));
                    Log.i("07653",""+totalPounds);
                }
            }


/*
            totalPounds =totalPounds+(totalPence/100);
            totalPence=totalPence%100;*/

            totalIncome=Double.toString(totalPounds);
        }


    }


    public static List<InOutTransaction> getTrasaction(Context context,Long catID,int month){

        FinancialYear financialYear=null;
        List<FinancialYear> financialYears=FinancialYearManager.getFinancialYear(context);
        if(financialYears!=null) {
            for (int a = 0; a < financialYears.size(); a++) {

                if (financialYears.get(a).getCompeted().equals("0")) {
                    financialYear = financialYears.get(a);
                    break;
                }
            }
        }




        Date startDate=financialYear.getStartdate();
        Date endDate=null;


        if(month==3){
            endDate= RepetTansactionManager.getNextDate(startDate,3,"months");

        }
        if(month==6){
            endDate= RepetTansactionManager.getNextDate(startDate,6,"months");
        }
        if(month==9){
            endDate= RepetTansactionManager.getNextDate(startDate,9,"months");
        }
        if(month==12){
            endDate= financialYear.getEnddate();
        }

        DaoSession daoSession=BaseManager.getDBSessoin(context);
        InOutTransactionDao transactionDao=daoSession.getInOutTransactionDao();

        QueryBuilder qb = transactionDao.queryBuilder();
      WhereCondition whereCondition= qb.and(InOutTransactionDao.Properties.Categoryid.eq(catID),InOutTransactionDao.Properties.Transactiondate.le(endDate));

        qb.where(whereCondition);
        //qb.where(TransactionDao.Properties.Transactiondate.le(endDate));

        List<InOutTransaction> transactions=qb.list();

        return transactions;
    }


    public static List<RepeatTransaction> getRepeatativeTrasaction(Context context,Long transcationID,int month){

        FinancialYear financialYear=null;
        List<FinancialYear> financialYears=FinancialYearManager.getFinancialYear(context);
        if(financialYears!=null)
            for(int a=0;a<financialYears.size();a++){

                if(financialYears.get(a).getCompeted().equals("0")){
                    financialYear=financialYears.get(a);
                    break;
                }
            }



        Date startDate=financialYear.getStartdate();
        Date endDate=null;

        Log.i("fin year 1056",""+financialYear);
        Log.i("startDate 1056",""+startDate);


        if(month==3){
            endDate= RepetTansactionManager.getNextDate(startDate,3,"months");
            Log.i("endDate 1056",""+endDate);
        }
        if(month==6){
            endDate= RepetTansactionManager.getNextDate(startDate,6,"months");
        }
        if(month==9){
            endDate= RepetTansactionManager.getNextDate(startDate,9,"months");
        }
        if(month==12){
            endDate= financialYear.getEnddate();
        }

        DaoSession daoSession=BaseManager.getDBSessoin(context);
        RepeatTransactionDao repeatTransactionDao=daoSession.getRepeatTransactionDao();

        QueryBuilder qb = repeatTransactionDao.queryBuilder();
        WhereCondition whereCondition=  qb.and(RepeatTransactionDao.Properties.ActualtrasactionID.eq(transcationID),RepeatTransactionDao.Properties.Transactiondate.le(endDate));
        qb.where(whereCondition);




        List<RepeatTransaction> repeatTransactions=qb.list();

        return repeatTransactions;
    }



    public static List<String> testfun(Context context,String inORout){

        List<String> amout=new ArrayList<String>();


         String whereCause="";
        if(inORout.equals("income")){
            whereCause=" where "+  RepeatTransactionDao.Properties.CatID.columnName+"=2";
        }

        else{
            whereCause=" where "+  RepeatTransactionDao.Properties.CatID.columnName+"!=2";
        }

        String qry="SELECT strftime('%Y', datetime("+RepeatTransactionDao.Properties.Transactiondate.columnName+"/1000, 'unixepoch', 'localtime')) as YEARS, "
                +"SUM("+RepeatTransactionDao.Properties.Pound.columnName+ ")  AS AMOUNT FROM "
        +  RepeatTransactionDao.TABLENAME
                +whereCause
        + " GROUP BY strftime('%Y', datetime("+RepeatTransactionDao.Properties.Transactiondate.columnName
                +"/1000, 'unixepoch', 'localtime')),"
        + "strftime('%m', datetime("+RepeatTransactionDao.Properties.Transactiondate.columnName+"/1000, 'unixepoch', 'localtime')) ORDER BY CAST(YEARS as INTEGER)";
        Log.i("testts 0091112",""+qry);

        int testts=0;
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        Cursor c = daoSession.getDatabase().rawQuery(qry, null);

        try {
            if (c.moveToFirst()) {
                do {
                     testts = c.getColumnIndex("AMOUNT");
                    amout.add(c.getString(testts));
                    Log.i("values 0091112",""+c.getString(testts));
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }


        Log.i("testts 0091112",""+testts);


        return amout;
    }

}
