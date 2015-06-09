package com.payu.sdk.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.payu.sdk.Constants;
import com.payu.sdk.GetResponseTask;
import com.payu.sdk.Params;
import com.payu.sdk.PayU;
import com.payu.sdk.PaymentListener;
import com.payu.sdk.R;
import com.payu.sdk.adapters.NetBankingAdapter;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NetBankingFragment extends ProcessPaymentFragment implements PaymentListener {

    String bankCode = "";

    ProgressDialog mProgressDialog;

    public NetBankingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View netBankingFragment = inflater.inflate(R.layout.fragment_net_banking, container, false);

        mProgressDialog = new ProgressDialog(getActivity());

        netBankingFragment.findViewById(R.id.nbPayButton);
        netBankingFragment.findViewById(R.id.nbPayButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Params requiredParams = new Params();
                requiredParams.put(PayU.BANKCODE, bankCode);
                startPaymentProcessActivity(PayU.PaymentMode.NB, requiredParams);
            }
        });

        return netBankingFragment;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // fetch the data once again if last fetched at is less than 15 min

        if ((System.currentTimeMillis() - PayU.dataFetchedAt) / (60000) < 15) {
            // dont fetch
            setupAdapter();
        } else {
            // fetch
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
                GetResponseTask getResponse = new GetResponseTask(NetBankingFragment.this);
                getResponse.execute(postParams);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }


        if(Constants.ENABLE_VAS && PayU.netBankingStatus == null){
            HashMap<String, String> varList = new HashMap<String, String>();

            varList.put("var1", "default");
            varList.put("var2", "default");
            varList.put("var3", "default");

            List<NameValuePair> postParams = null;

            try {
                postParams = PayU.getInstance(getActivity()).getParams(Constants.GET_VAS, varList);
                GetResponseTask getResponse = new GetResponseTask(NetBankingFragment.this);
                getResponse.execute(postParams);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

    }

    private void setupAdapter() {
        if(getActivity() != null && !getActivity().isFinishing()) {

            NetBankingAdapter adapter = new NetBankingAdapter(getActivity(), PayU.availableBanks);

            Spinner netBankingSpinner = (Spinner) getActivity().findViewById(R.id.netBankingSpinner);
            netBankingSpinner.setAdapter(adapter);

            netBankingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    try {
                        bankCode = ((JSONObject) adapterView.getAdapter().getItem(i)).getString("code");


                        if (bankCode.contentEquals("default")) {
                            //disable the button
//                        getActivity().findViewById(R.id.nbPayButton).setBackgroundResource(R.drawable.button);
                            getActivity().findViewById(R.id.nbPayButton).setEnabled(false);
                        } else {
                            //enable the button
//                        getActivity().findViewById(R.id.nbPayButton).setBackgroundResource(R.drawable.button_enabled);
                            if (PayU.netBankingStatus != null && PayU.netBankingStatus.get(bankCode) == 0) {
                                ((TextView) getActivity().findViewById(R.id.netBankingErrorText)).setText("Oops! " + ((JSONObject) adapterView.getAdapter().getItem(i)).getString("title") + " seems to be down. We recommend you pay using any other means of payment.");
                                getActivity().findViewById(R.id.netBankingErrorText).setVisibility(View.VISIBLE);
                            } else {
                                getActivity().findViewById(R.id.netBankingErrorText).setVisibility(View.GONE);
                            }
                            getActivity().findViewById(R.id.nbPayButton).setEnabled(true);
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

    @Override
    public void onPaymentOptionSelected(PayU.PaymentMode paymentMode) {

    }

    @Override
    public void onGetResponse(String responseMessage) {
        if(getActivity() != null && !getActivity().isFinishing()) {
            // setup adapter
            if (mProgressDialog != null && mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            if (PayU.availableBanks != null)
                setupAdapter();
            if (Constants.DEBUG)
                Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_SHORT).show();
        }
    }

}
