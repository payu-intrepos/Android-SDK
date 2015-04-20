package com.payu.sdk.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.payu.sdk.Constants;
import com.payu.sdk.GetResponseTask;
import com.payu.sdk.Params;
import com.payu.sdk.PayU;
import com.payu.sdk.Payment;
import com.payu.sdk.PaymentListener;
import com.payu.sdk.R;
import com.payu.sdk.adapters.CashCardAdapter;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class CashCardFragment extends ProcessPaymentFragment implements PaymentListener{

    String bankCode = "";

    Payment.Builder builder = new Payment().new Builder();

    ProgressDialog mProgressDialog;


    public CashCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mProgressDialog = new ProgressDialog(getActivity());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cash_card, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(PayU.availableCashCards == null){// lets fetch the cards.

            mProgressDialog.setMessage(getString(R.string.please_wait));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            List<NameValuePair> postParams = null;

            HashMap varList = new HashMap();

            if(getActivity().getIntent().getExtras().getString("user_credentials") == null){// ok we dont have a user credentials.
                varList.put(Constants.VAR1, Constants.DEFAULT);
            }else{
                varList.put(Constants.VAR1, getActivity().getIntent().getExtras().getString("user_credentials"));
            }

            try {
                postParams = PayU.getInstance(getActivity()).getParams(Constants.PAYMENT_RELATED_DETAILS, varList);
                GetResponseTask getResponse = new GetResponseTask(CashCardFragment.this);
                getResponse.execute(postParams);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }else{
            setupAdapter();
        }

        getActivity().findViewById(R.id.cashCardMakePayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Params requiredParams = new Params();
                requiredParams.put(PayU.BANKCODE, bankCode);
                startPaymentProcessActivity(PayU.PaymentMode.CASH, requiredParams);
            }
        });
    }

    @Override
    public void onPaymentOptionSelected(PayU.PaymentMode paymentMode) {

    }

    @Override
    public void onGetResponse(String responseMessage) {
        if(mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        if(Constants.DEBUG){
            Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_SHORT).show();
        }
        if(responseMessage.startsWith("Error:")){ // oops something went wrong.
            Intent intent = new Intent();
            intent.putExtra("result", responseMessage);
            getActivity().setResult(Activity.RESULT_CANCELED, intent);
            getActivity().finish();
        }else{ // we got response.
            setupAdapter();
        }
    }

    private void setupAdapter(){
        CashCardAdapter adapter = new CashCardAdapter(getActivity(), PayU.availableCashCards);

        Spinner cashCardSpinner = (Spinner) getActivity().findViewById(R.id.cashCardSpinner);
        cashCardSpinner.setAdapter(adapter);

        cashCardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    bankCode = ((JSONObject) adapterView.getAdapter().getItem(i)).getString("code");

                    if (bankCode.contentEquals("default")) {
                        getActivity().findViewById(R.id.cashCardMakePayment).setEnabled(false);
                        if(getActivity().getSupportFragmentManager().findFragmentById(R.id.cardFragmentPlaceHolder) != null)
                            getActivity().getSupportFragmentManager().beginTransaction().remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.cardFragmentPlaceHolder)).commit();
                        getActivity().findViewById(R.id.cashCardMakePayment).setVisibility(View.VISIBLE);

                    } else if(bankCode.contentEquals("CPMC")){
                        // oh, screwed, its citi rewards,
                        // lets attach the cards fragment, which will take care of validation.
                        CardsFragment cardFragment = new CardsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("rewardPoint", bankCode);
                        cardFragment.setArguments(bundle);

                        // Add the fragment to the 'fragment_container' FrameLayout
                        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.cardFragmentPlaceHolder, cardFragment).commit();
                        getActivity().findViewById(R.id.cashCardMakePayment).setVisibility(View.GONE);

                    } else {
                        if(getActivity().getSupportFragmentManager().findFragmentById(R.id.cardFragmentPlaceHolder) != null)
                            getActivity().getSupportFragmentManager().beginTransaction().remove(getActivity().getSupportFragmentManager().findFragmentById(R.id.cardFragmentPlaceHolder)).commit();

                        getActivity().findViewById(R.id.cashCardMakePayment).setVisibility(View.VISIBLE);
                        getActivity().findViewById(R.id.cashCardMakePayment).setEnabled(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
