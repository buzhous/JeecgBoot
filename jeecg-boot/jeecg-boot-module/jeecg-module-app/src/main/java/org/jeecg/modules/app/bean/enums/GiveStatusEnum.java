package org.jeecg.modules.app.bean.enums;

/**
 * 物品赠送状态
 */
public enum GiveStatusEnum {

    // 赠送状态：1-未领取 2-已领取 3-已撤回 4-已过期 5-已放弃 6-已回收
    UNCLAIMED(0, "未领取"),
    WITHDRAW(1, "已撤回"),
    RECEIVE(2, "已领取"),
    EXPIRED(3, "已过期"),
    GIVE_UP(4, "已放弃"),
    RECLAIMED(5, "已回收"),
    ;

    private int code;
    private String name;

    GiveStatusEnum(int code, String name) {
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
