package cashflow.sixdcash.com.cashflow.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import cashflowdb.DaoMaster;
import cashflowdb.DaoSession;


/**
 * Created by Praveen on 8/29/2016.
 */
public class BaseManager {


    public static final String LOG_TAG = "BaseManager";
    public static DaoMaster.DevOpenHelper helper=null;
    public static String databaseLocation;

    public BaseManager(Context context,String location){
          databaseLocation=location;
          getDevOpenHelper(context);
    }
    public static DaoSession getDBSessoin(Context context) {
        if(context!=null){
            DaoMaster.DevOpenHelper helper=getDevOpenHelper(context);
            SQLiteDatabase sqLiteDatabase=helper.getWritableDatabase();
            DaoMaster daoMaster=new DaoMaster(sqLiteDatabase);
            DaoSession daoSession=daoMaster.newSession();
            if (daoSession != null) {
                return daoSession;
            } else {
                Log.i(LOG_TAG, "getDBSessoin:  - daoSession is null");
                return null;
            }
        }
        else {
            Log.i(LOG_TAG, "getDBSessoin:  - Application context is null");
            return null;
        }
    }

    public void closeDatabase(Context context) {
        DaoMaster.DevOpenHelper helper=new DaoMaster.DevOpenHelper(context,databaseLocation+"/cashflowapp.sd",null);
        helper.close();
    }


    // singleTon Object
    public static DaoMaster.DevOpenHelper getDevOpenHelper(Context context){

         if(helper==null) {
             helper = new DaoMaster.DevOpenHelper(context,databaseLocation+"/cashflowapp.sd", null);
             return helper;
         }
         else
             return helper;
     }


}
