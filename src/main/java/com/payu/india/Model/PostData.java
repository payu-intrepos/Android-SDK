package com.payu.india.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by franklin on 5/30/15.
 * Simple Bean implementation of PostData
 * Every post param should be given in the form of PostData
 * {@link PostData#status} is the Status of the post data it can either be {@link com.payu.india.Payu.PayuConstants#ERROR} or {@link com.payu.india.Payu.PayuConstants#SUCCESS}
 * {@link PostData#code} is the error code, {@link com.payu.india.Payu.PayuErrors#NO_ERROR} in case of no error
 * {@link PostData#result} should have the final content. in case of error it should give the proper error message corresponding to error code.
 */
public class PostData implements Parcelable {
    public static final Parcelable.Creator<PostData> CREATOR
            = new Parcelable.Creator<PostData>() {
        public PostData createFromParcel(Parcel in) {
            return new PostData(in);
        }

        public PostData[] newArray(int size) {
            return new PostData[size];
        }
    };
    ;
    private String status;
    private String result;
    private int code;

    ;

    public PostData() {
    }

    private PostData(Parcel in) {
        status = in.readString();
        code = in.readInt();
        result = in.readString();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(result);
        dest.writeString(status);
        dest.writeInt(code);
    }
}
