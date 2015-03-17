package com.payu.sdk.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.payu.sdk.Constants;
import com.payu.sdk.Params;
import com.payu.sdk.PayU;
import com.payu.sdk.Payment;
import com.payu.sdk.ProcessPaymentActivity;
import com.payu.sdk.exceptions.HashException;
import com.payu.sdk.exceptions.MissingParameterException;

/**
 * Created by franklin on 10/12/14.
 */
public class ProcessPaymentFragment extends Fragment {
    protected void startPaymentProcessActivity(PayU.PaymentMode paymentMode, Params requiredParams) {
        try {
            Payment payment;
            Payment.Builder builder = new Payment().new Builder();

            builder.set(PayU.MODE, String.valueOf(paymentMode));
            for (String key : getActivity().getIntent().getExtras().keySet()) {
                builder.set(key, String.valueOf(getActivity().getIntent().getExtras().get(key)));
                requiredParams.put(key, builder.get(key));
            }

            payment = builder.create();

            String postData = PayU.getInstance(getActivity()).createPayment(payment, requiredParams);

            Intent intent = new Intent(getActivity(), ProcessPaymentActivity.class);
            intent.putExtra(Constants.POST_DATA, postData);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            getActivity().startActivityForResult(intent, PayU.RESULT);

        } catch (MissingParameterException e) {
            e.printStackTrace();
        } catch (HashException e) {
            e.printStackTrace();
        }
    }
}
