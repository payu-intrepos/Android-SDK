package com.payu.sdk.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.payu.sdk.Constants;
import com.payu.sdk.GetResponseTask;
import com.payu.sdk.Params;
import com.payu.sdk.PayU;
import com.payu.sdk.PaymentListener;
import com.payu.sdk.R;
import com.payu.sdk.adapters.StoredCardAdapter;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */

public class StoredCardFragment extends ProcessPaymentFragment implements PaymentListener {


    ProgressDialog mProgressDialog;

    String selectedItem = "Credit card";

    long mLastClickTime = 0;

    public StoredCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View storedCardFragment = inflater.inflate(R.layout.fragment_stored_card, container, false);

        mProgressDialog = new ProgressDialog(getActivity());

        return storedCardFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         getActivity().findViewById(R.id.useNewCardButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString(PayU.STORE_CARD, "store_card");

                CardsFragment cardsFragment = new CardsFragment();
                cardsFragment.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer, cardsFragment);
                transaction.commit();

            }
        });

        if(PayU.storedCards == null){
            fetchStoredCards();
        }else{
            setupAdapter();
        }
    }

    @Override
    public void onPaymentOptionSelected(PayU.PaymentMode paymentMode) {

    }

    @Override
    public void onGetResponse(String responseMessage) {

        if(Constants.DEBUG){
            Toast.makeText(getActivity(), responseMessage, Toast.LENGTH_SHORT).show();
        }

        if(PayU.storedCards != null){
            setupAdapter();
        }
    }


    private void makePayment(JSONObject selectedCard, String cvv) {
        Params requiredParams = new Params();
        try {

            requiredParams.put(PayU.CVV, cvv);

            requiredParams.put("store_card_token", selectedCard.getString("card_token"));

            startPaymentProcessActivity(PayU.PaymentMode.valueOf(selectedCard.getString("card_mode")), requiredParams);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void confirmDelete(final JSONObject selectedCard){
        try {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Delete card")
                    .setCancelable(false)
                    .setMessage("Do you want to delete " + selectedCard.getString("card_no") + " ?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            deleteCard(selectedCard);
                        }
                    }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Do nothing.
                }
            }).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private  void deleteCard(JSONObject selectedCard){

        mProgressDialog = new ProgressDialog(getActivity());

        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        try {
            List<NameValuePair> postParams = null;
            HashMap<String, String> varList = new HashMap<String, String>();
            // user credentials
            varList.put(Constants.VAR1, getActivity().getIntent().getExtras().getString(PayU.USER_CREDENTIALS));
            //card token
            varList.put(Constants.VAR2, selectedCard.getString("card_token"));
            postParams = PayU.getInstance(getActivity()).getParams(Constants.DELETE_USER_CARD, varList);
            GetResponseTask getStoredCards = new GetResponseTask(StoredCardFragment.this);
            getStoredCards.execute(postParams);
            fetchStoredCards();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fetchStoredCards(){

        if(mProgressDialog == null)
            mProgressDialog = new ProgressDialog(getActivity());

        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();


        List<NameValuePair> postParams = null;

        HashMap<String, String> varList = new HashMap<String, String>();
        varList.put(Constants.VAR1, getActivity().getIntent().getExtras().getString(PayU.USER_CREDENTIALS));

        try {
            postParams = PayU.getInstance(getActivity()).getParams(Constants.GET_USER_CARDS, varList);
            GetResponseTask getStoredCards = new GetResponseTask(StoredCardFragment.this);
            getStoredCards.execute(postParams);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void setupAdapter() {

        StoredCardAdapter adapter = new StoredCardAdapter(getActivity(), PayU.storedCards);

        if (PayU.storedCards.length() < 1) {
            getActivity().findViewById(R.id.noCardFoundTextView).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.savedCardTextView).setVisibility(View.GONE);
        }
        ListView listView = (ListView) getActivity().findViewById(R.id.storedCardListView);
        listView.setAdapter(adapter);

        mProgressDialog.dismiss();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {// make payment.
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){ // to prevent quick double click.
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                final JSONObject selectedCard = (JSONObject) adapterView.getAdapter().getItem(i);
                final EditText input = new EditText(getActivity());
                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(50, 0, 50, 0);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                input.setTransformationMethod(PasswordTransformationMethod.getInstance());
                int cvvLength;
                try {
                    input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(selectedCard.getString("card_no").matches("^3[47]+[0-9|X]*") ? 4 :3)});
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                input.setBackgroundResource(R.drawable.rectangle_box);
                input.setLines(1);
                input.setCompoundDrawablesWithIntrinsicBounds(null, null, getActivity().getResources().getDrawable(R.drawable.lock), null);
                linearLayout.addView(input, layoutParams);

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(linearLayout);
                builder.setTitle(Constants.CVV_TITLE);
                builder.setMessage(Constants.CVV_MESSAGE);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        makePayment(selectedCard, input.getText().toString());
                    }
                });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                final AlertDialog dialog = builder.create();

                if(!dialog.isShowing())
                    dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false); // initially ok button is disabled

                input.addTextChangedListener( new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            if((selectedCard.getString("card_no").matches("^3[47]+[0-9|X]*")  && input.getText().length() == 4) || (!selectedCard.getString("card_no").matches("^3[47]+[0-9|X]*")) && input.getText().length() == 3){ // ok allow the user to make payment
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            }else {// no dont allow the user to make payment
                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {// delete card.
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) { // to prevent double click
                    return false;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                final JSONObject selectedCard = (JSONObject) parent.getAdapter().getItem(position);

                confirmDelete(selectedCard);

                return false;
            }
        });
    }

}
