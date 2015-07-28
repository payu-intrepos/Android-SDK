package com.payu.india.PostParams;

import com.payu.india.Model.CCDCCard;
import com.payu.india.Model.Emi;
import com.payu.india.Model.PaymentDefaultParams;
import com.payu.india.Model.PaymentDetails;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;

/**
 * Created by franklin on 6/18/15.
 * To make a Payment using emi user need to call {@link EmiPostParams#getEmiPostParams()}
 * {@link EmiPostParams#EmiPostParams(PaymentDefaultParams, Emi, CCDCCard)} requires {@link PaymentDefaultParams} {@link Emi} and {@link CCDCCard}
 * {@link PaymentDefaultParams }includes the basic payment mandatory default params
 * {@link Emi} is the selected Emi object from your adapter
 * {@link CCDCCard} is the card details
 */
public class EmiPostParams extends PaymentDefaultPostParams {
    private PaymentDefaultParams paymentDefaultParams;
    private Emi emi;
    private CCDCCard ccdcCard;
    private StringBuilder post;


    /**
     * @param paymentDefaultParams Should have the all the mandatory params such as key, amount, txnid, productinfo, firstname, email, udf1..5, and other optional fields if any.
     * @param emi                  emi is the selected emi object return from your adapter.
     * @param ccdcCard             CCDCCard is the card object which should have all the mandatory card details like card number, cvv, expmon, expyr, card name, name on card, etc...
     */
    public EmiPostParams(PaymentDefaultParams paymentDefaultParams, Emi emi, CCDCCard ccdcCard) {
        super(paymentDefaultParams);
        this.paymentDefaultParams = paymentDefaultParams;
        this.emi = emi;
        this.ccdcCard = ccdcCard;
    }

    /**
     * Once the {@link PaymentDefaultParams} and {@link PaymentDetails} are set then you can call {@link EmiPostParams#EmiPostParams(PaymentDefaultParams, Emi, CCDCCard)}
     * {@link EmiPostParams#getPaymentDefaultPostParams()}  will return {@link PostData}. All validation will be taken place there only.
     * if it passes everything {@link PostData#getResult()} will give the postData else it gives the error reason.
     * send the card information also form {@link EmiPostParams#ccdcCard}
     * Set pg as {@link PayuConstants#EMI} and set the bankcode form {@link Emi#getBankCode()}
     *
     * @return {@link PostData}
     */
    public PostData getEmiPostParams() {
        PostData postData = getPaymentDefaultPostParams();
        if (postData.getCode() == PayuErrors.NO_ERROR) {
            post = new StringBuilder();
            post.append(postData.getResult());
            if (this.emi != null && this.emi.getBankCode() != null && this.emi.getBankCode().length() > 1) {
                post.append(concatParams(PayuConstants.PG, PayuConstants.EMI));
                post.append(concatParams(PayuConstants.BANK_CODE, this.emi.getBankCode()));
                // lets validate card number
                if (validateCardNumber("" + this.ccdcCard.getCardNumber())) {
                    // okay its a valid card number
                    post.append(concatParams(PayuConstants.CC_NUM, "" + this.ccdcCard.getCardNumber()));
                    // if card number is not smae then validate cvv and expiry.
                    if (!getIssuer("" + this.ccdcCard.getCardNumber()).contentEquals(PayuConstants.SMAE)) {
                        if (validateCvv("" + this.ccdcCard.getCardNumber(), "" + this.ccdcCard.getCvv())) {
                            post.append(concatParams(PayuConstants.C_CVV, "" + this.ccdcCard.getCvv()));
                        } else {
                            return getReturnData(PayuErrors.INVALID_CVV_EXCEPTION, PayuErrors.INVALID_CVV);
                        }
                        try {
                            if (validateExpiry(Integer.parseInt(this.ccdcCard.getExpiryMonth()), Integer.parseInt(this.ccdcCard.getExpiryYear()))) {
                                post.append(concatParams(PayuConstants.CC_EXP_YR, "" + this.ccdcCard.getExpiryYear()));
                                post.append(concatParams(PayuConstants.CC_EXP_MON, "" + this.ccdcCard.getExpiryMonth()));
                            } else {
                                return getReturnData(PayuErrors.CARD_EXPIRED_EXCEPTION, PayuErrors.CARD_EXPIRED);
                            }
                        } catch (NumberFormatException e) {
                            return getReturnData(PayuErrors.NUMBER_FORMAT_EXCEPTION, PayuErrors.CARD_EXPIRED); // TODO add proper message cast exception
                        }
                    }

                    post.append(this.ccdcCard.getNameOnCard() == null ? concatParams(PayuConstants.CC_NAME, "PayuUser") : concatParams(PayuConstants.CC_NAME, ccdcCard.getNameOnCard()));
                    if (this.paymentDefaultParams.getStoreCard() == 1) {
                        if (this.paymentDefaultParams.getUserCredentials() != null && this.paymentDefaultParams.getUserCredentials().contains(this.paymentDefaultParams.getKey() + ":")) {
                            post.append(this.ccdcCard.getCardName() == null ? concatParams(PayuConstants.CARD_NAME, "PayuUser") : concatParams(PayuConstants.NAME_ON_CARD, ccdcCard.getCardName()));
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
                return getReturnData(PayuErrors.INVALID_EMI_DETAILS);
            }
        } else {
            return postData;
        }
    }
}
