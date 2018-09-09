package cashflow.sixdcash.com.cashflow.adapter;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cashflow.sixdcash.com.cashflow.R;
import cashflow.sixdcash.com.cashflow.Utility.DateUtility;
import cashflow.sixdcash.com.cashflow.Utility.TransactionPanelbean;
import cashflow.sixdcash.com.cashflow.manager.DayInOutTransactionManager;
import cashflow.sixdcash.com.cashflow.manager.TransactionPanelManager;
import cashflowdb.DayInOutSummery;
import cashflowdb.HowOften;
import cashflowdb.InOutTransaction;

/**
 * Created by Praveen on 19-Oct-16.
 */

public class TransactonPanelAdapter extends RecyclerView.Adapter<TransactonPanelAdapter.TrasactionPanelHolder> {

    Context mContext;
    TransactonPanelAdapter transactonPanelAdapter;
    List<TransactionPanelbean> transactionPanelbeanList;
    RemoveItemListener removalListener;

    public interface RemoveItemListener {
        public void removedItemID(int pos);
    }


    public TransactonPanelAdapter(Context context, List<TransactionPanelbean> transactionPanelbeanList, RemoveItemListener removalListener) {

        mContext = context;
        this.removalListener = removalListener;
        this.transactionPanelbeanList = transactionPanelbeanList;
        //Toast.makeText(mContext,""+transactionPanelbeanList.size(),Toast.LENGTH_LONG).show();

    }

    @Override
    public TrasactionPanelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= null;


        //itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.transactionpanelviewitem, parent, false);

