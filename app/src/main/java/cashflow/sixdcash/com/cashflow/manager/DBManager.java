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
import cashflowdb.HowOften;
import cashflowdb.HowOftenDao;
import cashflowdb.InOutTransaction;
import cashflowdb.InOutTransactionDao;
import cashflowdb.RepeatTransaction;
import cashflowdb.RepeatTransactionDao;
import cashflowdb.Source;
import cashflowdb.SourceDao;
import cashflowdb.UserInfo;
import cashflowdb.UserInfoDao;
import de.greenrobot.dao.query.QueryBuilder;


/**
 * Created by Praveen on 8/29/2016.
 */
public class DBManager {

    Category category = null;


    public static void insertUserInfo(Context context) {
        try {
            DaoSession daoSession = BaseManager.getDBSessoin(context);
            UserInfoDao userInfoDao = daoSession.getUserInfoDao();

            List<UserInfo> userInfos = userInfoDao.loadAll();
            if (userInfos != null) {
                if (userInfos.size() == 0) {
                    UserInfo userInfo = new UserInfo("1", "Rahul Gupta", "12345", "", "", "");
                    userInfoDao.insertOrReplace(userInfo);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void UpdateUserInfo(Context context, String firstname, String lastname, String imageName) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        UserInfoDao userInfoDao = daoSession.getUserInfoDao();
        List<UserInfo> userInfos = userInfoDao.loadAll();
        UserInfo userInfo = userInfos.get(0);

        if (imageName.equals("")) {
            userInfo.setFirstname(firstname);
            userInfo.setLastname(lastname);
            userInfoDao.update(userInfo);
        } else {
            userInfo.setFirstname(firstname);
            userInfo.setLastname(lastname);
            userInfo.setPicname(imageName);
            userInfoDao.update(userInfo);
        }

    }

    public static UserInfo getUserInfo(Context context) {
        UserInfo userInfo = null;
        try {
            DaoSession daoSession = BaseManager.getDBSessoin(context);
            UserInfoDao userInfoDao = daoSession.getUserInfoDao();
            List<UserInfo> userInfos = userInfoDao.loadAll();
            userInfo = null;
            if (userInfos != null && userInfos.size() > 0) {
                userInfo = userInfos.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;

    }

    public static void insertCategory(Context context) {
        String[] catname = {"Start", "My Income", "The Basics", "Keeping Mobile", "Clothes and Stuff",
                "Entertainment", "Repayments", "Major Buys", "My Year"};

        //String[] catname= context.getResources().getStringArray(R.array.catarray);
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        CategoryDao categoryDao = daoSession.getCategoryDao();
        categoryDao.deleteAll();
        try {
            for (int i = 0; i < catname.length; i++) {
                Category category = new Category();

                if (!catname[i].equals("Start") && !catname[i].equals("My Year")) {
                    if (catname[i].equals("My Income"))
                        category.setInorexp("income");
                    else
                        category.setInorexp("expend");
                } else {
                    category.setInorexp("notInOut");
                }

                category.setCatname(catname[i]);

                categoryDao.insertOrReplace(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void insertSinfleSource(Context context, String sourceName, String catname) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        SourceDao sourceDao = daoSession.getSourceDao();

        Source source = new Source();
        source.setSourcename(sourceName);
        source.setIsChecked(false);
        source.setIsMandatory(false);
        source.setCategoryid(getCategoryIDByName(context, catname));
        sourceDao.insertOrReplace(source);
    }

    public static void insertSource(Context context) {


        try {


            //Travel to Work
            //String[] incomesource= context.getResources().getStringArray(R.array.incomesource);
           /* String[] incomesource = {"Wages", "Wages (partner)", "Job Seekers Allowance", "Housing Benefit", "Sale of Unwanted Items", "Other Income 1", "Other Income 2"};*/
            String[] incomesource = {"Wages", "Wages (partner)", "Benefits 1", "Benefits 2", "Sale of Unwanted Items", "Other Income 1", "Other Income 2"};


            /* String[] spendOnBasics_source = {"Food Essentials", "Travel to Work", "Rent", "Council Tax", "Water", "Gas and Electric", "Loan Repayment", "Other"};*/
            String[] spendOnBasics_source = {"Food Essentials", "Travel to Work", "Rent", "Council Tax", "Water", "Gas","Electric", "Mortgage",
                    "Bus/Train/Taxi", "Mobile","Internet", "Motor Fuel", "Road Tax", "Car Service & MOT",
                    "Motor Insurance","TV Licence", "Other"};
            /*String[] movingAroundANDkeppInTouch_source = {"Bus/Train/Taxi", "Mobile and Internet", "Motor Fuel", "Road Tax", "Car Service & MOT",
                    "Motor Insurance", "Other"};
*/
            String[] movingAroundANDkeppInTouch_source = {"Savings1","Savings2","Savings3","Savings4"};

            String[] clothsANDstuff_source = {"Clothes/Shoes", "Accessories", "Hair & Skin Care", "Fragrances", "Other"};
            String[] funANDentertainment_source = {"Snacks", "Takeaway meals", "Alcohol", "Tobacco", "Music/TV/Films", "Other"};
            String[] holidaysANDmajorbuys_source = {"Holiday / Major Buy 1", "Holiday / Major Buy 2", "Holiday / Major Buy 3",
                    "Holiday / Major Buy 4", "Holiday / Major Buy 5"};

            String[] Repayments_source = {"Loan 1", "Loan 2", "Credit Card 1", "Credit Card 2"};

            DaoSession daoSession = BaseManager.getDBSessoin(context);
            SourceDao sourceDao = daoSession.getSourceDao();


            // insert income sources
            for (int i = 0; i < incomesource.length; i++) {
                Source source = new Source();
                source.setSourcename(incomesource[i]);
                source.setIsChecked(false);
                if(i==2 || i==5 || i==6){
                    source.setIsMandatory(true);
                }else {
                    source.setIsMandatory(false);
                }
                source.setCategoryid(getCategoryIDByName(context, "My Income"));
                sourceDao.insertOrReplace(source);

            }

            // insert spending on baiscs sources
            for (int i = 0; i < spendOnBasics_source.length; i++) {
                Source source = new Source();
                source.setSourcename(spendOnBasics_source[i]);
                source.setIsChecked(false);
                if (i == 2 || i == 3 || i == 6) {
                    source.setIsMandatory(true);
                } else {
                    source.setIsMandatory(false);
                }
                source.setCategoryid(getCategoryIDByName(context, "The Basics"));
                sourceDao.insertOrReplace(source);

            }

            // insert Moving Around & Keeping In Touch sources
            for (int i = 0; i < movingAroundANDkeppInTouch_source.length; i++) {
                Source source = new Source();
                source.setSourcename(movingAroundANDkeppInTouch_source[i]);
                source.setIsChecked(false);
                if (i == 3 || i == 4 || i == 5) {
                    source.setIsMandatory(true);
                } else {
                    source.setIsMandatory(false);
                }
                source.setCategoryid(getCategoryIDByName(context, "Keeping Mobile"));
                sourceDao.insertOrReplace(source);

            }

            // insert Clothes and Stuff sources
            for (int i = 0; i < clothsANDstuff_source.length; i++) {
                Source source = new Source();
                source.setSourcename(clothsANDstuff_source[i]);
                source.setIsChecked(false);
                source.setIsMandatory(false);
                source.setCategoryid(getCategoryIDByName(context, "Clothes and Stuff"));
                sourceDao.insertOrReplace(source);

            }

            // insert Fun & Entertainment sources
            for (int i = 0; i < funANDentertainment_source.length; i++) {
                Source source = new Source();
                source.setSourcename(funANDentertainment_source[i]);
                source.setIsChecked(false);
                if(i==4) {
                    source.setIsMandatory(true);
                }else
                {
                    source.setIsMandatory(false);
                }
                source.setCategoryid(getCategoryIDByName(context, "Entertainment"));
                sourceDao.insertOrReplace(source);

            }

            // insert Holidays & Major Buys sources
            for (int i = 0; i < holidaysANDmajorbuys_source.length; i++) {
                Source source = new Source();
                source.setSourcename(holidaysANDmajorbuys_source[i]);
                source.setIsChecked(false);
                source.setIsMandatory(false);
                source.setCategoryid(getCategoryIDByName(context, "Major Buys"));
                sourceDao.insertOrReplace(source);

            }

            // insert Repayments sources
            for (int i = 0; i < Repayments_source.length; i++) {
                Source source = new Source();
                source.setSourcename(Repayments_source[i]);
                source.setIsChecked(false);
                source.setIsMandatory(false);
                source.setCategoryid(getCategoryIDByName(context, "Repayments"));
                sourceDao.insertOrReplace(source);

            }

            insertHowOften(context);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void insertHowOften(Context context) {

       /* String[] howofentype1 = {"Every month", "Every 4 weeks", "Every 2 weeks", "Every week"};

        String[] howofentype2 = {"Once in year"};

        String[] howofentype3 = {"Every month", "Every 3 months", "Every 6 months", "Once in year", "Every 4 weeks", "Every 2 weeks", "Every week"};

        String[] howofentype4 = {"10 times a year", "Every month", "Every 6 months", "Once in year"};

        String[] howofentype5 = {"Every month", "Every 3 months", "Every 6 months", "10 times a year", "Once in year", "Every 4 weeks", "Every 2 weeks", "Every week"};

        String[] howofentype6 = {"Every month", "Every week"};

        String[] howofentype7 = {"Every month", "Every 3 months"};

        String[] howofentype8 = {"Every month", "Every 6 months", "Every year"};

        String[] howofentype9 = {"Every month", "Every year"};

        String[] howofentype10 = {"Every month", "Every week", "Every 2 weeks"};

        String[] howofentype11 = {"Every week", "Every month", "Every 3 months", "Every year" };

        String[] howofentype12 = {"Every month", "Every week"};

        String[] howofentype13 = {"Every month", "Every week", "Once in year"};

        String[] howofentype14 = {"Once in year"};

        String[] howofentype15 = {"Every month", "Every 3 months", "Every 6 months", "Once in year", "Every 4 weeks", "Every 2 weeks", "Every week"};

        String[] howofentype16 = {"Every year"};
*/




        String[] howofentype1 = {"Every Week", "Every 2 Weeks", "Every 4 Weeks", "Every 6 Weeks","Every Month",
                "Every 2 Months","Every 3 Months","Every 6 Months","Every Year","Once in Year"};

        String[] howofentype2 ={"Once in Year"};

        String[] howofentype3 = {"Every Week", "Every 2 Weeks", "Every 4 Weeks", "Every 6 Weeks","Every Month",
                "Every 2 Months","Every 3 Months","Every 6 Months","Every Year","Once in Year"};

        String[] howofentype4 = {"Every Month (except Feb and Mar)","Every Month","Every 6 Months","Once in Year"};

        String[] howofentype5 = {"Every Week", "Every 2 Weeks", "Every 4 Weeks", "Every 6 Weeks","Every Month",
                "Every 2 Months","Every 3 Months","Every 6 Months","Once in Year"};

        String[] howofentype14 = {"One Off"};





        DaoSession daoSession = BaseManager.getDBSessoin(context);
        SourceDao sourceDao = daoSession.getSourceDao();
        List<Source> sources = sourceDao.loadAll();


        Log.i("Sources lenth1001", "" + sources.size());

        HowOftenDao howOftenDao = daoSession.getHowOftenDao();
        howOftenDao.deleteAll();

        if (sources != null) {

            for (int j = 0; j < sources.size(); j++) {

                Source source = sources.get(j);

                // how oftem for Wages,Wages (partner),Benefits and Food Eseentials Categories

                //"Loan 1","Loan 2","Credit Card 1","Credit Card 2"
                if (source.getSourcename().equalsIgnoreCase("Wages") ||
                        source.getSourcename().equalsIgnoreCase("Wages (partner)")) {


                    for (int x = 0; x < howofentype1.length; x++) {

                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype1[x]);

                        Log.i("sidhowoften 0011", "" + getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        Long rows = howOftenDao.insertOrReplace(howOften);
                        Log.i("insert or not 0011", "" + rows);

                    }

                } else if (source.getSourcename().equalsIgnoreCase("Sale of Unwanted Items")) {
                    for (int x = 0; x < howofentype2.length; x++) {
                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype2[x]);
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOftenDao.insertOrReplace(howOften);
                    }

                }

                // for Other Income 1,Other Income 2 and Rent Categories

                else if (source.getSourcename().equalsIgnoreCase("Other Income 1") || source.getSourcename().equalsIgnoreCase("Other Income 2")) {
                    for (int x = 0; x < howofentype3.length; x++) {
                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype3[x]);
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOftenDao.insertOrReplace(howOften);

                    }

                }


                // for Other Council Tax Category
                else if (source.getSourcename().equalsIgnoreCase("Council Tax")) {
                    for (int x = 0; x < howofentype4.length; x++) {

                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype4[x]);
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOftenDao.insertOrReplace(howOften);

                    }

                }


                // for Water Category
                else if (source.getSourcename().equalsIgnoreCase("Water")||
                        source.getSourcename().equalsIgnoreCase("Rent")||
                        source.getSourcename().equalsIgnoreCase("Housing Benefit") ||
                        source.getSourcename().equalsIgnoreCase("Food Essentials") ||
                        source.getSourcename().equalsIgnoreCase("Loan 1") ||
                        source.getSourcename().equalsIgnoreCase("Loan 2") ||
                        source.getSourcename().equalsIgnoreCase("Credit Card 1") ||
                        source.getSourcename().equalsIgnoreCase("Credit Card 2")) {
                    for (int x = 0; x < howofentype5.length; x++) {
                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype5[x]);
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOftenDao.insertOrReplace(howOften);

                    }

                }


                // for  Gas and Electric,Travel to Work, Loan Repayment,Other,Travel to Work and Motor FuelCategory
                else if (source.getSourcename().equalsIgnoreCase("Gas and Electric") ||
                        source.getSourcename().equalsIgnoreCase("Travel to Work") ||
                        source.getSourcename().equalsIgnoreCase("Mortgage") ||
                        source.getSourcename().equalsIgnoreCase("Other") ||
                        source.getSourcename().equalsIgnoreCase("Motor Fuel")) {
                    for (int x = 0; x < howofentype5.length; x++) {

                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype5[x]);
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOftenDao.insertOrReplace(howOften);

                    }

                }


                // for Mobile and Internet Category
                else if (source.getSourcename().equalsIgnoreCase("Mobile and Internet")) {
                    for (int x = 0; x < howofentype5.length; x++) {

                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype5[x]);
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOftenDao.insertOrReplace(howOften);

                    }

                }

                //for Road Tax Category
                else if (source.getSourcename().equalsIgnoreCase("Road Tax")) {
                    for (int x = 0; x < howofentype5.length; x++) {

                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype5[x]);
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOftenDao.insertOrReplace(howOften);

                    }

                }

                //for Motor Insurance Category
                else if (source.getSourcename().equalsIgnoreCase("Motor Insurance")) {
                    for (int x = 0; x < howofentype5.length; x++) {

                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype5[x]);
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOftenDao.insertOrReplace(howOften);

                    }

                }


                //for Clothes/Shoes,Accessories,Hair & Skin Care,Fragrances,Snacks,Takeaway meals,Alcohol,Tobacco Category
                else if (source.getSourcename().equalsIgnoreCase("Clothes/Shoes") ||
                        source.getSourcename().equalsIgnoreCase("Accessories") ||
                        source.getSourcename().equalsIgnoreCase("Hair & Skin Care") ||
                        source.getSourcename().equalsIgnoreCase("Fragrances") ||
                        source.getSourcename().equalsIgnoreCase("Snacks") ||
                        source.getSourcename().equalsIgnoreCase("Takeaway meals") ||
                        source.getSourcename().equalsIgnoreCase("Alcohol") ||
                        source.getSourcename().equalsIgnoreCase("Tobacco")) {
                    for (int x = 0; x < howofentype5.length; x++) {

                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype5[x]);
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOftenDao.insertOrReplace(howOften);

                    }

                }

                //for Car Service & MOT and Holiday Category
                else if (source.getSourcename().contains("Holiday / Major Buy 1") ||
                        source.getSourcename().contains("Holiday / Major Buy 2") ||
                        source.getSourcename().contains("Holiday / Major Buy 3") ||
                        source.getSourcename().contains("Holiday / Major Buy 4") ||
                        source.getSourcename().contains("Holiday / Major Buy 5")) {

                    for (int x = 0; x < howofentype14.length; x++) {
                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype14[x]);
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOftenDao.insertOrReplace(howOften);
                    }
                }
                //for Car Service & MOT and Holiday Category
                else if (source.getSourcename().equalsIgnoreCase("Car Service & MOT") ) {

                    for (int x = 0; x < howofentype5.length; x++) {
                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype5[x]);
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOftenDao.insertOrReplace(howOften);
                    }
                }


                //for TV Licence Category
                else if (source.getSourcename().equalsIgnoreCase("TV Licence")) {
                    for (int x = 0; x < howofentype5.length; x++) {
                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype5[x]);
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOftenDao.insertOrReplace(howOften);

                    }

                }


                //for Bus/Train/Taxi and Music/TV/Films Category
                else if (source.getSourcename().equalsIgnoreCase("Bus/Train/Taxi") ||
                        source.getSourcename().equalsIgnoreCase("Music/TV/Films")) {
                    for (int x = 0; x < howofentype5.length; x++) {

                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype5[x]);
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOftenDao.insertOrReplace(howOften);

                    }

                }


