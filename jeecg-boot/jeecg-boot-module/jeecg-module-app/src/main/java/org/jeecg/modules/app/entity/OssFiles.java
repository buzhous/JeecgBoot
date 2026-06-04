package org.jeecg.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
@TableName("tbl_oss_files")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(name = "tbl_oss_files对象", description = "文件管理")
public class OssFiles implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    private String id;

    @Excel(name = "临时验证Token", width = 15)
    @Schema(description = "临时验证Token")
    private String token;

    @Excel(name = "文件名称", width = 15)
    @Schema(description = "文件名称")
    private String fileName;

    @Excel(name = "文件地址", width = 15)
    @Schema(description = "文件地址")
    private String url;

    @Excel(name = "文档类型（folder:文件夹 excel:excel doc:word ppt:ppt image:图片  archive:其他文档 video:视频 pdf:pdf）", width = 15)
    @Schema(description = "文档类型（folder:文件夹 excel:excel doc:word ppt:ppt image:图片  archive:其他文档 video:视频 pdf:pdf）")
    private String fileType;

    @Excel(name = "文件上传类型(temp/本地上传(临时文件) manage/知识库)", width = 15)
    @Schema(description = "文件上传类型(temp/本地上传(临时文件) manage/知识库)")
    private String storeType;

    @Excel(name = "父级id", width = 15)
    @Schema(description = "父级id")
    private String parentId;

    @Excel(name = "租户id", width = 15)
    @Schema(description = "租户id")
    private String tenantId;

    @Excel(name = "文件大小（kb）", width = 15)
    @Schema(description = "文件大小（kb）")
    private Double fileSize;

    @Excel(name = "是否文件夹(1：是  0：否)", width = 15)
    @Schema(description = "是否文件夹(1：是  0：否)")
    private String izFolder;

    @Excel(name = "是否为1级文件夹，允许为空 (1：是 )", width = 15)
    @Schema(description = "是否为1级文件夹，允许为空 (1：是 )")
    private String izRootFolder;

    @Excel(name = "是否标星(1：是  0：否)", width = 15)
    @Schema(description = "是否标星(1：是  0：否)")
    private String izStar;

    @Excel(name = "下载次数", width = 15)
    @Schema(description = "下载次数")
    private Integer downCount;

    @Schema(description = "阅读次数")
    private Integer readCount;

    @Schema(description = "分享链接")
    private String shareUrl;

    @Schema(description = "分享权限(1.关闭分享 2.允许所有联系人查看 3.允许任何人查看)")
    private String sharePerms;

    @Schema(description = "是否允许下载(1：是  0：否)")
    private String enableDown;

    @Schema(description = "是否允许修改(1：是  0：否)")
    private String enableUpdat;

    @Schema(description = "删除状态(0-正常,1-删除至回收站)")
    @TableLogic
    private Integer delFlag;

    @Schema(description = "使用状态：0 未使用，1已使用")
    private Integer usageStatus;

    @Schema(description = "创建人登录名称")
    private String createBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建日期")
    private Date createTime;

    @Schema(description = "更新人登录名称")
    private String updateBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新日期")
    private Date updateTime;

}
