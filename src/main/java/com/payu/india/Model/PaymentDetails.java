package com.payu.india.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by franklin on 5/26/15.
 * Simple Bean implementation of Payment details.
 * Payment details should be used only with {@link PayuResponse#creditCard}, {@link PayuResponse#debitCard}, {@link PayuResponse#cashCard}, {@link PayuResponse#ivr}, {@link PayuResponse#ivrdc}, {@link PayuResponse#paisaWallet}
 */
public class PaymentDetails implements Parcelable {
    public static final Parcelable.Creator<PaymentDetails> CREATOR
            = new Parcelable.Creator<PaymentDetails>() {
        public PaymentDetails createFromParcel(Parcel in) {
            return new PaymentDetails(in);
        }

        public PaymentDetails[] newArray(int size) {
            return new PaymentDetails[size];
        }
    };
    private String bankName;
    private String bankCode;
    private String bankId;
    private String pgId;

    public PaymentDetails() {
    }

    private PaymentDetails(Parcel in) {
        bankCode = in.readString();
        bankName = in.readString();
        bankId = in.readString();
        pgId = in.readString();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getPgId() {
        return pgId;
    }

    public void setPgId(String pgId) {
        this.pgId = pgId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bankCode);
        dest.writeString(bankName);
        dest.writeString(bankId);
        dest.writeString(pgId);
    }
}
