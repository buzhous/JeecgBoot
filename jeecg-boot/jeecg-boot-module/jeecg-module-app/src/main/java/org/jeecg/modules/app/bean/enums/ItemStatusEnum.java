package org.jeecg.modules.app.bean.enums;

/**
 * 物品状态
 */
public enum ItemStatusEnum {

    // 同步状态：0-未同步 1-成功 2-失败
    TEMPORARY(0, "临时"),
    NORMAL(1, "正常"),
    USING(2, "使用中"),
    ;
    private int code;
    private String name;

    ItemStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
