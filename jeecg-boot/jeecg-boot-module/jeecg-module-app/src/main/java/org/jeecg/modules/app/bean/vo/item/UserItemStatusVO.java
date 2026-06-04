package org.jeecg.modules.app.bean.vo.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * 用户物品状态VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "用户物品状态响应对象")
public class UserItemStatusVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Excel(name = "灵石金额", width = 15)
    @Schema(description = "灵石金额", example = "100")
    private Integer fairyStoneAmount;

    @Excel(name = "金币金额", width = 15)
    @Schema(description = "金币金额", example = "500")
    private Integer goldCoinAmount;

    // 0否，1是
    @Schema(description = "更新线上物品", example = "1", allowableValues = {"0", "1"})
    private Integer isSyncOnlineItem;
}
