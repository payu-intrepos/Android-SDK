package com.payu.india.Interfaces;

import com.payu.india.Model.PayuResponse;

/**
 * Created by franklin on 7/1/15.
 * An Interface to Make a communication between {@link com.payu.india.Tasks.GetTransactionInfoTask} and Calling Activity
 * Calling Activity should implement {@link GetTransactionInfoApiListener}
 */
public interface GetTransactionInfoApiListener {
    /**
     * As soon as we receive data in {@link com.payu.india.Tasks.GetTransactionInfoTask}
     * {@link GetTransactionInfoApiListener#onGetTransactionApiListener(PayuResponse)}  will be notified with response
     *
     * @param payuResponse {@link PayuResponse#getResponseStatus()}
     */
    public void onGetTransactionApiListener(PayuResponse payuResponse);
}
