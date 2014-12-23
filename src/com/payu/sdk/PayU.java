package com.payu.sdk;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.payu.sdk.exceptions.HashException;
import com.payu.sdk.exceptions.MissingParameterException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by amit on 23/6/14.
 */


public class PayU {

    public static final int RESULT = 100;

    private static String mMerchantKey;

    private static final String TAG = "PayU";
    private static PayU INSTANCE;
    private final Activity mActivity;
    private final String mSalt;


    public static final String AMOUNT = "amount";
    public static final String TXNID = "txnid";
    public static final String PRODUCT_INFO = "productinfo";
    public static final String SURL = "surl";
    public static final String FURL = "furl";
    public static final String MERCHANT_KEY = "key";
    public static final String EMAIL = "email";
    public static final String FIRSTNAME = "firstname";
    public static final String BANKCODE = "bankcode";
    public static final String MODE = "mode";
    public static final String CARD_NUMBER = "ccnum";
    public static final String EXPIRY_MONTH = "ccexpmon";
    public static final String EXPIRY_YEAR = "ccexpyr";
    public static final String CARDHOLDER_NAME = "ccname";
    public static final String CVV = "ccvv";
    public static final String USER_CREDENTIALS = "user_credentials";
    public static final String OFFER_KEY = "offer_key";
    public static final String DROP_CATEGORY = "drop_category";
    public static final String ENFORCE_PAYMETHOD = "enforce_paymethod";
    public static final String PAYMENT_OPTIONS = "payment_options";
    public static final String STORE_CARD = "store_card";
    public static final String UDF = "device_type";
    public static final String SDK = "1";
//    public static final String DISABLE_PAYMENT_PROCESS_BACK_BUTTON = "disable_web_view_back";
    public static final String DISABLE_CUSTOM_BROWSER = "showCustom";

    public static final String VAR1 = "var1";
    public static final String VAR2 = "var2";
    public static final String VAR3 = "var3";
    public static final String VAR4 = "var4";
    public static final String VAR5 = "var5";
    public static final String VAR6 = "var6";
    public static final String VAR7 = "var7";
    public static final String VAR8 = "var8";
    public static final String VAR9 = "var9";

    public static JSONArray availableBanks;
    public static JSONArray availableEmi;
    public static JSONArray availableCashCards;
    public static JSONArray availableModes;
    public static JSONArray availableDebitCards;
    public static JSONArray availableCreditCards;
    public static JSONArray offerStatus;

    public static String dropCategory;
    public static String enforcePayMethod;

    public static PaymentMode[] paymentOptions = null;


    public static long dataFetchedAt;


    public static enum PaymentMode {
        CC, DC, NB, EMI, PAYU_MONEY, STORED_CARDS, CASH
    }

    static final Map<PaymentMode, String> PAYMENT_MODE_TITLES;

    static {
        PAYMENT_MODE_TITLES = new HashMap<PaymentMode, String>();
        PAYMENT_MODE_TITLES.put(PaymentMode.CC, "Credit Card");
        PAYMENT_MODE_TITLES.put(PaymentMode.DC, "Debit Card");
        PAYMENT_MODE_TITLES.put(PaymentMode.NB, "Net Banking");
        PAYMENT_MODE_TITLES.put(PaymentMode.EMI, "EMI");
        PAYMENT_MODE_TITLES.put(PaymentMode.PAYU_MONEY, "PayUMoney");
        PAYMENT_MODE_TITLES.put(PaymentMode.STORED_CARDS, "Stored Cards");
        PAYMENT_MODE_TITLES.put(PaymentMode.CASH, "Cash Card");
    }

    private static final String[] REQUIRED_CARD_PARAMS = new String[]{CARD_NUMBER, EXPIRY_MONTH, EXPIRY_YEAR, CARDHOLDER_NAME};

    private static final String[] REQUIRED_CC_PARAMS = new String[]{CVV};

    private static final String[] REQUIRED_NB_PARAMS = new String[]{BANKCODE};

    private PayU(Activity activity, String merchantId, String salt) {
        mMerchantKey = merchantId;
        mSalt = salt;
        mActivity = activity;
    }

