package org.jeecg.modules.app.bean.vo.gift;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 物品赠送请求VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "物品赠送请求VO")
public class ItemGiftReqVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "赠送类型：1-指定用户赠送，2-公开赠送", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer giftType;

    @Schema(description = "接收者用户ID（指定赠送时必填）")
    private String receiverUserId;

    @Schema(description = "物品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String itemId;

    @Schema(description = "物品原始ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String oriId;

    @Schema(description = "赠送数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantity;

    @Schema(description = "领取有效期结束时间（公开赠送时使用）")
    private Date validEndTime;

    @Schema(description = "每人限领数量（公开赠送时使用）")
    private Integer perLimit;

    @Schema(description = "赠送备注")
    private String remark;

}