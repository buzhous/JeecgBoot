package org.jeecg.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;


@Data
@TableName("tbl_item_ruins")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "tbl_item_ruins")
public class ItemRuins implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private java.lang.Long id;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private java.util.Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "修改时间")
    private java.util.Date updateTime;

    @Schema(description = "原始Id")
    private java.lang.String oriId;

    @Schema(description = "主题编码")
    private java.lang.String itemId;

    @Schema(description = "分类ID")
    private java.lang.String category;

    @Schema(description = "主题名称")
    private java.lang.String name;

    @Schema(description = "主题类型")
    private java.lang.String icon;

    @Schema(description = "数量")
    private java.lang.Integer quantity;

    @Schema(description = "主题描述")
    private java.lang.Integer level;

    @Schema(description = "价格")
    private java.math.BigDecimal price;

    @Schema(description = "描述")
    private java.lang.String description;

    @Schema(description = "排序")
    private java.lang.Integer sort;

    @Schema(description = "用户Id")
    private java.lang.String userId;

    @Schema(description = "状态: 0待拾取，1已拾取，2收回")
    private java.lang.Integer status;

    @Schema(description = "组件列表")
    private java.lang.String fields;

    @Schema(description = "拓展数据")
    private java.lang.String extendData;

    @Schema(description = "属性列表")
    private java.lang.String attributes;

    @Schema(description = "标签列表")
    private java.lang.String tags;


}
