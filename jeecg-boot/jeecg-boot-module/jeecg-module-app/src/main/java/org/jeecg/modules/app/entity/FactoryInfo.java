package org.jeecg.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Data
@TableName("tbl_factory_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(name = "tbl_factory_info对象", description = "工坊管理")
public class FactoryInfo implements Serializable {

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

    @Excel(name = "工坊名称", width = 15)
    @Schema(description = "工坊名称")
    private String factoryName;

    @Excel(name = "工坊编码", width = 15)
    @Schema(description = "工坊编码")
    private String factoryCode;

    @Excel(name = "工坊图标", width = 15)
    @Schema(description = "工坊图标")
    private String factoryIcon;

    @Excel(name = "工坊描述", width = 15)
    @Schema(description = "工坊描述")
    private String factoryDesc;

    @Excel(name = "工坊详情介绍", width = 15)
    @Schema(description = "工坊详情介绍")
    private String factoryText;

    @Excel(name = "工坊拥有者", width = 15)
    @Schema(description = "工坊拥有者")
    private Integer factoryOwnerId;

    @Excel(name = "工坊状态", width = 15)
    @Schema(description = "工坊状态")
    private Integer factoryStatus;

    @Excel(name = "审核时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "审核时间")
    private Date checkTime;

    @Excel(name = "审核人", width = 15)
    @Schema(description = "审核人")
    private Integer checkId;

}