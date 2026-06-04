package org.jeecg.modules.app.bean.enums;

/**
 * 赠送状态枚举
 */
public enum GiftStatusEnum {

    PENDING(0, "待领取"),
    PARTIAL_RECEIVED(1, "部分领取"),
    FULL_RECEIVED(2, "已领完"),
    CANCELLED(3, "已撤回"),
    EXPIRED(4, "已过期"),
    ;

    private int code;
    private String name;

    GiftStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}