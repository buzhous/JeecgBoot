package org.jeecg.modules.app.entity.user;

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


@Data
@TableName("tbl_user")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(name = "用户表", description = "用户表")
public class AppUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private String id;

    @Excel(name = "账号", width = 15)
    @Schema(description = "账号")
    private String username;

    @Excel(name = "密码", width = 15)
    @Schema(description = "密码")
    private String password;

    @Excel(name = "昵称", width = 15)
    @Schema(description = "昵称")
    private String nickname;

    @Excel(name = "姓名", width = 15)
    @Schema(description = "姓名")
    private String realname;

    @Excel(name = "手机号", width = 15)
    @Schema(description = "手机号")
    private String phone;

    @Dict(dicCode = "USER_GENDER")
    @Excel(name = "性别", width = 15)
    @Schema(description = "性别")
    private Integer gender;

    @Excel(name = "头像", width = 15)
    @Schema(description = "头像")
    private String avatar;

    @Excel(name = "封面", width = 15)
    @Schema(description = "封面")
    private String thumb;

    @Excel(name = "身份证", width = 15)
    @Schema(description = "身份证")
    private String idCard;

    @Excel(name = "生日", width = 15)
    @Schema(description = "生日")
    private String birthday;

    @Excel(name = "语言", width = 15)
    @Schema(description = "语言")
    private String language;

    @Excel(name = "省", width = 15)
    @Schema(description = "省")
    private String province;

    @Excel(name = "市", width = 15)
    @Schema(description = "市")
    private String city;

    @Excel(name = "县", width = 15)
    @Schema(description = "县")
    private String country;

    @Excel(name = "openid", width = 15)
    @Schema(description = "openid")
    private String openId;

    @Excel(name = "unionid", width = 15)
    @Schema(description = "unionid")
    private String unionId;

    @Excel(name = "详细地址", width = 15)
    @Schema(description = "详细地址")
    private String address;

    @Excel(name = "介绍", width = 15)
    @Schema(description = "介绍")
    private String intro;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private java.util.Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "修改时间")
    private java.util.Date updateTime;

    @Dict(dicCode = "APP_USER_STATUS")
    @Excel(name = "状态", width = 15)
    @Schema(description = "状态")
    private Integer status;

    @Dict(dicCode = "YES_NO")
    @Excel(name = "是否锁定", width = 15)
    @Schema(description = "是否锁定")
    private Integer isLock;

    @Dict(dicCode = "YES_NO")
    @Excel(name = "是否注销", width = 15)
    @Schema(description = "注销客户")
    private Integer isDel;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后登陆时间")
    private java.util.Date lastLoginAt;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "注册时间")
    private java.util.Date registerAt;

    private String salt;

    @TableField(exist = false)
    @Schema(description = "设备Id")
    private String deviceId;

}