package org.jeecg.modules.app.bean.vo.gift;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.app.entity.ItemInfo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 物品赠送记录列表VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "物品赠送记录列表VO")
public class ItemGiftListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "赠送记录ID")
    private String id;

    @Schema(description = "赠送类型：1-指定用户赠送，2-公开赠送")
    private Integer giftType;

    @Schema(description = "赠送类型名称")
    private String giftTypeName;

    @Schema(description = "赠送状态：0-待领取，1-部分领取，2-已领完，3-已撤回，4-已过期")
    private Integer status;

    @Schema(description = "赠送状态名称")
    private String statusName;

    @Schema(description = "赠送者用户ID")
    private String senderUserId;

    @Schema(description = "赠送者昵称")
    private String senderNickname;

    @Schema(description = "接收者用户ID（指定赠送时使用）")
    private String receiverUserId;

    @Schema(description = "接收者昵称")
    private String receiverNickname;

    @Schema(description = "物品ID")
    private String itemId;

    @Schema(description = "物品名称")
    private String itemName;

    @Schema(description = "物品图标")
    private String itemIcon;

    @Schema(description = "物品等级")
    private Integer itemLevel;

    @Schema(description = "物品信息")
    private ItemInfo itemInfo;

    @Schema(description = "赠送总数量")
    private Integer totalQuantity;

    @Schema(description = "已领取数量")
    private Integer receivedQuantity;

    @Schema(description = "兑换码")
    private String giftCode;

    @Schema(description = "二维码内容（Base64）")
    private String qrCode;

    @Schema(description = "领取有效期开始时间")
    private Date validStartTime;

    @Schema(description = "领取有效期结束时间")
    private Date validEndTime;

    @Schema(description = "每人限领数量")
    private Integer perLimit;

    @Schema(description = "赠送备注")
    private String remark;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "撤回时间")
    private Date cancelTime;

}