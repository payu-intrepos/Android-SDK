package com.payu.india.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by franklin on 5/31/15.
 * Simple Bean of payment default params,
 * Since its parcelable you can pass it through intent with out any difficult.
 * When app tries to make payment it should passed along with {@link com.payu.india.PostParams.CCDCPostParams}, {@link com.payu.india.PostParams.EmiPostParams}, etc.
 */
public class PaymentDefaultParams implements Parcelable {

    public static final Creator<PaymentDefaultParams> CREATOR = new Creator<PaymentDefaultParams>() {
        @Override
        public PaymentDefaultParams createFromParcel(Parcel in) {
            return new PaymentDefaultParams(in);
        }

        @Override
        public PaymentDefaultParams[] newArray(int size) {
            return new PaymentDefaultParams[size];
        }
    };
    private String key;
    private String txnId;
    private String amount;
    private String productInfo;
    private String firstName;
    private String email;
    private String surl;
    private String furl;
    private String hash;
    private String offerKey;
    private String phone;
    private String lastName;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String udf1;
    private String udf2;
    private String udf3;
    private String udf4;
    private String udf5;
    private String codUrl;
    private String dropCategory;
    private String enforcePayMethod;
    private String customNote;
    private String noteCategory;
    private String shippingFirstName;
    private String shippingLastName;
    private String shippingAddress1;
    private String shippingAddress2;
    private String shippingCity;
    private String shippingState;
    private String shippingCounty;
    private String shippingZipCode;
    private String shippingPhone;
    private String userCredentials;
    private int storeCard;

    protected PaymentDefaultParams(Parcel in) {
        key = in.readString();
        txnId = in.readString();
        amount = in.readString();
        productInfo = in.readString();
        firstName = in.readString();
        email = in.readString();
        surl = in.readString();
        furl = in.readString();
        hash = in.readString();
        offerKey = in.readString();
        phone = in.readString();
        lastName = in.readString();
        address1 = in.readString();
        address2 = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        zipCode = in.readString();
        udf1 = in.readString();
        udf2 = in.readString();
        udf3 = in.readString();
        udf4 = in.readString();
        udf5 = in.readString();
        codUrl = in.readString();
        dropCategory = in.readString();
        enforcePayMethod = in.readString();
        customNote = in.readString();
        noteCategory = in.readString();
        shippingFirstName = in.readString();
        shippingLastName = in.readString();
        shippingAddress1 = in.readString();
        shippingAddress2 = in.readString();
        shippingCity = in.readString();
        shippingState = in.readString();
        shippingCounty = in.readString();
        shippingZipCode = in.readString();
        shippingPhone = in.readString();
        userCredentials = in.readString();
        storeCard = in.readInt();
    }

    public PaymentDefaultParams() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }

    public String getFurl() {
        return furl;
    }

    public void setFurl(String furl) {
        this.furl = furl;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getOfferKey() {
        return offerKey;
    }

    public void setOfferKey(String offerKey) {
        this.offerKey = offerKey;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getUdf1() {
        return udf1;
    }

    public void setUdf1(String udf1) {
        this.udf1 = udf1;
    }

    public String getUdf2() {
        return udf2;
    }

    public void setUdf2(String udf2) {
        this.udf2 = udf2;
    }

    public String getUdf3() {
        return udf3;
    }

    public void setUdf3(String udf3) {
        this.udf3 = udf3;
    }

    public String getUdf4() {
        return udf4;
    }

    public void setUdf4(String udf4) {
        this.udf4 = udf4;
    }

    public String getUdf5() {
        return udf5;
    }

    public void setUdf5(String udf5) {
        this.udf5 = udf5;
    }

    public String getCodUrl() {
        return codUrl;
    }

    public void setCodUrl(String codUrl) {
        this.codUrl = codUrl;
    }

    public String getDropCategory() {
        return dropCategory;
    }

    public void setDropCategory(String dropCategory) {
        this.dropCategory = dropCategory;
    }

    public String getEnforcePayMethod() {
        return enforcePayMethod;
    }

    public void setEnforcePayMethod(String enforcePayMethod) {
        this.enforcePayMethod = enforcePayMethod;
    }

    public String getCustomNote() {
        return customNote;
    }

    public void setCustomNote(String customNote) {
        this.customNote = customNote;
    }

    public String getNoteCategory() {
        return noteCategory;
    }

    public void setNoteCategory(String noteCategory) {
        this.noteCategory = noteCategory;
    }

    public String getShippingFirstName() {
        return shippingFirstName;
    }

    public void setShippingFirstName(String shippingFirstName) {
        this.shippingFirstName = shippingFirstName;
    }

    public String getShippingLastName() {
        return shippingLastName;
    }

    public void setShippingLastName(String shippingLastName) {
        this.shippingLastName = shippingLastName;
    }

    public String getShippingAddress1() {
        return shippingAddress1;
    }

    public void setShippingAddress1(String shippingAddress1) {
        this.shippingAddress1 = shippingAddress1;
    }

    public String getShippingAddress2() {
        return shippingAddress2;
    }

    public void setShippingAddress2(String shippingAddress2) {
        this.shippingAddress2 = shippingAddress2;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingState() {
        return shippingState;
    }

    public void setShippingState(String shippingState) {
        this.shippingState = shippingState;
    }

    public String getShippingCounty() {
        return shippingCounty;
    }

    public void setShippingCounty(String shippingCounty) {
        this.shippingCounty = shippingCounty;
    }

    public String getShippingZipCode() {
        return shippingZipCode;
    }

    public void setShippingZipCode(String shippingZipCode) {
        this.shippingZipCode = shippingZipCode;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(String userCredentials) {
        this.userCredentials = userCredentials;
    }

    public int getStoreCard() {
        return storeCard;
    }

    public void setStoreCard(int storeCard) {
        this.storeCard = storeCard;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(txnId);
        dest.writeString(amount);
        dest.writeString(productInfo);
        dest.writeString(firstName);
        dest.writeString(email);
        dest.writeString(surl);
        dest.writeString(furl);
        dest.writeString(hash);
        dest.writeString(offerKey);
        dest.writeString(phone);
        dest.writeString(lastName);
        dest.writeString(address1);
        dest.writeString(address2);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(zipCode);
        dest.writeString(udf1);
        dest.writeString(udf2);
        dest.writeString(udf3);
        dest.writeString(udf4);
        dest.writeString(udf5);
        dest.writeString(codUrl);
        dest.writeString(dropCategory);
        dest.writeString(enforcePayMethod);
        dest.writeString(customNote);
        dest.writeString(noteCategory);
        dest.writeString(shippingFirstName);
        dest.writeString(shippingLastName);
        dest.writeString(shippingAddress1);
        dest.writeString(shippingAddress2);
        dest.writeString(shippingCity);
        dest.writeString(shippingState);
        dest.writeString(shippingCounty);
        dest.writeString(shippingZipCode);
        dest.writeString(shippingPhone);
        dest.writeString(userCredentials);
        dest.writeInt(storeCard);
    }
}