        if(isTablet(mContext)) {
            itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.transactionpanelviewitem, parent, false);
        }
        else{
            itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.transactionpanelviewitem_phone, parent, false);
        }


        return new TrasactionPanelHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TrasactionPanelHolder holder, final int position) {

      /*  if (position % 2 == 0) {
            holder.parentlay.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            holder.parentlay.setBackgroundColor(Color.parseColor("#d0ecfd"));
        }*/


        TransactionPanelbean transactionPanelbean = transactionPanelbeanList.get(position);

        holder.sn.setText("" + (position + 1));
        holder.catname.setText("" + transactionPanelbean.getCatname());
        holder.sourcename.setText("" + transactionPanelbean.getSourcename());
        holder.pounds.setText("" + transactionPanelbean.getPound());
        String aDouble = "";

       /* try {
            DecimalFormat df = new DecimalFormat("0.00");
            Double money=Double.parseDouble(transactionPanelbean.getHowoften());
            aDouble=df.format(money);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

//        holder.howoften.setText(""+aDouble);
        holder.date.setText("" + transactionPanelbean.getDate());
        if (transactionPanelbean.getComments_str() != null && !transactionPanelbean.getComments_str().equals("")) {
            holder.comment.setText("" + transactionPanelbean.getComments_str());
        } else {
            holder.comment.setText("No Comment");
        }
        if (transactionPanelbean.getHowoften() != null && !transactionPanelbean.getHowoften().equals("")) {
            holder.howoften.setText("" + transactionPanelbean.getHowoften());
        } else {
            holder.howoften.setText("How Often?");
        }

        holder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initdate(holder.date, holder.date.getText().toString());
            }
        });
        final InOutTransaction howOftenIOT = TransactionPanelManager.getInOutTransationById(transactionPanelbean.getTrascationID(), mContext);

        holder.howoften.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
                builderSingle.setTitle("Select Frequency");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,
                        android.R.layout.select_dialog_item);
                if (null != howOftenIOT) {
                    if (null != howOftenIOT.getSource() && howOftenIOT.getSource().getHowOftenList().size() > 0) {
                        for (HowOften howOften : howOftenIOT.getSource().getHowOftenList())
                            arrayAdapter.add(howOften.getOften());
                    }
                }


                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        holder.howoften.setText(strName);
                    }
                });
                builderSingle.show();
            }
        });
        InOutTransaction inOutTransaction = TransactionPanelManager.getInOutTransationById(transactionPanelbean.getTrascationID(), mContext);
        if (null != inOutTransaction) {

            if (null != inOutTransaction.getSource() && null!=inOutTransaction.getSource().getIsMandatory() && (!inOutTransaction.getSource().getIsMandatory())) {
                String typeOfCategory = TransactionPanelManager.getCategoryType(mContext, inOutTransaction.getCategoryid());
                Log.d("nehal: catType", typeOfCategory);
                switch (typeOfCategory) {
                    case "notInOut":
                        break;
                    case "income":
                        if (inOutTransaction.getIsrepetitive()) {
                            DayInOutSummery incomeRecurring = new DayInOutTransactionManager(mContext).getWorstDayInOut();
                            if (null != incomeRecurring) {
                                if (incomeRecurring.getBalance_in_hand() < 0.0) {
                                    int diffInDays = (int) ((incomeRecurring.getDate().getTime() - inOutTransaction.getTransactiondate().getTime()) / (1000 * 60 * 60 * 24));
                                    if (diffInDays >= 0) {
                                        holder.hintsAndTips.setVisibility(View.VISIBLE);
                                        holder.hintsAndTips.setText("Can you work more hours?");
                                        holder.hintsAndTips.setBackgroundColor(mContext.getResources().getColor(R.color.darkgreen));
                                    } else {
                                        holder.hintsAndTips.setVisibility(View.GONE);
                                    }
                                } else {
                                    holder.hintsAndTips.setVisibility(View.GONE);

                                }
                            } else {
                                holder.hintsAndTips.setVisibility(View.GONE);

                            }
                        } else {
                            DayInOutSummery incomeNonRecurring = new DayInOutTransactionManager(mContext).getWorstDayInOut();
                            if (null != incomeNonRecurring) {
                                if (incomeNonRecurring.getBalance_in_hand() < 0.0) {
                                    DayInOutSummery firstDebt = new DayInOutTransactionManager(mContext).getFirstDebt();
                                    if (null != firstDebt) {
                                        int diffInDays = (int) ((inOutTransaction.getTransactiondate().getTime() - firstDebt.getDate().getTime()) / (1000 * 60 * 60 * 24));
                                        if (diffInDays >= 0) {
                                            holder.hintsAndTips.setVisibility(View.VISIBLE);
                                            holder.hintsAndTips.setText("Can this item be sold sooner?");
                                            holder.hintsAndTips.setBackgroundColor(mContext.getResources().getColor(R.color.darkgreen));
                                        } else {
                                            holder.hintsAndTips.setVisibility(View.GONE);
                                        }
                                    } else {
                                        holder.hintsAndTips.setVisibility(View.GONE);
                                    }
                                } else {
                                    holder.hintsAndTips.setVisibility(View.GONE);
                                }
                            } else {
                                holder.hintsAndTips.setVisibility(View.GONE);

                            }
                        }
                        break;
                    case "expend":
                        if (inOutTransaction.getIsrepetitive()
                                && !(inOutTransaction.getSource().getSourcename().equalsIgnoreCase("Gas") || inOutTransaction.getSource().getSourcename().equalsIgnoreCase("Water"))) {
                            DayInOutSummery incomeRecurring = new DayInOutTransactionManager(mContext).getWorstDayInOut();
                            if (null != incomeRecurring) {
                                if (incomeRecurring.getBalance_in_hand() < 0.0) {
                                    int diffInDays = (int) ((incomeRecurring.getDate().getTime() - inOutTransaction.getTransactiondate().getTime()) / (1000 * 60 * 60 * 24));
                                    if (diffInDays >= 1) {
                                        holder.hintsAndTips.setVisibility(View.VISIBLE);
                                        holder.hintsAndTips.setText("Can this be reduced or paid later?");
                                        holder.hintsAndTips.setBackgroundColor(mContext.getResources().getColor(R.color.red));

                                    } else {
                                        holder.hintsAndTips.setVisibility(View.GONE);
                                    }
                                } else {
                                    holder.hintsAndTips.setVisibility(View.GONE);

                                }
                            } else {
                                holder.hintsAndTips.setVisibility(View.GONE);

                            }
                        } else {
                            DayInOutSummery incomeNonRecurring = new DayInOutTransactionManager(mContext).getWorstDayInOut();
                            if (null != incomeNonRecurring
                                    && !(inOutTransaction.getSource().getSourcename().equalsIgnoreCase("Gas") || inOutTransaction.getSource().getSourcename().equalsIgnoreCase("Water"))) {
                                if (incomeNonRecurring.getBalance_in_hand() < 0.0 && incomeNonRecurring.getExpense_pound() > 0.0) {
                                    DayInOutSummery firstDebt = new DayInOutTransactionManager(mContext).getFirstDebt();
                                    if (null != firstDebt) {
                                        int diffInDays = (int) ((inOutTransaction.getTransactiondate().getTime() - firstDebt.getDate().getTime()) / (1000 * 60 * 60 * 24));
                                        if (diffInDays <= 0) {
                                            holder.hintsAndTips.setVisibility(View.VISIBLE);
                                            holder.hintsAndTips.setText("Can this be reduced or paid later?");
                                            holder.hintsAndTips.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                                        } else {
                                            holder.hintsAndTips.setVisibility(View.GONE);
                                        }
                                    } else {
                                        holder.hintsAndTips.setVisibility(View.GONE);
                                    }

                                } else {
                                    holder.hintsAndTips.setVisibility(View.GONE);

                                }
                            } else {
                                holder.hintsAndTips.setVisibility(View.GONE);

                            }
                        }

                        break;
                }
            }
        }
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(mContext);
                Window window = dialog.getWindow();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.confirmdialog);
                window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);

                if(isTablet(mContext)){
                    window.setLayout(450, ActionBar.LayoutParams.WRAP_CONTENT);
                }

                else{
                    window.setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                }


                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                TextView tvMsg = (TextView) window.findViewById(R.id.msg_yesno);
                TextView tvNO = (TextView) window.findViewById(R.id.no_yesno);
                TextView tvYes = (TextView) window.findViewById(R.id.yes_yesno);
                tvMsg.setText("Are you sure, you want to delete it?");

                tvNO.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
                tvYes.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {


                        // transactionPanelbeanList.remove(position);
                        // transactonPanelAdapter.notifyDataSetChanged();
                        // removalListener.removedItemID(position);
//                        if (transactionPanelbeanList.get(position).getInoutORRepeate().equals("inout")) {
//                            TransactionPanelManager.deleteRETranscationByInOutTraID(mContext, transactionPanelbeanList.get(position).getTrascationID());
//                            TransactionPanelManager.deleteTranscation(mContext, transactionPanelbeanList.get(position).getTrascationID());
//                            transactionPanelbeanList.remove(position);
//                            transactonPanelAdapter.notifyDataSetChanged();
//                            removalListener.removedItemID(position);
//                        } else {
                            TransactionPanelManager.deleteRETranscationByInOutTraID(mContext, transactionPanelbeanList.get(position).getTrascationID());
                            TransactionPanelManager.deleteTranscation(mContext, transactionPanelbeanList.get(position).getTrascationID());
                            transactionPanelbeanList.remove(position);
                            transactonPanelAdapter.notifyDataSetChanged();
                            removalListener.removedItemID(position);

//                        }
                        new DayInOutTransactionManager(mContext).insertDayInOut();
                    /*    else{
                            TransactionPanelManager.deleteRETranscation(mContext,transactionPanelbeanList.get(position).getTrascationID());
                            transactionPanelbeanList.remove(position);
                            transactonPanelAdapter.notifyDataSetChanged();
                            removalListener.removedItemID(position);
                        }*/
                        Toast.makeText(mContext, "Transaction deleted.", Toast.LENGTH_LONG).show();
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }


                });
                dialog.show();

            }
        });

        holder.pounds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                holder.pounds.setHint("");
                String temptxt = editable.toString();
                if (temptxt.length() > 0) {

                    holder.poundsym.setVisibility(View.VISIBLE);

                    if (temptxt.contains(".")) {
                        int dotindex = temptxt.indexOf(".");
                        if (temptxt.length() > (dotindex + 1))
                            holder.pencesym.setVisibility(View.GONE);
                    } else {
                        holder.pencesym.setVisibility(View.GONE);
                    }


                } else {
                    holder.pounds.setHint("How Much?");
                    holder.pencesym.setVisibility(View.GONE);
                    holder.poundsym.setVisibility(View.GONE);
                }


            }
        });


        holder.confbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String amout = holder.pounds.getText().toString();
                final String date = holder.date.getText().toString();
                final String comments = holder.comment.getText().toString();
                final String howOften=holder.howoften.getText().toString();

                if (amout != null && !amout.equals("")) {

                    if (date != null && !date.equals("")) {
                        transactionPanelbeanList.get(position).setPound(amout);
                        transactionPanelbeanList.get(position).setDate(date);
                        transactionPanelbeanList.get(position).setComments_str(comments);
                        transactionPanelbeanList.get(position).setHowoften(howOften);

//                        if (transactionPanelbeanList.get(position).getInoutORRepeate().equals("inout")) {
//                            TransactionPanelManager.UpdateTranscation(mContext, transactionPanelbeanList.get(position).getTrascationID(), amout, date, comments);
//                        } else {
//                            // TransactionPanelManager.UpdateRETranscation(mContext, transactionPanelbeanList.get(position).getTrascationID(), amout, date,comments);
//                            TransactionPanelManager.UpdateRETranscationByInOutTranID(mContext, transactionPanelbeanList.get(position).getTrascationID(), amout, date, comments);
//                            TransactionPanelManager.UpdateTranscation(mContext, transactionPanelbeanList.get(position).getTrascationID(), amout, date, comments);
//                        }


                        final Long tempPOS=transactionPanelbeanList.get(position).getTrascationID();
                        new AsyncTask<Void,Void,Void>(){

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                progressDialog = initProgressDialog();
                                progressDialog.show();
                            }

                            @Override
                            protected Void doInBackground(Void... voids) {
                                TransactionPanelManager.UpdateTranscation(mContext, tempPOS, amout, date, comments,howOften);
                                new DayInOutTransactionManager(mContext).insertDayInOut();
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                transactonPanelAdapter.notifyDataSetChanged();
                                progressDialog.dismiss();
                                Toast.makeText(mContext, "Transaction Updated", Toast.LENGTH_LONG).show();
                            }
                        }.execute();



                    } else {
                        Toast.makeText(mContext, "Date not be Empty", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(mContext, "Money not be Empty", Toast.LENGTH_LONG).show();
                }




            }


        });

    }


    @Override
    public int getItemCount() {
        if (transactionPanelbeanList != null) return transactionPanelbeanList.size();
        return 0;
    }

    public class TrasactionPanelHolder extends RecyclerView.ViewHolder {

        TextView sn, catname, sourcename, pounds, howoften, date, comment, hintsAndTips;
        TextView poundsym, pencesym;
        Button confbtn;
        LinearLayout parentlay;
        ImageView deletebtn;

        public TrasactionPanelHolder(View itemView) {
            super(itemView);


            parentlay = (LinearLayout) itemView.findViewById(R.id.parentlay);

            sn = (TextView) itemView.findViewById(R.id.sn);
            catname = (TextView) itemView.findViewById(R.id.catname);
            sourcename = (TextView) itemView.findViewById(R.id.sourcename);
            pounds = (TextView) itemView.findViewById(R.id.pounds);
            howoften = (TextView) itemView.findViewById(R.id.howoften);
            date = (TextView) itemView.findViewById(R.id.date);
            comment = (TextView) itemView.findViewById(R.id.comment);
            confbtn = (Button) itemView.findViewById(R.id.confbtn);
            deletebtn = (ImageView) itemView.findViewById(R.id.deletebtn);
            hintsAndTips = (TextView) itemView.findViewById(R.id.hintsAndTips);
            poundsym = (TextView) itemView.findViewById(R.id.poundsym);
            pencesym = (TextView) itemView.findViewById(R.id.pencesym);
        }
    }

    public void initdate(final TextView nextdate, String sel_date) {

        final Calendar myCalendar = Calendar.getInstance();

        if (sel_date != null && !sel_date.equals("")) {

            Date date = DateUtility.getDateBYStringDate(sel_date);
            myCalendar.setTime(date);
        }
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

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


    public void setAdapter(TransactonPanelAdapter adapter) {
        transactonPanelAdapter = adapter;
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
    ProgressDialog progressDialog=null;
    public ProgressDialog initProgressDialog() {
        String s = "Updating...";
        SpannableString ss2 = new SpannableString(s);
        ss2.setSpan(new RelativeSizeSpan(1.3f), 0, ss2.length(), 0);
        ss2.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0, ss2.length(), 0);
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
