package com.payu.sdk;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by franklin on 10/7/14.
 */
public class GetResponseTask extends AsyncTask<List<NameValuePair>, String, JSONArray> {

    PaymentListener mResponseListener = null;

    public GetResponseTask(PaymentListener responseListener) {
        this.mResponseListener = responseListener;
    }

    @Override
    protected JSONArray doInBackground(List<NameValuePair>... lists) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Constants.FETCH_DATA_URL);

        Set<String> enforcePayMethodsSet = new HashSet<String>();

        Set<String> dropCategoriesSet = new HashSet<String>();

        if (PayU.enforcePayMethod != null) {
            String[] enforcePayMethodsArray = PayU.enforcePayMethod.split("\\|");
            for (int i = 0; i < enforcePayMethodsArray.length; i++) {
                enforcePayMethodsSet.add(enforcePayMethodsArray[i]);
            }
        }


        if (PayU.dropCategory != null) {
            String[] categories = PayU.dropCategory.split(",");
            for (int i = 0; i < categories.length; i++) {
                String[] splitCategories = categories[i].split("\\|");
                for (int j = 0; j < splitCategories.length; j++) {
                    dropCategoriesSet.add(splitCategories[j]);
                }
            }
        }

        try {

            JSONArray banksAvailable = new JSONArray();
            JSONArray emiAvailable = new JSONArray();
            JSONArray modesAvailable = new JSONArray();
            JSONArray cashCardsAvailable = new JSONArray();
            JSONArray creditCardsAvailable = new JSONArray();
            JSONArray debitCardsAvailable = new JSONArray();

            Boolean ccPresent = false;
            Boolean dcPresent = false;
            Boolean nbPresent = false;
            Boolean emiPresent = false;
            Boolean cashCardPresent = false;

            httppost.setEntity(new UrlEncodedFormEntity(lists[0]));

            JSONObject response = new JSONObject(EntityUtils.toString(httpclient.execute(httppost).getEntity()));


            // banks available, enforce method
            if (response.has("netbanking")) {
                Iterator<String> bankKeysIterator = response.getJSONObject("netbanking").keys();

                // for default option
                JSONObject banksObject = new JSONObject();
                banksObject.put("code", "null");
                banksObject.put("title", "");
                banksAvailable.put(banksObject);

                if (PayU.enforcePayMethod == null || enforcePayMethodsSet.contains("netbanking")) {
                    while (bankKeysIterator.hasNext()) {
                        String bankCode = (String) bankKeysIterator.next();
                        if (!dropCategoriesSet.contains(bankCode)) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("code", bankCode);
                            jsonObject.put("title", response.getJSONObject("netbanking").getJSONObject(bankCode).get("title"));
                            banksAvailable.put(jsonObject);
                        }
                    }
                    nbPresent = true;
                } else {
                    while (bankKeysIterator.hasNext()) {
                        String bankCode = (String) bankKeysIterator.next();
                        if (enforcePayMethodsSet.contains(bankCode) && !dropCategoriesSet.contains(bankCode)) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("code", bankCode);
                            jsonObject.put("title", response.getJSONObject("netbanking").getJSONObject(bankCode).get("title"));
                            banksAvailable.put(jsonObject);
                            nbPresent = true;
                        }
                    }
                }

                // set default option
                PayU.availableBanks = jsonArraySort(banksAvailable, "title");
                banksObject = new JSONObject();
                banksObject.put("code", "null");
                banksObject.put("title", "Select your bank");
                PayU.availableBanks.put(0, banksObject);

            }

            //emi available, enforce method check
            /*Iterator<String> emiKeysIterator = response.getJSONObject("emi").keys();
            if (PayU.enforcePayMethod == null || enforcePayMethodsSet.contains("Emi")) {
                while (emiKeysIterator.hasNext()) {
                    String emiCode = (String) emiKeysIterator.next();
                    if (!dropCategoriesSet.contains(emiCode)) {
                        JSONObject jsonObject = new JSONObject();
                        JSONArray jsonArray = new JSONArray();
                        JSONObject object = new JSONObject();
                        jsonObject.put("emiCode", emiCode);
                        jsonObject.put("emiInterval", response.getJSONObject("emi").getJSONObject(emiCode).get("title").toString());
                        jsonObject.put("bankName", response.getJSONObject("emi").getJSONObject(emiCode).get("bank").toString());
                        jsonArray.put(jsonObject);
                        object.put(response.getJSONObject("emi").getJSONObject(emiCode).get("bank").toString(), jsonArray);
                        emiAvailable.put(object);
                    }
                }
                emiPresent = true;
            } else {
                while (emiKeysIterator.hasNext()) {
                    String emiCode = (String) emiKeysIterator.next();
                    if (enforcePayMethodsSet.contains(emiCode) && !dropCategoriesSet.contains(emiCode)) {
                        JSONObject jsonObject = new JSONObject();
                        JSONArray jsonArray = new JSONArray();
                        JSONObject object = new JSONObject();
                        jsonObject.put("emiCode", emiCode);
                        jsonObject.put("emiInterval", response.getJSONObject("emi").getJSONObject(emiCode).get("title").toString());
                        jsonObject.put("bankName", response.getJSONObject("emi").getJSONObject(emiCode).get("bank").toString());
                        jsonArray.put(jsonObject);
                        object.put(response.getJSONObject("emi").getJSONObject(emiCode).get("bank").toString(), jsonArray);
                        emiAvailable.put(object);
                        emiPresent = true;
                    }
                }
            }*/

            if (response.has("emi")) {
                Iterator<String> emiKeysIterator = response.getJSONObject("emi").keys();
                if (PayU.enforcePayMethod == null || enforcePayMethodsSet.contains("Emi")) {
                    while (emiKeysIterator.hasNext()) {
                        String emiCode = (String) emiKeysIterator.next();
                        if (!dropCategoriesSet.contains(emiCode)) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("emiCode", emiCode);
                            jsonObject.put("emiInterval", response.getJSONObject("emi").getJSONObject(emiCode).get("title").toString());
                            jsonObject.put("bankName", response.getJSONObject("emi").getJSONObject(emiCode).get("bank").toString());
                            emiAvailable.put(jsonObject);
                        }
                    }
                    emiPresent = true;
                } else {
                    while (emiKeysIterator.hasNext()) {
                        String emiCode = (String) emiKeysIterator.next();
                        if (enforcePayMethodsSet.contains(emiCode) && !dropCategoriesSet.contains(emiCode)) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("emiCode", emiCode);
                            jsonObject.put("emiInterval", response.getJSONObject("emi").getJSONObject(emiCode).get("title").toString());
                            jsonObject.put("bankName", response.getJSONObject("emi").getJSONObject(emiCode).get("bank").toString());
                            emiAvailable.put(jsonObject);
                            emiPresent = true;
                        }
                    }
                }
                PayU.availableEmi = jsonArraySort(emiAvailable, "bankName");
            }


            // cash card available, enforce pay method

            if (response.has("cashcard")) {
                Iterator<String> cashCardIterator = response.getJSONObject("cashcard").keys();
                // for default options

                JSONObject cashObject = new JSONObject();
                cashObject.put("code", "null");
                cashObject.put("name", "");
                cashCardsAvailable.put(cashObject);

                if (PayU.enforcePayMethod == null || enforcePayMethodsSet.contains("cashcard")) {
                    while (cashCardIterator.hasNext()) {
                        String cashCardCode = cashCardIterator.next();
                        if (!dropCategoriesSet.contains(cashCardCode)) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("code", cashCardCode);
                            jsonObject.put("name", response.getJSONObject("cashcard").getJSONObject(cashCardCode).getString("title"));
                            cashCardsAvailable.put(jsonObject);
                        }
                    }
                    cashCardPresent = true;
                } else {
                    while (cashCardIterator.hasNext()) {
                        String cashCardCode = cashCardIterator.next();
                        JSONObject jsonObject = new JSONObject();
                        if (enforcePayMethodsSet.contains(cashCardCode) && !dropCategoriesSet.contains(cashCardCode)) {
                            jsonObject.put("code", cashCardCode);
                            jsonObject.put("name", response.getJSONObject("cashcard").getJSONObject(cashCardCode).getString("title"));
                            cashCardsAvailable.put(jsonObject);
                            cashCardPresent = true;
                        }
                    }
                }

                // for default select option

                PayU.availableCashCards = jsonArraySort(cashCardsAvailable, "name");
                cashObject = new JSONObject();
                cashObject.put("code", "null");
                cashObject.put("name", "Select your cash card");
                PayU.availableCashCards.put(0, cashObject);

            }

            if (response.has("creditcard")) {
                //   available credit cardsy
                Iterator<String> creditCardIterator = response.getJSONObject("creditcard").keys();
                if (PayU.enforcePayMethod == null || enforcePayMethodsSet.contains("creditcard")) {
                    while (creditCardIterator.hasNext()) {
                        String creditCardCode = creditCardIterator.next();
                        if (!dropCategoriesSet.contains(creditCardCode)) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("code", creditCardCode);
                            jsonObject.put("name", response.getJSONObject("creditcard").getJSONObject(creditCardCode).getString("title"));
                            creditCardsAvailable.put(jsonObject);
                        }
                    }
                    ccPresent = true;
                } else {
                    while (creditCardIterator.hasNext()) {
                        String creditCardCode = creditCardIterator.next();
                        if (enforcePayMethodsSet.contains(creditCardCode) && !dropCategoriesSet.contains(creditCardCode)) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("code", creditCardCode);
                            jsonObject.put("name", response.getJSONObject("creditcard").getJSONObject(creditCardCode).getString("title"));
                            creditCardsAvailable.put(jsonObject);
                            ccPresent = true;
                        }
                    }
                }
                PayU.availableCreditCards = creditCardsAvailable;

            }

            if (response.has("debitcard")) {
                // available debit cards

                Iterator<String> debitCardIterator = response.getJSONObject("debitcard").keys();
                if (PayU.enforcePayMethod == null || enforcePayMethodsSet.contains("debitcard")) {
                    while (debitCardIterator.hasNext()) {
                        String debitCardCode = debitCardIterator.next();
                        if (!dropCategoriesSet.contains(debitCardCode)) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("code", debitCardCode);
                            jsonObject.put("name", response.getJSONObject("debitcard").getJSONObject(debitCardCode).getString("title"));
                            debitCardsAvailable.put(jsonObject);
                        }
                    }
                    dcPresent = true;
                } else {
                    while (debitCardIterator.hasNext()) {
                        String debitCardCode = debitCardIterator.next();
                        if (enforcePayMethodsSet.contains(debitCardCode) && !dropCategoriesSet.contains(debitCardCode)) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("code", debitCardCode);
                            jsonObject.put("name", response.getJSONObject("debitcard").getJSONObject(debitCardCode).getString("title"));
                            debitCardsAvailable.put(jsonObject);
                            dcPresent = true;
                        }
                    }
                }
                PayU.availableDebitCards = debitCardsAvailable;

            }

            // lets find the available modes
            modesAvailable.put("STORED_CARDS");
            if (ccPresent)
                modesAvailable.put("CC");
            if (dcPresent)
                modesAvailable.put("DC");
            if (nbPresent)
                modesAvailable.put("NB");
            if (emiPresent)
                modesAvailable.put("EMI");
            if (cashCardPresent)
                modesAvailable.put("CASH");
