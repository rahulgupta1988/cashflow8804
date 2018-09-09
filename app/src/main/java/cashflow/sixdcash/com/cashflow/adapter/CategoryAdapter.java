package cashflow.sixdcash.com.cashflow.adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import cashflow.sixdcash.com.cashflow.R;
import cashflow.sixdcash.com.cashflow.Utility.LineDrawable;
import cashflow.sixdcash.com.cashflow.manager.BaseManager;
import cashflowdb.Category;
import cashflowdb.CategoryDao;
import cashflowdb.DaoSession;

/**
 * Created by Praveen on 8/29/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    Context mContext;
    List<Category> categories;
    List<Category> categoriesList;
    View lastClickView=null;
    CategoriesInteface categoriesInteface;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;

    String lastClickedCat;

    public interface CategoriesInteface {
         void selCat(String selcat,int position);
    }

    public CategoryAdapter(Context context,CategoriesInteface categoriesInteface,boolean isadmin,String lastClickedCat) {
        mContext = context;
        this.lastClickedCat=lastClickedCat;
        this.categoriesInteface=categoriesInteface;
        DaoSession daoSession = BaseManager.getDBSessoin(mContext);
        CategoryDao categoryDao = daoSession.getCategoryDao();
        categories = categoryDao.loadAll();

        if(isadmin){

            for(int i=0;i<categories.size();i++){

                if(categories.get(i).getCatname().equalsIgnoreCase("Start")
                        ||categories.get(i).getCatname().equalsIgnoreCase("My Year")){

                    categories.remove(i);

                }
            }
        }


       /* List<Source> sources=categories.get(0).getSourceList();
        for(Source source:sources){
            Log.i("Source list 5183",""+source.getSourcename());
        }*/
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=null;

        //itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryitem, parent, false);

        if(isTablet(mContext)) {
             itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryitem, parent, false);
        }
        else{
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryitem_phone, parent, false);
        }


        return new CategoryHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final CategoryHolder holder, final int position) {

        final Category category = categories.get(position);

        String catnamt_str = category.getCatname();


        if(catnamt_str.equalsIgnoreCase("My Income"))
            holder.catname.setText("Income");

        else if(catnamt_str.equalsIgnoreCase("The Basics"))
            holder.catname.setText("Basic Expenditure");

        else if(catnamt_str.equalsIgnoreCase("Major Buys"))
            holder.catname.setText("Major Purchases");

        else if(catnamt_str.equalsIgnoreCase("Keeping Mobile"))
            holder.catname.setText("Savings");

        else
            holder.catname.setText(catnamt_str);

        if (catnamt_str.equalsIgnoreCase("Start")) {

            if(lastClickedCat.equals("Start")) {
                if (lastClickView == null) {
                    holder.actvitarraiw.setVisibility(View.VISIBLE);
                    lastClickView = holder.actvitarraiw;
                }
            }

            holder.caticon.setBackgroundResource(R.drawable.money_at_start_3x);
            holder.catiamglay.setBackgroundColor(Color.parseColor("#99800b"));
           // holder.catname.setBackgroundColor(Color.parseColor("#a68b0c"));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.catname.setBackgroundResource(R.drawable.catripple);

            } else {
                Drawable drawable = LineDrawable.getStateListDrawable(R.color.moneycol,Color.parseColor("#ffffff"));
                holder.catname.setBackground(drawable);
            }

        }

        else if (catnamt_str.equalsIgnoreCase("My Income")) {

            if(lastClickedCat.equals("My Income")) {
                if (lastClickView == null) {
                    holder.actvitarraiw.setVisibility(View.VISIBLE);
                    lastClickView = holder.actvitarraiw;
                }
            }

            holder.caticon.setBackgroundResource(R.drawable.income_3x);
            holder.catiamglay.setBackgroundColor(Color.parseColor("#bc2a29"));
            //holder.catname.setBackgroundColor(Color.parseColor("#cc2e2d"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.catname.setBackgroundResource(R.drawable.incomeripple);
            } else {
                Drawable drawable = LineDrawable.getStateListDrawable(R.color.incomescol,Color.parseColor("#ffffff"));
                holder.catname.setBackground(drawable);
            }

        }
        else if (catnamt_str.equalsIgnoreCase("The Basics")) {


            if(lastClickedCat.equals("The Basics")) {
                if (lastClickView == null) {
                    holder.actvitarraiw.setVisibility(View.VISIBLE);
                    lastClickView = holder.actvitarraiw;
                }
            }

            holder.caticon.setBackgroundResource(R.drawable.staying_alive_3x);
            holder.catiamglay.setBackgroundColor(Color.parseColor("#2952d7"));
            //holder.catname.setBackgroundColor(Color.parseColor("#2c58e9"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.catname.setBackgroundResource(R.drawable.basicsripple);
            } else {
                Drawable drawable = LineDrawable.getStateListDrawable(R.color.basicscol,Color.parseColor("#ffffff"));
                holder.catname.setBackground(drawable);
            }
        }
        else if (catnamt_str.equals("Keeping Mobile")) {

            if(lastClickedCat.contains("Keeping Mobile")) {
                if (lastClickView == null) {
                    holder.actvitarraiw.setVisibility(View.VISIBLE);
                    lastClickView = holder.actvitarraiw;
                }
            }

            holder.caticon.setBackgroundResource(R.drawable.on_the_move_and_in_touch_3x);
            holder.catiamglay.setBackgroundColor(Color.parseColor("#d78729"));
            //holder.catname.setBackgroundColor(Color.parseColor("#e9922c"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.catname.setBackgroundResource(R.drawable.movingripple);
            } else {
                Drawable drawable = LineDrawable.getStateListDrawable(R.color.movingcol,Color.parseColor("#ffffff"));
                holder.catname.setBackground(drawable);
            }
        }
        else if (catnamt_str.contains("Clothes and Stuff")) {

            if(lastClickedCat.contains("Clothes and Stuff")) {
                if (lastClickView == null) {
                    holder.actvitarraiw.setVisibility(View.VISIBLE);
                    lastClickView = holder.actvitarraiw;
                }
            }

            holder.caticon.setBackgroundResource(R.drawable.style_and_fashion_3x);
            holder.catiamglay.setBackgroundColor(Color.parseColor("#1e7a23"));
            //holder.catname.setBackgroundColor(Color.parseColor("#218426"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.catname.setBackgroundResource(R.drawable.coltheripple);
            } else {
                Drawable drawable = LineDrawable.getStateListDrawable(R.color.clothescol,Color.parseColor("#ffffff"));
                holder.catname.setBackground(drawable);
            }
        }

        else if (catnamt_str.equals("Entertainment")) {

            if(lastClickedCat.equals("Entertainment")) {
                if (lastClickView == null) {
                    holder.actvitarraiw.setVisibility(View.VISIBLE);
                    lastClickView = holder.actvitarraiw;
                }
            }
            holder.caticon.setBackgroundResource(R.drawable.fun_time_3x);
            holder.catiamglay.setBackgroundColor(Color.parseColor("#721ea2"));
            //holder.catname.setBackgroundColor(Color.parseColor("#7c21b0"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.catname.setBackgroundResource(R.drawable.funripple);
            } else {
                Drawable drawable = LineDrawable.getStateListDrawable(R.color.funcol,Color.parseColor("#ffffff"));
                holder.catname.setBackground(drawable);
            }
        }
        else if (catnamt_str.equals("Major Buys")){

            if(lastClickedCat.equals("Major Buys")) {
                if (lastClickView == null) {
                    holder.actvitarraiw.setVisibility(View.VISIBLE);
                    lastClickView = holder.actvitarraiw;
                }
            }
            holder.caticon.setBackgroundResource(R.drawable.holidays_and_major_buys_3x);
            holder.catiamglay.setBackgroundColor(Color.parseColor("#0b89a4"));
            //holder.catname.setBackgroundColor(Color.parseColor("#0c95b2"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.catname.setBackgroundResource(R.drawable.holidaysripple);
            } else {
                Drawable drawable = LineDrawable.getStateListDrawable(R.color.holidayscol,Color.parseColor("#ffffff"));
                holder.catname.setBackground(drawable);
            }
        }

        else if (catnamt_str.contains("Repayments")){

            if(lastClickedCat.contains("Repayments")) {
                if (lastClickView == null) {
                    holder.actvitarraiw.setVisibility(View.VISIBLE);
                    lastClickView = holder.actvitarraiw;
                }
            }
            holder.caticon.setBackgroundResource(R.drawable.repayments_3x);
            holder.catiamglay.setBackgroundColor(Color.parseColor("#163958"));
            //holder.catname.setBackgroundColor(Color.parseColor("#183e60"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.catname.setBackgroundResource(R.drawable.repaymentsripple);
            } else {
                Drawable drawable = LineDrawable.getStateListDrawable(R.color.repaymentscol,Color.parseColor("#ffffff"));
                holder.catname.setBackground(drawable);
            }
        }

        else if (catnamt_str.equalsIgnoreCase("My Year")){

            if(lastClickedCat.equals("My Year")) {
                if (lastClickView == null) {
                    holder.actvitarraiw.setVisibility(View.VISIBLE);
                    lastClickView = holder.actvitarraiw;
                }
            }

            holder.caticon.setBackgroundResource(R.drawable.summary_3x);
            holder.catiamglay.setBackgroundColor(Color.parseColor("#4c4b43"));
            //holder.catname.setBackgroundColor(Color.parseColor("#525149"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.catname.setBackgroundResource(R.drawable.thisyearripple);
            } else {
                Drawable drawable = LineDrawable.getStateListDrawable(R.color.thisyearcol,Color.parseColor("#ffffff"));
                holder.catname.setBackground(drawable);
            }
        }


        holder.parentlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastClickView != null) {
                    lastClickView.setVisibility(View.GONE);
                }

                holder.actvitarraiw.setVisibility(View.VISIBLE);
                lastClickView = holder.actvitarraiw;
                categoriesInteface.selCat(categories.get(position).getCatname(), position);

            }
        });


        setAnimation(holder.parentlay, position);


     /*
        holder.binding.setCategory(category);
        holder.binding.setClick(new ClickInterface() {
            @Override
            public void onNewClick(View view) {

                if (tempview != null && tempbinding != null) {
                    tempview.setSelected(false);
                    holder.binding.setIsVisble(true);
                    tempbinding.setIsVisble(false);
                } else {
                    holder.binding.setIsVisble(true);
                }

                tempview = view;
                tempbinding = holder.binding;
                Toast.makeText(mContext, category.getCatname(), Toast.LENGTH_LONG).show();
                view.setBackgroundResource(R.drawable.categorystate);
                view.setSelected(true);

            }
        });*/
    }

    @Override
    public void onViewDetachedFromWindow(CategoryHolder holder) {
        super.onViewDetachedFromWindow(holder);

        clearAnimation(holder);
    }

    @Override
    public int getItemCount() {
        if(categories!=null && categories.size()>0)
        return categories.size();
        else return 0;
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {

        ImageView caticon,actvitarraiw;
        TextView catname;
        LinearLayout catiamglay,parentlay;

        public CategoryHolder(View itemView) {
            super(itemView);
            actvitarraiw = (ImageView) itemView.findViewById(R.id.actvitarraiw);
            caticon = (ImageView) itemView.findViewById(R.id.caticon);
            catname = (TextView) itemView.findViewById(R.id.catname);
            catiamglay=(LinearLayout)itemView.findViewById(R.id.catiamglay);
            parentlay=(LinearLayout)itemView.findViewById(R.id.parentlay);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void clearAnimation(CategoryHolder hold)
    {
        hold.parentlay.clearAnimation();
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

}
