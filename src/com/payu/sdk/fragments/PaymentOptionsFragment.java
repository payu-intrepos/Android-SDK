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
import android.widget.ListView;
import com.payu.sdk.Constants;
import com.payu.sdk.GetResponseTask;
import com.payu.sdk.PayU;
import com.payu.sdk.PaymentListener;
import com.payu.sdk.R;
import com.payu.sdk.adapters.PaymentModeAdapter;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PaymentOptionsFragment extends Fragment implements PaymentListener {
    PaymentListener mPaymentListener;
    ProgressDialog mProgressDialog;
    PayU.PaymentMode[] paymentOptions;
    private PayU.PaymentMode[] mAvailableOptions;


    public PaymentOptionsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().getIntArray(PayU.PAYMENT_OPTIONS) != null) {
            int modes[] = getArguments().getIntArray(PayU.PAYMENT_OPTIONS);
            mAvailableOptions = new PayU.PaymentMode[modes.length];
            int i = 0;
            for (int m : modes) {
                mAvailableOptions[i++] = PayU.PaymentMode.values()[m];
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_options, container, false);

        mProgressDialog = new ProgressDialog(getActivity());

        getResources().getDrawable(R.drawable.card).setAlpha(255);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }

        mPaymentListener = (PaymentListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPaymentListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getActivity() != null && !isRemoving() && isAdded()){
            mProgressDialog.setMessage(getString(R.string.please_wait));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
//        PayU.PaymentMode[] paymentOptions;

        if (paymentOptions == null) {
            paymentOptions = PayU.PaymentMode.values();

            // call api to find the available payment modes.

            List<NameValuePair> postParams = null;

            HashMap varList = new HashMap();

            if(getActivity().getIntent().getExtras().getString("user_credentials") == null){// ok we have a user credentials.
                varList.put(Constants.VAR1, Constants.DEFAULT);
            }else{
                varList.put(Constants.VAR1, getActivity().getIntent().getExtras().getString("user_credentials"));
            }

            try {
//                postParams = PayU.getInstance(getActivity()).getParams(Constants.GET_IBIBO_CODES, varList);
                postParams = PayU.getInstance(getActivity()).getParams(Constants.PAYMENT_RELATED_DETAILS, varList);
                GetResponseTask getResponse = new GetResponseTask(PaymentOptionsFragment.this);
                getResponse.execute(postParams);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        } else {
            setAvailableModes(null, PayU.paymentOptions);
        }

    }

    @Override
    public void onPaymentOptionSelected(PayU.PaymentMode paymentMode) {

    }

    @Override
    public void onGetResponse(String responseMessage){
        // list of available payment modes for the merchant

        if(PayU.availableModes != null){  // not ok something went wrong with the api call
            JSONArray availableModes = PayU.availableModes;

            if (mAvailableOptions != null) {
                List<String> availableModesList = new ArrayList<String>();
                List<String> availableOptionsList = new ArrayList<String>();
                try {
                    for (int i = 0; i < availableModes.length(); i++)
                        availableModesList.add(availableModes.getString(i));
                    for (int i = 0; i < mAvailableOptions.length; i++)
                        availableOptionsList.add(mAvailableOptions[i].toString());

                    availableModesList.toString();

                    availableOptionsList.retainAll(availableModesList);

                    availableOptionsList.toString();

                    paymentOptions = new PayU.PaymentMode[availableOptionsList.size()];
                    for (int i = 0; i < availableOptionsList.size(); i++) {
                        paymentOptions[i] = PayU.PaymentMode.valueOf(availableOptionsList.get(i));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                PayU.paymentOptions = paymentOptions;

                setAvailableModes(null, paymentOptions);
            } else
                setAvailableModes(availableModes, null);
        }else{
            Intent intent = new Intent();
            intent.putExtra("result", responseMessage);
            getActivity().setResult(Activity.RESULT_CANCELED, intent);
            getActivity().finish();
        }
    }

    private void setAvailableModes(JSONArray availableModes, PayU.PaymentMode[] availableOptions) {
        PaymentModeAdapter adapter;

        if (availableOptions != null)
            adapter = new PaymentModeAdapter(getActivity(), availableOptions);
        else {
            try {
                paymentOptions = new PayU.PaymentMode[availableModes.length()];
                for (int i = 0; i < availableModes.length(); i++) {
                    paymentOptions[i] = PayU.PaymentMode.valueOf(availableModes.getString(i));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            PayU.paymentOptions = paymentOptions;

            adapter = new PaymentModeAdapter(getActivity(), paymentOptions);
        }

        if(getActivity() != null){
            ListView listView = (ListView) getActivity().findViewById(R.id.paymentOptionsListView);
            listView.setAdapter(adapter);

            mProgressDialog.dismiss();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mPaymentListener.onPaymentOptionSelected((PayU.PaymentMode) adapterView.getAdapter().getItem(i));
                }
            });
        }

    }
}
