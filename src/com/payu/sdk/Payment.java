package com.payu.sdk;

import com.payu.sdk.exceptions.MissingParameterException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by amit on 25/6/14.
 */
public class Payment extends HashMap<String, String> {
/*    protected PayU.PaymentMode mMode;
    protected double mAmount;
    protected String mTxnId;
    protected String mProductInfo;
    protected String mSurl;*/

    public String get(String key) {
        String value = super.get(key);
        if (value == null) {
            value = "";
        }
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void put(String key, double value) {
        super.put(key, String.valueOf(value));
    }

    public Payment() {
    }

    public PayU.PaymentMode getMode() {
        return PayU.PaymentMode.valueOf(get(PayU.MODE));
    }

    public double getAmount() {
        return Double.valueOf(get(PayU.AMOUNT));
    }

    public String getTxnId() {
        return get(PayU.TXNID);
    }

    public String getProductInfo() {
        return get(PayU.PRODUCT_INFO);
    }

    public String getSurl() {
        return get(PayU.SURL);
    }

    public String getFurl() {
        return get(PayU.FURL);
    }

    public class Builder {
        private Payment payment;

        public Builder() {
            payment = new Payment();
        }

        public Builder set(String key, String value){
            payment.put(key,value);
            return this;
        }

        public String get(String key){
            return payment.get(key);
        }

        public Payment create() throws MissingParameterException {
            // validate things.
            String[] p = new String[]{PayU.AMOUNT, PayU.TXNID, PayU.PRODUCT_INFO, PayU.SURL, PayU.MODE};
            for (String param : p) {
                if (!payment.containsKey(param) || payment.get(param) == null || payment.get(param).equals("")) {
                    throw new MissingParameterException("Missing required parameter" + param);
                }
            }

//            if(payment.get(PayU.MODE) == null) {
//                throw new MissingParameterException("Invalid payment mode");
//            }

            if (Double.valueOf(payment.get(PayU.AMOUNT)) <= 0) {
                throw new MissingParameterException("Amount cannot be less than zero");
            }

            return payment;
        }
    }
}
