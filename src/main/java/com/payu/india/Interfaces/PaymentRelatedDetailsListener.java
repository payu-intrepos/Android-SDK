package com.payu.india.Interfaces;

import com.payu.india.Model.PayuResponse;

/**
 * Created by franklin on 6/4/15.
 * An Interface to Make a communication between {@link com.payu.india.Tasks.GetPaymentRelatedDetailsTask} and Calling Activity
 * Calling Activity should implement {@link PaymentRelatedDetailsListener}
 */
public interface PaymentRelatedDetailsListener {
    /**
     * As soon as we receive data in {@link com.payu.india.Tasks.GetPaymentRelatedDetailsTask}
     * {@link PaymentRelatedDetailsListener#onPaymentRelatedDetailsResponse(PayuResponse)} will be notified with response
     *
     * @param payuResponse {@link PayuResponse#getResponseStatus()}
     */
    public void onPaymentRelatedDetailsResponse(PayuResponse payuResponse);
}
