package com.payu.india.Interfaces;

import com.payu.india.Model.PayuResponse;
import com.payu.india.Tasks.GetStoredCardTask;

/**
 * Created by franklin on 6/19/15.
 * An Interface to Make a communication between {@link GetStoredCardTask} and Calling Activity
 * Calling Activity should implement {@link GetStoredCardApiListener}
 */
public interface GetStoredCardApiListener {
    /**
     * As soon as we receive data in {@link com.payu.india.Tasks.GetPaymentRelatedDetailsTask}
     * {@link GetStoredCardApiListener#onGetStoredCardApiResponse(PayuResponse)} will be notified with response
     *
     * @param payuResponse {@link PayuResponse#getResponseStatus()}
     */
    public void onGetStoredCardApiResponse(PayuResponse payuResponse);
}
