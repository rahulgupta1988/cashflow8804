package cashflow.sixdcash.com.cashflow.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cashflow.sixdcash.com.cashflow.R;
import cashflow.sixdcash.com.cashflow.TransactionPanel;
import cashflow.sixdcash.com.cashflow.Utility.DateUtility;
import cashflow.sixdcash.com.cashflow.Utility.ValidationUtility;
import cashflow.sixdcash.com.cashflow.manager.DBManager;
import cashflow.sixdcash.com.cashflow.manager.DayInOutTransactionManager;
import cashflow.sixdcash.com.cashflow.manager.FinancialYearManager;
import cashflow.sixdcash.com.cashflow.manager.TransactionPanelManager;
import cashflowdb.Category;
import cashflowdb.FinancialYear;
import cashflowdb.HowOften;
import cashflowdb.InOutTransaction;
import cashflowdb.Source;

/**
 * Created by Praveen on 8/26/2016.
 */
public class Home extends Fragment implements View.OnClickListener {

    Context mContext;
    ViewGroup mcontainer;
    View view;
    Button submit, addmore, trasctionbtn,submitmoneyplan;
    LinearLayout incomeinputview, trasactionlistview;
    AnimatorSet set;
    ImageView addMoreMenuButton,submitMenuButton;

    // Source and HowOfter Spinner
    List<HowOften> howOftens;


    Category categories;
    List<Source> temp_sources;
    List<Source> sources = new ArrayList<Source>();
    String catName;

    Toast toast = null;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mcontainer = container;
        view = inflater.inflate(R.layout.incomefragmentview, container, false);
        catName = getArguments().getString("catname");
        toast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
        categories = DBManager.getCategoryByName(mContext, catName);
        setSources();


