package com.payu.india.Extras;

import android.os.Parcel;
import android.os.Parcelable;

import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by franklin on 6/3/15.
 * This class helps us to calculate all the hashes required by payu,
 * It includes Payment hash, Webservices hash etc.
 */
public class PayUChecksum implements Parcelable {

    public static final Parcelable.Creator<PayUChecksum> CREATOR
            = new Parcelable.Creator<PayUChecksum>() {
        public PayUChecksum createFromParcel(Parcel in) {
            return new PayUChecksum(in);
        }

        public PayUChecksum[] newArray(int size) {
            return new PayUChecksum[size];
        }
    };
    // common for both payment and webservice hash
    String key;
    String salt;
    // for payment hash
    String txnid;
    String amount;
    String productinfo;
    String firstname;
    String email;
    String udf1;
    String udf2;
    String udf3;
    String udf4;
    String udf5;
    // for webservice hash.
    String var1;
    String command;
    // required params for generating webservice hash
    private String webServiceHashParams[] = {PayuConstants.KEY, PayuConstants.COMMAND, PayuConstants.VAR1, PayuConstants.SALT};

    // required params for generating payment hash
    private String paymentHashParams[] = {PayuConstants.KEY, PayuConstants.TXNID, PayuConstants.AMOUNT, PayuConstants.PRODUCT_INFO, PayuConstants.FIRST_NAME, PayuConstants.EMAIL, PayuConstants.UDF1, PayuConstants.UDF2, PayuConstants.UDF3, PayuConstants.UDF4, PayuConstants.UDF5, PayuConstants.SALT};

    public PayUChecksum() {
    }

