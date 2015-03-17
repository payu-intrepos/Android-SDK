package com.payu.sdk.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
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
import android.widget.Toast;

import com.payu.sdk.Cards;
import com.payu.sdk.Constants;
import com.payu.sdk.GetResponseTask;
import com.payu.sdk.Params;
import com.payu.sdk.PayU;
import com.payu.sdk.PaymentListener;
import com.payu.sdk.R;
import com.payu.sdk.adapters.EmiBankListAdapter;
import com.payu.sdk.adapters.EmiTimeIntervalAdapter;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmiDetailsFragment extends ProcessPaymentFragment implements PaymentListener{

    Spinner bankListSpinner;

    Spinner emiTimeIntervalSpinner;
    String bankCode;
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
    private int expiryMonth;
    private int expiryYear;
    private String cardNumber = "";
    private String cvv = "";
    private String nameOnCard = "";
    private String issuer;

    ProgressDialog mProgressDialog;


    public EmiDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View emi_fragment = inflater.inflate(R.layout.fragment_emi_details, container, false);

        mProgressDialog = new ProgressDialog(getActivity());

        Cards.initializeIssuers(getResources());

        if(PayU.availableEmi == null ){// lets fetch the cash cards.
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
                GetResponseTask getResponse = new GetResponseTask(EmiDetailsFragment.this);
                getResponse.execute(postParams);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        if(Constants.ENABLE_VAS && PayU.issuingBankDownBin == null){ // vas has not been called, lets fetch bank down time.
            HashMap<String, String> varList = new HashMap<String, String>();

            varList.put("var1", "default");
            varList.put("var2", "default");
            varList.put("var3", "default");

            List<NameValuePair> postParams = null;

            try {
                postParams = PayU.getInstance(getActivity()).getParams(Constants.GET_VAS, varList);
                GetResponseTask getResponse = new GetResponseTask(EmiDetailsFragment.this);
                getResponse.execute(postParams);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        mYear = Calendar.getInstance().get(Calendar.YEAR);
        mMonth = Calendar.getInstance().get(Calendar.MONTH);
        mDay = Calendar.getInstance().get(Calendar.DATE);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                ((TextView) emi_fragment.findViewById(R.id.expiryDatePickerEditText)).setText("" + (i2 + 1) + " / " + i);
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

        emi_fragment.findViewById(R.id.expiryDatePickerEditText).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP)
                    Cards.customDatePicker(getActivity(), mDateSetListener, mYear, mMonth, mDay).show();
                return false;
            }
        });

        emi_fragment.findViewById(R.id.makePayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Params requiredParams = new Params();

                String cardNumber = ((TextView) emi_fragment.findViewById(R.id.cardNumberEditText)).getText().toString();


                requiredParams.put(PayU.CARD_NUMBER, cardNumber);
                requiredParams.put(PayU.EXPIRY_MONTH, String.valueOf(expiryMonth));
                requiredParams.put(PayU.EXPIRY_YEAR, String.valueOf(expiryYear));
                requiredParams.put(PayU.CARDHOLDER_NAME, nameOnCard);
                requiredParams.put(PayU.CVV, cvv);


                requiredParams.put(PayU.BANKCODE, bankCode);
                startPaymentProcessActivity(PayU.PaymentMode.EMI, requiredParams);


            }
        });

        return emi_fragment;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(PayU.availableEmi == null){// gotta fetch data

        }else{
            setupAdapter();
        }

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
                nameOnCard = charSequence.toString();
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
                cardNumber = charSequence.toString();

                issuer = Cards.getIssuer(cardNumber);

                if (issuer.contentEquals("AMEX"))
                    ((EditText) getActivity().findViewById(R.id.cvvEditText)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                else
                    ((EditText) getActivity().findViewById(R.id.cvvEditText)).setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});


                if (Cards.validateCardNumber(cardNumber)) {
                    // valid name on card
                    isCardNumberValid = true;
                    valid(((EditText) getActivity().findViewById(R.id.cardNumberEditText)), Cards.ISSUER_DRAWABLE.get(issuer));
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

        ((EditText) getActivity().findViewById(R.id.cvvEditText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

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
        if(drawable != null) // for bank code
            drawable.setAlpha(255);
        if(editText != null) // for bank code
            editText.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        if (isCardNumberValid && !isExpired && isCvvValid && isNameOnCardValid && !bankCode.contentEquals("default") ) {
            getActivity().findViewById(R.id.makePayment).setEnabled(true);
//            getActivity().findViewById(R.id.makePayment).setBackgroundResource(R.drawable.button_enabled);
        } else {
            getActivity().findViewById(R.id.makePayment).setEnabled(false);
//            getActivity().findViewById(R.id.makePayment).setBackgroundResource(R.drawable.button);
        }
    }

    private void invalid(EditText editText, Drawable drawable) {
        if(drawable != null) // for bank code
            drawable.setAlpha(100);
        if(editText != null) // for bank code
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

    private JSONArray jsonArraySort(JSONArray jsonArray, final String key) throws JSONException {

        // sort the available banks
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getJSONObject(i));
        }

        Collections.sort(list, new Comparator<JSONObject>() {
            //You can change "Name" with "ID" if you want to sort by ID

            @Override
            public int compare(JSONObject a, JSONObject b) {
                try {
                    if (Integer.parseInt(a.getString(key).replaceAll("\\D+", "")) > Integer.parseInt(b.getString(key).replaceAll("\\D+", ""))) {
                        return -1;
                    } else
                        return 1;
                } catch (JSONException e) {

                }
                return 0;
            }
        });

        for (int i = 0; i < jsonArray.length(); i++) {
            sortedJsonArray.put(list.get(i));
        }
        return sortedJsonArray;
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
        }
        if(PayU.availableEmi != null){
            setupAdapter();
        }
    }

    public void setupAdapter(){
        JSONArray bankList = new JSONArray();

        Set<String> bankNames = new HashSet<String>();

        try {
            for (int i = 0; i < PayU.availableEmi.length(); i++) {
                JSONObject jsonObject = new JSONObject();
                String bankName = PayU.availableEmi.getJSONObject(i).getString("bankName");
                if (!bankNames.contains(bankName)) {
                    bankNames.add(bankName);
                    jsonObject.put("bank", bankName);
                    bankList.put(jsonObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        EmiBankListAdapter adapter = new EmiBankListAdapter(getActivity(), bankList);
        bankListSpinner = (Spinner) getActivity().findViewById(R.id.bankListSpinner);
        bankListSpinner.setAdapter(adapter);

        EmiTimeIntervalAdapter emiTimeIntervalAdapter = new EmiTimeIntervalAdapter(getActivity(), new JSONArray());
        emiTimeIntervalSpinner = (Spinner) getActivity().findViewById(R.id.emiTimeIntervalSpinner);
        emiTimeIntervalSpinner.setAdapter(emiTimeIntervalAdapter);


        bankListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                JSONArray emiArray = new JSONArray();
                try {
                    String selectedBank = ((JSONObject) adapterView.getAdapter().getItem(position)).getString("bank");

                    for (int i = 0; i < PayU.availableEmi.length(); i++) {
                        String bank = PayU.availableEmi.getJSONObject(i).getString("bankName");
                        if (bank.contentEquals(selectedBank)) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("emiInterval", PayU.availableEmi.getJSONObject(i).getString("emiInterval"));
                            jsonObject.put("emiCode", PayU.availableEmi.getJSONObject(i).getString("emiCode"));
                            emiArray.put(jsonObject);
                        }

                    }

                    EmiTimeIntervalAdapter adapter = new EmiTimeIntervalAdapter(getActivity(), jsonArraySort(emiArray, "emiInterval"));
                    emiTimeIntervalSpinner.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (emiTimeIntervalSpinner != null)
            emiTimeIntervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        bankCode = ((JSONObject) adapterView.getAdapter().getItem(i)).getString("emiCode");
                        if(!bankCode.contentEquals("default")){
                            // call validation
                            valid(null, null);
                        }else{
                            invalid(null, null);
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
