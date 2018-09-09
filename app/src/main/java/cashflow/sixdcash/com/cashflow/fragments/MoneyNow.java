package cashflow.sixdcash.com.cashflow.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cashflow.sixdcash.com.cashflow.HostActivty;
import cashflow.sixdcash.com.cashflow.R;
import cashflow.sixdcash.com.cashflow.Utility.DateUtility;
import cashflow.sixdcash.com.cashflow.Utility.ValidationUtility;
import cashflow.sixdcash.com.cashflow.manager.BaseManager;
import cashflow.sixdcash.com.cashflow.manager.DBManager;
import cashflow.sixdcash.com.cashflow.manager.DayInOutTransactionManager;
import cashflow.sixdcash.com.cashflow.manager.FinancialYearManager;
import cashflow.sixdcash.com.cashflow.manager.InitialBudgetManager;
import cashflowdb.DaoSession;
import cashflowdb.FinancialYear;
import cashflowdb.FinancialYearDao;
import cashflowdb.InOutTransactionDao;
import cashflowdb.InitialBudget;
import cashflowdb.InitialBudgetDao;
import cashflowdb.RepeatTransactionDao;

/**
 * Created by Praveen on 9/16/2016.
 */
public class MoneyNow extends Fragment implements View.OnClickListener {

    Context mContext;
    View view;

    Button submitmoneyplan;

    //financial year
    TextView fincialstartdate, fincialenddate;
    Button fincialsubmit;

    // money at start
    TextView startdate, moneycommenttxt;
    EditText howmuchpound;
    Button submit;
    LinearLayout startmoneycomment, mymoneycommentlay;


    // my goal
    TextView startdateofmygoal, goalcommenttxt;
    EditText howmuchpoundofmygoal;
    Button submitofmygoal;
    LinearLayout goalcomment, goalcommentlay;

    WebView welcome_view;


    // what i owe
    TextView startdateofwhatiwoe, owecommenttxt;
    EditText howmuchpoundofwhatiwoe;
    Button submitofwhatiwoe;
    LinearLayout owecomment, owecommentlay;
    EditText comt_txt_new;
    private Date currentDate = null;
    private Date preStartDate = null;
    private String goal_cmt_txt_new="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //view = inflater.inflate(R.layout.monerynowview, container, false);
        if(isTablet(mContext)) {
            view = inflater.inflate(R.layout.monerynowview, container, false);
        }
        else{
            view = inflater.inflate(R.layout.monerynowview_phone, container, false);
        }

