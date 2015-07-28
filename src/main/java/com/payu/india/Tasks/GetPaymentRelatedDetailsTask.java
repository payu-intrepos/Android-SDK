package com.payu.india.Tasks;

import android.os.AsyncTask;

import com.payu.india.Interfaces.PaymentRelatedDetailsListener;
import com.payu.india.Model.Emi;
import com.payu.india.Model.PaymentDetails;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuResponse;
import com.payu.india.Model.PostData;
import com.payu.india.Model.StoredCard;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by franklin on 5/28/15.
 * Async task which takes care of Getting all the payment releated details for a merchant
 * Takes PayuConfig as input and sends PayuResponse to the calling activity
 * Activity which calls {@link GetPaymentRelatedDetailsTask} should implement {@link PaymentRelatedDetailsListener}
 */
public class GetPaymentRelatedDetailsTask extends AsyncTask<PayuConfig, String, PayuResponse> {

    PaymentRelatedDetailsListener mPaymentRelatedDetailsListener;

    public GetPaymentRelatedDetailsTask(PaymentRelatedDetailsListener paymentRelatedDetailsListener) {
        this.mPaymentRelatedDetailsListener = paymentRelatedDetailsListener;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected PayuResponse doInBackground(PayuConfig... params) {

        PayuResponse payuResponse = new PayuResponse();
        PostData postData = new PostData();

        try {
            URL url = null;
            // get the payuConfig first
            PayuConfig payuConfig = params[0];

            // set the environment
            switch (payuConfig.getEnvironment()) {
                case PayuConstants.PRODUCTION_ENV:
                    url = new URL(PayuConstants.PRODUCTION_FETCH_DATA_URL);
                    break;
                case PayuConstants.MOBILE_STAGING_ENV:
                    url = new URL(PayuConstants.MOBILE_TEST_FETCH_DATA_URL);
                    break;
                case PayuConstants.STAGING_ENV:
                    url = new URL(PayuConstants.TEST_FETCH_DATA_URL);
                    break;
            }

            byte[] postParamsByte = payuConfig.getData().toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postParamsByte.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postParamsByte);

            InputStream responseInputStream = conn.getInputStream();

            StringBuffer responseStringBuffer = new StringBuffer();
            byte[] byteContainer = new byte[1024];
            for (int i; (i = responseInputStream.read(byteContainer)) != -1; ) {
                responseStringBuffer.append(new String(byteContainer, 0, i));
            }

            JSONObject response = new JSONObject(responseStringBuffer.toString());

            if (response.has(PayuConstants.IBIBO_CODES)) { // okay we have ibibo codes now
                JSONObject ibiboCodes = response.getJSONObject(PayuConstants.IBIBO_CODES);
                if (ibiboCodes.has(PayuConstants.CREDITCARD)) {
                    JSONObject creditCardCollections = ibiboCodes.getJSONObject(PayuConstants.CREDITCARD);
                    ArrayList<PaymentDetails> ccList = new ArrayList<>();
                    Iterator<String> keysIterator = creditCardCollections.keys();
                    while (keysIterator.hasNext()) {
                        String bankCode = keysIterator.next();
                        JSONObject ccObject = creditCardCollections.getJSONObject(bankCode);
                        PaymentDetails paymentDetails = new PaymentDetails();
                        paymentDetails.setBankCode(bankCode);
                        paymentDetails.setBankId(ccObject.getString(PayuConstants.BANK_ID));
                        paymentDetails.setBankName(ccObject.getString(PayuConstants.TITLE));
                        paymentDetails.setPgId(ccObject.getString(PayuConstants.PGID));
                        ccList.add(paymentDetails);
                    }
                    payuResponse.setCreditCard(ccList);
                }

                if (ibiboCodes.has(PayuConstants.DEBITCARD)) {
                    JSONObject debitCardCollections = ibiboCodes.getJSONObject(PayuConstants.DEBITCARD);
                    ArrayList<PaymentDetails> dbList = new ArrayList<>();
                    Iterator<String> keysIterator = debitCardCollections.keys();
                    while (keysIterator.hasNext()) {
                        String bankCode = keysIterator.next();
                        PaymentDetails paymentDetails = new PaymentDetails();
                        JSONObject dcObject = debitCardCollections.getJSONObject(bankCode);
                        paymentDetails.setBankCode(bankCode);
                        paymentDetails.setBankId(dcObject.getString(PayuConstants.BANK_ID));
                        paymentDetails.setBankName(dcObject.getString(PayuConstants.TITLE));
                        paymentDetails.setPgId(dcObject.getString(PayuConstants.PGID));
                        dbList.add(paymentDetails);
                    }
                    payuResponse.setDebitCard(dbList);
                }
                if (ibiboCodes.has(PayuConstants.NETBANKING)) {
                    JSONObject netBanksCollections = ibiboCodes.getJSONObject(PayuConstants.NETBANKING);
                    Iterator<String> keysIterator = netBanksCollections.keys();
                    ArrayList<PaymentDetails> nbList = new ArrayList<PaymentDetails>();
                    while (keysIterator.hasNext()) {
                        String bankCode = keysIterator.next();
                        JSONObject netBank = netBanksCollections.getJSONObject(bankCode);
                        PaymentDetails paymentDetails = new PaymentDetails();
                        paymentDetails.setBankCode(bankCode);
                        paymentDetails.setBankId(netBank.getString(PayuConstants.BANK_ID));
                        paymentDetails.setBankName(netBank.getString(PayuConstants.TITLE));
                        paymentDetails.setPgId(netBank.getString(PayuConstants.PGID));
                        nbList.add(paymentDetails);
                    }
                    payuResponse.setNetBanks(nbList);
                }
                if (ibiboCodes.has(PayuConstants.CASHCARD)) {
                    JSONObject cashCardCollections = ibiboCodes.getJSONObject(PayuConstants.CASHCARD);
                    Iterator<String> keysIterator = cashCardCollections.keys();
                    ArrayList<PaymentDetails> cashCardList = new ArrayList<PaymentDetails>();
                    while (keysIterator.hasNext()) {
                        String bankCode = keysIterator.next();
                        JSONObject cashCardJson = cashCardCollections.getJSONObject(bankCode);
                        PaymentDetails paymentDetails = new PaymentDetails();
                        paymentDetails.setBankCode(bankCode);
                        paymentDetails.setBankId(cashCardJson.getString(PayuConstants.BANK_ID));
                        paymentDetails.setBankName(cashCardJson.getString(PayuConstants.TITLE));
                        paymentDetails.setPgId(cashCardJson.getString(PayuConstants.PGID));
                        cashCardList.add(paymentDetails);
                    }
                    payuResponse.setCashCard(cashCardList);
                }
                if (ibiboCodes.has(PayuConstants.IVR)) {
                    JSONObject ivrCCCollections = ibiboCodes.getJSONObject(PayuConstants.IVR);
                    Iterator<String> keysIterator = ivrCCCollections.keys();
                    ArrayList<PaymentDetails> ivrCCList = new ArrayList<>();
                    while (keysIterator.hasNext()) {
                        String bankCode = keysIterator.next();
                        JSONObject ivrccObject = ivrCCCollections.getJSONObject(bankCode);
                        PaymentDetails paymentDetails = new PaymentDetails();
                        paymentDetails.setBankCode(bankCode);
                        paymentDetails.setBankId(ivrccObject.getString(PayuConstants.BANK_ID));
                        paymentDetails.setBankName(ivrccObject.getString(PayuConstants.TITLE));
                        paymentDetails.setPgId(ivrccObject.getString(PayuConstants.PGID));
                        ivrCCList.add(paymentDetails);
                    }
                    payuResponse.setIvr(ivrCCList);
                }

                if (ibiboCodes.has(PayuConstants.IVRDC)) {
                    JSONObject ivrDCCollections = ibiboCodes.getJSONObject(PayuConstants.IVRDC);
                    Iterator<String> keysIterator = ivrDCCollections.keys();
                    ArrayList<PaymentDetails> ivrDCList = new ArrayList<>();
                    while (keysIterator.hasNext()) {
                        String bankCode = keysIterator.next();
                        JSONObject ivrdcObject = ivrDCCollections.getJSONObject(bankCode);
                        PaymentDetails paymentDetails = new PaymentDetails();
                        paymentDetails.setBankCode(bankCode);
                        paymentDetails.setBankId(ivrdcObject.getString(PayuConstants.BANK_ID));
                        paymentDetails.setBankName(ivrdcObject.getString(PayuConstants.TITLE));
                        paymentDetails.setPgId(ivrdcObject.getString(PayuConstants.PGID));
                        ivrDCList.add(paymentDetails);
                    }
                    payuResponse.setIvrdc(ivrDCList);
                }

                if (ibiboCodes.has(PayuConstants.PAISAWALLET)) {
                    JSONObject paisaWalletCollections = ibiboCodes.getJSONObject(PayuConstants.PAISAWALLET);
                    Iterator<String> keysIterator = paisaWalletCollections.keys();
                    ArrayList<PaymentDetails> paisaWalletList = new ArrayList<>();
                    while (keysIterator.hasNext()) {
                        String bankCode = keysIterator.next();
                        JSONObject paisaWallet = paisaWalletCollections.getJSONObject(bankCode);
                        PaymentDetails paymentDetails = new PaymentDetails();
                        paymentDetails.setBankCode(bankCode);
                        paymentDetails.setBankId(paisaWallet.getString(PayuConstants.BANK_ID));
                        paymentDetails.setBankName(paisaWallet.getString(PayuConstants.TITLE));
                        paymentDetails.setPgId(paisaWallet.getString(PayuConstants.PGID));
                        paisaWalletList.add(paymentDetails);
                    }
                    payuResponse.setPaisaWallet(paisaWalletList);
                }

                if (ibiboCodes.has(PayuConstants.EMI_IN_RESPONSE)) {
                    JSONObject emiObjectCollections = ibiboCodes.getJSONObject(PayuConstants.EMI_IN_RESPONSE);
                    Iterator<String> keysIterator = emiObjectCollections.keys();
                    ArrayList<Emi> emiList = new ArrayList<Emi>();
                    while (keysIterator.hasNext()) {
                        String bankCode = keysIterator.next();
                        JSONObject emiObject = emiObjectCollections.getJSONObject(bankCode);
                        Emi emi = new Emi();
                        emi.setBankCode(bankCode);
                        emi.setBankName(emiObject.getString(PayuConstants.BANK));
                        emi.setBankTitle(emiObject.getString(PayuConstants.TITLE));
                        emi.setPgId(emiObject.getString(PayuConstants.PGID));
                        emiList.add(emi);
                    }
                    payuResponse.setEmi(emiList);
                }
            }

            if (response.has(PayuConstants.USERCARDS)) { //  we have user cards also
                if (response.getJSONObject(PayuConstants.USERCARDS).has(PayuConstants.USER_CARD)) { // yey! we have stored cards..
                    JSONObject cardsList = response.getJSONObject(PayuConstants.USERCARDS).getJSONObject(PayuConstants.USER_CARD);
                    Iterator<String> keysIterator = cardsList.keys();
                    ArrayList<StoredCard> userCardArrayList = new ArrayList<StoredCard>();

                    while (keysIterator.hasNext()) {
                        String cardToken = (String) keysIterator.next();
                        StoredCard userCard = new StoredCard();
                        JSONObject card = cardsList.getJSONObject(cardToken);

                        userCard.setNameOnCard(card.getString(PayuConstants.NAME_ON_CARD));
                        userCard.setCardName(card.getString(PayuConstants.CARD_NAME));
                        userCard.setExpiryYear(card.getString(PayuConstants.EXPIRY_YEAR));
                        userCard.setExpiryMonth(card.getString(PayuConstants.EXPIRY_MONTY));
                        userCard.setCardType(card.getString(PayuConstants.CARD_TYPE));
                        userCard.setCardToken(card.getString(PayuConstants.CARD_TOKEN));
                        userCard.setIsExpired(card.getInt(PayuConstants.IS_EXPIRED) == 0 ? false : true);
                        userCard.setCardMode(card.getString(PayuConstants.CARD_MODE));
                        userCard.setMaskedCardNumber(card.getString(PayuConstants.CARD_NO));
                        userCard.setCardBrand(card.getString(PayuConstants.CARD_BRAND));
                        userCard.setCardBin(card.getString(PayuConstants.CARD_BIN));
                        userCard.setIsDomestic(card.getString(PayuConstants.IS_DOMESTIC));
                        userCardArrayList.add(userCard);
                        userCard = null;
                    }
                    payuResponse.setStoredCards(userCardArrayList);
                }
            }

            if (response.has(PayuConstants.STATUS) && response.getString(PayuConstants.STATUS).contentEquals("0")) {
                postData = new PostData();
                postData.setCode(PayuErrors.INVALID_HASH);
                postData.setStatus(PayuConstants.ERROR);
                postData.setResult(response.getString(PayuConstants.MSG));
            } else {

                postData.setCode(PayuErrors.NO_ERROR);
                // Result status should be success and the status of user cards
                postData.setResult(PayuErrors.SDK_DETAILS_FETCHED_SUCCESSFULLY + response.getJSONObject(PayuConstants.USERCARDS).getString(PayuConstants.MSG));
                postData.setStatus(PayuConstants.SUCCESS);

            }

        } catch (ProtocolException e) {
            postData.setCode(PayuErrors.PROTOCOL_EXCEPTION);
            postData.setStatus(PayuConstants.ERROR);
            postData.setResult(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            postData.setCode(PayuErrors.UN_SUPPORTED_ENCODING_EXCEPTION);
            postData.setStatus(PayuConstants.ERROR);
            postData.setResult(e.getMessage());
        } catch (JSONException e) {
            postData.setCode(PayuErrors.JSON_EXCEPTION);
            postData.setStatus(PayuConstants.ERROR);
            postData.setResult(e.getMessage());
        } catch (IOException e) {
            postData.setCode(PayuErrors.IO_EXCEPTION);
            postData.setStatus(PayuConstants.ERROR);
            postData.setResult(e.getMessage());
        }

        payuResponse.setResponseStatus(postData);
        return payuResponse;
    }

    @Override
    protected void onPostExecute(PayuResponse payuResponse) {
        super.onPostExecute(payuResponse);
        mPaymentRelatedDetailsListener.onPaymentRelatedDetailsResponse(payuResponse);
    }
}
