package org.jeecg.modules.app.bean.vo.inventory;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.app.bean.vo.category.UserCategoryVO;
import org.jeecg.modules.app.bean.vo.fields.ElementAttributeVO;
import org.jeecg.modules.app.bean.vo.fields.ElementTagVO;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 物品库存VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "物品库存")
public class ItemInventoryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "物品持有ID", example = "INV001")
    private String id;

    @Schema(description = "物品持有用户ID")
    private String userId;

    @Schema(description = "原始Id")
    private String oriId;

    @Schema(description = "物品ID", example = "ITEM789")
    private String itemId;

    @Schema(description = "库存数量", example = "5")
    private Integer quantity;

    @Schema(description = "图标", example = "phone-icon.png")
    private String icon;

    @Schema(description = "物品名称", example = "智能手机")
    private String name;

    @Schema(description = "等级", example = "5")
    private Integer level;

    @Schema(description = "物品描述")
    private String description;

    @Schema(description = "分类信息")
    private UserCategoryVO category;

    @Schema(description = "标签组")
    private List<ElementTagVO> tags;

    @Schema(description = "属性列表")
    private List<ElementAttributeVO> attributes;

    @Schema(description = "状态: 0默认，1使用中，2转赠中", example = "0", allowableValues = {"0", "1", "2"})
    private Integer status;


}
