package cashflowdb;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "DAY_IN_OUT_SUMMERY".
 */
public class DayInOutSummery {

    private Long id;
    private Double income_pound;
    private Double expense_pound;
    private Double balance_in_hand;
    private java.util.Date date;
    private Boolean is_worst_case;
    private Boolean is_debt;

    public DayInOutSummery() {
    }

    public DayInOutSummery(Long id) {
        this.id = id;
    }

    public DayInOutSummery(Long id, Double income_pound, Double expense_pound, Double balance_in_hand, java.util.Date date, Boolean is_worst_case, Boolean is_debt) {
        this.id = id;
        this.income_pound = income_pound;
        this.expense_pound = expense_pound;
        this.balance_in_hand = balance_in_hand;
        this.date = date;
        this.is_worst_case = is_worst_case;
        this.is_debt = is_debt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getIncome_pound() {
        return income_pound;
    }

    public void setIncome_pound(Double income_pound) {
        this.income_pound = income_pound;
    }

    public Double getExpense_pound() {
        return expense_pound;
    }

    public void setExpense_pound(Double expense_pound) {
        this.expense_pound = expense_pound;
    }

    public Double getBalance_in_hand() {
        return balance_in_hand;
    }

    public void setBalance_in_hand(Double balance_in_hand) {
        this.balance_in_hand = balance_in_hand;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public Boolean getIs_worst_case() {
        return is_worst_case;
    }

    public void setIs_worst_case(Boolean is_worst_case) {
        this.is_worst_case = is_worst_case;
    }

    public Boolean getIs_debt() {
        return is_debt;
    }

    public void setIs_debt(Boolean is_debt) {
        this.is_debt = is_debt;
    }

}
