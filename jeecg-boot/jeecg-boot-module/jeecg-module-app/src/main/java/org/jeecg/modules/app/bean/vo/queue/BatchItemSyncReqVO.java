package org.jeecg.modules.app.bean.vo.queue;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 批量队列同步上传请求对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "批量队列同步上传请求对象")
public class BatchItemSyncReqVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "同步操作：新增-add,更新-update,销毁-destroy", requiredMode = Schema.RequiredMode.REQUIRED)
    private String syncOps;

    @Schema(description = "物品列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ItemSyncReqVO> items;

}