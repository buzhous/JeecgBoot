package org.jeecg.modules.app.bean.vo.init;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录初始化VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "用户登录初始化响应对象")
public class InitVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "灵石数量", example = "100")
    private Integer fairyStoneAmount;

    @Schema(description = "金币数量", example = "500")
    private Integer goldCoinAmount;

    // 0否，1是
    @Schema(description = "是否具备首次登录赠送资格", example = "1")
    private Integer isFirstGift;

    // 0否，1是
    @Schema(description = "是否更新侧栏标签", example = "0")
    private Integer isRefreshTag;

}