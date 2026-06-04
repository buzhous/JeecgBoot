package org.jeecg.modules.app.bean.vo.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "订单响应对象")
public class OrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "订单ID", example = "ORDER001")
    private String id;

    @Schema(description = "订单编号", example = "ORD202301150001")
    private String orderId;

    @Schema(description = "使用时间", example = "2023-01-15T14:30:00")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date useTime;

    @Schema(description = "物品图标", example = "phone-icon.png")
    private String itemIcon;

    @Schema(description = "物品名称", example = "智能手机")
    private String itemName;

    @Schema(description = "物品数量", example = "1")
    private Integer itemCount;

    @Schema(description = "状态：1使用成功；2使用失败", example = "1", allowableValues = {"1", "2"})
    private Integer status;

}