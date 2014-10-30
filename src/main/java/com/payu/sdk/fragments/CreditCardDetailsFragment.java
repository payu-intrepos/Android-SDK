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
import org.w3c.dom.Text;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreditCardDetailsFragment extends Fragment implements PaymentListener {
    Payment payment;
    Params requiredParams = new Params();

    Payment.Builder builder = new Payment().new Builder();

    private int expiryMonth = 5;
    private int expiryYear = 2015;
    private String cardNumber = "";
    private String cvv = "";
    private String nameOnCard = "";
    private String issuer = null;

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

    public CreditCardDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View creditCardDetails = inflater.inflate(R.layout.fragment_credit_card_details, container, false);

        mYear = Calendar.getInstance().get(Calendar.YEAR);
        mMonth = Calendar.getInstance().get(Calendar.MONTH);
        mDay = Calendar.getInstance().get(Calendar.DATE);


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                ((TextView) creditCardDetails.findViewById(R.id.expiryDatePickerEditText)).setText("" + (i2 + 1) + " / " + i);

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

        calenderDrawable = getResources().getDrawable(R.drawable.calendar);

        creditCardDetails.findViewById(R.id.expiryDatePickerEditText).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    SetupCardDetails.customDatePicker(getActivity(), mDateSetListener, mYear, mMonth, mDay).show();
                }
                return false;
            }
        });

        creditCardDetails.findViewById(R.id.makePayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    builder.setMode(PayU.PaymentMode.CC);
                    builder.setAmount(getActivity().getIntent().getExtras().getDouble(PayU.AMOUNT));
                    builder.setProductInfo(getActivity().getIntent().getExtras().getString(PayU.PRODUCT_INFO));
                    builder.setTxnId(getActivity().getIntent().getExtras().getString(PayU.TXNID));
                    builder.setSurl(getActivity().getIntent().getExtras().getString(PayU.SURL));
                    payment = builder.create();

                    requiredParams.put(PayU.CARD_NUMBER, cardNumber);
                    requiredParams.put(PayU.EXPIRY_MONTH, String.valueOf(expiryMonth));
                    requiredParams.put(PayU.EXPIRY_YEAR, String.valueOf(expiryYear));
                    requiredParams.put(PayU.CARDHOLDER_NAME, nameOnCard);
                    requiredParams.put(PayU.CVV, cvv);

                    /*payment params*/
                    requiredParams.put(PayU.TXNID, payment.getTxnId());
                    requiredParams.put(PayU.AMOUNT, String.valueOf(payment.getAmount()));
                    requiredParams.put(PayU.PRODUCT_INFO, payment.getProductInfo());
                    requiredParams.put(PayU.SURL, payment.getSurl());

                    if (getArguments().getString(PayU.STORE_CARD) != null) {
                        requiredParams.put("card_name", nameOnCard);
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

                    String postData = PayU.getInstance(getActivity()).createPayment(payment, requiredParams);

                    Intent intent = new Intent(getActivity(), ProcessPaymentActivity.class);
                    intent.putExtra(Constants.POST_DATA, postData);

                    //disable back button
                    if(getActivity().getIntent().getExtras().getString(PayU.DISABLE_PAYMENT_PROCESS_BACK_BUTTON) != null)
                        intent.putExtra(PayU.DISABLE_PAYMENT_PROCESS_BACK_BUTTON, getActivity().getIntent().getExtras().getString(PayU.DISABLE_PAYMENT_PROCESS_BACK_BUTTON));

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivityForResult(intent, PayU.RESULT);

                } catch (MissingParameterException e) {
                    e.printStackTrace();
                } catch (HashException e) {
                    e.printStackTrace();
                }
            }
        });

        return creditCardDetails;
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

        ((TextView)getActivity().findViewById(R.id.enterCardDetailsTextView)).setText(getString(R.string.enter_credit_card_details));

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

                if (cardNumber.startsWith("34") || cardNumber.startsWith("37"))
                    ((EditText) getActivity().findViewById(R.id.cvvEditText)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                else
                    ((EditText) getActivity().findViewById(R.id.cvvEditText)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});

                if (cardNumber.length() > 11 && Luhn.validate(cardNumber)) {
                    // valid name on card
                    isCardNumberValid = true;
                    if (getActivity().getIntent().getExtras().getString(PayU.OFFER_KEY) != null)
                        checkOffer(cardNumber, getActivity().getIntent().getExtras().getString(PayU.OFFER_KEY));
                    valid(((EditText) getActivity().findViewById(R.id.cardNumberEditText)), SetupCardDetails.getCardDrawable(getResources(), cardNumber));
                } else {
                    issuer = null;
                    isCardNumberValid = false;
                    invalid(((EditText) getActivity().findViewById(R.id.cardNumberEditText)), cardNumberDrawable);
                    cardNumberDrawable.setAlpha(100);

                    // redo the header changes
                    if (getActivity().findViewById(R.id.offerMessageTextView) != null && getActivity().findViewById(R.id.offerMessageTextView).getVisibility() == View.VISIBLE) {
                        getActivity().findViewById(R.id.offerAmountTextView).setVisibility(View.GONE);
                        ((TextView) getActivity().findViewById(R.id.offerMessageTextView)).setText("");
                        TextView amount = (TextView) getActivity().findViewById(R.id.amountTextView);
                        amount.setGravity(Gravity.CENTER);
                        amount.setTextColor(Color.BLACK);
//                        ((TextView) getActivity().findViewById(R.id.amountTextView)).setPaintFlags(((TextView) getActivity().findViewById(R.id.amountTextView)).getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                        amount.setPaintFlags(0);
                        amount.setText(getString(R.string.amount, getActivity().getIntent().getExtras().getDouble(PayU.AMOUNT)));
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


    private void valid(EditText editText, Drawable drawable) {
        drawable.setAlpha(255);
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        if (getActivity().findViewById(R.id.expiryCvvLinearLayout).getVisibility() == View.GONE) {
            isExpired = false;
            isCvvValid = true;
        } else {

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
        varList.put(PayU.VAR4, "CC");
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
                // display the offer message;
                ((TextView) getActivity().findViewById(R.id.offerMessageTextView)).setText(getString(R.string.eligible_for_discount, response.getJSONObject(0).getDouble("discount")));
                // change amount
                TextView amount = (TextView) getActivity().findViewById(R.id.amountTextView);
                amount.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                amount.setGravity(Gravity.RIGHT);
                amount.setTextColor(Color.GRAY);
                getActivity().findViewById(R.id.offerAmountTextView).setVisibility(View.VISIBLE);
                ((TextView) getActivity().findViewById(R.id.offerAmountTextView)).setText(getString(R.string.amount, getActivity().getIntent().getExtras().getDouble(PayU.AMOUNT) - response.getJSONObject(0).getDouble("discount")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
