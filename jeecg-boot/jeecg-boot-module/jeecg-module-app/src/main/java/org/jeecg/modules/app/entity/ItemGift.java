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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 物品赠送记录
 */
@Data
@TableName("tbl_item_gift")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(name = "物品赠送记录", description = "物品赠送记录")
public class ItemGift implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "ID")
    private String id;

    @Schema(description = "赠送类型：1-指定用户赠送，2-公开赠送", example = "1")
    private Integer giftType;

    @Schema(description = "赠送状态：0-待领取，1-部分领取，2-已领完，3-已撤回，4-已过期")
    private Integer status;

    @Schema(description = "赠送者用户ID")
    private String senderUserId;

    @Schema(description = "接收者用户ID（指定赠送时使用）")
    private String receiverUserId;

    @Schema(description = "物品ID")
    private String itemId;

    @Schema(description = "物品原始ID")
    private String oriId;

    @Schema(description = "库存ID")
    private String inventoryId;

    @Schema(description = "赠送总数量")
    private Integer totalQuantity;

    @Schema(description = "已领取数量")
    private Integer receivedQuantity;

    @Schema(description = "兑换码")
    private String giftCode;

    @Schema(description = "二维码内容（Base64）")
    private String qrCode;

    @Schema(description = "领取有效期开始时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validStartTime;

    @Schema(description = "领取有效期结束时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date validEndTime;

    @Schema(description = "每人限领数量（公开赠送时使用）")
    private Integer perLimit;

    @Schema(description = "赠送备注")
    private String remark;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "撤回时间")
    private Date cancelTime;

    @TableField(exist = false)
    @Schema(description = "物品信息")
    private ItemInfo itemInfo;

    @TableField(exist = false)
    @Schema(description = "赠送者信息")
    private String senderNickname;

    @TableField(exist = false)
    @Schema(description = "接收者信息")
    private String receiverNickname;

}