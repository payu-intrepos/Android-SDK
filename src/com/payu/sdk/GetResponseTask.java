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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by franklin on 10/7/14.
 */
public class GetResponseTask extends AsyncTask<List<NameValuePair>, String, String> {

    PaymentListener mResponseListener = null;
    JSONObject response;
    private String localException = "";

    public GetResponseTask(PaymentListener responseListener) {
        this.mResponseListener = responseListener;
    }

    @Override
    protected String doInBackground(List<NameValuePair>... lists) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(Constants.FETCH_DATA_URL);

//        make api call here

        try {
            httppost.setEntity(new UrlEncodedFormEntity(lists[0]));
            response = new JSONObject(EntityUtils.toString(httpclient.execute(httppost).getEntity()));
            if(response.has("status") && response.getInt("status") == 0){ // not ok, some thing went wrong!
                return response.has("msg") ? response.getString("msg") : response.getString("status");
            }
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
//            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (JSONException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }

        if(response.length() > 0 && lists[0].get(1).getValue().contentEquals(Constants.PAYMENT_RELATED_DETAILS)){ // this is get ibibo codes call
            try {
                // we have  1. usercards, 2. merchant ibibo codes
                JSONObject userCards = new JSONObject();
                JSONObject ibiboCodes = new JSONObject();
                userCards = response.getJSONObject("userCards");
                ibiboCodes = response.getJSONObject("ibiboCodes");

                //ibibo codes

                // lets reset the old values.
                PayU.availableBanks = new JSONArray();
                PayU.availableEmi = new JSONArray();
                PayU.availableCashCards = new JSONArray();
                PayU.availableModes = new JSONArray();
                PayU.availableDebitCards = new JSONArray();
                PayU.availableCreditCards = new JSONArray();

                Boolean ccPresent = false;
                Boolean dcPresent = false;
                Boolean nbPresent = false;
                Boolean emiPresent = false;
                Boolean cashCardPresent = false;

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


                // banks available, enforce method
                if (ibiboCodes.has("netbanking")) {

                    JSONObject emptyObject = new JSONObject();
                    emptyObject.put("code", " ");
                    emptyObject.put("title", " ");
                    PayU.availableBanks.put(emptyObject);

                    Iterator<String> bankKeysIterator = ibiboCodes.getJSONObject("netbanking").keys();

                    if (PayU.enforcePayMethod == null || enforcePayMethodsSet.contains("netbanking")) {
                        while (bankKeysIterator.hasNext()) {
                            String bankCode = (String) bankKeysIterator.next();
                            if (!dropCategoriesSet.contains(bankCode)) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("code", bankCode);
                                jsonObject.put("title", ibiboCodes.getJSONObject("netbanking").getJSONObject(bankCode).get("title"));
//                                    banksAvailable.put(jsonObject);
                                PayU.availableBanks.put(jsonObject);
                            }
                        }
                        nbPresent = true;
                    } else {
                        while (bankKeysIterator.hasNext()) {
                            String bankCode = (String) bankKeysIterator.next();
                            if (enforcePayMethodsSet.contains(bankCode) && !dropCategoriesSet.contains(bankCode)) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("code", bankCode);
                                jsonObject.put("title", ibiboCodes.getJSONObject("netbanking").getJSONObject(bankCode).get("title"));
//                                    banksAvailable.put(jsonObject);
                                PayU.availableBanks.put(jsonObject);
                                nbPresent = true;
                            }
                        }
                    }

                    // set default option, we need an empty object.

                    PayU.availableBanks = jsonArraySort(PayU.availableBanks, "title");
                    if(Constants.SET_DEFAULT_NET_BANKING){
                        JSONObject banksObject = new JSONObject();
                        banksObject.put("code", "default");
                        banksObject.put("title", "Select your bank");
                        PayU.availableBanks.put(0, banksObject);
                    }

                }

                if (ibiboCodes.has("emi")) {
                    // add empty array
                    JSONObject emptyObject = new JSONObject();
                    emptyObject.put("emiCode", " ");
                    emptyObject.put("emiInterval", " ");
                    emptyObject.put("bankName", " ");
                    PayU.availableEmi.put(emptyObject);

                    Iterator<String> emiKeysIterator = ibiboCodes.getJSONObject("emi").keys();
                    if (PayU.enforcePayMethod == null || enforcePayMethodsSet.contains("Emi")) {
                        while (emiKeysIterator.hasNext()) {
                            String emiCode = (String) emiKeysIterator.next();
                            if (!dropCategoriesSet.contains(emiCode)) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("emiCode", emiCode);
                                jsonObject.put("emiInterval", ibiboCodes.getJSONObject("emi").getJSONObject(emiCode).get("title").toString());
                                jsonObject.put("bankName", ibiboCodes.getJSONObject("emi").getJSONObject(emiCode).get("bank").toString());
                                PayU.availableEmi.put(jsonObject);
                            }
                        }
                        emiPresent = true;
                    } else {
                        while (emiKeysIterator.hasNext()) {
                            String emiCode = (String) emiKeysIterator.next();
                            if (enforcePayMethodsSet.contains(emiCode) && !dropCategoriesSet.contains(emiCode)) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("emiCode", emiCode);
                                jsonObject.put("emiInterval", ibiboCodes.getJSONObject("emi").getJSONObject(emiCode).get("title").toString());
                                jsonObject.put("bankName", ibiboCodes.getJSONObject("emi").getJSONObject(emiCode).get("bank").toString());
                                PayU.availableEmi.put(jsonObject);
                                emiPresent = true;
                            }
                        }
                    }


