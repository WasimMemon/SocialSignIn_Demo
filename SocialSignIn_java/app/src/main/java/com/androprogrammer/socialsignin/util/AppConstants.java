package com.androprogrammer.socialsignin.util;

public class AppConstants {

    public static final String INVALID_TOKEN = "InvalidToken";

    public static final String SESSION_EXPIRED = "Session Expired.";

    public static final int SPLASH_DURATION = 1000;
    public static final int MAX_ATTEMPTS = 5;


    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    public class PlanActivationKeys {

        public static final int KEY_PLAN_ACTIVATED = 10003;
        public static final int KEY_PLAN_EXPIRED = 10001;

    }

    public class APICode {
        public static final int LOGIN_TO_TWITTER= 100;
        public static final int AUTHENTICATION = 1000;
        public static final int LOG_OUT = 1001;
        public static final int SIGN_UP = 1002;





    }

    public class INTENTDATA {

        public static final String EXTRA_AUTH_URL_KEY = "URL_AUTH_KEY";
        public static final String EXTRA_CALLBACK_URL_KEY = "URL_CALLBACK_KEY";

        public static final String USERDETAIL_MODEL = "UserDataModel";
        public static final String SIMPLE_SIGNUP = "Normal_Singup";
        public static final String EDIT_MODE = "isEditMode";
        public static final String USERDETAIL_BUNDLE = "UserData";
        public static final String USERDETAIL_IMAGE = "UserImage";


    }

    public class ResponseKey {

        public static final String IsSuccess = "IsSuccess";
        public static final String Message = "Message";
        public static final String Data = "Data";
        public static final String Token = "Token";
        public static final String ErrorCode = "ErrorCode";


    }


    public class SharedPreferenceKeys {
        public static final String IS_DbCreated = "dbCreated";
        public static final String IS_LoggedIn = "IsLoggedIn";
        public static final String Twitter_Secret = "TwitterAccessSecret";
        public static final String Twitter_Token = "TwitterAccessToken";
        public static final String Twitter_UserId = "UserId";
        public static final String Twitter_UserTag = "UserTag";


        public static final String ToUserId = "ToUserId";


    }

    public class DialogIdentifier {
        public static final int LogOut = 1;
        public static final int Forgot_Password = 2;
        public static final int FINISH_ACTIVITY = 3;
        public static final int LOGIN_DEVICECHANGE = 4;
        public static final int POST_NEWS = 5;
        public static final int HIDE_CALLS_CONFIRMATION = 6;
        public static final int HIDE_CALLS = 7;
        public static final int CHECK_MOBILE_VERIFICATION = 8;
		public static final int UPDATE_CALL_DETAIL = 9;
    }

    public class ErrorCodes {
        public static final int SESSIONEXPIRE = 101;
        public static final int APP_ERROR_ANR = 102;
        public static final int ERROR_NOTVERIFIED = 104;
    }
}
