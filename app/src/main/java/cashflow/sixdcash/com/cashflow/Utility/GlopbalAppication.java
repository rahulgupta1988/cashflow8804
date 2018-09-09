package cashflow.sixdcash.com.cashflow.Utility;

import android.app.Application;
import android.util.Log;

import java.io.File;

import cashflow.sixdcash.com.cashflow.manager.BaseManager;
import cashflow.sixdcash.com.cashflow.manager.DBManager;
import cashflow.sixdcash.com.cashflow.manager.FinancialYearManager;
import cashflowdb.CategoryDao;
import cashflowdb.DaoSession;

/**
 * Created by Praveen on 9/21/2016.
 */
public class GlopbalAppication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        File externalloc = getExternalFilesDir("cashdata");
        new BaseManager(getApplicationContext(), externalloc.getAbsolutePath());


        // get category count to check data is alredy inserted or not using CategoryDao
        DaoSession daoSession = BaseManager.getDBSessoin(this);
        CategoryDao categoryDao = daoSession.getCategoryDao();
        Log.i("count 232", "" + categoryDao.count());


        // check if cetegory count is 0 then insert data otherwise not
        if (categoryDao != null && categoryDao.count() == 0) {
            Log.i("first time data insert", "first time data insert");
            //DBManager.insertUserInfo(this);
            DBManager.insertCategory(this);
            DBManager.insertSource(this);


        } else {
            Log.i("data already inserted", "data already inserted");
        }



        DBManager.insertUserInfo(getApplicationContext());
    }
}
