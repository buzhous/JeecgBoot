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

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("tbl_user_give_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "赠送记录")
public class UserGiveRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description= "主键ID")
    private String id;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description= "记录创建时间")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description= "记录更新时间")
    private Date updateTime;

    @Schema(description= "赠送者用户ID（")
    private String giveUid;

    @Schema(description= "接收者用户ID")
    private String receiveUid;

    @Schema(description= "物品ID（关联商品表主键）")
    private String itemId;

    @Schema(description= "库存数量")
    private Integer quantity;

    @Schema(description= "物品名称")
    private String itemName;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description= "赠送时间")
    private Date giveTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description= "领取时间")
    private Date receiveTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description= "过期时间")
    private Date expiredTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description= "取回过期时间")
    private Date getExpiredTime;

    // 状态主语：赠送人
    @Schema(description= "赠送状态：1-未领取 2-已领取 3-已撤回 4-已过期")
    private Integer status;

    @Schema(description= "备注（如赠送留言等）")
    private String remark;

    @Schema(description = "取回过期状态：0无，1已过取回期限")
    private Integer getExpiredStatus;

}
