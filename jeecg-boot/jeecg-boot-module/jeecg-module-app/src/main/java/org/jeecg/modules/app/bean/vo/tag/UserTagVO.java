package org.jeecg.modules.app.bean.vo.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;


@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "用户标签")
public class UserTagVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "标签ID")
    private String id;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "标签名称")
    private String name;

    @Schema(description = "ICON")
    private String icon;

    @Schema(description = "颜色")
    private String color;

    @Schema(description = "排序")
    private Integer sort;

}
