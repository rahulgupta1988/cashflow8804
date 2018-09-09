package cashflow.sixdcash.com.cashflow.admin;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cashflow.sixdcash.com.cashflow.HostActivty;
import cashflow.sixdcash.com.cashflow.R;

import cashflow.sixdcash.com.cashflow.adapter.CategoryAdapter;
import cashflow.sixdcash.com.cashflow.manager.DBManager;
import cashflowdb.InOutTransaction;
import cashflowdb.UserInfo;


/**
 * Created by Praveen on 10/4/2016.
 */
public class AdminHome extends AppCompatActivity {

    private Context mContext;
    private String lastClickedCat = "My Income";

    LinearLayout headerlay;
    TextView headertitle;

    RecyclerView catlist;
    FragmentManager frgManager;
    // footer
    LinearLayout buttonplantab, nextweektarget, fourweektarget, howspendmoney;

    // top bar icons

    ImageView settingicon,homeicon,helpicon;
    private TextView withusername;
    private TextView hyperLinkTV;
    private TextView copyRightTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        String mycatName=intent.getStringExtra("catName");

        if(mycatName!=null && !mycatName.equals("")){
            if (!mycatName.equalsIgnoreCase("Start") && !mycatName.equalsIgnoreCase("My Year")) {
                lastClickedCat = mycatName;
            }
        }

        mContext = this;

        //setContentView(R.layout.adminview);

        if(isTablet(mContext)) {
            setContentView(R.layout.adminview);
        }
        else{
            setContentView(R.layout.adminview_phone);
        }


