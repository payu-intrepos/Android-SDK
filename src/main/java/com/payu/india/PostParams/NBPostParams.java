package com.payu.india.PostParams;

import com.payu.india.Model.PaymentDefaultParams;
import com.payu.india.Model.PaymentDetails;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;

/**
 * Created by franklin on 6/18/15.
 * To make a Payment using cash card user need to call {@link NBPostParams#getNBPostParams()}
 * {@link NBPostParams#NBPostParams(PaymentDefaultParams, PaymentDetails)}  requires {@link PaymentDefaultParams} and {@link PaymentDetails}
 * {@link PaymentDefaultParams }includes the basic payment mandatory default params
 * {@link PaymentDetails} is the selected Cash card object.
 */
public class NBPostParams extends PaymentDefaultPostParams {

    private PaymentDefaultParams paymentDefaultParams;
    private PaymentDetails paymentDetails;
    private StringBuilder post;

    /**
     * @param paymentDefaultParams Should have the all the mandatory params such as key, amount, txnid, productinfo, firstname, email, udf1..5, and other optional fields if any.
     * @param paymentDetails       Payment details is the selected neb banking object from your adapter.
     */
    public NBPostParams(PaymentDefaultParams paymentDefaultParams, PaymentDetails paymentDetails) {
        super(paymentDefaultParams);
        this.paymentDefaultParams = paymentDefaultParams;
        this.paymentDetails = paymentDetails;
    }

    /**
     * Once the {@link PaymentDefaultParams} and {@link PaymentDetails} are set then you can call {@link NBPostParams#getNBPostParams()}
     * {@link NBPostParams#getPaymentDefaultPostParams() } will return {@link PostData}. All validation will be taken place there.
     * if it passes everything {@link PostData#getResult()} will give the postData else it gives the error reason.
     * Set pg as NB and set the bank code from {@link PaymentDetails#getBankCode()}
     *
     * @return {@link PostData}
     */
    public PostData getNBPostParams() {
        PostData postData = getPaymentDefaultPostParams();
        if (postData.getCode() == PayuErrors.NO_ERROR) {
            post = new StringBuilder();
            post.append(postData.getResult());
            post.append(concatParams(PayuConstants.PG, PayuConstants.NB));
            // lets validate payment bank code
            if (this.paymentDetails != null && this.paymentDetails.getBankCode() != null && this.paymentDetails.getBankCode().length() > 1) { // assuming we have a valid bank code now.
                post.append(concatParams(PayuConstants.BANK_CODE, this.paymentDetails.getBankCode()));
            } else {
                return getReturnData(PayuErrors.INVALID_BANKCODE_EXCEPTION, PayuErrors.INVALID_BANK_CODE);
            }
            return getReturnData(PayuErrors.NO_ERROR, PayuConstants.SUCCESS, trimAmpersand(post.toString()));
        } else {
            return postData;
        }
    }
}
