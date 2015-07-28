package com.payu.india.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;

/**
 * Created by franklin on 6/4/15.
 * Simple Bean implementations of Payu Config
 * Every payu Async tasks require Config object
 * {@link PayuConfig#data } Should be the final data, which will be posted on payu server
 * {@link PayuConfig#environment} Should be the environment.
 * {@link PayuConstants#MOBILE_STAGING_ENV}
 * {@link PayuConstants#PRODUCTION_ENV}
 * {@link PayuConstants#STAGING_ENV}
 * if none of the evn specified sdk will point to {@link PayuConstants#PRODUCTION_ENV}
 */
public class PayuConfig implements Parcelable {

    public static final Creator<PayuConfig> CREATOR = new Creator<PayuConfig>() {
        @Override
        public PayuConfig createFromParcel(Parcel in) {
            return new PayuConfig(in);
        }

        @Override
        public PayuConfig[] newArray(int size) {
            return new PayuConfig[size];
        }
    };
    private String data;
    private int environment;

    ;

    public PayuConfig() {
    }

    protected PayuConfig(Parcel in) {
        data = in.readString();
        environment = in.readInt();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getEnvironment() {
        return environment;
    }

    public void setEnvironment(int environment) {
        this.environment = environment;
    }

    public PostData getConfig() {
        PostData postData = new PostData();
        if (this.data == null || this.data.length() < 1) {
            postData.setCode(PayuErrors.MISSING_PARAMETER_EXCEPTION);
            postData.setResult(PayuConstants.ERROR);
            postData.setResult(PayuErrors.POST_DATA_MISSING);
            return postData;
        }

        switch (this.environment) {
            case PayuConstants.STAGING_ENV:
                this.environment = PayuConstants.STAGING_ENV;
                break;
            case PayuConstants.PRODUCTION_ENV:
                this.environment = PayuConstants.PRODUCTION_ENV;
                break;
            case PayuConstants.MOBILE_STAGING_ENV:
                this.environment = PayuConstants.MOBILE_STAGING_ENV;
                break;
            default:
                this.environment = PayuConstants.PRODUCTION_ENV;
                break;
        }

        postData.setCode(PayuErrors.NO_ERROR);
        postData.setStatus(PayuConstants.SUCCESS);
        postData.setResult(PayuConstants.SUCCESS);
        return postData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
        dest.writeInt(environment);
    }
}
