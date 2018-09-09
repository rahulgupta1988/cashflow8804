package cashflow.sixdcash.com.cashflow;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cashflow.sixdcash.com.cashflow.Utility.ConnectionDetector;
import cashflow.sixdcash.com.cashflow.Utility.ValidationUtility;
import cashflow.sixdcash.com.cashflow.manager.DBManager;

public class AppEULA {
    private String EULA_PREFIX = "MY_MONEY_APP";
    private Context mContext;

    public AppEULA(Context context) {
        mContext = context;
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

    @TargetApi(Build.VERSION_CODES.M)
    public void show() {
        boolean bAlreadyAccepted = getAULATermsAccepted();
        if (bAlreadyAccepted == false) {

            if (ConnectionDetector.isConnectingToInternet(mContext)) {
                View promptsView = LayoutInflater.from(mContext).inflate(R.layout.terms_and_conditions, null);

                final Button iAgreeButton = (Button) promptsView.findViewById(R.id.iAgreeButton);
                iAgreeButton.setEnabled(false);
                iAgreeButton.setTextColor(Color.parseColor("#b6b6ba"));
                Button dontAgree = (Button) promptsView.findViewById(R.id.dontAgree);
                TextView tnCURLtv = (TextView) promptsView.findViewById(R.id.tnc_url);
                final TextInputEditText userName_editText = (TextInputEditText) promptsView.findViewById(R.id.userName_editText);
                userName_editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        if (null != userName_editText.getText()) {
                            boolean isReady = userName_editText.getText().toString().length() > 3;
                            if (isReady) {
                                iAgreeButton.setEnabled(isReady);
                                iAgreeButton.setTextColor(Color.parseColor("#0297F1"));
                            } else {
                                iAgreeButton.setEnabled(false);
                                iAgreeButton.setTextColor(Color.parseColor("#b6b6ba"));
                            }
                        } else {
                            iAgreeButton.setEnabled(false);

                            iAgreeButton.setTextColor(Color.parseColor("#b6b6ba"));
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                tnCURLtv.setClickable(true);
                tnCURLtv.setMovementMethod(LinkMovementMethod.getInstance());

//                Terms and Conditions – [URL]

                String text = "<font color='#0297F1'><a href='http://flickerassociates.co.uk/terms-and-conditions/'>Terms and Conditions</a></font>";
                tnCURLtv.setText(Html.fromHtml(text));

                final Dialog dialog = new AlertDialog.Builder(mContext)
                        .setCancelable(false).setView(promptsView).create();
                // (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.show();
                dialog.getWindow().setAttributes(lp);

                iAgreeButton.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        setSharedPreferencesEULA(true);
                        String firstname = userName_editText.getText().toString().trim();


                        if (ValidationUtility.validEditTextString(firstname)) {
                            String str = firstname;
                            String cap_firstname = str.substring(0, 1).toUpperCase() + str.substring(1);
                            String[] firstName_dialog=cap_firstname.split(" ");
                            DBManager.UpdateUserInfo(mContext, cap_firstname, "", "");
                        }

                        ((Activity) mContext).finish();
                        Intent intent = new Intent(mContext, HostActivty.class);
                        mContext.startActivity(intent);
                        dialog.cancel();
                    }
                });

                dontAgree.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        ((Activity) mContext).finish();
                        dialog.cancel();
                    }
                });
                dialog.show();
            } else {
                View promptsView = LayoutInflater.from(mContext).inflate(R.layout.no_network_connection, null);
                TextView no_network_ok = (TextView) promptsView.findViewById(R.id.no_network_ok);


                final Dialog dialog = new AlertDialog.Builder(mContext)
                        .setCancelable(false).setView(promptsView).create();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(lp);

                no_network_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((Activity) mContext).finish();
                        dialog.cancel();
                    }
                });

                dialog.show();

            }


        }
    }


    public void setSharedPreferencesEULA(boolean accepted) {
        PackageInfo versionInfo = getPackageInfo();

        // The eulaKey changes every time you increment the version number in
        // the AndroidManifest.xml
        final String eulaKey = EULA_PREFIX + versionInfo.versionName;
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs
                .edit();
        editor.putBoolean(eulaKey, true);
        editor.commit();
    }

    public boolean getAULATermsAccepted() {
        PackageInfo versionInfo = getPackageInfo();

        // The eulaKey changes every time you increment the version number in
        // the AndroidManifest.xml
        final String eulaKey = EULA_PREFIX + versionInfo.versionName;
        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(mContext);

        return prefs.getBoolean(eulaKey, false);
    }

   /* private class AppBrowser extends WebViewClient {
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d(AppEULA.class.getSimpleName(), failingUrl + " des " + description + " code " + errorCode);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!Uri.parse(url).getHost().equals("academia.infominez.com")) {
                return false;
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(AppEULA.class.getSimpleName(), " url " + url);
            super.onPageFinished(view, url);
        }
    }*/
}