package org.jeecg.modules.app.bean.enums;

/**
 * 物品拾取状态
 */
public enum RuinsStatusEnum {

    // 拾取状态：0-未拾取 1-已拾取 2-已收回 3-已充公
    NOT_PICKUP(0, "未拾取"),
    PICKUP(1, "已拾取"),
    RECALLED(2, "已收回"),
    CONFISCATED(3, "已充公"),
    ;
    private int code;
    private String name;

    RuinsStatusEnum(int code, String name) {
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
