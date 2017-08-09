package com.harrier.lifecyclehealth.utils;

/**
 * Created by satyam on 01/07/2016.
 */
public class AppConstants {


    public static final String STATUS_SUCCESS = "0";
    public static final String STATUS_FAIL = "1";
    public static final String STATUS_TOKEN_EXPIRE = "2";
    public static final Integer REQUEST_TIMEOUT = 30000; // 30 secs
    public static final String SHARED_PREFERENCE_NAME = "LifeCycleHealth";

    /* X -Token*/
    public static final String USER_TOKEN = "user_token";

    /*Base Url*/
    /*1) Pooja Mam*/
    // public static final String BASE_URL = "http://192.168.0.125:3004/api/v1/";

    /*2) Test Url*/
    public static final String BASE_URL = "https://tst.api.lifecyclehealth.com/api/v1/";

    /*3) Production*/
    //  public static final String BASE_URl="https://api.lifecyclehealth.com/api/v1/";

    /* URLS*/

    public static final String URL_TERM_N_CONDITION = "http://www.lifecyclehealth.com/terms";
    public static final String URL_CHECK_USERNAME = "user/checkUsername/";
    public static final String URL_AUTHENTICATE_USER = "validate/user";
    public static final String URL_VALIDATE_MOBILE_NUMBER = "secure/user/mobileVerifyCode";
    public static final String URL_VERIFY_CODE = "secure/validate/mobileVerifyCode";
    public static final String URL_FORGOT_PASSWORD_SEND_CODE = "user/regeneratepassword";
    public static final String URL_FORGOT_PASSWORD_AUTHENTICATE_CODE = "validate/regPasswordCode/plain";
    public static final String URL_FORGOT_PASSWORD_UPDATE_PASS = "user/resetPassword";
    /* Survey*/
    public static final String URL_SURVEY_PLAN_PATIENT = "secure/patient/Activity/SurveyPlan";
    public static final String URL_SURVEY_PLAN_Provider = "secure/provider/Activity/SurveyPlan";
    public static final String URL_SURVEY_PLAN_QA_PATIENT="secure/patient/ActivitySurvey/Question/";
    public static final String URL_SURVEY_PLAN_QA_PROVIDER="secure/provider/ActivitySurvey/Question/";


    /*Login Screen*/
    public static final String USER_ROLE = "Web User";
    /* Verify Mobile Screen*/
    public static final String REQUEST_KEY_VERIFY_MOB = "MobilePhone";
    public static final String REQUEST_KEY_VERIFY_COUNTRY = "Country_Code";
    public static final String REQUEST_KEY_VERIFY_MOB_VERY = "Mobile_Verification_Code";
    /* Forgot password Screen*/
    public static final String REQUEST_KEY_EMAIL = "Email";
    public static final String REQUEST_KEY_AUTH_ID = "id";
    public static final String REQUEST_KEY_AUTH_OTP = "OTP";
    public static final String REQUEST_KEY_PASSWORD = "password";

    /*Extras*/
    public static final String EXTRA_LOGIN_COUNTRY_CODE = "extra_country_code";
    public static final String EXTRA_LOGIN_MOBILE_NUMBER = "extra_mobile_number";
    public static final String EXTRA_FORGOT_PASS_USERNAME = "extra_user_name";

    /*Preference & Constant*
     */
    public static final String PREF_FORGOT_PASS_CURRENT_STEP = "forgot_password_step";
    public static final String PREF_CURRENT_USER = "current_user";
    public static final String PREF_ROLE_CURRENT_USER = "user_role";
    public static final String PREF_IS_PATIENT = "isPatient";
    public static final String CONST_PATIENT = "Patient";
    public static final String CONST_SURV_LIST_TODO="To Do";
    public static final String CONST_SURV_LIST_SCHEDULE="Scheduled";
    public static final String CONST_SURV_LIST_COMPLETED="Completed";

    public static final int CONST_STEP_ONE = 1;
    public static final int CONST_STEP_TWO = 2;
    public static final int CONST_STEP_THREE = 3;
    public static final int CONST_STEP_FOUR = 4;
    public static final int CONST_STEP_RESET = 0;


}
