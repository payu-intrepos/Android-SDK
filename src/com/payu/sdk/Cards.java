package com.payu.sdk;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by franklin.michael on 30-06-2014.
 */
public class Cards {

    public static HashMap<String, Drawable> ISSUER_DRAWABLE;

    public static void initializeIssuers(Resources resources){ // oops, Cant use Resource out side Activity. so doing this

        ISSUER_DRAWABLE = new HashMap<String, Drawable>();
        ISSUER_DRAWABLE.put(PayU.VISA, resources.getDrawable(R.drawable.visa));
        ISSUER_DRAWABLE.put(PayU.LASER, resources.getDrawable(R.drawable.laser));
        ISSUER_DRAWABLE.put(PayU.DISCOVER, resources.getDrawable(R.drawable.discover));
        ISSUER_DRAWABLE.put(PayU.MAES, resources.getDrawable(R.drawable.maestro));
        ISSUER_DRAWABLE.put(PayU.MAST, resources.getDrawable(R.drawable.master));
        ISSUER_DRAWABLE.put(PayU.AMEX, resources.getDrawable(R.drawable.amex));
        ISSUER_DRAWABLE.put(PayU.DINR, resources.getDrawable(R.drawable.diner));
        ISSUER_DRAWABLE.put(PayU.JCB, resources.getDrawable(R.drawable.jcb));
        ISSUER_DRAWABLE.put(PayU.SMAE, resources.getDrawable(R.drawable.maestro));
    }


    // sbi bins 504435,504645,504645,504775,504809,504993,600206,603845,622018
    // (remaining bins if any) we add from GetResponseTask
    public static Set<String> SBI_MAES_BIN ;
    static {
        SBI_MAES_BIN = new HashSet<String>();
        SBI_MAES_BIN.add("504435");
        SBI_MAES_BIN.add("504645");
        SBI_MAES_BIN.add("504645");
        SBI_MAES_BIN.add("504775");
        SBI_MAES_BIN.add("504809");
        SBI_MAES_BIN.add("504993");
        SBI_MAES_BIN.add("600206");
        SBI_MAES_BIN.add("603845");
        SBI_MAES_BIN.add("622018");
    }

    public static Boolean validateCardNumber (String cardNumber){
        if(cardNumber.length() < 12){
            return false;
        }else if (getIssuer(cardNumber).contentEquals(PayU.VISA) && cardNumber.length() == 16) {
            return luhn(cardNumber);
        } else if (getIssuer(cardNumber).contentEquals(PayU.MAST) && cardNumber.length() == 16) {
            return luhn(cardNumber);
        } else if ((getIssuer(cardNumber).contentEquals(PayU.MAES) || getIssuer(cardNumber).contentEquals(PayU.SMAE))&& cardNumber.length() >= 12 && cardNumber.length() <= 19) {
            return luhn(cardNumber);
        } else if (getIssuer(cardNumber).contentEquals(PayU.DINR) && cardNumber.length() == 14) {
            return luhn(cardNumber);
        } else if (getIssuer(cardNumber).contentEquals(PayU.AMEX) && cardNumber.length() == 15) {
            return luhn(cardNumber);
        } else if (getIssuer(cardNumber).contentEquals(PayU.JCB) && cardNumber.length() == 16) {
            return luhn(cardNumber);
        }
        return false;
    }

    public static Boolean luhn(String cardNumber){
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        if (sum % 10 == 0) {
            return true;
        }
        return false;
    }

    public static DatePickerDialog customDatePicker(Activity activity, DatePickerDialog.OnDateSetListener mDateSetListener, int mYear, int mMonth, int mDay) {
        DatePickerDialog dpd = new DatePickerDialog(activity,mDateSetListener, mYear, mMonth, mDay);
        //android.R.style.Widget_Holo,
//        dpd.getDatePicker().setMinDate(new Date().getTime() - 1000);


        if (Build.VERSION.SDK_INT >= 11) {
            try {
                Method m =   dpd.getDatePicker().getClass().getMethod("setCalendarViewShown", boolean.class);
                m.invoke(  dpd.getDatePicker(), false);
            }
            catch (Exception e) {} // eat exception in our case
        }
        if (Build.VERSION.SDK_INT >= 11) {
            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        }


        try {
            Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (Field datePickerDialogField : datePickerDialogFields) {

                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    Field datePickerFields[] = datePickerDialogField.getType().getDeclaredFields();
                    for (Field datePickerField : datePickerFields) {
                        if ("mDayPicker".equals(datePickerField.getName()) || "mDaySpinner".equals(datePickerField.getName()) || "DAY".equals(datePickerDialogField.getName())) {
                            datePickerField.setAccessible(true);
                            ((View) datePickerField.get(datePicker)).setVisibility(View.GONE);
                        }
                    }
                }
            }
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int daySpinnerId = Resources.getSystem().getIdentifier("mDayPicker", "id", "android");

                if (daySpinnerId != 0) {
                    View daySpinner =   dpd.getDatePicker().findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                    }
                }
            }

            dpd.getDatePicker().setSpinnersShown(true);
            dpd.getDatePicker().setCalendarViewShown(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        dpd.setTitle(null);
        return dpd;
    }


    public static String getIssuer(String mCardNumber){
        if (mCardNumber.startsWith("4")) {
            return PayU.VISA;
        } else if(mCardNumber.matches("^((6304)|(6706)|(6771)|(6709))[\\d]+")) {
            return PayU.LASER;
        } else if(mCardNumber.matches("6(?:011|5[0-9]{2})[0-9]{12}[\\d]+")) {
            return PayU.LASER;
        } else if (mCardNumber.matches("(5[06-8]|6\\d)\\d{14}(\\d{2,3})?[\\d]+") || mCardNumber.matches("(5[06-8]|6\\d)[\\d]+") || mCardNumber.matches("((504([435|645|774|775|809|993]))|(60([0206]|[3845]))|(622[018])\\d)[\\d]+")) {
            if(mCardNumber.length() > 6){ // wel we have 6 digit bin
                if(SBI_MAES_BIN.contains(mCardNumber.substring(0, 6))){
                    return PayU.SMAE;
                }
            }
            return PayU.MAES;
        } else if (mCardNumber.matches("^5[1-5][\\d]+")) {
            return PayU.MAST;
        } else if (mCardNumber.matches("^3[47][\\d]+")) {
            return PayU.AMEX;
        } else if (mCardNumber.startsWith("36") || mCardNumber.matches("^30[0-5][\\d]+") || mCardNumber.matches("2(014|149)[\\d]+")) {
            return PayU.DINR;
        } else if(mCardNumber.matches("^35(2[89]|[3-8][0-9])[\\d]+")) {
            return PayU.JCB;
        }
        return "";
    }

    public static boolean validateCvv(String cardNumber, String cvv){
        String issuer = getIssuer(cardNumber);
        if(issuer.contentEquals(PayU.SMAE)){
            return true;
        }else if(issuer.contentEquals(PayU.AMEX) & cvv.length() == 4){
            return true;
        }else if(!issuer.contentEquals(PayU.AMEX) && cvv.length() == 3 ){
            return true;
        }
        return false;
    }

}
