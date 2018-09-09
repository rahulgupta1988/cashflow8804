package cashflow.sixdcash.com.cashflow.Utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Praveen on 9/20/2016.
 */
public class ValidationUtility {


    // EMAIL_PATTERN
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public ValidationUtility(){}

    public static boolean validEmailAddress(String emailid){
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(emailid);
        return matcher.matches();
    }

    public static boolean validEditTextString(String txt){
        String adjusted = txt.replaceAll("(?m)^[ \t]*\r?\n", "");
        boolean haveonlybanklines=adjusted.length()>0;
        boolean empty=txt != null && txt.length() > 0;

        if(haveonlybanklines && empty) return true;
        else return false;
    }



}
