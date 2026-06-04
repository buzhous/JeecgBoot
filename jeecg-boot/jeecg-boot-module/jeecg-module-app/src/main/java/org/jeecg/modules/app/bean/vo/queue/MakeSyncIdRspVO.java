package org.jeecg.modules.app.bean.vo.queue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "生成物品同步ID响应")
public class MakeSyncIdRspVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "原始Id")
    private String oriId;

    @Schema(description = "物品Id")
    private String itemId;

    @Schema(description = "持有Id")
    private String inventoryId;

    @Schema(description = "队列Id")
    private String queueId;

    @Schema(description = "版本")
    private String version;

    @Schema(description = "同步状态")
    private Integer syncStatus;

}
