package org.jeecg.modules.app.bean.vo.queue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量队列同步上传响应对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "批量队列同步上传响应对象")
public class BatchItemSyncRspVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "成功数量")
    private Integer successCount = 0;

    @Schema(description = "失败数量")
    private Integer failCount = 0;

    @Schema(description = "成功列表")
    private List<ItemSyncRspVO> successList = new ArrayList<>();

    @Schema(description = "失败列表")
    private List<FailItemSyncRspVO> failList = new ArrayList<>();

    @Data
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = false)
    @Schema(description = "失败物品同步响应对象")
    public static class FailItemSyncRspVO implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @Schema(description = "初始ID")
        private String oriId;

        @Schema(description = "物品ID")
        private String itemId;

        @Schema(description = "错误信息")
        private String errorMsg;
    }

}