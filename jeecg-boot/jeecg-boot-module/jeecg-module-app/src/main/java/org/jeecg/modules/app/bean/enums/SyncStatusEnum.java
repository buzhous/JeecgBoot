package org.jeecg.modules.app.bean.enums;

/**
 * 物品同步状态
 */
public enum SyncStatusEnum {

    // 同步状态：0-未同步 1-成功 2-失败
    UNSYNCED(0, "未同步"),
    SUCCESS(1, "成功"),
    FAIL(2, "失败"),
    DESTROYED(3, "已销毁"),
    ;
    private int code;
    private String name;

    SyncStatusEnum(int code, String name) {
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
