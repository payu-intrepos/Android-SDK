package com.payu.india.Payu;

import com.payu.india.Model.PostData;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by franklin on 6/18/15.
 * Payu util class provides protected, public functions to sdk and App.
 * Use the {@link PayuUtils#SBI_MAES_BIN } set to find whether the number is state bank maestro number or not
 * Use the {@link PayuUtils#validateCardNumber(String)} method to validate user card number, it includes length luhn validation.
 * Use the {@link PayuUtils#luhn(String)} method to validate card number, use this function only if you validate length at your end.
 * Use the {@link PayuUtils#getIssuer(String)} method to get the issuer, it will return the issuer from {VISA, AMEX, MAES, MAST, SMAE, LASER, DINR, JCB}
 * use the {@link PayuUtils#validateCvv(String, String)} method to validate cvv, AMEX has 4 digit, SMAE has no cvv, other has 3 dight cvv.
 * use the {@link PayuUtils#validateExpiry(int, int)} method to validate whether the card is expired or not!.
 * use the {@link PayuUtils#concatParams(String, String)} method to concat both params1 and param2 using equal to (=) and add a ampersand at the end!
 * adding default arguments{@link PayuUtils#getReturnData(String)} {@link PayuUtils#getReturnData(int, String)}
 * use the {@link PayuUtils#getReturnData(int, String, String)} to send the data back to app as {@link PostData}
 * use the {@link PayuUtils#trimAmpersand(String)} remove leading ampersand if any!.
 */
public class PayuUtils {

    /**
     * Known Sate bank maestro bins
     * helps us to validate cvv expmon, expyr,
     * SMAE cards do not have CVV, Expiry month, Expiry year.
     */
    public static Set<String> SBI_MAES_BIN;

    static {
        SBI_MAES_BIN = new HashSet<String>();
        SBI_MAES_BIN.add("504435");
        SBI_MAES_BIN.add("504645");
        SBI_MAES_BIN.add("504775");
        SBI_MAES_BIN.add("504809");
        SBI_MAES_BIN.add("504993");
        SBI_MAES_BIN.add("600206");
        SBI_MAES_BIN.add("603845");
        SBI_MAES_BIN.add("622018");
        SBI_MAES_BIN.add("504774");
    }

    /**
     * includes length validation also
     * use {@link PayuUtils#luhn(String)} to validate.
     *
     * @param cardNumber any card number
     * @return true if valid else false
     */
    public Boolean validateCardNumber(String cardNumber) {
        if (cardNumber.length() < 12) {
            return false;
        } else if (getIssuer(cardNumber).contentEquals(PayuConstants.VISA) && cardNumber.length() == 16) {
            return luhn(cardNumber);
        } else if (getIssuer(cardNumber).contentEquals(PayuConstants.MAST) && cardNumber.length() == 16) {
            return luhn(cardNumber);
        } else if ((getIssuer(cardNumber).contentEquals(PayuConstants.MAES) || getIssuer(cardNumber).contentEquals(PayuConstants.SMAE)) && cardNumber.length() >= 12 && cardNumber.length() <= 19) {
            return luhn(cardNumber);
        } else if (getIssuer(cardNumber).contentEquals(PayuConstants.DINR) && cardNumber.length() == 14) {
            return luhn(cardNumber);
        } else if (getIssuer(cardNumber).contentEquals(PayuConstants.AMEX) && cardNumber.length() == 15) {
            return luhn(cardNumber);
        } else if (getIssuer(cardNumber).contentEquals(PayuConstants.JCB) && cardNumber.length() == 16) {
            return luhn(cardNumber);
        }
        return false;
    }

    /**
     * Universal luhn validation for Credit, Debit cards.
     *
     * @param cardNumber any card number
     * @return true if valid else false
     */
    public Boolean luhn(String cardNumber) {
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

    /**
     * Gives the Issuer of the card number.
     * using simple regex we can actually figure out the card belong to which issuer amoung VISA, AMEX, MAES, MAST, SMAE, LASER, DINR, JCB
     *
     * @param mCardNumber first 6 digit of card number is more than enough to figure out. (Card Bin)
     * @return Issuer of the card
     */
    public String getIssuer(String mCardNumber) {
        if (mCardNumber.startsWith("4")) {
            return PayuConstants.VISA;
        } else if (mCardNumber.matches("^((6304)|(6706)|(6771)|(6709))[\\d]+")) {
            return PayuConstants.LASER;
        } else if (mCardNumber.matches("6(?:011|5[0-9]{2})[0-9]{12}[\\d]+")) {
            return PayuConstants.LASER;
        } else if (mCardNumber.matches("(5[06-8]|6\\d)\\d{14}(\\d{2,3})?[\\d]+") || mCardNumber.matches("(5[06-8]|6\\d)[\\d]+") || mCardNumber.matches("((504([435|645|774|775|809|993]))|(60([0206]|[3845]))|(622[018])\\d)[\\d]+")) {
            if (mCardNumber.length() > 6) { // wel we have 6 digit bin
                if (SBI_MAES_BIN.contains(mCardNumber.substring(0, 6))) {
                    return PayuConstants.SMAE;
                }
            }
            return PayuConstants.MAES;
        } else if (mCardNumber.matches("^5[1-5][\\d]+")) {
            return PayuConstants.MAST;
        } else if (mCardNumber.matches("^3[47][\\d]+")) {
            return PayuConstants.AMEX;
        } else if (mCardNumber.startsWith("36") || mCardNumber.matches("^30[0-5][\\d]+") || mCardNumber.matches("2(014|149)[\\d]+")) {
            return PayuConstants.DINR;
        } else if (mCardNumber.matches("^35(2[89]|[3-8][0-9])[\\d]+")) {
            return PayuConstants.JCB;
        }
        return "";
    }

    /**
     * helps to validate cvv
     * we need card bin to figure out issuer,
     * from issuer we validate cvv
     * Oh! AMEX has 4 digit,
     * Crap! SMAE has no cvv,
     * okay, other has 3 dight cvv.
     *
     * @param cardNumber Card bin
     * @param cvv        cvv
     * @return true if valid cvv else false
     */
    public boolean validateCvv(String cardNumber, String cvv) {
        String issuer = getIssuer(cardNumber);
        if (issuer.contentEquals(PayuConstants.SMAE)) {
            return true;
        } else if (issuer.contentEquals(PayuConstants.AMEX) & cvv.length() == 4) {
            return true;
        } else if (!issuer.contentEquals(PayuConstants.AMEX) && cvv.length() == 3) {
            return true;
        }
        return false;
    }

    /**
     * helps to validate whether the card is expired or not!.
     *
     * @param expiryMonth expmon
     * @param expiryYear  expyr
     * @return true if valid else false
     */
    public boolean validateExpiry(int expiryMonth, int expiryYear) {
        Calendar calendar = Calendar.getInstance();
        if (expiryMonth < 1 || expiryMonth > 12 || String.valueOf(expiryYear).length() != 4) { // expiry month validation
            return false;
        } else if (calendar.get(Calendar.YEAR) > expiryYear || (calendar.get(Calendar.YEAR) == expiryYear && calendar.get(Calendar.MONTH) + 1 > expiryMonth)) { // expiry date validation.
            return false;
        }
        return true;
    }

    /**
     * just to make our life easier, lets define a function
     * concat two strings with equal sign and add an ampersand at the end
     *
     * @param key   example txnid
     * @param value example payu12345
     * @return concatenated String
     */

    protected String concatParams(String key, String value) {
        return key + "=" + value + "&";
    }

    /**
     * Defalut param of {@link PayuUtils#getReturnData(int, String, String)}
     * Use this if your first param is {@link PayuErrors#MISSING_PARAMETER_EXCEPTION} and second param is {@link PayuConstants#ERROR}
     *
     * @param result result it the result of your PostData
     * @return PostData object.
     */
    protected PostData getReturnData(String result) {
        return getReturnData(PayuErrors.MISSING_PARAMETER_EXCEPTION, PayuConstants.ERROR, result);
    }

    /**
     * Defalut param of {@link PayuUtils#getReturnData(int, String, String)}
     * Use this param if there is something wrong while validating params {@link PayuConstants#ERROR}
     *
     * @param code   error code to be returned with PostData
     * @param result error message (result) to be returned with PostData
     * @return PostData object.
     */
    protected PostData getReturnData(int code, String result) {
        return getReturnData(code, PayuConstants.ERROR, result);
    }

    /**
     * Use this is method to return the PostData to the user {@link PostData#code} {@link PostData#status} {@link PostData#result}
     *
     * @param code    error code to be returned with PostData
     * @param status, status message it can either be {@link PayuConstants#SUCCESS} or {@link PayuConstants#ERROR}
     * @param result  error message (result) to be returned with PostData
     * @return PostData object.
     */
    protected PostData getReturnData(int code, String status, String result) {
        PostData postData = new PostData();
        postData.setCode(code);
        postData.setStatus(status);
        postData.setResult(result);
        return postData;
    }

    /**
     * This method is used to trim the leading Ampersand sign
     *
     * @param data , should be concatenated data with equal sign and ampersand.
     * @return example txnid=payu12345
     */
    protected String trimAmpersand(String data) {
        return data.charAt(data.length() - 1) == '&' ? data.substring(0, data.length() - 1) : data;
    }
}
