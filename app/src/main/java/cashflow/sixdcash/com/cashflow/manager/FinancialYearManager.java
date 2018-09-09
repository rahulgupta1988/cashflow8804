package cashflow.sixdcash.com.cashflow.manager;

import android.content.Context;

import java.util.List;

import cashflow.sixdcash.com.cashflow.Utility.DateUtility;
import cashflowdb.DaoSession;
import cashflowdb.FinancialYear;
import cashflowdb.FinancialYearDao;

/**
 * Created by Praveen on 10/3/2016.
 */
public class FinancialYearManager {

    public static void insertFinalcialStartANDEnd(Context  context,String startDate,String endDate){

        DaoSession daoSession = BaseManager.getDBSessoin(context);
        FinancialYearDao financialYearDao=daoSession.getFinancialYearDao();

        FinancialYear financialYear=new FinancialYear();
        financialYear.setStartdate(DateUtility.getDateBYStringDate(startDate));
        financialYear.setEnddate(DateUtility.getDateBYStringDate(endDate));
        financialYear.setCompeted("0");
        financialYearDao.insertOrReplace(financialYear);


    }


    public static List<FinancialYear> getFinancialYear(Context context){

        List<FinancialYear> financialYears= null;
        try {
            DaoSession daoSession = BaseManager.getDBSessoin(context);
            FinancialYearDao financialYearDao=daoSession.getFinancialYearDao();
            financialYears = financialYearDao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return financialYears;

    }
}
