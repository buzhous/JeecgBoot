package org.jeecg.modules.app.bean.enums;

/**
 * 用户物品状态
 */
public enum InventoryStatusEnum {

    // 同步状态：0-未同步 1-成功 2-失败
    TEMPORARY(0, "临时"),
    NORMAL(1, "正常"),
    USING(2, "使用中"),
    GIFTED(3, "已赠送"),
    DESTROYED(4, "已销毁"),
    ;
    private int code;
    private String name;

    InventoryStatusEnum(int code, String name) {
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
