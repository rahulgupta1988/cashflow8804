package cashflow.sixdcash.com.cashflow.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cashflow.sixdcash.com.cashflow.R;
import cashflow.sixdcash.com.cashflow.TransactionPanel;
import cashflow.sixdcash.com.cashflow.Utility.ChartValueFormatter;
import cashflow.sixdcash.com.cashflow.Utility.CustomBarDataSet;
import cashflow.sixdcash.com.cashflow.Utility.DateUtility;
import cashflow.sixdcash.com.cashflow.admin.AdminHome;
import cashflow.sixdcash.com.cashflow.manager.DayInOutTransactionManager;
import cashflow.sixdcash.com.cashflow.manager.GraphTranCalManager;
import cashflow.sixdcash.com.cashflow.manager.InitialBudgetManager;
import cashflowdb.DayInOutSummery;
import cashflowdb.InitialBudget;

/**
 * Created by Praveen on 9/15/2016.
 */
public class SummeryFragment extends Fragment implements View.OnClickListener {

    Context mContext;
    View view;
    BarChart budgetchart;

    ArrayList<BarEntry> barEntriesIncom, barEntriesExpend;
    ArrayList<String> BarEntryLabels;

    BarDataSet barDataSetIncome, barDataSetExpend;

    BarData barData;
    ImageView seldatefrom, seldateto;
    TextView fromdate, todate;

    // summary view
    TextView worstCase, WorLCaseDate, warningTV, firstWorstCase;
//    TextView threemonthin,sixmonthin,ninemonthin,twelvemonthin,
//            threemonthout,sixmonthout,ninemonthout,twelvemonthout,
//            threemonthinout,sixmonthinout,ninemonthinout,twelvemonthinout;

//    TextView remainingmoney;
    TextView goal_txt;
    private int temp_intervals = 366;
    private List<DayInOutSummery> dayInOutSummeries;
    private TextView worstOrLowestCase;
    private LinearLayout summeryLL2;
    private TextView yearBalSummery;
    private TextView yearBalDate;
    private LinearLayout summerParentLL;
    private TextView firstDebtDays;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         if(isTablet(mContext)) {
             view = inflater.inflate(R.layout.summeryfragview, container, false);
        }
        else{
             view = inflater.inflate(R.layout.summeryfragview_phone, container, false);
        }

       //view = inflater.inflate(R.layout.summeryfragview, container, false);