    private PayUChecksum(Parcel in) {
        key = in.readString();
        txnid = in.readString();
        amount = in.readString();
        productinfo = in.readString();
        firstname = in.readString();
        email = in.readString();
        udf1 = in.readString();
        udf2 = in.readString();
        udf3 = in.readString();
        udf4 = in.readString();
        udf5 = in.readString();
        salt = in.readString();
        var1 = in.readString();
        command = in.readString();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getVar1() {
        return var1;
    }

    public void setVar1(String var1) {
        this.var1 = var1;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * Simple method to calulate hash,
     * Takes the required params form getter.
     *
     * @return PostData
     * PostData will have {@link PostData#code} {@link PostData#status} {@link PostData#result}
     * code is the status code, status explains whether it is success or error, result is the actuall hash if success, error message if failure
     */
    public PostData getHash() {
        StringBuffer webServiceHash = new StringBuffer();
        if (this.command != null && this.var1 != null) { // for webservice api
            for (String k : webServiceHashParams) {
                switch (k) { //     sha512(key|command|var1|salt)
                    case PayuConstants.KEY:
                        if (this.key == null || this.key.length() < 1)
                            return getReturnData(PayuErrors.MANDATORY_PARAM_KEY_IS_MISSING);
                        webServiceHash.append(this.key + "|");
                        break;
                    case PayuConstants.COMMAND:
                        if (PayuConstants.COMMAND_SET.contains(this.command))
                            webServiceHash.append(this.command + "|");
                        else
                            return getReturnData(PayuErrors.MANDATORY_PARAM_COMMAND_IS_MISSING);
                        break;
                    case PayuConstants.VAR1:
                        if (this.var1.length() > 1)
                            webServiceHash.append(this.var1 + "|");
                        else
                            return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING);
                        break;
                    case PayuConstants.SALT:
                        if (this.salt == null || this.salt.length() < 1)
                            return getReturnData(PayuErrors.MANDATORY_PARAM_SALT_IS_MISSING);
                        else
                            webServiceHash.append(this.salt);
                        break;

                }
            }

            // okay, lets calculate the hash

            return calculateHash(webServiceHash.toString());

        } else if (this.amount != null && this.txnid != null && this.productinfo != null && this.salt != null) { // for payment hash

            StringBuilder paymentHash = new StringBuilder();
            for (String key : paymentHashParams) { //    sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||SALT)
                switch (key) {
                    case PayuConstants.KEY:
                        if (this.key == null || this.key.length() < 1) {
                            return getReturnData(PayuErrors.MANDATORY_PARAM_KEY_IS_MISSING);
                        } else {
                            paymentHash.append(this.key + "|");
                        }
                        break;
                    case PayuConstants.TXNID:
                        if (this.txnid.length() < 1) {
                            return getReturnData(PayuErrors.TRANSACTION_ID_MISSING);
                        } else {
                            paymentHash.append(this.txnid + "|");
                        }
                        break;
                    case PayuConstants.AMOUNT:
                        try {
                            if (null == amount || Double.parseDouble(this.amount) <= 0) {
                                return getReturnData(PayuErrors.INVALID_AMOUNT);
                            } else {
                                paymentHash.append(this.amount + "|");
                            }
                        } catch (NumberFormatException e) {
                            return getReturnData(PayuErrors.NUMBER_FORMAT_EXCEPTION, PayuErrors.INVALID_AMOUNT);
                        }
                        break;
                    case PayuConstants.PRODUCT_INFO:
                        if (this.productinfo == null) {
                            return getReturnData(PayuErrors.INVALID_PRODUCT_INFO);
                        } else {
                            paymentHash.append(this.productinfo + "|");
                        }
                        break;
                    case PayuConstants.FIRST_NAME:
                        if (this.firstname == null) {
                            return getReturnData(PayuErrors.INVALID_FIRST_NAME);
                        } else {
                            paymentHash.append(this.firstname + "|");
                        }
                        break;
                    case PayuConstants.EMAIL:
                        if (this.email == null) {
                            return getReturnData(PayuErrors.INVALID_EMAIL);
                        } else {
                            paymentHash.append(this.email + "|");
                        }
                        break;
                    case PayuConstants.UDF1:
                        if (this.udf1 == null) {
                            return getReturnData(PayuErrors.INVALID_UDF1);
                        } else {
                            paymentHash.append(this.udf1 + "|");
                        }
                        break;
                    case PayuConstants.UDF2:
                        if (this.udf2 == null) {
                            return getReturnData(PayuErrors.INVALID_UDF2);
                        } else {
                            paymentHash.append(this.udf2 + "|");
                        }
                        break;
                    case PayuConstants.UDF3:
                        if (this.udf3 == null) {
                            return getReturnData(PayuErrors.INVALID_UDF3);
                        } else {
                            paymentHash.append(this.udf3 + "|");
                        }
                        break;
                    case PayuConstants.UDF4:
                        if (this.udf4 == null) {
                            return getReturnData(PayuErrors.INVALID_UDF4);
                        } else {
                            paymentHash.append(this.udf4 + "|");
                        }
                        break;
                    case PayuConstants.UDF5:
                        if (this.udf5 == null) {
                            return getReturnData(PayuErrors.INVALID_UDF5);
                        } else {
                            paymentHash.append(this.udf5 + "||||||");

                        }
                        break;
                    case PayuConstants.SALT:
                        if (this.salt.length() < 1) {
                            return getReturnData(PayuErrors.INVALID_SALT);
                        } else {
                            paymentHash.append(this.salt);
                        }
                        break;
                }
            }

            return calculateHash(paymentHash.toString());
        } else {
            // could not figure out what hash is this.
            return getReturnData(PayuErrors.MANDATORY_PARAM_COMMAND_IS_MISSING + " " + PayuErrors.INVALID_AMOUNT);
        }
    }

    /**
     * Defalut param of {@link PayUChecksum#getReturnData(int, String, String)}
     * Use this if your first param is {@link PayuErrors#MISSING_PARAMETER_EXCEPTION} and second param is {@link PayuConstants#ERROR}
     *
     * @param result result it the result of your PostData
     * @return PostData object.
     */
    protected PostData getReturnData(String result) {
        return getReturnData(PayuErrors.MISSING_PARAMETER_EXCEPTION, PayuConstants.ERROR, result);
    }

    /**
     * Defalut param of {@link PayUChecksum#getReturnData(int, String, String)}
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(txnid);
        dest.writeString(amount);
        dest.writeString(productinfo);
        dest.writeString(firstname);
        dest.writeString(email);
        dest.writeString(udf1);
        dest.writeString(udf2);
        dest.writeString(udf3);
        dest.writeString(udf4);
        dest.writeString(udf5);
        dest.writeString(salt);
        dest.writeString(command);
    }

    /**
     * Apply sha-512
     * digest the string
     * generate the string
     *
     * @param hashString the string which is going to be used for generating hash
     * @return hash
     */

    private PostData calculateHash(String hashString) {
        try {
            StringBuilder hash = new StringBuilder();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            for (byte hashByte : mdbytes) {
                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }

            return getReturnData(PayuErrors.NO_ERROR, PayuConstants.SUCCESS, hash.toString());
        } catch (NoSuchAlgorithmException e) {
            return getReturnData(PayuErrors.NO_SUCH_ALGORITHM_EXCEPTION, PayuErrors.INVALID_ALGORITHM_SHA);
        }
    }


}
