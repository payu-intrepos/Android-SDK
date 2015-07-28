package com.payu.india.Extras;

/**
 * Created by franklin on 7/7/15.
 * Simple sdk helper class helps the developer/merchant/qc to find out sdk basic details.
 */
public class PayUSdkDetails {

    /**
     * Empty constructor helps to create new instances
     */
    public PayUSdkDetails() {
    } // TODO get Activity instance and read app/device info

    /**
     * Current build number,
     * Every time we make a change, we update this manually!.
     * The format is DDMMYY
     *
     * @return String ex: 07072077
     */
    public String getSdkBuildNumber() {
        return "23072015";
    }

    /**
     * Read and send the version code from build.gradle file of sdk.
     *
     * @return String 7.0
     */
    public String getSdkVersionCode() {
        return "" + com.payu.india.BuildConfig.VERSION_CODE;
    }

    /**
     * Read and send the version name from build.gradle file of sdk.
     *
     * @return String 0.7
     */
    public String getSdkVersionName() {
        return com.payu.india.BuildConfig.VERSION_NAME;
    }

    /**
     * Read and send SDk's app id.
     *
     * @return String 8765
     */
    public String getSdkApplicationId() {
        return com.payu.india.BuildConfig.APPLICATION_ID;
    }

    /**
     * Read and send sdk build flavor
     *
     * @return String free
     */
    public String getSdkFlavor() {
        return com.payu.india.BuildConfig.FLAVOR;
    }

    /**
     * Read and send sdk build type
     *
     * @return String Release
     */
    public String getSdkBuildType() {
        return com.payu.india.BuildConfig.BUILD_TYPE;
    }
}
