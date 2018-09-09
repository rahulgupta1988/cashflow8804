package cashflow.sixdcash.com.cashflow.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cashflow.sixdcash.com.cashflow.HostActivty;
import cashflow.sixdcash.com.cashflow.R;
import cashflow.sixdcash.com.cashflow.TransactionPanel;
import cashflow.sixdcash.com.cashflow.Utility.DateUtility;

import cashflow.sixdcash.com.cashflow.Utility.TransactionPanelbean;
import cashflow.sixdcash.com.cashflow.adapter.TransactonPanelAdapter;
import cashflow.sixdcash.com.cashflow.manager.DBManager;
import cashflow.sixdcash.com.cashflow.manager.DayInOutTransactionManager;
import cashflow.sixdcash.com.cashflow.manager.TransactionPanelManager;
import cashflowdb.InOutTransaction;
import cashflowdb.RepeatTransaction;

/**
 * Created by Praveen on 21-Oct-16.
 */

public class TrasactionPanelFragment extends Fragment {

    Context mContext;
    View view,headertilelay;
    TextView empty_view;
    RecyclerView trasactionlist;
    Button addmoreitems;

    List<InOutTransaction> inOutTransactions;
    List<RepeatTransaction> repeatTransactions;
    List<TransactionPanelbean> transactionPanelbeanList;
    List<TransactionPanelbean> filtered_transactionPanelbeanList;

    String catName="";
    private TextView hyperLinkTV;
    private TextView copyRightTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //view = inflater.inflate(R.layout.trasacationpanel, container, false);
        if(isTablet(mContext)) {
            view = inflater.inflate(R.layout.trasacationpanel, container, false);
        }
        else{
            view = inflater.inflate(R.layout.trasacationpanel_phone, container, false);
        }



        catName  = getArguments().getString("catname");
        empty_view=(TextView)view.findViewById(R.id.empty_view);
        headertilelay=(View)view.findViewById(R.id.headertilelay);
        trasactionlist=(RecyclerView)view.findViewById(R.id.trasactionlist);
        addmoreitems=(Button)view.findViewById(R.id.addmoreitems);
        hyperLinkTV=(TextView)view.findViewById(R.id.hyperLinkTV);
        copyRightTV=(TextView)view.findViewById(R.id.copyRightTV);
        hyperLinkTV.setClickable(true);
        hyperLinkTV.setMovementMethod(LinkMovementMethod.getInstance());
        copyRightTV.setClickable(true);
        copyRightTV.setMovementMethod(LinkMovementMethod.getInstance());

