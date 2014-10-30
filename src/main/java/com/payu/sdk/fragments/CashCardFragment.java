package com.payu.sdk.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.payu.sdk.adapters.CashCardAdapter;
import com.payu.sdk.Constants;
import com.payu.sdk.Params;
import com.payu.sdk.PayU;
import com.payu.sdk.Payment;
import com.payu.sdk.ProcessPaymentActivity;
import com.payu.sdk.R;
import com.payu.sdk.exceptions.HashException;
import com.payu.sdk.exceptions.MissingParameterException;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class CashCardFragment extends Fragment {

    Payment payment;
    Params requiredParams = new Params();
    String bankCode = "";

    Payment.Builder builder = new Payment().new Builder();


    public CashCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cash_card, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CashCardAdapter adapter = new CashCardAdapter(getActivity(), PayU.availableCashCards);

        Spinner cashCardSpinner = (Spinner) getActivity().findViewById(R.id.cashCardSpinner);
        cashCardSpinner.setAdapter(adapter);

        cashCardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    bankCode = ((JSONObject) adapterView.getAdapter().getItem(i)).getString("code");
                    if (bankCode.contentEquals("null")) {
                        //disable the button
//                        getActivity().findViewById(R.id.makePayment).setBackgroundResource(R.drawable.button);
                        getActivity().findViewById(R.id.makePayment).setEnabled(false);
                    } else {
                        //enable the button
//                        getActivity().findViewById(R.id.makePayment).setBackgroundResource(R.drawable.button_enabled);
                        getActivity().findViewById(R.id.makePayment).setEnabled(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getActivity().findViewById(R.id.makePayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    builder.setMode(PayU.PaymentMode.CASH);
                    builder.setAmount(getActivity().getIntent().getExtras().getDouble(PayU.AMOUNT));
                    builder.setProductInfo(getActivity().getIntent().getExtras().getString(PayU.PRODUCT_INFO));
                    builder.setTxnId(getActivity().getIntent().getExtras().getString(PayU.TXNID));
                    builder.setSurl(getActivity().getIntent().getExtras().getString(PayU.SURL));
                    payment = builder.create();

                    requiredParams.put(PayU.TXNID, payment.getTxnId());
                    requiredParams.put(PayU.AMOUNT, String.valueOf(payment.getAmount()));
                    requiredParams.put(PayU.PRODUCT_INFO, payment.getProductInfo());
                    requiredParams.put(PayU.SURL, payment.getSurl());
                    requiredParams.put(PayU.BANKCODE, bankCode);

                    String postData = PayU.getInstance(getActivity()).createPayment(payment, requiredParams);

                    Intent intent = new Intent(getActivity(), ProcessPaymentActivity.class);
                    intent.putExtra(Constants.POST_DATA, postData);

                    //disable back button
                    if(getActivity().getIntent().getExtras().getString(PayU.DISABLE_PAYMENT_PROCESS_BACK_BUTTON) != null)
                        intent.putExtra(PayU.DISABLE_PAYMENT_PROCESS_BACK_BUTTON, getActivity().getIntent().getExtras().getString(PayU.DISABLE_PAYMENT_PROCESS_BACK_BUTTON));

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intent);

                } catch (MissingParameterException e) {
                    e.printStackTrace();
                } catch (HashException e) {
                    e.printStackTrace();
                }
            }
        });

        /*getActivity().findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount() < 1)
                    getActivity().onBackPressed();
                else
                    getActivity().getSupportFragmentManager().popBackStack();
            }
        });*/
    }
}
