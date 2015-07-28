package com.payu.india.Interfaces;

import com.payu.india.Model.PayuResponse;

/**
 * Created by franklin on 7/1/15.
 * An Interface to Make a communication between {@link com.payu.india.Tasks.GetIbiboCodesTask} and Calling Activity
 * Calling Activity should implement {@link GetCardInformationApiListener}
 * Do not use this, use {@link com.payu.india.Tasks.GetPaymentRelatedDetailsTask} instead.
 */
public interface GetIbiboCodesApiListener {

    /**
     * Deprecated use {@link com.payu.india.Interfaces.PaymentRelatedDetailsListener#onPaymentRelatedDetailsResponse(PayuResponse)}
     * As soon as we receive data in {@link com.payu.india.Tasks.GetPaymentRelatedDetailsTask}
     * {@link GetIbiboCodesApiListener#onGetIbiboCodesApiResponse(PayuResponse)}  will be notified with response
     *
     * @param payuResponse {@link PayuResponse#getResponseStatus()}
     */

    @Deprecated
    public void onGetIbiboCodesApiResponse(PayuResponse payuResponse);
}
