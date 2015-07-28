package com.payu.india.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.payu.india.PostParams.CCDCPostParams;

/**
 * Created by franklin on 6/13/15.
 * Simple Bean of credit card debit card,
 * Since its parcelable you can pass it through intent with out any difficult.
 * When app tries to make payment it should get the post params form {@link CCDCPostParams#getCCDCPostParams()}
 */
public class CCDCCard implements Parcelable {

    public static final Creator<CCDCCard> CREATOR = new Creator<CCDCCard>() {
        @Override
        public CCDCCard createFromParcel(Parcel in) {
            return new CCDCCard(in);
        }

        @Override
        public CCDCCard[] newArray(int size) {
            return new CCDCCard[size];
        }
    };
    private String cardNumber;
    private String cvv;
    private String expiryMonth;
    private String expiryYear;
    private String nameOnCard;
    private String cardName;

    protected CCDCCard(Parcel in) {
        cardNumber = in.readString();
        cvv = in.readString();
        expiryMonth = in.readString();
        expiryYear = in.readString();
        nameOnCard = in.readString();
        cardName = in.readString();
    }

    public CCDCCard() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cardNumber);
        dest.writeString(cvv);
        dest.writeString(expiryMonth);
        dest.writeString(expiryYear);
        dest.writeString(nameOnCard);
        dest.writeString(cardName);
    }
}
