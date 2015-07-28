package com.payu.india.Tasks;

import android.os.AsyncTask;

import com.payu.india.Interfaces.ValueAddedServiceApiListener;
import com.payu.india.Model.CardStatus;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuResponse;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by franklin on 7/1/15.
 * Async task which takes care of getting value added services - netbank/issuing bank down/up status
 * Takes PayuConfig as input and sends PayuResponse to the calling activity
 * Activity which calls {@link ValueAddedServiceTask } should implement {@link ValueAddedServiceApiListener}
 */
public class ValueAddedServiceTask extends AsyncTask<PayuConfig, String, PayuResponse> {

    private ValueAddedServiceApiListener mValueAddedServiceApiListener;

    public ValueAddedServiceTask(ValueAddedServiceApiListener valueAddedServiceApiListener) {
        mValueAddedServiceApiListener = valueAddedServiceApiListener;
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

            // lets assume api has failed
            postData.setResult(PayuConstants.ERROR);
            postData.setCode(PayuErrors.INVALID_HASH);
            if (response.has(PayuConstants.MSG)) {
                postData.setResult(response.getString(PayuConstants.MSG));
            }


            if (response.has("netBankingStatus")) {
                HashMap<String, Integer> netBankingStatus = new HashMap<>();
                JSONObject netBanking = response.getJSONObject("netBankingStatus");
                Iterator<String> keysIterator = netBanking.keys();

                while (keysIterator.hasNext()) {
                    String bankCode = (String) keysIterator.next();
                    netBankingStatus.put(bankCode, netBanking.getJSONObject(bankCode).getInt("up_status"));
                }
                payuResponse.setNetBankingDownStatus(netBankingStatus);

                postData.setResult(PayuConstants.SUCCESS);
                postData.setCode(PayuErrors.NO_ERROR);
                postData.setStatus(PayuConstants.SUCCESS);
            }

            if (response.has("issuingBankDownBins")) {
                HashMap<String, CardStatus> issuingBankStatus = new HashMap<>();
                CardStatus cardStatus;

                JSONArray issuingBank = response.getJSONArray("issuingBankDownBins");
                for (int i = 0, length = issuingBank.length(); i < length; i++) {
                    for (int j = 0, binArrayLength = (issuingBank.getJSONObject(i).getJSONArray("bins_arr").length()); j < binArrayLength; j++) {
                        cardStatus = new CardStatus();
                        cardStatus.setBankName(issuingBank.getJSONObject(i).getString("title"));
                        cardStatus.setStatusCode(issuingBank.getJSONObject(i).getInt("status"));
                        issuingBankStatus.put(issuingBank.getJSONObject(i).getJSONArray("bins_arr").getString(j), cardStatus);
                    }
                }
                payuResponse.setIssuingBankStatus(issuingBankStatus);

                postData.setResult(PayuConstants.SUCCESS);
                postData.setCode(PayuErrors.NO_ERROR);
                postData.setStatus(PayuConstants.SUCCESS);
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
        mValueAddedServiceApiListener.onValueAddedServiceApiResponse(payuResponse);
    }
}