        init();
        getFinancialYear();
        return view;
    }

    public void init() {
        incomeinputview = (LinearLayout) view.findViewById(R.id.incomeinputview);
        trasactionlistview = (LinearLayout) view.findViewById(R.id.trasactionlistview);
        addmore = (Button) view.findViewById(R.id.addmore);
        submit = (Button) view.findViewById(R.id.submit);
        trasctionbtn = (Button) view.findViewById(R.id.trasctionbtn);
        trasctionbtn.setOnClickListener(this);
        addmore.setOnClickListener(this);
        submit.setOnClickListener(this);


        //getInputView();
        count = 0;
        setTransactionView(null);
        //addInputView();
        // setTransactionAdapter();
    }


    int count = 0;

    public void setTransactionView(List<InOutTransaction> inOutTransactions1) {

        List<InOutTransaction> inOutTransactions = null;

        if (inOutTransactions1 == null) {
            Long id = DBManager.getCategoryIDByName(mContext, catName);
            inOutTransactions = DBManager.getTrasactionByID(mContext, id);
        } else {
            inOutTransactions = inOutTransactions1;
        }

        if (inOutTransactions != null) {
            for (int i = 0; i < inOutTransactions.size(); i++) {
                InOutTransaction inOutTransaction = inOutTransactions.get(i);


                count++;
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                View itemView = null;
                //itemView=inflater.inflate(R.layout.transactionreadview, null, false);
                if(isTablet(mContext)) {
                  itemView=inflater.inflate(R.layout.transactionreadview, null, false);
                }
                else{
                    itemView=inflater.inflate(R.layout.transactionreadview_phone, null, false);
                }



                TextView sn, catname, sourcename, pounds, howoften, date, comment, hintAndTips;
                TextView poundsym, pencesym, frequency;
                LinearLayout parentlay;
                parentlay = (LinearLayout) itemView.findViewById(R.id.parentlay);

                frequency = (TextView) itemView.findViewById(R.id.frequency);
                sn = (TextView) itemView.findViewById(R.id.sn);
                catname = (TextView) itemView.findViewById(R.id.catname);
                sourcename = (TextView) itemView.findViewById(R.id.sourcename);
                pounds = (TextView) itemView.findViewById(R.id.pounds);
                howoften = (TextView) itemView.findViewById(R.id.howoften);
                date = (TextView) itemView.findViewById(R.id.date);
                comment = (TextView) itemView.findViewById(R.id.comment);
                hintAndTips = (TextView) itemView.findViewById(R.id.hintAndTips);

                poundsym = (TextView) itemView.findViewById(R.id.poundsym);
                pencesym = (TextView) itemView.findViewById(R.id.pencesym);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                trasactionlistview.setLayoutParams(params);


                sn.setText("" + (count));
                sourcename.setText(TransactionPanelManager.getSourceByID(mContext, inOutTransaction.getSourceid()));
                frequency.setText(inOutTransaction.getHowoften());
                String aDouble = "";

                try {
                    DecimalFormat df = new DecimalFormat("0.00");
                    Double money = inOutTransaction.getPound();
                    aDouble = df.format(money);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                pounds.setText("" + aDouble);
                howoften.setText("" + inOutTransaction.getHowoften());
                date.setText("" + DateUtility.getStringBYDate(inOutTransaction.getTransactiondate()));

                if (inOutTransaction.getComment() != null && !inOutTransaction.getComment().equals("")) {
                    comment.setText("" + inOutTransaction.getComment());
                } else {
                    comment.setText("No Comment");
                }

                trasactionlistview.addView(itemView);
            }
        }


    }


    public void setViewAsCategory(String catName) {
        toast.cancel();
        this.catName = catName;
        if (sources != null)
            sources.clear();

        categories = DBManager.getCategoryByName(mContext, catName);
        setSources();
        trasactionlistview.removeAllViews();
        incomeinputview.removeAllViews();
        count = 0;
        setTransactionView(null);
       // addInputView();
    }


    public void setSources() {

        temp_sources = categories.getSourceList();
        for (int i = 0; i < temp_sources.size(); i++) {
            if (temp_sources.get(i).getIsChecked()) {
                sources.add(temp_sources.get(i));
            }
        }

    }

    ArrayAdapter<String> catadapter;

    public void initSourceSelector(final Spinner sourcespin, final Spinner howOftenSpinner, final TextView nextdate) {


        List<String> sounceName = null;
        if (sources != null) {
            sounceName = new ArrayList<String>();
            sounceName.add("Select Item");
            for (Source sounce : sources) {
                sounceName.add(sounce.getSourcename());
            }
            catadapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, sounceName);
            catadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sourcespin.setAdapter(catadapter);

            sourcespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                  /* if (howOftens != null) {
                        howOftens = null;
                    }

                    sourcespin.setTag(catadapter.getItem(position));
                    howOftens = sources.get(position).getHowOftenList();
                    initHowOftenSelector(howOftenSpinner, nextdate);*/
                    howOftens = new ArrayList<HowOften>();

                    if (position != 0) {
                        if (howOftens != null) {
                            howOftens = null;
                        }

                        howOftens = sources.get(position - 1).getHowOftenList();
                        initHowOftenSelector(howOftenSpinner, nextdate);
                    } else {
                        initHowOftenSelector(howOftenSpinner, nextdate);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }

    }

    ArrayAdapter<String> sourceadapter;

    public void initHowOftenSelector(final Spinner howoften, final TextView nextdate) {


        List<String> howof = new ArrayList<String>();
        howof.add("Frequency?");
        for (HowOften howOften_temp : howOftens) {
            Log.i("often9101", "" + howOften_temp.getOften());
            howof.add(howOften_temp.getOften());
        }

        sourceadapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, howof);
        sourceadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        howoften.setAdapter(sourceadapter);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (Context) activity;
    }

   View selectedView=null;
    public void addInputView() {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        View incomeinputitemView=null;
        //incomeinputitemView = inflater.inflate(R.layout.incomeinputitem, null, false);
        if(isTablet(mContext)) {
             incomeinputitemView = inflater.inflate(R.layout.incomeinputitem, null, false);
    }
        else{
             incomeinputitemView = inflater.inflate(R.layout.incomeinputitem_phone, null, false);
        }




        final FrameLayout touchlay = (FrameLayout) incomeinputitemView.findViewById(R.id.touchlay);
        final LinearLayout deleteview = (LinearLayout) incomeinputitemView.findViewById(R.id.deleteview);
        final LinearLayout maininputlay = (LinearLayout) incomeinputitemView.findViewById(R.id.maininputlay);
        final ImageView cancel = (ImageView) incomeinputitemView.findViewById(R.id.cancel);
        final LinearLayout selcomment = (LinearLayout) incomeinputitemView.findViewById(R.id.selcomment);
        final LinearLayout commentlay = (LinearLayout) incomeinputitemView.findViewById(R.id.commentlay);
        final TextView commenttxt = (TextView) incomeinputitemView.findViewById(R.id.commenttxt);
        final EditText howmuchpound = (EditText) incomeinputitemView.findViewById(R.id.howmuchpound);
        final TextView poundsym = (TextView) incomeinputitemView.findViewById(R.id.poundsym);
        final TextView pencesym = (TextView) incomeinputitemView.findViewById(R.id.pencesym);
        final LinearLayout deleticon = (LinearLayout) incomeinputitemView.findViewById(R.id.deleticon);
        final TextView nextdate = (TextView) incomeinputitemView.findViewById(R.id.nextdate);
        final Spinner sourcespin = (Spinner) incomeinputitemView.findViewById(R.id.sourcespin);
        final Spinner howoften = (Spinner) incomeinputitemView.findViewById(R.id.howoften);
        final Button savebtn=(Button)incomeinputitemView.findViewById(R.id.savebtn);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        incomeinputitemView.setLayoutParams(params);

        initSourceSelector(sourcespin, howoften, nextdate);

        deleticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertForDelete(touchlay);
            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedView=touchlay;
                saveIncome(touchlay);
            }
        });

        selcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showdialog(commentlay, selcomment, commenttxt);
            }


        });

        howmuchpound.addTextChangedListener(new TextWatcher() {
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
                            // pencesym.setVisibility(View.VISIBLE);
                        }
                    } else {
                       // pencesym.setVisibility(View.GONE);
                    }


                } else {
                    howmuchpound.setHint("Amount?");
                    //pencesym.setVisibility(View.GONE);
                   // poundsym.setVisibility(View.GONE);
                }


            }
        });
        nextdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initdate(nextdate);
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ObjectAnimator.ofFloat(maininputlay, "translationX", -130, 0).setDuration(300).start();

                AnimatorSet set = new AnimatorSet();

                set.playSequentially(
                        ObjectAnimator.ofFloat(maininputlay, "alpha", 0.5f, 1f),
                        ObjectAnimator.ofFloat(maininputlay, "translationX", -130, 0)


                );

                set.setDuration(200);
                set.start();
                ObjectAnimator.ofFloat(deleteview, "translationX", 0, 200).setDuration(200).start();


            }
        });

        incomeinputview.addView(incomeinputitemView);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.addmore:
                addInputView();
                break;
            case R.id.submit:
                sumitIncome();
                //  new InitBackground().execute();
                break;

            case R.id.trasctionbtn:
                Intent adminhome = new Intent(mContext, TransactionPanel.class);
                adminhome.putExtra("catName", catName);
                startActivity(adminhome);
                ((Activity) mContext).finish();
                ((Activity) mContext).overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;


        }
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
        changeSortPopUp.setHeight(70);
        changeSortPopUp.setFocusable(true);


        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = -(wid / 2);
        int OFFSET_Y = howmuchpound.getHeight();

        // Clear the default translucent background
        changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        changeSortPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, rc.left + OFFSET_X, rc.top + OFFSET_Y);

    }

    public void sumitIncome() {

        List<FinancialYear> financialYears = FinancialYearManager.getFinancialYear(mContext);
        if (financialYears == null || financialYears.size() == 0) {
            if (toast != null) {
                toast.setText("Please Create Initial Plan at start Before Insert any In/Out");
                toast.show();
            }
        } else {

            //validatedatat();
            if (transactionsList.size() > 0) {
                new TrasactionTask().execute();
            }
        }


    }

    public void saveIncome(View touchlay) {

        List<FinancialYear> financialYears = FinancialYearManager.getFinancialYear(mContext);
        if (financialYears == null || financialYears.size() == 0) {
            if (toast != null) {
                toast.setText("Please Create Initial Plan at start Before Insert any In/Out");
                toast.show();
            }
        } else {

            validatedatat(touchlay);
            if (transactionsList.size() > 0) {
                new TrasactionTask().execute();
            }
        }


    }


    List<InOutTransaction> transactionsList = new ArrayList<InOutTransaction>();

    public void validatedatat(View touchlay) {

        try {
           /* int child_count = incomeinputview.getChildCount();
            Log.i("child_count", "" + child_count);


            for (int z = 0; z < child_count; z++) {*/


              //  Log.i("count 91", "" + child_count);
               /* final View income_view = incomeinputview.getChildAt(z);*/
            final View income_view = touchlay;
                Spinner sourceview = (Spinner) income_view.findViewById(R.id.sourcespin);
                TextView commenttxt = (TextView) income_view.findViewById(R.id.commenttxt);
                TextView howmuchpound = (TextView) income_view.findViewById(R.id.howmuchpound);
                Spinner howoften = (Spinner) income_view.findViewById(R.id.howoften);


                // get values
                String sourcename = sourceview.getSelectedItem().toString();
                String comment = commenttxt.getText().toString();
                String pound = howmuchpound.getText().toString();
                String often = howoften.getSelectedItem().toString();


                Log.i("source 1082", ""  + " " + sourcename);
                Log.i("comment", ""  + " " + comment);
                Log.i("pound", ""  + " " + pound);
                Log.i("often 1082", ""  + " " + often);

                int flag = 0;


                if (!sourcename.equalsIgnoreCase("") && !sourcename.equals("Select Item")) {
                    if (!pound.equalsIgnoreCase("") && pound != null && !pound.equals("Amount?")) {
                        if (!often.equals("Frequency?")) {

                            Date nxtdt = null;
                            //if (!often.equalsIgnoreCase("one off")) {
                            TextView nextdate = (TextView) income_view.findViewById(R.id.nextdate);
                            String temp_nxtdt = nextdate.getText().toString();
                            if (!temp_nxtdt.equalsIgnoreCase("") && temp_nxtdt != null && !temp_nxtdt.equals("Next Date")) {
                                nxtdt = DateUtility.getDateBYStringDate(temp_nxtdt);
                                Log.i("nxtdt", ""  + " " + nxtdt);
                                flag = 1;
                            } else {
                                vlaidationAnim(view);
                                transactionsList.clear();
                                // Toast.makeText(mContext, "Please insert Next Date.", Toast.LENGTH_LONG).show();
                                if (toast != null) {
                                    toast.setText("Please insert Next Date.");
                                    toast.show();
                                }
                                flag = 0;
                            }

                            // }

                    /*else{
                        flag=1;
                    }*/

                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Date transaction_srartDate = sdf.parse(DateUtility.getStringBYDate(nxtdt));
                            if (transaction_srartDate.before(finacialyear_srartDate) || transaction_srartDate.after(finacialyear_endDate)) {

                                vlaidationAnim(view);
                                transactionsList.clear();
                                if (toast != null) {
                                    toast.setText("Transaction Date should be in financial year.");
                                    toast.show();
                                }
                             //   break;
                            } else {
                                if (flag == 1) {
                                    flag = 0;
                                    InOutTransaction transaction = new InOutTransaction();


                                    try {
                                        transaction.setTransactiondate(nxtdt);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    transaction.setComment(comment);
                                    Double pound_dub = Double.parseDouble(pound);

                                    transaction.setPound(pound_dub);
                                    transaction.setHowoften(often);
                                    transaction.setNextdate(nxtdt);

                                    if (!often.equalsIgnoreCase("one off")
                                            && !often.equalsIgnoreCase("Every year")
                                            && !often.equalsIgnoreCase("Once in year"))
                                        transaction.setIsrepetitive(true);
                                    else
                                        transaction.setIsrepetitive(false);

                                    transaction.setCategoryid(categories.getId());

                                    Long sourceID_long = DBManager.getSourceIDByName(mContext, sourcename, categories.getId());
                                    transaction.setSourceid(sourceID_long);

                                    transactionsList.add(transaction);

                                }
                            }

                        } else {
                            vlaidationAnim(view);
                            transactionsList.clear();
                            if (toast != null) {
                                toast.setText("Please Select Frequency.");
                                toast.show();
                            }
                            //break;
                        }
                    } else {
                        vlaidationAnim(view);
                        transactionsList.clear();
                        if (toast != null) {
                            toast.setText("Please enter amount.");
                            toast.show();
                        }
                       // break;
                    }
                } else {
                    vlaidationAnim(view);
                    transactionsList.clear();
                    if (toast != null) {
                        toast.setText("Please Select Item.");
                        toast.show();
                    }
                    //break;
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initdate(final TextView nextdate) {

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

    CoordinatorLayout dialoflayparent;

    public void showdialog(final LinearLayout commentlay, final LinearLayout selcomment, final TextView textView) {

        final PopupWindow changeSortPopUp = new PopupWindow(mContext);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.fragment_dialog, null);

        final EditText commentedit = (EditText) layout.findViewById(R.id.commentedit);
        final Button setbtn = (Button) layout.findViewById(R.id.setbtn);
        dialoflayparent = (CoordinatorLayout) layout.findViewById(R.id.dialoflayparent);


        if (textView.getTag() != null) {

            String getTag = textView.getTag().toString();
            if (getTag != null && !getTag.equalsIgnoreCase(""))
                commentedit.setText(getTag);
        }

        setbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    if (ValidationUtility.validEditTextString(commentedit.getText().toString())) {

                        String comtxt = commentedit.getText().toString().trim();
                        textView.setTag(comtxt);
                       /* if (comtxt.length() > 7) {
                            String comtxt_temp = comtxt.substring(0, 7) + "...";
                            textView.setText(comtxt_temp);
                        } else {
                            textView.setText(comtxt);
                        }*/
                        textView.setText(comtxt);
                        commentlay.setVisibility(View.VISIBLE);
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
        selcomment.getWindowVisibleDisplayFrame(rc);
        int[] xy = new int[2];
        selcomment.getLocationInWindow(xy);
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
             OFFSET_Y = selcomment.getHeight() - 10;

        }
        else{
            changeSortPopUp.setWidth(500);
            changeSortPopUp.setHeight(350);
            OFFSET_X = selcomment.getWidth()-450;
            OFFSET_Y = selcomment.getHeight() - 10;
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

    private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }


    public void vlaidationAnim(View view) {
        AnimatorSet set = new AnimatorSet();

        set.playSequentially(
                ObjectAnimator.ofFloat(view, "translationX", 0, 300),
                ObjectAnimator.ofFloat(view, "translationX", 300, 1)
        );

        set.setDuration(100);
        set.start();
    }


    public void alertForDelete(final View delete_view) {
        {

            final Dialog dialog = new Dialog(mContext);
            Window window = dialog.getWindow();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.confirmdialog);
            window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);


            if(isTablet(mContext)){
                window.setLayout(400, ActionBar.LayoutParams.WRAP_CONTENT);
            }

            else{
                window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            }


            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            TextView tvMsg = (TextView) window.findViewById(R.id.msg_yesno);
            TextView tvNO = (TextView) window.findViewById(R.id.no_yesno);
            TextView tvYes = (TextView) window.findViewById(R.id.yes_yesno);
            tvMsg.setText("Are you sure, You want to Delete it?");

            tvNO.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
            tvYes.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    incomeinputview.removeView(delete_view);
                    dialog.dismiss();

                }
            });
            dialog.show();


        }
    }

    class InitBackground extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            sumitIncome();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (transactionsList.size() > 0) {
                    transactionsList.clear();
                    incomeinputview.removeAllViews();
                    //Toast.makeText(mContext, "Transaction Done.", Toast.LENGTH_LONG).show();
                    if (toast != null) {
                        toast.setText("Transaction Done.");
                        toast.show();
                    }
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (toast != null) {
            toast.cancel();
            toast = null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }

    Date finacialyear_srartDate, finacialyear_endDate;

    public void getFinancialYear() {
        List<FinancialYear> financialYears = FinancialYearManager.getFinancialYear(mContext);
        if (financialYears != null && financialYears.size() > 0) {
            for (int i = 0; i < financialYears.size(); i++) {
                FinancialYear financialYear = financialYears.get(i);
                if (financialYear.getCompeted().equals("0")) {

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        finacialyear_srartDate = sdf.parse(DateUtility.getStringBYDate(financialYear.getStartdate()));
                        finacialyear_endDate = sdf.parse(DateUtility.getStringBYDate(financialYear.getEnddate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    break;
                }

            }


        }


    }


    public class TrasactionTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = initProgressDialog();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DBManager.insertTransactionBYList(mContext, transactionsList, categories.getId());
            new DayInOutTransactionManager(mContext).insertDayInOut();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            setTransactionView(transactionsList);
            if (toast != null) {
                toast.setText("Transaction Done.");
                toast.show();
            }
            transactionsList.clear();
            //incomeinputview.removeAllViews();
            incomeinputview.removeView(selectedView);

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
                new ColorDrawable(Color.LTGRAY));
        progressDialog.setMessage(ss2);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        return progressDialog;
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

}
