package com.payu.india.Tasks;

import android.os.AsyncTask;

import com.payu.india.Interfaces.VerifyPaymentApiListener;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuResponse;
import com.payu.india.Model.PostData;
import com.payu.india.Model.TransactionDetails;
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
 * Created by franklin on 7/4/15.
 * Async task which takes care of getting verify payment details
 * Takes PayuConfig as input and sends PayuResponse to the calling activity
 * Activity which calls {@link VerifyPaymentTask } should implement {@link VerifyPaymentApiListener}
 */
public class VerifyPaymentTask extends AsyncTask<PayuConfig, String, PayuResponse> {

    VerifyPaymentApiListener mVerifyPaymentApiListener;

    public VerifyPaymentTask(VerifyPaymentApiListener verifyPaymentApiListener) {
        mVerifyPaymentApiListener = verifyPaymentApiListener;
    }

    @Override
    protected PayuResponse doInBackground(PayuConfig... params) {
        PayuResponse payuResponse = new PayuResponse();
        PostData postData = new PostData();
        ArrayList<TransactionDetails> transactionDetailsCollections = new ArrayList<>();
        TransactionDetails transactionDetailsObject = new TransactionDetails();

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
            } else if (response.has(PayuConstants.STATUS) && response.getInt(PayuConstants.STATUS) == 1) {
                postData.setCode(PayuErrors.NO_ERROR);
                postData.setStatus(PayuConstants.SUCCESS);

                JSONObject transactionDetailsList = response.getJSONObject(PayuConstants.TRANSACTION_DETAILS);
                Iterator<String> transactionDetailsListIterator = transactionDetailsList.keys();
                while (transactionDetailsListIterator.hasNext()) {
                    JSONObject transactionDetails = transactionDetailsList.getJSONObject(transactionDetailsListIterator.next());
                    Iterator<String> transactionDetailsIterator = transactionDetails.keys();
                    transactionDetailsObject = new TransactionDetails();
                    while (transactionDetailsIterator.hasNext()) {
                        String key = transactionDetailsIterator.next();
                        switch (key) {
                            case PayuConstants.MIHPAY_ID:
                                transactionDetailsObject.setMihpayId(transactionDetails.getString(PayuConstants.MIHPAY_ID));
                                break;
                            case PayuConstants.REQUEST_ID:
                                transactionDetailsObject.setRequestId(transactionDetails.getString(PayuConstants.REQUEST_ID));
                                break;
                            case PayuConstants.BANK_REF_NUM:
                                transactionDetailsObject.setBankReferenceNumber(transactionDetails.getString(PayuConstants.BANK_REF_NUM));
                                break;
                            case PayuConstants.AMT:
                                transactionDetailsObject.setAmount(transactionDetails.getString(PayuConstants.AMT));
                                break;
                            case PayuConstants.TXNID:
                                transactionDetailsObject.setTxnid(transactionDetails.getString(PayuConstants.TXNID));
                                break;
                            case PayuConstants.ADDITIONAL_CHARGES:
                                transactionDetailsObject.setAdditionalCharges(transactionDetails.getString(PayuConstants.ADDITIONAL_CHARGES));
                                break;
                            case PayuConstants.PRODUCT_INFO:
                                transactionDetailsObject.setProductinfo(transactionDetails.getString(PayuConstants.PRODUCT_INFO));
                                break;
                            case PayuConstants.FIRST_NAME:
                                transactionDetailsObject.setFirstname(transactionDetails.getString(PayuConstants.FIRST_NAME));
                                break;
                            case PayuConstants.BANK_CODE:
                                transactionDetailsObject.setBankCode(transactionDetails.getString(PayuConstants.BANK_CODE));
                                break;
                            case PayuConstants.UDF1:
                                transactionDetailsObject.setUdf1(transactionDetails.getString(PayuConstants.UDF1));
                                break;
                            case PayuConstants.UDF2:
                                transactionDetailsObject.setUdf2(transactionDetails.getString(PayuConstants.UDF2));
                                break;
                            case PayuConstants.UDF3:
                                transactionDetailsObject.setUdf3(transactionDetails.getString(PayuConstants.UDF3));
                                break;
                            case PayuConstants.UDF4:
                                transactionDetailsObject.setUdf4(transactionDetails.getString(PayuConstants.UDF4));
                                break;
                            case PayuConstants.UDF5:
                                transactionDetailsObject.setUdf5(transactionDetails.getString(PayuConstants.UDF5));
                                break;
                            case PayuConstants.FIELD9:
                                transactionDetailsObject.setField9(transactionDetails.getString(PayuConstants.FIELD9));
                                break;
                            case PayuConstants.ERROR_CODE:
                                transactionDetailsObject.setErrorCode(transactionDetails.getString(PayuConstants.ERROR_CODE));
                                break;
                            case PayuConstants.CARD_TYPE:
                                transactionDetailsObject.setCardtype(transactionDetails.getString(PayuConstants.CARD_TYPE));
                                break;
                            case PayuConstants.ERROR_MESSAGE:
                                transactionDetailsObject.setErrorMessage(transactionDetails.getString(PayuConstants.ERROR_MESSAGE));
                                break;
                            case PayuConstants.NET_AMOUNT_DEBIT:
                                transactionDetailsObject.setNetAmountDebit(transactionDetails.getString(PayuConstants.NET_AMOUNT_DEBIT));
                                break;
                            case PayuConstants.DISC:
                                transactionDetailsObject.setDiscount(transactionDetails.getString(PayuConstants.DISC));
                                break;
                            case PayuConstants.MODE:
                                transactionDetailsObject.setMode(transactionDetails.getString(PayuConstants.MODE));
                                break;
                            case PayuConstants.PG_TYPE:
                                transactionDetailsObject.setPgType(transactionDetails.getString(PayuConstants.PG_TYPE));
                                break;
                            case PayuConstants.CARD_NO:
                                transactionDetailsObject.setCardNo(transactionDetails.getString(PayuConstants.CARD_NO));
                                break;
                            case PayuConstants.ADDED_ON:
                                transactionDetailsObject.setAddedon(transactionDetails.getString(PayuConstants.ADDED_ON));
                                break;
                            case PayuConstants.STATUS:
                                transactionDetailsObject.setStatus(transactionDetails.getString(PayuConstants.STATUS));
                                break;
                            case PayuConstants.UNMAPPED_STATUS:
                                transactionDetailsObject.setUnmappedStatus(transactionDetails.getString(PayuConstants.UNMAPPED_STATUS));
                                break;
                            case PayuConstants.MERCHANT_UTR:
                                transactionDetailsObject.setMerchantUTR(transactionDetails.getString(PayuConstants.MERCHANT_UTR));
                                break;
                            case PayuConstants.SETTLED_AT:
                                transactionDetailsObject.setSettledAt(transactionDetails.getString(PayuConstants.SETTLED_AT));
                                break;

                        }
                    }
                    transactionDetailsCollections.add(transactionDetailsObject);
                }

                payuResponse.setTransactionDetailsList(transactionDetailsCollections);
            }

            // To do set the values.

        } catch (MalformedURLException e) {
            // TODO set exception details in postdata setResult
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
        mVerifyPaymentApiListener.onVerifyPaymentResponse(payuResponse);
    }
}
