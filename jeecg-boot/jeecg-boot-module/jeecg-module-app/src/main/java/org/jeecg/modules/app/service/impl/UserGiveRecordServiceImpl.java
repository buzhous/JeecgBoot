package org.jeecg.modules.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.bean.enums.GiveStatusEnum;
import org.jeecg.modules.app.bean.vo.item.ItemGiveVO;
import org.jeecg.modules.app.bean.vo.item.ItemInfoCreateVO;
import org.jeecg.modules.app.bean.vo.inventory.ItemInventoryVO;
import org.jeecg.modules.app.entity.ItemInfo;
import org.jeecg.modules.app.entity.UserGiveRecord;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.mapper.UserGiveRecordMapper;
import org.jeecg.modules.app.service.IItemInfoService;
import org.jeecg.modules.app.service.IItemUserInventoryService;
import org.jeecg.modules.app.service.IUserGiveRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserGiveRecordServiceImpl extends ServiceImpl<UserGiveRecordMapper, UserGiveRecord> implements IUserGiveRecordService {

    @Autowired
    private IItemUserInventoryService iItemUserInventoryService;

    @Autowired
    private IItemInfoService iItemInfoService;

    // 创建物品赠送记录
    @Override
    public boolean createGiveUserItemRecord(UserGiveRecord userGiveRecord) {
        boolean result = this.save(userGiveRecord);
        return result;
    }

    // 查询用户物品赠送记录
    @Override
    public UserGiveRecord queryGiveUserItemRecord(String userId, String recordId) {
        QueryWrapper<UserGiveRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("give_uid", userId);
        queryWrapper.eq("id", recordId);
        return this.baseMapper.selectOne(queryWrapper);
    }

    // 查询用户物品接收记录
    @Override
    public UserGiveRecord queryReceiveUserItemRecord(String userId, String recordId) {
        QueryWrapper<UserGiveRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receive_uid", userId);
        queryWrapper.eq("id", recordId);
        return this.baseMapper.selectOne(queryWrapper);
    }

    // 更新物品赠送记录状态
    @Override
    public boolean updateGiveRecordStatus(UserGiveRecord userGiveRecord) {
        // 更新物品状态
        return this.updateById(userGiveRecord);
    }

    // 用户物品入库
    @Override
    @Transactional
    public ItemInventoryVO createUserInventoryItem(AppUser loginUser, ItemInventoryVO inventoryVO) {
        // 用户Id
        String userId = loginUser.getId();
        // 物品信息入库
        ItemInfoCreateVO createVO = BeanUtil.copyProperties(inventoryVO, ItemInfoCreateVO.class);
        iItemInfoService.createItemInfo(createVO);
        String itemId = createVO.getItemId();

        // 物品入库用户空间
        ItemInventoryVO itemInventoryVO = new ItemInventoryVO();
        itemInventoryVO.setQuantity(inventoryVO.getQuantity() == null ? 1 : inventoryVO.getQuantity());
        itemInventoryVO.setItemId(itemId);
        iItemUserInventoryService.createItemUserInventory(itemInventoryVO);

        return itemInventoryVO;
    }

    // 赠送指定用户物品
    @Override
    @Transactional
    public boolean giveUser(AppUser loginUser, ItemGiveVO itemGiveVO) {
        // 用户Id
        String userId = loginUser.getId();
        String giveUserId = itemGiveVO.getGiveUserId();
        String itemId = itemGiveVO.getItemId();
        String itemInventoryId = itemGiveVO.getInventoryId();
        // 用户物品赠送，写入待领取表
        UserGiveRecord userGiveRecord = new UserGiveRecord();
        userGiveRecord.setGiveUid(userId);
        userGiveRecord.setReceiveUid(giveUserId);
        userGiveRecord.setItemId(itemId);
        userGiveRecord.setGiveTime(DateUtil.date());
        userGiveRecord.setStatus(GiveStatusEnum.UNCLAIMED.getCode());
        userGiveRecord.setGetExpiredTime(DateUtil.offset(DateUtil.date(), DateField.DAY_OF_YEAR, 14));
        userGiveRecord.setQuantity(itemGiveVO.getQuantity() == null ? 1 : itemGiveVO.getQuantity());
        boolean result = this.createGiveUserItemRecord(userGiveRecord);
        if (!result) {
            // 删除用户与物品关系
            boolean delResult = iItemUserInventoryService.removeUserItem(userId, itemInventoryId);
            if (!delResult) {
                // 回滚
                throw new RuntimeException("error");
            }
        }
        return false;
    }

    // 用户领取被赠送物品
    @Override
    @Transactional
    public boolean receiveGiveItem(AppUser loginUser, ItemGiveVO itemGiveVO) {
        String userId = loginUser.getId();
        UserGiveRecord giveRecord = this.queryReceiveUserItemRecord(userId, itemGiveVO.getId());
        if (giveRecord == null) {
            return false;
        }
        ItemInfo itemInfo = iItemInfoService.queryItemInfo(giveRecord.getItemId());
        if (itemInfo == null) {
            return false;
        }
        // 领取物品
        UserGiveRecord giveRecordReceive = new UserGiveRecord();
        giveRecordReceive.setId(giveRecord.getId());
        giveRecordReceive.setStatus(GiveStatusEnum.RECEIVE.getCode());
        this.updateGiveRecordStatus(giveRecordReceive);

        // 物品入库用户空间
        ItemInventoryVO itemInventoryVO = new ItemInventoryVO();
        itemInventoryVO.setQuantity(giveRecord.getQuantity() == null ? 1 : giveRecord.getQuantity());
        itemInventoryVO.setItemId(giveRecord.getItemId());
        boolean result = iItemUserInventoryService.createItemUserInventory(itemInventoryVO);
        if (!result) {
            // 回滚
            throw new RuntimeException("error");
        }
        return false;
    }

    // 放弃领取，平台暂存7天，未领回，则作废处理
    @Override
    @Transactional
    public boolean giveUpGiveItem(AppUser loginUser, ItemGiveVO itemGiveVO) {
        String userId = loginUser.getId();
        UserGiveRecord giveRecord = this.queryReceiveUserItemRecord(userId, itemGiveVO.getId());
        if (giveRecord == null) {
            return false;
        }
        // 物品领取过期，更新状态
        boolean expired = this.checkAndUpdateGiveItemExpired(loginUser, itemGiveVO);
        if (expired) {
            return false;
        }
        UserGiveRecord giveRecordReceive = new UserGiveRecord();
        giveRecordReceive.setId(giveRecord.getId());
        giveRecordReceive.setStatus(GiveStatusEnum.GIVE_UP.getCode());
        giveRecordReceive.setGetExpiredTime(DateUtil.offset(DateUtil.date(), DateField.DAY_OF_YEAR, 14));
        boolean result = this.updateGiveRecordStatus(giveRecordReceive);
        if (!result) {
            // 回滚
            throw new RuntimeException("error");
        }
        return false;
    }

    // 用户赠送物品取回
    @Override
    @Transactional
    public boolean retrieveGiveItem(AppUser loginUser, ItemGiveVO itemGiveVO) {
        String userId = loginUser.getId();
        UserGiveRecord giveRecord = this.queryGiveUserItemRecord(userId, itemGiveVO.getId());
        if (giveRecord == null) {
            return false;
        }
        // 物品领取过期，更新状态
        boolean expired = this.checkAndUpdateRetrieveItemExpired(loginUser, itemGiveVO);
        if (expired) {
            return false;
        }
        UserGiveRecord giveRecordReceive = new UserGiveRecord();
        giveRecordReceive.setId(giveRecord.getId());
        giveRecordReceive.setGetExpiredStatus(GiveStatusEnum.GIVE_UP.getCode());
        giveRecordReceive.setGetExpiredTime(DateUtil.offset(DateUtil.date(), DateField.DAY_OF_YEAR, 14));
        boolean result = this.updateGiveRecordStatus(giveRecordReceive);
        if (!result) {
            // 回滚
            throw new RuntimeException("error");
        }
        return false;
    }

    // 更新物品取回过期状态
    public boolean checkAndUpdateRetrieveItemExpired(AppUser loginUser, ItemGiveVO itemGiveVO) {
        String userId = loginUser.getId();
        UserGiveRecord giveRecord = this.queryGiveUserItemRecord(userId, itemGiveVO.getId());
        if (giveRecord == null) {
            return false;
        }
        // 物品过期，更新状态
        if (DateUtil.compare(giveRecord.getGetExpiredTime(), DateUtil.date()) < 0) {
            // 物品过期，更新状态
            UserGiveRecord giveRecordReceive = new UserGiveRecord();
            giveRecordReceive.setId(giveRecord.getId());
            // 物品过期，更新状态
            giveRecordReceive.setGetExpiredStatus(1);
            boolean result = this.updateGiveRecordStatus(giveRecordReceive);
            if (!result) {
                // 回滚
                throw new RuntimeException("error");
            }
            return true;
        }
        return false;
    }

    // 更新物品领取过期状态
    public boolean checkAndUpdateGiveItemExpired(AppUser loginUser, ItemGiveVO itemGiveVO) {
        // 用户Id
        String userId = loginUser.getId();
        // 确权
        UserGiveRecord giveRecord = this.queryReceiveUserItemRecord(userId, itemGiveVO.getId());
        if (giveRecord == null) {
            return false;
        }
        // 物品过期，更新状态
        if (DateUtil.compare(giveRecord.getGetExpiredTime(), DateUtil.date()) < 0) {
            // 物品过期，更新状态
            UserGiveRecord giveRecordReceive = new UserGiveRecord();
            giveRecordReceive.setId(giveRecord.getId());
            // 物品过期，更新状态
            giveRecordReceive.setStatus(GiveStatusEnum.EXPIRED.getCode());
            boolean result = this.updateGiveRecordStatus(giveRecordReceive);
            if (!result) {
                // 回滚
                throw new RuntimeException("error");
            }
            return true;
        }
        return false;
    }


}
