package org.jeecg.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;


@Data
@TableName("tbl_queue_id")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "队列服务ID")
public class QueueId implements Serializable {
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private Long id;

    @Excel(name = "队列Id", width = 15)
    @Schema(description = "队列Id")
    private String queueId;

    @Excel(name = "同步状态", width = 15)
    @Schema(description = "同步状态")
    private Integer status;
}
