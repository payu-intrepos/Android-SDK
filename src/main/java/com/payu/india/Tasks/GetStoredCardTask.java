package com.payu.india.Tasks;

import android.os.AsyncTask;

import com.payu.india.Interfaces.GetStoredCardApiListener;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by franklin on 6/19/15.
 * Async task which takes care of getting stored user cards
 * Takes PayuConfig as input and sends PayuResponse to the calling activity
 * Activity which calls {@link GetStoredCardTask } should implement {@link GetStoredCardApiListener}
 */
public class GetStoredCardTask extends AsyncTask<PayuConfig, String, PayuResponse> {

    private GetStoredCardApiListener mGetStoredCardApiListener;
    private StoredCard mUserCard;

    public GetStoredCardTask(GetStoredCardApiListener getStoredCardApiListener) {
        this.mGetStoredCardApiListener = getStoredCardApiListener;
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

            if (response.has(PayuConstants.USER_CARD)) { // yey! we have stored cards..
                JSONObject cardsList = response.getJSONObject(PayuConstants.USER_CARD);
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

            if (response.has(PayuConstants.MSG)) {
                postData.setResult(response.getString(PayuConstants.MSG));
            }
            if (response.has(PayuConstants.STATUS)) {
                postData.setCode(PayuErrors.NO_ERROR);
                postData.setStatus(PayuConstants.SUCCESS);
            } else {
                postData.setCode(PayuErrors.GET_USER_CARD_EXCEPTION);
                postData.setStatus(PayuConstants.ERROR);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // gotta return payuResponse
        payuResponse.setResponseStatus(postData);
        return payuResponse;
    }

    @Override
    protected void onPostExecute(PayuResponse payuResponse) {
        super.onPostExecute(payuResponse);
        mGetStoredCardApiListener.onGetStoredCardApiResponse(payuResponse);
    }
}
