package com.payu.sdk.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.payu.sdk.Constants;
import com.payu.sdk.Luhn;
import com.payu.sdk.PayU;
import com.payu.sdk.PaymentListener;
import com.payu.sdk.R;
import com.payu.sdk.SetupCardDetails;
import com.payu.sdk.StoreCardTask;
import com.payu.sdk.adapters.SelectCardAdapter;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class EditCardFragment extends Fragment implements PaymentListener {


    ProgressDialog mProgressDialog;

    String storedCardToken;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    int mYear;
    int mMonth;
    int mDay;
    Boolean isNameOnCardValid = false;
    Boolean isCardNumberValid = false;
    Boolean isExpired = true;
    Drawable nameOnCardDrawable;
    Drawable cardNumberDrawable;
    Drawable calenderDrawable;
    Drawable cvvDrawable;
    private int expiryMonth;
    private int expiryYear;
    private String cardNumber = "";
    private String nameOnCard = "";
    private String cardType;
    private String cardMode;


    public EditCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View editCardFragment = inflater.inflate(R.layout.fragment_edit_card, container, false);


        mProgressDialog = new ProgressDialog(getActivity());

        editCardFragment.findViewById(R.id.cvvEditText).setVisibility(View.INVISIBLE);

        mYear = Calendar.getInstance().get(Calendar.YEAR);
        mMonth = Calendar.getInstance().get(Calendar.MONTH);
        mDay = Calendar.getInstance().get(Calendar.DATE);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                ((TextView) editCardFragment.findViewById(R.id.expiryDatePickerEditText)).setText("" + (i2 + 1) + " / " + i);
                expiryMonth = i2 + 1;
                expiryYear = i;

                if (expiryYear > Calendar.getInstance().get(Calendar.YEAR)) {
                    isExpired = false;
                    valid(((EditText) getActivity().findViewById(R.id.expiryDatePickerEditText)), calenderDrawable);
                } else if (expiryYear == Calendar.getInstance().get(Calendar.YEAR) && expiryMonth - 1 >= Calendar.getInstance().get(Calendar.MONTH)) {
                    isExpired = false;
                    valid(((EditText) getActivity().findViewById(R.id.expiryDatePickerEditText)), calenderDrawable);
                } else {
                    isExpired = true;
                    invalid(((EditText) getActivity().findViewById(R.id.expiryDatePickerEditText)), calenderDrawable);
                }

            }
        };

        editCardFragment.findViewById(R.id.expiryDatePickerEditText).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                    SetupCardDetails.customDatePicker(getActivity(), mDateSetListener, mYear, mMonth, mDay).show();
                return false;
            }
        });

        editCardFragment.findViewById(R.id.editCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setMessage(getString(R.string.please_wait));
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();

                String cardNumber = ((TextView) editCardFragment.findViewById(R.id.cardNumberEditText)).getText().toString();
//                cardBrand = SetupCardDetails.findIssuer(cardNumber, cardType);

                // in response cardmode: cc, card_type:visa, card_brand: master

                if (cardMode.contentEquals("CC")) {
                    cardType = "CC";
                } else {
                    if (cardNumber.startsWith("4"))
                        cardType = "VISA";
                    else
                        cardType = "MAST";
                }

                List<NameValuePair> postParams = null;

                HashMap<String, String> varList = new HashMap<String, String>();
                // user credentials
                varList.put(Constants.VAR1, getArguments().getString(PayU.USER_CREDENTIALS));
                //card token
                varList.put(Constants.VAR2, storedCardToken);
                // card name
                varList.put(Constants.VAR3, ((EditText) editCardFragment.findViewById(R.id.cardNameEditText)).getText().toString());
                //card mode cc
                varList.put(Constants.VAR4, cardMode);
                //card type
                varList.put(Constants.VAR5, cardType);
                // name on card
                varList.put(Constants.VAR6, ((EditText) editCardFragment.findViewById(R.id.nameOnCardEditText)).getText().toString());
                // card number
                varList.put(Constants.VAR7, cardNumber);
                // card exp month
                varList.put(Constants.VAR8, "" + expiryMonth);
                // card exp year
                varList.put(Constants.VAR9, "" + expiryYear);
                try {
                    postParams = PayU.getInstance(getActivity()).getParams(Constants.EDIT_USER_CARD, varList);
                    StoreCardTask getStoredCards = new StoreCardTask(EditCardFragment.this);
                    getStoredCards.execute(postParams);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }

        });

        return editCardFragment;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nameOnCardDrawable = getResources().getDrawable(R.drawable.user);
        cardNumberDrawable = getResources().getDrawable(R.drawable.card);
        calenderDrawable = getResources().getDrawable(R.drawable.calendar);
        cvvDrawable = getResources().getDrawable(R.drawable.lock);

        nameOnCardDrawable.setAlpha(100);
        cardNumberDrawable.setAlpha(100);
        calenderDrawable.setAlpha(100);
        cvvDrawable.setAlpha(100);


        ((EditText) getActivity().findViewById(R.id.nameOnCardEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, nameOnCardDrawable, null);
        ((EditText) getActivity().findViewById(R.id.cardNumberEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, cardNumberDrawable, null);
        ((EditText) getActivity().findViewById(R.id.expiryDatePickerEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, calenderDrawable, null);
        ((EditText) getActivity().findViewById(R.id.cvvEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, cvvDrawable, null);

        ((EditText) getActivity().findViewById(R.id.nameOnCardEditText)).addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                nameOnCard = ((EditText) getActivity().findViewById(R.id.nameOnCardEditText)).getText().toString();
                if (nameOnCard.length() > 1) {
                    isNameOnCardValid = true;
                    valid(((EditText) getActivity().findViewById(R.id.nameOnCardEditText)), nameOnCardDrawable);
                } else {
                    isNameOnCardValid = false;
                    invalid(((EditText) getActivity().findViewById(R.id.nameOnCardEditText)), nameOnCardDrawable);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ((EditText) getActivity().findViewById(R.id.cardNumberEditText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                cardNumber = ((EditText) getActivity().findViewById(R.id.cardNumberEditText)).getText().toString();
                if (cardNumber.length() > 0 && Luhn.validate(cardNumber)) {
                    // valid name on card
                    isCardNumberValid = true;
                    valid(((EditText) getActivity().findViewById(R.id.cardNumberEditText)), SetupCardDetails.getCardDrawable(getResources(), cardNumber));
                } else {
                    isCardNumberValid = false;
                    invalid(((EditText) getActivity().findViewById(R.id.cardNumberEditText)), cardNumberDrawable);
                    cardNumberDrawable.setAlpha(100);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getUserCards();

    }


    @Override
    public void onPaymentOptionSelected(PayU.PaymentMode paymentMode) {

    }

    @Override
    public void onGetAvailableBanks(JSONArray response) {

    }

    @Override
    public void onGetStoreCardDetails(JSONArray response) {
        mProgressDialog.dismiss();
        try {
            if (response.length() < 1) {
                getActivity().findViewById(R.id.editCard).setEnabled(false);
                getActivity().findViewById(R.id.editCard).setBackgroundResource(R.drawable.button);
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.status)
                        .setMessage(R.string.no_card_found)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        }).show();
            } else if (response.get(0).toString().contains("edited Successfully")) {

                getUserCards();

                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.status)
                        .setMessage(response.get(0).toString())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        }).show();
            } else {
                SelectCardAdapter adapter = new SelectCardAdapter(getActivity(), response);
                final Spinner selectCardSpinner = (Spinner) getActivity().findViewById(R.id.selectCardSpinner);
                selectCardSpinner.setAdapter(adapter);
                selectCardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        final JSONObject selectedCard = (JSONObject) adapterView.getAdapter().getItem(i);
                        try {
//                            clearData();
                            storedCardToken = selectedCard.getString("card_token");
                            ((EditText) getActivity().findViewById(R.id.cardNameEditText)).setText(selectedCard.getString("card_name"));
                            ((EditText) getActivity().findViewById(R.id.nameOnCardEditText)).setText(selectedCard.getString("name_on_card"));
                            ((EditText) getActivity().findViewById(R.id.cardNumberEditText)).getText().clear();
                            ((EditText) getActivity().findViewById(R.id.cardNumberEditText)).setHint(selectedCard.getString("card_no"));
                            ((EditText) getActivity().findViewById(R.id.expiryDatePickerEditText)).setText(selectedCard.getString("expiry_year") + "/" + selectedCard.getString("expiry_month"));
                            expiryMonth = selectedCard.getInt("expiry_month");
                            expiryYear = selectedCard.getInt("expiry_year");
                            isExpired = false;
                            cardMode = selectedCard.getString("card_mode");
//                            cardType = selectedCard.getString("card_type");
                            valid(((EditText) getActivity().findViewById(R.id.expiryDatePickerEditText)), calenderDrawable);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void valid(EditText editText, Drawable drawable) {
        drawable.setAlpha(255);
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        if (isCardNumberValid && !isExpired && isNameOnCardValid && storedCardToken != null) {
            getActivity().findViewById(R.id.editCard).setEnabled(true);
//            getActivity().findViewById(R.id.editCard).setBackgroundResource(R.drawable.button);
        } else {
            getActivity().findViewById(R.id.editCard).setEnabled(false);
//            getActivity().findViewById(R.id.editCard).setBackgroundResource(R.drawable.button);
        }
    }

    private void invalid(EditText editText, Drawable drawable) {
        drawable.setAlpha(100);
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        getActivity().findViewById(R.id.editCard).setEnabled(false);
        getActivity().findViewById(R.id.editCard).setBackgroundResource(R.drawable.button);
    }

    private void getUserCards() {

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        List<NameValuePair> postParams = null;

        HashMap varList = new HashMap();
        varList.put(Constants.VAR1, getArguments().getString(PayU.USER_CREDENTIALS));

        try {
            postParams = PayU.getInstance(getActivity()).getParams(Constants.GET_USER_CARDS, varList);
            StoreCardTask getStoredCards = new StoreCardTask(EditCardFragment.this);
            getStoredCards.execute(postParams);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
