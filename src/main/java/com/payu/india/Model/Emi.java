package com.payu.india.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.payu.india.PostParams.EmiPostParams;

/**
 * Created by franklin on 5/26/15.
 * Simple Bean of Emi card,
 * Since its parcelable you can pass it through intent with out any difficulty.
 * When app tries to make payment it should get the post params form {@link EmiPostParams#getEmiPostParams()}
 */
public class Emi implements Parcelable {

    public static final Parcelable.Creator<Emi> CREATOR
            = new Parcelable.Creator<Emi>() {
        public Emi createFromParcel(Parcel in) {
            return new Emi(in);
        }

        public Emi[] newArray(int size) {
            return new Emi[size];
        }
    };
    private String bankName;
    private String bankCode;
    private String bankTitle;
    private String pgId;
    private String bankId;

    public Emi() {
    }

    private Emi(Parcel in) {
        bankCode = in.readString();
        bankName = in.readString();
        bankTitle = in.readString();
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

    public String getBankTitle() {
        return bankTitle;
    }

    public void setBankTitle(String bankTitle) {
        this.bankTitle = bankTitle;
    }

    public String getPgId() {
        return pgId;
    }

    public void setPgId(String pgId) {
        this.pgId = pgId;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bankCode);
        dest.writeString(bankName);
        dest.writeString(bankTitle);
        dest.writeString(bankId);
        dest.writeString(pgId);
    }
}
