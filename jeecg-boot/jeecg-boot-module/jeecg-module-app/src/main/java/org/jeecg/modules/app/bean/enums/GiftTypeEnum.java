package org.jeecg.modules.app.bean.enums;

/**
 * 赠送类型枚举
 */
public enum GiftTypeEnum {

    DIRECT(1, "指定用户赠送"),
    PUBLIC(2, "公开赠送"),
    ;

    private int code;
    private String name;

    GiftTypeEnum(int code, String name) {
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