package cashflow.sixdcash.com.cashflow.Utility.pojo;

import java.util.Date;

/**
 * Created by opj1 on 20/01/2017.
 */
public class DayInOutPOJO {
    String inorex;
    double income;
    double expense;
    boolean hasIncome;
    boolean hasExpense;
    Date date;

    public String getInorex() {
        return inorex;
    }

    public void setInorex(String inorex) {
        this.inorex = inorex;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public boolean isHasIncome() {
        return hasIncome;
    }

    public void setHasIncome(boolean hasIncome) {
        this.hasIncome = hasIncome;
    }

    public boolean isHasExpense() {
        return hasExpense;
    }

    public void setHasExpense(boolean hasExpense) {
        this.hasExpense = hasExpense;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return this.date+" "+this.income+" "+this.expense+" "+ this.inorex;
    }
}
