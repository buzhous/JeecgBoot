package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.entity.UserCoupon;

import java.util.List;

public interface IUserCouponService extends IService<UserCoupon> {

    Page<UserCoupon> queryUserCouponPage(Page<UserCoupon> page, String userId, Integer status);

    List<UserCoupon> queryAvailableCouponsByUserId(String userId);

    boolean receiveCoupon(String userId, Long couponId);

    boolean useCoupon(String userId, Long userCouponId, String orderId);

    int countUserReceivedCoupons(String userId, Long couponId);

    int countUserDailyReceivedCoupons(String userId, Long couponId);

    int countAvailableCouponsByUserId(String userId);

    UserCoupon getByUserIdAndCouponId(String userId, Long couponId, String orderId);

}