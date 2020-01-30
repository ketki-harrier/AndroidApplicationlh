package com.lifecyclehealth.lifecyclehealth.utils;

import com.lifecyclehealth.lifecyclehealth.model.ColorCode;

/**
 * Created by 01/07/2016.
 */
public class AppConstants {

    public static final String STATUS_SUCCESS = "0";
    //public static final String IsPatient = "isPatient";
    public static final String Is_Care_Giver = "isCareGiver";
    public static final String STATUS_FAIL = "1";
    public static final String STATUS_TOKEN_EXPIRE = "2";
    public static final Integer REQUEST_TIMEOUT = 600000; // 60 secs
    public static final String SHARED_PREFERENCE_NAME = "LifeCycleHealth";

    public static final String seedValue = "12345";
    public static final String notificationCount = "notificationCount";
    public static final String messageCount = "messageCount";

    /* Moxtra variables*/
    public static final String Moxtra_ORG_ID = "Moxtra_ORG_ID";
    public static final String Moxtra_uniqueId = "uniqueId";
    public static final String Moxtra_Access_Token = "Moxtra_Access_Token";
    public static final String SESSION_KEY = "SESSION_KEY";
    public static final String GCM_token = "GCM_token";
    public static final String Patient_Name = "Patient";

    /* X -Token*/
    public static final String USER_TOKEN = "user_token";

    /*Base Url*/
    /*1) Pooja Mam*/
    // public static final String BASE_URL = "http://192.168.0.132:3004/api/v1/";

    /*2) Test Url*/
      public static final String BASE_URL = "https://tst.api.lifecyclehealth.com/api/v1/";

    /*3) Production*/
  //  public static final String BASE_URL = "https://api.lifecyclehealth.com/api/v1/";

    /* URLS*/
    public static final String URL_SUPPORT = "secure/support";
    public static final String URL_TERM_N_CONDITION = "http://www.lifecyclehealth.com/terms";
    public static final String URL_CHECK_USERNAME = "user/checkUsername/";
    public static final String URL_CHECK_IS_USER_TOUCH_ACCEPT = "user/touchEnable/";
    public static final String URL_AUTHENTICATE_USER = "validate/user";
    public static final String URL_VALIDATE_MOBILE_NUMBER = "secure/user/mobileVerifyCode";
    public static final String URL_VERIFY_CODE = "secure/validate/mobileVerifyCode";
    public static final String URL_FORGOT_USERNAME = "user/forgot/username";
    public static final String URL_FORGOT_PASSWORD_SEND_CODE = "user/regeneratepassword";
    public static final String URL_FORGOT_PASSWORD_AUTHENTICATE_CODE = "validate/regPasswordCode/plain";
    public static final String URL_FORGOT_PASSWORD_UPDATE_PASS = "user/resetPassword";
    public static final String URL_CHANGE_PASSWORD_AUTO_GENERATED = "user/changePassword";
    public static final String URL_CHANGE_TOUCH_STATE = "secure/user/touchEnable/";

    /* Survey*/
    public static final String URL_SURVEY_PLAN_PATIENT = "secure/patient/Activity/SurveyPlan";
    public static final String URL_SURVEY_PLAN_Provider = "secure/provider/Activity/SurveyPlan";
    public static final String URL_SURVEY_PLAN_QA_PATIENT = "secure/patient/ActivitySurvey/Question/";
    public static final String URL_SURVEY_PLAN_QA_PROVIDER = "secure/provider/ActivitySurvey/Question/";
    public static final String URL_SURVEY_SUBMIT_ANSWER = "secure/patient/ActivitySurvey";
    public static final String URL_SURVEY_REMOVE_IMAGE = "secure/patient/ActivitySurvey/removefile";
    public static final String URL_SURVEY_SUBMIT_ANSWER_FILE = "secure/patient/ActivitySurvey/withfile";
    public static final String URL_SURVEY_SUBMIT_ANSWER_ELECTRONIC_PATIENT = "secure/patient/ActivitySurvey";
    public static final String URL_SURVEY_SUBMIT_ANSWER_ELECTRONIC_PROVIDER = "secure/provider/ActivitySurvey";

