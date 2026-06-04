package org.jeecg.modules.app.bean.vo.fields;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 物品组件对象 VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "物品组件对象")
public class FieldElementVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "组件类型")
    private Integer element;

    @Schema(description = "组件名称")
    private String name;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "是否显示")
    private Integer isShow;

    @Schema(description = "组件属性")
    private FieldAttributeVO attributes;

    @Schema(description = "内容")
    private ElementContentVO content;

}

