package com.payu.india.Interfaces;

import com.payu.india.Model.PayuResponse;
import com.payu.india.Tasks.SaveCardTask;

/**
 * Created by franklin on 6/26/15.
 * An Interface to Make a communication between {@link SaveCardTask} and Calling Activity
 * Calling Activity should implement {@link GetStoredCardApiListener}
 */
public interface SaveCardApiListener {
    /**
     * As soon as we receive data in {@link SaveCardTask}
     * {@link SaveCardApiListener#onSaveCardResponse(PayuResponse)}  will be notified with response
     *
     * @param payuResponse {@link PayuResponse#getResponseStatus()}
     */
    public void onSaveCardResponse(PayuResponse payuResponse);
}
