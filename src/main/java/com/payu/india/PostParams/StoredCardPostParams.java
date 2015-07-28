package com.payu.india.PostParams;

import com.payu.india.Model.PaymentDefaultParams;
import com.payu.india.Model.PostData;
import com.payu.india.Model.StoredCard;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;

/**
 * Created by franklin on 6/18/15.
 * To make a Payment using stored credit/debit card user need to call {@link StoredCardPostParams#getPaymentDefaultPostParams()}
 * {@link StoredCardPostParams#StoredCardPostParams(PaymentDefaultParams, StoredCard)}  requires {@link PaymentDefaultParams} and {@link StoredCard}
 * {@link PaymentDefaultParams }includes the basic payment mandatory default params
 * {@link StoredCard} is the user card object you get from card adapter.
 */
public class StoredCardPostParams extends PaymentDefaultPostParams {

    private StoredCard storedCard;
    private PaymentDefaultParams paymentDefaultParams;
    private StringBuilder post;

    public StoredCardPostParams(PaymentDefaultParams paymentDefaultParams, StoredCard storedCard) {
        super(paymentDefaultParams);
        this.paymentDefaultParams = paymentDefaultParams;
        this.storedCard = storedCard;
    }

    public PostData getstoredCardPostParams() {
        PostData postData = getPaymentDefaultPostParams();
        if (postData.getCode() == PayuErrors.NO_ERROR) {
            post = new StringBuilder();
            post.append(postData.getResult());
            if (this.storedCard != null) {
                post.append(concatParams(PayuConstants.PG, PayuConstants.CC));
                post.append(concatParams(PayuConstants.BANK_CODE, PayuConstants.CC));
                if (this.storedCard.getCardToken() != null) {
                    // its stored card payment! we gotta verify user credentials
                    if (this.paymentDefaultParams.getUserCredentials() != null && this.paymentDefaultParams.getUserCredentials().contains(this.paymentDefaultParams.getKey() + ":")) {
                        post.append(concatParams(PayuConstants.USER_CREDENTIALS, this.paymentDefaultParams.getUserCredentials()));
                        post.append(concatParams(PayuConstants.STORE_CARD_TOKEN, this.storedCard.getCardToken()));
                        if (this.storedCard.getCardBin() != null) {
                            // here we have the card bin we can validate cvv, expiry
                            if (!getIssuer(this.storedCard.getCardBin()).contentEquals(PayuConstants.SMAE)) {
                                if (this.storedCard.getCvv() == null) {
                                    return getReturnData(PayuErrors.INVALID_CVV);
                                }
                                if (!validateExpiry(Integer.parseInt(this.storedCard.getExpiryMonth()), Integer.parseInt(this.storedCard.getExpiryYear()))) {
                                    return getReturnData(PayuErrors.CARD_EXPIRED);
                                }
                            }
                        }
                        post.append(this.storedCard.getCvv() != null ? concatParams(PayuConstants.C_CVV, this.storedCard.getCvv()) : concatParams(PayuConstants.C_CVV, "123")); // its not necessary that all the stored cards should have a cvv && we dont have card number so no validation.
                        post.append(this.storedCard.getExpiryMonth() != null ? concatParams(PayuConstants.CC_EXP_MON, this.storedCard.getExpiryMonth()) : concatParams(PayuConstants.CC_EXP_MON, "12"));
                        post.append(this.storedCard.getExpiryYear() != null ? concatParams(PayuConstants.CC_EXP_YR, this.storedCard.getExpiryYear()) : concatParams(PayuConstants.CC_EXP_MON, "2080"));

                        post.append(this.storedCard.getNameOnCard() == null ? concatParams(PayuConstants.CC_NAME, "PayuUser") : concatParams(PayuConstants.CC_NAME, storedCard.getNameOnCard()));
                        // okey we have data
                        return getReturnData(PayuErrors.NO_ERROR, PayuConstants.SUCCESS, trimAmpersand(post.toString()));
                    } else {
                        return getReturnData(PayuErrors.USER_CREDENTIALS_NOT_FOUND_EXCEPTION, PayuErrors.USER_CREDENTIALS_MISSING);
                    }
                } else {
                    return getReturnData(PayuErrors.MISSING_PARAMETER_EXCEPTION, PayuErrors.CARD_TOKEN_MISSING);
                }
            } else {
                return getReturnData(PayuErrors.INVALID_CARD_DETAILS);
            }
        } else {
            return postData;
        }
    }

}
