package com.payu.sdk;

/**
 * Created by franklin.michael on 27-06-2014.
 */
public interface PaymentListener {
    public void onPaymentOptionSelected(PayU.PaymentMode paymentMode);

    public void onGetResponse(String responseMessage);
}
