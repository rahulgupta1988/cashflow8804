package cashflow.sixdcash.com.cashflow.Utility;

/**
 * Created by Praveen on 19-Oct-16.
 */

public class TransactionPanelbean {

    Long trascationID;
    String catname;
    String comments_str;
    String pound;
    String howoften;
    String date;

    public String getComments_str() {
        return comments_str;
    }

    public void setComments_str(String comments_str) {
        this.comments_str = comments_str;
    }

    public String getInoutORRepeate() {
        return inoutORRepeate;
    }

    public void setInoutORRepeate(String inoutORRepeate) {
        this.inoutORRepeate = inoutORRepeate;
    }

    String inoutORRepeate;

    public Long getTrascationID() {
        return trascationID;
    }

    public void setTrascationID(Long trascationID) {
        this.trascationID = trascationID;
    }

    String sourcename;

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getSourcename() {
        return sourcename;
    }

    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }

    public String getPound() {
        return pound;
    }

    public void setPound(String pound) {
        this.pound = pound;
    }

    public String getHowoften() {
        return howoften;
    }

    public void setHowoften(String howoften) {
        this.howoften = howoften;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
