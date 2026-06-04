package org.jeecg.modules.app.bean.enums;

/**
 * 物品同步操作类型
 */
public enum SyncOpsEnum {

    ADD("add", "新增"),
    EDIT("edit", "修改"),
    DESTROY("destroy", "已销毁"),
    DEFAULT("default", "默认"),
    ;
    private String code;
    private String name;

    SyncOpsEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static SyncOpsEnum fromCode(String code) {
        for (SyncOpsEnum e : SyncOpsEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return SyncOpsEnum.DEFAULT;
    }

}