                    PayU.availableEmi = jsonArraySort(PayU.availableEmi, "bankName");
                    JSONObject emiObject = new JSONObject();
                    emiObject.put("emiCode", "default");
                    emiObject.put("emiInterval", "Select Duration");
                    emiObject.put("bankName", "Select Bank");
                    PayU.availableEmi.put(0, emiObject);
                }


                // cash card available, enforce pay method

                if (ibiboCodes.has("cashcard")) {
                    Iterator<String> cashCardIterator = ibiboCodes.getJSONObject("cashcard").keys();
                    // for default options

                    JSONObject emptyEmiObject = new JSONObject();
                    emptyEmiObject.put("code", " ");
                    emptyEmiObject.put("name", " ");
                    PayU.availableCashCards.put(emptyEmiObject);

                    if (PayU.enforcePayMethod == null || enforcePayMethodsSet.contains("cashcard")) {
                        while (cashCardIterator.hasNext()) {
                            String cashCardCode = cashCardIterator.next();
                            if (!dropCategoriesSet.contains(cashCardCode)) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("code", cashCardCode);
                                jsonObject.put("name", ibiboCodes.getJSONObject("cashcard").getJSONObject(cashCardCode).getString("title"));
                                PayU.availableCashCards.put(jsonObject);
                            }
                        }
                        cashCardPresent = true;
                    } else {
                        while (cashCardIterator.hasNext()) {
                            String cashCardCode = cashCardIterator.next();
                            JSONObject jsonObject = new JSONObject();
                            if (enforcePayMethodsSet.contains(cashCardCode) && !dropCategoriesSet.contains(cashCardCode)) {
                                jsonObject.put("code", cashCardCode);
                                jsonObject.put("name", ibiboCodes.getJSONObject("cashcard").getJSONObject(cashCardCode).getString("title"));
                                PayU.availableCashCards.put(jsonObject);
                                cashCardPresent = true;
                            }
                        }
                    }

                    // for default select option

                    PayU.availableCashCards = jsonArraySort(PayU.availableCashCards, "name");
                    if(Constants.SET_DEFAULT_CASH_CARD){
                        JSONObject cashObject = new JSONObject();
                        cashObject.put("code", "default");
                        cashObject.put("name", "Select your cash card");
                        PayU.availableCashCards.put(0, cashObject);
                    }
                }

                if (ibiboCodes.has("creditcard")) {
                    //   available credit cardsy
                    Iterator<String> creditCardIterator = ibiboCodes.getJSONObject("creditcard").keys();
                    if (PayU.enforcePayMethod == null || enforcePayMethodsSet.contains("creditcard")) {
                        while (creditCardIterator.hasNext()) {
                            String creditCardCode = creditCardIterator.next();
                            if (!dropCategoriesSet.contains(creditCardCode)) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("code", creditCardCode);
                                jsonObject.put("name", ibiboCodes.getJSONObject("creditcard").getJSONObject(creditCardCode).getString("title"));
                                PayU.availableCreditCards.put(jsonObject);
                            }
                        }
                        ccPresent = true;
                    } else {
                        while (creditCardIterator.hasNext()) {
                            String creditCardCode = creditCardIterator.next();
                            if (enforcePayMethodsSet.contains(creditCardCode) && !dropCategoriesSet.contains(creditCardCode)) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("code", creditCardCode);
                                jsonObject.put("name", ibiboCodes.getJSONObject("creditcard").getJSONObject(creditCardCode).getString("title"));
                                PayU.availableCreditCards.put(jsonObject);
                                ccPresent = true;
                            }
                        }
                    }
//                        PayU.availableCreditCards = creditCardsAvailable;

                }

                if (ibiboCodes.has("debitcard")) {
                    // available debit cards

                    Iterator<String> debitCardIterator = ibiboCodes.getJSONObject("debitcard").keys();
                    if (PayU.enforcePayMethod == null || enforcePayMethodsSet.contains("debitcard")) {
                        while (debitCardIterator.hasNext()) {
                            String debitCardCode = debitCardIterator.next();
                            if (!dropCategoriesSet.contains(debitCardCode)) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("code", debitCardCode);
                                jsonObject.put("name", ibiboCodes.getJSONObject("debitcard").getJSONObject(debitCardCode).getString("title"));
                                PayU.availableDebitCards.put(jsonObject);
                            }
                        }
                        dcPresent = true;
                    } else {
                        while (debitCardIterator.hasNext()) {
                            String debitCardCode = debitCardIterator.next();
                            if (enforcePayMethodsSet.contains(debitCardCode) && !dropCategoriesSet.contains(debitCardCode)) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("code", debitCardCode);
                                jsonObject.put("name", ibiboCodes.getJSONObject("debitcard").getJSONObject(debitCardCode).getString("title"));
                                PayU.availableDebitCards.put(jsonObject);
                                dcPresent = true;
                            }
                        }
                    }
