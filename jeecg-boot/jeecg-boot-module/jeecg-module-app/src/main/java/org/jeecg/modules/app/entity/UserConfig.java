package org.jeecg.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("tbl_user_config")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(name = "tbl_user_config对象", description = "用户配置")
public class UserConfig implements Serializable {

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

    @Excel(name = "用户Id", width = 15)
    @Schema(description = "用户Id")
    private String userId;

    @Schema(description = "设备Id")
    private String deviceId;

    @Excel(name = "是否刷新标签", width = 15)
    @Schema(description = "是否刷新标签")
    private Integer isRefreshTag;

    @Schema(description = "是否需要更新线上物品")
    private Integer isSyncOnlineItem;

    @Schema(description = "是否设备首次登录")
    private Integer isDeviceFirst;

    @Excel(name = "系统设置项", width = 15)
    @Schema(description = "系统设置项")
    private String configs;

    @TableField(exist = false)
    private String userAccount;

}