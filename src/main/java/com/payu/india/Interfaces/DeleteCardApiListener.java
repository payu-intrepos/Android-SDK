package com.payu.india.Interfaces;

import com.payu.india.Model.PayuResponse;

/**
 * Created by franklin on 6/19/15.
 * An Interface to Make a communication between {@link com.payu.india.Tasks.DeleteCardTask} and Calling Activity
 * Calling Activity should implement {@link DeleteCardApiListener}
 */
public interface DeleteCardApiListener {
    /**
     * As soon as we receive data in {@link com.payu.india.Tasks.DeleteCardTask}
     * {@link DeleteCardApiListener#onDeleteCardApiResponse(PayuResponse)} will be notified with response     *
     *
     * @param payuResponse {@link PayuResponse#getResponseStatus()}
     */
    public void onDeleteCardApiResponse(PayuResponse payuResponse);
}
