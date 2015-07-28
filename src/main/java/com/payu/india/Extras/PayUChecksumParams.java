package com.payu.india.Extras;

import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.india.Payu.PayuUtils;

/**
 * Created by franklin on 6/25/15.
 * Simple Bean class for getting PayuChecksum.
 * Use it only if payu server generate hash {@link com.payu.india.Payu.PayuConstants#MOBILE_STAGING_ENV}
 */
public class PayUChecksumParams extends PayuUtils {

    // payment hash
    private String command;
    private String key;
    private String txnid;
    private String amount;
    private String productinfo;
    private String firstname;
    private String email;
    private String udf1;
    private String udf2;
    private String udf3;
    private String udf4;
    private String udf5;
    private String user_credentials;
    // server generate hash so no need of salt
    private String[] paymentHashParams = {PayuConstants.KEY, PayuConstants.TXNID, PayuConstants.AMOUNT, PayuConstants.PRODUCT_INFO, PayuConstants.FIRST_NAME, PayuConstants.EMAIL, PayuConstants.UDF1, PayuConstants.UDF2, PayuConstants.UDF3, PayuConstants.UDF4, PayuConstants.UDF5};

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductinfo() {
        return productinfo;
    }

    public void setProductinfo(String productinfo) {
        this.productinfo = productinfo;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUdf1() {
        return udf1;
    }

    public void setUdf1(String udf1) {
        this.udf1 = udf1;
    }

    public String getUdf2() {
        return udf2;
    }

    public void setUdf2(String udf2) {
        this.udf2 = udf2;
    }

    public String getUdf3() {
        return udf3;
    }

    public void setUdf3(String udf3) {
        this.udf3 = udf3;
    }

    public String getUdf4() {
        return udf4;
    }

    public void setUdf4(String udf4) {
        this.udf4 = udf4;
    }

    public String getUdf5() {
        return udf5;
    }

    public void setUdf5(String udf5) {
        this.udf5 = udf5;
    }

    public String getUser_credentials() {
        return user_credentials;
    }

    public void setUser_credentials(String user_credentials) {
        this.user_credentials = user_credentials;
    }

    // for webservice api

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public PostData getChecksumPostParams() {
        StringBuilder paymentHash = new StringBuilder();
        for (String key : paymentHashParams) { //    sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||SALT)
            switch (key) {
                case PayuConstants.KEY:
                    if (this.key == null || this.key.length() < 1) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_KEY_IS_MISSING);
                    } else {
                        paymentHash.append(concatParams(PayuConstants.KEY, this.key));
                    }
                    break;
                case PayuConstants.TXNID:
                    if (this.txnid.length() < 1) {
                        return getReturnData(PayuErrors.TRANSACTION_ID_MISSING);
                    } else {
                        paymentHash.append(concatParams(PayuConstants.TXNID, this.txnid));
                    }
                    break;
                case PayuConstants.AMOUNT:
                    if (Double.parseDouble(this.amount) < 1) {
                        return getReturnData(PayuErrors.INVALID_AMOUNT);
                    } else {
                        paymentHash.append(concatParams(PayuConstants.AMOUNT, this.amount));
                    }
                    break;
                case PayuConstants.PRODUCT_INFO:
                    if (this.productinfo == null) {
                        return getReturnData(PayuErrors.INVALID_PRODUCT_INFO);
                    } else {
                        paymentHash.append(concatParams(PayuConstants.PRODUCT_INFO, this.productinfo));
                    }
                    break;
                case PayuConstants.FIRST_NAME:
                    if (this.firstname == null) {
                        return getReturnData(PayuErrors.INVALID_FIRST_NAME);
                    } else {
                        paymentHash.append(concatParams(PayuConstants.FIRST_NAME, this.firstname));
                    }
                    break;
                case PayuConstants.EMAIL:
                    if (this.email == null) {
                        return getReturnData(PayuErrors.INVALID_EMAIL);
                    } else {
                        paymentHash.append(concatParams(PayuConstants.EMAIL, this.email));
                    }
                    break;
                case PayuConstants.UDF1:
                    if (this.udf1 == null) {
                        return getReturnData(PayuErrors.INVALID_UDF1);
                    } else {
                        paymentHash.append(concatParams(PayuConstants.UDF1, this.udf1));
                    }
                    break;
                case PayuConstants.UDF2:
                    if (this.udf2 == null) {
                        return getReturnData(PayuErrors.INVALID_UDF2);
                    } else {
                        paymentHash.append(concatParams(PayuConstants.UDF2, this.udf2));
                    }
                    break;
                case PayuConstants.UDF3:
                    if (this.udf3 == null) {
                        return getReturnData(PayuErrors.INVALID_UDF3);
                    } else {
                        paymentHash.append(concatParams(PayuConstants.UDF3, this.udf3));
                    }
                    break;
                case PayuConstants.UDF4:
                    if (this.udf4 == null) {
                        return getReturnData(PayuErrors.INVALID_UDF4);
                    } else {
                        paymentHash.append(concatParams(PayuConstants.UDF4, this.udf4));
                    }
                    break;
                case PayuConstants.UDF5:
                    if (this.udf5 == null) {
                        return getReturnData(PayuErrors.INVALID_UDF5);
                    } else {
                        paymentHash.append(concatParams(PayuConstants.UDF5, this.udf5));

                    }
                    break;
            }
        }

        // lets add user credentials.
        paymentHash.append(concatParams(PayuConstants.COMMAND, PayuConstants.MOBILE_HASH_TEST_WS));
        paymentHash.append(null == this.user_credentials ? concatParams(PayuConstants.USER_CREDENTIALS, PayuConstants.DEFAULT) : concatParams(PayuConstants.USER_CREDENTIALS, this.user_credentials));

        PostData postData = new PostData();
        postData.setCode(PayuErrors.NO_ERROR);
        postData.setStatus(PayuConstants.SUCCESS);
        postData.setResult(trimAmpersand(paymentHash.toString()));
        return postData;
    }
}
