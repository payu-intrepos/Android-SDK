package com.payu.india.Interfaces;

import com.payu.india.Model.PayuResponse;

/**
 * Created by franklin on 7/4/15.
 * An Interface to Make a communication between {@link com.payu.india.Tasks.VerifyPaymentTask} and Calling Activity
 * Calling Activity should implement {@link VerifyPaymentApiListener}
 */
public interface VerifyPaymentApiListener {
    /**
     * As soon as we receive data in {@link com.payu.india.Tasks.VerifyPaymentTask}
     * {@link VerifyPaymentApiListener#onVerifyPaymentResponse(PayuResponse)} will be notified with response
     *
     * @param payuResponse {@link PayuResponse#getResponseStatus()}
     */
    public void onVerifyPaymentResponse(PayuResponse payuResponse);
}
