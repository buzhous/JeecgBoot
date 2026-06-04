package org.jeecg.modules.app.controller.coupon;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.exception.AppException;
import org.jeecg.modules.app.entity.UserCoupon;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.service.IUserCouponService;
import org.jeecg.modules.app.utils.AppAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户优惠券")
@Slf4j
@RestController
@RequestMapping("/app/user/coupon")
public class AppUserCouponController {

    @Autowired
    private IUserCouponService userCouponService;

    //@Operation(summary = "优惠券列表")
    @GetMapping(value = "/list")
    public Result<Page<UserCoupon>> list(@RequestParam(required = false) Integer status,
                                          @RequestParam(defaultValue = "1") Integer pageNo,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        Page<UserCoupon> page = new Page<>(pageNo, pageSize);
        Page<UserCoupon> result = userCouponService.queryUserCouponPage(page, loginUser.getId(), status);
        return Result.OK(result);
    }

    @Operation(summary = "可用优惠券列表")
    @GetMapping(value = "/available")
    public Result<List<UserCoupon>> availableCoupons() {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        List<UserCoupon> coupons = userCouponService.queryAvailableCouponsByUserId(loginUser.getId());
        return Result.OK(coupons);
    }

    @Operation(summary = "可用优惠券数量")
    @GetMapping(value = "/available/count")
    public Result<Integer> availableCouponCount() {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        int count = userCouponService.countAvailableCouponsByUserId(loginUser.getId());
        return Result.OK(count);
    }

    @Operation(summary = "使用优惠券")
    @PostMapping(value = "/use")
    public Result<Boolean> useCoupon(@RequestParam Long userCouponId, @RequestParam String orderId) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        boolean result = userCouponService.useCoupon(loginUser.getId(), userCouponId, orderId);
        return Result.OK(result);
    }

    @Operation(summary = "优惠券详情")
    @GetMapping(value = "/detail")
    public Result<UserCoupon> detail(@RequestParam Long userCouponId) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        UserCoupon userCoupon = userCouponService.getById(userCouponId);
        if (ObjectUtil.isEmpty(userCoupon)) {
            throw new AppException(ExceptionEnum.USER_COUPON_NOT_EXIST);
        }
        if (!userCoupon.getUserId().equals(loginUser.getId())) {
            throw new AppException(ExceptionEnum.COUPON_NOT_BELONG_TO_USER);
        }
        return Result.OK(userCoupon);
    }

    @Operation(summary = "获取用户指定优惠券")
    @GetMapping(value = "/getByCouponId")
    public Result<UserCoupon> getByCouponId(@RequestParam Long couponId, @RequestParam(required = false) String orderId) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        UserCoupon userCoupon = userCouponService.getByUserIdAndCouponId(loginUser.getId(), couponId, orderId);
        return Result.OK(userCoupon);
    }

}