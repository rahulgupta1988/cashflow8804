package cashflow.sixdcash.com.cashflow.admin;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cashflow.sixdcash.com.cashflow.R;
import cashflow.sixdcash.com.cashflow.Utility.ListUtility;
import cashflow.sixdcash.com.cashflow.Utility.ValidationUtility;
import cashflow.sixdcash.com.cashflow.manager.DBManager;
import cashflowdb.Category;
import cashflowdb.Source;

/**
 * Created by Praveen on 10/4/2016.
 */
public class AdminHomeContainer extends Fragment implements View.OnClickListener {

    Context mContext;
    ViewGroup mcontainer;
    View view;
    LinearLayout incomeinputview;
    AnimatorSet set;

    // Source and HowOfter Spinner
    Category categories;
    List<Source> sources;
    AddIncomeSourceAdapter addIncomeSourceAdapter = null;
    int atLoadTime = 0;
    String catName;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mcontainer = container;
        view = inflater.inflate(R.layout.adminconrainerview, container, false);
        catName = getArguments().getString("catname");

        categories = DBManager.getCategoryByName(mContext, catName);
        sources = categories.getSourceList();
        init();
        return view;
    }

    public void init() {
        incomeinputview = (LinearLayout) view.findViewById(R.id.incomeinputview);
        addInputView();
    }


    public void setViewAsCategory(String nowCatName) {
        if (sources != null)
            sources.clear();

        catName = nowCatName;
        categories = DBManager.getCategoryByName(mContext, nowCatName);
        sources = categories.getSourceList();
        atLoadTime = 0;
        incomeinputview.removeAllViews();
        addInputView();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = (Context) activity;
    }

    int viewID = 0;

    public void addInputView() {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View incomeinputitemView = null;

        //incomeinputitemView = inflater.inflate(R.layout.admincontainerinput, null, false);

        if(isTablet(mContext)) {
            incomeinputitemView = inflater.inflate(R.layout.admincontainerinput, null, false);
        }
        else{
            incomeinputitemView = inflater.inflate(R.layout.admincontainerinput_phone, null, false);
        }


        final LinearLayout maininputlay = (LinearLayout) incomeinputitemView.findViewById(R.id.maininputlay);

        final ListView slelctedincomesouceslist = (ListView) incomeinputitemView.findViewById(R.id.slelctedincomesouceslist);

        final EditText sourcfromedit = (EditText) incomeinputitemView.findViewById(R.id.sourcfromedit);
        final Button addbtn = (Button) incomeinputitemView.findViewById(R.id.addbtn);


        addIncomeSourceAdapter = new AddIncomeSourceAdapter(mContext, sources, new AddIncomeSourceAdapter.ToAndCCChangeListener() {
            @Override
            public void changeCCAndTO(int pos) {
                sources.remove(pos);
                addIncomeSourceAdapter.notifyDataSetChanged();
                //ListUtility.getListViewSize(slelctedincomesouceslist);
            }
        });

        slelctedincomesouceslist.setAdapter(addIncomeSourceAdapter);
        //ListUtility.getListViewSize(slelctedincomesouceslist);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sourceName = sourcfromedit.getText().toString().trim();
                if (ValidationUtility.validEditTextString(sourceName)) {
                    DBManager.insertSinfleSource(mContext, sourceName.trim(), catName);
                    categories = DBManager.getCategoryByName(mContext, catName);
                    List<Source> temp_sources = categories.getSourceList();
                    sources.add(temp_sources.get(temp_sources.size() - 1));
                    addIncomeSourceAdapter.notifyDataSetChanged();
                    //ListUtility.getListViewSize(slelctedincomesouceslist);
                    DBManager.insertHowOftenForSingleSource(mContext, sourceName,categories.getId());
                    sourcfromedit.setText("");
                } else {
                    Toast.makeText(mContext, "Enter valid source.", Toast.LENGTH_LONG).show();
                }
            }
        });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        incomeinputitemView.setLayoutParams(params);
        incomeinputview.setTag(viewID);
        incomeinputview.addView(incomeinputitemView);
        viewID++;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {



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


    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

}
