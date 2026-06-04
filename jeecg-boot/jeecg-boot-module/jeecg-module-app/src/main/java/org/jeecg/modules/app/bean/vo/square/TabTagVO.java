package org.jeecg.modules.app.bean.vo.square;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 标签页标签VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "标签页标签响应对象")
public class TabTagVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "标签ID", example = "TABTAG001")
    private String id;

    @Schema(description = "标签名称", example = "热门")
    private String tabName;

    @Schema(description = "标签图标", example = "tab-hot-icon.png")
    private String tabIcon;

    @Schema(description = "标签编码", example = "HOT")
    private String tabCode;
}
