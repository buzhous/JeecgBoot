package org.jeecg.modules.app.bean.vo.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Schema(name = "优惠券更新VO", description = "优惠券更新请求参数")
public class CouponUpdateVO {

    @Schema(description = "优惠券ID", required = true)
    private Long id;

    @Schema(description = "优惠券名称")
    private String couponName;

    @Schema(description = "优惠券类型: 1-满减券, 2-折扣券, 3-兑换券")
    private Integer couponType;

    @Schema(description = "折扣金额或折扣率")
    private BigDecimal discountValue;

    @Schema(description = "最低消费金额")
    private BigDecimal minAmount;

    @Schema(description = "优惠券描述")
    private String description;

    @Schema(description = "有效期开始时间")
    private Date validStartTime;

    @Schema(description = "有效期结束时间")
    private Date validEndTime;

    @Schema(description = "发放总数量")
    private Integer totalCount;

    @Schema(description = "每人限领数量")
    private Integer perLimit;

    @Schema(description = "每日限领数量")
    private Integer dailyLimit;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "备注")
    private String remark;

}