        String text = "Built by "+"<font color='#0297F1'><a href='http://www.wsipolarisdigital.com'>WSI Polaris Digital Marketing</a></font>";
        String copyRightText = "<b>My Money Planner Version "+getPackageInfo().versionName+"</b><br><a href='http://www.flickerassociates.co.uk'>© Flicker Associates 2018</a>";
        hyperLinkTV.setText(Html.fromHtml(text));
        copyRightTV.setText(Html.fromHtml(copyRightText));
        addmoreitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adminhome=new Intent(mContext, HostActivty.class);
                adminhome.putExtra("catName",catName);
                startActivity(adminhome);
                ((Activity)mContext).finish();
                ((Activity)mContext).overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });
        new InitBackground().execute();
        return view;
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
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext=(Context)activity;
    }

    public void setViewAsCategory(String nowCatName) {
        catName=nowCatName;
    }

    public void selectedSource(String sourceName){

        if(filtered_transactionPanelbeanList==null) {
            filtered_transactionPanelbeanList = new ArrayList<TransactionPanelbean>();
        }
        filterTransactionByCatName(catName, sourceName);
        //Toast.makeText(mContext, "" + sourceName, Toast.LENGTH_LONG).show();

    }


    public void getAllTransaction(){

        inOutTransactions=new ArrayList<InOutTransaction>();
        repeatTransactions=new ArrayList<RepeatTransaction>();
        transactionPanelbeanList=new ArrayList<TransactionPanelbean>();

        inOutTransactions=DBManager.getTrasaction(mContext);
        repeatTransactions=DBManager.getRepeatTrasaction(mContext);

        for(int i=0;i<inOutTransactions.size();i++){

            InOutTransaction inOutTransaction=inOutTransactions.get(i);
            //if(!inOutTransaction.getIsrepetitive()){
            TransactionPanelbean transactionPanelbean=new TransactionPanelbean();
            if(!inOutTransaction.getIsrepetitive()) {
                transactionPanelbean.setInoutORRepeate("inout");
            }
            else{
                transactionPanelbean.setInoutORRepeate("rept");
            }
            transactionPanelbean.setTrascationID(inOutTransaction.getId());
            transactionPanelbean.setCatname(TransactionPanelManager.getCategoryByID(mContext,inOutTransaction.getCategoryid()));
            transactionPanelbean.setSourcename(TransactionPanelManager.getSourceByID(mContext,inOutTransaction.getSourceid()));

            String aDouble="";
            try {
                DecimalFormat df = new DecimalFormat("0.00");
                Double money=inOutTransaction.getPound();
                aDouble=df.format(money);
            } catch (Exception e) {
                e.printStackTrace();
            }

            transactionPanelbean.setPound(aDouble);
            transactionPanelbean.setHowoften(inOutTransaction.getHowoften());
            transactionPanelbean.setDate(DateUtility.getStringBYDate(inOutTransaction.getTransactiondate()));
            transactionPanelbean.setComments_str(inOutTransaction.getComment());
            transactionPanelbeanList.add(transactionPanelbean);
        }
        // }


       /* for(int j=0;j<repeatTransactions.size();j++){

            RepeatTransaction repeatTransaction=repeatTransactions.get(j);

            TransactionPanelbean transactionPanelbean=new TransactionPanelbean();

            transactionPanelbean.setInoutORRepeate("rept");
            transactionPanelbean.setTrascationID(repeatTransaction.getId());
            transactionPanelbean.setCatname(TransactionPanelManager.getCategoryByID(mContext,repeatTransaction.getCatID()));
            transactionPanelbean.setSourcename(TransactionPanelManager.getSourceByTranID(mContext,repeatTransaction.getActualtrasactionID()));

            String aDouble="";
            try {
                DecimalFormat df = new DecimalFormat("0.00");
                Double money=repeatTransaction.getPound();
                aDouble=df.format(money);
            } catch (Exception e) {
                e.printStackTrace();
            }

            transactionPanelbean.setPound(aDouble);
            transactionPanelbean.setHowoften(TransactionPanelManager.getHowOftenTranID(mContext,repeatTransaction.getActualtrasactionID()));
            transactionPanelbean.setDate(DateUtility.getStringBYDate(repeatTransaction.getTransactiondate()));
            //transactionPanelbean.setComments_str(TransactionPanelManager.getComentByTranID(mContext,repeatTransaction.getActualtrasactionID()));
            transactionPanelbean.setComments_str(repeatTransaction.getRecomments());

            transactionPanelbeanList.add(transactionPanelbean);
        }
*/
    }

    public void filterTransactionByCatName(String catName,String sourceName){

        if(filtered_transactionPanelbeanList!=null) {
            filtered_transactionPanelbeanList.clear();
            for (int n = 0; n < transactionPanelbeanList.size(); n++) {
                TransactionPanelbean transactionPanelbean = transactionPanelbeanList.get(n);
                if (!sourceName.equals("All")) {
                    if (transactionPanelbean.getCatname().equals(catName) && transactionPanelbean.getSourcename().equals(sourceName)) {
                        filtered_transactionPanelbeanList.add(transactionPanelbean);
                    }
                } else {
                    if (transactionPanelbean.getCatname().equals(catName)) {
                        filtered_transactionPanelbeanList.add(transactionPanelbean);
                    }
                }
            }
        }

        //Toast.makeText(mContext,"count "+filtered_transactionPanelbeanList.size(), Toast.LENGTH_LONG).show();
        setTransactionPanelAdapter();

    }

    TransactonPanelAdapter adapter=null;
    private void setTransactionPanelAdapter() {

        if(adapter!=null)
            adapter=null;

        adapter = new TransactonPanelAdapter(mContext, filtered_transactionPanelbeanList, new TransactonPanelAdapter.RemoveItemListener() {
            @Override
            public void removedItemID(int pos) {
                transactionPanelbeanList.remove(pos);
            }
        });
        adapter.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        trasactionlist.setLayoutManager(layoutManager);
        trasactionlist.setItemAnimator(new DefaultItemAnimator());
        trasactionlist.setAdapter(adapter);

        if(adapter.getItemCount()>0){
            empty_view.setVisibility(View.GONE);
            // headertilelay.setVisibility(View.VISIBLE);
            trasactionlist.setVisibility(View.VISIBLE);
        }

        else{
            // headertilelay.setVisibility(View.GONE);
            trasactionlist.setVisibility(View.GONE);
            empty_view.setVisibility(View.VISIBLE);
        }




    }


    class InitBackground extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            getAllTransaction();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            filterTransactionByCatName(catName,"All");
        }
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    ProgressDialog progressDialog=null;

    public void updateTrasactions() {

        if (trasactionlist.getAdapter().getItemCount() > 0){
            progressDialog = initProgressDialog();
            progressDialog.show();
        }


        for (int init =0;init<trasactionlist.getAdapter().getItemCount();init++) {
            Log.i("12232 i",""+init);

            RecyclerView.ViewHolder viewHolder = trasactionlist.findViewHolderForAdapterPosition(init);
            TextView pounds = (TextView) viewHolder.itemView.findViewById(R.id.pounds);
            TextView date = (TextView) viewHolder.itemView.findViewById(R.id.date);
            TextView comment = (TextView) viewHolder.itemView.findViewById(R.id.comment);
            TextView howoften = (TextView) viewHolder.itemView.findViewById(R.id.howoften);



         /*   Log.i("pounds", "" + pounds.getText().toString());
            Log.i("date", "" + date.getText().toString());
            Log.i("comment", "" + comment.getText().toString());
            Log.i("howoften", "" + howoften.getText().toString());*/

            String  amout  = pounds.getText().toString();
            String  date_txt = date.getText().toString();
            String  comments = comment.getText().toString();
            String  howOften=howoften.getText().toString();

            if (amout == null || amout.equals("")) {
                amout="0.0";
            }

            filtered_transactionPanelbeanList.get(init).setPound(amout);
            filtered_transactionPanelbeanList.get(init).setDate(date_txt);
            filtered_transactionPanelbeanList.get(init).setComments_str(comments);
            filtered_transactionPanelbeanList.get(init).setHowoften(howOften);

            // new TrasactionUpdateTask(init,amout,date_txt,comments,howOften).execute();
        }
    }

    public class TrasactionUpdateTask extends AsyncTask<Void,Void,Void>{
        String  amout  = "";
        String  date_txt = "";
        String  comments = "";
        String  howOften="";
        int init=0;
        public  TrasactionUpdateTask(int init,String amout,String date_txt,String comments,String howOften){
            this.init=init;
            this.amout=amout;
            this.date_txt=date_txt;
            this.comments=comments;
            this.howOften=howOften;
        }
        @Override
        protected Void doInBackground(Void... integers) {
            TransactionPanelManager.UpdateTranscation(mContext, filtered_transactionPanelbeanList.get(init).getTrascationID(), amout, date_txt, comments,howOften);
            new DayInOutTransactionManager(mContext).insertDayInOut();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(init==(trasactionlist.getAdapter().getItemCount()-1)){

                adapter.notifyDataSetChanged();
                progressDialog.dismiss();

               /* new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected Void doInBackground(Void... voids) {
                        new DayInOutTransactionManager(mContext).insertDayInOut();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                }.execute();*/
            }
        }
    }


    public ProgressDialog initProgressDialog() {
        String s = "Updating...";
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
}