                //for Other - Style & Fashion Category
                else if (source.getSourcename().equalsIgnoreCase("Other - Style & Fashion")) {
                    for (int x = 0; x < howofentype5.length; x++) {

                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype5[x]);
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOftenDao.insertOrReplace(howOften);

                    }

                } else {
                    for (int x = 0; x < howofentype5.length; x++) {
                        HowOften howOften = new HowOften();
                        howOften.setOften(howofentype5[x]);
                        howOften.setSourceid(getSourceIDByName(context, source.getSourcename(), source.getCategoryid()));
                        howOftenDao.insertOrReplace(howOften);

                    }
                }


            }
        }

    }

    public static void insertHowOftenForSingleSource(Context context, String sourceName, Long catID) {
        if(catID!=8) {
          /*  String[] howofentype1 = {*//*"one off",*//* "Every month", "Every 3 months", "Every 6 months", "Every year", "Every 4 weeks", "Every 2 weeks", "Every week"};*/

            String[] howofentype1 = {"Every Week", "Every 2 Weeks", "Every 4 Weeks", "Every 6 Weeks","Every Month",
                    "Every 2 Months","Every 3 Months","Every 6 Months","Every Year","Once in Year"};

        DaoSession daoSession = BaseManager.getDBSessoin(context);
        SourceDao sourceDao = daoSession.getSourceDao();


        HowOftenDao howOftenDao = daoSession.getHowOftenDao();

        for (int x = 0; x < howofentype1.length; x++) {

            HowOften howOften = new HowOften();
            howOften.setOften(howofentype1[x]);
            howOften.setSourceid(getSourceIDByName(context, sourceName, catID));
            howOftenDao.insertOrReplace(howOften);

        }
        }else {
//            String[] howofentype1 = {"one off", "Every month", "Every 3 months", "Every 6 months", "Every year", "Every 4 weeks", "Every 2 weeks", "Every week"};
           /* String[] howofentype1 = {"Once in year", "Every month", "Every 3 months", "Every 6 months", "Every year", "Every 4 weeks", "Every 2 weeks", "Every week"};*/
            String[] howofentype1 = {"Every Week", "Every 2 Weeks", "Every 4 Weeks", "Every 6 Weeks","Every Month",
                    "Every 2 Months","Every 3 Months","Every 6 Months","Every Year","Once in Year"};

            DaoSession daoSession = BaseManager.getDBSessoin(context);
            SourceDao sourceDao = daoSession.getSourceDao();


            HowOftenDao howOftenDao = daoSession.getHowOftenDao();

            for (int x = 0; x < howofentype1.length; x++) {
                HowOften howOften = new HowOften();
                howOften.setOften(howofentype1[x]);
                howOften.setSourceid(getSourceIDByName(context, sourceName, catID));
                howOftenDao.insertOrReplace(howOften);

            }
        }

    }


    public static Long getCategoryIDByName(Context context, String catname) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        CategoryDao categoryDao = daoSession.getCategoryDao();
        List<Category> categories = categoryDao.queryBuilder().where(CategoryDao.Properties.Catname.eq(catname)).list();
        Log.i("cat id235", "" + categories.get(0).getId());
        return categories.get(0).getId();

    }


    public static Long getSourceIDByName(Context context, String source_str, Long catID) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        SourceDao sourceDao = daoSession.getSourceDao();
        List<Source> sources;

        QueryBuilder qb = sourceDao.queryBuilder();
        qb.where(SourceDao.Properties.Sourcename.eq(source_str));
        qb.where(SourceDao.Properties.Categoryid.eq(catID));
        //sources = sourceDao.queryBuilder().where(SourceDao.Properties.Sourcename.eq(source_str)).list();
        sources = qb.list();
        return sources.get(0).getId();

    }


    /*public static void deleteSourceBySourceID(Context context, Long sourceID) {

        String SQL_DELETE = "DELETE  FROM '" + SourceDao.TABLENAME
                + "' WHERE " + SourceDao.Properties.Id.columnName + " = " + sourceID;

        Log.i("delete q589", "" + SQL_DELETE);

        DaoSession daoSession = BaseManager.getDBSessoin(context);
        Cursor c = daoSession.getDatabase().rawQuery(SQL_DELETE, null);
        Log.i("delete q589", "" + SQL_DELETE);
        Log.i("cursor  q589", "" + c.getCount());


    }
*/

    public static void checkSourceBySourceID(Context context, Long sourceID, boolean ckecked) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        SourceDao sourceDao = daoSession.getSourceDao();
        List<Source> sources = sourceDao.queryBuilder().where(SourceDao.Properties.Id.eq(sourceID)).list();

        if (sources != null) {
            for (Source post : sources) {

                post.setIsChecked(ckecked);
                sourceDao.update(post);
            }
        }

    }

    public static void unCheckAllSource(Context context) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        SourceDao sourceDao = daoSession.getSourceDao();
        List<Source> sources = sourceDao.loadAll();

        if (sources != null) {
            for (Source post : sources) {

                post.setIsChecked(false);
                sourceDao.update(post);
            }
        }

    }


    public static Category getCategoryByName(Context context, String catname) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        CategoryDao categoryDao = daoSession.getCategoryDao();
        List<Category> categories = categoryDao.queryBuilder().where(CategoryDao.Properties.Catname.eq(catname)).list();
        Log.i("categories 111", "" + categories.toString());
        return categories.get(0);

    }


    public static void insertInOutTransaction(Context context, String sourcename,
                                              String comment, String pound,
                                              String pence, String howoften, String nextdate,
                                              Long catID, Long sourceID) {

        DaoSession daoSession = BaseManager.getDBSessoin(context);
        InOutTransactionDao transactionDao = daoSession.getInOutTransactionDao();


        Date todayDate = DateUtility.getTodayDate();
        Log.i("todayDate", "" + todayDate);

        InOutTransaction transaction = new InOutTransaction();
        transaction.setTransactiondate(todayDate);


        //transaction.setInOutTransactiondate(todayDate);
        //transaction.setSourcename(sourcename);
        transaction.setComment(comment);

        Double pound_dub = Double.parseDouble(pound);

        transaction.setPound(pound_dub);
        transaction.setHowoften(howoften);

        Date nxtdate = DateUtility.getDateBYStringDate(nextdate);
        if (nxtdate != null)

            transaction.setNextdate(nxtdate);

        transaction.setCategoryid(catID);
        transaction.setSourceid(sourceID);

        Long rows = transactionDao.insertOrReplace(transaction);
        Log.i("row inserted", "" + rows);


    }

    public static void insertTransactionBYList(Context context, List<InOutTransaction> transactions, Long catID) {

// transactionDao.insertOrReplaceInTx(transactions);
        for (int i = 0; i < transactions.size(); i++) {


            DaoSession daoSession = BaseManager.getDBSessoin(context);
            InOutTransactionDao transactionDao = daoSession.getInOutTransactionDao();
            transactionDao.insertOrReplace(transactions.get(i));

            InOutTransaction transaction = transactions.get(i);
            String source_str=transaction.getSource().getSourcename();

            if (transaction.getIsrepetitive()) {

                String howoften = transaction.getHowoften();
                Log.i("str 810672", "" + howoften);
                int intervals = 0;
                int count = 0;
                String weekORMonth = "";

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

                else if (howoften.equalsIgnoreCase("Every 6 weeks")) {
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
                } else if ((howoften.equalsIgnoreCase("Every Month (except Feb and Mar)"))
                        && source_str.equals("Council Tax")) {
                    intervals = 1;
                    count = 10;
                    weekORMonth = "months";
                }


                Long last_transactionID = getLastInOutTransactionID(context);
                Log.i("last_transactionID999", "" + last_transactionID);

                Log.i("asfdsf 45601", "" + transaction.getNextdate());
                RepetTansactionManager.repeatTrasaction(context, transaction.getPound(),
                        last_transactionID, transaction.getNextdate(), intervals, count, weekORMonth, catID, transaction.getComment());

            } else {
                RepeatTransactionDao repeatTransactionDao = daoSession.getRepeatTransactionDao();
                RepeatTransaction repeatTransaction = new RepeatTransaction();
                repeatTransaction.setPound(transaction.getPound());
                repeatTransaction.setTransactiondate(transaction.getNextdate());
                repeatTransaction.setRecomments(transaction.getComment());
                repeatTransaction.setActualtrasactionID(getLastInOutTransactionID(context));
                repeatTransaction.setCatID(catID);
                repeatTransaction.setConfirm(true);
                repeatTransactionDao.insertOrReplace(repeatTransaction);

            }

        }


    }

    public static Long getLastInOutTransactionID(Context context) {

        Long result = 0L;
        /*String SQL_LAST_VALUE_OF_COLUMN = "SELECT TOP 1  FROM '" + InOutTransactionDao.TABLENAME
                + "' ORDER BY " + InOutTransactionDao.Properties.Id.columnName + " DESC";
*/

        String SQL_LAST_VALUE_OF_COLUMN = "SELECT * FROM '" + InOutTransactionDao.TABLENAME
                + "' WHERE  _id = (SELECT MAX(_id)  FROM '" + InOutTransactionDao.TABLENAME + "'" + ")";


        Log.i("last query 824905", "" + SQL_LAST_VALUE_OF_COLUMN);


        DaoSession daoSession = BaseManager.getDBSessoin(context);
        Cursor c = daoSession.getDatabase().rawQuery(SQL_LAST_VALUE_OF_COLUMN, null);
        try {
            if (c.moveToFirst()) {
                do {
                    result = c.getLong(0);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return result;
    }

    public static List<InOutTransaction> getTrasaction(Context context) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        InOutTransactionDao transactionDao = daoSession.getInOutTransactionDao();

        List<InOutTransaction> transactions;

        transactions = transactionDao.loadAll();
        return transactions;
    }

    public static List<InOutTransaction> getTrasactionByID(Context context, Long catID) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        InOutTransactionDao transactionDao = daoSession.getInOutTransactionDao();
        List<InOutTransaction> inOutTransactions = transactionDao.queryBuilder().where(InOutTransactionDao.Properties.Categoryid.eq(catID)).list();
        return inOutTransactions;
    }


    public static List<RepeatTransaction> getRepeatTrasaction(Context context) {
        DaoSession daoSession = BaseManager.getDBSessoin(context);
        RepeatTransactionDao transactionDao = daoSession.getRepeatTransactionDao();

        List<RepeatTransaction> transactions;

        transactions = transactionDao.loadAll();
        return transactions;
    }


}
