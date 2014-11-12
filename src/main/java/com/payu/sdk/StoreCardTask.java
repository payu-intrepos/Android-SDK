package com.payu.sdk;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by franklin on 10/7/14.
 */
public class StoreCardTask extends AsyncTask<List<NameValuePair>, String, JSONArray> {

    PaymentListener mResponseListener = null;

    public StoreCardTask(PaymentListener responseListener) {
        this.mResponseListener = responseListener;
    }

    @Override
    protected JSONArray doInBackground(List<NameValuePair>... lists) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Constants.FETCH_DATA_URL);

        JSONArray storedCards = new JSONArray();

        try {
            httppost.setEntity(new UrlEncodedFormEntity(lists[0]));

            JSONObject response = new JSONObject(EntityUtils.toString(httpclient.execute(httppost).getEntity()));

            // fetch saved cards
            if (response.has("user_cards")) {

                JSONObject user_cards = response.getJSONObject("user_cards");
                Iterator<String> keysIterator = user_cards.keys();

                while (keysIterator.hasNext()) {
                    String cardToken = (String) keysIterator.next();
                    storedCards.put(response.getJSONObject("user_cards").getJSONObject(cardToken));
                }

                return storedCards;
            }
            // store a new card

            if (response.get("msg").toString().contentEquals("Card Stored Successfully.")) {
                storedCards.put(response.get("msg"));

                return storedCards;
            }

            // edit card

            if (response.get("msg").toString().contains("edited Successfully")) {
                storedCards.put(response.get("msg"));

                return storedCards;
            }

            // delete card

            if (response.get("msg").toString().contains("deleted successfully")) {
                storedCards.put(response.get("msg"));

                return storedCards;
            }

            // offer validate

            if (response.has("offer_key")) {
                storedCards.put(response);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return storedCards;
    }

    @Override
    protected void onPostExecute(JSONArray result) {
        mResponseListener.onGetStoreCardDetails(result);
    }
}
