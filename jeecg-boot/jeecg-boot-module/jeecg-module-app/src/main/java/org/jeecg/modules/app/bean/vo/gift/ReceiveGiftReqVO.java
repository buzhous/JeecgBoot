package org.jeecg.modules.app.bean.vo.gift;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 领取赠送请求VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "领取赠送请求VO")
public class ReceiveGiftReqVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "赠送记录ID（指定赠送时使用）")
    private String giftId;

    @Schema(description = "兑换码（公开赠送时使用）")
    private String giftCode;

    @Schema(description = "领取数量（默认1）")
    private Integer quantity;

}