        init();
        return view;
    }

    public void init() {

        welcome_view=(WebView)view.findViewById(R.id.welcome_view);
        welcome_view.loadUrl("file:///android_asset/welcome.html");
        comt_txt_new = (EditText) view.findViewById(R.id.comt_txt);


        submitmoneyplan = (Button) view.findViewById(R.id.submitmoneyplan);
        submitmoneyplan.setOnClickListener(this);

        // financial start

        fincialstartdate = (TextView) view.findViewById(R.id.fincialstartdate);
        fincialenddate = (TextView) view.findViewById(R.id.fincialenddate);
        fincialsubmit = (Button) view.findViewById(R.id.fincialsubmit);

        fincialstartdate.setOnClickListener(this);
        fincialenddate.setOnClickListener(this);
        fincialsubmit.setOnClickListener(this);


        // money at start

        final TextView startpoundsym = (TextView) view.findViewById(R.id.startpoundsym);
        final TextView startpencesym = (TextView) view.findViewById(R.id.startpencesym);

        mymoneycommentlay = (LinearLayout) view.findViewById(R.id.mymoneycommentlay);
        moneycommenttxt = (TextView) view.findViewById(R.id.moneycommenttxt);


        howmuchpound = (EditText) view.findViewById(R.id.howmuchpound);
        startdate = (TextView) view.findViewById(R.id.startdate);
        submit = (Button) view.findViewById(R.id.submit);
        startmoneycomment = (LinearLayout) view.findViewById(R.id.startmoneycomment);


        howmuchpound.addTextChangedListener(new TextWatcherForEdit(startpoundsym, startpencesym, howmuchpound));
        howmuchpound.setOnClickListener(this);
        startdate.setOnClickListener(this);
        submit.setOnClickListener(this);
        startmoneycomment.setOnClickListener(this);

        // my goal

        final TextView goalpoundsym = (TextView) view.findViewById(R.id.goalpoundsym);
        final TextView goalpencesym = (TextView) view.findViewById(R.id.goalpencesym);

        goalcommentlay = (LinearLayout) view.findViewById(R.id.goalcommentlay);
        goalcommenttxt = (TextView) view.findViewById(R.id.goalcommenttxt);

        howmuchpoundofmygoal = (EditText) view.findViewById(R.id.howmuchpoundofmygoal);
        startdateofmygoal = (TextView) view.findViewById(R.id.startdateofmygoal);
        submitofmygoal = (Button) view.findViewById(R.id.submitofmygoal);
        goalcomment = (LinearLayout) view.findViewById(R.id.goalcomment);

        howmuchpoundofmygoal.addTextChangedListener(new TextWatcherForEdit(goalpoundsym, goalpencesym, howmuchpoundofmygoal));
        howmuchpoundofmygoal.setOnClickListener(this);
        startdateofmygoal.setOnClickListener(this);
        submitofmygoal.setOnClickListener(this);
        goalcomment.setOnClickListener(this);


        // what i woe

        final TextView owepoundsym = (TextView) view.findViewById(R.id.owepoundsym);
        final TextView owegoalpencesym = (TextView) view.findViewById(R.id.owegoalpencesym);

        owecommentlay = (LinearLayout) view.findViewById(R.id.owecommentlay);
        owecommenttxt = (TextView) view.findViewById(R.id.owecommenttxt);

        howmuchpoundofwhatiwoe = (EditText) view.findViewById(R.id.howmuchpoundofwhatiwoe);
        startdateofwhatiwoe = (TextView) view.findViewById(R.id.startdateofwhatiwoe);
        submitofwhatiwoe = (Button) view.findViewById(R.id.submitofwhatiwoe);
        owecomment = (LinearLayout) view.findViewById(R.id.owecomment);

        howmuchpoundofwhatiwoe.addTextChangedListener(new TextWatcherForEdit(owepoundsym, owegoalpencesym, howmuchpoundofwhatiwoe));
        howmuchpoundofwhatiwoe.setOnClickListener(this);
        startdateofwhatiwoe.setOnClickListener(this);
        submitofwhatiwoe.setOnClickListener(this);
        owecomment.setOnClickListener(this);


        setFinancialYear();
        setValues();

    }


    public void setFinancialYear() {

        List<FinancialYear> financialYears = FinancialYearManager.getFinancialYear(mContext);
        if (financialYears != null && financialYears.size() > 0) {

            String srartDate = "";
            String endDate = "";
            for (int i = 0; i < financialYears.size(); i++) {
                FinancialYear financialYear = financialYears.get(i);
                if (financialYear.getCompeted().equals("0")) {

                    srartDate = DateUtility.getStringBYDate(financialYear.getStartdate());
                    endDate = DateUtility.getStringBYDate(financialYear.getEnddate());
                    //fincialstartdate.setText("" + srartDate);
                    //fincialenddate.setText("" + endDate);
                    break;
                }

            }

            if (srartDate.equals("") || endDate.equals("")) {
                Toast.makeText(mContext, "Please Create Initial Plan at start", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(mContext, "Please Create Initial Plan at start", Toast.LENGTH_LONG).show();
        }

    }

    String setStartcmt;
    String setgoalcmt;
    String setowecmt;

    public void setValues() {

        List<InitialBudget> initialBudgets = InitialBudgetManager.getInitialBudget(mContext);

        Log.i("initialBudgets size232", "" + initialBudgets.size());
        if (initialBudgets != null && initialBudgets.size() > 0) {

            for (int i = 0; i < initialBudgets.size(); i++) {
                InitialBudget initialBudget = initialBudgets.get(i);
                if (initialBudget.getBugetID().equals("1")) {

                    String aDouble = "";
                    try {
                        DecimalFormat df = new DecimalFormat("0.00");
                        Double money = initialBudget.getPound();
                        aDouble = df.format(money);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String howmuchp = aDouble;
                    String startdat = DateUtility.getStringBYDate(initialBudget.getStartdate());

                    setStartcmt = initialBudget.getComment();

                    if (setStartcmt != null && !setStartcmt.equals("")) {
                        moneycommenttxt.setText(setStartcmt.toString());
                        mymoneycommentlay.setVisibility(View.VISIBLE);
                    }

                    if (setStartcmt != null)
                        startmoneycomment.setTag(setStartcmt);

                    howmuchpound.setText(howmuchp);
                    startdate.setText(startdat);
                } else if (initialBudget.getBugetID().equals("2")) {

                    String aDouble = "";
                    try {
                        DecimalFormat df = new DecimalFormat("0.00");
                        Double money = initialBudget.getPound();
                        aDouble = df.format(money);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String howmuchp = aDouble;
                    String startdat = DateUtility.getStringBYDate(initialBudget.getStartdate());
                    setgoalcmt = initialBudget.getComment();

                    if (setgoalcmt != null && !setgoalcmt.equals("")) {
                       /* goalcommenttxt.setText(setgoalcmt.toString());
                        goalcommentlay.setVisibility(View.VISIBLE);*/
                        comt_txt_new.setText(setgoalcmt);
                    }

                    if (setgoalcmt != null)
                        goalcomment.setTag(setgoalcmt);

                    howmuchpoundofmygoal.setText(howmuchp);
                    startdateofmygoal.setText(startdat);

                } else if (initialBudget.getBugetID().equals("3")) {
                    String aDouble = "";
                    try {
                        DecimalFormat df = new DecimalFormat("0.00");
                        Double money = initialBudget.getPound();
                        aDouble = df.format(money);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String howmuchp = aDouble;
                    String startdat = DateUtility.getStringBYDate(initialBudget.getStartdate());
                    setowecmt = initialBudget.getComment();


                    if (setowecmt != null && !setowecmt.equals("")) {
                        owecommenttxt.setText(setowecmt.toString());
                        owecommentlay.setVisibility(View.VISIBLE);
                    }

                    if (setowecmt != null)
                        owecomment.setTag(setowecmt);

                    howmuchpoundofwhatiwoe.setText(howmuchp);
                    startdateofwhatiwoe.setText(startdat);
                }
            }

        }

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (Context) activity;
    }

    PopupWindow changeSortPopUp;

    public void amoutInputDialog(final TextView howmuchpound, final String poundORpence) {
        changeSortPopUp = new PopupWindow(mContext);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.inputseekbar, null);
        SeekBar seek = (SeekBar) layout.findViewById(R.id.seek);
        TextView maxval = (TextView) layout.findViewById(R.id.maxval);
        String temp = "";


        if (poundORpence.equals("pound")) {
            maxval.setText("\u00A3 500");
            seek.setMax(500);
            temp = howmuchpound.getText().toString();
            if (temp != null && !temp.equalsIgnoreCase("")) {
                int current_money = Integer.parseInt(temp);
                seek.setProgress(current_money);
            }
        } else {
            maxval.setText("\u00A3 100");
            temp = howmuchpound.getText().toString();
            if (temp != null && !temp.equalsIgnoreCase("")) {
                int current_money = Integer.parseInt(temp);
                seek.setProgress(current_money);
            }
            seek.setMax(100);
        }

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (poundORpence.equals("pound"))
                    howmuchpound.setText("" + progress);
                else
                    howmuchpound.setText("" + progress);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Rect rc = new Rect();
        howmuchpound.getWindowVisibleDisplayFrame(rc);
        int[] xy = new int[2];
        howmuchpound.getLocationInWindow(xy);
        rc.offset(xy[0], xy[1]);
        // Creating the PopupWindow


        changeSortPopUp.setAnimationStyle(R.style.animationName);
        changeSortPopUp.setContentView(layout);
        int wid = howmuchpound.getWidth();
        changeSortPopUp.setWidth(wid * 5);
        //changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setHeight(60);
        changeSortPopUp.setFocusable(true);


        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = -(wid / 2);
        int OFFSET_Y = howmuchpound.getHeight();

        // Clear the default translucent background
        changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        changeSortPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, rc.left + OFFSET_X, rc.top + OFFSET_Y);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            // financial year

            case R.id.fincialstartdate:
                initdate(fincialstartdate, fincialenddate);
                break;
            case R.id.fincialenddate:
                //initdate(fincialenddate);
                break;
            case R.id.fincialsubmit:
                //insertFinancialYear();
                break;


            // money at start
            case R.id.howmuchpound:
                //amoutInputDialog(howmuchpound, "pound");
                break;


            case R.id.startdate:
                initdate(startdate, null);
                break;

            case R.id.submit:
                //insertInitalMoney();

                break;

            case R.id.startmoneycomment:
                showdialog(startmoneycomment, mymoneycommentlay, moneycommenttxt);
                break;


            // my goal

            case R.id.howmuchpoundofmygoal:
                //amoutInputDialog(howmuchpoundofmygoal, "pound");
                break;


            case R.id.startdateofmygoal:
                initdate(startdateofmygoal, null);
                break;

            case R.id.submitofmygoal:
                //insertGoalMoney();
                break;
            case R.id.goalcomment:
                showdialog(goalcomment, goalcommentlay, goalcommenttxt);
                break;

            // what i woe


            case R.id.howmuchpoundofwhatiwoe:
                //amoutInputDialog(howmuchpoundofwhatiwoe,"pound");
                break;


            case R.id.startdateofwhatiwoe:
                initdate(startdateofwhatiwoe, null);
                break;

            case R.id.submitofwhatiwoe:
                //insertOweMoney();
                break;

            case R.id.owecomment:
                showdialog(owecomment, owecommentlay, owecommenttxt);
                break;

            case R.id.submitmoneyplan:
                //insertFinancialYear();
                insertInitalMoney();

                break;
        }
    }

    String fincialstartdt;
    String fincialenddt;
//    public void insertFinancialYear(){
//        fincialstartdt=fincialstartdate.getText().toString();
//        fincialenddt=fincialenddate.getText().toString();
//
//
//        if(fincialstartdt!=null &&  !fincialstartdt.equalsIgnoreCase("")){
//            if(fincialenddt!=null &&  !fincialenddt.equalsIgnoreCase("")){
//                insertInitalMoney();
////                new DayInOutTransactionManager(mContext).insertDayInOut();
//                //Toast.makeText(mContext,"Financial Year Created.",Toast.LENGTH_LONG).show();
//            }
//            else {
//                Toast.makeText(mContext,"Please enter Year End Date.",Toast.LENGTH_LONG).show();}
//        }
//        else {
//            Toast.makeText(mContext,"Please enter Year Start Date.",Toast.LENGTH_LONG).show();
//        }
//    }

    String howmuchp;
    String startdat;

    String goalhowmuchp;
    String goalstartdat;


    String owehowmuchp;
    String owestartdat;

    public void insertInitalMoney() {


        goal_cmt_txt_new = comt_txt_new.getText().toString();

        howmuchp = howmuchpound.getText().toString();
        startdat = startdate.getText().toString();

        fincialstartdt = startdat;
        if (fincialstartdt != null && !fincialstartdt.equals("")) {
            fincialenddt = getYearEndDate(fincialstartdt);
        }
        preStartDate = HostActivty.getBugetYearStartDate(mContext);

        currentDate = DateUtility.getDateBYStringDate(startdat);
        int diffInDays=0;
        if (null!=preStartDate && (!preStartDate.equals(""))&&null!=currentDate && (!currentDate.equals(""))) {
            diffInDays = (int) ((currentDate.getTime() - preStartDate.getTime()) / (1000 * 60 * 60 * 24));
        }

        if (diffInDays==0) {
            insertInitialMoneyDB(0);
        }else {
            resetConfirmation("This action will reset all income and expenses. Are you sure you wish to reset?");
        }
    }

    private void insertInitialMoneyDB(int dateDiff) {
        if (howmuchp != null && !howmuchp.equalsIgnoreCase("")) {

            if (startdat != null && !startdat.equalsIgnoreCase("")) {


                String strcmt = "";
                String goalcmt = "";

                if (startmoneycomment.getTag() != null)
                    strcmt = startmoneycomment.getTag().toString();


                FinancialYearManager.insertFinalcialStartANDEnd(mContext, fincialstartdt, fincialenddt);
                InitialBudgetManager.insertInitailMoney(mContext, "initailmoney", howmuchp, startdat, strcmt);
                InitialBudgetManager.insertInitailMoney(mContext, "goalmoney", "0.0", "", goal_cmt_txt_new);
                if(dateDiff==0) {
                    Toast.makeText(mContext, "Submitted Initial Plan.", Toast.LENGTH_LONG).show();
                }


                // formate at two decimal places
                String aDouble1 = "", aDouble2 = "";
                DecimalFormat df = new DecimalFormat("0.00");
                try {
                    aDouble1 = df.format(Double.parseDouble(howmuchp));
                    aDouble2 = df.format(Double.parseDouble(goalhowmuchp));


                    howmuchpound.setText("");
                    howmuchpound.setText(aDouble1);

                    howmuchpoundofmygoal.setText("");
                    howmuchpoundofmygoal.setText(aDouble2);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                new DayInOutTransactionManager(mContext).insertDayInOut();


            } else
                Toast.makeText(mContext, "Please enter start date of start money.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "Please enter start money.", Toast.LENGTH_LONG).show();
        }
    }

    public void resetConfirmation(final String msg) {
        final Dialog dialog = new Dialog(mContext);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmdialog);
        window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
        window.setLayout(450, ActionBar.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tvMsg = (TextView) window.findViewById(R.id.msg_yesno);
        TextView tvNO = (TextView) window.findViewById(R.id.no_yesno);
        TextView tvYes = (TextView) window.findViewById(R.id.yes_yesno);
        tvMsg.setText(msg);

        tvNO.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(null!=preStartDate && (!preStartDate.equals(""))) {
                    startdate.setText(DateUtility.getStringBYDate(preStartDate));
                }
                dialog.dismiss();
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                resetData();
                dialog.dismiss();
                Toast.makeText(mContext, "Reset successful.Please reinsert your income and expanse.", Toast.LENGTH_SHORT).show();
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

        insertInitialMoneyDB(1);
    }
    public void insertGoalMoney() {
        goalhowmuchp = howmuchpoundofmygoal.getText().toString();
        goalstartdat = startdateofmygoal.getText().toString();

        if (goalhowmuchp != null && !goalhowmuchp.equalsIgnoreCase("")) {

            if (goalstartdat != null && !goalstartdat.equalsIgnoreCase("")) {

                //insertOweMoney();

                String strcmt = "";
                String goalcmt = "";

                if (startmoneycomment.getTag() != null)
                    strcmt = startmoneycomment.getTag().toString();
                if (goalcomment.getTag() != null)
                    goalcmt = goalcomment.getTag().toString();


                Log.i("793", "" + fincialstartdt + "  " + fincialenddt);


                FinancialYearManager.insertFinalcialStartANDEnd(mContext, fincialstartdt, fincialenddt);
                InitialBudgetManager.insertInitailMoney(mContext, "initailmoney", howmuchp, startdat, strcmt);
                InitialBudgetManager.insertInitailMoney(mContext, "goalmoney", goalhowmuchp, goalstartdat, goalcmt);
                Toast.makeText(mContext, "Submitted Initial Plan.", Toast.LENGTH_LONG).show();


                // formate at two decimal places
                String aDouble1 = "", aDouble2 = "";
                DecimalFormat df = new DecimalFormat("0.00");
                try {
                    aDouble1 = df.format(Double.parseDouble(howmuchp));
                    aDouble2 = df.format(Double.parseDouble(goalhowmuchp));


                    howmuchpound.setText("");
                    howmuchpound.setText(aDouble1);

                    howmuchpoundofmygoal.setText("");
                    howmuchpoundofmygoal.setText(aDouble2);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else
                Toast.makeText(mContext, "Please enter goal start date.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "Please enter goal money.", Toast.LENGTH_LONG).show();
        }
    }


    public void insertOweMoney() {
        owehowmuchp = howmuchpoundofwhatiwoe.getText().toString();
        owestartdat = startdateofwhatiwoe.getText().toString();

        if (owehowmuchp != null && !owehowmuchp.equalsIgnoreCase("")) {

            if (owestartdat != null && !owestartdat.equalsIgnoreCase("")) {

                String strcmt = "";
                String goalcmt = "";
                String owecmt = "";

                if (startmoneycomment.getTag() != null)
                    strcmt = startmoneycomment.getTag().toString();
                if (goalcomment.getTag() != null)
                    goalcmt = goalcomment.getTag().toString();
                if (owecomment.getTag() != null)
                    owecmt = owecomment.getTag().toString();

                FinancialYearManager.insertFinalcialStartANDEnd(mContext, fincialstartdt, fincialenddt);
                InitialBudgetManager.insertInitailMoney(mContext, "initailmoney", howmuchp, startdat, strcmt);
                InitialBudgetManager.insertInitailMoney(mContext, "goalmoney", goalhowmuchp, goalstartdat, goalcmt);
                InitialBudgetManager.insertInitailMoney(mContext, "owemoney", owehowmuchp, owestartdat, owecmt);
                Toast.makeText(mContext, "Submitted Initial Plan.", Toast.LENGTH_LONG).show();


                // formate at two decimal places
                String aDouble1 = "", aDouble2 = "", aDouble3 = "";
                DecimalFormat df = new DecimalFormat("0.00");
                try {
                    aDouble1 = df.format(Double.parseDouble(howmuchp));
                    aDouble2 = df.format(Double.parseDouble(goalhowmuchp));
                    aDouble3 = df.format(Double.parseDouble(owehowmuchp));

                    howmuchpound.setText("");
                    howmuchpound.setText(aDouble1);

                    howmuchpoundofmygoal.setText("");
                    howmuchpoundofmygoal.setText(aDouble2);

                    howmuchpoundofwhatiwoe.setText("");
                    howmuchpoundofwhatiwoe.setText(aDouble3);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else
                Toast.makeText(mContext, "Please enter owe start date.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "Please enter owe money.", Toast.LENGTH_LONG).show();
        }
    }


     DatePickerDialog mDatePicker=null;
    public void initdate(final TextView startdate, final TextView nextdate) {

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
               /* myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                nextdate.setText(sdf.format(myCalendar.getTime()));*/
            }


        };


        mDatePicker = new DatePickerDialog(mContext, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));


        mDatePicker.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            dialog.cancel();
                        }
                    }
                });

        mDatePicker.setButton(DialogInterface.BUTTON_POSITIVE,
                "Select", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            DatePicker datePicker = mDatePicker
                                    .getDatePicker();

                            myCalendar.set(Calendar.YEAR, datePicker.getYear());
                            myCalendar.set(Calendar.MONTH, datePicker.getMonth());
                            myCalendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                            String myFormat = "dd-MM-yyyy"; //In which you need put here
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

                            startdate.setText(sdf.format(myCalendar.getTime()));
                            if (nextdate != null) {
                                nextdate.setText(getYearEndDate(sdf.format(myCalendar.getTime())));

                            } else {
                                // fincialenddt=getYearEndDate(sdf.format(myCalendar.getTime()));
                            }
                        }
                    }
                });





        mDatePicker.setCancelable(false);
        //mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        mDatePicker.show();
    }


    public String getYearEndDate(String startDate) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Calendar cal = null;
        try {

            Date date = format.parse(startDate);
            cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.YEAR, 1);
            cal.add(Calendar.DATE, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Date endt = cal.getTime();

        return format.format(endt);
    }


    CoordinatorLayout dialoflayparent;

    public void showdialog(final LinearLayout commentlay, final LinearLayout mymoneycommentlay, final TextView moneycommenttxt) {

        final PopupWindow changeSortPopUp = new PopupWindow(mContext);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.fragment_dialog, null);

        final LinearLayout parent = (LinearLayout) layout.findViewById(R.id.parent);
        parent.setBackgroundResource(R.drawable.mymoney_popup);
        final EditText commentedit = (EditText) layout.findViewById(R.id.commentedit);
        final Button setbtn = (Button) layout.findViewById(R.id.setbtn);
        dialoflayparent = (CoordinatorLayout) layout.findViewById(R.id.dialoflayparent);

        if (commentlay.getTag() != null) {
            commentedit.setText(commentlay.getTag().toString());
        }


        setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    if (ValidationUtility.validEditTextString(commentedit.getText().toString())) {

                        String comtxt = commentedit.getText().toString().trim();
                        commentlay.setTag(comtxt);

                        mymoneycommentlay.setVisibility(View.VISIBLE);
                        moneycommenttxt.setText(comtxt);

                        changeSortPopUp.dismiss();
                    } else {


                        Toast.makeText(mContext,"Please Enter Comment",Toast.LENGTH_SHORT).show();
                        //showsnack("Please Enter Comment");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        Rect rc = new Rect();
        commentlay.getWindowVisibleDisplayFrame(rc);
        int[] xy = new int[2];
        commentlay.getLocationInWindow(xy);
        rc.offset(xy[0], xy[1]);
        // Creating the PopupWindow
        changeSortPopUp.setAnimationStyle(R.style.animationName);
        changeSortPopUp.setContentView(layout);
        int OFFSET_X=0;
        int OFFSET_Y=0;
        if(isTablet(mContext)) {
            changeSortPopUp.setWidth(300);
            changeSortPopUp.setHeight(150);
             OFFSET_X = 0;
             OFFSET_Y = commentlay.getHeight() - 20;


        }
        else{
            changeSortPopUp.setWidth(500);
            changeSortPopUp.setHeight(350);
             OFFSET_X = commentlay.getWidth() - 100;
             OFFSET_Y = commentlay.getHeight() - 50;

        }
        changeSortPopUp.setFocusable(true);


        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.

        // Clear the default translucent background
        changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        changeSortPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, rc.left + OFFSET_X, rc.top + OFFSET_Y);
    }

    public void showsnack(String msg) {

        Snackbar snackbar = Snackbar
                .make(dialoflayparent, msg, Snackbar.LENGTH_SHORT);


        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTypeface(Typeface.create("sans-serif-condensed", Typeface.NORMAL));
        textView.setTextColor(Color.parseColor("#ffffff"));

        snackbar.show();

    }


    public class TextWatcherForEdit implements TextWatcher {

        TextView poundsym, pencesym;
        EditText howmuchpound;

        TextWatcherForEdit(TextView poundsym, TextView pencesym, EditText howmuchpound) {
            this.poundsym = poundsym;
            this.pencesym = pencesym;
            this.howmuchpound = howmuchpound;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            howmuchpound.setHint("");
            String temptxt = editable.toString();
            if (temptxt.length() > 0) {

                poundsym.setVisibility(View.VISIBLE);

                if (temptxt.contains(".")) {

                    int dotindex = temptxt.indexOf(".");
                    if (temptxt.length() > (dotindex + 1)) {
                        //pencesym.setVisibility(View.VISIBLE);
                    }
                } else {
                   // pencesym.setVisibility(View.GONE);
                }


            } else {
                howmuchpound.setHint("Amount?");
               // pencesym.setVisibility(View.GONE);
               // poundsym.setVisibility(View.GONE);
            }


        }
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

}
