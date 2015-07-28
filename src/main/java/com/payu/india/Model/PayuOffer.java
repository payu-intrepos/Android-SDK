package com.payu.india.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by franklin on 7/3/15.
 * Simple bean implementation of payu offers.
 * should be used with {@link com.payu.india.Interfaces.GetOfferStatusApiListener}
 */
public class PayuOffer implements Parcelable {

    public static final Creator<PayuOffer> CREATOR = new Creator<PayuOffer>() {
        @Override
        public PayuOffer createFromParcel(Parcel in) {
            return new PayuOffer(in);
        }

        @Override
        public PayuOffer[] newArray(int size) {
            return new PayuOffer[size];
        }
    };
    private String status;
    private String msg;
    private String errorCode;
    private String offerKey;
    private String offerType;
    private String offerAvailedCount;
    private String offerRemainingCount;
    private String discount;
    private String category;

    protected PayuOffer(Parcel in) {
        status = in.readString();
        msg = in.readString();
        errorCode = in.readString();
        offerKey = in.readString();
        offerType = in.readString();
        offerAvailedCount = in.readString();
        offerRemainingCount = in.readString();
        discount = in.readString();
        category = in.readString();
    }

    public PayuOffer() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getOfferKey() {
        return offerKey;
    }

    public void setOfferKey(String offerKey) {
        this.offerKey = offerKey;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getOfferAvailedCount() {
        return offerAvailedCount;
    }

    public void setOfferAvailedCount(String offerAvailedCount) {
        this.offerAvailedCount = offerAvailedCount;
    }

    public String getOfferRemainingCount() {
        return offerRemainingCount;
    }

    public void setOfferRemainingCount(String offerRemainingCount) {
        this.offerRemainingCount = offerRemainingCount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(msg);
        dest.writeString(errorCode);
        dest.writeString(offerKey);
        dest.writeString(offerType);
        dest.writeString(offerAvailedCount);
        dest.writeString(offerRemainingCount);
        dest.writeString(discount);
        dest.writeString(category);
    }
}
