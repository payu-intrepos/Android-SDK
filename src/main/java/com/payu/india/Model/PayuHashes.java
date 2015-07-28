package com.payu.india.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by franklin on 6/25/15.
 * Simple implementation of Payu hashes
 * once the user calculate all the hashes from his server he should create a {@link PayuHashes} object.
 * and assign all the required hashes. Use {@link com.payu.india.Payu.PayuConstants#PAYU_HASHES} for sending it via intent.
 * its a bean, no need to worry about serialization.
 * in case of sdkui all Activity's onCreate function look for intent.getParacleable{@link com.payu.india.Payu.PayuConstants#PAYU_HASHES}
 */
public class PayuHashes implements Parcelable {

    public static final Creator<PayuHashes> CREATOR = new Creator<PayuHashes>() {
        @Override
        public PayuHashes createFromParcel(Parcel in) {
            return new PayuHashes(in);
        }

        @Override
        public PayuHashes[] newArray(int size) {
            return new PayuHashes[size];
        }
    };
    private String paymentHash;
    private String verifyPaymentHash;
    private String checkPaymentHash;
    private String cancelRefundTransactionHash;
    private String checkActionStatusHash;
    private String captureTransactionHash;
    private String updateRequestsHash;
    private String codVerifyHash;
    private String codCancelHash;
    private String getTdrHash;
    private String udfUpdateHash;
    private String createInvoiceHash;
    private String checkOfferStatusHash;
    private String netBankingStatusHash;
    private String issuingBankStatusHash;
    private String transactionDetailsHash;
    private String transactionInfoHash;
    private String checkIsDomesticHash;
    private String storedCardsHash;
    private String saveCardHash;
    private String editCardHash;
    private String deleteCardHash;
    @Deprecated
    private String merchantIbiboCodesHash;
    private String vasForMobileSdkHash;
    private String paymentRelatedDetailsForMobileSdkHash;

    protected PayuHashes(Parcel in) {
        paymentHash = in.readString();
        verifyPaymentHash = in.readString();
        checkPaymentHash = in.readString();
        cancelRefundTransactionHash = in.readString();
        checkActionStatusHash = in.readString();
        captureTransactionHash = in.readString();
        updateRequestsHash = in.readString();
        codVerifyHash = in.readString();
        codCancelHash = in.readString();
        getTdrHash = in.readString();
        udfUpdateHash = in.readString();
        createInvoiceHash = in.readString();
        checkOfferStatusHash = in.readString();
        netBankingStatusHash = in.readString();
        issuingBankStatusHash = in.readString();
        transactionDetailsHash = in.readString();
        transactionInfoHash = in.readString();
        checkIsDomesticHash = in.readString();
        storedCardsHash = in.readString();
        saveCardHash = in.readString();
        editCardHash = in.readString();
        deleteCardHash = in.readString();
        merchantIbiboCodesHash = in.readString();
        vasForMobileSdkHash = in.readString();
        paymentRelatedDetailsForMobileSdkHash = in.readString();
    }

    public PayuHashes() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(paymentHash);
        dest.writeString(verifyPaymentHash);
        dest.writeString(checkPaymentHash);
        dest.writeString(cancelRefundTransactionHash);
        dest.writeString(checkActionStatusHash);
        dest.writeString(captureTransactionHash);
        dest.writeString(updateRequestsHash);
        dest.writeString(codVerifyHash);
        dest.writeString(codCancelHash);
        dest.writeString(getTdrHash);
        dest.writeString(udfUpdateHash);
        dest.writeString(createInvoiceHash);
        dest.writeString(checkOfferStatusHash);
        dest.writeString(netBankingStatusHash);
        dest.writeString(issuingBankStatusHash);
        dest.writeString(transactionDetailsHash);
        dest.writeString(transactionInfoHash);
        dest.writeString(checkIsDomesticHash);
        dest.writeString(storedCardsHash);
        dest.writeString(saveCardHash);
        dest.writeString(editCardHash);
        dest.writeString(deleteCardHash);
        dest.writeString(merchantIbiboCodesHash);
        dest.writeString(vasForMobileSdkHash);
        dest.writeString(paymentRelatedDetailsForMobileSdkHash);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPaymentHash() {
        return paymentHash;
    }

    public void setPaymentHash(String paymentHash) {
        this.paymentHash = paymentHash;
    }

    public String getVerifyPaymentHash() {
        return verifyPaymentHash;
    }

    public void setVerifyPaymentHash(String verifyPaymentHash) {
        this.verifyPaymentHash = verifyPaymentHash;
    }

    public String getCheckPaymentHash() {
        return checkPaymentHash;
    }

    public void setCheckPaymentHash(String checkPaymentHash) {
        this.checkPaymentHash = checkPaymentHash;
    }

    public String getCancelRefundTransactionHash() {
        return cancelRefundTransactionHash;
    }

    public void setCancelRefundTransactionHash(String cancelRefundTransactionHash) {
        this.cancelRefundTransactionHash = cancelRefundTransactionHash;
    }

    public String getCheckActionStatusHash() {
        return checkActionStatusHash;
    }

    public void setCheckActionStatusHash(String checkActionStatusHash) {
        this.checkActionStatusHash = checkActionStatusHash;
    }

    public String getCaptureTransactionHash() {
        return captureTransactionHash;
    }

    public void setCaptureTransactionHash(String captureTransactionHash) {
        this.captureTransactionHash = captureTransactionHash;
    }

    public String getUpdateRequestsHash() {
        return updateRequestsHash;
    }

    public void setUpdateRequestsHash(String updateRequestsHash) {
        this.updateRequestsHash = updateRequestsHash;
    }

    public String getCodVerifyHash() {
        return codVerifyHash;
    }

    public void setCodVerifyHash(String codVerifyHash) {
        this.codVerifyHash = codVerifyHash;
    }

    public String getCodCancelHash() {
        return codCancelHash;
    }

    public void setCodCancelHash(String codCancelHash) {
        this.codCancelHash = codCancelHash;
    }

    public String getGetTdrHash() {
        return getTdrHash;
    }

    public void setGetTdrHash(String getTdrHash) {
        this.getTdrHash = getTdrHash;
    }

    public String getUdfUpdateHash() {
        return udfUpdateHash;
    }

    public void setUdfUpdateHash(String udfUpdateHash) {
        this.udfUpdateHash = udfUpdateHash;
    }

    public String getCreateInvoiceHash() {
        return createInvoiceHash;
    }

    public void setCreateInvoiceHash(String createInvoiceHash) {
        this.createInvoiceHash = createInvoiceHash;
    }

    public String getCheckOfferStatusHash() {
        return checkOfferStatusHash;
    }

    public void setCheckOfferStatusHash(String checkOfferStatusHash) {
        this.checkOfferStatusHash = checkOfferStatusHash;
    }

    public String getNetBankingStatusHash() {
        return netBankingStatusHash;
    }

    public void setNetBankingStatusHash(String netBankingStatusHash) {
        this.netBankingStatusHash = netBankingStatusHash;
    }

    public String getIssuingBankStatusHash() {
        return issuingBankStatusHash;
    }

    public void setIssuingBankStatusHash(String issuingBankStatusHash) {
        this.issuingBankStatusHash = issuingBankStatusHash;
    }

    public String getTransactionDetailsHash() {
        return transactionDetailsHash;
    }

    public void setTransactionDetailsHash(String transactionDetailsHash) {
        this.transactionDetailsHash = transactionDetailsHash;
    }

    public String getTransactionInfoHash() {
        return transactionInfoHash;
    }

    public void setTransactionInfoHash(String transactionInfoHash) {
        this.transactionInfoHash = transactionInfoHash;
    }

    public String getCheckIsDomesticHash() {
        return checkIsDomesticHash;
    }

    public void setCheckIsDomesticHash(String checkIsDomesticHash) {
        this.checkIsDomesticHash = checkIsDomesticHash;
    }

    public String getStoredCardsHash() {
        return storedCardsHash;
    }

    public void setStoredCardsHash(String storedCardsHash) {
        this.storedCardsHash = storedCardsHash;
    }

    public String getSaveCardHash() {
        return saveCardHash;
    }

    public void setSaveCardHash(String saveCardHash) {
        this.saveCardHash = saveCardHash;
    }

    public String getEditCardHash() {
        return editCardHash;
    }

    public void setEditCardHash(String editCardHash) {
        this.editCardHash = editCardHash;
    }

    public String getDeleteCardHash() {
        return deleteCardHash;
    }

    public void setDeleteCardHash(String deleteCardHash) {
        this.deleteCardHash = deleteCardHash;
    }

    public String getMerchantIbiboCodesHash() {
        return merchantIbiboCodesHash;
    }

    public void setMerchantIbiboCodesHash(String merchantIbiboCodesHash) {
        this.merchantIbiboCodesHash = merchantIbiboCodesHash;
    }

    public String getVasForMobileSdkHash() {
        return vasForMobileSdkHash;
    }

    public void setVasForMobileSdkHash(String vasForMobileSdkHash) {
        this.vasForMobileSdkHash = vasForMobileSdkHash;
    }

    public String getPaymentRelatedDetailsForMobileSdkHash() {
        return paymentRelatedDetailsForMobileSdkHash;
    }

    public void setPaymentRelatedDetailsForMobileSdkHash(String paymentRelatedDetailsForMobileSdkHash) {
        this.paymentRelatedDetailsForMobileSdkHash = paymentRelatedDetailsForMobileSdkHash;
    }
}

