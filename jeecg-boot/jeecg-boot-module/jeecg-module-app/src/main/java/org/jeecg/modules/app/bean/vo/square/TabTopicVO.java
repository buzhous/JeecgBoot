package org.jeecg.modules.app.bean.vo.square;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 标签页主题VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "标签页主题响应对象")
public class TabTopicVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主题ID", example = "TABTOPIC001")
    private String id;

    @Schema(description = "主题名称", example = "分类浏览")
    private String tabName;

    @Schema(description = "主题图标", example = "tab-topic-icon.png")
    private String tabIcon;

    @Schema(description = "主题编码", example = "CATEGORY")
    private String tabCode;

    @Schema(description = "标签列表")
    private List<TabTagVO> tabItems;
}
