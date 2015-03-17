package com.payu.sdk.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.payu.sdk.Constants;
import com.payu.sdk.GetResponseTask;
import com.payu.sdk.PayU;
import com.payu.sdk.PaymentListener;
import com.payu.sdk.R;
import com.payu.sdk.adapters.SelectCardAdapter;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;


public class DeleteCardFragment extends Fragment implements PaymentListener {

    ProgressDialog mProgressDialog;

    String storedCardToken;

    public DeleteCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View deleteCardFragment = inflater.inflate(R.layout.fragment_delete_card, container, false);

        mProgressDialog = new ProgressDialog(getActivity());


        deleteCardFragment.findViewById(R.id.deleteCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<NameValuePair> postParams = null;

                HashMap<String, String> varList = new HashMap<String, String>();

                // user credentials
                varList.put(Constants.VAR1, getArguments().getString(PayU.USER_CREDENTIALS));
                //card token
                varList.put(Constants.VAR2, storedCardToken);

                try {
                    postParams = PayU.getInstance(getActivity()).getParams(Constants.DELETE_USER_CARD, varList);
                    GetResponseTask deleteCardTask = new GetResponseTask(DeleteCardFragment.this);
                    deleteCardTask.execute(postParams);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });

        return deleteCardFragment;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(PayU.storedCards.length() < 1){
            getUserCards();
        }

    }


    @Override
    public void onPaymentOptionSelected(PayU.PaymentMode paymentMode) {

    }

    @Override
    public void onGetResponse(String responseMessage) {


        mProgressDialog.dismiss();

        if(responseMessage.startsWith("Error:")){ // oops something went wrong.
            Intent intent = new Intent();
            intent.putExtra("result", responseMessage);
            getActivity().setResult(Activity.RESULT_CANCELED, intent);
            getActivity().finish();
        }

        if (PayU.storedCards.length() < 1) {
            // disable the spinner
            getActivity().findViewById(R.id.selectCardSpinner).setVisibility(View.GONE);
            getActivity().findViewById(R.id.deleteCard).setBackgroundResource(R.drawable.button);
            getActivity().findViewById(R.id.deleteCard).setEnabled(false);
            // disable the button.
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.status)
                    .setMessage(R.string.no_card_found)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    }).show();
        } else if (responseMessage.contains("deleted successfully")) {

            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.status)
                    .setMessage(responseMessage.toString())
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            getUserCards();
                        }
                    }).show();
        } else {
            getActivity().findViewById(R.id.selectCardSpinner).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.deleteCard).setBackgroundResource(R.drawable.button);
            getActivity().findViewById(R.id.deleteCard).setEnabled(true);

            SelectCardAdapter adapter = new SelectCardAdapter(getActivity(), PayU.storedCards);
            Spinner selectCardSpinner = (Spinner) getActivity().findViewById(R.id.selectCardSpinner);
            selectCardSpinner.setAdapter(adapter);
            selectCardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    final JSONObject selectedCard = (JSONObject) adapterView.getAdapter().getItem(i);
                    try {
                        storedCardToken = selectedCard.getString("card_token");
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

    private void getUserCards() {
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        List<NameValuePair> postParams = null;

        HashMap varList = new HashMap();
        varList.put(Constants.VAR1, getArguments().getString(PayU.USER_CREDENTIALS));

        try {
            postParams = PayU.getInstance(getActivity()).getParams(Constants.GET_USER_CARDS, varList);
            GetResponseTask getStoredCards = new GetResponseTask(DeleteCardFragment.this);
            getStoredCards.execute(postParams);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
