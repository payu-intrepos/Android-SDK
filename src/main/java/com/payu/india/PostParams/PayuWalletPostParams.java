package com.payu.india.PostParams;

import com.payu.india.Model.PaymentDefaultParams;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;

/**
 * Created by franklin on 6/19/15.
 * To make a Payment using payumoney pasiawallet need to call {@link PayuWalletPostParams#getPayuWalletPostParams()}
 * {@link PayuWalletPostParams#PayuWalletPostParams(PaymentDefaultParams)}  requires {@link PaymentDefaultParams}
 * {@link PaymentDefaultParams }includes the basic payment mandatory default params
 */
public class PayuWalletPostParams extends PaymentDefaultPostParams {

    private PaymentDefaultParams paymentDefaultParams;
    private StringBuilder post;

    /**
     * @param paymentDefaultParams Should have the all the mandatory params such as key, amount, txnid, productinfo, firstname, email, udf1..5, and other optional fields if any.
     */
    public PayuWalletPostParams(PaymentDefaultParams paymentDefaultParams) {
        super(paymentDefaultParams);
        this.paymentDefaultParams = paymentDefaultParams;
    }

    /**
     * Once the {@link PaymentDefaultParams} set then you can call {@link PayuWalletPostParams#getPayuWalletPostParams()}
     * {@link CCDCPostParams#getPaymentDefaultPostParams()}  } will return {@link PostData}. All validation will be taken place there only.
     * if it passes everything {@link PostData#getResult()} will give the postData else it gives the error reason.
     * Set pg as {@link PayuConstants#WALLET}and set the bank code as {@link PayuConstants#PAYUW}
     *
     * @return {@link PostData}
     */
    public PostData getPayuWalletPostParams() {
        PostData postData = getPaymentDefaultPostParams();
        if (postData.getCode() == PayuErrors.NO_ERROR) {
            post = new StringBuilder();
            post.append(postData.getResult());
            post.append(concatParams(PayuConstants.BANK_CODE, PayuConstants.PAYUW.toLowerCase()));
            post.append(concatParams(PayuConstants.PG, PayuConstants.WALLET));

            return getReturnData(PayuErrors.NO_ERROR, PayuConstants.SUCCESS, trimAmpersand(post.toString()));
        } else {
            return postData;
        }
    }
}
