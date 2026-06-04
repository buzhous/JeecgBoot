package org.jeecg.modules.app.bean.enums;

/**
 * @version 1.0
 * @description: 字段元素枚举
 */
public enum FieldElementEnum {

    name(1, "名称"),
    icon(2, "图标"),
    category(3, "类别"),
    level(4, "等级"),
    purchaseTime(5, "购入时间"),
    price(6, "价格"),
    quantity(7, "数量"),
    description(8, "描述"),
    ;

    private Integer element;
    private String eleName;

    FieldElementEnum() {
    }

    FieldElementEnum(int element, String eleName) {
        this.element = element;
        this.eleName = eleName;
    }

    public int getElement() {
        return element;
    }

    public void setElement(int element) {
        this.element = element;
    }

    public String getEleName() {
        return eleName;
    }

    public void setEleName(String eleName) {
        this.eleName = eleName;
    }

    public static String getValue(int element) {
        for (FieldElementEnum value : FieldElementEnum.values()) {
            if (value.getElement() == element) {
                return value.getEleName();
            }
        }
        return null;
    }

}
