package com.payu.india.PostParams;

import com.payu.india.Model.MerchantWebService;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.india.Payu.PayuUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by franklin on 6/18/15.
 * We should support all the web services.
 * Every webservice require an Task and an Interface.
 * {@link MerchantWebService} is the bean which holds the information about the api
 * actually {@link MerchantWebService#getCommand()} defines what data client demands to server.
 */
public class MerchantWebServicePostParams extends PayuUtils {

    private MerchantWebService merchantWebService;

    private StringBuilder post;

    /**
     * Dont allow user to create empty objects.
     */
    private MerchantWebServicePostParams() {
    }

    /**
     * @param merchantWebService should have all the mandatory params like key, command var1, hash and the option params like var2, etc..
     */
    public MerchantWebServicePostParams(MerchantWebService merchantWebService) {
        this.merchantWebService = merchantWebService;
    }

    /**
     * we gotta check all webservices.
     * find the command then
     * Validate all the post params accordingly.
     *
     * @return postData
     */
    public PostData getMerchantWebServicePostParams() {
        post = new StringBuilder();
        int expiryMonth = 0;
        int expiryYear = 0;

        if (this.merchantWebService.getKey() == null) {
            return getReturnData(PayuErrors.MANDATORY_PARAM_KEY_IS_MISSING);
        } else {
            post.append(concatParams(PayuConstants.KEY, this.merchantWebService.getKey()));
        }
        if (this.merchantWebService.getHash() == null) {
            return getReturnData(PayuErrors.MANDATORY_PARAM_HASH_IS_MISSING);
        } else {
            post.append(concatParams(PayuConstants.HASH, this.merchantWebService.getHash()));
        }
        if (this.merchantWebService.getCommand() == null || !PayuConstants.COMMAND_SET.contains(this.merchantWebService.getCommand())) {
            return getReturnData(PayuErrors.MANDATORY_PARAM_COMMAND_IS_MISSING);
        } else { // now we have the command . .
            post.append(concatParams(PayuConstants.COMMAND, this.merchantWebService.getCommand()));
            switch (this.merchantWebService.getCommand()) { // TODO  add command to post variable
                case PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.USER_CREDENTIALS_OR_DEFAULT_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                    }
                    break;
                case PayuConstants.VAS_FOR_MOBILE_SDK:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.USE_DEFAULT);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                        post.append(this.merchantWebService.getVar2() == null ? concatParams(PayuConstants.VAR2, PayuConstants.DEFAULT) : concatParams(PayuConstants.VAR2, this.merchantWebService.getVar2()));
                        post.append(this.merchantWebService.getVar2() == null ? concatParams(PayuConstants.VAR3, PayuConstants.DEFAULT) : concatParams(PayuConstants.VAR3, this.merchantWebService.getVar3()));
                    }
                    break;
                case PayuConstants.GET_MERCHANT_IBIBO_CODES:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.USE_DEFAULT);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                    }
                    break;
                case PayuConstants.VERIFY_PAYMENT: // TODO validate verify payment options
                    if (this.merchantWebService.getVar1() == null) { // var 1 is list of txn id separated by pipe something like (12334|23234|234234|)
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.TRANSACTION_ID_MISSING + PayuErrors.MORE_THAN_ONE_TXNID);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                    }
                    break;
                case PayuConstants.CHECK_PAYMENT:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.MIHPAY_ID_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                    }
                    break;

                case PayuConstants.CANCEL_REFUND_TRANSACTION:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.MIHPAY_ID_MISSING);
                    } else if (this.merchantWebService.getVar2() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR2_IS_MISSING + " " + PayuConstants.VAR2 + PayuErrors.INVALID_TOKEN_ID);
                    } else if (this.merchantWebService.getVar3() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR3_IS_MISSING + " " + PayuConstants.VAR3 + PayuErrors.INVALID_AMOUNT_TO_REFUND);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                        post.append(concatParams(PayuConstants.VAR2, this.merchantWebService.getVar2()));
                        post.append(concatParams(PayuConstants.VAR3, this.merchantWebService.getVar3()));
                    }
                    break;
                case PayuConstants.CHECK_ACTION_STATUS:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.REQUEST_ID);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                    }
                    break;
                case PayuConstants.CAPTURE_TRANSACTION:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.MIHPAY_ID_MISSING);
                    } else if (this.merchantWebService.getVar2() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR2_IS_MISSING + " " + PayuErrors.INVALID_TOKEN_ID);

                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                        post.append(concatParams(PayuConstants.VAR2, this.merchantWebService.getVar2()));
                    }
                    break;
                case PayuConstants.UPDATE_REQUESTS:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.MIHPAY_ID_MISSING);
                    } else if (this.merchantWebService.getVar2() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR2_IS_MISSING + " " + PayuConstants.VAR2 + PayuErrors.REQUEST_ID);
                    } else if (this.merchantWebService.getVar3() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR3_IS_MISSING + " " + PayuConstants.VAR3 + PayuErrors.INVALID_BANK_REFERENCE_ID);
                    } else if (this.merchantWebService.getVar4() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR4_IS_MISSING + " " + PayuConstants.VAR4 + PayuErrors.INVALID_AMOUNT);
                    } else if (this.merchantWebService.getVar5() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR5_IS_MISSING + " " + PayuConstants.VAR5 + PayuErrors.INVALID_ACTION);
                    } else if (this.merchantWebService.getVar5() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR6_IS_MISSING + " " + PayuConstants.VAR6 + PayuErrors.NEW_STATUS);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                        post.append(concatParams(PayuConstants.VAR2, this.merchantWebService.getVar2()));
                        post.append(concatParams(PayuConstants.VAR3, this.merchantWebService.getVar3()));
                        post.append(concatParams(PayuConstants.VAR4, this.merchantWebService.getVar4()));
                        post.append(concatParams(PayuConstants.VAR5, this.merchantWebService.getVar5()));
                        post.append(concatParams(PayuConstants.VAR6, this.merchantWebService.getVar6()));
                    }
                    break;
                case PayuConstants.COD_VERIFY:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.MIHPAY_ID_MISSING);
                    } else if (this.merchantWebService.getVar2() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR2_IS_MISSING + " " + PayuConstants.VAR2 + PayuErrors.INVALID_TOKEN_ID);
                    } else if (this.merchantWebService.getVar3() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR3_IS_MISSING + " " + PayuConstants.VAR3 + PayuErrors.INVALID_AMOUNT);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                        post.append(concatParams(PayuConstants.VAR2, this.merchantWebService.getVar2()));
                        post.append(concatParams(PayuConstants.VAR3, this.merchantWebService.getVar3()));
                    }
                    break;
                case PayuConstants.COD_CANCEL:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.MIHPAY_ID_MISSING);
                    } else if (this.merchantWebService.getVar2() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR2_IS_MISSING + " " + PayuConstants.VAR2 + PayuErrors.INVALID_TOKEN_ID);
                    } else if (this.merchantWebService.getVar3() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR3_IS_MISSING + " " + PayuConstants.VAR3 + PayuErrors.INVALID_AMOUNT);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                        post.append(concatParams(PayuConstants.VAR2, this.merchantWebService.getVar2()));
                        post.append(concatParams(PayuConstants.VAR3, this.merchantWebService.getVar3()));
                    }
                    break;
                case PayuConstants.COD_SETTLED:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.MIHPAY_ID_MISSING);
                    } else if (this.merchantWebService.getVar2() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR2_IS_MISSING + " " + PayuConstants.VAR2 + PayuErrors.INVALID_TOKEN_ID);
                    } else if (this.merchantWebService.getVar3() == null) {
                        return getReturnData(PayuErrors.MISSING_PARAMETER_EXCEPTION + " " + PayuConstants.VAR3 + PayuErrors.INVALID_AMOUNT);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                        post.append(concatParams(PayuConstants.VAR2, this.merchantWebService.getVar2()));
                        post.append(concatParams(PayuConstants.VAR3, this.merchantWebService.getVar3()));
                    }
                    break;
                case PayuConstants.GET_TDR:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.MIHPAY_ID_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                    }
                    break;
                case PayuConstants.UDF_UPDATE:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.TRANSACTION_ID_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                        post.append(this.merchantWebService.getVar2() == null ? "" : concatParams(PayuConstants.VAR2, this.merchantWebService.getVar2()));
                        post.append(this.merchantWebService.getVar3() == null ? "" : concatParams(PayuConstants.VAR3, this.merchantWebService.getVar3()));
                        post.append(this.merchantWebService.getVar4() == null ? "" : concatParams(PayuConstants.VAR4, this.merchantWebService.getVar4()));
                        post.append(this.merchantWebService.getVar5() == null ? "" : concatParams(PayuConstants.VAR5, this.merchantWebService.getVar5()));
                        post.append(this.merchantWebService.getVar6() == null ? "" : concatParams(PayuConstants.VAR6, this.merchantWebService.getVar6()));
                    }
                case PayuConstants.CREATE_INVOICE:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING); // TODO add more messages!
                    } else {
                        try {
                            JSONObject invoiceObject = new JSONObject(this.merchantWebService.getVar1());
                            String invoiceMandatoryKeys[] = {PayuConstants.AMOUNT, PayuConstants.TXNID, PayuConstants.PRODUCT_INFO, PayuConstants.FIRST_NAME, PayuConstants.EMAIL, PayuConstants.PHONE};
//                            String invoiceOptionalKeys[] = {"address1", "city", "state", "country", "zipcode", "template_id", "validation_period", "send_email_now"};
                            for (String key : invoiceMandatoryKeys) { // TODO ignore the mandatory params
                                if (invoiceObject.getString(key) == null || invoiceObject.getString(key).length() < 1) {
                                    return getReturnData("Mandatory param " + key + " is missing for creating an Invoice");
                                } else {
                                    switch (key) {
                                        case PayuConstants.AMOUNT:
                                            try {
                                                Double.parseDouble(invoiceObject.getString(key));
                                            } catch (NumberFormatException e) {
                                                return getReturnData(PayuErrors.INVALID_AMOUNT_EXCEPTION, PayuErrors.INVALID_AMOUNT);
                                            }
                                            break;
                                        case PayuConstants.TXNID: // TODO validate TXNID
                                            break;
                                        case PayuConstants.PRODUCT_INFO: // TODO validate production info
                                            break;
                                        case PayuConstants.FIRST_NAME: // TODO validate First Name
                                            break;
                                        case PayuConstants.EMAIL: // TODO validate email
                                            break;
                                        case PayuConstants.PHONE: // TODO validate Phone
                                            break;
                                    }
                                }
                            }
                            post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                        } catch (JSONException e) {
                            return getReturnData(PayuErrors.JSON_EXCEPTION, PayuConstants.VAR1 + " " + PayuErrors.INVALID_JSON);
                        }
                    }
                    break;
                case PayuConstants.CHECK_OFFER_STATUS:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.OFFER_KEY_MISSING);
                    } else if (this.merchantWebService.getVar5() == null || !validateCardNumber(this.merchantWebService.getVar5())) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR5_IS_MISSING + " " + PayuConstants.VAR5 + PayuErrors.INVALID_CARD_NUMBER);
                    } else {
                        // good to go
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                        post.append(this.merchantWebService.getVar2() == null ? "" : concatParams(PayuConstants.VAR2, this.merchantWebService.getVar2()));
                        post.append(this.merchantWebService.getVar3() == null ? "" : concatParams(PayuConstants.VAR3, this.merchantWebService.getVar3()));
                        post.append(this.merchantWebService.getVar4() == null ? "" : concatParams(PayuConstants.VAR4, this.merchantWebService.getVar4()));
                        post.append(concatParams(PayuConstants.VAR5, this.merchantWebService.getVar5()));
                        post.append(this.merchantWebService.getVar6() == null ? "" : concatParams(PayuConstants.VAR6, this.merchantWebService.getVar5()));
                    }
                    break;
                case PayuConstants.GET_NETBANKING_STATUS:
                    if (this.merchantWebService.getVar1() == null || this.merchantWebService.getVar1().length() < 1) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.BANK_CODE_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                    }
                    break;
                case PayuConstants.GET_ISSUING_BANK_STATUS:
                    if (this.merchantWebService.getVar1() == null || this.merchantWebService.getVar1().length() != 6) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.CARD_BIN_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                    }
                    break;
                case PayuConstants.GET_TRANSACTION_DETAILS:
                    if (this.merchantWebService.getVar1() == null) { // TODO add validation for stating date
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.FROM_DATE_MISSING);
                    } else if (this.merchantWebService.getVar2() == null) { // TODO add validation for ending
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR2_IS_MISSING + " " + PayuConstants.VAR2 + PayuErrors.END_DATE_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                        post.append(concatParams(PayuConstants.VAR2, this.merchantWebService.getVar2()));
                    }
                    break;
                case PayuConstants.GET_TRANSACTION_INFO:
                    if (this.merchantWebService.getVar1() == null) { // TODO add validation for stating date with time
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.FROM_DATE_TIME_MISSING);
                    } else if (this.merchantWebService.getVar2() == null) { // TODO add validation for ending date with time
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR2_IS_MISSING + " " + PayuConstants.VAR2 + PayuErrors.END_DATE_TIME_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                        post.append(concatParams(PayuConstants.VAR2, this.merchantWebService.getVar2()));
                    }
                    break;
                case PayuConstants.CHECK_IS_DOMESTIC:
                    if (this.merchantWebService.getVar1() == null || this.merchantWebService.getVar1().length() < 6) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.CARD_BIN_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                    }
                    break;
                case PayuConstants.GET_USER_CARDS:
                    if (this.merchantWebService.getVar1() == null || !this.merchantWebService.getVar1().contains(this.merchantWebService.getKey() + ":")) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.USER_CREDENTIALS_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                    }
                    break;
                case PayuConstants.SAVE_USER_CARD: // TODO verify card type and mode with back end
                    if (this.merchantWebService.getVar1() == null || !this.merchantWebService.getVar1().contains(this.merchantWebService.getKey() + ":")) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.USER_CREDENTIALS_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, merchantWebService.getVar1()));
                    }
                    if (this.merchantWebService.getVar2() == null || this.merchantWebService.getVar2().length() < 1) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR2_IS_MISSING + " " + PayuConstants.VAR2 + PayuErrors.CARD_NAME_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR2, this.merchantWebService.getVar2()));
                    }
                    if (this.merchantWebService.getVar3() == null || !this.merchantWebService.getVar3().contentEquals(PayuConstants.CC)) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR3_IS_MISSING + " " + PayuConstants.VAR3 + PayuErrors.CARD_MODE_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR3, this.merchantWebService.getVar3()));
                    }
                    if (this.merchantWebService.getVar4() == null || !this.merchantWebService.getVar4().contentEquals(PayuConstants.CC)) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR4_IS_MISSING + " " + PayuConstants.VAR4 + PayuErrors.CARD_TYPE_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR4, this.merchantWebService.getVar4()));
                    }
                    if (this.merchantWebService.getVar5() == null || this.merchantWebService.getVar5().length() < 1) { // Name on card
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR5_IS_MISSING + " " + PayuConstants.VAR5 + PayuErrors.NAME_ON_CARD_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR5, this.merchantWebService.getVar5()));
                    }
                    if (this.merchantWebService.getVar6() == null || !validateCardNumber(this.merchantWebService.getVar6())) { // card number
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR6_IS_MISSING + " " + PayuConstants.VAR6 + PayuErrors.CARD_NUMBER_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR6, this.merchantWebService.getVar6()));
                    }

                    expiryMonth = 0;
                    expiryYear = 0;
                    try {
                        expiryMonth = Integer.parseInt(this.merchantWebService.getVar7());
                    } catch (NumberFormatException e) {
                        return getReturnData(PayuErrors.NUMBER_FORMAT_EXCEPTION, PayuErrors.INVALID_MONTH);
                    }
                    try {
                        expiryYear = Integer.parseInt(this.merchantWebService.getVar8());
                    } catch (NumberFormatException e) {
                        return getReturnData(PayuErrors.NUMBER_FORMAT_EXCEPTION, PayuErrors.INVALID_YEAR);
                    }
                    if (validateExpiry(expiryMonth, expiryYear)) {
                        post.append(concatParams(PayuConstants.VAR7, this.merchantWebService.getVar7()));
                        post.append(concatParams(PayuConstants.VAR8, this.merchantWebService.getVar8()));
                    } else {
                        return getReturnData(PayuErrors.CARD_EXPIRED_EXCEPTION, PayuErrors.CARD_EXPIRED);
                    }
                    break;
                case PayuConstants.EDIT_USER_CARD:
                    if (this.merchantWebService.getVar1() == null || !this.merchantWebService.getVar1().contains(this.merchantWebService.getKey() + ":")) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuConstants.VAR1 + PayuErrors.USER_CREDENTIALS_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                    }
                    if (this.merchantWebService.getVar2() == null || this.merchantWebService.getVar2().length() < 1) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR2_IS_MISSING + " " + PayuConstants.VAR2 + PayuErrors.CARD_TOKEN_MISSING);

                    } else {
                        post.append(concatParams(PayuConstants.VAR2, this.merchantWebService.getVar2()));
                    }
                    if (this.merchantWebService.getVar3() == null || this.merchantWebService.getVar3().length() < 1) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR3_IS_MISSING + " " + PayuConstants.VAR3 + PayuErrors.CARD_NAME_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR3, this.merchantWebService.getVar3()));
                    }
                    if (this.merchantWebService.getVar4() == null || !this.merchantWebService.getVar4().contentEquals(PayuConstants.CC)) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR4_IS_MISSING + " " + PayuConstants.VAR4 + PayuErrors.CARD_MODE_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR4, this.merchantWebService.getVar4()));
                    }
                    if (this.merchantWebService.getVar5() == null || !this.merchantWebService.getVar5().contentEquals(PayuConstants.CC)) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR5_IS_MISSING + " " + PayuConstants.VAR5 + PayuErrors.CARD_TYPE_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR5, this.merchantWebService.getVar5()));
                    }
                    if (this.merchantWebService.getVar6() == null || this.merchantWebService.getVar6().length() < 1) { // Name on card
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR6_IS_MISSING + " " + PayuConstants.VAR6 + PayuErrors.NAME_ON_CARD_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR6, this.merchantWebService.getVar6()));
                    }
                    if (this.merchantWebService.getVar7() == null || !validateCardNumber(this.merchantWebService.getVar7())) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR7_IS_MISSING + " " + PayuConstants.VAR7 + PayuErrors.CARD_NUMBER_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR7, this.merchantWebService.getVar7()));
                    }
                    expiryMonth = 0;
                    expiryYear = 0;
                    try {
                        expiryMonth = Integer.parseInt(this.merchantWebService.getVar8());
                    } catch (NumberFormatException e) {
                        return getReturnData(PayuErrors.NUMBER_FORMAT_EXCEPTION, PayuErrors.INVALID_MONTH);
                    }
                    try {
                        expiryYear = Integer.parseInt(this.merchantWebService.getVar9());
                    } catch (NumberFormatException e) {
                        return getReturnData(PayuErrors.NUMBER_FORMAT_EXCEPTION, PayuErrors.INVALID_YEAR);
                    }
                    if (validateExpiry(expiryMonth, expiryYear)) {
                        post.append(concatParams(PayuConstants.VAR8, this.merchantWebService.getVar8()));
                        post.append(concatParams(PayuConstants.VAR9, this.merchantWebService.getVar9()));
                    } else {
                        return getReturnData(PayuErrors.CARD_EXPIRED_EXCEPTION, PayuErrors.CARD_EXPIRED);
                    }
                    break;
                case PayuConstants.DELETE_USER_CARD:
                    if (this.merchantWebService.getVar1() == null && !this.merchantWebService.getVar1().contains(this.merchantWebService.getKey() + ":")) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING + " " + PayuErrors.USER_CREDENTIALS_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                    }
                    if (this.merchantWebService.getVar2() == null && this.merchantWebService.getVar2().length() < 2) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR2_IS_MISSING + " " + PayuConstants.VAR2 + PayuErrors.CARD_TOKEN_MISSING);
                    } else {
                        post.append(concatParams(PayuConstants.VAR2, this.merchantWebService.getVar2()));
                    }
                    break;

                case PayuConstants.MOBILE_HASH_TEST_WS:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING); // TODO give valid error message
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                    }
                    break;

                case PayuConstants.GET_HASHES:
                    if (this.merchantWebService.getVar1() == null) {
                        return getReturnData(PayuErrors.MANDATORY_PARAM_VAR1_IS_MISSING); // TODO give valid error message
                    } else {
                        post.append(concatParams(PayuConstants.VAR1, this.merchantWebService.getVar1()));
                    }
                    break;
            }
        }
        // lets remove the ampersand at the last
        return getReturnData(PayuErrors.NO_ERROR, PayuConstants.SUCCESS, trimAmpersand(post.toString()));
    }
}
