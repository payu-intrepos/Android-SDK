package com.payu.india.Payu;


/**
 * Created by franklin on 5/30/15.
 * Payu Errors
 * when SDK validates the post params (paymentDefaultParams, CCDCParams, NetbankingParams, CashCardParam, Emi params, paisawallet param)
 * any one of these error code, proper error message should be return via postParam
 */
public interface PayuErrors {

    //prefix and postfix
    public String PRE_PREIX = "should be the ";
    public String CARD_PREIX = PRE_PREIX + "card ";
    public String UDF_POSTIX = " should not be null, it can be empty or string";
    public String INVALID_PREIX1 = "Please enter valid ";
    public String INVALID_PREIX2 = "Please provide valid ";
    public String MANDATORY_PARAM_PREIX = "Mandatory param ";
    public String IS_MISSING_POSTIX = " is missing";

    public int NO_ERROR = 0000;  // TODO rename this gus as SUCCESS or something else.
    public int MISSING_PARAMETER_EXCEPTION = 5001;
    public int NUMBER_FORMAT_EXCEPTION = 5002;
    public int INVALID_AMOUNT_EXCEPTION = 5003;
    public int UN_SUPPORTED_ENCODING_EXCEPTION = 5004;
    public int INVALID_BANKCODE_EXCEPTION = 5005;
    public int INVALID_PG_EXCEPTION = 5006;
    public int INVALID_CARD_TOKEN_EXCEPTION = 5007;
    public int INVALID_CARD_NUMBER_EXCEPTION = 5008;
    public int INVALID_CVV_EXCEPTION = 5009;
    public int INVALID_MONTH_EXCEPTION = 5010;
    public int INVALID_YEAR_EXCEPTION = 5011;
    public int CARD_EXPIRED_EXCEPTION = 5012;
    public int USER_CREDENTIALS_NOT_FOUND_EXCEPTION = 5013;
    public int INVALID_USER_CREDENTIALS = 5014;
    public int JSON_EXCEPTION = 5014;
    public int NO_SUCH_ALGORITHM_EXCEPTION = 5015;
    public int PROTOCOL_EXCEPTION = 5016;
    public int IO_EXCEPTION = 5016;

    public int DELETE_CARD_EXCEPTION = 5017;
    public int GET_USER_CARD_EXCEPTION = 5018;

    public int INVALID_HASH = 5019;

    public String MANDATORY_PARAM_VAR1_IS_MISSING = MANDATORY_PARAM_PREIX + "var1" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_VAR2_IS_MISSING = MANDATORY_PARAM_PREIX + "var2" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_VAR3_IS_MISSING = MANDATORY_PARAM_PREIX + "var3" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_VAR4_IS_MISSING = MANDATORY_PARAM_PREIX + "var4" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_VAR5_IS_MISSING = MANDATORY_PARAM_PREIX + "var5" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_VAR6_IS_MISSING = MANDATORY_PARAM_PREIX + "var6" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_VAR7_IS_MISSING = MANDATORY_PARAM_PREIX + "var7" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_VAR8_IS_MISSING = MANDATORY_PARAM_PREIX + "var8" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_VAR9_IS_MISSING = MANDATORY_PARAM_PREIX + "var9" + IS_MISSING_POSTIX;


    public String MANDATORY_PARAM_KEY_IS_MISSING = MANDATORY_PARAM_PREIX + "key" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_SALT_IS_MISSING = MANDATORY_PARAM_PREIX + "salt" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_HASH_IS_MISSING = MANDATORY_PARAM_PREIX + "hash" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_COMMAND_IS_MISSING = MANDATORY_PARAM_PREIX + "command" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_TXNID_IS_MISSING = MANDATORY_PARAM_PREIX + "txnid" + IS_MISSING_POSTIX;

    public String MANDATORY_PARAM_FIRST_NAME_IS_MISSING = MANDATORY_PARAM_PREIX + "firstname" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_EMAIL_IS_MISSING = MANDATORY_PARAM_PREIX + "email" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_PRODUCT_INFO_IS_MISSING = MANDATORY_PARAM_PREIX + "product info" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_SURL_IS_MISSING = MANDATORY_PARAM_PREIX + "surl" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_FURL_IS_MISSING = MANDATORY_PARAM_PREIX + "furl" + IS_MISSING_POSTIX;


    public String MANDATORY_PARAM_BANK_CODE_IS_MISSING = MANDATORY_PARAM_PREIX + "bankcode" + IS_MISSING_POSTIX;
    public String MANDATORY_PARAM_PG_IS_MISSING = MANDATORY_PARAM_PREIX + "pg is missing, pg should be any one of CC, EMI, CASH, NB, PAYU_MONEY";

    public String TRANSACTION_ID_MISSING = PRE_PREIX + "Transaction id (txnid)";
    public String MIHPAY_ID_MISSING = PRE_PREIX + "Payu id (mihpayid) of the transaction which was given by payu.";
    public String CARD_TOKEN_MISSING = PRE_PREIX + "card token, you get the card token when you store/fetch a card";
    public String USER_CREDENTIALS_MISSING = PRE_PREIX + "user credentials and it should be merchant_key:unique_user_id.";
    public String USER_CREDENTIALS_OR_DEFAULT_MISSING = " should be user_credentials (merchant_key:unique_user_id.) to get the merchant information and stored card or default to get only the merchant information";
    public String CARD_NUMBER_MISSING = " should be a valid credit / debit card number";
    public String NAME_ON_CARD_MISSING = " should be name on card";