//            if (ccPresent && dcPresent)
//                modesAvailable.put("STORED_CARDS");

            // now paisa wallet left out, lets check that.
            Iterator<String> modeKeysIterator = response.keys();
            if ((PayU.enforcePayMethod == null || enforcePayMethodsSet.contains("paisawallet")) && (PayU.dropCategory == null) || !dropCategoriesSet.contains("paisawallet")) {
                while (modeKeysIterator.hasNext()) {
                    String option = modeKeysIterator.next();
                    if (option.contentEquals("paisawallet"))
                        modesAvailable.put("PAYU_MONEY");
                }
            }

            // set the time data fetched at
            PayU.dataFetchedAt = System.currentTimeMillis();

            return modesAvailable;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray result) {
        mResponseListener.onGetAvailableBanks(result);
    }

    public JSONArray jsonArraySort(JSONArray jsonArray, final String key) throws JSONException {

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
                String valA = new String();
                String valB = new String();

                try {
                    valA = (String) a.get(key);
                    valB = (String) b.get(key);
                } catch (JSONException e) {

                }

                return valA.compareTo(valB);
                //if you want to change the sort order, simply use the following:
                //return -valA.compareTo(valB);
            }
        });

        for (int i = 0; i < jsonArray.length(); i++) {
            sortedJsonArray.put(list.get(i));
        }

        return sortedJsonArray;
    }
}
