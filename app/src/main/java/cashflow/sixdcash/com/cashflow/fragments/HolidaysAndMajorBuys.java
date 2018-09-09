package cashflow.sixdcash.com.cashflow.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cashflow.sixdcash.com.cashflow.R;
import cashflow.sixdcash.com.cashflow.Utility.ValidationUtility;
import cashflow.sixdcash.com.cashflow.manager.DBManager;
import cashflowdb.Category;
import cashflowdb.HowOften;
import cashflowdb.Source;

/**
 * Created by Praveen on 9/16/2016.
 */
public class HolidaysAndMajorBuys extends Fragment implements View.OnClickListener{
    Context mContext;
    ViewGroup mcontainer;
    View view;
    Button submit, addmore;
    LinearLayout incomeinputview;
    AnimatorSet set;

    // Source and HowOfter Spinner
    List<HowOften> howOftens = new ArrayList<HowOften>();
    List<String> howof = new ArrayList<String>();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mcontainer = container;
        view = inflater.inflate(R.layout.incomefragmentview, container, false);
        init();

        return view;
    }

    public void init() {
        incomeinputview = (LinearLayout) view.findViewById(R.id.incomeinputview);
        addmore = (Button) view.findViewById(R.id.addmore);
        submit = (Button) view.findViewById(R.id.submit);
        addmore.setOnClickListener(this);
        submit.setOnClickListener(this);
        addInputView();
    }


    public void initSourceSelector(Spinner sourcespin, final Spinner howOftenSpinner) {
        Category categories = DBManager.getCategoryByName(mContext, "Holidays & Major Buys");
        final List<Source> sources = categories.getSourceList();
        List<String> sounceName = null;
        if (sources != null) {
            sounceName = new ArrayList<String>();
            for (Source sounce : sources) {
                sounceName.add(sounce.getSourcename());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, sounceName);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sourcespin.setAdapter(adapter);

            /*sourcespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                    *//*if(howOftens!=null){
                        howOftens.clear();
                    }*//*
                      howOftens=sources.get(position).getHowOftenList();
                    Toast.makeText(mContext,""+sources.get(position).getSourcename(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext,""+howOftens.size(),Toast.LENGTH_SHORT).show();
                      initHowOftenSelector(howOftenSpinner);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });*/

        }

    }

    public void initHowOftenSelector(Spinner howoften) {
        List<String> howof = null;
        howof = new ArrayList<String>();
        String[] often = getResources().getStringArray(R.array.how_often);
        for (String howoften_str : often) {
            howof.add(howoften_str);
        }

       /* if(howof!=null){
            howof.clear();
        }

        for (HowOften howOften_temp : howOftens) {
            howof.add(howOften_temp.getOften());
        }*/


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, howof);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        howoften.setAdapter(adapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (Context) activity;
    }

    int viewID = 0;

    public void addInputView() {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View incomeinputitemView = inflater.inflate(R.layout.incomeinputitem, null, false);

        final FrameLayout touchlay = (FrameLayout) incomeinputitemView.findViewById(R.id.touchlay);
        final LinearLayout deleteview = (LinearLayout) incomeinputitemView.findViewById(R.id.deleteview);
        final LinearLayout maininputlay = (LinearLayout) incomeinputitemView.findViewById(R.id.maininputlay);

        final ImageView cancel = (ImageView) incomeinputitemView.findViewById(R.id.cancel);
        final ImageView trash = (ImageView) incomeinputitemView.findViewById(R.id.trash);


        final LinearLayout selcomment = (LinearLayout) incomeinputitemView.findViewById(R.id.selcomment);
        final LinearLayout commentlay = (LinearLayout) incomeinputitemView.findViewById(R.id.commentlay);
        final TextView commenttxt = (TextView) incomeinputitemView.findViewById(R.id.commenttxt);
        final TextView howmuchpound = (TextView) incomeinputitemView.findViewById(R.id.howmuchpound);
        final TextView howmuchpence = (TextView) incomeinputitemView.findViewById(R.id.howmuchpence);
        final TextView nextdate = (TextView) incomeinputitemView.findViewById(R.id.nextdate);
        //final ImageView infoimg = (ImageView) incomeinputitemView.findViewById(R.id.infoimg);

        final Spinner sourcespin = (Spinner) incomeinputitemView.findViewById(R.id.sourcespin);
        final Spinner howoften = (Spinner) incomeinputitemView.findViewById(R.id.howoften);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        incomeinputitemView.setLayoutParams(params);

        selcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showdialog(commentlay,selcomment,commenttxt);
            }


        });

        howmuchpound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amoutInputDialog(howmuchpound, "pound");
            }
        });

        howmuchpence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amoutInputDialog(howmuchpence, "pence");
            }
        });

        nextdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initdate(nextdate);
            }
        });

        /*infoimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfodialog(infoimg);
            }
        });*/

        initSourceSelector(sourcespin, howoften);
        initHowOftenSelector(howoften);



       /* touchlay.setOnTouchListener(new MyTouchListener() {
            @Override
            public void rightToLeft() {
                AnimatorSet set=new AnimatorSet();

                set.playSequentially(
                        ObjectAnimator.ofFloat(maininputlay, "alpha",1f,0.5f),
                        ObjectAnimator.ofFloat(maininputlay, "translationX", 0, -130)


                );

                set.setDuration(200);
                set.start();
                //ObjectAnimator.ofFloat(maininputlay, "translationX", 0, -130).setDuration(300).start();
                deleteview.setVisibility(View.VISIBLE);

            }
        });
*/
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

                deleteview.setVisibility(View.GONE);
            }
        });

        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ObjectAnimator.ofFloat(maininputlay, "translationX", maininputlay.getWidth(), 0).setDuration(300).start();

                maininputlay.animate()
                        .translationXBy(-maininputlay.getWidth())
                        .setDuration(500)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                incomeinputview.removeView(touchlay);
                            }
                        });

            }
        });

        incomeinputview.setTag(viewID);
        incomeinputview.addView(incomeinputitemView);
        viewID++;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.addmore:
                addInputView();
                break;
            case R.id.submit:
                sumitIncome();
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
        int child_count = incomeinputview.getChildCount();
        Log.i("child_count", "" + child_count);

        for (int z = 0; z < child_count; z++) {

            View view = incomeinputview.getChildAt(z);
            Spinner sourceview = (Spinner) view.findViewById(R.id.sourcespin);
            TextView commenttxt = (TextView) view.findViewById(R.id.commenttxt);
            TextView howmuchpound = (TextView) view.findViewById(R.id.howmuchpound);
            TextView howmuchpence = (TextView) view.findViewById(R.id.howmuchpence);
            Spinner howoften = (Spinner) view.findViewById(R.id.howoften);
            TextView nextdate = (TextView) view.findViewById(R.id.nextdate);


            // get values
            String source = sourceview.getSelectedItem().toString();
            String comment = commenttxt.getText().toString();
            String pound = howmuchpound.getText().toString();
            String pence = howmuchpence.getText().toString();
            String often = howoften.getSelectedItem().toString();
            String nxtdt = nextdate.getText().toString();


            Log.i("source", "" + z + " " + source);
            Log.i("comment", "" + z + " " + comment);
            Log.i("pound", "" + z + " " + pound);
            Log.i("pence", "" + z + " " + pence);
            Log.i("often", "" + z + " " + often);
            Log.i("nxtdt", "" + z + " " + nxtdt);

           // DBManager.insertTransaction(mContext, source, comment, pound, pence, often, nxtdt);


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
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                            nextdate.setText(sdf.format(myCalendar.getTime()));
                        }
                    }
                });
        mDatePicker.setCancelable(false);
        //mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        mDatePicker.show();
    }

    CoordinatorLayout dialoflayparent;

    public void showdialog(final LinearLayout commentlay,final LinearLayout selcomment,final TextView textView) {

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
                        showsnack("Please Enter Comment");
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
        changeSortPopUp.setWidth(400);
        changeSortPopUp.setHeight(250);
        changeSortPopUp.setFocusable(true);


        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = 0;
        int OFFSET_Y = selcomment.getHeight()-10;

        // Clear the default translucent background
        changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        changeSortPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, rc.left + OFFSET_X, rc.top + OFFSET_Y);
    }


   /* public void showInfodialog(final ImageView imageView) {

        final PopupWindow changeSortPopUp = new PopupWindow(mContext);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.infoitemview, null);
        TextView infotext = (TextView) layout.findViewById(R.id.infotext);
        infotext.setMovementMethod(new ScrollingMovementMethod());

        Rect rc = new Rect();
        imageView.getWindowVisibleDisplayFrame(rc);
        int[] xy = new int[2];
        imageView.getLocationInWindow(xy);
        rc.offset(xy[0], xy[1]);
        // Creating the PopupWindow
        changeSortPopUp.setAnimationStyle(R.style.animationName);
        changeSortPopUp.setContentView(layout);
        changeSortPopUp.setWidth(400);
        changeSortPopUp.setHeight(200);
        changeSortPopUp.setFocusable(true);


        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = 0;
        int OFFSET_Y = imageView.getHeight() - 10;

        // Clear the default translucent background
        changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        changeSortPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, rc.left + OFFSET_X, rc.top + OFFSET_Y);
    }*/

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
}