//                        PayU.availableDebitCards = debitCardsAvailable;

                }

                // lets find the available modes
                if(PayU.userCredentials != null){
                    PayU.availableModes.put("STORED_CARDS");
                }
                if (ccPresent || dcPresent)
                    PayU.availableModes.put("CC");
                //if (dcPresent)
                //modesAvailable.put("DC");
                if (nbPresent)
                    PayU.availableModes.put("NB");
                if (emiPresent)
                    PayU.availableModes.put("EMI");
                if (cashCardPresent)
                    PayU.availableModes.put("CASH");

                Iterator<String> modeKeysIterator = ibiboCodes.keys();
                if ((PayU.enforcePayMethod == null || enforcePayMethodsSet.contains("paisawallet")) && (PayU.dropCategory == null) || !dropCategoriesSet.contains("paisawallet")) {
                    while (modeKeysIterator.hasNext()) {
                        String option = modeKeysIterator.next();
                        if (option.contentEquals("paisawallet"))
                            PayU.availableModes.put("PAYU_MONEY");
                    }
                }

                // lets store the stored cards.

                if(userCards.length() > 0){
                    storeUserCards(userCards);
                }
                // set the time data fetched at
                PayU.dataFetchedAt = System.currentTimeMillis();


                return null;


            } catch (JSONException e) {
               localException = e.getMessage();
//                e.printStackTrace();
            }


        } else if(response.length() > 0 && lists[0].get(1).getValue().contentEquals(Constants.GET_VAS)){
            // here we find the issuing , net banking downtime.
            PayU.netBankingStatus = new HashMap<String, Integer>();
            if(response.has("netBankingStatus")){
                try {
                    JSONObject netBanking = response.getJSONObject("netBankingStatus");
                    Iterator<String> keysIterator = netBanking.keys();

                    while (keysIterator.hasNext()) {
                        String bankCode = (String) keysIterator.next();
                        PayU.netBankingStatus.put(bankCode, netBanking.getJSONObject(bankCode).getInt("up_status"));
                    }
                } catch (JSONException e) {
                    localException = e.getMessage();
//                    e.printStackTrace();
                }
            }
            if(response.has("issuingBankDownBins")){
                PayU.issuingBankDownBin = new JSONObject();
                try {
                    JSONArray issuingBank = response.getJSONArray("issuingBankDownBins");
                    for(int i = 0,length = issuingBank.length(); i < length ; i++){

                        if(issuingBank.getJSONObject(i).getInt("status") == 0){
                            for(int j = 0, binArrayLength = (issuingBank.getJSONObject(i).getJSONArray("bins_arr").length()); j < binArrayLength; j++){
                                PayU.issuingBankDownBin.put(issuingBank.getJSONObject(i).getJSONArray("bins_arr").getString(j), issuingBank.getJSONObject(i).getString("title"));
                            }
                        }
                    }
                } catch (JSONException e) {
                    localException = e.getMessage();
                }
                if(response.has("sbiMaesBins")){

                    try {
                        JSONArray sbiMaesBins = response.getJSONArray("sbiMaesBins");
                        for(int i = 0, length = sbiMaesBins.length(); i < length; i++){
                            if(!Cards.SBI_MAES_BIN.contains(sbiMaesBins.getString(i))){
                                Cards.SBI_MAES_BIN.add(sbiMaesBins.getString(i));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

        } else if(response.length() > 0 && lists[0].get(1).getValue().contentEquals(Constants.GET_USER_CARDS)){
            storeUserCards(response);
        }else if(response.length() > 0 && lists[0].get(1).getValue().contentEquals(Constants.OFFER_STATUS)){
            if(response.has("offer_key")){
                PayU.offerStatus = new JSONArray();
                PayU.offerStatus.put(response);
            }
        }

        if(response.has("msg")){
            try {
                return response.getString("msg");
            } catch (JSONException e) {
                localException = e.getMessage();
//                e.printStackTrace();
            }
        }
        // not null & no msg then just a json array. lets notify the user .

        return  localException.length() > 1 ? localException :"Data fetched successfully.";

    }

    private void storeUserCards(JSONObject userCards) {

        PayU.storedCards = new JSONArray();

        if (userCards.has("user_cards")) {
            try {
                JSONObject user_cards = userCards.getJSONObject("user_cards");
                Iterator<String> keysIterator = user_cards.keys();

                while (keysIterator.hasNext()) {
                    String cardToken = (String) keysIterator.next();
                    PayU.storedCards.put(user_cards.getJSONObject(cardToken));
                }
            } catch (JSONException e) {
               localException =  e.getMessage();
//                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(String responseMessage) {
        mResponseListener.onGetResponse(responseMessage);
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
                    localException = e.getMessage();
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
