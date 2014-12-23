package com.payu.sdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.payu.sdk.exceptions.HashException;
import com.payu.sdk.exceptions.MissingParameterException;
import com.payu.sdk.fragments.CashCardFragment;
import com.payu.sdk.fragments.CreditCardDetailsFragment;
import com.payu.sdk.fragments.DebitCardDetailsFragment;
import com.payu.sdk.fragments.EmiDetailsFragment;
import com.payu.sdk.fragments.NetBankingFragment;
import com.payu.sdk.fragments.PaymentOptionsFragment;
import com.payu.sdk.fragments.StoredCardFragment;

import org.json.JSONArray;


public class PaymentOptionsActivity extends FragmentActivity implements PaymentListener {

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);
        extras = getIntent().getExtras();
        ((TextView) findViewById(R.id.amountTextView)).setText(getString(R.string.amount, extras.getDouble(PayU.AMOUNT)));

        if (savedInstanceState == null) {
            PaymentOptionsFragment fragment = new PaymentOptionsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(PayU.PAYMENT_OPTIONS, getIntent().getExtras().getSerializable(PayU.PAYMENT_OPTIONS));
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment, "paymentOptions").commit();
        }

    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_payment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onPaymentOptionSelected(PayU.PaymentMode paymentMode) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();

        bundle.putString(PayU.OFFER_KEY, extras.getString(PayU.OFFER_KEY));
        bundle.putString(PayU.DROP_CATEGORY, extras.getString(PayU.DROP_CATEGORY));
        bundle.putString(PayU.ENFORCE_PAYMETHOD, extras.getString(PayU.ENFORCE_PAYMETHOD));
//        bundle.putString(PayU.DISABLE_PAYMENT_PROCESS_BACK_BUTTON, extras.getString(PayU.DISABLE_PAYMENT_PROCESS_BACK_BUTTON));
        bundle.putString(PayU.DISABLE_CUSTOM_BROWSER, extras.getString(PayU.DISABLE_CUSTOM_BROWSER));

        switch (paymentMode) {
            case EMI:
                transaction.replace(R.id.fragmentContainer, new EmiDetailsFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case CC:
                CreditCardDetailsFragment creditCardFragment = new CreditCardDetailsFragment();
                creditCardFragment.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer, creditCardFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case DC:
                DebitCardDetailsFragment debitCardDetailsFragment = new DebitCardDetailsFragment();
                debitCardDetailsFragment.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer, debitCardDetailsFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case NB:
                transaction.replace(R.id.fragmentContainer, new NetBankingFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case STORED_CARDS:
                StoredCardFragment storedCardFragment = new StoredCardFragment();
                storedCardFragment.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer, storedCardFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case PAYU_MONEY:
                /* open the payment process webview */
                try {
                    payuMoney();
                } catch (MissingParameterException e) {
                    e.printStackTrace();
                } catch (HashException e) {
                    e.printStackTrace();
                }
                break;
            case CASH:
                transaction.replace(R.id.fragmentContainer, new CashCardFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            default:
                // default
                break;
        }

    }

    private void payuMoney() throws MissingParameterException, HashException {
        Payment payment;
        Payment.Builder builder = new Payment().new Builder();
        Params requiredParams = new Params();
//        requiredParams.put("service_provider", "payu_paisa");

        builder.set(PayU.MODE, String.valueOf(PayU.PaymentMode.PAYU_MONEY));
        for(String key : getIntent().getExtras().keySet()) {
            builder.set(key, String.valueOf(getIntent().getExtras().get(key)));
            requiredParams.put(key, builder.get(key));
        }

        payment = builder.create();

        String postData = PayU.getInstance(this).createPayment(payment, requiredParams);

        Intent intent = new Intent(this, ProcessPaymentActivity.class);
        intent.putExtra(Constants.POST_DATA, postData);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivityForResult(intent, PayU.RESULT);    }

    @Override
    public void onGetAvailableBanks(JSONArray response) {

    }

    @Override
    public void onGetStoreCardDetails(JSONArray response) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PayU.RESULT) {
            setResult(resultCode);
            finish();
        }
    }

}
