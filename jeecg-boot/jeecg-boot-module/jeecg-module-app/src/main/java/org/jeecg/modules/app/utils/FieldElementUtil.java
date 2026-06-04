package org.jeecg.modules.app.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import org.jeecg.modules.app.bean.vo.fields.ElementAttributeVO;
import org.jeecg.modules.app.bean.vo.fields.ElementTagVO;
import org.jeecg.modules.app.bean.vo.fields.ExtendDataVO;
import org.jeecg.modules.app.bean.vo.fields.FieldElementVO;

import java.lang.reflect.Method;

public class FieldElementUtil {

    /**
     * 反向转换：将VO对象的List/Object字段转换为JSON字符串
     * 根据源对象的字段进行反向转换：
     * - List字段转换为JSON数组字符串
     * - Object字段转换为JSON对象字符串
     *
     * @param source 源对象（包含List/Object字段）
     * @param target 目标对象（包含String类型字段用于存储JSON）
     * @param <T> 源对象类型
     * @param <R> 目标对象类型
     * @return 转换后的目标对象
     */
    public static <T, R> R convertToJsonFields(T source, R target) {
        // 先拷贝类型匹配的字段
        BeanUtil.copyProperties(source, target, CopyOptions.create()
                .ignoreNullValue()
                .ignoreError()
        );
        // 定义需要转换为JSON的字段映射
        String[] jsonFields = {"fields", "tags", "attributes", "extendData"};
        for (String fieldName : jsonFields) {
            try {
                // 获取源对象的字段值
                Method getter = source.getClass().getMethod("get" + capitalize(fieldName));
                Object value = getter.invoke(source);
                if (value != null) {
                    // 将值转换为JSON字符串
                    String jsonStr = JSON.toJSONString(value);
                    // 设置到目标对象
                    Method setter = target.getClass().getMethod("set" + capitalize(fieldName), String.class);
                    setter.invoke(target, jsonStr);
                }
            } catch (Exception e) {
                // 字段不存在或类型不匹配，跳过
            }
        }
        return target;
    }

    // =======================================================================================================

    /**
     * 通用对象转换方法
     * 根据目标对象的字段进行智能转换：
     * - 基础类型字段直接拷贝
     * - JSON字符串字段自动解析为对应的List或Object
     *
     * @param source 源对象
     * @param target 目标对象
     * @param <T> 源对象类型
     * @param <R> 目标对象类型
     * @return 转换后的目标对象
     */
    public static <T, R> R convertObject(T source, R target) {
        // 拷贝类型匹配的字段，忽略类型不匹配的字段
        BeanUtil.copyProperties(source, target, CopyOptions.create()
                .ignoreNullValue()
                .ignoreError()
        );

        // 通过反射获取源对象的属性值
        String fieldsStr = getFieldValue(source, "fields");
        String tagsStr = getFieldValue(source, "tags");
        String attributesStr = getFieldValue(source, "attributes");
        String extendDataStr = getFieldValue(source, "extendData");

        // 根据目标对象的字段进行转换
        setFieldValueIfExists(target, "fields", fieldsStr, FieldElementVO.class);
        setFieldValueIfExists(target, "tags", tagsStr, ElementTagVO.class);
        setFieldValueIfExists(target, "attributes", attributesStr, ElementAttributeVO.class);
        setFieldValueIfExists(target, "extendData", extendDataStr, ExtendDataVO.class);

        return target;
    }

    // =======================================================================================================

    /**
     * 通过反射获取对象的字段值
     */
    private static <T> String getFieldValue(T obj, String fieldName) {
        try {
            Method getter = obj.getClass().getMethod("get" + capitalize(fieldName));
            Object value = getter.invoke(obj);
            return value != null ? value.toString() : null;
        } catch (Exception e) {
            // 字段不存在或其他异常，返回null
            return null;
        }
    }

    /**
     * 如果目标对象存在指定字段，则设置其值（JSON字符串转对象）
     */
    private static <R, V> void setFieldValueIfExists(R target, String fieldName, String jsonStr, Class<V> valueType) {
        if (StringUtils.isEmpty(jsonStr)) {
            return;
        }

        try {
            Method setter = target.getClass().getMethod("set" + capitalize(fieldName), java.util.List.class);
            // 目标字段是List类型，解析为List
            java.util.List<V> list = JSON.parseArray(jsonStr, valueType);
            setter.invoke(target, list);
        } catch (NoSuchMethodException e) {
            // 目标对象没有该List字段，尝试作为单个对象设置
            try {
                Method objSetter = target.getClass().getMethod("set" + capitalize(fieldName), valueType);
                Object obj = JSON.parseObject(jsonStr, valueType);
                objSetter.invoke(target, obj);
            } catch (Exception ex) {
                // 目标对象也没有该Object字段，忽略
            }
        } catch (Exception e) {
            // 其他异常，忽略
        }
    }

    /**
     * 首字母大写
     */
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}