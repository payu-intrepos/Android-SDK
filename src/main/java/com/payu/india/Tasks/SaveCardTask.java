package com.payu.india.Tasks;

import android.os.AsyncTask;

import com.payu.india.Interfaces.SaveCardApiListener;
import com.payu.india.Model.PayuConfig;
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

/**
 * Created by franklin on 6/26/15.
 * Async task which takes care of saving a card
 * Takes PayuConfig as input and sends PayuResponse to the calling activity
 * Activity which calls {@link SaveCardTask } should implement {@link SaveCardApiListener}
 */
public class SaveCardTask extends AsyncTask<PayuConfig, String, PayuResponse> {

    private SaveCardApiListener mSaveCardApiListener;

    public SaveCardTask(SaveCardApiListener saveCardApiListener) {
        mSaveCardApiListener = saveCardApiListener;
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

            if (response.has(PayuConstants.STATUS) && response.getString(PayuConstants.STATUS).contentEquals("0")) { // hash mismatch
                postData.setCode(PayuErrors.INVALID_HASH);
                postData.setStatus(PayuConstants.ERROR);
            }
            if (response.has(PayuConstants.STATUS) && response.getString(PayuConstants.STATUS).contentEquals("1")) {
                postData.setStatus(PayuConstants.SUCCESS);
                postData.setCode(PayuErrors.NO_ERROR);
            }
            if (response.has(PayuConstants.MSG)) {
                postData.setResult(response.getString(PayuConstants.MSG));
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
        mSaveCardApiListener.onSaveCardResponse(payuResponse);
    }
}
