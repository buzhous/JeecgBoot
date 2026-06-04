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
@Schema(description = "物品同步下载请求")
public class ItemSyncDownloadReqVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "原始Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String oriId;

    @Schema(description = "物品Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String itemId;

    @Schema(description = "队列Id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String queueId;

    @Schema(description = "版本", requiredMode = Schema.RequiredMode.REQUIRED)
    private String version;

    @Schema(description = "同步状态（内部）")
    private Integer syncStatus;

    @Schema(description = "用户ID（内部）")
    private String userId;

}
