package com.payu.india.Interfaces;

import com.payu.india.Model.PayuResponse;
import com.payu.india.Tasks.EditCardTask;

/**
 * Created by franklin on 6/30/15.
 * An Interface to Make a communication between {@link EditCardTask} and Calling Activity
 * Calling Activity should implement {@link EditCardApiListener}
 */
public interface EditCardApiListener {
    /**
     * As soon as we receive data in {@link EditCardTask}
     * {@link EditCardApiListener#onEditCardApiListener(PayuResponse)} will be notified with response
     *
     * @param payuResponse {@link PayuResponse#getResponseStatus()}
     */
    public void onEditCardApiListener(PayuResponse payuResponse);
}
