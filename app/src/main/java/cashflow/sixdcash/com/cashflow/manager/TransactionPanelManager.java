package cashflow.sixdcash.com.cashflow.manager;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.List;

import cashflow.sixdcash.com.cashflow.Utility.DateUtility;
import cashflow.sixdcash.com.cashflow.Utility.RepetTansactionManager;
import cashflowdb.Category;
import cashflowdb.CategoryDao;
import cashflowdb.DaoSession;
import cashflowdb.InOutTransaction;
import cashflowdb.InOutTransactionDao;
import cashflowdb.RepeatTransaction;
import cashflowdb.RepeatTransactionDao;
import cashflowdb.Source;
import cashflowdb.SourceDao;

/**
 * Created by Praveen on 19-Oct-16.
 */

public class TransactionPanelManager {

    public static String getCategoryByID(Context context, Long catID) {
        try {
            DaoSession daoSession = BaseManager.getDBSessoin(context);
            CategoryDao categoryDao = daoSession.getCategoryDao();
            List<Category> categories = categoryDao.queryBuilder().where(CategoryDao.Properties.Id.eq(catID)).list();
            Log.i("cat id235", "" + categories.get(0).getId());
            return categories.get(0).getCatname();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }


    public static String getSourceByID(Context context, Long sourceID) {
        try {
            DaoSession daoSession = BaseManager.getDBSessoin(context);
            SourceDao sourceDao = daoSession.getSourceDao();
            List<Source> sources = sourceDao.queryBuilder().where(SourceDao.Properties.Id.eq(sourceID)).list();
            return sources.get(0).getSourcename();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }


    public static String getSourceByTranID(Context context, Long tranID) {
        try {
            DaoSession daoSession = BaseManager.getDBSessoin(context);
            InOutTransactionDao inOutTransaction = daoSession.getInOutTransactionDao();
            List<InOutTransaction> inOutTransactions = inOutTransaction.queryBuilder().where(InOutTransactionDao.Properties.Id.eq(tranID)).list();
            return getSourceByID(context, inOutTransactions.get(0).getSourceid());
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }


    public static String getComentByTranID(Context context, Long tranID) {
        try {
            DaoSession daoSession = BaseManager.getDBSessoin(context);
            InOutTransactionDao inOutTransaction = daoSession.getInOutTransactionDao();
            List<InOutTransaction> inOutTransactions = inOutTransaction.queryBuilder().where(InOutTransactionDao.Properties.Id.eq(tranID)).list();
            return inOutTransactions.get(0).getComment();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }


    public static String getHowOftenTranID(Context context, Long tranID) {
        try {
            DaoSession daoSession = BaseManager.getDBSessoin(context);
            InOutTransactionDao inOutTransaction = daoSession.getInOutTransactionDao();
            List<InOutTransaction> inOutTransactions = inOutTransaction.queryBuilder().where(InOutTransactionDao.Properties.Id.eq(tranID)).list();
            return inOutTransactions.get(0).getHowoften();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }


    public static void UpdateTranscation(Context context, Long transactionID, String pounds, String date, String comments, String howOften) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        InOutTransactionDao inOutTransactionDao = daoSession.getInOutTransactionDao();

        List<InOutTransaction> inOutTransactions = inOutTransactionDao.queryBuilder().where(InOutTransactionDao.Properties.Id.eq(transactionID)).list();

        if (inOutTransactions != null && inOutTransactions.size() > 0) {
            InOutTransaction inOutTransaction = inOutTransactions.get(0);
            deleteRepeatTransactions(inOutTransaction.getId(), context);

            if (pounds != null && !pounds.equals("")) {
                Double dob_pounds = Double.parseDouble(pounds);
                inOutTransaction.setPound(dob_pounds);
            }

            if (date != null && !date.equals("")) {
                Date date1 = DateUtility.getDateBYStringDate(date);
                inOutTransaction.setTransactiondate(date1);
            }
            if (date != null && !date.equals("")) {
                Date date1 = DateUtility.getDateBYStringDate(date);
                inOutTransaction.setNextdate(date1);
            }
            if (howOften != null && !howOften.equals("")) {
                inOutTransaction.setHowoften(howOften);
            }

            inOutTransaction.setComment(comments);
            inOutTransactionDao.update(inOutTransaction);

            insertRepeatTransaction(inOutTransaction, context);

        }
    }

    private static void deleteRepeatTransactions(Long transactionId, Context context) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        RepeatTransactionDao repeatTransactionDao = daoSession.getRepeatTransactionDao();
        List<RepeatTransaction> transactionList = null;
        try {
            transactionList = repeatTransactionDao.queryBuilder().where(RepeatTransactionDao.Properties.ActualtrasactionID.eq(transactionId)).list();
            ;
            if (null != transactionList && transactionList.size() > 0) {
                for (RepeatTransaction rt : transactionList) {
                    repeatTransactionDao.delete(rt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void insertRepeatTransaction(InOutTransaction transaction, Context context) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        if (transaction.getIsrepetitive()) {
            String howoften = transaction.getHowoften();
            Log.i("str 810672", "" + howoften);
            int intervals = 0;
            int count = 0;
            String weekORMonth = "";
            String source_str=transaction.getSource().getSourcename();
            if (howoften.equalsIgnoreCase("Every week")) {
                intervals = 7;
                count = 52;
                weekORMonth = "weeks";
            } else if (howoften.equalsIgnoreCase("Every 2 weeks")) {
                intervals = 14;
                count = 52 / 2;
                weekORMonth = "weeks";
            } else if (howoften.equalsIgnoreCase("Every 4 weeks")) {
                Log.i("810672", "" + howoften);
                intervals = 28;
                count = 52 / 4;
                weekORMonth = "weeks";


            }

            else if (howoften.equalsIgnoreCase("Every 6 Weeks")) {
                Log.i("810672", "" + howoften);
                intervals = 42;
                count = 52 / 6;
                weekORMonth = "weeks";

            }

            else if (howoften.equalsIgnoreCase("Every month")) {
                intervals = 1;
                count = 12;
                weekORMonth = "months";
            }

            else if (howoften.equalsIgnoreCase("Every 2 Months")) {
                intervals = 2;
                count = 6;
                weekORMonth = "months";
            }
            else if (howoften.equalsIgnoreCase("Every 3 months")) {
                intervals = 3;
                count = 4;
                weekORMonth = "months";
            } else if (howoften.equalsIgnoreCase("Every 6 months")) {
                intervals = 6;
                count = 2;
                weekORMonth = "months";
            }
            else if ((howoften.equalsIgnoreCase("Every Month (except Feb and Mar)"))
                    && source_str.equals("Council Tax")) {
                intervals = 1;
                count = 10;
                weekORMonth = "months";
            }


            Long last_transactionID = transaction.getId();
            Log.i("last_transactionID999", "" + last_transactionID);

            Log.i("asfdsf 45601", "" + transaction.getNextdate());
            RepetTansactionManager.repeatTrasaction(context, transaction.getPound(),
                    last_transactionID, transaction.getNextdate(), intervals, count, weekORMonth, transaction.getCategoryid(), transaction.getComment());

        } else {
            RepeatTransactionDao repeatTransactionDao = daoSession.getRepeatTransactionDao();
            RepeatTransaction repeatTransaction = new RepeatTransaction();
            repeatTransaction.setPound(transaction.getPound());
            repeatTransaction.setTransactiondate(transaction.getNextdate());
            repeatTransaction.setRecomments(transaction.getComment());
            repeatTransaction.setActualtrasactionID(transaction.getId());
            repeatTransaction.setCatID(transaction.getCategoryid());
            repeatTransaction.setConfirm(true);
            repeatTransactionDao.insertOrReplace(repeatTransaction);

        }
    }

    public static void UpdateRETranscation(Context context, Long transactionID, String pounds, String date, String comments) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        RepeatTransactionDao repeatTransactionDao = daoSession.getRepeatTransactionDao();

        List<RepeatTransaction> repeatTransactions = repeatTransactionDao.queryBuilder().where(RepeatTransactionDao.Properties.Id.eq(transactionID)).list();

        if (repeatTransactions != null && repeatTransactions.size() > 0) {
            RepeatTransaction repeatTransaction = repeatTransactions.get(0);

            if (pounds != null && !pounds.equals("")) {
                Double dob_pounds = Double.parseDouble(pounds);
                repeatTransaction.setPound(dob_pounds);
            }

            if (date != null && !date.equals("")) {
                Date date1 = DateUtility.getDateBYStringDate(date);
                repeatTransaction.setTransactiondate(date1);
            }

            repeatTransaction.setRecomments(comments);
            repeatTransactionDao.update(repeatTransaction);


        }
    }


    public static void UpdateRETranscationByInOutTranID(Context context, Long transactionID, String pounds, String date, String comments) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        RepeatTransactionDao repeatTransactionDao = daoSession.getRepeatTransactionDao();

        List<RepeatTransaction> repeatTransactions = repeatTransactionDao.queryBuilder().where(RepeatTransactionDao.Properties.ActualtrasactionID.eq(transactionID)).list();

        if (repeatTransactions != null && repeatTransactions.size() > 0) {
            for (int i = 0; i < repeatTransactions.size(); i++) {
                RepeatTransaction repeatTransaction = repeatTransactions.get(i);

                if (pounds != null && !pounds.equals("")) {
                    Double dob_pounds = Double.parseDouble(pounds);
                    repeatTransaction.setPound(dob_pounds);
                }

                if (date != null && !date.equals("")) {
                    Date date1 = DateUtility.getDateBYStringDate(date);
                    // repeatTransaction.setTransactiondate(date1);
                }

                repeatTransaction.setRecomments(comments);
                repeatTransactionDao.update(repeatTransaction);
            }


        }
    }


    public static void deleteTranscation(Context context, Long transactionID) {

        DaoSession daoSession = BaseManager.getDBSessoin(context);
        InOutTransactionDao inOutTransactionDao = daoSession.getInOutTransactionDao();

        List<InOutTransaction> inOutTransactions = inOutTransactionDao.queryBuilder().where(InOutTransactionDao.Properties.Id.eq(transactionID)).list();

        if (inOutTransactions != null && inOutTransactions.size() > 0) {
            InOutTransaction inOutTransaction = inOutTransactions.get(0);
            inOutTransactionDao.delete(inOutTransaction);
        }
    }

    public static void deleteRETranscation(Context context, Long transactionID) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        RepeatTransactionDao repeatTransactionDao = daoSession.getRepeatTransactionDao();

        List<RepeatTransaction> repeatTransactions = repeatTransactionDao.queryBuilder().where(RepeatTransactionDao.Properties.Id.eq(transactionID)).list();

        if (repeatTransactions != null && repeatTransactions.size() > 0) {
            RepeatTransaction repeatTransaction = repeatTransactions.get(0);
            repeatTransactionDao.delete(repeatTransaction);
        }
    }

    public static void deleteRETranscationByInOutTraID(Context context, Long transactionID) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        RepeatTransactionDao repeatTransactionDao = daoSession.getRepeatTransactionDao();

        List<RepeatTransaction> repeatTransactions = repeatTransactionDao.queryBuilder().where(RepeatTransactionDao.Properties.ActualtrasactionID.eq(transactionID)).list();

        if (repeatTransactions != null && repeatTransactions.size() > 0) {
            for (int i = 0; i < repeatTransactions.size(); i++) {
                RepeatTransaction repeatTransaction = repeatTransactions.get(i);
                repeatTransactionDao.delete(repeatTransaction);
            }

        }
    }

    public static String getCategoryType(Context mContext, long catID) {
        try {
            DaoSession daoSession = BaseManager.getDBSessoin(mContext);
            CategoryDao categoryDao = daoSession.getCategoryDao();
            List<Category> categories = categoryDao.queryBuilder().where(CategoryDao.Properties.Id.eq(catID)).list();
            Log.i("cat id235", "" + categories.get(0).getId());
            return categories.get(0).getInorexp();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }

    public static InOutTransaction getInOutTransationById(Long trascationID, Context mContext) {
        try {
            DaoSession daoSession = BaseManager.getDBSessoin(mContext);
            InOutTransactionDao inOutTransactionDao = daoSession.getInOutTransactionDao();
            List<InOutTransaction> inOutTransactions = inOutTransactionDao.queryBuilder().where(InOutTransactionDao.Properties.Id.eq(trascationID)).list();
            if (null != inOutTransactions && inOutTransactions.size() > 0) {
                return inOutTransactions.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }
}
