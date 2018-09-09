package cashflow.sixdcash.com.cashflow.manager;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cashflow.sixdcash.com.cashflow.Utility.DateUtility;
import cashflowdb.DaoSession;
import cashflowdb.InitialBudget;
import cashflowdb.InitialBudgetDao;

/**
 * Created by Praveen on 9/30/2016.
 */
public class InitialBudgetManager {

    public static DaoSession getSession(Context context){
        return BaseManager.getDBSessoin(context);
    }




    public static void insertInitailMoney(Context context,String moneytype,String pound,String startdate,String comment){

        String bugetID="0";

        try {
            InitialBudgetDao initialBudgetDao=getSession(context).getInitialBudgetDao();

            Double pound_dub = Double.parseDouble(pound);
            Log.d("N:"+ InitialBudgetManager.class.getSimpleName(),pound_dub!=null?pound_dub+"":"null");

            if(moneytype.equals("initailmoney")) {
                bugetID="1";
            }

            else if(moneytype.equals("goalmoney")) {
                bugetID="2";
            }
            else if(moneytype.equals("owemoney")) {
                bugetID="3";
            }


            List<InitialBudget> initialBudgets  = initialBudgetDao.queryBuilder().where(InitialBudgetDao.Properties.BugetID.eq(bugetID)).list();

            if (initialBudgets != null && initialBudgets.size()>0) {
                Log.i("update 13243","update");
                for (InitialBudget initialBudget : initialBudgets) {
                    initialBudget.setPound(pound_dub);
                    initialBudget.setStartdate(DateUtility.getDateBYStringDate(startdate));
                    initialBudget.setComment(comment);
                    initialBudgetDao.update(initialBudget);
                }
            }
            else{
                Log.i("insert 13243", "insert");
                InitialBudget initialBudget=new InitialBudget();
                initialBudget.setBugetID(bugetID);
                initialBudget.setMoneytype(moneytype);
                initialBudget.setPound(pound_dub);
                if(null!=startdate && (!startdate.equals("")))
                initialBudget.setStartdate(DateUtility.getDateBYStringDate(startdate));
                initialBudget.setComment(comment);
                initialBudgetDao.insert(initialBudget);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }


    public static List<InitialBudget> getInitialBudget(Context context){
        try {
            InitialBudgetDao initialBudgetDao=getSession(context).getInitialBudgetDao();
            List<InitialBudget> initialBudgets=initialBudgetDao.loadAll();
            return initialBudgets;
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

}