    /*Profile*/
    public static final String URL_PROFILE_GETIMAGE = "secure/user/profile";
    public static final String URL_PROFILE_GETDATA = "secure/user";
    public static final String URL_PROFILE_SETIMAGE = "secure/user/profile";
    public static final String URL_PROFILE_CAREGIVER = "secure/caregiver/list";
    public static final String URL_PROFILE_CAREGIVER_FOR = "secure/caregiver/all/patient/list";

    /* Caregiver Login */
    public static final String URL_GET_CAREGIVERNAME = "secure/caregiver/patient/list";
    public static final String URL_LOGIN_AS_CAREGIVERNAME = "secure/caregiver/login";
    public static final String URL_LOGIN_AS_SELF = "secure/self/login";

    /*get color */
    public static final String URL_VISUAL_PREFERENCE = "secure/visual/preferences";

    /* Meet */
    public static final String URL_MEET_LIST = "secure/meet";
    public static final String URL_MEET_LIST_DETAILS = "secure/meet/details/";
    public static final String URL_MEET_START = "secure/meet/update/starts/";
    public static final String URL_MEET_END = "secure/meet/update/ends/";
    public static final String URL_MEET_INVITE_PARTICIPANT = "secure/meet/member/";
    public static final String URL_MEET_INVITE_PARTICIPANT_TO_MEET = "secure/meet/invite";

    /*Notification*/
    // public static final String URL_DELETE_NOTIFICATION = "secure/alert/markedRead/";
    public static final String URL_READ_NOTIFICATION = "secure/alert/markedRead/";
    //public static final String  URL_READ_NOTIFICATION = "secure/alert/delete/";
    public static final String URL_DELETE_NOTIFICATION = "secure/alert/delete/";
    public static final String URL_SURVEY_CHECK_AVAILABILITY_NOTIFICATION = "secure/alert/isexists";
    public static final String URL_FILTER_NOTIFICATION = "secure/alert/list";
    public static final String URL_FILTER_REPORT = "secure/patient/ActivitySurvey/Question/";

    /*Carousal*/
    public static final String URL_CAROUSAL = "secure/mobile/carousal/";


    /*More change password*/
    public static final String URL_CHANGEPASSWORD = "user/changePassword";

    /*Login Screen*/
    public static final String USER_ROLE = "Web User";
    public static final String LOGIN_ID = "loginId";
    public static final String AES_Key = "AES_Key";
    public static final String EMAIL_ID = "emailId";
    public static final String LOGIN_NAME = "loginName";
    public static final String selected_role = "Role";
    public static final String selected_value = "newvalue";
    //public static final String IS_TOUCH_ACCESS = "IS_TOUCH_ACCESS";
    public static final int TIME_TO_WAIT = 10 * 60 * 1000;
    public static long TOUCH_TIME = 0;
    public static final String TOUCH_EMAIL_ID = "touchEmailId";
    public static final String IS_TIMEOUT = "IS_TIMEOUT";
    public static final String IS_LOGOUT = "IS_LOGOUT";
    public static final String IS_IN_MEET = "IS_IN_MEET";
    public static final String IS_USERNAME_EDITABLE = "IS_USERNAME_EDITABLE";
    public static final String IS_MAINACTIVITY_ALIVE = "IS_MAINACTIVITY_ALIVE";
    public static final String IS_LOGINACTIVITY_ALIVE = "IS_LOGINACTIVITY_ALIVE";
    public static final String IS_IN_MEET_FRAGMENT = "IS_IN_MEET_FRAGMENT";
    public static final String SET_COLOR_CODE = "visualBrandingPreferences";


