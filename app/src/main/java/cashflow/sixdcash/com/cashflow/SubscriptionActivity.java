package cashflow.sixdcash.com.cashflow;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cashflow.sixdcash.com.cashflow.Utility.DateUtility;
import cashflow.sixdcash.com.cashflow.manager.SubscriptionManagerCF;

public class SubscriptionActivity extends Activity /* implements View.OnClickListener*/ {

    Button submitSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
    }

   /* public void init() {
        subsTitle=(TextView)findViewById(R.id.subsTitle);
        adminName.setText("Admin Name");
        password.setText("pass");
        subsStartDate = getCurrentDate();
        subscription_TIL = (TextInputLayout) findViewById(R.id.subscription_TIL);
        submitSubscription = (Button) findViewById(R.id.submitSubscription);
        submitSubscription.setOnClickListener(this);
        subs_end_date_TIE.setOnClickListener(this);
        mainLL.requestFocus();
    }

    private String getCurrentDate() {
        final Calendar myCalendar = Calendar.getInstance();
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        return sdf.format(myCalendar.getTime());
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.subs_end_date_TIE:
                initdate(subs_end_date_TIE);
                break;

            case R.id.submitSubscription:
                if (null != subs_end_date_TIE.getText() && (!subs_end_date_TIE.equals("")) && subs_end_date_TIE.length() > 0) {

                    if(!checkSubsValid(subs_end_date_TIE.getText().toString())){
                        SubscriptionManagerCF.insertSubscriptionInfo(SubscriptionActivity.this, "Admin Name", "pass",
                                subsStartDate, subs_end_date_TIE.getText().toString(), true);
                        Intent intent=new Intent(SubscriptionActivity.this,HostActivty.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(SubscriptionActivity.this,"Please select more than current Date",Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }
    private boolean checkSubsValid(String subsEnd) {
        Date subsEndDate= DateUtility.getDateBYStringDate(subsEnd);

        if (null!=subsEndDate){
            int diffInDays = (int) ((subsEndDate.getTime() - DateUtility.getTodayDate().getTime()) / (1000 * 60 * 60 * 24));
            if (diffInDays <=0) {
                return true;//negative for end of subs
            }else {
                return false;
            }
        }
        return false;
    }


    public void initdate(final TextInputEditText startdate) {

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

            }

        };


        final DatePickerDialog mDatePicker = new DatePickerDialog(SubscriptionActivity.this, date, myCalendar
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
                "OK", new DialogInterface.OnClickListener() {
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

                        }
                    }
                });
        mDatePicker.setCancelable(false);
        mDatePicker.show();
    }
*/
}
