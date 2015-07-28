package com.payu.india.PostParams;

import com.payu.india.Model.CCDCCard;
import com.payu.india.Model.PaymentDefaultParams;
import com.payu.india.Model.PaymentDetails;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;

/**
 * Created by franklin on 6/18/15.
 * To make a Payment using credit/debit card user need to call {@link CCDCPostParams#getCCDCPostParams()}
 * {@link CCDCPostParams#CCDCPostParams(PaymentDefaultParams, CCDCCard)} requires {@link PaymentDefaultParams} and {@link CCDCCard}
 * {@link PaymentDefaultParams }includes the basic payment mandatory default params
 * {@link CCDCCard} is the card object with all card details.
 */
public class CCDCPostParams extends PaymentDefaultPostParams {

    private PaymentDefaultParams paymentDefaultParams;
    private CCDCCard ccdcCard;
    private StringBuilder post;

    /**
     * @param paymentDefaultParams Should have the all the mandatory params such as key, amount, txnid, productinfo, firstname, email, udf1..5, and other optional fields if any.
     * @param ccdcCard             CCDCCard is the card object which should have all the mandatory card details like card number, cvv, expmon, expyr, card name, name on card, etc...
     */
    public CCDCPostParams(PaymentDefaultParams paymentDefaultParams, CCDCCard ccdcCard) {
        super(paymentDefaultParams);
        this.paymentDefaultParams = paymentDefaultParams;
        this.ccdcCard = ccdcCard;
    }

    /**
     * Once the {@link PaymentDefaultParams} and {@link PaymentDetails} are set then you can call {@link StoredCardPostParams#getstoredCardPostParams()}
     * {@link StoredCardPostParams#getPaymentDefaultPostParams()}  } will return {@link PostData}. All validation will be taken place there only.
     * if it passes everything {@link PostData#getResult()} will give the postData else it gives the error reason.
     * Set pg as CC and set the bank code as CC
     *
     * @return {@link PostData}
     */
    public PostData getCCDCPostParams() {
        // lets verify payment default params first
        PostData postData = getPaymentDefaultPostParams();
        if (postData.getCode() == PayuErrors.NO_ERROR) {
            post = new StringBuilder();
            post.append(postData.getResult());
            post.append(concatParams(PayuConstants.PG, PayuConstants.CC));
            post.append(concatParams(PayuConstants.BANK_CODE, PayuConstants.CC));
            if (validateCardNumber(this.ccdcCard.getCardNumber())) {
                // okay its a valid card number
                post.append(concatParams(PayuConstants.CC_NUM, this.ccdcCard.getCardNumber()));
                // if card number is not smae then validate cvv and expiry.
                if (!getIssuer(this.ccdcCard.getCardNumber()).contentEquals(PayuConstants.SMAE)) {
                    if (validateCvv(this.ccdcCard.getCardNumber(), this.ccdcCard.getCvv())) {
                        post.append(concatParams(PayuConstants.C_CVV, this.ccdcCard.getCvv()));
                    } else {
                        return getReturnData(PayuErrors.INVALID_CVV_EXCEPTION, PayuErrors.INVALID_CVV);
                    }
                    try {
                        if (validateExpiry(Integer.parseInt(this.ccdcCard.getExpiryMonth()), Integer.parseInt(this.ccdcCard.getExpiryYear()))) {
                            post.append(concatParams(PayuConstants.CC_EXP_YR, this.ccdcCard.getExpiryYear()));
                            post.append(concatParams(PayuConstants.CC_EXP_MON, this.ccdcCard.getExpiryMonth()));
                        } else {
                            return getReturnData(PayuErrors.CARD_EXPIRED_EXCEPTION, PayuErrors.CARD_EXPIRED);
                        }
                    } catch (NumberFormatException e) {
                        return getReturnData(PayuErrors.NUMBER_FORMAT_EXCEPTION, PayuErrors.CARD_EXPIRED); // todo wrong expiry format
                    }
                }else{
                    post.append(null != this.ccdcCard.getCvv() ? concatParams(PayuConstants.C_CVV, this.ccdcCard.getCvv()) : concatParams(PayuConstants.C_CVV, "123"));
                    post.append(null != this.ccdcCard.getExpiryMonth() ? concatParams(PayuConstants.CC_EXP_MON, this.ccdcCard.getExpiryMonth()) : concatParams(PayuConstants.CC_EXP_MON, "12"));
                    post.append(null != this.ccdcCard.getExpiryYear() ? concatParams(PayuConstants.CC_EXP_YR, this.ccdcCard.getExpiryYear()) : concatParams(PayuConstants.CC_EXP_YR, "2020"));

                }

                // if name on card is not give use default name on card as "PayuUser"
                String nameOnCard = null != this.ccdcCard.getNameOnCard() ? this.ccdcCard.getNameOnCard() : "PayuUser";
                // if card name is not given use name on card instead
                String cardName = null != this.ccdcCard.getCardName() ? this.ccdcCard.getCardName() : nameOnCard;
                post.append(concatParams(PayuConstants.CC_NAME, nameOnCard));
                if (this.paymentDefaultParams.getStoreCard() == 1) {
                    if (this.paymentDefaultParams.getUserCredentials() != null && this.paymentDefaultParams.getUserCredentials().contains(this.paymentDefaultParams.getKey() + ":")) {
                        post.append(concatParams(PayuConstants.CARD_NAME, cardName));
                        post.append(this.paymentDefaultParams.getUserCredentials() != null ? concatParams(PayuConstants.USER_CREDENTIALS, this.paymentDefaultParams.getUserCredentials()) : "");
                        post.append(this.paymentDefaultParams.getStoreCard() == 1 ? concatParams(PayuConstants.STORED_CARD, "" + this.paymentDefaultParams.getStoreCard()) : "");
                    } else {
                        return getReturnData(PayuErrors.USER_CREDENTIALS_NOT_FOUND);
                    }
                }
                // TODO add validation for store_card and user_credentials
                // thats it we can return post Data
                return getReturnData(PayuErrors.NO_ERROR, PayuConstants.SUCCESS, trimAmpersand(post.toString()));
            } else {
                return getReturnData(PayuErrors.INVALID_CARD_NUMBER_EXCEPTION, PayuErrors.INVALID_CARD_NUMBER);
            }
        } else {
            return postData;
        }
    }
}
