package org.jeecg.modules.app.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.boot.starter.lock.annotation.JLock;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.exception.AppException;
import org.jeecg.modules.app.constant.LockConstant;
import org.jeecg.modules.app.entity.Coupon;
import org.jeecg.modules.app.entity.UserCoupon;
import org.jeecg.modules.app.mapper.UserCouponMapper;
import org.jeecg.modules.app.service.ICouponService;
import org.jeecg.modules.app.service.IUserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserCouponServiceImpl extends ServiceImpl<UserCouponMapper, UserCoupon> implements IUserCouponService {

    @Autowired
    private ICouponService couponService;

    @Override
    public Page<UserCoupon> queryUserCouponPage(Page<UserCoupon> page, String userId, Integer status) {
        LambdaQueryWrapper<UserCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCoupon::getUserId, userId);
        if (ObjectUtil.isNotEmpty(status)) {
            queryWrapper.eq(UserCoupon::getStatus, status);
        }
        queryWrapper.orderByDesc(UserCoupon::getCreateTime);
        return this.page(page, queryWrapper);
    }

    @Override
    public List<UserCoupon> queryAvailableCouponsByUserId(String userId) {
        LambdaQueryWrapper<UserCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCoupon::getUserId, userId);
        queryWrapper.eq(UserCoupon::getStatus, 0);
        queryWrapper.le(UserCoupon::getValidStartTime, new Date());
        queryWrapper.ge(UserCoupon::getValidEndTime, new Date());
        queryWrapper.orderByDesc(UserCoupon::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @JLock(lockKey = LockConstant.COUPON_RECEIVE_LOCK + "#couponId")
    public boolean receiveCoupon(String userId, Long couponId) {
        Coupon coupon = couponService.getById(couponId);
        if (ObjectUtil.isEmpty(coupon)) {
            throw new AppException(ExceptionEnum.COUPON_NOT_EXIST);
        }
        if (coupon.getStatus() != 1) {
            throw new AppException(ExceptionEnum.COUPON_NOT_AVAILABLE);
        }
        if (coupon.getRemainCount() <= 0) {
            throw new AppException(ExceptionEnum.COUPON_STOCK_EMPTY);
        }
        Date now = new Date();
        if (now.before(coupon.getValidStartTime()) || now.after(coupon.getValidEndTime())) {
            throw new AppException(ExceptionEnum.COUPON_NOT_IN_VALID_TIME);
        }
        int receivedCount = this.countUserReceivedCoupons(userId, couponId);
        if (coupon.getPerLimit() > 0 && receivedCount >= coupon.getPerLimit()) {
            throw new AppException(ExceptionEnum.COUPON_RECEIVE_LIMIT_EXCEEDED);
        }
        if (coupon.getDailyLimit() != null && coupon.getDailyLimit() > 0) {
            int dailyReceivedCount = this.countUserDailyReceivedCoupons(userId, couponId);
            if (dailyReceivedCount >= coupon.getDailyLimit()) {
                throw new AppException(ExceptionEnum.COUPON_DAILY_LIMIT_EXCEEDED);
            }
        }
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setCouponName(coupon.getCouponName());
        userCoupon.setCouponType(coupon.getCouponType());
        userCoupon.setDiscountValue(coupon.getDiscountValue());
        userCoupon.setMinAmount(coupon.getMinAmount());
        userCoupon.setValidStartTime(coupon.getValidStartTime());
        userCoupon.setValidEndTime(coupon.getValidEndTime());
        userCoupon.setObtainType(1);
        userCoupon.setStatus(0);
        userCoupon.setCreateTime(DateUtil.date());
        userCoupon.setUpdateTime(DateUtil.date());
        boolean saved = this.save(userCoupon);
        if (saved) {
            coupon.setRemainCount(coupon.getRemainCount() - 1);
            coupon.setUpdateTime(DateUtil.date());
            couponService.updateById(coupon);
        }
        return saved;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean useCoupon(String userId, Long userCouponId, String orderId) {
        UserCoupon userCoupon = this.getById(userCouponId);
        if (ObjectUtil.isEmpty(userCoupon)) {
            throw new AppException(ExceptionEnum.USER_COUPON_NOT_EXIST);
        }
        if (!userCoupon.getUserId().equals(userId)) {
            throw new AppException(ExceptionEnum.COUPON_NOT_BELONG_TO_USER);
        }
        if (userCoupon.getStatus() != 0) {
            throw new AppException(ExceptionEnum.COUPON_ALREADY_USED);
        }
        Date now = new Date();
        if (now.after(userCoupon.getValidEndTime())) {
            throw new AppException(ExceptionEnum.COUPON_EXPIRED);
        }
        userCoupon.setStatus(1);
        userCoupon.setUseTime(now);
        userCoupon.setOrderId(orderId);
        userCoupon.setUpdateTime(now);
        return this.updateById(userCoupon);
    }

    @Override
    public int countUserReceivedCoupons(String userId, Long couponId) {
        LambdaQueryWrapper<UserCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCoupon::getUserId, userId);
        queryWrapper.eq(UserCoupon::getCouponId, couponId);
        return (int) this.count(queryWrapper);
    }

    @Override
    public int countUserDailyReceivedCoupons(String userId, Long couponId) {
        LambdaQueryWrapper<UserCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCoupon::getUserId, userId);
        queryWrapper.eq(UserCoupon::getCouponId, couponId);
        Date today = DateUtil.beginOfDay(new Date());
        Date tomorrow = DateUtil.offsetDay(today, 1);
        queryWrapper.ge(UserCoupon::getCreateTime, today);
        queryWrapper.lt(UserCoupon::getCreateTime, tomorrow);
        return (int) this.count(queryWrapper);
    }

    @Override
    public int countAvailableCouponsByUserId(String userId) {
        LambdaQueryWrapper<UserCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCoupon::getUserId, userId);
        queryWrapper.eq(UserCoupon::getStatus, 0);
        queryWrapper.le(UserCoupon::getValidStartTime, new Date());
        queryWrapper.ge(UserCoupon::getValidEndTime, new Date());
        return (int) this.count(queryWrapper);
    }

    @Override
    public UserCoupon getByUserIdAndCouponId(String userId, Long couponId, String orderId) {
        LambdaQueryWrapper<UserCoupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCoupon::getUserId, userId);
        queryWrapper.eq(UserCoupon::getCouponId, couponId);
        queryWrapper.eq(UserCoupon::getStatus, 0);
        if (ObjectUtil.isNotEmpty(orderId)) {
            queryWrapper.eq(UserCoupon::getOrderId, orderId);
        }
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

}