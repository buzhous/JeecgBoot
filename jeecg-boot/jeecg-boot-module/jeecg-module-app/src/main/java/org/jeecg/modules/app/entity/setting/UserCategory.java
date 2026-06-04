package org.jeecg.modules.app.entity.setting;

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


@Data
@TableName("tbl_user_category")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "用户分类")
public class UserCategory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private String id;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private java.util.Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "修改时间")
    private java.util.Date updateTime;

    @Excel(name = "用户Id", width = 15)
    @Schema(description = "用户Id")
    private java.lang.String userId;

    @Excel(name = "名称", width = 15)
    @Schema(description = "名称")
    private java.lang.String name;

    @Excel(name = "图标", width = 15)
    @Schema(description = "图标")
    private java.lang.String icon;

    @Excel(name = "排序", width = 15)
    @Schema(description = "排序")
    private java.lang.Integer sort;

    @Excel(name = "系统分类", width = 15)
    @Schema(description = "系统分类")
    private java.lang.Integer isSys;
}
