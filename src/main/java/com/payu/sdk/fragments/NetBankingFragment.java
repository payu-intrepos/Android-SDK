package com.payu.sdk.fragments;


import android.app.ProgressDialog;
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
import com.payu.sdk.adapters.NetBankingAdapter;
import com.payu.sdk.Params;
import com.payu.sdk.PayU;
import com.payu.sdk.Payment;
import com.payu.sdk.PaymentListener;
import com.payu.sdk.ProcessPaymentActivity;
import com.payu.sdk.R;
import com.payu.sdk.exceptions.HashException;
import com.payu.sdk.exceptions.MissingParameterException;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NetBankingFragment extends Fragment implements PaymentListener {


    Payment payment;
    Params requiredParams = new Params();
    String bankCode = "";

    ProgressDialog mProgressDialog;

    Payment.Builder builder = new Payment().new Builder();

    public NetBankingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View netBankingFragment = inflater.inflate(R.layout.fragment_net_banking, container, false);

        mProgressDialog = new ProgressDialog(getActivity());

        netBankingFragment.findViewById(R.id.nbPayButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    builder.setMode(PayU.PaymentMode.NB);
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

//                    if (getActivity().getIntent().getExtras().getString(PayU.DROP_CATEGORY) != null) {
//                        requiredParams.put(PayU.DROP_CATEGORY, getActivity().getIntent().getExtras().getString(PayU.DROP_CATEGORY));
//                    }

                    // get the parameters required
                    String postData = PayU.getInstance(getActivity()).createPayment(payment, requiredParams);
                    // we get post data and url here,,,we launch ProcessPaymentActivity from here..
//
                    Intent intent = new Intent(getActivity(), ProcessPaymentActivity.class);
                    intent.putExtra("postData", postData);

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

            HashMap<String, String> varList = new HashMap<String, String>();
            varList.put(Constants.VAR1, Constants.DEFAULT);

            try {
                postParams = PayU.getInstance(getActivity()).getParams(Constants.GET_IBIBO_CODES, varList);
                GetResponseTask getResponse = new GetResponseTask(NetBankingFragment.this);
                getResponse.execute(postParams);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        /*getActivity().findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });*/

    }

    private void setupAdapter() {

        NetBankingAdapter adapter = new NetBankingAdapter(getActivity(), PayU.availableBanks);

        Spinner netBankingSpinner = (Spinner) getActivity().findViewById(R.id.netBankingSpinner);
        netBankingSpinner.setAdapter(adapter);

        netBankingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try {
                    bankCode = ((JSONObject) adapterView.getAdapter().getItem(i)).getString("code");
                    if (bankCode.contentEquals("null")) {
                        //disable the button
//                        getActivity().findViewById(R.id.nbPayButton).setBackgroundResource(R.drawable.button);
                        getActivity().findViewById(R.id.nbPayButton).setEnabled(false);
                    } else {
                        //enable the button
//                        getActivity().findViewById(R.id.nbPayButton).setBackgroundResource(R.drawable.button_enabled);
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

    @Override
    public void onPaymentOptionSelected(PayU.PaymentMode paymentMode) {

    }

    @Override
    public void onGetAvailableBanks(JSONArray response) {
        // setup adapter
        mProgressDialog.dismiss();
        setupAdapter();
    }

    @Override
    public void onGetStoreCardDetails(JSONArray response) {

    }
}
