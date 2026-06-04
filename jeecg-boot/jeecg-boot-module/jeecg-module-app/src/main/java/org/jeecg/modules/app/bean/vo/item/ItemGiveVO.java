package org.jeecg.modules.app.bean.vo.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 物品赠送VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "物品赠送VO")
public class ItemGiveVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "记录ID", example = "GIVE001")
    private String id;

    @Schema(description = "赠送用户ID", example = "USER123")
    private String giveUserId;

    @Schema(description = "接受用户ID", example = "USER456")
    private String receiveUserId;

    @Schema(description = "物品ID", example = "ITEM789")
    private String itemId;

    @Schema(description = "物品库存归属ID", example = "INV001")
    private String inventoryId;

    @Schema(description = "物品数量", example = "1")
    private Integer quantity;

}