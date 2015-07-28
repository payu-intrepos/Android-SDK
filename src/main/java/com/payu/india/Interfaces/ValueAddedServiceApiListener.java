package com.payu.india.Interfaces;

import com.payu.india.Model.PayuResponse;

/**
 * Created by franklin on 7/1/15.
 * An Interface to Make a communication between {@link com.payu.india.Tasks.ValueAddedServiceTask} and Calling Activity
 * Calling Activity should implement {@link VerifyPaymentApiListener}
 */
public interface ValueAddedServiceApiListener {
    /**
     * As soon as we receive data in {@link com.payu.india.Tasks.ValueAddedServiceTask}
     * {@link ValueAddedServiceApiListener#onValueAddedServiceApiResponse(PayuResponse)} will be notified with response
     *
     * @param payuResponse {@link PayuResponse#getResponseStatus()}
     */
    public void onValueAddedServiceApiResponse(PayuResponse payuResponse);
}
