package com.payu.sdk.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.payu.sdk.Params;
import com.payu.sdk.PayU;
import com.payu.sdk.Payment;
import com.payu.sdk.R;
import com.payu.sdk.adapters.CashCardAdapter;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class CashCardFragment extends ProcessPaymentFragment {

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
                Params requiredParams = new Params();
                requiredParams.put(PayU.BANKCODE, bankCode);
                startPaymentProcessActivity(PayU.PaymentMode.CASH, requiredParams);
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
