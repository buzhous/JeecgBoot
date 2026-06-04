package org.jeecg.modules.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.enums.GiftStatusEnum;
import org.jeecg.modules.app.bean.enums.GiftTypeEnum;
import org.jeecg.modules.app.bean.enums.SyncStatusEnum;
import org.jeecg.modules.app.bean.exception.AppException;
import org.jeecg.modules.app.bean.vo.gift.ItemGiftListVO;
import org.jeecg.modules.app.bean.vo.gift.ItemGiftReqVO;
import org.jeecg.modules.app.bean.vo.gift.ItemGiftRspVO;
import org.jeecg.modules.app.bean.vo.gift.ReceiveGiftReqVO;
import org.jeecg.modules.app.entity.*;
import org.jeecg.modules.app.mapper.ItemGiftMapper;
import org.jeecg.modules.app.mapper.ItemGiftReceiveMapper;
import org.jeecg.modules.app.service.IItemGiftService;
import org.jeecg.modules.app.service.IItemInfoService;
import org.jeecg.modules.app.service.IItemSyncService;
import org.jeecg.modules.app.service.IItemUserInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ItemGiftServiceImpl extends ServiceImpl<ItemGiftMapper, ItemGift> implements IItemGiftService {

    @Lazy
    @Autowired
    private IItemUserInventoryService itemUserInventoryService;

    @Lazy
    @Autowired
    private IItemInfoService itemInfoService;

    @Lazy
    @Autowired
    private IItemSyncService itemSyncService;

    @Autowired
    private ItemGiftReceiveMapper itemGiftReceiveMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItemGiftRspVO createGift(ItemGiftReqVO reqVO, String userId) {
        ItemUserInventory inventory = itemUserInventoryService.queryUserItem(userId, reqVO.getItemId());
        if (ObjectUtil.isEmpty(inventory)) {
            throw new AppException(ExceptionEnum.ITEM_NOT_EXIST);
        }

        int inventoryQuantity = ObjectUtil.isEmpty(inventory.getQuantity()) ? 0 : inventory.getQuantity();
        if (reqVO.getQuantity() > inventoryQuantity) {
            throw new AppException(ExceptionEnum.INSUFFICIENT_QUANTITY);
        }

        ItemGift itemGift = new ItemGift();
        itemGift.setGiftType(reqVO.getGiftType());
        itemGift.setStatus(GiftStatusEnum.PENDING.getCode());
        itemGift.setSenderUserId(userId);
        itemGift.setReceiverUserId(reqVO.getReceiverUserId());
        itemGift.setItemId(reqVO.getItemId());
        itemGift.setOriId(reqVO.getOriId());
        itemGift.setInventoryId(inventory.getId());
        itemGift.setTotalQuantity(reqVO.getQuantity());
        itemGift.setReceivedQuantity(0);
        itemGift.setRemark(reqVO.getRemark());
        itemGift.setCreateTime(new Date());
        itemGift.setUpdateTime(new Date());
        itemGift.setValidStartTime(new Date());
        itemGift.setValidEndTime(DateUtil.offsetDay(new Date(), 999));
        if (GiftTypeEnum.PUBLIC.getCode() == reqVO.getGiftType()) {
            itemGift.setGiftCode(generateGiftCode());
            itemGift.setQrCode(generateQrCode(itemGift.getGiftCode()));
            itemGift.setValidStartTime(new Date());
            itemGift.setValidEndTime(reqVO.getValidEndTime());
            itemGift.setPerLimit(ObjectUtil.isEmpty(reqVO.getPerLimit()) ? 1 : reqVO.getPerLimit());
        }

        this.save(itemGift);

        inventory.setQuantity(inventoryQuantity - reqVO.getQuantity());
        itemUserInventoryService.updateItemById(inventory);

        ItemSync itemSync = itemSyncService.getItemSyncByItemId(reqVO.getItemId());
        if (ObjectUtil.isNotEmpty(itemSync)) {
            itemSync.setQuantity(inventory.getQuantity());
            if (inventory.getQuantity() <= 0) {
                itemSync.setStatus(SyncStatusEnum.DESTROYED.getCode());
            }
            itemSyncService.updateById(itemSync);
        }

        return BeanUtil.copyProperties(itemGift, ItemGiftRspVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean receiveGift(ReceiveGiftReqVO reqVO, String userId) {
        ItemGift itemGift = null;

        if (ObjectUtil.isNotEmpty(reqVO.getGiftCode())) {
            itemGift = this.getGiftByCode(reqVO.getGiftCode());
        } else if (ObjectUtil.isNotEmpty(reqVO.getGiftId())) {
            itemGift = this.getById(reqVO.getGiftId());
        }

        if (ObjectUtil.isEmpty(itemGift)) {
            throw new AppException(ExceptionEnum.GIFT_NOT_EXIST);
        }

        if (GiftStatusEnum.CANCELLED.getCode() == itemGift.getStatus()) {
            throw new AppException(ExceptionEnum.GIFT_CANCELLED);
        }

        if (GiftStatusEnum.EXPIRED.getCode() == itemGift.getStatus()) {
            throw new AppException(ExceptionEnum.GIFT_EXPIRED);
        }

        if (GiftStatusEnum.FULL_RECEIVED.getCode() == itemGift.getStatus()) {
            throw new AppException(ExceptionEnum.GIFT_RECEIVED);
        }

        Date now = new Date();
        if (ObjectUtil.isNotEmpty(itemGift.getValidEndTime()) && now.after(itemGift.getValidEndTime())) {
            itemGift.setStatus(GiftStatusEnum.EXPIRED.getCode());
            this.updateById(itemGift);
            throw new AppException(ExceptionEnum.GIFT_EXPIRED);
        }

        if (GiftTypeEnum.DIRECT.getCode() == itemGift.getGiftType()) {
            if (!userId.equals(itemGift.getReceiverUserId())) {
                throw new AppException(ExceptionEnum.GIFT_NOT_BELONG_TO_USER);
            }
            // 领取全部数量物品
            reqVO.setQuantity(itemGift.getTotalQuantity());
        } else {
            if (itemGift.getPerLimit() != null && itemGift.getPerLimit() > 0) {
                int receivedCount = countUserReceivedGift(itemGift.getId(), userId);
                if (receivedCount >= itemGift.getPerLimit()) {
                    throw new AppException(ExceptionEnum.GIFT_PER_LIMIT_EXCEEDED);
                }
            }
            if (itemGift.getPerLimit() == null || itemGift.getPerLimit() == 0) {
                throw new AppException(ExceptionEnum.GIFT_PER_LIMIT_EXCEEDED);
            }
            // 领取数量不得超过限制数量
            if (reqVO.getQuantity() > itemGift.getPerLimit()) {
                throw new AppException(ExceptionEnum.GIFT_PER_LIMIT_EXCEEDED);
            }
        }

        int receiveQuantity = ObjectUtil.isEmpty(reqVO.getQuantity()) ? 1 : reqVO.getQuantity();
        int remainQuantity = itemGift.getTotalQuantity() - itemGift.getReceivedQuantity();

        if (receiveQuantity > remainQuantity) {
            throw new AppException(ExceptionEnum.GIFT_INSUFFICIENT_QUANTITY);
        }

        ItemSync itemSync = itemSyncService.getItemSyncByItemId(itemGift.getItemId());
        if (ObjectUtil.isEmpty(itemSync)) {
            throw new AppException(ExceptionEnum.ITEM_NOT_EXIST);
        }

        ItemInfo itemInfo = itemInfoService.getById(itemGift.getItemId());
        if (ObjectUtil.isEmpty(itemInfo)) {
            throw new AppException(ExceptionEnum.ITEM_NOT_EXIST);
        }

        ItemUserInventory userInventory = itemUserInventoryService.queryUserItem(userId, itemGift.getItemId());
        ItemSync userItemSync = itemSyncService.getItemSyncByOriIdAndUserId(itemGift.getOriId(), userId);

        if (ObjectUtil.isEmpty(userInventory)) {
            userInventory = new ItemUserInventory();
            userInventory.setItemId(itemGift.getItemId());
            userInventory.setUserId(userId);
            userInventory.setQuantity(receiveQuantity);
            userInventory.setStatus(0);
            userInventory.setIsDrop(0);
            userInventory.setIsSync(0);
            userInventory.setCreateTime(new Date());
            userInventory.setGetTime(new Date());
            itemUserInventoryService.save(userInventory);

            if (ObjectUtil.isEmpty(userItemSync)) {
                ItemSync newItemSync = new ItemSync();
                BeanUtil.copyProperties(itemSync, newItemSync);
                newItemSync.setId(null);
                newItemSync.setUserId(userId);
                newItemSync.setQuantity(receiveQuantity);
                newItemSync.setSyncStatus(SyncStatusEnum.UNSYNCED.getCode());
                newItemSync.setVersion(1);
                newItemSync.setCreateTime(new Date());
                newItemSync.setUpdateTime(new Date());
                itemSyncService.save(newItemSync);
            } else {
                userItemSync.setQuantity(userItemSync.getQuantity() + receiveQuantity);
                userItemSync.setUpdateTime(new Date());
                itemSyncService.updateById(userItemSync);
            }
        } else {
            userInventory.setQuantity(userInventory.getQuantity() + receiveQuantity);
            itemUserInventoryService.updateItemById(userInventory);

            if (ObjectUtil.isNotEmpty(userItemSync)) {
                userItemSync.setQuantity(userItemSync.getQuantity() + receiveQuantity);
                userItemSync.setUpdateTime(new Date());
                itemSyncService.updateById(userItemSync);
            }
        }

        itemGift.setReceivedQuantity(itemGift.getReceivedQuantity() + receiveQuantity);
        if (itemGift.getReceivedQuantity() >= itemGift.getTotalQuantity()) {
            itemGift.setStatus(GiftStatusEnum.FULL_RECEIVED.getCode());
        } else {
            itemGift.setStatus(GiftStatusEnum.PARTIAL_RECEIVED.getCode());
        }
        itemGift.setUpdateTime(new Date());
        this.updateById(itemGift);

        saveGiftReceiveRecord(itemGift.getId(), userId, receiveQuantity);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelGift(String giftId, String userId) {
        ItemGift itemGift = this.getById(giftId);
        if (ObjectUtil.isEmpty(itemGift)) {
            throw new AppException(ExceptionEnum.GIFT_NOT_EXIST);
        }

        if (!userId.equals(itemGift.getSenderUserId())) {
            throw new AppException(ExceptionEnum.GIFT_NOT_BELONG_TO_USER);
        }

        if (GiftStatusEnum.CANCELLED.getCode() == itemGift.getStatus()) {
            throw new AppException(ExceptionEnum.GIFT_ALREADY_CANCELLED);
        }

        if (GiftStatusEnum.FULL_RECEIVED.getCode() == itemGift.getStatus()) {
            throw new AppException(ExceptionEnum.GIFT_ALREADY_RECEIVED);
        }

        int remainQuantity = itemGift.getTotalQuantity() - itemGift.getReceivedQuantity();

        if (remainQuantity > 0) {
            ItemUserInventory inventory = itemUserInventoryService.queryUserItem(userId, itemGift.getItemId());
            if (ObjectUtil.isEmpty(inventory)) {
                inventory = new ItemUserInventory();
                inventory.setItemId(itemGift.getItemId());
                inventory.setUserId(userId);
                inventory.setQuantity(remainQuantity);
                inventory.setStatus(0);
                inventory.setIsDrop(0);
                inventory.setIsSync(0);
                inventory.setCreateTime(new Date());
                inventory.setGetTime(new Date());
                itemUserInventoryService.save(inventory);
            } else {
                inventory.setQuantity(inventory.getQuantity() + remainQuantity);
                itemUserInventoryService.updateItemById(inventory);
            }

            ItemSync itemSync = itemSyncService.getItemSyncByItemId(itemGift.getItemId());
            if (ObjectUtil.isNotEmpty(itemSync) && itemSync.getStatus() != null && itemSync.getStatus() == 3) {
                itemSync.setStatus(0);
                itemSyncService.updateById(itemSync);
            }
        }

        itemGift.setStatus(GiftStatusEnum.CANCELLED.getCode());
        itemGift.setCancelTime(new Date());
        itemGift.setUpdateTime(new Date());
        this.updateById(itemGift);

        return true;
    }

    @Override
    public Page<ItemGiftListVO> queryGiftList(Page<ItemGift> page, String userId, Integer type) {
        LambdaQueryWrapper<ItemGift> queryWrapper = new LambdaQueryWrapper<>();

        if (type != null && type == 1) {
            queryWrapper.eq(ItemGift::getSenderUserId, userId);
        } else if (type != null && type == 2) {
            queryWrapper.eq(ItemGift::getReceiverUserId, userId);
            queryWrapper.eq(ItemGift::getGiftType, GiftTypeEnum.DIRECT.getCode());
        }

        queryWrapper.orderByDesc(ItemGift::getCreateTime);
        Page<ItemGift> resultPage = this.page(page, queryWrapper);

        Page<ItemGiftListVO> voPage = new Page<>(resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal());
        List<ItemGiftListVO> voList = new java.util.ArrayList<>();

        for (ItemGift gift : resultPage.getRecords()) {
            ItemGiftListVO vo = cn.hutool.core.bean.BeanUtil.copyProperties(gift, ItemGiftListVO.class);

            if (gift.getGiftType() != null) {
                for (GiftTypeEnum enumItem : GiftTypeEnum.values()) {
                    if (enumItem.getCode() == gift.getGiftType()) {
                        vo.setGiftTypeName(enumItem.getName());
                        break;
                    }
                }
            }

            if (gift.getStatus() != null) {
                for (GiftStatusEnum enumItem : GiftStatusEnum.values()) {
                    if (enumItem.getCode() == gift.getStatus()) {
                        vo.setStatusName(enumItem.getName());
                        break;
                    }
                }
            }

            if (ObjectUtil.isNotEmpty(gift.getItemId())) {
                ItemInfo itemInfo = itemInfoService.getById(gift.getItemId());
                vo.setItemInfo(itemInfo);
                if (itemInfo != null) {
                    vo.setItemName(itemInfo.getName());
                    vo.setItemIcon(itemInfo.getIcon());
                    vo.setItemLevel(itemInfo.getLevel());
                }
            }

            voList.add(vo);
        }

        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public ItemGift getGiftByCode(String giftCode) {
        LambdaQueryWrapper<ItemGift> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ItemGift::getGiftCode, giftCode);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    private String generateGiftCode() {
        return RandomUtil.randomString(8).toUpperCase();
    }

    private String generateQrCode(String giftCode) {
        return "QR_GIFT_CODE:" + giftCode;
    }

    private int countUserReceivedGift(String giftId, String userId) {
        LambdaQueryWrapper<ItemGiftReceive> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ItemGiftReceive::getGiftId, giftId);
        queryWrapper.eq(ItemGiftReceive::getUserId, userId);
        return Math.toIntExact(itemGiftReceiveMapper.selectCount(queryWrapper));
    }

    private void saveGiftReceiveRecord(String giftId, String userId, int quantity) {
        ItemGiftReceive receiveRecord = new ItemGiftReceive();
        receiveRecord.setGiftId(giftId);
        receiveRecord.setUserId(userId);
        receiveRecord.setQuantity(quantity);
        receiveRecord.setReceiveTime(new Date());
        receiveRecord.setCreateTime(new Date());
        itemGiftReceiveMapper.insert(receiveRecord);
    }

}