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
import java.math.BigDecimal;
import java.util.Date;


@Data
@TableName("tbl_item_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(name = "物品表", description = "物品表")
public class ItemInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private java.lang.Long id;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "原始Id")
    private String oriId;

    @Schema(description = "分类ID")
    private java.lang.String category;

    @Schema(description = "主题名称")
    private String name;

    @Schema(description = "主题类型")
    private String icon;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "主题描述")
    private Integer level;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "组件列表")
    private String fields;

    @Schema(description = "拓展数据")
    private String extendData;

    @Schema(description = "属性列表")
    private String attributes;

    @Schema(description = "标签列表")
    private String tags;

    @Schema(description = "用户Id")
    private String userId;


}