    public String CARD_TYPE_MISSING = CARD_PREIX + "type; please use CC as card type";
    public String CARD_MODE_MISSING = CARD_PREIX + "mode; please use CC as card mode";
    public String CARD_NAME_MISSING = CARD_PREIX + "name (nickname of the card)";

    public String CARD_BIN_MISSING = "number or the card bin (first 6 digit of the card)";
    public String END_DATE_TIME_MISSING = PRE_PREIX + "till date in YYYY-MM-DD hh:mm:ss format.";
    public String FROM_DATE_TIME_MISSING = PRE_PREIX + "from date in YYYY-MM-DD hh:mm:ss format";
    public String END_DATE_MISSING = "till date in YYYY-MM-DD format.";
    public String FROM_DATE_MISSING = PRE_PREIX + "from date in YYYY-MM-DD format";
    public String BANK_CODE_MISSING = PRE_PREIX + "bank code for one bank, default for getting all banks";
    public String OFFER_KEY_MISSING = " should be offer key example : offer@1234 ";
    public String POST_DATA_MISSING = "Post data is missing";

    public String USER_CREDENTIALS_NOT_FOUND = " Card can not be stored!, user_credentials is missing!";

    public String CARD_EXPIRED = " It seems the card is expired!";

    public String INVALID_YEAR = " Invalid year, year should be 4 digit YYYY format";
    public String INVALID_MONTH = " Invalid month, it should be two digit number range from 01 to 12 MM format";
    public String INVALID_JSON = " should be a stringified JSON object; It seems there is an exception while parsing JSON";
    public String INVALID_AMOUNT = " Amount should be a Double value example 5.00";
    public String INVALID_AMOUNT_TO_REFUND = "  should contain the amount which needs to be refunded. Please note that both partial and full refunds are allowed.";
    public String INVALID_TOKEN_ID = PRE_PREIX + "Token ID(unique token from merchant)";
    public String INVALID_BANK_REFERENCE_ID = PRE_PREIX + "Bank Ref Id for the requested transaction.";
    public String INVALID_ACTION = PRE_PREIX + "Action (cancel/capture/refund)";
    public String INVALID_BANK_CODE_CC = "For credit card payment bank code should be CC";
    public String INVALID_BANK_CODE = "Invalid bank code please verify";
    public String INVALID_PAYMENT_OPTION = " Invalid card payment option. ccnum / card_token missing.";
    public String INVALID_CARD_TOKEN = " Invalid card token";
    public String INVALID_CARD_NUMBER = " Invalid card number, Failed while applying Luhn";
    public String INVALID_CVV = " Invalid cvv, please verify";
    public String INVALID_URL = " should be something like https://www.payu.in/txnstatus";
    public String INVALID_PG = "Invalid pg!, pg should be any one of CC, EMI, CASH, NB, PAYU_MONEY";
    public String INVALID_ALGORITHM_SHA = " Message digest sha 512 not found!";
    public String INVALID_PRODUCT_INFO = "Product info should not be null, it can be empty or string";
    public String INVALID_FIRST_NAME = "First name should not be null, it can be empty or string";
    public String INVALID_EMAIL = "Email should not be null, it can be empty or string";


    public String INVALID_UDF1 = "UDF1" + UDF_POSTIX;
    public String INVALID_UDF2 = "UDF2" + UDF_POSTIX;
    public String INVALID_UDF3 = "UDF3" + UDF_POSTIX;
    public String INVALID_UDF4 = "UDF4" + UDF_POSTIX;
    public String INVALID_UDF5 = "UDF5" + UDF_POSTIX;


    public String INVALID_SALT = "Salt should be a valid string";


    public String INVALID_POST_PARAMS = INVALID_PREIX1 + "MerchantWebService object / PaymentDefaultParams and PaymnentModeParam";
    public String INVALID_PAYMENT_DEFAULT_PARAMS = INVALID_PREIX1 + "PaymentDefaultParamsObject";
    public String INVALID_PAYMENT_MODES = INVALID_PREIX2 + "PaymentModes (Anyone from NB, CASH, EMI, CC, PAYU_MONEY)";
    public String INVALID_CARD_DETAILS = INVALID_PREIX2 + "card details (card number , cvv, exp month, exp year, card name)";
    public String INVALID_EMI_DETAILS = INVALID_PREIX2 + "email details";
    public String INVALID_USER_CREDENTIALS_MISSING = " Invalid user credentials, user_credentials should be merchant_key:unique_user_id.";

    public String USE_DEFAULT = " Please send var1 as 'default'";
    public String MORE_THAN_ONE_TXNID = "if you want to verify more than one transaction please separate them by pipe : ex 6234567|45678987|4567876 ";
    public String NEW_STATUS = " should be new status to be set";
    public String REQUEST_ID = PRE_PREIX + "Request ID which you get while cancel_refund_transaction api";

    // no error block
    public String SDK_DETAILS_FETCHED_SUCCESSFULLY = "Data fetched successfully, Stored card status: ";

}
