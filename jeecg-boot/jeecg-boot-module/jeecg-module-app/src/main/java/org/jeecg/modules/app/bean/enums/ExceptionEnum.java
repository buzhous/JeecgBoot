package org.jeecg.modules.app.bean.enums;

/**
 * 系统状态码及错误码
 *
 * @version 1.0
 * @description: 状态码及错误码
 */
public enum ExceptionEnum {

    // 系统错误
    SUCCESS(200, "success"),
    SYSTEM_ERROR(-1, "系统异常，请稍后再试"),

    // 登录/用户 10100-10199
    ACCESS_DENIED(10100, "访问未授权"),
    REQUEST_TIMEOUT(10101, "请求超时"),
    TOKEN_INVALID_ERROR(10102, "Token Invalid"),
    USER_INFO_ERROR(10103, "用户信息错误"),
    PASSWORD_ERROR(10104, "账号密码错误"),
    PHONE_ERROR(10105, "手机号格式错误"),
    SMS_CODE_ERROR(10106, "短信验证码错误"),
    PASSWORD_NOT_MATCH(10107, "密码不匹配"),
    USER_INFO_NOT_EXIST(10108, "用户信息不存在"),
    // 物品 10200-10500
    ITEM_NOT_EXIST(10200, "物品不存在"),
    ITEM_DESTROYED(10201, "物品已被销毁"),
    // 优惠券 10500-10600
    COUPON_NOT_EXIST(10500, "优惠券不存在"),
    COUPON_ALREADY_PUBLISHED(10501, "优惠券已发布"),
    COUPON_ALREADY_OFFLINE(10502, "优惠券已下架"),
    COUPON_NOT_AVAILABLE(10503, "优惠券不可领取"),
    COUPON_STOCK_EMPTY(10504, "优惠券已领完"),
    COUPON_NOT_IN_VALID_TIME(10505, "优惠券不在领取时间范围内"),
    COUPON_RECEIVE_LIMIT_EXCEEDED(10506, "已达到领取上限"),
    COUPON_DAILY_LIMIT_EXCEEDED(10511, "今日已达领取上限"),
    USER_COUPON_NOT_EXIST(10507, "用户优惠券不存在"),
    COUPON_NOT_BELONG_TO_USER(10508, "优惠券不属于该用户"),
    COUPON_ALREADY_USED(10509, "优惠券已使用"),
    COUPON_EXPIRED(10510, "优惠券已过期"),
    // 通用业务错误 11000-12000
    DATA_UPDATE_ERROR(11000, "更新失败"),
    DATA_DELETE_ERROR(11001, "删除失败"),
    REQUEST_PARAM_ERROR(11002, "请求参数错误"),
    LOCK_FAILED(11003, "锁获取失败"),
    ;

    private int code;
    private String msg;

    ExceptionEnum() {
    }

    ExceptionEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static String getValue(int code) {
        for (ExceptionEnum value : ExceptionEnum.values()) {
            if (value.getCode() == code) {
                return value.getMsg();
            }
        }
        return null;
    }

}
