package com.payu.india.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by franklin on 7/2/15.
 * simple bean implementation of card status.
 * it holds the issuing bank's up/down status.
 * To avail this facility app should make an api using {@link com.payu.india.Tasks.ValueAddedServiceTask}
 * app will get response at {@link PayuResponse#issuingBankStatus}
 * TODO assign values for the following in {@link com.payu.india.Payu.PayuConstants}
 * UP
 * Down
 * Partial down.
 */
public class CardStatus implements Parcelable {

    public static final Creator<CardStatus> CREATOR = new Creator<CardStatus>() {
        @Override
        public CardStatus createFromParcel(Parcel in) {
            return new CardStatus(in);
        }

        @Override
        public CardStatus[] newArray(int size) {
            return new CardStatus[size];
        }
    };
    private String bankName;
    private int statusCode;

    public CardStatus() {
    }

    protected CardStatus(Parcel in) {
        bankName = in.readString();
        statusCode = in.readInt();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bankName);
        dest.writeInt(statusCode);
    }
}
