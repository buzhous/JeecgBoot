package org.jeecg.modules.app.bean.vo.coupon;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(name = "优惠券创建VO", description = "优惠券创建请求参数")
public class CouponCreateVO {

    @Schema(description = "优惠券名称", required = true)
    private String couponName;

    @Schema(description = "优惠券类型: 1-满减券, 2-折扣券, 3-兑换券", required = true)
    private Integer couponType;

    @Schema(description = "折扣金额或折扣率", required = true)
    private BigDecimal discountValue;

    @Schema(description = "最低消费金额")
    private BigDecimal minAmount;

    @Schema(description = "优惠券描述")
    private String description;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "有效期开始时间", required = true)
    private Date validStartTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "有效期结束时间", required = true)
    private Date validEndTime;

    @Schema(description = "发放总数量", required = true)
    private Integer totalCount;

    @Schema(description = "每人限领数量", required = true)
    private Integer perLimit;

    @Schema(description = "每日限领数量")
    private Integer dailyLimit;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "备注")
    private String remark;

}