        init();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (Context) activity;
    }

    public void init() {

//        remainingmoney = (TextView) view.findViewById(R.id.remainingmoney);
        goal_txt = (TextView) view.findViewById(R.id.goal_txt);

        seldatefrom = (ImageView) view.findViewById(R.id.seldatefrom);
        seldateto = (ImageView) view.findViewById(R.id.seldateto);
        fromdate = (TextView) view.findViewById(R.id.fromdate);
        todate = (TextView) view.findViewById(R.id.todate);

        seldatefrom.setOnClickListener(this);
        seldateto.setOnClickListener(this);

        budgetchart = (BarChart) view.findViewById(R.id.budgetchart);

        try {

            setChartData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setValues();

    }

    String setgoalcmt = "";

    public void setValues() {

        List<InitialBudget> initialBudgets = InitialBudgetManager.getInitialBudget(mContext);

        Log.i("initialBudgets size232", "" + initialBudgets.size());
        if (initialBudgets != null && initialBudgets.size() > 0) {

            for (int i = 0; i < initialBudgets.size(); i++) {
                InitialBudget initialBudget = initialBudgets.get(i);
                if (initialBudget.getBugetID().equals("2")) {
                    setgoalcmt = initialBudget.getComment();

                    if (setgoalcmt != null && !setgoalcmt.equals("")) {
                       /* goalcommenttxt.setText(setgoalcmt.toString());
                        goalcommentlay.setVisibility(View.VISIBLE);*/
                        Log.i("len8802", "" + setgoalcmt.length());
                        if (setgoalcmt.length() > 80) {
                            goal_txt.setOnClickListener(this);
                            String tem_goal_cmt = setgoalcmt.substring(0, 80) + "...";
                            String htmlstr = "<dev><font color=red>Goal:- </font>" + tem_goal_cmt + "</dev>";

                            goal_txt.setText(Html.fromHtml(htmlstr));
                        } else {
                            String htmlstr = "<dev><font color=red>Goal:- </font>" + setgoalcmt + "</dev>";
                            Log.i("97909", "" + htmlstr);
                            goal_txt.setText(Html.fromHtml(htmlstr));
                        }
                    }
                }
            }
        }
    }

/*
    public void calculateAmoutAfteryear() {

       */
/* List<InitialBudget> initialBudgets= InitialBudgetManager.getInitialBudget(mContext);

        if(initialBudgets!=null && initialBudgets.size()>0){

            InitialBudget startMoneyBuget=initialBudgets.get(0);
            InitialBudget goalBuget=initialBudgets.get(1);
            //InitialBudget oweMoneyBuget=initialBudgets.get(2);

            Double total_Income=Double.parseDouble(twelvemonthsum)+startMoneyBuget.getPound();
           // Double total_Expence=Double.parseDouble(twelvemonthOUTsum)+oweMoneyBuget.getPound();
            Double total_Expence=Double.parseDouble(twelvemonthOUTsum);
            Double final_buget=total_Income-total_Expence;

            DecimalFormat df = new DecimalFormat("0.00");
            String finalMoney=df.format(final_buget);

            remainingmoney.setText("\u00A3"+finalMoney+"p");

        }*//*


        DayInOutSummery dayInOutSummery = new DayInOutTransactionManager(mContext).getYearEdnMoney();
        if (null != dayInOutSummery) {
            DecimalFormat df = new DecimalFormat("0.00");
            String finalMoney = df.format(dayInOutSummery.getBalance_in_hand());
            remainingmoney.setText("\u00A3" + finalMoney + "p");
        }
    }
*/

    public void setChartProp() {

        final int[] LEGEND_COLORS = {
                Color.rgb(76, 153, 0), Color.rgb(255, 0, 0), Color.rgb(255, 255, 255)
        };

        // set description
        Date bugetStartDate = GraphTranCalManager.getBugetYearStartDate(mContext);
        Date bugetEndDate = GraphTranCalManager.getBugetYearENDDate(mContext);

        Legend legend = budgetchart.getLegend();
        if (null!=bugetStartDate && (!bugetStartDate.equals(""))&& null!=bugetEndDate && (!bugetEndDate.equals("")) )
        legend.setCustom(LEGEND_COLORS, new String[]{"Positive Balance", "Negative Balance", DateUtility.getStringBYDate(bugetStartDate) + "  To  " + DateUtility.getStringBYDate(bugetEndDate)});
        budgetchart.getLegend().setEnabled(true);


        budgetchart.setDescription("");
        budgetchart.setExtraBottomOffset(10);
        // Hide the legend
        budgetchart.setPinchZoom(false);
        budgetchart.setDoubleTapToZoomEnabled(false);

        //budgetchart.setOnChartValueSelectedListener(new ChartListener());

        XAxis xAxis = budgetchart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(0);
        xAxis.setLabelsToSkip(0);
        xAxis.setLabelRotationAngle(270);
//        xAxis.setTextSize(12f);

        YAxis yAxis_right = budgetchart.getAxisRight();
        yAxis_right.setDrawLabels(false);

        YAxis yAxis = budgetchart.getAxisLeft();
        yAxis.setValueFormatter(new ChartValueFormatter());
        yAxis.setDrawGridLines(false);
        yAxis.setStartAtZero(false);
//        yAxis.setTextSize(12f);

    }

    public static final int[] INCOME_COLORS = {
            Color.rgb(76, 153, 0)
    };

    public static final int[] EXPEND_COLORS = {
            Color.rgb(255, 0, 0)
    };

    public void setChartData() {
        barEntriesIncom = new ArrayList<>();
        barEntriesExpend = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();
        new InitBackground().execute();
    }

    public void initChartData() {

     /*   barDataSetIncome = new BarDataSet(barEntriesIncom, "");
        barDataSetIncome.setColors(INCOME_COLORS);
        barDataSetIncome.setHighLightColor(Color.argb(5, 255, 255, 0));

        barDataSetExpend = new BarDataSet(barEntriesExpend, "");
        barDataSetExpend.setColors(EXPEND_COLORS);
        barDataSetExpend.setHighLightColor(Color.argb(5, 255, 255, 0));*/

        barDataSetIncome = new CustomBarDataSet(barEntriesIncom, "");
        barDataSetIncome.setColors(new int[]{Color.rgb(255, 0, 0),
                Color.rgb(76, 153, 0)});

        barDataSetIncome.setBarSpacePercent(40);
//        barDataSetExpend.setBarSpacePercent(40);

        List<IBarDataSet> datasets = new ArrayList<IBarDataSet>();
        datasets.add(barDataSetIncome);
//        datasets.add(barDataSetExpend);

        barData = new BarData(BarEntryLabels, datasets);
        // barData.setValueFormatter(new Chartdataformatter()); // tp set float data


        barData.setGroupSpace(0f);

        budgetchart.setData(barData);
        budgetchart.animateY(500);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.seldatefrom:
                initdate(fromdate);
                break;

            case R.id.seldateto:
                initdate(todate);
                break;
            case R.id.goal_txt:
                settingBar();
                break;
        }
    }

    public void initdate(final TextView nextdate) {

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
            }

        };

        final DatePickerDialog mDatePicker = new DatePickerDialog(mContext, date, myCalendar
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

                            nextdate.setText(sdf.format(myCalendar.getTime()));
                        }
                    }
                });
        mDatePicker.setCancelable(false);
        //mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        mDatePicker.show();
    }


    public void AddValuesToBarEntryLabels() {

        try {
            int count=0;
            for (DayInOutSummery dayInOutSummery:dayInOutSummeries){
                Date dayInOutDate = dayInOutSummery.getDate();
                if(null!=dayInOutDate) {
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTime(dayInOutDate);

                    String newDate=new SimpleDateFormat("dd MMM").format(dayInOutDate);
//                    int month = calendar.get(Calendar.MONTH);
//                    int date = calendar.get(Calendar.DATE);
                    if(count%7==0) {
                        BarEntryLabels.add(newDate);
                    }else {
                        BarEntryLabels.add("");
                    }
                    count++;
                }
            }
            /*Date bugetStartDate = GraphTranCalManager.getBugetYearStartDate(mContext);
            if(null!=bugetStartDate) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(bugetStartDate);
                int startmonth = calendar.get(Calendar.MONTH);
                int date = calendar.get(Calendar.DATE);

                int flag = startmonth;

          *//*  for(int y=startmonth;y<(startmonth+12);y++){

                if(y==12)
                    flag=0;
                BarEntryLabels.add(getMonthForInt(flag));
                flag++;
            }*//*

                int totalCount = dayInOutSummeries.size();

                int labelCheck = 0;
                while (totalCount > 0) {

//                Log.i("month90192", "" + flag + "   " + getMonthForInt(flag));
                    labelCheck++;
                    if (labelCheck % 30 == 0) {
                        BarEntryLabels.add(getTwoDigits(date));
//                        BarEntryLabels.add(getMonthForInt(flag).substring(0, 3).toUpperCase());
                        flag++;
                    } else
                        BarEntryLabels.add("");

                *//*if(flag==11)
                    flag=0;
                else {
                    flag++;
                }*//*
                    totalCount--;
                }
            }
*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private String getTwoDigits(int date) {
        return String.format("%02d", date);
    }
    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }



    public void monthlyTotalInOut() {

        DecimalFormat df = new DecimalFormat("0.00");
        float initial_money = (float) (getInitialMoney());
        float temp_total = 0;

        int count = 0;
        for (DayInOutSummery dayInOutSummery : dayInOutSummeries) {
            float total = new Float(df.format(new Double(dayInOutSummery.getBalance_in_hand())));
            barEntriesIncom.add(new BarEntry(total, count));
            Log.d("nehal: amt", dayInOutSummery.getDate() + " " + dayInOutSummery.getBalance_in_hand());
            count++;
        }



    }

    public class ChartListener implements OnChartValueSelectedListener {

        @Override
        public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

            String label = BarEntryLabels.get(e.getXIndex());

            String incomeORexpend;

            if (dataSetIndex == 0) incomeORexpend = "Income";
            else incomeORexpend = "Expend";
            Toast.makeText(mContext, "" + label + " " + incomeORexpend + " " + e.getVal(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNothingSelected() {

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

    public void setSummery() {

        summerParentLL=(LinearLayout)view.findViewById(R.id.summerParentLL);
        worstCase = (TextView) view.findViewById(R.id.worstCase);
        WorLCaseDate = (TextView) view.findViewById(R.id.WorLCaseDate);
        warningTV = (TextView) view.findViewById(R.id.WoLWarning);
        firstWorstCase = (TextView) view.findViewById(R.id.firstWorstCase);
        firstDebtDays = (TextView) view.findViewById(R.id.firstDebtDays);
        worstOrLowestCase = (TextView) view.findViewById(R.id.worst_lowest_case);
        summeryLL2=(LinearLayout)view.findViewById(R.id.summeryLL2);
        yearBalSummery=(TextView)view.findViewById(R.id.yearBalSummery);
        yearBalDate=(TextView)view.findViewById(R.id.yearBalDate);
        DayInOutTransactionManager manager=new DayInOutTransactionManager(mContext);
        DayInOutSummery dayInOutSummery = manager.getWorstDayInOut();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        DecimalFormat df = new DecimalFormat("#,###.00");
        DayInOutSummery firstDebt=manager.getFirstDebt();

        if (null != dayInOutSummery) {
            summerParentLL.setVisibility(View.VISIBLE);
            if (dayInOutSummery.getBalance_in_hand() >= 0.0 && dayInOutSummery.getBalance_in_hand() <= 99.99) {
                worstOrLowestCase.setText("Lowest Point");
                String finalMoney = df.format(dayInOutSummery.getBalance_in_hand());
                worstCase.setText("\u00A3" + finalMoney);
                worstCase.setTextColor(getResources().getColor(R.color.black));
                String wOrLDateString = formatter.format(dayInOutSummery.getDate());
                WorLCaseDate.setText(wOrLDateString);
                warningTV.setText("Careful - Running Low");
                warningTV.setTextColor(getResources().getColor(R.color.black));
                warningTV.setBackgroundColor(getResources().getColor(R.color.amber));
                summeryLL2.setVisibility(View.INVISIBLE);
            } else if (dayInOutSummery.getBalance_in_hand() >99.99) {
                worstOrLowestCase.setText("Lowest Point");
                String finalMoney = df.format(dayInOutSummery.getBalance_in_hand());
                worstCase.setText("\u00A3" + finalMoney);
                worstCase.setTextColor(getResources().getColor(R.color.black));
                String wOrLDateString = formatter.format(dayInOutSummery.getDate());
                WorLCaseDate.setText(wOrLDateString);
                warningTV.setText("");
//                warningTV.setTextColor(getResources().getColor(R.color.black));
                warningTV.setBackgroundColor(Color.TRANSPARENT);
                summeryLL2.setVisibility(View.INVISIBLE);
            } else if (dayInOutSummery.getBalance_in_hand() < 0.0){
                worstOrLowestCase.setText("Worst Case");
                String finalMoney = df.format(dayInOutSummery.getBalance_in_hand());
                worstCase.setText("\u00A3" + finalMoney);
                worstCase.setTextColor(getResources().getColor(R.color.red));
                String wOrLDateString = formatter.format(dayInOutSummery.getDate());
                WorLCaseDate.setText(wOrLDateString);
                warningTV.setText("Danger Ahead!");
                warningTV.setTextColor(getResources().getColor(R.color.white));
                warningTV.setBackgroundColor(getResources().getColor(R.color.red));
                summeryLL2.setVisibility(View.VISIBLE);
                if (null!=firstDebt){
                    firstWorstCase.setText(formatter.format(firstDebt.getDate()));
                    Date firstDate = DayInOutTransactionManager.getBugetYearStartDate(mContext);
                    if(null!=firstDate) {
                        Date nextDate = firstDebt.getDate();
                        int diffInDays = (int) ((nextDate.getTime() - firstDate.getTime()) / (1000 * 60 * 60 * 24));
                        if (diffInDays >=0) {
                            Log.d("nehal: Date Diff ", +diffInDays + "");
                            firstDebtDays.setText("Day " + String.valueOf(diffInDays+1));
                        }
                    }
                }

            }
        }

        DayInOutSummery yearEndDay=manager.getYearEdnMoney();
        if (null!=yearEndDay){
            summerParentLL.setVisibility(View.VISIBLE);
            String finalMoney = df.format(yearEndDay.getBalance_in_hand());
            yearBalSummery.setText("\u00A3" + finalMoney);
            String yearEndDateString = formatter.format(yearEndDay.getDate());
            yearBalDate.setText(yearEndDateString);
            if (yearEndDay.getBalance_in_hand()>0.0){
                yearBalSummery.setTextColor(mContext.getResources().getColor(R.color.darkgreen));
            }
        }

    }

//    List<String> monthlyIncome;
//    List<String> monthlyExpends;

    public void getMonthluInOut() {

        try {
          dayInOutSummeries = new DayInOutTransactionManager(mContext).getDayInOutSummery();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class InitBackground extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getMonthluInOut();
//            calculateSumerrybyMonth();
           /* incomeData();
            expendData();*/
            monthlyTotalInOut();
            AddValuesToBarEntryLabels();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                setChartProp();
                initChartData();
                setSummery();

//                calculateAmoutAfteryear();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void settingBar() {

        final PopupWindow changeSortPopUp = new PopupWindow(mContext);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.goaltextdetails, null);

        TextView textView = (TextView) layout.findViewById(R.id.goal_txt);
        textView.setText(setgoalcmt);

        Rect rc = new Rect();
        goal_txt.getWindowVisibleDisplayFrame(rc);
        int[] xy = new int[2];
        goal_txt.getLocationInWindow(xy);
        rc.offset(xy[0], xy[1]);
        // Creating the PopupWindow
        changeSortPopUp.setAnimationStyle(R.style.animationName);
        changeSortPopUp.setContentView(layout);
        changeSortPopUp.setWidth(goal_txt.getWidth());
        changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setFocusable(true);


        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = 0;
        int OFFSET_Y = goal_txt.getHeight();

        // Clear the default translucent background
        changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        changeSortPopUp.showAtLocation(layout, Gravity.LEFT, rc.left + OFFSET_X, rc.top + OFFSET_Y);
    }


     public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
}
