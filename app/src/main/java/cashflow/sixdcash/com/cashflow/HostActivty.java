package cashflow.sixdcash.com.cashflow;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.DefaultXAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.XAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.File;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cashflow.sixdcash.com.cashflow.Utility.ChartValueFormatter;
import cashflow.sixdcash.com.cashflow.Utility.CircularImageView;
import cashflow.sixdcash.com.cashflow.Utility.CustomBarDataSet;
import cashflow.sixdcash.com.cashflow.Utility.DateUtility;
import cashflow.sixdcash.com.cashflow.Utility.ImageSelector;
import cashflow.sixdcash.com.cashflow.Utility.LogOutDialog;
import cashflow.sixdcash.com.cashflow.Utility.ValidationUtility;
import cashflow.sixdcash.com.cashflow.adapter.CategoryAdapter;
import cashflow.sixdcash.com.cashflow.admin.AdminHome;
import cashflow.sixdcash.com.cashflow.fragments.Home;
import cashflow.sixdcash.com.cashflow.fragments.MoneyNow;
import cashflow.sixdcash.com.cashflow.fragments.SummeryFragment;
import cashflow.sixdcash.com.cashflow.manager.BaseManager;
import cashflow.sixdcash.com.cashflow.manager.DBManager;
import cashflow.sixdcash.com.cashflow.manager.DayInOutTransactionManager;
import cashflow.sixdcash.com.cashflow.manager.FinancialYearManager;
import cashflow.sixdcash.com.cashflow.manager.GraphTranCalManager;
import cashflow.sixdcash.com.cashflow.manager.InitialBudgetManager;
import cashflowdb.Category;
import cashflowdb.DaoSession;
import cashflowdb.DayInOutSummery;
import cashflowdb.FinancialYear;
import cashflowdb.FinancialYearDao;
import cashflowdb.InOutTransactionDao;
import cashflowdb.InitialBudget;
import cashflowdb.InitialBudgetDao;
import cashflowdb.RepeatTransactionDao;
import cashflowdb.UserInfo;


public class HostActivty extends Activity implements View.OnClickListener {


    private Context mContext;
    private String lastClickedCat = "Start";

    LinearLayout headerlay;
    TextView headertitle;
    TextView withusername;
    ImageView homeicon;

    RecyclerView catlist;
    FragmentManager frgManager;
    // footer
    LinearLayout buttonplantab, nextweektarget, fourweektarget, howspendmoney;

    // top bar icons

    ImageView settingicon, usersetting, reseticon, editImageView, submitMenuButton,helpicon;
    int temp_intervals = 0;
    private PieChart pieChartOfCategory;
    private LinearLayout hintAndTips;
    private RelativeLayout copyRightLL;
    private TextView hyperLinkTV;
    private TextView copyRightTV;
    private LinearLayout parentLL;
    ImageView addMoreMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

      /*  String unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i("uniq2343",""+unique_id);*/


        Intent intent = getIntent();
        String mycatName = intent.getStringExtra("catName");
        if (mycatName != null && !mycatName.equals("")) {
            lastClickedCat = mycatName;
        }

        mContext = this;

        //setContentView(R.layout.activity_host_activty);
        if(isTablet(mContext)) {
            setContentView(R.layout.activity_host_activty);
        }
        else{
            setContentView(R.layout.activity_host_activty_phone);
        }


        init();
        setCatAdapter();
      /*
               final AppEULA appEULA = new AppEULA(mContext);

       parentLL = (LinearLayout) findViewById(R.id.parentLL);
        parentLL.post(new Runnable() {
            @Override
            public void run() {
                appEULA.show();
            }
        });*/
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void init() {

        headerlay = (LinearLayout) findViewById(R.id.headerlay);
        headertitle = (TextView) findViewById(R.id.headertitle);

        String cat_name= "";
        if(lastClickedCat.equalsIgnoreCase("My Income"))
            cat_name="Income";

        else if(lastClickedCat.equalsIgnoreCase("The Basics"))
            cat_name="Basic Expenditure";

        else if(lastClickedCat.equalsIgnoreCase("Major Buys"))
            cat_name="Major Purchases";

        else if(lastClickedCat.equalsIgnoreCase("Keeping Mobile"))
            cat_name="Savings";

        else
            cat_name=lastClickedCat;


        headertitle.setText("My Money Planner - " + cat_name);
        withusername = (TextView) findViewById(R.id.withusername);


        helpicon=(ImageView)findViewById(R.id.helpicon);
        helpicon.setOnClickListener(this);
        // setting
        settingicon = (ImageView) findViewById(R.id.settingicon);
        settingicon.setOnClickListener(this);

        editImageView = (ImageView) findViewById(R.id.editImageView);
        editImageView.setOnClickListener(this);

        usersetting = (ImageView) findViewById(R.id.usersetting);
        usersetting.setOnClickListener(this);

        homeicon = (ImageView) findViewById(R.id.homeicon);
        homeicon.setOnClickListener(this);



        reseticon = (ImageView) findViewById(R.id.reseticon);
        reseticon.setOnClickListener(this);


        addMoreMenuButton = (ImageView) findViewById(R.id.addMoreMenuButton);
        submitMenuButton = (ImageView) findViewById(R.id.submitMenuButton);
        addMoreMenuButton.setOnClickListener(this);
        submitMenuButton.setOnClickListener(this);

        setUserPic();
        setUserName();
        buttonplantab = (LinearLayout) findViewById(R.id.buttonplantab);
        copyRightLL = (RelativeLayout) findViewById(R.id.copyRightLL);
        nextweektarget = (LinearLayout) findViewById(R.id.nextweektarget);
        fourweektarget = (LinearLayout) findViewById(R.id.fourweektarget);
        howspendmoney = (LinearLayout) findViewById(R.id.howspendmoney);
        hintAndTips = (LinearLayout) findViewById(R.id.HaTHost);
        hyperLinkTV = (TextView) findViewById(R.id.hyperLinkTV);
        copyRightTV = (TextView) findViewById(R.id.copyRightTV);
        hyperLinkTV.setClickable(true);
        hyperLinkTV.setMovementMethod(LinkMovementMethod.getInstance());
        copyRightTV.setClickable(true);
        copyRightTV.setMovementMethod(LinkMovementMethod.getInstance());

        String text = "Built by " + "<font color='#0297F1'><a href='http://www.wsipolarisdigital.com'>WSI Polaris Digital Marketing</a></font>";
        String copyRightText = "<b>My Money Planner Version "+getPackageInfo().versionName+"</b><br><a href='http://www.flickerassociates.co.uk'>© Flicker Associates 2018</a>";
        hyperLinkTV.setText(Html.fromHtml(text));
        copyRightTV.setText(Html.fromHtml(copyRightText));
        nextweektarget.setOnClickListener(this);
        fourweektarget.setOnClickListener(this);
        howspendmoney.setOnClickListener(this);
        hintAndTips.setOnClickListener(this);

        isSummery(false);
        setHeaderColor(lastClickedCat);
    }


