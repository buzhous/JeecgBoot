package org.jeecg.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Data
@TableName("tbl_topic")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(name = "tbl_topic对象", description = "主题管理")
public class Topic implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private String id;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "主题编码")
    private String topicCode;

    @Schema(description = "主题名称")
    private String topicName;

    @Schema(description = "主题类型")
    private Integer topicType;

    @Schema(description = "主题ICON")
    private String topicIcon;

    @Schema(description = "主题描述")
    private String topicDesc;

    @Schema(description = "主题分类")
    private Integer topicCate;

    @Schema(description = "主题状态")
    private Integer topicStatus;

    @Schema(description = "物品分类")
    private Integer itemCate;

    @Schema(description = "是否热门")
    private Integer isHot;

    @Schema(description = "主题价格")
    private Integer price;

    @Schema(description = "是否收费")
    private Integer isCharge;

    @Schema(description = "主题字段")
    private String fields;

}