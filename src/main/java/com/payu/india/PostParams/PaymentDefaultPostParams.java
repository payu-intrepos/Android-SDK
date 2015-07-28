package com.payu.india.PostParams;

import com.payu.india.Model.PaymentDefaultParams;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.india.Payu.PayuUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by franklin on 6/18/15.
 * Simple class to verify and append necessary characters.
 * it includes payment default params such as key, txnid, amout, product info etc...
 */
public class PaymentDefaultPostParams extends PayuUtils {

    private PaymentDefaultParams paymentDefaultParams;
    private StringBuilder post;

    /**
     * Do not allow the user to create an empty object.
     */
    private PaymentDefaultPostParams() {
    }

    /**
     * @param paymentDefaultParams should have all the mandatory params like key, txnid, amout, firstname, email, udf1..5, hash and the option params phone, shippingaddress, etc..
     */
    protected PaymentDefaultPostParams(PaymentDefaultParams paymentDefaultParams) {
        this.paymentDefaultParams = paymentDefaultParams;
    }

    /**
     * Method to validate all mandatory and non mandatory params.
     * Append the necessary characters to make actual post data
     * find the command then
     * Validate all the post params accordingly.
     *
     * @return postData
     */
    protected PostData getPaymentDefaultPostParams() {
        post = null;
        post = new StringBuilder();

        // lets set the default Parameters
        post.append(concatParams(PayuConstants.DEVICE_TYPE, "1"));
        //TODO we gotta set Instrument_type and instrument_id something like
//        post.append(concatParams(PayuConstants.INSTRUMENT_TYPE, ""));
//        post.append(concatParams(PayuConstants.INSTRUMENT_ID, ""));

        // lets begin with the mandatory default params.
        // TODO apply the validation according to the pg, payment mode!
        for (int i = 0; i < PayuConstants.PAYMENT_PARAMS_ARRAY.length; i++) {
            switch (PayuConstants.PAYMENT_PARAMS_ARRAY[i]) {
//                PayuConstants.KEY, PayuConstants.TXNID, PayuConstants.AMOUNT, PayuConstants.PRODUCT_INFO, PayuConstants.FIRST_NAME, PayuConstants.EMAIL, PayuConstants.SURL, PayuConstants.FURL, PayuConstants.HASH
                case PayuConstants.KEY: // TODO add validation for key
                    if (this.paymentDefaultParams.getKey() == null || this.paymentDefaultParams.getKey().length() < 1)
                        return getReturnData(PayuErrors.MANDATORY_PARAM_KEY_IS_MISSING);
                    post.append(concatParams(PayuConstants.KEY, this.paymentDefaultParams.getKey()));
                    break;
                case PayuConstants.TXNID: // TODO add validation for txnid
                    if (this.paymentDefaultParams.getTxnId() == null || this.paymentDefaultParams.getTxnId().length() < 1)
                        return getReturnData(PayuErrors.MANDATORY_PARAM_TXNID_IS_MISSING);
                    post.append(concatParams(PayuConstants.TXNID, this.paymentDefaultParams.getTxnId()));
                    break;
                case PayuConstants.AMOUNT: // validation for amount
                    Double amount = 0.0;
                    try { // this will take care of null check also!
                        amount = this.paymentDefaultParams != null ? Double.parseDouble(this.paymentDefaultParams.getAmount()) : 0.0;
                    } catch (NumberFormatException e) {
                        return getReturnData(PayuErrors.NUMBER_FORMAT_EXCEPTION, PayuErrors.INVALID_AMOUNT);
                    } catch (NullPointerException e) {
                        return getReturnData(PayuErrors.INVALID_AMOUNT_EXCEPTION, PayuErrors.INVALID_AMOUNT);
                    }
                    if (amount < 1) {
                        return getReturnData(PayuErrors.INVALID_AMOUNT_EXCEPTION, PayuErrors.INVALID_AMOUNT);
                    }
                    post.append(concatParams(PayuConstants.AMOUNT, this.paymentDefaultParams.getAmount()));
                    break;
                case PayuConstants.PRODUCT_INFO: // TODO add validation for product info
                    if (this.paymentDefaultParams.getProductInfo() == null || this.paymentDefaultParams.getProductInfo().length() < 1)
                        return getReturnData(PayuErrors.MANDATORY_PARAM_PRODUCT_INFO_IS_MISSING);
                    post.append(concatParams(PayuConstants.PRODUCT_INFO, this.paymentDefaultParams.getProductInfo()));
                    break;
                case PayuConstants.FIRST_NAME: // TODO add validation for first name
                    if (this.paymentDefaultParams.getFirstName() == null) // empty string is allowed
                        return getReturnData(PayuErrors.MANDATORY_PARAM_FIRST_NAME_IS_MISSING);
                    post.append(concatParams(PayuConstants.FIRST_NAME, this.paymentDefaultParams.getFirstName()));
                    break;
                case PayuConstants.EMAIL: // TODO add validation for email
                    if (this.paymentDefaultParams.getEmail() == null)
                        return getReturnData(PayuErrors.MANDATORY_PARAM_EMAIL_IS_MISSING);
                    post.append(concatParams(PayuConstants.EMAIL, this.paymentDefaultParams.getEmail()));
                    break;
                case PayuConstants.SURL: // TODO add validation for SURL
                    if (this.paymentDefaultParams.getSurl() == null || this.paymentDefaultParams.getSurl().length() < 1)
                        return getReturnData(PayuErrors.MANDATORY_PARAM_SURL_IS_MISSING);
                    // we gotta encode surl
                    try {
                        post.append(PayuConstants.SURL + "=" + URLEncoder.encode(this.paymentDefaultParams.getSurl(), "UTF-8") + "&");
                    } catch (UnsupportedEncodingException e) {
                        return getReturnData(PayuErrors.UN_SUPPORTED_ENCODING_EXCEPTION, PayuConstants.SURL + PayuErrors.INVALID_URL);
                    }
                    break;
                case PayuConstants.FURL: // TODO add validation for FURL
                    if (this.paymentDefaultParams.getFurl() == null || this.paymentDefaultParams.getFurl().length() < 1)
                        return getReturnData(PayuErrors.MANDATORY_PARAM_FURL_IS_MISSING);
                    // we gotta encode furl
                    try {
                        post.append(PayuConstants.FURL + "=" + URLEncoder.encode(this.paymentDefaultParams.getFurl(), "UTF-8") + "&");
                    } catch (UnsupportedEncodingException e) {
                        return getReturnData(PayuErrors.UN_SUPPORTED_ENCODING_EXCEPTION, PayuConstants.FURL + PayuErrors.INVALID_URL);
                    }
                    break;
                case PayuConstants.HASH: // TODO add validation for Hash
                    if (this.paymentDefaultParams.getHash() == null || this.paymentDefaultParams.getHash().length() < 1)
                        return getReturnData(PayuErrors.MANDATORY_PARAM_HASH_IS_MISSING);
                    post.append(concatParams(PayuConstants.HASH, this.paymentDefaultParams.getHash()));
                    break;
                case PayuConstants.UDF1:
                    if (this.paymentDefaultParams.getUdf1() == null)
                        return getReturnData(PayuErrors.INVALID_UDF1);
                    post.append(concatParams(PayuConstants.UDF1, this.paymentDefaultParams.getUdf1()));
                    break;
                case PayuConstants.UDF2: // TODO add validation for UDF2
                    if (this.paymentDefaultParams.getUdf2() == null)
                        return getReturnData(PayuErrors.INVALID_UDF2);
                    post.append(concatParams(PayuConstants.UDF2, this.paymentDefaultParams.getUdf2()));
                    break;
                case PayuConstants.UDF3: // TODO add validation for UDF3
                    if (this.paymentDefaultParams.getUdf3() == null)
                        return getReturnData(PayuErrors.INVALID_UDF3);
                    post.append(concatParams(PayuConstants.UDF3, this.paymentDefaultParams.getUdf3()));
                    break;
                case PayuConstants.UDF4: // TODO add validation for UDF4
                    if (this.paymentDefaultParams.getUdf4() == null)
                        return getReturnData(PayuErrors.INVALID_UDF4);
                    post.append(concatParams(PayuConstants.UDF4, this.paymentDefaultParams.getUdf4()));
                    break;
                case PayuConstants.UDF5: // TODO add validation for UDF5
                    if (this.paymentDefaultParams.getUdf5() == null)
                        return getReturnData(PayuErrors.INVALID_UDF5);
                    post.append(concatParams(PayuConstants.UDF5, this.paymentDefaultParams.getUdf5()));
                    break;
            }
        }

        if (this.paymentDefaultParams.getPhone() != null) { // TODO add phone number validation
            post.append(concatParams(PayuConstants.PHONE, this.paymentDefaultParams.getPhone()));
        }

        // optional fields.
        post.append(this.paymentDefaultParams.getOfferKey() != null ? concatParams(PayuConstants.OFFER_KEY, this.paymentDefaultParams.getOfferKey()) : "");
        post.append(this.paymentDefaultParams.getLastName() != null ? concatParams(PayuConstants.LASTNAME, this.paymentDefaultParams.getLastName()) : "");
        post.append(this.paymentDefaultParams.getAddress1() != null ? concatParams(PayuConstants.ADDRESS1, this.paymentDefaultParams.getAddress1()) : "");
        post.append(this.paymentDefaultParams.getAddress2() != null ? concatParams(PayuConstants.ADDRESS2, this.paymentDefaultParams.getAddress2()) : "");
        post.append(this.paymentDefaultParams.getCity() != null ? concatParams(PayuConstants.CITY, this.paymentDefaultParams.getCity()) : "");
        post.append(this.paymentDefaultParams.getState() != null ? concatParams(PayuConstants.STATE, this.paymentDefaultParams.getState()) : "");
        post.append(this.paymentDefaultParams.getCountry() != null ? concatParams(PayuConstants.COUNTRY, this.paymentDefaultParams.getCountry()) : "");
        post.append(this.paymentDefaultParams.getZipCode() != null ? concatParams(PayuConstants.ZIPCODE, this.paymentDefaultParams.getZipCode()) : "");
        post.append(this.paymentDefaultParams.getCodUrl() != null ? concatParams(PayuConstants.CODURL, this.paymentDefaultParams.getCodUrl()) : "");
        post.append(this.paymentDefaultParams.getDropCategory() != null ? concatParams(PayuConstants.DROP_CATEGORY, this.paymentDefaultParams.getDropCategory()) : "");
        post.append(this.paymentDefaultParams.getEnforcePayMethod() != null ? concatParams(PayuConstants.ENFORCE_PAYMETHOD, this.paymentDefaultParams.getEnforcePayMethod()) : "");
        post.append(this.paymentDefaultParams.getCustomNote() != null ? concatParams(PayuConstants.CUSTOM_NOTE, this.paymentDefaultParams.getCustomNote()) : "");
        post.append(this.paymentDefaultParams.getNoteCategory() != null ? concatParams(PayuConstants.NOTE_CATEGORY, this.paymentDefaultParams.getNoteCategory()) : "");
        post.append(this.paymentDefaultParams.getShippingFirstName() != null ? concatParams(PayuConstants.SHIPPING_FIRSTNAME, this.paymentDefaultParams.getShippingFirstName()) : "");
        post.append(this.paymentDefaultParams.getShippingLastName() != null ? concatParams(PayuConstants.SHIPPING_LASTNAME, this.paymentDefaultParams.getShippingLastName()) : "");
        post.append(this.paymentDefaultParams.getShippingAddress1() != null ? concatParams(PayuConstants.SHIPPING_ADDRESS1, this.paymentDefaultParams.getShippingAddress1()) : "");
        post.append(this.paymentDefaultParams.getShippingAddress2() != null ? concatParams(PayuConstants.SHIPPING_ADDRESS2, this.paymentDefaultParams.getShippingAddress2()) : "");
        post.append(this.paymentDefaultParams.getShippingCity() != null ? concatParams(PayuConstants.SHIPPING_CITY, this.paymentDefaultParams.getShippingCity()) : "");
        post.append(this.paymentDefaultParams.getShippingState() != null ? concatParams(PayuConstants.SHIPPING_STATE, this.paymentDefaultParams.getShippingState()) : "");
        post.append(this.paymentDefaultParams.getShippingCounty() != null ? concatParams(PayuConstants.SHIPPING_CONTRY, this.paymentDefaultParams.getShippingCounty()) : "");
        post.append(this.paymentDefaultParams.getShippingZipCode() != null ? concatParams(PayuConstants.SHIPPING_ZIPCODE, this.paymentDefaultParams.getShippingZipCode()) : "");
        post.append(this.paymentDefaultParams.getShippingPhone() != null ? concatParams(PayuConstants.SHIPPING_PHONE, this.paymentDefaultParams.getShippingPhone()) : "");
        return getReturnData(PayuErrors.NO_ERROR, PayuConstants.SUCCESS, post.toString());
    }
}
