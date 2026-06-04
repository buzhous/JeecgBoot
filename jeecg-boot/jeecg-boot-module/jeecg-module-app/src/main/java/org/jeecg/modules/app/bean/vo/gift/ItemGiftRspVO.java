package org.jeecg.modules.app.bean.vo.gift;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 物品赠送响应VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "物品赠送响应VO")
public class ItemGiftRspVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "赠送记录ID")
    private String id;

    @Schema(description = "物品原始ID")
    private String oriId;

    @Schema(description = "物品ID")
    private String itemId;

    @Schema(description = "库存ID")
    private String inventoryId;

    @Schema(description = "兑换码（公开赠送时返回）")
    private String giftCode;

    @Schema(description = "二维码内容（Base64，公开赠送时返回）")
    private String qrCode;

    @Schema(description = "创建时间")
    private Date createTime;

}