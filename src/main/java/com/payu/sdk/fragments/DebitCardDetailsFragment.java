package com.payu.sdk.fragments;


import android.app.DatePickerDialog;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.payu.sdk.Constants;
import com.payu.sdk.Luhn;
import com.payu.sdk.Params;
import com.payu.sdk.PayU;
import com.payu.sdk.Payment;
import com.payu.sdk.PaymentListener;
import com.payu.sdk.ProcessPaymentActivity;
import com.payu.sdk.R;
import com.payu.sdk.SetupCardDetails;
import com.payu.sdk.StoreCardTask;
import com.payu.sdk.exceptions.HashException;
import com.payu.sdk.exceptions.MissingParameterException;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DebitCardDetailsFragment extends Fragment implements PaymentListener {

    Payment payment;
    Params requiredParams = new Params();

    Payment.Builder builder = new Payment().new Builder();

    private int expiryMonth;
    private int expiryYear;
    private String cardNumber = "";
    private String cvv = "";
    private String nameOnCard = "";
    private String cardName = "";

    DatePickerDialog.OnDateSetListener mDateSetListener;
    int mYear;
    int mMonth;
    int mDay;

    Boolean isNameOnCardValid = false;
    Boolean isCardNumberValid = false;
    Boolean isExpired = true;
    Boolean isCvvValid = false;

    Drawable nameOnCardDrawable;
    Drawable cardNumberDrawable;
    Drawable calenderDrawable;
    Drawable cvvDrawable;
    Drawable cardNameDrawable;

    public DebitCardDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View debitCardDetails = inflater.inflate(R.layout.fragment_debit_card_details, container, false);

        mYear = Calendar.getInstance().get(Calendar.YEAR);
        mMonth = Calendar.getInstance().get(Calendar.MONTH);
        mDay = Calendar.getInstance().get(Calendar.DATE);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                ((TextView) debitCardDetails.findViewById(R.id.expiryDatePickerEditText)).setText("" + (i2 + 1) + " / " + i);

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

        debitCardDetails.findViewById(R.id.expiryDatePickerEditText).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    SetupCardDetails.customDatePicker(getActivity(), mDateSetListener, mYear, mMonth, mDay).show();
                }
                return false;
            }
        });

        /* store card */
        if(getActivity().getIntent().getExtras().getString(PayU.USER_CREDENTIALS) != null){
            debitCardDetails.findViewById(R.id.storeCardCheckBox).setVisibility(View.VISIBLE);
        }
        // this comes form stored card fragment
        if(getArguments().getString(PayU.STORE_CARD) != null){
            debitCardDetails.findViewById(R.id.storeCardCheckBox).setVisibility(View.VISIBLE);
            ((CheckBox)debitCardDetails.findViewById(R.id.storeCardCheckBox)).setChecked(true);
            debitCardDetails.findViewById(R.id.cardNameEditText).setVisibility(View.VISIBLE);
        }

        debitCardDetails.findViewById(R.id.storeCardCheckBox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CheckBox) view).isChecked()){
                    getArguments().putString(PayU.STORE_CARD, PayU.STORE_CARD);
                    debitCardDetails.findViewById(R.id.cardNameEditText).setVisibility(View.VISIBLE);
                }else{
                    getArguments().remove(PayU.STORE_CARD);
                    debitCardDetails.findViewById(R.id.cardNameEditText).setVisibility(View.GONE);
                }

            }
        });

        debitCardDetails.findViewById(R.id.makePayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cardNumber = ((TextView) debitCardDetails.findViewById(R.id.cardNumberEditText)).getText().toString();

                try {

                    builder.setMode(PayU.PaymentMode.DC);
                    builder.setAmount(getActivity().getIntent().getExtras().getDouble(PayU.AMOUNT));
                    builder.setProductInfo(getActivity().getIntent().getExtras().getString(PayU.PRODUCT_INFO));
                    builder.setTxnId(getActivity().getIntent().getExtras().getString(PayU.TXNID));
                    builder.setSurl(getActivity().getIntent().getExtras().getString(PayU.SURL));
                    payment = builder.create();

                    requiredParams.put(PayU.CARD_NUMBER, cardNumber);
                    requiredParams.put(PayU.EXPIRY_MONTH, String.valueOf(expiryMonth));
                    requiredParams.put(PayU.EXPIRY_YEAR, String.valueOf(expiryYear));
                    requiredParams.put(PayU.CARDHOLDER_NAME, ((TextView) debitCardDetails.findViewById(R.id.nameOnCardEditText)).getText().toString());
                    requiredParams.put(PayU.CVV, ((TextView) debitCardDetails.findViewById(R.id.cvvEditText)).getText().toString());

                    /*payment params*/
                    requiredParams.put(PayU.TXNID, payment.getTxnId());
                    requiredParams.put(PayU.AMOUNT, String.valueOf(payment.getAmount()));
                    requiredParams.put(PayU.PRODUCT_INFO, payment.getProductInfo());
                    requiredParams.put(PayU.SURL, payment.getSurl());

                    if (getArguments().getString(PayU.STORE_CARD) != null) {
                        requiredParams.put("card_name", cardName);
                        requiredParams.put(PayU.STORE_CARD, "1");
                        requiredParams.put("user_credentials", getActivity().getIntent().getExtras().getString(PayU.USER_CREDENTIALS));
                    }

                    /*additional data*/
                    if (getActivity().getIntent().getExtras().getString(PayU.FIRSTNAME) != null)
                        requiredParams.put(PayU.FIRSTNAME, getActivity().getIntent().getExtras().getString(PayU.FIRSTNAME));
                    if (getActivity().getIntent().getExtras().getString(PayU.EMAIL) != null)
                        requiredParams.put(PayU.EMAIL, getActivity().getIntent().getExtras().getString(PayU.EMAIL));

                    /* offer */
                    if (getActivity().getIntent().getExtras().getString(PayU.OFFER_KEY) != null) {
                        requiredParams.put(PayU.OFFER_KEY, getActivity().getIntent().getExtras().getString(PayU.OFFER_KEY));
                    }

                    if (getActivity().getIntent().getExtras().getString(PayU.DROP_CATEGORY) != null) {
                        requiredParams.put(PayU.DROP_CATEGORY, getActivity().getIntent().getExtras().getString(PayU.DROP_CATEGORY));
                    }

                    if (getActivity().getIntent().getExtras().getString(PayU.ENFORCE_PAYMETHOD) != null) {
                        requiredParams.put(PayU.ENFORCE_PAYMETHOD, getActivity().getIntent().getExtras().getString(PayU.ENFORCE_PAYMETHOD));
                    }

                    // get the parameters required
                    String postData = PayU.getInstance(getActivity()).createPayment(payment, requiredParams);
                    // we get post data and url here,,,we launch ProcessPaymentActivity from here..
//
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

        return debitCardDetails;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nameOnCardDrawable = getResources().getDrawable(R.drawable.user);
        cardNumberDrawable = getResources().getDrawable(R.drawable.card);
        calenderDrawable = getResources().getDrawable(R.drawable.calendar);
        cvvDrawable = getResources().getDrawable(R.drawable.lock);
        cardNameDrawable = getResources().getDrawable(R.drawable.user);

        nameOnCardDrawable.setAlpha(100);
        cardNumberDrawable.setAlpha(100);
        calenderDrawable.setAlpha(100);
        cvvDrawable.setAlpha(100);

        ((TextView)getActivity().findViewById(R.id.enterCardDetailsTextView)).setText(getString(R.string.enter_debit_card_details));

        ((EditText) getActivity().findViewById(R.id.nameOnCardEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, nameOnCardDrawable, null);
        ((EditText) getActivity().findViewById(R.id.cardNumberEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, cardNumberDrawable, null);
        ((EditText) getActivity().findViewById(R.id.expiryDatePickerEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, calenderDrawable, null);
        ((EditText) getActivity().findViewById(R.id.cvvEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, cvvDrawable, null);
        ((EditText) getActivity().findViewById(R.id.cardNameEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, cardNameDrawable, null);

        ((EditText)getActivity().findViewById(R.id.cardNameEditText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                cardName = ((EditText) getActivity().findViewById(R.id.cardNameEditText)).getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


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

                if (cardNumber.startsWith("34") || cardNumber.startsWith("37"))
                    ((EditText) getActivity().findViewById(R.id.cvvEditText)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                else
                    ((EditText) getActivity().findViewById(R.id.cvvEditText)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});


                if (SetupCardDetails.findIssuer(cardNumber, "DC") == "MAES") {
                    // disable cvv and expiry
                    getActivity().findViewById(R.id.expiryCvvLinearLayout).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.haveCvvExpiryLinearLayout).setVisibility(View.VISIBLE);
                    getActivity().findViewById(R.id.dontHaveCvvExpiryLinearLayout).setVisibility(View.GONE);
                    if (cardNumber.length() > 11 && Luhn.validate(cardNumber)) {
                        isCardNumberValid = true;
                        if (getActivity().getIntent().getExtras().getString(PayU.OFFER_KEY) != null)
                            checkOffer(cardNumber, getActivity().getIntent().getExtras().getString(PayU.OFFER_KEY));
                        valid(((EditText) getActivity().findViewById(R.id.cardNumberEditText)), SetupCardDetails.getCardDrawable(getResources(), cardNumber));
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
                    if (cardNumber.length() > 11 && Luhn.validate(cardNumber)) {
                        isCardNumberValid = true;
                        if (getActivity().getIntent().getExtras().getString(PayU.OFFER_KEY) != null)
                            checkOffer(cardNumber, getActivity().getIntent().getExtras().getString(PayU.OFFER_KEY));
                        valid(((EditText) getActivity().findViewById(R.id.cardNumberEditText)), SetupCardDetails.getCardDrawable(getResources(), cardNumber));
                    } else {
                        isCardNumberValid = false;
                        invalid(((EditText) getActivity().findViewById(R.id.cardNumberEditText)), cardNumberDrawable);
                        cardNumberDrawable.setAlpha(100);
                        resetHeader();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ((EditText) getActivity().findViewById(R.id.cvvEditText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                cvv = ((EditText) getActivity().findViewById(R.id.cvvEditText)).getText().toString();
                if (cardNumber.startsWith("34") || cardNumber.startsWith("37")) {
                    if (cvv.length() == 4) {
                        //valid
                        isCvvValid = true;
                        valid(((EditText) getActivity().findViewById(R.id.cvvEditText)), cvvDrawable);

                    } else {
                        //invalid
                        isCvvValid = false;
                        invalid(((EditText) getActivity().findViewById(R.id.cvvEditText)), cvvDrawable);
                    }
                } else {
                    if (cvv.length() == 3) {
                        //valid
                        isCvvValid = true;
                        valid(((EditText) getActivity().findViewById(R.id.cvvEditText)), cvvDrawable);
                    } else {
                        //invalid
                        isCvvValid = false;
                        invalid(((EditText) getActivity().findViewById(R.id.cvvEditText)), cvvDrawable);
                    }
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
        if (isCardNumberValid && !isExpired && isCvvValid && isNameOnCardValid) {
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
        if (!isNameOnCardValid && nameOnCard.length() > 0 && !getActivity().findViewById(R.id.nameOnCardEditText).isFocused())
            ((EditText) getActivity().findViewById(R.id.nameOnCardEditText)).setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.error_icon), null);
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
            postParams = PayU.getInstance(getActivity()).getParams("check_offer_status", varList);
            StoreCardTask getStoredCards = new StoreCardTask(this);
            getStoredCards.execute(postParams);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentOptionSelected(PayU.PaymentMode paymentMode) {

    }

    @Override
    public void onGetAvailableBanks(JSONArray response) {

    }

    @Override
    public void onGetStoreCardDetails(JSONArray response) {
        try {
            if (response.getJSONObject(0).getInt("status") == 1 && getActivity().findViewById(R.id.offerMessageTextView) != null) {

                ((TextView) getActivity().findViewById(R.id.offerMessageTextView)).setText(getString(R.string.eligible_for_discount, response.getJSONObject(0).getDouble("discount")));
                // change amount
                ((TextView) getActivity().findViewById(R.id.amountTextView)).setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                ((TextView) getActivity().findViewById(R.id.amountTextView)).setGravity(Gravity.RIGHT);
                ((TextView) getActivity().findViewById(R.id.amountTextView)).setTextColor(Color.GRAY);
                getActivity().findViewById(R.id.offerAmountTextView).setVisibility(View.VISIBLE);
                ((TextView) getActivity().findViewById(R.id.offerAmountTextView)).setText(getString(R.string.amount, getActivity().getIntent().getExtras().getDouble(PayU.AMOUNT) - response.getJSONObject(0).getDouble("discount")));
                ((TextView) getActivity().findViewById(R.id.amountTextView)).setText(String.valueOf(getActivity().getIntent().getExtras().getDouble(PayU.AMOUNT) - response.getJSONObject(0).getDouble("discount")));
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
//                        ((TextView) getActivity().findViewById(R.id.amountTextView)).setPaintFlags(((TextView) getActivity().findViewById(R.id.amountTextView)).getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            ((TextView) getActivity().findViewById(R.id.amountTextView)).setPaintFlags(0);
            ((TextView) getActivity().findViewById(R.id.amountTextView)).setText(getString(R.string.amount, getActivity().getIntent().getExtras().getDouble(PayU.AMOUNT)));
        }
    }

}
