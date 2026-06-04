package org.jeecg.modules.app.bean.vo.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.app.bean.vo.fields.ElementAttributeVO;
import org.jeecg.modules.app.bean.vo.fields.ElementTagVO;
import org.jeecg.modules.app.bean.vo.fields.ExtendDataVO;
import org.jeecg.modules.app.bean.vo.fields.FieldElementVO;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 物品创建VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "物品创建请求对象")
public class ItemInfoCreateVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "初始ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String oriId;

    @Schema(description = "物品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String itemId;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "分类ID")
    private String category;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "物品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "等级", type = "integer", minimum = "1", maximum = "4")
    private Integer level;

    @Schema(description = "价格")
    private BigDecimal price;

    @Schema(description = "物品描述")
    private String description;

    @Schema(description = "标签组")
    private List<ElementTagVO> tags;

    @Schema(description = "属性列表")
    private List<ElementAttributeVO> attributes;

    @Schema(description = "组件列表")
    private List<FieldElementVO> fields;

    @Schema(description = "状态", type = "integer", minimum = "0", maximum = "9")
    private Integer status;

    @Schema(description = "扩展VO")
    private ExtendDataVO extendData;


}
