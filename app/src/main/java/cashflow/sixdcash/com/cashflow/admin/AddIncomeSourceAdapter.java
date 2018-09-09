package cashflow.sixdcash.com.cashflow.admin;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cashflow.sixdcash.com.cashflow.R;
import cashflow.sixdcash.com.cashflow.manager.DBManager;
import cashflowdb.HowOften;
import cashflowdb.InOutTransaction;
import cashflowdb.RepeatTransaction;
import cashflowdb.Source;


/**
 * Created by Praveen on 10/4/2016.
 */
public class AddIncomeSourceAdapter extends BaseAdapter {

    Context mContext;
    List<Source> selccto;
    private ToAndCCChangeListener listener;

    public interface ToAndCCChangeListener {
        public void changeCCAndTO(int pos);
    }

    public AddIncomeSourceAdapter(Context context, List<Source> selccto, ToAndCCChangeListener listener) {
        mContext = context;
        this.selccto = selccto;
        this.listener = listener;
    }


    @Override
    public int getCount() {
        return selccto.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final HolderView holder;
        if (convertView == null) {
            holder = new HolderView();
            convertView =null;

            //convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.selsourcesitem, parent, false);
            if(isTablet(mContext)) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.selsourcesitem, parent, false);
            }
            else{
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.selsourcesitem_phone, parent, false);
            }

            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.cancel = (ImageView) convertView.findViewById(R.id.cancel);
            holder.checkedsource = (CheckBox) convertView.findViewById(R.id.checkedsource);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
            holder.checkedsource.setOnCheckedChangeListener(null);
        }
        holder.checkedsource.setTag(position);

        Source userSource = selccto.get(position);
        holder.name.setText(userSource.getSourcename());
        holder.checkedsource.setChecked(userSource.getIsChecked());

        holder.checkedsource.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Long SID = selccto.get(pos).getId();
                DBManager.checkSourceBySourceID(mContext, SID, b);


                int getPosition = (Integer) compoundButton.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                selccto.get(getPosition).setIsChecked(compoundButton.isChecked()); // Set the value of checkbox to maintain its state.

            }
        });

       /* holder.checkedsource.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Long SID = selccto.get(pos).getId();
                DBManager.checkSourceBySourceID(mContext, SID, b);

            }
        });*/

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                tvMsg.setText("Are you sure, You want Delete it ?\n" +
                        "It will delete All transaction related to it.");

                tvNO.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
                tvYes.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Long SID = selccto.get(pos).getId();
                        List<HowOften> howOftens=selccto.get(pos).getHowOftenList();
                        List<InOutTransaction> transactions=selccto.get(pos).getInOutTransactionList();

                        for(int y=0;y<transactions.size();y++){

                            List<RepeatTransaction> repeatTransactions=transactions.get(y).getRepeatTransactionList();
                            for(int t=0;t<repeatTransactions.size();t++){

                                repeatTransactions.get(t).delete();
                            }

                            transactions.get(y).delete();
                        }

                        for(int z=0;z<howOftens.size();z++){
                            howOftens.get(z).delete();
                        }

                        selccto.get(pos).delete();
                        listener.changeCCAndTO(pos);

                        if (dialog != null) {
                            dialog.dismiss();
                        }

                    }
                });
                dialog.show();



            }
        });

        return convertView;
    }

    public class HolderView {

        TextView name;
        ImageView cancel;
        CheckBox checkedsource;

    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
}
