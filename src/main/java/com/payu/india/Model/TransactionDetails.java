package com.payu.india.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by franklin on 7/2/15.
 * Bean for the responses of {@link com.payu.india.Payu.PayuConstants#VERIFY_PAYMENT} , {@link com.payu.india.Payu.PayuConstants#GET_TRANSACTION_DETAILS}
 */
public class TransactionDetails implements Parcelable {

    public static final Creator<TransactionDetails> CREATOR = new Creator<TransactionDetails>() {
        @Override
        public TransactionDetails createFromParcel(Parcel in) {
            return new TransactionDetails(in);
        }

        @Override
        public TransactionDetails[] newArray(int size) {
            return new TransactionDetails[size];
        }
    };
    private String mihpayId;
    private String requestId;
    private String bankReferenceNumber;
    private String field9;
    private String netAmountDebit;
    private String pgType;
    private String nameOnCard;
    private String unmappedStatus;
    private String merchantUTR;
    private String settledAt;
    private String id;
    private String status;
    private String key;
    private String merchantname;
    private String txnid;
    private String firstname;
    private String lastname;
    private String addedon;
    private String bankName;
    private String paymentGateway;
    private String phone;
    private String email;
    private String amount;
    private String discount;
    private String additionalCharges;
    private String productinfo;
    private String errorCode;
    private String errorMessage;
    private String bankRefNo;
    private String ibiboCode;
    private String mode;
    private String ip;
    private String cardNo;
    private String cardtype;
    private String offerKey;
    private String field2;
    private String udf1;
    private String udf2;
    private String udf3;
    private String udf4;
    private String udf5;
    private String pgMid;
    private String offerType;
    private String failureReason;
    private String merServiceFee;
    private String merServiceTax;
    private String bankCode;

    protected TransactionDetails(Parcel in) {
        mihpayId = in.readString();
        requestId = in.readString();
        bankReferenceNumber = in.readString();
        field9 = in.readString();
        netAmountDebit = in.readString();
        pgType = in.readString();
        nameOnCard = in.readString();
        unmappedStatus = in.readString();
        merchantUTR = in.readString();
        settledAt = in.readString();
        id = in.readString();
        status = in.readString();
        key = in.readString();
        merchantname = in.readString();
        txnid = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        addedon = in.readString();
        bankName = in.readString();
        paymentGateway = in.readString();
        phone = in.readString();
        email = in.readString();
        amount = in.readString();
        discount = in.readString();
        additionalCharges = in.readString();
        productinfo = in.readString();
        errorCode = in.readString();
        errorMessage = in.readString();
        bankRefNo = in.readString();
        ibiboCode = in.readString();
        mode = in.readString();
        ip = in.readString();
        cardNo = in.readString();
        cardtype = in.readString();
        offerKey = in.readString();
        field2 = in.readString();
        udf1 = in.readString();
        udf2 = in.readString();
        udf3 = in.readString();
        udf4 = in.readString();
        udf5 = in.readString();
        pgMid = in.readString();
        offerType = in.readString();
        failureReason = in.readString();
        merServiceFee = in.readString();
        merServiceTax = in.readString();
        bankCode = in.readString();
    }

    public TransactionDetails() {
    }

    public String getMihpayId() {
        return mihpayId;
    }

    public void setMihpayId(String mihpayId) {
        this.mihpayId = mihpayId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getBankReferenceNumber() {
        return bankReferenceNumber;
    }

    public void setBankReferenceNumber(String bankReferenceNumber) {
        this.bankReferenceNumber = bankReferenceNumber;
    }

    public String getField9() {
        return field9;
    }

    public void setField9(String field9) {
        this.field9 = field9;
    }

    public String getNetAmountDebit() {
        return netAmountDebit;
    }

    public void setNetAmountDebit(String netAmountDebit) {
        this.netAmountDebit = netAmountDebit;
    }

    public String getPgType() {
        return pgType;
    }

    public void setPgType(String pgType) {
        this.pgType = pgType;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getUnmappedStatus() {
        return unmappedStatus;
    }

    public void setUnmappedStatus(String unmappedStatus) {
        this.unmappedStatus = unmappedStatus;
    }

    public String getMerchantUTR() {
        return merchantUTR;
    }

    public void setMerchantUTR(String merchantUTR) {
        this.merchantUTR = merchantUTR;
    }

    public String getSettledAt() {
        return settledAt;
    }

    public void setSettledAt(String settledAt) {
        this.settledAt = settledAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMerchantname() {
        return merchantname;
    }

    public void setMerchantname(String merchantname) {
        this.merchantname = merchantname;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddedon() {
        return addedon;
    }

    public void setAddedon(String addedon) {
        this.addedon = addedon;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(String paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(String additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public String getProductinfo() {
        return productinfo;
    }

    public void setProductinfo(String productinfo) {
        this.productinfo = productinfo;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getBankRefNo() {
        return bankRefNo;
    }

    public void setBankRefNo(String bankRefNo) {
        this.bankRefNo = bankRefNo;
    }

    public String getIbiboCode() {
        return ibiboCode;
    }

    public void setIbiboCode(String ibiboCode) {
        this.ibiboCode = ibiboCode;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getOfferKey() {
        return offerKey;
    }

    public void setOfferKey(String offerKey) {
        this.offerKey = offerKey;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
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

    public String getPgMid() {
        return pgMid;
    }

    public void setPgMid(String pgMid) {
        this.pgMid = pgMid;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getMerServiceFee() {
        return merServiceFee;
    }

    public void setMerServiceFee(String merServiceFee) {
        this.merServiceFee = merServiceFee;
    }

    public String getMerServiceTax() {
        return merServiceTax;
    }

    public void setMerServiceTax(String merServiceTax) {
        this.merServiceTax = merServiceTax;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mihpayId);
        dest.writeString(requestId);
        dest.writeString(bankReferenceNumber);
        dest.writeString(field9);
        dest.writeString(netAmountDebit);
        dest.writeString(pgType);
        dest.writeString(nameOnCard);
        dest.writeString(unmappedStatus);
        dest.writeString(merchantUTR);
        dest.writeString(settledAt);
        dest.writeString(id);
        dest.writeString(status);
        dest.writeString(key);
        dest.writeString(merchantname);
        dest.writeString(txnid);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(addedon);
        dest.writeString(bankName);
        dest.writeString(paymentGateway);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(amount);
        dest.writeString(discount);
        dest.writeString(additionalCharges);
        dest.writeString(productinfo);
        dest.writeString(errorCode);
        dest.writeString(errorMessage);
        dest.writeString(bankRefNo);
        dest.writeString(ibiboCode);
        dest.writeString(mode);
        dest.writeString(ip);
        dest.writeString(cardNo);
        dest.writeString(cardtype);
        dest.writeString(offerKey);
        dest.writeString(field2);
        dest.writeString(udf1);
        dest.writeString(udf2);
        dest.writeString(udf3);
        dest.writeString(udf4);
        dest.writeString(udf5);
        dest.writeString(pgMid);
        dest.writeString(offerType);
        dest.writeString(failureReason);
        dest.writeString(merServiceFee);
        dest.writeString(merServiceTax);
        dest.writeString(bankCode);
    }
}