    private PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }
    private void setCatAdapter() {
        catlist = (RecyclerView) findViewById(R.id.catlist);
        CategoryAdapter adapter = new CategoryAdapter(mContext, categoriesInteface, false, lastClickedCat);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        catlist.setLayoutManager(layoutManager);
        catlist.setItemAnimator(new DefaultItemAnimator());
        catlist.setAdapter(adapter);
        fillContanierInitially();
    }

    private void setUserPic() {
        String userprofpic = "";
        UserInfo userInfo = DBManager.getUserInfo(mContext);
        if (userInfo != null) {
            userprofpic = userInfo.getPicname();
            if (userprofpic != null && !userprofpic.equals("")) {
                Bitmap bitmap = getBitmap(userprofpic);


                if (bitmap != null) {
                    usersetting.setBackground(null);
                    usersetting.setImageBitmap(bitmap);
                }
            }
        }
    }

    private void setUserName() {
        UserInfo userInfo = DBManager.getUserInfo(mContext);
        if (userInfo != null) {
            String username = userInfo.getFirstname();
            if(isTablet(mContext)){
                if (!username.equals(""))
                withusername.setText(username + "'s" + "\n  Money Planner");
            }
            else {
                if (!username.equals("")) {
                    if (username.length() > 15) {
                        username = username.substring(0, 12);
                        withusername.setText(username + "'s..." + "\n  Money Planner");
                    } else {
                        withusername.setText(username + "'s" + "\n  Money Planner");
                    }


                }
            }
        }
    }

    CategoryAdapter.CategoriesInteface categoriesInteface = new CategoryAdapter.CategoriesInteface() {

        @Override
        public void selCat(String selcat, int position) {
            //headerlay.setBackground(drawable);

            String cat_name= "";
            if(selcat.equalsIgnoreCase("My Income"))
                cat_name="Income";

            else if(selcat.equalsIgnoreCase("The Basics"))
                cat_name="Basic Expenditure";

            else if(selcat.equalsIgnoreCase("Major Buys"))
                cat_name="Major Purchases";

            else if(selcat.equalsIgnoreCase("Keeping Mobile"))
                cat_name="Savings";

            else
                cat_name=selcat;

            headertitle.setText("My Money Planner - " + cat_name);
            setHeaderColor(selcat);
            setContainer(selcat);
        }
    };


    String head_color="#99800b";
    public void setHeaderColor(String catName) {

        if (catName.equalsIgnoreCase("Start")) {
            head_color="#99800b";
            headerlay.setBackgroundColor(Color.parseColor("#99800b"));
        } else if (catName.equalsIgnoreCase("My Income")) {
            head_color="#bc2a29";
            headerlay.setBackgroundColor(Color.parseColor("#bc2a29"));
        } else if (catName.equalsIgnoreCase("The Basics")) {
            head_color="#2952d7";
            headerlay.setBackgroundColor(Color.parseColor("#2952d7"));
        } else if (catName.contains("Keeping Mobile")) {
            head_color="#d78729";
            headerlay.setBackgroundColor(Color.parseColor("#d78729"));
        } else if (catName.contains("Clothes and Stuff")) {
            head_color="#1e7a23";
            headerlay.setBackgroundColor(Color.parseColor("#1e7a23"));
        } else if (catName.contains("Entertainment")) {
            head_color="#721ea2";
            headerlay.setBackgroundColor(Color.parseColor("#721ea2"));
        } else if (catName.contains("Major Buys")) {
            head_color="#0b89a4";
            headerlay.setBackgroundColor(Color.parseColor("#0b89a4"));
        } else if (catName.contains("Repayments")) {
            head_color="#163958";
            headerlay.setBackgroundColor(Color.parseColor("#163958"));
        } else if (catName.equalsIgnoreCase("My Year")) {
            head_color="#4c4b43";
            headerlay.setBackgroundColor(Color.parseColor("#4c4b43"));
        }


    }


    private void fillContanierInitially() {

        if (lastClickedCat.equals("Start")) {
            addMoreMenuButton.setVisibility(View.GONE);
            submitMenuButton.setVisibility(View.GONE);
            homeicon.setVisibility(View.VISIBLE);
            MoneyNow moneyNow = new MoneyNow();
            frgManager = getFragmentManager();
            FragmentTransaction ft = frgManager.beginTransaction();
            ft.add(R.id.container, moneyNow, "moneyNow");
            ft.commit();
        } else if (lastClickedCat.equals("My Year")) {
            addMoreMenuButton.setVisibility(View.GONE);
            submitMenuButton.setVisibility(View.GONE);
            homeicon.setVisibility(View.GONE);
            isSummery(true);
            SummeryFragment summeryFragment = new SummeryFragment();
            frgManager = getFragmentManager();
            FragmentTransaction ft = frgManager.beginTransaction();
            ft.add(R.id.container, summeryFragment, "summeryFragment");
            ft.commit();
        } else {
            addMoreMenuButton.setVisibility(View.VISIBLE);
            submitMenuButton.setVisibility(View.GONE);
            homeicon.setVisibility(View.VISIBLE);
            home = new Home();
            frgManager = getFragmentManager();

            Bundle bundle = new Bundle();
            bundle.putString("catname", lastClickedCat);
            home.setArguments(bundle);

            FragmentTransaction ft = frgManager.beginTransaction();
            ft.add(R.id.container, home, "home");
            ft.commit();
        }

    }

    Home home;

    private void setContainer(String catnamt_str) {

        if (!lastClickedCat.equalsIgnoreCase(catnamt_str)) {
            lastClickedCat = catnamt_str;
            if (catnamt_str.equalsIgnoreCase("Start")) {
                addMoreMenuButton.setVisibility(View.GONE);
                submitMenuButton.setVisibility(View.GONE);
                homeicon.setVisibility(View.VISIBLE);
                isSummery(false);
                Fragment fragment = frgManager.findFragmentByTag("moneyNow");
                if (fragment == null) {
                    removeFragment();
                    MoneyNow moneyNow = new MoneyNow();
                    frgManager = getFragmentManager();
                    FragmentTransaction ft = frgManager.beginTransaction();
                    ft.add(R.id.container, moneyNow, "moneyNow");
                    ft.commit();
                }

            } else if (catnamt_str.equalsIgnoreCase("My Year")) {
                addMoreMenuButton.setVisibility(View.GONE);
                submitMenuButton.setVisibility(View.GONE);
                homeicon.setVisibility(View.GONE);
                isSummery(true);
                Fragment fragment = frgManager.findFragmentByTag("summeryFragment");
                if (fragment == null) {
                    removeFragment();
                    SummeryFragment summeryFragment = new SummeryFragment();
                    frgManager = getFragmentManager();
                    FragmentTransaction ft = frgManager.beginTransaction();
                    ft.add(R.id.container, summeryFragment, "summeryFragment");
                    ft.commit();
                }
            } else {
                addMoreMenuButton.setVisibility(View.VISIBLE);
                submitMenuButton.setVisibility(View.GONE);
                homeicon.setVisibility(View.VISIBLE);
                isSummery(false);
                Fragment fragment = frgManager.findFragmentByTag("home");
                if (fragment == null) {
                    removeFragment();
                    home = new Home();
                    frgManager = getFragmentManager();

                    Bundle bundle = new Bundle();
                    bundle.putString("catname", catnamt_str);
                    home.setArguments(bundle);

                    FragmentTransaction ft = frgManager.beginTransaction();
                    ft.add(R.id.container, home, "home");
                    ft.commit();
                } else {
                    home.setViewAsCategory(catnamt_str);
                }
            }
        }

    }

    public void removeFragment() {

        View view = this.getCurrentFocus();

        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


        Fragment fragment1 = frgManager.findFragmentByTag("moneyNow");
        Fragment fragment2 = frgManager.findFragmentByTag("home");
        Fragment fragment3 = frgManager.findFragmentByTag("spendingOnBasics");
        Fragment fragment4 = frgManager.findFragmentByTag("moveAndKeppTouch");
        Fragment fragment5 = frgManager.findFragmentByTag("clothAndStuff");
        Fragment fragment6 = frgManager.findFragmentByTag("funAndEntertainment");
        Fragment fragment7 = frgManager.findFragmentByTag("holidaysAndMajorBuys");
        Fragment fragment8 = frgManager.findFragmentByTag("repayment");
        Fragment fragment9 = frgManager.findFragmentByTag("summeryFragment");

        if (fragment1 != null) {
            Log.i("fragment1", "fragment1");
            frgManager.beginTransaction().remove(fragment1).commit();
        } else if (fragment2 != null) {
            Log.i("fragment2", "fragment2");
            frgManager.beginTransaction().remove(fragment2).commit();
        } else if (fragment3 != null) {
            Log.i("fragment3", "fragment3");
            frgManager.beginTransaction().remove(fragment3).commit();
        } else if (fragment4 != null) {
            Log.i("fragment4", "fragment4");
            frgManager.beginTransaction().remove(fragment4).commit();
        } else if (fragment5 != null) {
            Log.i("fragment5", "fragment5");
            frgManager.beginTransaction().remove(fragment5).commit();
        } else if (fragment6 != null) {
            Log.i("fragment6", "fragment6");
            frgManager.beginTransaction().remove(fragment6).commit();
        } else if (fragment7 != null) {
            Log.i("fragment7", "fragment7");
            frgManager.beginTransaction().remove(fragment7).commit();
        } else if (fragment8 != null) {
            Log.i("fragment8", "fragment8");
            frgManager.beginTransaction().remove(fragment8).commit();
        } else if (fragment9 != null) {
            Log.i("fragment9", "fragment9");
            frgManager.beginTransaction().remove(fragment9).commit();
        }
    }


    public void isSummery(boolean yesORno) {

        if (yesORno) {
            buttonplantab.setVisibility(View.VISIBLE);
            copyRightLL.setVisibility(View.GONE);
        } else {
            buttonplantab.setVisibility(View.GONE);
            copyRightLL.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onBackPressed() {
        new LogOutDialog().logoutDailogBox(mContext, "Are you sure,  You want to Exit?");

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.settingicon:
                //settingBar();
                Intent adminhome1 = new Intent(mContext, AdminHome.class);
                adminhome1.putExtra("catName", lastClickedCat);
                startActivity(adminhome1);
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;

            case R.id.editImageView:
//                editBar();
                Intent adminhome = new Intent(mContext, TransactionPanel.class);
                adminhome.putExtra("catName", lastClickedCat);
                startActivity(adminhome);
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;
            case R.id.HaTHost:
//                editBar();
                Intent adminForHint = new Intent(mContext, TransactionPanel.class);
                adminForHint.putExtra("catName", "Start");
                startActivity(adminForHint);
                finish();
                break;

            case R.id.usersetting:
                editProfileDialog();
                break;

            case R.id.homeicon:
                headertitle.setText("My Money Planner - " + "My Year");
                setHeaderColor("My Year");
                setContainer("My Year");
                break;

            case R.id.helpicon:
                helpBar();
                break;

            case R.id.addMoreMenuButton:
                if (null != home) {
                    home.addInputView();
                }
                break;

            case R.id.submitMenuButton:
                if (null != home) {
                    home.sumitIncome();
                }
                break;

            case R.id.reseticon:
                resetConfirmation("Are you sure, You want Reset All Data?");
                break;

            case R.id.nextweektarget:
                temp_intervals = 7;
                graphDialog();
                break;

            case R.id.fourweektarget:
                temp_intervals = 28;
                graphDialog();
                break;

            case R.id.howspendmoney:
                getPieChartDialog();
                break;
        }
    }

    private void getPieChartDialog() {

        final Dialog dialog = new Dialog(mContext, R.style.AppTheme);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Window window = dialog.getWindow();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.piechart_dialog_lay);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setWindowAnimations(R.style.DialogAnimation);

        TextView title = (TextView) window.findViewById(R.id.title);
        ImageView cancel = (ImageView) window.findViewById(R.id.cancel);
        pieChartOfCategory = (PieChart) window.findViewById(R.id.pieChartOfCat);

        setPieChart(pieChartOfCategory);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    public void resetData() {
        DaoSession daoSession = BaseManager.getDBSessoin(mContext);

        RepeatTransactionDao repeatTransactionDao = daoSession.getRepeatTransactionDao();
        repeatTransactionDao.deleteAll();

        InOutTransactionDao transactionDao = daoSession.getInOutTransactionDao();
        transactionDao.deleteAll();

        FinancialYearDao financialYearDao = daoSession.getFinancialYearDao();
        financialYearDao.deleteAll();

        InitialBudgetDao initialBudgetDao = daoSession.getInitialBudgetDao();
        initialBudgetDao.deleteAll();

//        DBManager.unCheckAllSource(mContext);
        removeFragment();
        lastClickedCat = "Start";
        setCatAdapter();
        setHeaderColor("Start");
        //setContainer("Start");

        new DayInOutTransactionManager(mContext).insertDayInOut();
    }


    public void settingBar() {

        final PopupWindow changeSortPopUp = new PopupWindow(mContext);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.settingdailog, null);
        LinearLayout adminlay = (LinearLayout) layout.findViewById(R.id.adminlay);
//        LinearLayout transactionpanellay=(LinearLayout)layout.findViewById(R.id.transactionpanellay);


        adminlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSortPopUp.dismiss();
                Intent adminhome = new Intent(mContext, AdminHome.class);
                adminhome.putExtra("catName", lastClickedCat);
                startActivity(adminhome);
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });

       /* transactionpanellay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSortPopUp.dismiss();
                Intent adminhome=new Intent(mContext, TransactionPanel.class);
                adminhome.putExtra("catName",lastClickedCat);
                startActivity(adminhome);
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });*/


        Rect rc = new Rect();
        settingicon.getWindowVisibleDisplayFrame(rc);
        int[] xy = new int[2];
        settingicon.getLocationInWindow(xy);
        rc.offset(xy[0], xy[1]);
        // Creating the PopupWindow
        changeSortPopUp.setAnimationStyle(R.style.animationName);
        changeSortPopUp.setContentView(layout);
        if(isTablet(mContext)) {
            changeSortPopUp.setWidth(200);
            changeSortPopUp.setHeight(100);

        }
        else{
            changeSortPopUp.setWidth(400);
            changeSortPopUp.setHeight(200);
        }

        changeSortPopUp.setFocusable(true);


        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = 0;
        int OFFSET_Y = settingicon.getHeight();

        // Clear the default translucent background
        changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        changeSortPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, rc.left + OFFSET_X, rc.top + OFFSET_Y);

    }


    public Bitmap getBitmap(String filename) {

        Log.i("bit 0124", "" + filename);
        String root = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File myDir = new File(root + "/expressionApp/" + filename);
        if (myDir.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(myDir.getAbsolutePath());
            return myBitmap;
        }

        return null;
    }


    ImageSelector imageSelector = null;
    String userPic = "";
    String userprofpic = "";


    public void editProfileDialog() {

        usersetting.setClickable(false);

        final Dialog dialog = new Dialog(mContext, R.style.AppTheme);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        final Window window = dialog.getWindow();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        //dialog.setContentView(R.layout.editprofiledialog);
        if(isTablet(mContext)) {
            dialog.setContentView(R.layout.editprofiledialog);
        }
        else{
            dialog.setContentView(R.layout.editprofiledialog_phone);
        }

        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setWindowAnimations(R.style.DialogAnimation);

        final LinearLayout prof_header_lay=(LinearLayout)window.findViewById(R.id.prof_header_lay);

        prof_header_lay.setBackgroundColor(Color.parseColor(head_color));

        final EditText firstnametxt = (EditText) window.findViewById(R.id.firstnametxt);
        final EditText lastnametxt = (EditText) window.findViewById(R.id.lastnametxt);
        final CircularImageView imageView_round = (CircularImageView) window.findViewById(R.id.imageView_round);
        final ImageView cancel = (ImageView) window.findViewById(R.id.cancel);
        final TextView submit = (TextView) window.findViewById(R.id.submit);


        UserInfo userInfo = DBManager.getUserInfo(mContext);
        if (userInfo != null) {
            firstnametxt.setText(userInfo.getFirstname().toString());
            lastnametxt.setText(userInfo.getLastname().toString());
            userprofpic = userInfo.getPicname();
            if (userprofpic != null && !userprofpic.equals("")) {
                Bitmap bitmap = getBitmap(userprofpic);

                if (bitmap != null) {
                    imageView_round.setBackground(null);
                    imageView_round.setImageBitmap(bitmap);
                }
            }
        }


        imageView_round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelector = new ImageSelector(mContext, imageView_round, new ImageSelector.ImageListener() {
                    @Override
                    public void getImageName(String imagename) {
                        userPic = imagename;
                    }
                });

                imageSelector.selectImage();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                usersetting.setClickable(true);
                dialog.dismiss();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String firstname = firstnametxt.getText().toString().trim();
                String lastname = lastnametxt.getText().toString().trim();


                if (ValidationUtility.validEditTextString(firstname)) {

                    if (ValidationUtility.validEditTextString(lastname)) {

                        usersetting.setClickable(true);

                        String str = firstname;
                        String cap_firstname = str.substring(0, 1).toUpperCase() + str.substring(1);

                        if (userPic != null && !userPic.equals("")) {
                            DBManager.UpdateUserInfo(mContext, cap_firstname, lastname, userPic);
                            Bitmap bitmap = getBitmap(userPic);
                            if (bitmap != null) {
                                usersetting.setBackground(null);
                                usersetting.setImageBitmap(bitmap);
                            }

                        } else {
                            DBManager.UpdateUserInfo(mContext, cap_firstname, lastname, "");
                        }

                        setUserName();
                        Toast.makeText(mContext, "Profile Updated.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    } else {
                        lastnametxt.setError("Enter Last Name");
                        //Toast.makeText(mContext,"Enter Last Name.",Toast.LENGTH_LONG).show();
                    }

                } else {
                    firstnametxt.setError("Enter First Name");
                    //Toast.makeText(mContext,"Enter First Name.",Toast.LENGTH_LONG).show();
                }
            }
        });


        dialog.show();
    }

    BarChart weekchartchart;

    public void graphDialog() {
        final Dialog dialog = new Dialog(mContext, R.style.AppTheme);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Window window = dialog.getWindow();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.graph_dialog_lay);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setWindowAnimations(R.style.DialogAnimation);

        TextView title = (TextView) window.findViewById(R.id.title);
        ImageView cancel = (ImageView) window.findViewById(R.id.cancel);
        weekchartchart = (BarChart) window.findViewById(R.id.weekchartchart);

        if (temp_intervals == 7) {
            title.setText("My 1 Week Target");
            try {
                setChartData(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (temp_intervals == 28) {
            title.setText("My 4 Week Target");
            try {
                setChartData(4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    ArrayList<BarEntry> barEntriesIncom, barEntriesExpend;
    ArrayList<String> BarEntryLabels;

    CustomBarDataSet barDataSetIncome/*,barDataSetExpend*/;

    BarData barData;

    //    List<String> monthlyIncome;
//    List<String> monthlyExpends;
    private List<DayInOutSummery> dayInOutSummeries;

    LinkedHashMap<String, Double> catExpHashMap;

    public void setChartData(int typOfChart) {
        barEntriesIncom = new ArrayList<>();
        barEntriesExpend = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();
        new InitBackground(typOfChart).execute();
    }

    public void setPieChart(PieChart pieChartOfCategory) {
        catExpHashMap = new LinkedHashMap<>();
        new InitPieChart().execute(pieChartOfCategory);
    }

    class InitBackground extends AsyncTask<Void, Void, Void> {
        int typOfChart = 0;

        public InitBackground(int typOfChart) {
            this.typOfChart = typOfChart;
        }

        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = initProgressDialog();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getMonthluInOut();
           /* incomeData();
            expendData();*/
            dayTotalInOut(typOfChart);
            AddValuesToBarEntryLabels(typOfChart);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            try {
                setChartProp(typOfChart);
                initChartData();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public ProgressDialog initProgressDialog() {
        String s = "Please wait...";
        SpannableString ss2 = new SpannableString(s);
        ss2.setSpan(new RelativeSizeSpan(1.3f), 0, ss2.length(), 0);
        ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, ss2.length(), 0);
        ProgressDialog progressDialog = new ProgressDialog(mContext,
                android.R.style.Theme_DeviceDefault_Light_Dialog);
        Window window = progressDialog.getWindow();
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setMessage(ss2);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        return progressDialog;
    }


    class InitPieChart extends AsyncTask<PieChart, Void, PieChart> {


        private PieChart pieChartInIt;

        @Override
        protected PieChart doInBackground(PieChart... params) {
            pieChartInIt = configureChart(params[0]);
            return pieChartInIt;
        }

        @Override
        protected void onPostExecute(PieChart pieChart) {
            pieChart = setPieChartData(pieChart);
            pieChart.animateXY(1500, 1500);
        }
    }

    private PieChart setPieChartData(PieChart chart) {
        LinkedHashMap<String, Double> catExpHashMap = getCategoryList();
        double total = GraphTranCalManager.getTotal(HostActivty.this);

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> KeyList = new ArrayList<String>(catExpHashMap.keySet());
        ArrayList<String> xVals = new ArrayList<String>();

        ArrayList<Double> poundDataList = new ArrayList<>(catExpHashMap.values());
        if (null != catExpHashMap && catExpHashMap.size() > 0) {
            for (int i = 0; i < catExpHashMap.size(); i++) {
                if (poundDataList.get(i) > 0) {
                    Double percentage = ((poundDataList.get(i).doubleValue() / total) * 100);
                    DecimalFormat df = new DecimalFormat("#,###");


                    String cat_name= KeyList.get(i);

                    if(cat_name.equalsIgnoreCase("My Income"))
                        cat_name="Income";

                    else if(cat_name.equalsIgnoreCase("The Basics"))
                        cat_name="Basic Expenditure";

                    else if(cat_name.equalsIgnoreCase("Major Buys"))
                        cat_name="Major Purchases";

                    else if(cat_name.equalsIgnoreCase("Keeping Mobile"))
                        cat_name="Savings";



                    yVals1.add(new Entry(percentage.floatValue(), i, cat_name+ " £" + df.format(Math.round(poundDataList.get(i).doubleValue()))));
                    xVals.add("");
                }
            }


            PieDataSet set1 = new PieDataSet(yVals1, "");
            set1.setSliceSpace(0f);
            set1.setValueTextSize(14f);
            ArrayList<Integer> colors = new ArrayList<Integer>();
            Random rnd = new Random();
            int rgbColor;
            for (int i = 0; i < catExpHashMap.size(); i++) {
                rgbColor = Color.argb(160, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                colors.add(rgbColor);
            }

            set1.setColors(colors);
            PieData data = new PieData(xVals, set1);
            data.setDataSet(set1);
            data.getFirstRight();
            chart.setData(data);
            chart.isDrawSliceTextEnabled();
            data.setValueTextSize(14f);
            data.setValueFormatter(new PercentValueFormatter());
            chart.highlightValues(null);
            chart.invalidate();
        }
        return chart;
    }

    public class PercentValueFormatter implements ValueFormatter {

        public PercentValueFormatter() {
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return entry.getData() + " (" + Math.round(value) + "%)";
        }
    }

    public class xAxisFromatter implements XAxisValueFormatter {

        @Override
        public String getXValue(String original, int index, ViewPortHandler viewPortHandler) {
            return original;
        }
    }

    private LinkedHashMap<String, Double> getCategoryList() {
        return catExpHashMap = GraphTranCalManager.getCategoryList(mContext, "expend");
    }

    public PieChart configureChart(PieChart chart) {
        chart.setHoleColor(getResources().getColor(android.R.color.background_dark));
        chart.setHoleRadius(60f);
        chart.setDescription("");
        chart.setTransparentCircleRadius(5f);
//        chart.setDrawYValues(true);
        chart.setDrawCenterText(true);
        chart.setDrawHoleEnabled(false);
        chart.setRotationAngle(0);
//        chart.setDrawXValues(false);
        chart.setRotationEnabled(true);
        chart.setUsePercentValues(true);
        return chart;
    }

    public void getMonthluInOut() {

        try {
//            // mothly income
//            monthlyIncome = GraphTranCalManager.getWeeklyIcome(mContext, "income", temp_intervals);
//            Log.i("month 0178", "" + monthlyIncome.toString());
//
//            // monthly expends
//            monthlyExpends = GraphTranCalManager.getWeeklyIcome(mContext, "expend", temp_intervals);
//            Log.i("month 01780", "" + monthlyExpends.toString());
/*            monthlyIncome=BugetManager.testfun(mContext,"income");
            monthlyExpends=BugetManager.testfun(mContext,"expend");*/
            dayInOutSummeries = new DayInOutTransactionManager(mContext).getDayInOutSummery();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void dayTotalInOut(int typOfChart) {

        try {
//            DecimalFormat df = new DecimalFormat("0.00");
//            float initial_money = (float) (getInitialMoney());
//            float temp_total = 0;
//            for (int t = 0; t < monthlyIncome.size(); t++) {
//
//                float indata = new Float(df.format(new Double(monthlyIncome.get(t))));
//                float outdata = new Float(df.format(new Double(monthlyExpends.get(t))));
//
//                temp_total = ((indata) - outdata) + temp_total;
//
//                float total = new Float(df.format(new Double(temp_total + initial_money)));
//                Log.i("1345325", "" + total);
//                barEntriesIncom.add(new BarEntry(total, t));
            DecimalFormat df = new DecimalFormat("0.00");
            float initial_money = (float) (getInitialMoney());
            float temp_total = 0;

            int count = 0;
            switch (typOfChart) {
                case 1:
                    for (int i = 0; i < 7; i++) {
                        float total = new Float(df.format(new Double(dayInOutSummeries.get(i).getBalance_in_hand())));
                        barEntriesIncom.add(new BarEntry(total, count));
                        Log.d("nehal: amt 1week", dayInOutSummeries.get(i).getDate() + " " + dayInOutSummeries.get(i).getBalance_in_hand());
                        count++;
                    }
                    break;
                case 4:
                    for (int i = 0; i < 28; i++) {
                        float total = new Float(df.format(new Double(dayInOutSummeries.get(i).getBalance_in_hand())));
                        barEntriesIncom.add(new BarEntry(total, count));
                        Log.d("nehal: amt 4week", dayInOutSummeries.get(i).getDate() + " " + dayInOutSummeries.get(i).getBalance_in_hand());
                        count++;
                    }
                    break;
            }

//
               /* if(total>0){
                    barEntriesIncom.add(new BarEntry(total, t));
                    barEntriesExpend.add(new BarEntry(0f,t));
                }
                else if(total<0){
                    barEntriesIncom.add(new BarEntry(0f, t));
                   // barEntriesExpend.add(new BarEntry(Math.abs(total),t));
                    barEntriesExpend.add(new BarEntry(total,t));
                }

                else {
                    barEntriesIncom.add(new BarEntry(0f, t));
                    barEntriesExpend.add(new BarEntry(0f,t));
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getInitialMoney() {
        Double initial_money = 0.0;
        List<InitialBudget> initialBudgets = InitialBudgetManager.getInitialBudget(mContext);

        if (initialBudgets != null && initialBudgets.size() > 0) {
            InitialBudget startMoneyBuget = initialBudgets.get(0);
            initial_money = startMoneyBuget.getPound();
        }

        return initial_money;
    }


    public void AddValuesToBarEntryLabels(int typOfChart) {
        try {

            Calendar calendar = Calendar.getInstance();
            Date temp_StartDate = getBugetYearStartDate(mContext);
            calendar.setTime(temp_StartDate);

            int day = calendar.get(Calendar.DAY_OF_WEEK);
//            int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);
            int day_of_month = calendar.getActualMaximum(Calendar.DATE);
            int date = calendar.get(Calendar.DATE);
            int month_bar = calendar.get(Calendar.MONTH);

            int flag = day;
            int totalCount = 0; /*= dayInOutSummeries.size();*/
            switch (typOfChart) {
                case 1:
                    totalCount = 7;
                    break;
                case 4:
                    totalCount = 28;
                    break;
            }

            while (totalCount > 0) {
//                Log.i("month90192", "" + flag + "   " + getWeekForInt(flag));
                switch (typOfChart) {
                    case 1:
                        if(month_bar==12)
                            month_bar=0;
                        BarEntryLabels.add(getWeekForInt(flag) + " " + getTwoDigits(date) + "-" + getTwoDigits(month_bar + 1));
                        break;
                    case 4:
                        if(month_bar==12)
                            month_bar=0;
                        BarEntryLabels.add(getWeekForInt(flag) + "  " + getTwoDigits(date) + "-" + getTwoDigits(month_bar + 1));
                        break;
                }
                if (date == day_of_month) {
                    date = 1;
                    month_bar += 1;
                    if (month_bar == Calendar.DECEMBER) {
                        month_bar = 0;
                    }
                } else {
                    date++;
                }
                if (flag == 7)
                    flag = 1;
                else {
                    flag++;
                }

                totalCount--;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getTwoDigits(int month) {
        return String.format("%02d", month);
    }

    String getWeekForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getShortWeekdays();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    Date bugetStartDate = null;
    Date bugetEndDate = null;

    public void setChartProp(int typOfChart) {

        final int[] LEGEND_COLORS = {
                Color.rgb(76, 153, 0), Color.rgb(255, 0, 0), Color.rgb(255, 255, 255)
        };

        startEndDate();

        Legend legend = weekchartchart.getLegend();
        legend.setCustom(LEGEND_COLORS, new String[]{"Positive Balance", "Negative Balance", DateUtility.getStringBYDate(bugetStartDate) + "  To  " + DateUtility.getStringBYDate(bugetEndDate)});
        weekchartchart.getLegend().setEnabled(true);

        weekchartchart.setDescription("");
        weekchartchart.setExtraBottomOffset(10);
        // Hide the legend
        weekchartchart.setPinchZoom(false);
        weekchartchart.setDoubleTapToZoomEnabled(false);
        // to hide background lines
        weekchartchart.getXAxis().setDrawGridLines(false);
        //weekchartchart.setOnChartValueSelectedListener(new ChartListener());

        XAxis xAxis = weekchartchart.getXAxis();
        if (typOfChart == 4)
            xAxis.setLabelRotationAngle(270);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new xAxisFromatter());
        xAxis.setLabelsToSkip(0);
        xAxis.setTextSize(14f);


        YAxis yAxis_right = weekchartchart.getAxisRight();
        yAxis_right.setDrawLabels(false);

        YAxis yAxis = weekchartchart.getAxisLeft();

        yAxis.setValueFormatter(new ChartValueFormatter());
        yAxis.setStartAtZero(false);
        yAxis.setTextSize(14f);
    }

    public void startEndDate() {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date todayDate = new Date();
        // bugetStartDate=DateUtility.getDateBYStringDate(format.format(todayDate));
        bugetStartDate = getBugetYearStartDate(mContext);

        Calendar cal = null;

        // after interval
        try {
            Date date = format.parse(format.format(bugetStartDate));
            cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, temp_intervals - 1);
            bugetEndDate = cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static final int[] INCOME_COLORS = {
            Color.rgb(76, 153, 0)
    };

    public static final int[] EXPEND_COLORS = {
            Color.rgb(255, 0, 0)
    };


    public void initChartData() {

        barDataSetIncome = new CustomBarDataSet(barEntriesIncom, "");
        barDataSetIncome.setColors(new int[]{Color.rgb(255, 0, 0),
                Color.rgb(76, 153, 0)});


        List<IBarDataSet> datasets = new ArrayList<IBarDataSet>();
        datasets.add(barDataSetIncome);

//        datasets.add(barDataSetExpend);


        barData = new BarData(BarEntryLabels, datasets);
        // barData.setValueFormatter(new Chartdataformatter()); // tp set float data
        barData.setDrawValues(false);
        barData.setGroupSpace(0f);
        weekchartchart.setData(barData);
        weekchartchart.animateY(500);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageSelector.onActivityResult(requestCode, resultCode, data);
    }


    public void resetConfirmation(final String msg) {
        final Dialog dialog = new Dialog(mContext);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmdialog);
        window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
        if(isTablet(mContext)) {
            window.setLayout(450, ActionBar.LayoutParams.WRAP_CONTENT);
        }

        else{
            window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        }
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvMsg = (TextView) window.findViewById(R.id.msg_yesno);
        TextView tvNO = (TextView) window.findViewById(R.id.no_yesno);
        TextView tvYes = (TextView) window.findViewById(R.id.yes_yesno);
        tvMsg.setText(msg);

        tvNO.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                resetData();
                dialog.dismiss();
                Toast.makeText(mContext, "Reset Done.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public static Date getBugetYearStartDate(Context context) {
        FinancialYear financialYear = null;
        Date startDate = null;
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



    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }


    public void helpBar() {

        final Dialog dialog = new Dialog(mContext, R.style.AppTheme);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        final Window window = dialog.getWindow();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        //dialog.setContentView(R.layout.editprofiledialog);
        if(isTablet(mContext)) {
            dialog.setContentView(R.layout.helpdailog);
        }
        else{
            dialog.setContentView(R.layout.helpdailog);
        }

        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setWindowAnimations(R.style.DialogAnimation);

        final LinearLayout prof_header_lay=(LinearLayout)window.findViewById(R.id.prof_header_lay);

        prof_header_lay.setBackgroundColor(Color.parseColor(head_color));


        final ImageView cancel = (ImageView) window.findViewById(R.id.cancel);
        final WebView help_text=(WebView)window.findViewById(R.id.help_text);

        String help_str="";
        if (lastClickedCat.equalsIgnoreCase("Start")) {

            help_text.loadUrl("file:///android_asset/mymoneynow.html");
        } else if (lastClickedCat.equalsIgnoreCase("My Income")
                ||lastClickedCat.equalsIgnoreCase("The Basics")
                ||lastClickedCat.contains("Keeping Mobile")
                ||lastClickedCat.contains("Clothes and Stuff")
                ||lastClickedCat.contains("Entertainment")
                ||lastClickedCat.contains("Major Buys")
                ||lastClickedCat.contains("Repayments")) {
            help_text.loadUrl("file:///android_asset/myIncome.html");
        } else if (lastClickedCat.equalsIgnoreCase("My Year")) {
            help_text.loadUrl("file:///android_asset/myyear.html");
        }









        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                usersetting.setClickable(true);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
