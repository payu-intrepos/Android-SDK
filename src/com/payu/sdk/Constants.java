package com.payu.sdk;

/**
 * Created by franklin on 8/7/14.
 */
public class Constants {

    public static final boolean DEBUG = false; // true for test env, false for prod env.
    public static final boolean SDK_HASH_GENERATION = false;

    public static final boolean ENABLE_VAS = true;
    public static final boolean SET_DEFAULT_NET_BANKING = true;
    public static final boolean SET_DEFAULT_CASH_CARD = true;


    public static final String HASH = "hash";
    public static final String POST_DATA = "postData";

    public static final String CVV_TITLE = "CVV Required";
    public static final String CVV_MESSAGE = "Enter your CVV to make payment.";
    public static final String CVV_ERROR_MESSAGE = "Please enter valid CVV number";

    // api commands.

    public static final String DEFAULT = "default";
    public static final String GET_USER_CARDS = "get_user_cards";
    public static final String DELETE_USER_CARD = "delete_user_card";
    public static final String EDIT_USER_CARD = "edit_user_card";
    public static final String GET_IBIBO_CODES = "get_merchant_ibibo_codes";
    public static final String SAVE_USER_CARD = "save_user_card";
    public static final String OFFER_STATUS = "check_offer_status";
    public static final String GET_VAS = "vas_for_mobile_sdk";
    public static final String PAYMENT_RELATED_DETAILS = "payment_related_details_for_mobile_sdk";


    public static final String VAR1 = "var1";
    public static final String VAR2 = "var2";
    public static final String VAR3 = "var3";
    public static final String VAR4 = "var4";
    public static final String VAR5 = "var5";
    public static final String VAR6 = "var6";
    public static final String VAR7 = "var7";
    public static final String VAR8 = "var8";
    public static final String VAR9 = "var9";


    // mobile dedicated test env
    private static final String TEST_PAYMENT_URL =  "https://mobiletest.payu.in/_payment";
    private static final String TEST_FETCH_DATA_URL = "https://mobiletest.payu.in/merchant/postservice?form=2";

    // production
    private static final String PRODUCTION_PAYMENT_URL = "https://secure.payu.in/_payment";
    private static final String PRODUCTION_FETCH_DATA_URL = "https://info.payu.in/merchant/postservice.php?form=2";

    public static final String FETCH_DATA_URL = DEBUG ? TEST_FETCH_DATA_URL : PRODUCTION_FETCH_DATA_URL;
    public static final String PAYMENT_URL = DEBUG ? TEST_PAYMENT_URL : PRODUCTION_PAYMENT_URL;
}
