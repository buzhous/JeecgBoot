package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.bean.vo.coupon.CouponCreateVO;
import org.jeecg.modules.app.bean.vo.coupon.CouponUpdateVO;
import org.jeecg.modules.app.entity.Coupon;

import java.util.List;

public interface ICouponService extends IService<Coupon> {

    Page<Coupon> queryCouponPage(Page<Coupon> page, Coupon coupon);

    List<Coupon> queryAvailableCoupons();

    boolean publishCoupon(Long couponId);

    boolean offlineCoupon(Long couponId);

    boolean updateCoupon(CouponUpdateVO updateVO);

    boolean deleteCoupon(Long couponId);

    Coupon createCoupon(CouponCreateVO createVO);

}