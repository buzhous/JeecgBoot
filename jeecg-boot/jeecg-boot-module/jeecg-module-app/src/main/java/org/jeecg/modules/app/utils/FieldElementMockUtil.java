package org.jeecg.modules.app.utils;

import org.jeecg.modules.app.bean.enums.FieldElementEnum;
import org.jeecg.modules.app.bean.vo.fields.ElementContentVO;
import org.jeecg.modules.app.bean.vo.fields.FieldAttributeVO;
import org.jeecg.modules.app.bean.vo.fields.FieldElementVO;

import java.util.ArrayList;
import java.util.List;

public class FieldElementMockUtil {

    /**
     * 模拟名称字段元素
     *
     * @param name 名称
     * @return 字段元素VO
     */
    public static FieldElementVO mockNameFieldElement(String name) {
        FieldElementVO fieldElementVO = new FieldElementVO();

        fieldElementVO.setElement(FieldElementEnum.name.getElement());
        fieldElementVO.setName(FieldElementEnum.name.name());
        fieldElementVO.setSort(1);
        fieldElementVO.setIsShow(1);

        FieldAttributeVO fieldAttributeVO = new FieldAttributeVO();
        fieldElementVO.setAttributes(fieldAttributeVO);

        ElementContentVO elementContentVO = new ElementContentVO();
        elementContentVO.setName(name != null ? name : FieldElementEnum.name.getEleName());
        fieldElementVO.setContent(elementContentVO);

        return fieldElementVO;
    }

    /**
     * 模拟ICON字段元素
     *
     * @param iconName ICON名称
     * @return 字段元素VO
     */
    public static FieldElementVO mockIconFieldElement(String iconName) {
        FieldElementVO fieldElementVO = new FieldElementVO();
        fieldElementVO.setElement(FieldElementEnum.icon.getElement());
        fieldElementVO.setName(FieldElementEnum.icon.name());
        fieldElementVO.setSort(1);
        fieldElementVO.setIsShow(1);
        FieldAttributeVO fieldAttributeVO = new FieldAttributeVO();
        fieldElementVO.setAttributes(fieldAttributeVO);
        ElementContentVO elementContentVO = new ElementContentVO();
        elementContentVO.setName(iconName != null ? iconName : FieldElementEnum.name.getEleName());
        fieldElementVO.setContent(elementContentVO);
        return fieldElementVO;
    }

    /**
     * 模拟描述字段元素
     *
     * @param description 描述
     * @return 字段元素VO
     */
    public static FieldElementVO mockDescriptionFieldElement(String description) {
        FieldElementVO fieldElementVO = new FieldElementVO();
        fieldElementVO.setElement(FieldElementEnum.description.getElement());
        fieldElementVO.setName(FieldElementEnum.description.name());
        fieldElementVO.setSort(1);
        fieldElementVO.setIsShow(1);
        FieldAttributeVO fieldAttributeVO = new FieldAttributeVO();
        fieldElementVO.setAttributes(fieldAttributeVO);
        ElementContentVO elementContentVO = new ElementContentVO();
        elementContentVO.setName(description != null ? description : FieldElementEnum.description.getEleName());
        fieldElementVO.setContent(elementContentVO);
        return fieldElementVO;
    }

    /**
     * 模拟类别字段元素
     *
     * @param category 类别
     * @return 字段元素VO
     */
    public static FieldElementVO mockCategoryFieldElement(String category) {
        FieldElementVO fieldElementVO = new FieldElementVO();
        fieldElementVO.setElement(FieldElementEnum.category.getElement());
        fieldElementVO.setName(FieldElementEnum.category.name());
        fieldElementVO.setSort(1);
        fieldElementVO.setIsShow(1);
        FieldAttributeVO fieldAttributeVO = new FieldAttributeVO();
        fieldElementVO.setAttributes(fieldAttributeVO);
        ElementContentVO elementContentVO = new ElementContentVO();
        elementContentVO.setName(category != null ? category : FieldElementEnum.category.getEleName());
        fieldElementVO.setContent(elementContentVO);
        return fieldElementVO;
    }

    /**
     * 模拟等级字段元素
     *
     * @param level 等级
     * @return 字段元素VO
     */
    public static FieldElementVO mockLevelFieldElement(String level) {
        FieldElementVO fieldElementVO = new FieldElementVO();
        fieldElementVO.setElement(FieldElementEnum.level.getElement());
        fieldElementVO.setName(FieldElementEnum.level.name());
        fieldElementVO.setSort(1);
        fieldElementVO.setIsShow(1);
        FieldAttributeVO fieldAttributeVO = new FieldAttributeVO();
        fieldElementVO.setAttributes(fieldAttributeVO);
        ElementContentVO elementContentVO = new ElementContentVO();
        elementContentVO.setName(level != null ? level : FieldElementEnum.level.getEleName());
        fieldElementVO.setContent(elementContentVO);
        return fieldElementVO;
    }

    public static List<FieldElementVO> mockElementFieldElementBase() {
        List<FieldElementVO> fieldElements = new ArrayList<>();
        fieldElements.add(mockNameFieldElement("测试名称"));
        fieldElements.add(mockIconFieldElement("测试图标"));
        fieldElements.add(mockCategoryFieldElement("测试类别"));
        fieldElements.add(mockLevelFieldElement("测试等级"));
        fieldElements.add(mockDescriptionFieldElement("测试描述"));
        return fieldElements;
    }

}
