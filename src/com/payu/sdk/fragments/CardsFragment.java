package com.payu.sdk.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.payu.sdk.Cards;
import com.payu.sdk.Constants;
import com.payu.sdk.GetResponseTask;
import com.payu.sdk.Params;
import com.payu.sdk.PayU;
import com.payu.sdk.PaymentListener;
import com.payu.sdk.R;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class CardsFragment extends ProcessPaymentFragment implements PaymentListener {

    Boolean isCardNumberValid = false;
    Boolean isExpired = true;
    Boolean isCvvValid = false;
    Drawable nameOnCardDrawable;
    Drawable cardNumberDrawable;
    Drawable calenderDrawable;
    Drawable cvvDrawable;
    Drawable cardNameDrawable;
    Drawable issuerDrawable;
    private int expiryMonth;
    private int expiryYear;
    private String cardNumber = "";
    private String cvv = "";
    private String nameOnCard = "";
    private String cardName = "";

    private String issuer = "";

    public CardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View cardDetails = inflater.inflate(R.layout.fragment_cards, container, false);

        // initialize issuer drawable
        Cards.initializeIssuers(getResources());

        if(Constants.ENABLE_VAS && PayU.issuingBankDownBin == null){ // vas has not been called, lets fetch bank down time.
            HashMap<String, String> varList = new HashMap<String, String>();

            varList.put("var1", "default");
            varList.put("var2", "default");
            varList.put("var3", "default");

            List<NameValuePair> postParams = null;


            try {
                postParams = PayU.getInstance(getActivity()).getParams(Constants.GET_VAS, varList);
                GetResponseTask getResponse = new GetResponseTask(CardsFragment.this);
                getResponse.execute(postParams);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        ((EditText)cardDetails.findViewById(R.id.expiryDatePickerEditText)).setFocusable(false);



        cardDetails.findViewById(R.id.expiryDatePickerEditText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity(), R.style.ProgressDialog);
                View datePickerLayout = getActivity().getLayoutInflater().inflate(R.layout.date_picker, null);
                dialog.setContentView(datePickerLayout);
                dialog.setCancelable(false);
                Button okButton = (Button) datePickerLayout.findViewById(R.id.datePickerOkButton);
                final DatePicker datePicker = (DatePicker) datePickerLayout.findViewById(R.id.datePicker);

                try { // lets hide the day spinner on pre lollipop devices
                    Field datePickerFields[] = datePicker.getClass().getDeclaredFields();
                    for (Field datePickerField : datePickerFields) {
                        if ("mDayPicker".equals(datePickerField.getName()) || "mDaySpinner".equals(datePickerField.getName()) || "mDelegate".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            ((View) datePickerField.get(datePicker)).setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {

                }
                dialog.show();
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        expiryMonth = datePicker.getMonth() + 1;
                        expiryYear = datePicker.getYear();
                        ((EditText) cardDetails.findViewById(R.id.expiryDatePickerEditText)).setText("" + expiryMonth + " / " + expiryYear);
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
                        dialog.dismiss();
                    }
                });
            }
        });

        /* store card */
        if (getActivity().getIntent().getExtras().getString(PayU.USER_CREDENTIALS) != null) {
            cardDetails.findViewById(R.id.storeCardCheckBox).setVisibility(View.VISIBLE);
        }
        // this comes form stored card fragment
        if (getArguments().getString(PayU.STORE_CARD) != null) {
            cardDetails.findViewById(R.id.storeCardCheckBox).setVisibility(View.VISIBLE);
            ((CheckBox) cardDetails.findViewById(R.id.storeCardCheckBox)).setChecked(true);
            cardDetails.findViewById(R.id.cardNameEditText).setVisibility(View.VISIBLE);
        }

        cardDetails.findViewById(R.id.storeCardCheckBox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    getArguments().putString(PayU.STORE_CARD, PayU.STORE_CARD);
                    cardDetails.findViewById(R.id.cardNameEditText).setVisibility(View.VISIBLE);
                } else {
                    getArguments().remove(PayU.STORE_CARD);
                    cardDetails.findViewById(R.id.cardNameEditText).setVisibility(View.GONE);
                }
            }
        });

        cardDetails.findViewById(R.id.makePayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Params requiredParams = new Params();

                String nameOnCard = ((TextView) cardDetails.findViewById(R.id.nameOnCardEditText)).getText().toString();
                if (nameOnCard.length() < 3) {
                    nameOnCard = "PayU " + nameOnCard;
                }

                requiredParams.put(PayU.CARD_NUMBER, cardNumber);
                requiredParams.put(PayU.EXPIRY_MONTH, String.valueOf(expiryMonth));
                requiredParams.put(PayU.EXPIRY_YEAR, String.valueOf(expiryYear));
                requiredParams.put(PayU.CARDHOLDER_NAME, nameOnCard);
                requiredParams.put(PayU.CVV, cvv);

                if (getArguments().getString(PayU.STORE_CARD) != null) { // this comes from the stored card fragment.
                    requiredParams.put("card_name", cardName);
                    requiredParams.put(PayU.STORE_CARD, "1");
                    startPaymentProcessActivity(PayU.PaymentMode.CC, requiredParams);
                } else if (getArguments().getString("rewardPoint") != null) { // this comes from cash card fragment..citi reward
                    requiredParams.put(PayU.BANKCODE, getArguments().getString("rewardPoint"));
                    startPaymentProcessActivity(PayU.PaymentMode.CASH, requiredParams);
                } else {
                    startPaymentProcessActivity(PayU.PaymentMode.CC, requiredParams);
                }
            }
        });
        return cardDetails;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nameOnCardDrawable = getResources().getDrawable(R.drawable.user);
        cardNumberDrawable = getResources().getDrawable(R.drawable.card);
        calenderDrawable = getResources().getDrawable(R.drawable.calendar);
        cvvDrawable = getResources().getDrawable(R.drawable.lock);
        cardNameDrawable = getResources().getDrawable(R.drawable.user);

        cardNumberDrawable.setAlpha(100);
        calenderDrawable.setAlpha(100);
        cvvDrawable.setAlpha(100);


        ((EditText) getActivity().findViewById(R.id.nameOnCardEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, nameOnCardDrawable, null);
        ((EditText) getActivity().findViewById(R.id.cardNumberEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, cardNumberDrawable, null);
        ((EditText) getActivity().findViewById(R.id.expiryDatePickerEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, calenderDrawable, null);
        ((EditText) getActivity().findViewById(R.id.cvvEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, cvvDrawable, null);
        ((EditText) getActivity().findViewById(R.id.cardNameEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, cardNameDrawable, null);


        // lets get the data from bundle
        if(savedInstanceState != null){
            expiryMonth = savedInstanceState.getInt("ccexpmon");
            expiryYear = savedInstanceState.getInt("ccexpyr");

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


        ((EditText) getActivity().findViewById(R.id.cardNameEditText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                cardName = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        ((EditText) getActivity().findViewById(R.id.cardNumberEditText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ((EditText)getActivity().findViewById(R.id.cvvEditText)).getText().clear();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                cardNumber = charSequence.toString();

                issuer = Cards.getIssuer(cardNumber);

                if(issuer.contentEquals("AMEX")){
                    ((EditText) getActivity().findViewById(R.id.cvvEditText)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                }else{
                    ((EditText) getActivity().findViewById(R.id.cvvEditText)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
                }
                if(issuer != null){
                    issuerDrawable = Cards.ISSUER_DRAWABLE.get(issuer);
                }

                if (issuer.contentEquals("SMAE")) {
                    // disable cvv and expiry
                    getActivity().findViewById(R.id.expiryCvvLinearLayout).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.haveCvvExpiryLinearLayout).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.dontHaveCvvExpiryLinearLayout).setVisibility(View.GONE);
                    if (Cards.validateCardNumber(cardNumber)) {
                        isCardNumberValid = true;
                        if(PayU.issuingBankDownBin != null && PayU.issuingBankDownBin.has(cardNumber.substring(0, 6))){// oops bank is down.
                            try {
                                ((TextView)getActivity().findViewById(R.id.issuerDownTextView)).setText(PayU.issuingBankDownBin.getString(cardNumber.substring(0,6)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            getActivity().findViewById(R.id.issuerDownTextView).setVisibility(View.VISIBLE);
                        }else{
                            getActivity().findViewById(R.id.issuerDownTextView).setVisibility(View.GONE);
                        }
                        if (getActivity().getIntent().getExtras().getString(PayU.OFFER_KEY) != null)
                            checkOffer(cardNumber, getActivity().getIntent().getExtras().getString(PayU.OFFER_KEY));
                        valid(((EditText) getActivity().findViewById(R.id.cardNumberEditText)), issuerDrawable);
                    } else {
                        isCardNumberValid = false;
                        invalid(((EditText) getActivity().findViewById(R.id.cardNumberEditText)), cardNumberDrawable);
                        cardNumberDrawable.setAlpha(100);
                        resetHeader();
                    }
                } else {
                    // enable cvv and expiry
                    getActivity().findViewById(R.id.expiryCvvLinearLayout).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.haveCvvExpiryLinearLayout).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.dontHaveCvvExpiryLinearLayout).setVisibility(View.GONE);
                    if (Cards.validateCardNumber(cardNumber)) {

                        isCardNumberValid = true;

                        if(PayU.issuingBankDownBin != null && PayU.issuingBankDownBin.has(cardNumber.substring(0, 6))){// oops bank is down.
                            try {
                                ((TextView)getActivity().findViewById(R.id.issuerDownTextView)).setText("We are experiencing high failure rate for "+PayU.issuingBankDownBin.getString(cardNumber.substring(0,6)) + " cards at this time. We recommend you pay using any other means of payment.");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            getActivity().findViewById(R.id.issuerDownTextView).setVisibility(View.VISIBLE);
                        }else{
                            getActivity().findViewById(R.id.issuerDownTextView).setVisibility(View.GONE);
                        }

                        if (getActivity().getIntent().getExtras().getString(PayU.OFFER_KEY) != null)
                            checkOffer(cardNumber, getActivity().getIntent().getExtras().getString(PayU.OFFER_KEY));
                        valid(((EditText) getActivity().findViewById(R.id.cardNumberEditText)), issuerDrawable);
                    } else {
                        isCardNumberValid = false;
                        invalid(((EditText) getActivity().findViewById(R.id.cardNumberEditText)), cardNumberDrawable);
                        cardNumberDrawable.setAlpha(100);
                        resetHeader();
                    }
                }

                // lets set the issuer drawable.

                if(issuer != null && issuerDrawable != null){
                    ((EditText)getActivity().findViewById(R.id.cardNumberEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, issuerDrawable, null);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ((EditText) getActivity().findViewById(R.id.cvvEditText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                cvv = charSequence.toString();
                if(Cards.validateCvv(cardNumber, cvv)){
                    isCvvValid = true;
                    valid(((EditText) getActivity().findViewById(R.id.cvvEditText)), cvvDrawable);
                }else{
                    isCvvValid = false;
                    invalid(((EditText) getActivity().findViewById(R.id.cvvEditText)), cvvDrawable);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }


        });
        getActivity().findViewById(R.id.haveClickHereTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().findViewById(R.id.expiryCvvLinearLayout).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.haveCvvExpiryLinearLayout).setVisibility(View.GONE);
                getActivity().findViewById(R.id.dontHaveCvvExpiryLinearLayout).setVisibility(View.VISIBLE);
                isCvvValid = false;
                isExpired = true;
                ((EditText) getActivity().findViewById(R.id.expiryDatePickerEditText)).getText().clear();
                ((EditText) getActivity().findViewById(R.id.cvvEditText)).getText().clear();
                invalid(((EditText) getActivity().findViewById(R.id.cvvEditText)), cvvDrawable);

            }
        });

        getActivity().findViewById(R.id.dontHaveClickHereTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().findViewById(R.id.expiryCvvLinearLayout).setVisibility(View.GONE);
                getActivity().findViewById(R.id.haveCvvExpiryLinearLayout).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.dontHaveCvvExpiryLinearLayout).setVisibility(View.GONE);
                valid(((EditText) getActivity().findViewById(R.id.cvvEditText)), cvvDrawable);
            }
        });

        getActivity().findViewById(R.id.cardNumberEditText).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    makeInvalid();
                }
            }
        });
        getActivity().findViewById(R.id.nameOnCardEditText).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    makeInvalid();
                }
            }
        });

        getActivity().findViewById(R.id.cvvEditText).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    makeInvalid();
                }
            }
        });

        getActivity().findViewById(R.id.expiryDatePickerEditText).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    makeInvalid();
                }
            }
        });

    }

    private void valid(EditText editText, Drawable drawable) {
        drawable.setAlpha(255);
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        if (getActivity().findViewById(R.id.expiryCvvLinearLayout).getVisibility() == View.GONE) {
            isExpired = false;
            isCvvValid = true;
        }
        if (isCardNumberValid && !isExpired && isCvvValid ) {
            getActivity().findViewById(R.id.makePayment).setEnabled(true);
//            getActivity().findViewById(R.id.makePayment).setBackgroundResource(R.drawable.button_enabled);
        } else {
            getActivity().findViewById(R.id.makePayment).setEnabled(false);
//            getActivity().findViewById(R.id.makePayment).setBackgroundResource(R.drawable.button);
        }
    }

    private void invalid(EditText editText, Drawable drawable) {
        drawable.setAlpha(100);
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        getActivity().findViewById(R.id.makePayment).setEnabled(false);
        getActivity().findViewById(R.id.makePayment).setBackgroundResource(R.drawable.button);
    }

    private void makeInvalid() {
        if (!isCardNumberValid && cardNumber.length() > 0 && !getActivity().findViewById(R.id.cardNumberEditText).isFocused())
            ((EditText) getActivity().findViewById(R.id.cardNumberEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.error_icon), null);
        if (!isCvvValid && cvv.length() > 0 && !getActivity().findViewById(R.id.cvvEditText).isFocused())
            ((EditText) getActivity().findViewById(R.id.cvvEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.error_icon), null);
    }

    private void checkOffer(String cardNumber, String offerKey) {
        List<NameValuePair> postParams = null;

        HashMap<String, String> varList = new HashMap<String, String>();

        // offer key
        varList.put(PayU.VAR1, offerKey);
        // amount
        varList.put(PayU.VAR2, "" + getActivity().getIntent().getExtras().getDouble(PayU.AMOUNT));
        // category
        varList.put(PayU.VAR3, "CC");
        // bank code
        varList.put(PayU.VAR4, cardNumber.startsWith("4") ? "VISA" : "MAST");
        //  card number
        varList.put(PayU.VAR5, cardNumber);
        // name on card
        varList.put(PayU.VAR6, nameOnCard);
        // phone number
        varList.put(PayU.VAR7, "");
        // email id
        varList.put(PayU.VAR8, "");

        try {
            postParams = PayU.getInstance(getActivity()).getParams(Constants.OFFER_STATUS, varList);
            GetResponseTask getOfferStatus = new GetResponseTask(this);
            getOfferStatus.execute(postParams);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentOptionSelected(PayU.PaymentMode paymentMode) {

    }

    @Override
    public void onGetResponse(String responseMessage) {

        if(responseMessage.startsWith("Error:")){
            Intent intent = new Intent();
            intent.putExtra("result", responseMessage);
            getActivity().setResult(Activity.RESULT_CANCELED, intent);
            getActivity().finish();
        }

        try {
            if (PayU.offerStatus != null && PayU.offerStatus.getJSONObject(0).getInt("status") == 1 && getActivity().findViewById(R.id.offerMessageTextView) != null) {

                ((TextView) getActivity().findViewById(R.id.offerMessageTextView)).setText(getString(R.string.eligible_for_discount, PayU.offerStatus.getJSONObject(0).getDouble("discount")));
                // change amount
                ((TextView) getActivity().findViewById(R.id.amountTextView)).setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ((TextView) getActivity().findViewById(R.id.amountTextView)).setGravity(Gravity.RIGHT);
                ((TextView) getActivity().findViewById(R.id.amountTextView)).setTextColor(Color.GRAY);
                getActivity().findViewById(R.id.offerAmountTextView).setVisibility(View.VISIBLE);
                ((TextView) getActivity().findViewById(R.id.offerAmountTextView)).setText(getString(R.string.amount, getActivity().getIntent().getExtras().getDouble(PayU.AMOUNT) - PayU.offerStatus.getJSONObject(0).getDouble("discount")));
                ((TextView) getActivity().findViewById(R.id.amountTextView)).setText(String.valueOf(getActivity().getIntent().getExtras().getDouble(PayU.AMOUNT) - PayU.offerStatus.getJSONObject(0).getDouble("discount")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void resetHeader() {
        if (getActivity().findViewById(R.id.offerMessageTextView) != null && getActivity().findViewById(R.id.offerMessageTextView).getVisibility() == View.VISIBLE) {
            getActivity().findViewById(R.id.offerAmountTextView).setVisibility(View.GONE);
            ((TextView) getActivity().findViewById(R.id.offerMessageTextView)).setText("");
            ((TextView) getActivity().findViewById(R.id.amountTextView)).setGravity(Gravity.CENTER);
            ((TextView) getActivity().findViewById(R.id.amountTextView)).setTextColor(Color.BLACK);
            ((TextView) getActivity().findViewById(R.id.amountTextView)).setPaintFlags(0);
            ((TextView) getActivity().findViewById(R.id.amountTextView)).setText(getString(R.string.amount, getActivity().getIntent().getExtras().getDouble(PayU.AMOUNT)));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // lets put the expiry month and expiry year here.

        outState.putInt("ccexpmon", expiryMonth);
        outState.putInt("ccexpyr", expiryYear);
    }
}
