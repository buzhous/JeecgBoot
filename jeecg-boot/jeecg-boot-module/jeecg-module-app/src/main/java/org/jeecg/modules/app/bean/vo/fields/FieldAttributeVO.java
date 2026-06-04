package org.jeecg.modules.app.bean.vo.fields;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FieldAttributeVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "属性类型")
    private String type;

    @Schema(description = "颜色")
    private String color;

}