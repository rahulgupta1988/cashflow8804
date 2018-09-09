package cashflow.sixdcash.com.cashflow;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import cashflow.sixdcash.com.cashflow.Utility.CircularImageView;
import cashflow.sixdcash.com.cashflow.Utility.ImageSelector;
import cashflow.sixdcash.com.cashflow.Utility.ValidationUtility;
import cashflow.sixdcash.com.cashflow.adapter.CategoryAdapter;
import cashflow.sixdcash.com.cashflow.fragments.TrasactionPanelFragment;
import cashflow.sixdcash.com.cashflow.manager.BaseManager;
import cashflow.sixdcash.com.cashflow.manager.DBManager;
import cashflow.sixdcash.com.cashflow.manager.DayInOutTransactionManager;
import cashflowdb.Category;
import cashflowdb.DaoSession;
import cashflowdb.FinancialYearDao;
import cashflowdb.InOutTransactionDao;
import cashflowdb.InitialBudgetDao;
import cashflowdb.RepeatTransactionDao;
import cashflowdb.Source;
import cashflowdb.UserInfo;

/**
 * Created by Praveen on 19-Oct-16.
 */

public class TransactionPanel extends Activity {

    Context mContext;
    RecyclerView catlist;
    FragmentManager frgManager;
    private String lastClickedCat = "My Income";

    LinearLayout headerlay;
    TextView headertitle;

    ImageView settingicon,homeicon;

    Spinner sourcespin;

    Category categories;
    List<Source> temp_sources;
    List<Source> sources = new ArrayList<Source>();
    List<String> sounceName = new ArrayList<String>();
    private TextView withusername;
    ImageView addmoreitems;

    ImageView usersetting,reseticon,submitMenuButton,helpicon;


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

        //setContentView(R.layout.transactionpanel_activity);

        mContext = this;
       // setContentView(R.layout.transactionpanel_activity);
       if(isTablet(mContext)) {
            setContentView(R.layout.transactionpanel_activity);
        }
        else{
            setContentView(R.layout.transactionpanel_activity_phone);
        }



