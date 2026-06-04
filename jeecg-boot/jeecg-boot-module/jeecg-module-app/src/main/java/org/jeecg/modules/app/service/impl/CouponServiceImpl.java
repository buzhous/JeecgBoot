package org.jeecg.modules.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.exception.AppException;
import org.jeecg.modules.app.bean.vo.coupon.CouponCreateVO;
import org.jeecg.modules.app.bean.vo.coupon.CouponUpdateVO;
import org.jeecg.modules.app.entity.Coupon;
import org.jeecg.modules.app.mapper.CouponMapper;
import org.jeecg.modules.app.service.ICouponService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {

    @Override
    public Page<Coupon> queryCouponPage(Page<Coupon> page, Coupon coupon) {
        LambdaQueryWrapper<Coupon> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(coupon.getStatus())) {
            queryWrapper.eq(Coupon::getStatus, coupon.getStatus());
        }
        if (ObjectUtil.isNotEmpty(coupon.getCouponName())) {
            queryWrapper.like(Coupon::getCouponName, coupon.getCouponName());
        }
        queryWrapper.orderByDesc(Coupon::getCreateTime);
        return this.page(page, queryWrapper);
    }

    @Override
    public List<Coupon> queryAvailableCoupons() {
        LambdaQueryWrapper<Coupon> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Coupon::getStatus, 1);
        queryWrapper.gt(Coupon::getRemainCount, 0);
        queryWrapper.le(Coupon::getValidStartTime, new Date());
        queryWrapper.ge(Coupon::getValidEndTime, new Date());
        queryWrapper.orderByDesc(Coupon::getSort);
        return this.list(queryWrapper);
    }

    @Override
    public boolean publishCoupon(Long couponId) {
        Coupon coupon = this.getById(couponId);
        if (ObjectUtil.isEmpty(coupon)) {
            throw new AppException(ExceptionEnum.COUPON_NOT_EXIST);
        }
        if (coupon.getStatus() == 1) {
            throw new AppException(ExceptionEnum.COUPON_ALREADY_PUBLISHED);
        }
        coupon.setStatus(1);
        coupon.setUpdateTime(DateUtil.date());
        return this.updateById(coupon);
    }

    @Override
    public boolean offlineCoupon(Long couponId) {
        Coupon coupon = this.getById(couponId);
        if (ObjectUtil.isEmpty(coupon)) {
            throw new AppException(ExceptionEnum.COUPON_NOT_EXIST);
        }
        if (coupon.getStatus() == 2) {
            throw new AppException(ExceptionEnum.COUPON_ALREADY_OFFLINE);
        }
        coupon.setStatus(2);
        coupon.setUpdateTime(DateUtil.date());
        return this.updateById(coupon);
    }

    @Override
    public boolean updateCoupon(CouponUpdateVO updateVO) {
        if (ObjectUtil.isEmpty(updateVO.getId())) {
            throw new AppException(ExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Coupon existCoupon = this.getById(updateVO.getId());
        if (ObjectUtil.isEmpty(existCoupon)) {
            throw new AppException(ExceptionEnum.COUPON_NOT_EXIST);
        }
        BeanUtil.copyProperties(updateVO, existCoupon, "id", "createTime", "updateTime", "remainCount", "usedCount", "status");
        existCoupon.setUpdateTime(DateUtil.date());
        return this.updateById(existCoupon);
    }

    @Override
    public boolean deleteCoupon(Long couponId) {
        Coupon coupon = this.getById(couponId);
        if (ObjectUtil.isEmpty(coupon)) {
            throw new AppException(ExceptionEnum.COUPON_NOT_EXIST);
        }
        return this.removeById(couponId);
    }

    @Override
    public Coupon createCoupon(CouponCreateVO createVO) {
        if (ObjectUtil.isEmpty(createVO.getCouponName())) {
            throw new AppException(ExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (ObjectUtil.isEmpty(createVO.getCouponType())) {
            throw new AppException(ExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (ObjectUtil.isEmpty(createVO.getDiscountValue())) {
            throw new AppException(ExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (ObjectUtil.isEmpty(createVO.getValidStartTime())) {
            throw new AppException(ExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (ObjectUtil.isEmpty(createVO.getValidEndTime())) {
            throw new AppException(ExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (ObjectUtil.isEmpty(createVO.getTotalCount())) {
            throw new AppException(ExceptionEnum.REQUEST_PARAM_ERROR);
        }
        if (ObjectUtil.isEmpty(createVO.getPerLimit())) {
            throw new AppException(ExceptionEnum.REQUEST_PARAM_ERROR);
        }
        Coupon coupon = BeanUtil.copyProperties(createVO, Coupon.class);
        coupon.setCreateTime(DateUtil.date());
        coupon.setUpdateTime(DateUtil.date());
        coupon.setRemainCount(createVO.getTotalCount());
        coupon.setUsedCount(0);
        coupon.setStatus(0);
        boolean saved = this.save(coupon);
        if (!saved) {
            throw new AppException(ExceptionEnum.DATA_UPDATE_ERROR);
        }
        return coupon;
    }

}