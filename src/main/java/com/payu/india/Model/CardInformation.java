package com.payu.india.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by franklin on 7/1/15.
 * Simple bean implementation of card information
 * When the app uses {@link com.payu.india.Tasks.GetCardInformationTask} the response will be passed as {@link CardInformation}
 * along with the {@link PostData} in {@link PayuResponse}
 */
public class CardInformation implements Parcelable {

    public static final Creator<CardInformation> CREATOR = new Creator<CardInformation>() {
        @Override
        public CardInformation createFromParcel(Parcel in) {
            return new CardInformation(in);
        }

        @Override
        public CardInformation[] newArray(int size) {
            return new CardInformation[size];
        }
    };
    private Boolean isDomestic;
    private String issuingBank;
    private String cardCategory;
    private String cardType;

    public CardInformation() {
    }

    protected CardInformation(Parcel in) {
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }

    public Boolean getIsDomestic() {
        return isDomestic;
    }

    public void setIsDomestic(Boolean isDomestic) {
        this.isDomestic = isDomestic;
    }

    public String getCardCategory() {
        return cardCategory;
    }

    public void setCardCategory(String cardCategory) {
        this.cardCategory = cardCategory;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
