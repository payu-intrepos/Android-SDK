package com.payu.sdk;

import org.json.JSONArray;

/**
 * Created by franklin.michael on 27-06-2014.
 */
public interface PaymentListener {
    public void onPaymentOptionSelected(PayU.PaymentMode paymentMode);

    public void onGetAvailableBanks(JSONArray response);

    public void onGetStoreCardDetails(JSONArray response);
}
