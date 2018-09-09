package cashflow.sixdcash.com.cashflow;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import cashflow.sixdcash.com.cashflow.Utility.DateUtility;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AppEULA appEULA = new AppEULA(this);


        //if With subscription app required
/*        if (!checkSubsValid(DateUtility.getDateBYStringDate("30-06-2017")))
        {
            Intent intent = new Intent(SplashActivity.this, HostActivty.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(SplashActivity.this, SubscriptionActivity.class);
            startActivity(intent);
            finish();

        }*/


//        if With subscription app not required


        new AsyncTask<Object, Void, Void>() {

            @Override
            protected Void doInBackground(Object... params) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.run();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (!checkSubsValid(DateUtility.getDateBYStringDate("30-12-2020"))) {
                    if (appEULA.getAULATermsAccepted()) {
                        Intent intent = new Intent(SplashActivity.this, HostActivty.class);
                        startActivity(intent);
                        finish();
                    } else {
                        appEULA.show();
                    }
                } else {
                    Intent intent = new Intent(SplashActivity.this, SubscriptionActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        }.execute();
    }


    private boolean checkSubsValid(Date subsEndDate) {
        if (null != subsEndDate) {
            int diffInDays = (int) ((subsEndDate.getTime() - DateUtility.getTodayDate().getTime()) / (1000 * 60 * 60 * 24));
            if (diffInDays <= 0) {
                return true;//negative for end of subs
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        SplashActivity.this.finish();
    }
}