        init();
        setCatAdapter();
    }
    private void setUserName() {
        UserInfo userInfo = DBManager.getUserInfo(mContext);
        if (userInfo != null) {
            String username = userInfo.getFirstname();

            if(isTablet(mContext)){
                if (!username.equals(""))
                    withusername.setText(username + "'s" + "\n  Money Planner");
            }

            else{

            if (!username.equals("")) {
                if (username.length() > 15) {
                    username = username.substring(0, 12);
                    withusername.setText(username + "'s..." + "\n  Money Planner");
                } else {
                    withusername.setText(username + "'s" + "\n  Money Planner");
                }

            }
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getAllTransaction();
    }

    public void init() {

        headerlay=(LinearLayout)findViewById(R.id.headerlay);
        headertitle=(TextView)findViewById(R.id.headertitle);


        String cat_name= "";
        if(lastClickedCat.equalsIgnoreCase("My Income"))
            cat_name="Income";

        else if(lastClickedCat.equalsIgnoreCase("The Basics"))
            cat_name="Basic Expenditure";

        else if(lastClickedCat.equalsIgnoreCase("Major Buys"))
            cat_name="Major Purchases";

        else if(lastClickedCat.equalsIgnoreCase("Keeping Mobile"))
            cat_name="Savings";

        else
            cat_name=lastClickedCat;


        headertitle.setText("My Money Planner - "+cat_name);

        // setting
        settingicon=(ImageView)findViewById(R.id.settingicon);
        settingicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // settingBar();
                onBackPressed();
            }
        });
        homeicon=(ImageView)findViewById(R.id.homeicon);
        homeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastClickedCat="My Year";
                onBackPressed();
            }
        });

        helpicon=(ImageView)findViewById(R.id.helpicon);
        helpicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpBar();
            }
        });

        hyperLinkTV=(TextView)findViewById(R.id.hyperLinkTV);
        copyRightTV=(TextView)findViewById(R.id.copyRightTV);
        hyperLinkTV.setClickable(true);
        hyperLinkTV.setMovementMethod(LinkMovementMethod.getInstance());
        copyRightTV.setClickable(true);
        copyRightTV.setMovementMethod(LinkMovementMethod.getInstance());

        String text = "Built by "+"<font color='#0297F1'><a href='http://www.wsipolarisdigital.com'>WSI Polaris Digital Marketing</a></font>";
        String copyRightText = "<b>My Money Planner Version "+getPackageInfo().versionName+"</b><br><a href='http://www.flickerassociates.co.uk'>© Flicker Associates 2018</a>";
        hyperLinkTV.setText(Html.fromHtml(text));
        copyRightTV.setText(Html.fromHtml(copyRightText));
        buttonplantab = (LinearLayout) findViewById(R.id.buttonplantab);
        nextweektarget = (LinearLayout) findViewById(R.id.nextweektarget);
        fourweektarget = (LinearLayout) findViewById(R.id.fourweektarget);
        howspendmoney = (LinearLayout) findViewById(R.id.howspendmoney);
        withusername = (TextView) findViewById(R.id.withusername);

        setUserName();
        setHeaderColor(lastClickedCat);
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
    private void setCatAdapter() {
        catlist = (RecyclerView) findViewById(R.id.catlist);
        CategoryAdapter adapter = new CategoryAdapter(mContext, categoriesInteface, true,lastClickedCat);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        catlist.setLayoutManager(layoutManager);
        catlist.setItemAnimator(new DefaultItemAnimator());
        catlist.setAdapter(adapter);
        fillContanierInitially();
    }

    CategoryAdapter.CategoriesInteface categoriesInteface = new CategoryAdapter.CategoriesInteface() {

        @Override
        public void selCat(String selcat, int position) {

            String cat_name= "";
            if(selcat.equalsIgnoreCase("My Income"))
                cat_name="Income";

            else if(selcat.equalsIgnoreCase("The Basics"))
                cat_name="Basic Expenditure";

            else if(selcat.equalsIgnoreCase("Major Buys"))
                cat_name="Major Purchases";

            else if(selcat.equalsIgnoreCase("Keeping Mobile"))
                cat_name="Savings";

            else
                cat_name=selcat;

            headertitle.setText("My Money Planner - "+cat_name);
            setHeaderColor(selcat);
            setContainer(selcat);
        }
    };

    String head_color="#99800b";
    public void setHeaderColor(String catName){

        Log.i("catname903242",""+lastClickedCat);
        if (catName.equalsIgnoreCase("Start")) {
            head_color="#99800b";
            headerlay.setBackgroundColor(Color.parseColor("#99800b"));
        } else if (catName.equalsIgnoreCase("My Income")) {
            head_color="#bc2a29";
            headerlay.setBackgroundColor(Color.parseColor("#bc2a29"));
        } else if (catName.equalsIgnoreCase("The Basics")) {
            head_color="#2952d7";
            headerlay.setBackgroundColor(Color.parseColor("#2952d7"));
        } else if (catName.contains("Keeping Mobile")) {
            head_color="#d78729";
            headerlay.setBackgroundColor(Color.parseColor("#d78729"));
        } else if (catName.contains("Clothes and Stuff")) {
            head_color="#1e7a23";
            headerlay.setBackgroundColor(Color.parseColor("#1e7a23"));
        } else if (catName.contains("Entertainment")) {
            head_color="#721ea2";
            headerlay.setBackgroundColor(Color.parseColor("#721ea2"));
        } else if (catName.contains("Major Buys")) {
            head_color="#0b89a4";
            headerlay.setBackgroundColor(Color.parseColor("#0b89a4"));
        } else if (catName.contains("Repayments")) {
            head_color="#163958";
            headerlay.setBackgroundColor(Color.parseColor("#163958"));
        } else if (catName.equalsIgnoreCase("My Year")) {
            head_color="#4c4b43";
            headerlay.setBackgroundColor(Color.parseColor("#4c4b43"));
        }



    }


    private void fillContanierInitially() {
        home = new AdminHomeContainer();
        frgManager = getFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("catname", lastClickedCat);
        home.setArguments(bundle);

        FragmentTransaction ft = frgManager.beginTransaction();
        ft.add(R.id.container, home, "home");
        ft.commit();
    }

    AdminHomeContainer home;

    private void setContainer(String catnamt_str) {

        if (!lastClickedCat.equalsIgnoreCase(catnamt_str)) {
            lastClickedCat = catnamt_str;
            Fragment fragment = frgManager.findFragmentByTag("home");
            if (fragment == null) {
                removeFragment();
                home = new AdminHomeContainer();
                frgManager = getFragmentManager();

                Bundle bundle = new Bundle();
                bundle.putString("catname", catnamt_str);
                home.setArguments(bundle);

                FragmentTransaction ft = frgManager.beginTransaction();
                ft.add(R.id.container, home, "home");
                ft.commit();
            } else {
                home.setViewAsCategory(catnamt_str);
            }

        }

    }

    public void removeFragment() {

        Fragment fragment1 = frgManager.findFragmentByTag("moneyNow");
        Fragment fragment2 = frgManager.findFragmentByTag("home");
        Fragment fragment3 = frgManager.findFragmentByTag("spendingOnBasics");
        Fragment fragment4 = frgManager.findFragmentByTag("moveAndKeppTouch");
        Fragment fragment5 = frgManager.findFragmentByTag("clothAndStuff");
        Fragment fragment6 = frgManager.findFragmentByTag("funAndEntertainment");
        Fragment fragment7 = frgManager.findFragmentByTag("holidaysAndMajorBuys");
        Fragment fragment8 = frgManager.findFragmentByTag("repayment");
        Fragment fragment9 = frgManager.findFragmentByTag("summeryFragment");

        if (fragment1 != null) {
            Log.i("fragment1", "fragment1");
            frgManager.beginTransaction().remove(fragment1).commit();
        } else if (fragment2 != null) {
            Log.i("fragment2", "fragment2");
            frgManager.beginTransaction().remove(fragment2).commit();
        } else if (fragment3 != null) {
            Log.i("fragment3", "fragment3");
            frgManager.beginTransaction().remove(fragment3).commit();
        } else if (fragment4 != null) {
            Log.i("fragment4", "fragment4");
            frgManager.beginTransaction().remove(fragment4).commit();
        } else if (fragment5 != null) {
            Log.i("fragment5", "fragment5");
            frgManager.beginTransaction().remove(fragment5).commit();
        } else if (fragment6 != null) {
            Log.i("fragment6", "fragment6");
            frgManager.beginTransaction().remove(fragment6).commit();
        } else if (fragment7 != null) {
            Log.i("fragment7", "fragment7");
            frgManager.beginTransaction().remove(fragment7).commit();
        } else if (fragment8 != null) {
            Log.i("fragment8", "fragment8");
            frgManager.beginTransaction().remove(fragment8).commit();
        } else if (fragment9 != null) {
            Log.i("fragment9", "fragment9");
            frgManager.beginTransaction().remove(fragment9).commit();
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent temp_HostActivity=new Intent(mContext, HostActivty.class);
        temp_HostActivity.putExtra("catName",lastClickedCat);
        startActivity(temp_HostActivity);
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }


    public void getAllTransaction() {


        List<InOutTransaction> transactions = DBManager.getTrasaction(mContext);

        for (int s = 0; s < transactions.size(); s++) {

            InOutTransaction transaction = transactions.get(s);

            Log.i("transitionID", "" + transaction.getId());
            Log.i("transition Category", "" + transaction.getCategoryid());
            Log.i("transition how often", "" + transaction.getHowoften());
            Log.i("transition Date", "" + transaction.getTransactiondate());
            Log.i("transition next Date", "" + transaction.getNextdate());


        }


    }


    public void settingBar(){

        final PopupWindow changeSortPopUp = new PopupWindow(mContext);
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.settingdailog, null);
        LinearLayout adminlay=(LinearLayout)layout.findViewById(R.id.adminlay);
//        LinearLayout transactionpanellay=(LinearLayout)layout.findViewById(R.id.transactionpanellay);
//        transactionpanellay.setVisibility(View.GONE);
        TextView admintext=(TextView)layout.findViewById(R.id.admintext);
        admintext.setText("Home");

        adminlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSortPopUp.dismiss();
                onBackPressed();
            }
        });


        Rect rc = new Rect();
        settingicon.getWindowVisibleDisplayFrame(rc);
        int[] xy = new int[2];
        settingicon.getLocationInWindow(xy);
        rc.offset(xy[0], xy[1]);
        // Creating the PopupWindow
        changeSortPopUp.setAnimationStyle(R.style.animationName);
        changeSortPopUp.setContentView(layout);
        if(isTablet(mContext)) {
            changeSortPopUp.setWidth(200);
            changeSortPopUp.setHeight(100);

        }
        else{
            changeSortPopUp.setWidth(400);
            changeSortPopUp.setHeight(200);
        }
        changeSortPopUp.setFocusable(true);


        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = 0;
        int OFFSET_Y = settingicon.getHeight();

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

    public void helpBar() {

        final Dialog dialog = new Dialog(mContext, R.style.AppTheme);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        final Window window = dialog.getWindow();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        //dialog.setContentView(R.layout.editprofiledialog);
        if(isTablet(mContext)) {
            dialog.setContentView(R.layout.helpdailog);
        }
        else{
            dialog.setContentView(R.layout.helpdailog);
        }

        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setWindowAnimations(R.style.DialogAnimation);

        final LinearLayout prof_header_lay=(LinearLayout)window.findViewById(R.id.prof_header_lay);

        prof_header_lay.setBackgroundColor(Color.parseColor(head_color));


        final ImageView cancel = (ImageView) window.findViewById(R.id.cancel);
        final WebView help_text=(WebView)window.findViewById(R.id.help_text);

        help_text.loadUrl("file:///android_asset/adminhelp.html");


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}