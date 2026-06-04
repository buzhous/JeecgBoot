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
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Data
@TableName("tbl_user_amount")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(name = "tbl_user_amount对象", description = "用户资金")
public class UserAmount implements Serializable {

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

    @Excel(name = "用户Id", width = 15)
    @Schema(description = "用户Id")
    private String userId;

    @TableField(exist = false)
    private String userAccount;

    @Excel(name = "灵石金额", width = 15)
    @Schema(description = "灵石金额")
    private Integer fairyStoneAmount;

    @Excel(name = "金币金额", width = 15)
    @Schema(description = "金币金额")
    private Integer goldCoinAmount;

    @Dict(dicCode = "YES_NO")
    @Excel(name = "账户锁定", width = 15)
    @Schema(description = "账户锁定")
    private Integer isLock;

    @Excel(name = "锁定时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "锁定时间")
    private Date lockTime;

}