        init();


    }

    public void init(){
        submitMenuButton=(ImageView)findViewById(R.id.submitMenuButton);
        usersetting = (ImageView) findViewById(R.id.usersetting);
        reseticon = (ImageView) findViewById(R.id.reseticon);
        usersetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfileDialog();
            }
        });
        reseticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetConfirmation("Are you sure, You want Reset All Data?");
            }
        });

        helpicon=(ImageView)findViewById(R.id.helpicon);
        helpicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpBar();
            }
        });

        addmoreitems=(ImageView) findViewById(R.id.addmoreitems);
        sourcespin=(Spinner)findViewById(R.id.sourcespin);
        headerlay=(LinearLayout)findViewById(R.id.headerlay);
        headertitle=(TextView)findViewById(R.id.headertitle);

        addmoreitems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adminhome=new Intent(mContext, HostActivty.class);
                adminhome.putExtra("catName",lastClickedCat);
                startActivity(adminhome);
                finish();
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });

       /* submitMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                home.updateTrasactions();
            }
        });*/

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

        headertitle.setText(""+cat_name+" - Hints and Tips");

        settingicon=(ImageView)findViewById(R.id.settingicon);
        withusername = (TextView) findViewById(R.id.withusername);
        setUserPic();
        setUserName();
        settingicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //settingBar();
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
        setCatAdapter();
        setSources(lastClickedCat);
        setHeaderColor(lastClickedCat);




    }

    private void setUserPic() {
        String userprofpic = "";
        UserInfo userInfo = DBManager.getUserInfo(mContext);
        if (userInfo != null) {
            userprofpic = userInfo.getPicname();
            if (userprofpic != null && !userprofpic.equals("")) {
                Bitmap bitmap = getBitmap(userprofpic);


                if (bitmap != null) {
                    usersetting.setBackground(null);
                    usersetting.setImageBitmap(bitmap);
                }
            }
        }
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

    public Bitmap getBitmap(String filename) {

        Log.i("bit 0124", "" + filename);
        String root = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File myDir = new File(root + "/expressionApp/" + filename);
        if (myDir.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(myDir.getAbsolutePath());
            return myBitmap;
        }

        return null;
    }

    public void setSources(String nowCatName) {

       if(sources!=null)
           sources.clear();

        categories = DBManager.getCategoryByName(mContext, nowCatName);
        temp_sources = categories.getSourceList();
        for (int i = 0; i < temp_sources.size(); i++) {
            if (temp_sources.get(i).getIsChecked()) {
                sources.add(temp_sources.get(i));
            }
        }
        initSourceSelector();

    }

    ArrayAdapter<String> sourceapter;
    public void initSourceSelector(){
        sourceapter=null;
        if(sounceName!=null)
            sounceName.clear();

        if (sources != null) {
            sounceName.add("All");
            for (Source sounce : sources) {
                sounceName.add(sounce.getSourcename());
            }
            sourceapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, sounceName);
            sourceapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sourcespin.setAdapter(sourceapter);

            sourcespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    home.selectedSource(sourceapter.getItem(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    String cat_name= "";
    CategoryAdapter.CategoriesInteface categoriesInteface = new CategoryAdapter.CategoriesInteface() {

        @Override
        public void selCat(String selcat, int position) {


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


             headertitle.setText(cat_name+" - Hints and Tips");
             setHeaderColor(selcat);
             setSources(selcat);
             setContainer(selcat);
        }
    };

    String head_color="#99800b";

    public void setHeaderColor(String catName){

        if (catName.equalsIgnoreCase("Start")) {
            headerlay.setBackgroundColor(Color.parseColor("#99800b"));
        }
        else if (catName.equalsIgnoreCase("My Income")) {
            head_color="#bc2a29";
            headerlay.setBackgroundColor(Color.parseColor("#bc2a29"));
        }
        else if (catName.equalsIgnoreCase("The Basics")) {
            head_color="#2952d7";
            headerlay.setBackgroundColor(Color.parseColor("#2952d7"));
        }
        else if (catName.contains("Keeping Mobile")) {
            head_color="#d78729";
            headerlay.setBackgroundColor(Color.parseColor("#d78729"));
        }
        else if (catName.contains("Clothes and Stuff")) {
            head_color="#1e7a23";
            headerlay.setBackgroundColor(Color.parseColor("#1e7a23"));
        }
        else if (catName.contains("Entertainment")) {
            head_color="#721ea2";
            headerlay.setBackgroundColor(Color.parseColor("#721ea2"));
        }
        else if (catName.contains("Major Buys")){
            head_color="#0b89a4";
            headerlay.setBackgroundColor(Color.parseColor("#0b89a4"));
        }
        else if (catName.contains("Repayments")){
            head_color="#163958";
            headerlay.setBackgroundColor(Color.parseColor("#163958"));
        }
        else if (catName.equalsIgnoreCase("My Year")){
            head_color="#4c4b43";
            headerlay.setBackgroundColor(Color.parseColor("#4c4b43"));
        }


    }

    private void setCatAdapter() {
        catlist = (RecyclerView) findViewById(R.id.catlist);
        CategoryAdapter adapter = new CategoryAdapter(mContext, categoriesInteface,true,lastClickedCat);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        catlist.setLayoutManager(layoutManager);
        catlist.setItemAnimator(new DefaultItemAnimator());
        catlist.setAdapter(adapter);
        fillContanierInitially();
    }

    TrasactionPanelFragment home;
    private void fillContanierInitially() {
        home = new TrasactionPanelFragment();
        frgManager = getFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("catname", lastClickedCat);
        home.setArguments(bundle);

        FragmentTransaction ft = frgManager.beginTransaction();
        ft.add(R.id.container, home, "home");
        ft.commit();
    }

    private void setContainer(String catnamt_str) {

        if (!lastClickedCat.equalsIgnoreCase(catnamt_str)) {
            lastClickedCat = catnamt_str;
            home.setViewAsCategory(catnamt_str);
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
        int OFFSET_Y = settingicon.getHeight()+5;

        // Clear the default translucent background
        changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        changeSortPopUp.showAtLocation(layout, Gravity.NO_GRAVITY, rc.left + OFFSET_X, rc.top + OFFSET_Y);
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

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    ImageSelector imageSelector = null;
    String userPic = "";
    String userprofpic = "";


    public void editProfileDialog() {

        usersetting.setClickable(false);

        final Dialog dialog = new Dialog(mContext, R.style.AppTheme);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        final Window window = dialog.getWindow();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        //dialog.setContentView(R.layout.editprofiledialog);
        if(isTablet(mContext)) {
            dialog.setContentView(R.layout.editprofiledialog);
        }
        else{
            dialog.setContentView(R.layout.editprofiledialog_phone);
        }

        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setWindowAnimations(R.style.DialogAnimation);

        final LinearLayout prof_header_lay=(LinearLayout)window.findViewById(R.id.prof_header_lay);

        prof_header_lay.setBackgroundColor(Color.parseColor(head_color));

        final EditText firstnametxt = (EditText) window.findViewById(R.id.firstnametxt);
        final EditText lastnametxt = (EditText) window.findViewById(R.id.lastnametxt);
        final CircularImageView imageView_round = (CircularImageView) window.findViewById(R.id.imageView_round);
        final ImageView cancel = (ImageView) window.findViewById(R.id.cancel);
        final TextView submit = (TextView) window.findViewById(R.id.submit);


        UserInfo userInfo = DBManager.getUserInfo(mContext);
        if (userInfo != null) {
            firstnametxt.setText(userInfo.getFirstname().toString());
            lastnametxt.setText(userInfo.getLastname().toString());
            userprofpic = userInfo.getPicname();
            if (userprofpic != null && !userprofpic.equals("")) {
                Bitmap bitmap = getBitmap(userprofpic);

                if (bitmap != null) {
                    imageView_round.setBackground(null);
                    imageView_round.setImageBitmap(bitmap);
                }
            }
        }


        imageView_round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelector = new ImageSelector(mContext, imageView_round, new ImageSelector.ImageListener() {
                    @Override
                    public void getImageName(String imagename) {
                        userPic = imagename;
                    }
                });

                imageSelector.selectImage();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                usersetting.setClickable(true);
                dialog.dismiss();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String firstname = firstnametxt.getText().toString().trim();
                String lastname = lastnametxt.getText().toString().trim();


                if (ValidationUtility.validEditTextString(firstname)) {

                    if (ValidationUtility.validEditTextString(lastname)) {

                        usersetting.setClickable(true);

                        String str = firstname;
                        String cap_firstname = str.substring(0, 1).toUpperCase() + str.substring(1);

                        if (userPic != null && !userPic.equals("")) {
                            DBManager.UpdateUserInfo(mContext, cap_firstname, lastname, userPic);
                            Bitmap bitmap = getBitmap(userPic);
                            if (bitmap != null) {
                                usersetting.setBackground(null);
                                usersetting.setImageBitmap(bitmap);
                            }

                        } else {
                            DBManager.UpdateUserInfo(mContext, cap_firstname, lastname, "");
                        }

                        setUserName();
                        Toast.makeText(mContext, "Profile Updated.", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    } else {
                        lastnametxt.setError("Enter Last Name");
                        //Toast.makeText(mContext,"Enter Last Name.",Toast.LENGTH_LONG).show();
                    }

                } else {
                    firstnametxt.setError("Enter First Name");
                    //Toast.makeText(mContext,"Enter First Name.",Toast.LENGTH_LONG).show();
                }
            }
        });


        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageSelector.onActivityResult(requestCode, resultCode, data);
    }

    public void resetConfirmation(final String msg) {
        final Dialog dialog = new Dialog(mContext);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmdialog);
        window.setType(WindowManager.LayoutParams.FIRST_SUB_WINDOW);
        if(isTablet(mContext)) {
            window.setLayout(450, ActionBar.LayoutParams.WRAP_CONTENT);
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
                dialog.dismiss();
            }
        });
        tvYes.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                resetData();
                dialog.dismiss();
                Toast.makeText(mContext, "Reset Done.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void resetData() {
        DaoSession daoSession = BaseManager.getDBSessoin(mContext);

        RepeatTransactionDao repeatTransactionDao = daoSession.getRepeatTransactionDao();
        repeatTransactionDao.deleteAll();

        InOutTransactionDao transactionDao = daoSession.getInOutTransactionDao();
        transactionDao.deleteAll();

        FinancialYearDao financialYearDao = daoSession.getFinancialYearDao();
        financialYearDao.deleteAll();

        InitialBudgetDao initialBudgetDao = daoSession.getInitialBudgetDao();
        initialBudgetDao.deleteAll();

//        DBManager.unCheckAllSource(mContext);
        //removeFragment();
        lastClickedCat = "Start";
        setCatAdapter();
        setHeaderColor("Start");
        //setContainer("Start");

        new DayInOutTransactionManager(mContext).insertDayInOut();
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
        help_text.loadUrl("file:///android_asset/transactionhelp.html");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                usersetting.setClickable(true);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
