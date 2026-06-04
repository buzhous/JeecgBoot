package org.jeecg.modules.app.bean.vo.queue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "队列同步列表对象")
public class ItemSyncListVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "初始ID")
    private String oriId;

    @Schema(description = "物品ID")
    private String itemId;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "物品名称")
    private String name;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "等级")
    private Integer level;

    @Schema(description = "价格")
    private Double price;

    @Schema(description = "物品描述")
    private String description;

    //@Schema(description = "标签组")
    //private List<ElementTagVO> tags;

    //@Schema(description = "属性列表")
    //private List<ElementAttributeVO> attributes;

    //@Schema(description = "组件列表")
    //private List<FieldElementVO> fields;

    @Schema(description = "状态")
    private Integer status;

    //@Schema(description = "扩展VO")
    //private ExtendDataVO extendData;

    @Schema(description = "队列ID")
    private String queueId;

    @Schema(description = "版本", requiredMode = Schema.RequiredMode.REQUIRED)
    private String version;

    @Schema(description = "同步状态")
    private Integer syncStatus;

}
