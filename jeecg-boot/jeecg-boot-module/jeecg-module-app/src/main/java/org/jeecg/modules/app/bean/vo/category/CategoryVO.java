package org.jeecg.modules.app.bean.vo.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;


@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(name = "类别VO", description = "分类管理")
public class CategoryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private String id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "类型")
    private Integer type;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "系统分类")
    private Integer isSys;

    @Schema(description = "详情")
    private String detail;

}