    public static synchronized PayU getInstance(Activity activity) {
        if (INSTANCE == null) {
            try {
                Bundle bundle = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA).metaData;
                INSTANCE = new PayU(activity, bundle.getString("payu_merchant_id"), bundle.getString("payu_merchant_salt"));
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
            } catch (NullPointerException e) {
                Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
            }
        }
        return INSTANCE;
    }

    public void startPaymentProcess(double amount, HashMap<String, String> userParams) {
        startPaymentProcess(amount, userParams, null);
    }

    public void startPaymentProcess(double amount, HashMap<String, String> userParams, PaymentMode[] modes) {
        Intent intent = new Intent(mActivity, PaymentOptionsActivity.class);
        intent.putExtra(AMOUNT, amount);
        for(String key : userParams.keySet()) {
            intent.putExtra(key, userParams.get(key));
        }
        intent.putExtra(MERCHANT_KEY, mMerchantKey);

        if (userParams.containsKey(DROP_CATEGORY)) {
            dropCategory = userParams.get(DROP_CATEGORY).replaceAll("\\s+", "");
        }
        if (userParams.containsKey(ENFORCE_PAYMETHOD)) {
            enforcePayMethod = userParams.get(ENFORCE_PAYMETHOD);
        }
        if (modes != null) {
            int[] m = new int[modes.length];
            int i = 0;
            for (PaymentMode mo : modes) {
                m[i++] = mo.ordinal();
            }

            intent.putExtra(PAYMENT_OPTIONS, m);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mActivity.startActivityForResult(intent, RESULT);
    }

    public String createPayment(Payment payment, Params params) throws MissingParameterException, HashException {
        params.put("pg", payment.getMode().toString());
        params.put(UDF, SDK);
        switch (payment.getMode()) {
            case CC:
                params.put(BANKCODE, "CC");
                validateParams(REQUIRED_CC_PARAMS, params);
                if (params.get("store_card_token").length() > 1)
                    break;
                validateParams(REQUIRED_CARD_PARAMS, params);

                break;
            case DC:
                if (params.get("store_card_token").length() > 1)
                    break;
                else if (params.get(CVV).length() < 3) {
                    params.put(BANKCODE, "SMAE");
                    params.remove(CVV);
                    params.remove(EXPIRY_MONTH);
                    params.remove(EXPIRY_YEAR);
                    break;
                } else
                    params.put(BANKCODE, "VISA");
                validateParams(REQUIRED_CC_PARAMS, params);
                validateParams(REQUIRED_CARD_PARAMS, params);
                break;
            case NB:
                validateParams(REQUIRED_NB_PARAMS, params);
                break;
            case PAYU_MONEY:
                params.put(BANKCODE, "payuw");
                params.put("pg", "wallet");
                break;
            case EMI:
                validateParams(REQUIRED_CARD_PARAMS, params);
                validateParams(REQUIRED_CC_PARAMS, params);
                break;
        }
//        validateParams(REQUIRED_PAYMENT_PARAMS, params);
        // create the payment hash

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
//            String postData = mMerchantKey + "|" + params.get(TXNID) + "|" + params.get(AMOUNT) + "|" + params.get(PRODUCT_INFO) + "|" + params.get(FIRSTNAME) + "|" + params.get(EMAIL) + "|";
            String postData = mMerchantKey + "|" + params.get("txnid") + "|" + params.get("amount") + "|" + params.get("productinfo") + "|" + params.get("firstname") + "|" + params.get("email") + "|";

            for (int i = 1; i <= 10; i++) {
                postData += params.get("udf" + String.valueOf(i)) + "|";
            }
            postData += mSalt;
            messageDigest.update(postData.getBytes());
            byte[] mdbytes = messageDigest.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte hashByte : mdbytes) {
//                hexString.append(Integer.toHexString(0xFF & hashByte));
                hexString.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
//            postData = MERCHANT_KEY + "=" + mMerchantKey + "&";
            postData = "";
            for (String key : params.keySet()) {
                postData += key + "=" + URLEncoder.encode(params.get(key), "UTF-8") + "&";
            }
            postData += Constants.HASH + "=" + hexString.toString();
            return postData;
        } catch (NoSuchAlgorithmException e) {
            throw new HashException();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
        // okay we are now ready to fire this query
    }

    private void validateParams(String[] required_params, Params params) throws MissingParameterException {
        for (String param : required_params) {
            if (!params.containsKey(param)) {
                throw new MissingParameterException("Parameter " + param + " is missing");
            }
        }
    }

    //    public List<NameValuePair> getParams(String command, String var) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    public List<NameValuePair> getParams(String mCommand, HashMap<String, String> paramsList) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");

        String postData = mMerchantKey + "|" + mCommand + "|" + paramsList.get("var1") + "|" + mSalt;

        messageDigest.update(postData.getBytes());
        byte[] mdBytes = messageDigest.digest();
        StringBuilder hexString = new StringBuilder();

        for (byte hashByte : mdBytes) {
            hexString.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
        }

//        postData = "key=" + mMerchantKey + "&command=" + command + "&var1=" + var  +"&hash=" + hexString.toString() ;

        List<NameValuePair> params = new ArrayList<NameValuePair>(4);
        params.add(new BasicNameValuePair("key", mMerchantKey));
        params.add(new BasicNameValuePair("command", mCommand));
        for (int i = 1; i <= paramsList.size(); i++) {
            params.add(new BasicNameValuePair("var" + i, String.valueOf(paramsList.get("var" + i))));
        }
        params.add(new BasicNameValuePair("hash", hexString.toString()));
        //sdk user define fields.
        params.add(new BasicNameValuePair(UDF, SDK));
        return params;
    }

    /*public static void checkOfferStatus(Activity activity, String cardNumber, String offerKey, String category ){

        List<NameValuePair> postParams = null;

        HashMap varList = new HashMap();

        // offer key
        varList.put("var1", offerKey);
        // empty
        varList.put("var2", "");
        // category
        varList.put("var3", category);
        // bank code
        varList.put("var4", "");
        // empty
        varList.put("var5", cardNumber);

        try {
            postParams = PayU.getInstance(activity).getParams("check_offer_status", varList);
            StoreCardTask getStoredCards = new StoreCardTask((PaymentListener) activity);
            getStoredCards.execute(postParams);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }*/


}
