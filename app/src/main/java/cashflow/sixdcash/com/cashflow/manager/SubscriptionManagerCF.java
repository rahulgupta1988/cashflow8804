package cashflow.sixdcash.com.cashflow.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cashflow.sixdcash.com.cashflow.Utility.DateUtility;
import cashflowdb.DaoSession;
import cashflowdb.Subscription;
import cashflowdb.SubscriptionDao;

/**
 * Created by opj1 on 16/02/2017.
 */
public class SubscriptionManagerCF {
    public static void insertSubscriptionInfo(Context context, Date startDate, Date endDate, boolean isSubscribed) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        SubscriptionDao subscriptionDao = daoSession.getSubscriptionDao();

        Subscription subscription = new Subscription();
        subscription.setIsSubscribed(isSubscribed);
        subscription.setSubscriptionStartDate(startDate);
        subscription.setSubscriptionEndDate(endDate);
        subscriptionDao.insertOrReplace(subscription);
    }


    public static boolean isSubscribed(Context context) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        SQLiteDatabase db = daoSession.getDatabase();
        String query = "SELECT IS_SUBSCRIBED as is_subs FROM SUBSCRIPTION ORDER BY _id DESC LIMIT 1;";
        Cursor cursor = db.rawQuery(query, null);
        int isSubscribed;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    isSubscribed = cursor.getInt(cursor.getColumnIndex("is_subs"));
                    Log.d("N: " + SubscriptionManagerCF.class.getSimpleName(), "isSubscribed==> " + isSubscribed);
                    if (isSubscribed == 1) {
                        return true;
                    } else if (isSubscribed == 0) {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            return false;
        }
        return false;
    }

    public static Date getSubsEndDate(Context context) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        SQLiteDatabase db = daoSession.getDatabase();
        String query = "Select DATE(SUBSCRIPTION_END_DATE/1000, 'unixepoch', 'localtime') as end_date from SUBSCRIPTION ORDER BY _id DESC LIMIT 1;";
        Cursor cursor = db.rawQuery(query, null);
        String subsEndDate=null;
        Date endDate=null;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    subsEndDate = cursor.getString(cursor.getColumnIndex("end_date"));
                    Log.d("N: " + SubscriptionManagerCF.class.getSimpleName(), "end_date==> " + subsEndDate);
                    DateFormat formatter =  new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    endDate = formatter.parse(subsEndDate);
                    if (null!= endDate) {
                        return endDate;
                    } else{
                        return endDate;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            return endDate;
        }
        return endDate;
    }
}
