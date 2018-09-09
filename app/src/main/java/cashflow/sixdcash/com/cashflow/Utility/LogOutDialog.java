package cashflow.sixdcash.com.cashflow.Utility;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cashflow.sixdcash.com.cashflow.R;

/**
 * Created by Praveen on 9/26/2016.
 */
public class LogOutDialog {
    Dialog dialog = null;

    public void logoutDailogBox(final Context mCtx, final String msg) {
        dialog = new Dialog(mCtx);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmdialog);
        window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);

        if(isTablet(mCtx)){
            window.setLayout(400, ActionBar.LayoutParams.WRAP_CONTENT);
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
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                ((Activity)mCtx).finish();
            }
        });
        dialog.show();
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

}