    public static final String SERVER_DB_TOUCH = "SERVER_DB_TOUCH";
    public static final String LOCAL_DB_TOUCH = "LOCAL_DB_TOUCH";

    //public static byte[] AES_Key;

    /*MESSAGE*/
    public static final String URL_MESSAGE_PATIENT_LIST = "secure/patient/list";
    public static final String URL_MESSAGE_EPISODE_LIST_FOR_PATIENT = "secure/episode/list";
    public static final String URL_MESSAGE_EPISODE_LIST_FOR_PROVIDER = "secure/episode/list";
    public static final String URL_MESSAGE_INVITEE_LIST_FOR_EPISODE = "secure/episode/participant/patient";
    public static final String URL_MESSAGE_INVITEE_LIST_FOR_NO_EPISODE = "secure/user/patientWise";
    public static final String URL_MESSAGE_CREATE_CONVERSATION = "secure/binders";
    public static final String URL_MESSAGE_GET_CONVERSATION = "secure/chat/unread/5";
    public static final String URL_MESSAGE_GET_NOTIFICATION = "secure/alert/list";
    public static final String URL_CHAT_GET_BINDER_PATIENT = "secure/binder";
    public static final String URL_CHAT_GET_BINDER_PATIENT_INVITEES = "secure/binders/member";
    public static final String URL_CHAT_SET_BINDER_PATIENT_INVITEES = "secure/binders/invite";
    public static final String URL_MESSAGE_INVITEE_LIST_FOR_PROVIDER_TO_PROVIDER = "secure/user/list/exceptme/formoxtra";
    public static final String URL_MESSAGE_INVITEE_LIST_FOR_PROVIDER_WITH_MULTIPLE_PATIENT = "secure/user/list/multi/patient";
    public static final String URL_MESSAGE_STARTMEET = "secure/meet/now";
    public static final String URL_SCHEDULE_MEET = "secure/schedule/meet";
    public static final String URL_MESSAGE_ACKNOWLEDGE = "secure/check/message/acknowledge";
    public static final String URL_BUSINESS_HOURS = "secure/businessHours/global";
    public static final String URL_SET_MESSAGE_ACKNOWLEDGE = "secure/message/acknowledge";
    public static final String URL_GET_PATIENTDIARY_EPISODELIST = "secure/patient/episode/list";

    /*Designate Meet*/
    public static final String URL_MEET_CHECK_IS_DESIGNATE_SELECT = "secure/check/designate";
    public static final String URL_MEET_CHECK_IS_DESIGNATE_SELECT_GLOBAL = "secure/get/designate/globalAndProvider/";
    public static final String URL_MEET_CHECK_IS_DESIGNATE_UN_SELECT = "secure/check/provider";

    /* Verify Mobile Screen*/
    public static final String REQUEST_KEY_VERIFY_MOB = "MobilePhone";
    public static final String REQUEST_KEY_VERIFY_COUNTRY = "Country_Code";
    public static final String REQUEST_KEY_VERIFY_MOB_VERY = "Mobile_Verification_Code";

    /* Forgot password Screen*/
    public static final String REQUEST_KEY_USERNAME = "Email";
    public static final String REQUEST_KEY_EMAIL = "UserName";
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
    public static final String PREF_IS_CHECKED = "isChecked";
    public static final String LOGIN_NAMECAREGIVER = "LOGIN_NAMECAREGIVER";

    public static final String CONST_PATIENT = "Patient";
    public static final String CONST_SURV_LIST_TODO = "To Do";
    public static final String CONST_SURV_LIST_SCHEDULE = "Scheduled";
    public static final String CONST_SURV_LIST_COMPLETED = "Completed";

    public static final int CONST_STEP_ONE = 1;
    public static final int CONST_STEP_TWO = 2;
    public static final int CONST_STEP_THREE = 3;
    public static final int CONST_STEP_FOUR = 4;
    public static final int CONST_STEP_RESET = 0;


}
