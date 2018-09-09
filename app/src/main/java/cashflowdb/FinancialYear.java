package cashflowdb;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "FINANCIAL_YEAR".
 */
public class FinancialYear {

    private Long id;
    private java.util.Date startdate;
    private java.util.Date enddate;
    private String competed;

    public FinancialYear() {
    }

    public FinancialYear(Long id) {
        this.id = id;
    }

    public FinancialYear(Long id, java.util.Date startdate, java.util.Date enddate, String competed) {
        this.id = id;
        this.startdate = startdate;
        this.enddate = enddate;
        this.competed = competed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.util.Date getStartdate() {
        return startdate;
    }

    public void setStartdate(java.util.Date startdate) {
        this.startdate = startdate;
    }

    public java.util.Date getEnddate() {
        return enddate;
    }

    public void setEnddate(java.util.Date enddate) {
        this.enddate = enddate;
    }

    public String getCompeted() {
        return competed;
    }

    public void setCompeted(String competed) {
        this.competed = competed;
    }

}
