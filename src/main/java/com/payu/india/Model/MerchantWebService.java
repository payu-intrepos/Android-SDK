package com.payu.india.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by franklin on 6/1/15.
 * Simple Bean of Webservice params,
 * Since its parcelable you can pass it through intent with out any difficult.
 * When app tries to make payment it should passed along with {@link com.payu.india.PostParams.CCDCPostParams}, {@link com.payu.india.PostParams.EmiPostParams}, etc.
 */
public class MerchantWebService implements Parcelable {
    public static final Parcelable.Creator<MerchantWebService> CREATOR
            = new Parcelable.Creator<MerchantWebService>() {
        public MerchantWebService createFromParcel(Parcel in) {
            return new MerchantWebService(in);
        }

        public MerchantWebService[] newArray(int size) {
            return new MerchantWebService[size];
        }
    };
    private String command;
    private String key;
    private String hash;
    private String var1;
    private String var2;
    private String var3;
    private String var4;
    private String var5;
    private String var6;
    private String var7;
    private String var8;
    private String var9;
    private String var10;
    private String var11;
    private String var12;
    private String var13;
    private String var14;
    private String var15;

    public MerchantWebService() {
    }

    private MerchantWebService(Parcel in) {
        key = in.readString();
        command = in.readString();
        hash = in.readString();
        var1 = in.readString();
        var2 = in.readString();
        var3 = in.readString();
        var4 = in.readString();
        var5 = in.readString();
        var6 = in.readString();
        var7 = in.readString();
        var8 = in.readString();
        var9 = in.readString();
        var10 = in.readString();
        var11 = in.readString();
        var12 = in.readString();
        var13 = in.readString();
        var14 = in.readString();
        var15 = in.readString();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String comment) {
        this.command = comment;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getVar1() {
        return var1;
    }

    public void setVar1(String var1) {
        this.var1 = var1;
    }

    public String getVar2() {
        return var2;
    }

    public void setVar2(String var2) {
        this.var2 = var2;
    }

    public String getVar3() {
        return var3;
    }

    public void setVar3(String var3) {
        this.var3 = var3;
    }

    public String getVar4() {
        return var4;
    }

    public void setVar4(String var4) {
        this.var4 = var4;
    }

    public String getVar5() {
        return var5;
    }

    public void setVar5(String var5) {
        this.var5 = var5;
    }

    public String getVar6() {
        return var6;
    }

    public void setVar6(String var6) {
        this.var6 = var6;
    }

    public String getVar7() {
        return var7;
    }

    public void setVar7(String var7) {
        this.var7 = var7;
    }

    public String getVar8() {
        return var8;
    }

    public void setVar8(String var8) {
        this.var8 = var8;
    }

    public String getVar9() {
        return var9;
    }

    public void setVar9(String var9) {
        this.var9 = var9;
    }

    public String getVar10() {
        return var10;
    }

    public void setVar10(String var10) {
        this.var10 = var10;
    }

    public String getVar11() {
        return var11;
    }

    public void setVar11(String var11) {
        this.var11 = var11;
    }

    public String getVar12() {
        return var12;
    }

    public void setVar12(String var12) {
        this.var12 = var12;
    }

    public String getVar13() {
        return var13;
    }

    public void setVar13(String var13) {
        this.var13 = var13;
    }

    public String getVar14() {
        return var14;
    }

    public void setVar14(String var14) {
        this.var14 = var14;
    }

    public String getVar15() {
        return var15;
    }

    public void setVar15(String var15) {
        this.var15 = var15;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(command);
        dest.writeString(hash);
        dest.writeString(var1);
        dest.writeString(var2);
        dest.writeString(var3);
        dest.writeString(var4);
        dest.writeString(var5);
        dest.writeString(var6);
        dest.writeString(var7);
        dest.writeString(var8);
        dest.writeString(var9);
        dest.writeString(var10);
        dest.writeString(var11);
        dest.writeString(var12);
        dest.writeString(var13);
        dest.writeString(var14);
        dest.writeString(var15);
    }
}
