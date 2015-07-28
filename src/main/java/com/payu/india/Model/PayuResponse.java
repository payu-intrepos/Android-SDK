package com.payu.india.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by franklin on 5/26/15.
 * Simple Bean implementation of PayuResponse.
 * Everytime Task talks to payu server, the responses will be filled in {@link PayuResponse}
 * {@link PayuResponse#storedCards} is a collection (ArrayList) of {@link StoredCard}
 * {@link PayuResponse#emi} is a collection (ArrayList) of {@link Emi}
 * {@link PayuResponse#creditCard} is a collection (ArrayList) of {@link PaymentDetails}
 * {@link PayuResponse#debitCard} is a collection (ArrayList) of {@link PaymentDetails}
 * {@link PayuResponse#cashCard} is a collection (ArrayList) of {@link PaymentDetails}
 * {@link PayuResponse#netBanks} is a collection (ArrayList) of {@link PaymentDetails}
 * {@link PayuResponse#ivr} is a collection (ArrayList) of {@link PaymentDetails}
 * {@link PayuResponse#ivrdc} is a collection (ArrayList) of {@link PaymentDetails}
 * {@link PayuResponse#paisaWallet} is a collection (ArrayList) of {@link PaymentDetails}
 * {@link PayuResponse#responseStatus} is {@link PostData}
 * {@link PayuResponse#netBankingDownStatus} is a hashmap  key(string) - bank code, value(string) - status TODO set the status value in constants.
 * {@link PayuResponse#cardInformation} is  {@link CardInformation}
 * {@link PayuResponse#issuingBankStatus} is a hash map - key -(string) - card bin(first 6 digit of cardno), value({@link CardStatus}) - holds the name of the issuing bank and status.
 * {@link PayuResponse#payuOffer} is {@link PayuOffer}
 * {@link PayuResponse#transactionDetailsList} is collection of (ArrayList) of {@link TransactionDetails}
 */

public class PayuResponse implements Parcelable {

    public static final Creator<PayuResponse> CREATOR = new Creator<PayuResponse>() {
        @Override
        public PayuResponse createFromParcel(Parcel in) {
            return new PayuResponse(in);
        }

        @Override
        public PayuResponse[] newArray(int size) {
            return new PayuResponse[size];
        }
    };
    private ArrayList<StoredCard> storedCards;
    private ArrayList<Emi> emi;
    private ArrayList<PaymentDetails> creditCard;
    private ArrayList<PaymentDetails> debitCard;
    private ArrayList<PaymentDetails> netBanks;
    private ArrayList<PaymentDetails> cashCard;
    private ArrayList<PaymentDetails> ivr;
    private ArrayList<PaymentDetails> ivrdc;
    private ArrayList<PaymentDetails> paisaWallet;
    private PostData responseStatus;
    private CardInformation cardInformation;
    private HashMap<String, Integer> netBankingDownStatus;
    private HashMap<String, CardStatus> issuingBankStatus;
    private PayuOffer payuOffer;
    private ArrayList<TransactionDetails> transactionDetailsList;

    protected PayuResponse(Parcel in) {
        storedCards = in.createTypedArrayList(StoredCard.CREATOR);
        emi = in.createTypedArrayList(Emi.CREATOR);
        creditCard = in.createTypedArrayList(PaymentDetails.CREATOR);
        debitCard = in.createTypedArrayList(PaymentDetails.CREATOR);
        netBanks = in.createTypedArrayList(PaymentDetails.CREATOR);
        cashCard = in.createTypedArrayList(PaymentDetails.CREATOR);
        ivr = in.createTypedArrayList(PaymentDetails.CREATOR);
        ivrdc = in.createTypedArrayList(PaymentDetails.CREATOR);
        paisaWallet = in.createTypedArrayList(PaymentDetails.CREATOR);
        responseStatus = in.readParcelable(PostData.class.getClassLoader());
        cardInformation = in.readParcelable(CardInformation.class.getClassLoader());
        payuOffer = in.readParcelable(PayuOffer.class.getClassLoader());
        transactionDetailsList = in.createTypedArrayList(TransactionDetails.CREATOR);
    }

    public PayuResponse() {
    }

    public ArrayList<StoredCard> getStoredCards() {
        return storedCards;
    }

    public void setStoredCards(ArrayList<StoredCard> storedCards) {
        this.storedCards = storedCards;
    }

    public ArrayList<Emi> getEmi() {
        return emi;
    }

    public void setEmi(ArrayList<Emi> emi) {
        this.emi = emi;
    }

    public ArrayList<PaymentDetails> getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(ArrayList<PaymentDetails> creditCard) {
        this.creditCard = creditCard;
    }

    public ArrayList<PaymentDetails> getDebitCard() {
        return debitCard;
    }

    public void setDebitCard(ArrayList<PaymentDetails> debitCard) {
        this.debitCard = debitCard;
    }

    public ArrayList<PaymentDetails> getNetBanks() {
        return netBanks;
    }

    public void setNetBanks(ArrayList<PaymentDetails> netBanks) {
        this.netBanks = netBanks;
    }

    public ArrayList<PaymentDetails> getCashCard() {
        return cashCard;
    }

    public void setCashCard(ArrayList<PaymentDetails> cashCard) {
        this.cashCard = cashCard;
    }

    public ArrayList<PaymentDetails> getIvr() {
        return ivr;
    }

    public void setIvr(ArrayList<PaymentDetails> ivr) {
        this.ivr = ivr;
    }

    public ArrayList<PaymentDetails> getIvrdc() {
        return ivrdc;
    }

    public void setIvrdc(ArrayList<PaymentDetails> ivrdc) {
        this.ivrdc = ivrdc;
    }

    public ArrayList<PaymentDetails> getPaisaWallet() {
        return paisaWallet;
    }

    public void setPaisaWallet(ArrayList<PaymentDetails> paisaWallet) {
        this.paisaWallet = paisaWallet;
    }

    public PostData getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(PostData responseStatus) {
        this.responseStatus = responseStatus;
    }

    public CardInformation getCardInformation() {
        return cardInformation;
    }

    public void setCardInformation(CardInformation cardInformation) {
        this.cardInformation = cardInformation;
    }

    public HashMap<String, Integer> getNetBankingDownStatus() {
        return netBankingDownStatus;
    }

    public void setNetBankingDownStatus(HashMap<String, Integer> netBankingDownStatus) {
        this.netBankingDownStatus = netBankingDownStatus;
    }

    public HashMap<String, CardStatus> getIssuingBankStatus() {
        return issuingBankStatus;
    }

    public void setIssuingBankStatus(HashMap<String, CardStatus> issuingBankStatus) {
        this.issuingBankStatus = issuingBankStatus;
    }

    public PayuOffer getPayuOffer() {
        return payuOffer;
    }

    public void setPayuOffer(PayuOffer payuOffer) {
        this.payuOffer = payuOffer;
    }

    public ArrayList<TransactionDetails> getTransactionDetailsList() {
        return transactionDetailsList;
    }

    public void setTransactionDetailsList(ArrayList<TransactionDetails> transactionDetailsList) {
        this.transactionDetailsList = transactionDetailsList;
    }

    // to avoid null pointer exception lets define isAvailable method.
    public Boolean isStoredCardsAvailable() {
        if (this.storedCards != null && this.storedCards.size() > 0)
            return true;
        return false;
    }

    public Boolean isEmiAvailable() {
        if (this.emi != null && this.emi.size() > 0)
            return true;
        return false;
    }

    public Boolean isCreditCardAvailable() {
        if (this.creditCard != null && this.creditCard.size() > 0)
            return true;
        return false;
    }

    public Boolean isDebitCardAvailable() {
        if (this.debitCard != null && this.debitCard.size() > 0)
            return true;
        return false;
    }

    public Boolean isNetBanksAvailable() {
        if (this.netBanks != null && this.netBanks.size() > 0)
            return true;
        return false;
    }

    public Boolean isCashCardAvailable() {
        if (this.cashCard != null && this.cashCard.size() > 0)
            return true;
        return false;
    }

    public Boolean isIVRAvailable() {
        if (this.ivr != null && this.ivr.size() > 0)
            return true;
        return false;
    }

    public Boolean isIVRDCAvailable() {
        if (this.ivrdc != null && this.ivrdc.size() > 0)
            return true;
        return false;
    }

    public Boolean isPaisaWalletAvailable() {
        if (this.paisaWallet != null && this.paisaWallet.size() > 0)
            return true;
        return false;
    }

    public Boolean isResponseAvailable() {
        if (this.responseStatus != null)
            return true;
        return false;
    }

    public Boolean isCardInformationAvailable() {
        if (this.cardInformation != null)
            return true;
        return false;
    }

    public Boolean isNetBankingStatusAvailable() {
        if (this.netBankingDownStatus != null)
            return true;
        return false;
    }

    public Boolean isIssuingBankStatusAvailable() {
        if (this.issuingBankStatus != null)
            return true;
        return false;
    }

    public Boolean isPayuOfferAvailable() {
        if (this.payuOffer != null)
            return true;
        return false;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(storedCards);
        dest.writeTypedList(emi);
        dest.writeTypedList(creditCard);
        dest.writeTypedList(debitCard);
        dest.writeTypedList(netBanks);
        dest.writeTypedList(cashCard);
        dest.writeTypedList(ivr);
        dest.writeTypedList(ivrdc);
        dest.writeTypedList(paisaWallet);
        dest.writeParcelable(responseStatus, flags);
        dest.writeParcelable(cardInformation, flags);
        dest.writeParcelable(payuOffer, flags);
        dest.writeTypedList(transactionDetailsList);
    }
}
