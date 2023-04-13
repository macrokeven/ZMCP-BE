package com.letoy.main.utils;

public class Constants {

    //预期外的错误
    public static final int UNEXPECTED_SYSTEM_ERROR = 8;

    public static final int NO_AUTHENTICATION = 7;

    public static final int TOKEN_INVALID = 6;

    //一般指数据库的操作，或者正常操作中出现的异常
    public static final int EXPECTED_ERROR = 9;
    public static final int LOGIN_FAILED = 22;

    public static final int REQUEST_TOO_MANY_TIMES = 5;

    public static final String DEFAULT_PHONE_NUMBER = "phoneNumber";

    public static final String DEFAULT_ACTIVE_CODE = "activeCode";

    public static final int SYSTEM_ERROR = 10;

    public static final int PHONE_NUMBER_DUPLICATED = 5;


    public static final int USER_NOT_ACTIVATE = 15;

    public static final int USER_PASSWORD_INCORRECT = 16;

    public static final int NORMAL_USER = 55;

    public static final int ADMIN_USER = 66;


    public static final int ORDER_PROCESSING = 1;

    public static final int ORDER_FINISHED = 5;


    public static final int INITIALIZATION = 0;

    public static final int SYSTEM_MODULE = 0;

    public static final int EVENT_INFO_MODULE = 1;

    public static final int USER_INFO_MODULE = 2;
    public static final int ACTIVATE_CODE_MODULE = 3;

    public static final int TRANSACTION_LOG = 0;
    public static final int SYSTEM_WARNING_LOG = 1;
    public static final int SYSTEM_ERROR_LOG = 2;
    public static final int SYSTEM_ACCESS_LOG = 3;

    public static final int USER_INFO_TRANSACTION = 1;
    public static final int EVENT_INFO_TRANSACTION = 1;


    //操作返回失败
    public static final int FAIL = 10;

    public static final int SUCCESS = 0;
    public static final String LOGIN_SMS_TEMPLATE_ID = "1296069";
    public static final String RESET_SMS_TEMPLATE_ID = "1583298";

    public static final int LOGIN_ACTIVE_CODE = 0;
    public static final int RESET_PHONE_ACTIVE_CODE = 1;

    public static String getLogType(int logType) {
        return switch (logType) {
            case TRANSACTION_LOG -> "<div class='transaction' >事务</div>";
            case SYSTEM_WARNING_LOG -> "<div class='warning' >警告</div>";
            case SYSTEM_ERROR_LOG -> "<div class='error' >错误</div>";
            case SYSTEM_ACCESS_LOG -> "<div class='access' >访问</div>";
            default -> "";
        };
    }

    public static String getMsg(int ERROR_CODE) {
        String msg = "";
        return switch (ERROR_CODE) {
            case SYSTEM_ERROR -> "系统错误";
            case USER_NOT_ACTIVATE -> "未缴费！";
            case USER_PASSWORD_INCORRECT -> "用户密码错误";
            default -> msg;
        };
    }


}
