package com.payu.india.Interfaces;

import com.payu.india.Model.PayuResponse;

/**
 * Created by franklin on 7/1/15.
 * An Interface to Make a communication between {@link com.payu.india.Tasks.GetOfferStatusTask} and Calling Activity
 * Calling Activity should implement {@link GetOfferStatusApiListener}
 */
public interface GetOfferStatusApiListener {
    /**
     * As soon as we receive data in {@link com.payu.india.Tasks.GetOfferStatusTask}
     * {@link GetOfferStatusApiListener#onGetOfferStatusApiResponse(PayuResponse)}   will be notified with response
     *
     * @param payuResponse {@link PayuResponse#getResponseStatus()}
     */
    public void onGetOfferStatusApiResponse(PayuResponse payuResponse);
}
