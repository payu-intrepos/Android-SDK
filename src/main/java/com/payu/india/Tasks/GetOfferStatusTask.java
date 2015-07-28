package com.payu.india.Tasks;

import android.os.AsyncTask;

import com.payu.india.Interfaces.GetOfferStatusApiListener;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuOffer;
import com.payu.india.Model.PayuResponse;
import com.payu.india.Model.PostData;
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
import java.util.Iterator;

/**
 * Created by franklin on 7/3/15.
 * Async task which takes care of getting offer status
 * Takes PayuConfig as input and sends PayuResponse to the calling activity
 * Activity which calls {@link GetOfferStatusTask } should implement {@link GetOfferStatusApiListener}
 */
public class GetOfferStatusTask extends AsyncTask<PayuConfig, String, PayuResponse> {

    GetOfferStatusApiListener mGetOfferStatusApiListener;

    public GetOfferStatusTask(GetOfferStatusApiListener getOfferStatusApiListener) {
        mGetOfferStatusApiListener = getOfferStatusApiListener;
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

            if (response.has(PayuConstants.MSG)) {
                postData.setResult(response.getString(PayuConstants.MSG));
            }
            if (response.has(PayuConstants.STATUS) && response.getInt(PayuConstants.STATUS) == 0) {
                postData.setCode(PayuErrors.INVALID_HASH);
                postData.setStatus(PayuConstants.ERROR);
            } else {
                postData.setCode(PayuErrors.NO_ERROR);
                postData.setStatus(PayuConstants.SUCCESS);
            }
            PayuOffer payuOffer = new PayuOffer();

            Iterator<String> keys = response.keys();

            while (keys.hasNext()) {
                switch (keys.next()) {
                    case PayuConstants.STATUS:
                        payuOffer.setStatus(response.getString(PayuConstants.STATUS));
                        break;
                    case PayuConstants.MSG:
                        payuOffer.setMsg(response.getString(PayuConstants.MSG));
                        break;
                    case PayuConstants.ERROR_CODE:
                        payuOffer.setErrorCode(response.getString(PayuConstants.ERROR_CODE));
                        break;
                    case PayuConstants.OFFER_KEY:
                        payuOffer.setOfferKey(response.getString(PayuConstants.OFFER_KEY));
                        break;
                    case PayuConstants.OFFER_TYPE:
                        payuOffer.setOfferType(response.getString(PayuConstants.OFFER_TYPE));
                        break;
                    case PayuConstants.OFFER_AVAILED_COUNT:
                        payuOffer.setOfferAvailedCount(response.getString(PayuConstants.OFFER_AVAILED_COUNT));
                        break;
                    case PayuConstants.OFFER_REMAINING_COUNT:
                        payuOffer.setOfferRemainingCount(response.getString(PayuConstants.OFFER_REMAINING_COUNT));
                        break;
                    case PayuConstants.DISCOUNT:
                        payuOffer.setDiscount(response.getString(PayuConstants.DISCOUNT));
                        break;
                    case PayuConstants.CATEGORY:
                        payuOffer.setCategory(response.getString(PayuConstants.CATEGORY));
                        break;
                }
            }

            payuResponse.setPayuOffer(payuOffer);

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
        mGetOfferStatusApiListener.onGetOfferStatusApiResponse(payuResponse);
    }
}
