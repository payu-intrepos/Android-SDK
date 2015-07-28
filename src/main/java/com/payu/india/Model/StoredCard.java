package com.payu.india.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by franklin on 5/26/15.
 * Simple bean implementation of user Card
 * When the api get user card, all the user cards should be dumped in {@link PayuResponse#storedCards}
 */
public class StoredCard implements Parcelable {  // GC Rename this to stored card..

    public static final Creator<StoredCard> CREATOR = new Creator<StoredCard>() {
        @Override
        public StoredCard createFromParcel(Parcel in) {
            return new StoredCard(in);
        }

        @Override
        public StoredCard[] newArray(int size) {
            return new StoredCard[size];
        }
    };
    private String nameOnCard;
    private String cardName;
    private String expiryYear;
    private String expiryMonth;
    private String cardType;
    private String cardToken;
    private Boolean isExpired;
    private String cardMode;
    private String maskedCardNumber;
    private String cardBrand;
    private String cardBin;
    private String isDomestic;
    private String cvv;

    protected StoredCard(Parcel in) {
        nameOnCard = in.readString();
        cardName = in.readString();
        expiryYear = in.readString();
        expiryMonth = in.readString();
        cardType = in.readString();
        cardToken = in.readString();
        cardMode = in.readString();
        maskedCardNumber = in.readString();
        cardBrand = in.readString();
        cardBin = in.readString();
        isDomestic = in.readString();
        cvv = in.readString();
    }

    public StoredCard() {
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public Boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Boolean isExpired) {
        this.isExpired = isExpired;
    }

    public String getCardMode() {
        return cardMode;
    }

    public void setCardMode(String cardMode) {
        this.cardMode = cardMode;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public void setMaskedCardNumber(String maskedCardNumber) {
        this.maskedCardNumber = maskedCardNumber;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getCardBin() {
        return cardBin;
    }

    public void setCardBin(String cardBin) {
        this.cardBin = cardBin;
    }

    public String getIsDomestic() {
        return isDomestic;
    }

    public void setIsDomestic(String isDomestic) {
        this.isDomestic = isDomestic;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nameOnCard);
        dest.writeString(cardName);
        dest.writeString(expiryYear);
        dest.writeString(expiryMonth);
        dest.writeString(cardType);
        dest.writeString(cardToken);
        dest.writeString(cardMode);
        dest.writeString(maskedCardNumber);
        dest.writeString(cardBrand);
        dest.writeString(cardBin);
        dest.writeString(isDomestic);
        dest.writeString(cvv);
    }
}
