package org.jeecg.modules.app.constant;

/**
 * 用户常量信息
 *
 * @author elinx
 */
public class UserConstant {

    /**
     * 用户信息缓存键名
     */
    public static String USER_INFO_CACHE = "app:userinfo:userid:";

    /**
     * 用户信息缓存有效期
     */
    public static Long USER_INFO_TTL = 86400 * 30 * 12L;

    /**
     * 登录短信验证码缓存键名
     */
    public static String LOGIN_SMS_CODE_CACHE = "app:userinfo:smscode:";

    /**
     * 登录短信验证码缓存有效期
     */
    public static int LOGIN_SMS_CODE_TTL = 60 * 5;

    /**
     * 用户缓存-用户名
     */
    public static String USER_INFO_USERNAME_CACHE = "app:userinfo:username:";

    /**
     * 用户缓存-手机号
     */
    public static String USER_INFO_PHONE_CACHE = "app:userinfo:phone:";

    /**
     * 用户缓存-token
     */
    public static String USER_INFO_TOKEN_CACHE = "app:userinfo:token:";


}
