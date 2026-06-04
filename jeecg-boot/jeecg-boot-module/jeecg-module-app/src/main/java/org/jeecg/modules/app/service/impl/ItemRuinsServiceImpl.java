package org.jeecg.modules.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.boot.starter.lock.annotation.JLock;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.enums.RuinsStatusEnum;
import org.jeecg.modules.app.bean.exception.AppException;
import org.jeecg.modules.app.bean.vo.queue.ItemSyncReqVO;
import org.jeecg.modules.app.bean.vo.ruins.ItemRuinsListVO;
import org.jeecg.modules.app.constant.LockConstant;
import org.jeecg.modules.app.entity.ItemRuins;
import org.jeecg.modules.app.entity.ItemSync;
import org.jeecg.modules.app.mapper.ItemRuinsMapper;
import org.jeecg.modules.app.service.IItemRuinsService;
import org.jeecg.modules.app.service.IItemSyncService;
import org.jeecg.modules.app.service.IUserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ItemRuinsServiceImpl extends ServiceImpl<ItemRuinsMapper, ItemRuins> implements IItemRuinsService {

    @Autowired
    IItemSyncService itemSyncService;

    @Autowired
    IUserCouponService userCouponService;

    @Override
    public IPage<ItemRuinsListVO> queryNotPickupRuinsPage(Integer page, Integer size) {
        // 查询分页列表
        QueryWrapper<ItemRuins> queryWrapper = new QueryWrapper<>();
        // 获取未拾取
        queryWrapper.lambda().eq(ItemRuins::getStatus, RuinsStatusEnum.NOT_PICKUP.getCode());
        Page<ItemRuins> page1 = new Page<>(page, size);
        IPage<ItemRuins> ruinsPage = this.baseMapper.selectPage(page1, queryWrapper);
        // 转换分页数据到IPage<ItemRuinsListVO>
        IPage<ItemRuinsListVO> iPageVO = new Page<>(ruinsPage.getCurrent(), ruinsPage.getSize(), ruinsPage.getTotal());
        iPageVO.setRecords(BeanUtil.copyToList(ruinsPage.getRecords(), ItemRuinsListVO.class));
        return iPageVO;
    }

    @Override
    public ItemRuins queryRuinByItemId(String itemId, String oriId) {
        QueryWrapper<ItemRuins> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ItemRuins::getItemId, itemId);
        queryWrapper.lambda().eq(ItemRuins::getOriId, oriId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public ItemRuins queryRuinByRuinsId(String ruinsId, String itemId) {
        QueryWrapper<ItemRuins> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ItemRuins::getItemId, itemId);
        queryWrapper.lambda().eq(ItemRuins::getId, ruinsId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean dropToRuins(ItemSync itemSync) {
        ItemRuins itemRuins = BeanUtil.copyProperties(itemSync, ItemRuins.class);
        ItemRuins ruinExist = queryRuinByItemId(itemRuins.getItemId(), itemRuins.getOriId());
        if (ruinExist == null) {
            itemRuins.setStatus(RuinsStatusEnum.NOT_PICKUP.getCode());
            return this.saveOrUpdate(itemRuins);
        } else {
            itemRuins.setStatus(RuinsStatusEnum.NOT_PICKUP.getCode());
            return this.updateById(itemRuins);
        }
    }

    @Override
    @JLock(lockKey = LockConstant.ITEM_PICKUP_LOCK + "#runsId")
    public boolean pickup(String runsId, String itemId, String userId, Long userCouponId) {
        ItemRuins ruins = queryRuinByRuinsId(runsId, itemId);
        if (ruins == null) {
            throw new AppException(ExceptionEnum.ITEM_NOT_EXIST);
        }
        
        // 先扣除用户优惠券
        boolean couponUsed = userCouponService.useCoupon(userId, userCouponId, runsId);
        if (!couponUsed) {
            throw new AppException(ExceptionEnum.ITEM_DESTROYED);
        }
        
        // 更新销毁物品用户Id和状态
        ItemRuins ruinsUpdate = new ItemRuins();
        ruinsUpdate.setId(ruins.getId());
        ruinsUpdate.setUserId(userId);
        ruinsUpdate.setStatus(RuinsStatusEnum.PICKUP.getCode());
        this.updateById(ruinsUpdate);
        // 插入用户物品同步表
        ItemSyncReqVO syncVO = BeanUtil.copyProperties(ruins, ItemSyncReqVO.class);
        syncVO.setUserId(userId);
        syncVO.setItemId(itemId);
        syncVO.setOriId(runsId);
        return itemSyncService.syncFromRuins(syncVO);
    }

}