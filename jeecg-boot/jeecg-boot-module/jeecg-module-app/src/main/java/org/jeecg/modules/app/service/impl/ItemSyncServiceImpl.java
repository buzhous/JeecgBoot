package org.jeecg.modules.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.enums.SyncOpsEnum;
import org.jeecg.modules.app.bean.enums.SyncStatusEnum;
import org.jeecg.modules.app.bean.exception.AppException;
import org.jeecg.modules.app.bean.vo.inventory.ItemInventoryVO;
import org.jeecg.modules.app.bean.vo.item.ItemInfoCreateVO;
import org.jeecg.modules.app.bean.vo.queue.BatchItemSyncReqVO;
import org.jeecg.modules.app.bean.vo.queue.BatchItemSyncRspVO;
import org.jeecg.modules.app.bean.vo.queue.ItemSyncDownloadReqVO;
import org.jeecg.modules.app.bean.vo.queue.ItemSyncDownloadRspVO;
import org.jeecg.modules.app.bean.vo.queue.ItemSyncReqVO;
import org.jeecg.modules.app.bean.vo.queue.ItemSyncRspVO;
import org.jeecg.modules.app.bean.vo.queue.MakeSyncIdReqVO;
import org.jeecg.modules.app.entity.ItemSync;
import org.jeecg.modules.app.entity.ItemUserInventory;
import org.jeecg.modules.app.mapper.ItemSyncMapper;
import org.jeecg.modules.app.service.IItemInfoService;
import org.jeecg.modules.app.service.IItemRuinsService;
import org.jeecg.modules.app.service.IItemSyncService;
import org.jeecg.modules.app.service.IItemUserInventoryService;
import org.jeecg.modules.app.utils.FieldElementUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemSyncServiceImpl extends ServiceImpl<ItemSyncMapper, ItemSync> implements IItemSyncService {

    @Lazy
    @Autowired
    IItemRuinsService itemRuinsService;

    @Lazy
    @Autowired
    IItemUserInventoryService itemUserInventoryService;

    @Lazy
    @Autowired
    IItemInfoService itemInfoService;

    // 根据itemId查询物品同步记录方法
    @Override
    public ItemSync getItemSyncByItemId(String itemId) {
        LambdaQueryWrapper<ItemSync> queryWrapper = new QueryWrapper<ItemSync>().lambda();
        queryWrapper.eq(ItemSync::getItemId, itemId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public ItemSync queryItemSyncByItemId(String itemId, String oriId, String userId) {
        LambdaQueryWrapper<ItemSync> queryWrapper = new QueryWrapper<ItemSync>().lambda();
        queryWrapper.eq(ItemSync::getItemId, itemId);
        queryWrapper.eq(ItemSync::getOriId, oriId);
        queryWrapper.eq(ItemSync::getUserId, userId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    // 根据oriId查询物品同步记录方法
    @Override
    public ItemSync getItemSyncByOriId(String oriId, String inventoryId) {
        LambdaQueryWrapper<ItemSync> queryWrapper = new QueryWrapper<ItemSync>().lambda();
        queryWrapper.eq(ItemSync::getOriId, oriId);
        queryWrapper.eq(ItemSync::getInventoryId, inventoryId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    // 检查原始Id+用户ID查找是否存在
    @Override
    public ItemSync getItemSyncByOriIdAndUserId(String oriId, String userId) {
        LambdaQueryWrapper<ItemSync> queryWrapper = new QueryWrapper<ItemSync>().lambda();
        queryWrapper.eq(ItemSync::getOriId, oriId);
        queryWrapper.eq(ItemSync::getUserId, userId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    // 检测线上物品ID是否存在
    @Override
    public ItemSync queryItemSync(MakeSyncIdReqVO syncVO) {
        if (ObjectUtil.isNotEmpty(syncVO.getItemId())) {
            ItemSync itemSync = this.getItemSyncByItemId(syncVO.getItemId());
            if (ObjectUtil.isNotEmpty(itemSync)) {
                return itemSync;
            }
        }
        // 检查是否存在队列id+原始Id
        if (ObjectUtil.isNotEmpty(syncVO.getQueueId()) && ObjectUtil.isNotEmpty(syncVO.getOriId())) {
            ItemSync itemSync = this.getItemSyncByOriId(syncVO.getOriId(), syncVO.getInventoryId());
            if (ObjectUtil.isNotEmpty(itemSync)) {
                return itemSync;
            }
        }
        // 检查原始Id+用户ID查找是否存在
        if (ObjectUtil.isNotEmpty(syncVO.getOriId()) && ObjectUtil.isNotEmpty(syncVO.getUserId())) {
            ItemSync itemSync = this.getItemSyncByOriIdAndUserId(syncVO.getOriId(), syncVO.getUserId());
            if (ObjectUtil.isNotEmpty(itemSync)) {
                return itemSync;
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ItemSync makeSyncId(MakeSyncIdReqVO syncIdReqVO) {
        // 校验物品Id是否存在
        ItemSync itemSyncExist = this.queryItemSync(syncIdReqVO);
        if (ObjectUtil.isNotEmpty(itemSyncExist)) {
            return itemSyncExist;
        }
        // 校验用户ID是否存在
        if (ObjectUtil.isEmpty(syncIdReqVO.getUserId())) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }

        ItemSync itemSync = new ItemSync();
        itemSync.setOriId(syncIdReqVO.getOriId());
        itemSync.setUserId(syncIdReqVO.getUserId());
        itemSync.setCategory("0");
        itemSync.setVersion(1);
        itemSync.setSyncStatus(SyncStatusEnum.UNSYNCED.getCode());

        // 创建线上物品
        ItemInfoCreateVO itemInfoCreateVO = new ItemInfoCreateVO();
        itemInfoCreateVO.setOriId(syncIdReqVO.getOriId());
        itemInfoCreateVO.setUserId(syncIdReqVO.getUserId());
        itemInfoService.createItemInfoTmp(itemInfoCreateVO);
        // 创建线上用户物品库存
        ItemInventoryVO itemInventoryVO = new ItemInventoryVO();
        itemInventoryVO.setItemId(itemInfoCreateVO.getItemId());
        itemInventoryVO.setUserId(syncIdReqVO.getUserId());
        itemUserInventoryService.createItemUserInventoryTmp(itemInventoryVO);

        // 补全物品Id、持有Id
        itemSync.setItemId(itemInfoCreateVO.getItemId());
        itemSync.setInventoryId(itemInventoryVO.getId());

        // 创建同步物品
        this.saveOrUpdate(itemSync);

        return itemSync;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean syncUploadAdd(ItemSyncReqVO syncReqVO) {
        // 根据itemId、oriId更新同步状态
        ItemSync itemSyncExist = this.queryItemSyncByItemId(syncReqVO.getItemId(), syncReqVO.getOriId(), syncReqVO.getUserId());
        if (ObjectUtil.isEmpty(itemSyncExist)) {
            throw new AppException(ExceptionEnum.ITEM_NOT_EXIST);
        }
        if (itemSyncExist.getStatus().equals(SyncStatusEnum.DESTROYED.getCode())) {
            throw new AppException(ExceptionEnum.ITEM_DESTROYED);
        }
        ItemSync itemSync = FieldElementUtil.convertObject(syncReqVO, new ItemSync());
        itemSync.setSyncStatus(SyncStatusEnum.SUCCESS.getCode());
        itemSync.setVersion(itemSyncExist.getVersion() + 1);
        syncReqVO.setSyncStatus(SyncStatusEnum.SUCCESS.getCode());
        syncReqVO.setVersion(itemSyncExist.getVersion() + 1);
        itemSync.setId(itemSyncExist.getId());
        // 更新物品同步信息
        boolean result = this.updateById(itemSync);
        if (result) {
            // 更新线上物品
            ItemInfoCreateVO itemInfoCreateVO = FieldElementUtil.convertObject(itemSync, new ItemInfoCreateVO());
            itemInfoService.updateItemInfo(itemInfoCreateVO);
            // 更新线上用户物品库存
            ItemInventoryVO itemInventoryVO = FieldElementUtil.convertObject(itemSync, new ItemInventoryVO());
            itemUserInventoryService.updateItemUserInventory(itemInventoryVO);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean syncUploadEdit(ItemSyncReqVO syncReqVO) {
        // 根据itemId、oriId更新同步状态
        ItemSync itemSyncExist = this.queryItemSyncByItemId(syncReqVO.getItemId(), syncReqVO.getOriId(), syncReqVO.getUserId());
        if (ObjectUtil.isEmpty(itemSyncExist)) {
            throw new AppException(ExceptionEnum.ITEM_NOT_EXIST);
        }
        if (itemSyncExist.getStatus().equals(SyncStatusEnum.DESTROYED.getCode())) {
            throw new AppException(ExceptionEnum.ITEM_DESTROYED);
        }
        ItemSync itemSync = FieldElementUtil.convertToJsonFields(syncReqVO, new ItemSync());
        itemSync.setSyncStatus(SyncStatusEnum.SUCCESS.getCode());
        itemSync.setVersion(itemSyncExist.getVersion() + 1);
        syncReqVO.setSyncStatus(SyncStatusEnum.SUCCESS.getCode());
        syncReqVO.setVersion(itemSyncExist.getVersion() + 1);
        itemSync.setId(itemSyncExist.getId());
        // 更新物品同步信息
        boolean result = this.updateById(itemSync);
        if (result) {
            // 更新线上物品
            ItemInfoCreateVO itemInfoCreateVO = FieldElementUtil.convertObject(itemSync, new ItemInfoCreateVO());
            itemInfoService.updateItemInfo(itemInfoCreateVO);
            // 更新线上用户物品库存
            ItemInventoryVO itemInventoryVO = FieldElementUtil.convertObject(itemSync, new ItemInventoryVO());
            itemUserInventoryService.updateItemUserInventory(itemInventoryVO);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean syncDestroy(ItemSyncReqVO syncVO) {
        ItemSync itemSyncExist = this.queryItemSyncByItemId(syncVO.getItemId(),
                syncVO.getOriId(), syncVO.getUserId());
        if (ObjectUtil.isEmpty(itemSyncExist)) {
            throw new AppException(ExceptionEnum.ITEM_NOT_EXIST);
        }
        if (itemSyncExist.getStatus().equals(SyncStatusEnum.DESTROYED.getCode())) {
            throw new AppException(ExceptionEnum.ITEM_DESTROYED);
        }

        ItemUserInventory inventory = itemUserInventoryService.queryUserItem(syncVO.getUserId(), syncVO.getItemId());
        if (ObjectUtil.isEmpty(inventory)) {
            throw new AppException(ExceptionEnum.ITEM_NOT_EXIST);
        }

        int inventoryQuantity = ObjectUtil.isEmpty(inventory.getQuantity()) ? 0 : inventory.getQuantity();
        int destroyQuantity = ObjectUtil.isEmpty(syncVO.getQuantity()) ? inventoryQuantity : syncVO.getQuantity();

        if (destroyQuantity > inventoryQuantity) {
            throw new AppException(ExceptionEnum.INSUFFICIENT_QUANTITY);
        }

        if (destroyQuantity >= inventoryQuantity) {
            ItemSync itemSyncUpdate = new ItemSync();
            itemSyncUpdate.setId(itemSyncExist.getId());
            itemSyncUpdate.setStatus(SyncStatusEnum.DESTROYED.getCode());
            itemSyncUpdate.setVersion(syncVO.getVersion());
            this.updateById(itemSyncUpdate);

            return itemRuinsService.dropToRuins(itemSyncExist);
        } else {
            inventory.setQuantity(inventoryQuantity - destroyQuantity);
            itemUserInventoryService.updateItemById(inventory);

            ItemSync itemSyncUpdate = new ItemSync();
            itemSyncUpdate.setId(itemSyncExist.getId());
            itemSyncUpdate.setQuantity(destroyQuantity);
            itemSyncUpdate.setVersion(syncVO.getVersion());
            this.updateById(itemSyncUpdate);

            return true;
        }
    }

    @Override
    public ItemSyncDownloadRspVO syncDownload(ItemSyncDownloadReqVO syncVO) {
        // 查询单个同步队列信息
        ItemSync itemSync = BeanUtil.copyProperties(syncVO, ItemSync.class);
        itemSync.setSyncStatus(SyncStatusEnum.SUCCESS.getCode());
        // 根据itemId、oriId更新同步状态
        LambdaQueryWrapper<ItemSync> queryWrapper = new QueryWrapper<ItemSync>().lambda();
        queryWrapper.eq(ItemSync::getItemId, itemSync.getItemId());
        queryWrapper.eq(ItemSync::getOriId, itemSync.getOriId());
        queryWrapper.eq(ItemSync::getUserId, syncVO.getUserId());
        queryWrapper.last("limit 1");
        ItemSync itemSyncExist = this.getOne(queryWrapper);
        if (ObjectUtil.isEmpty(itemSyncExist)) {
            throw new AppException(ExceptionEnum.ITEM_NOT_EXIST);
        }
        return ItemSyncDownloadRspVO.convertToVO(itemSyncExist);
    }

    @Override
    public boolean syncFromRuins(ItemSyncReqVO syncVO) {
        ItemSync itemSync = JSON.parseObject(JSON.toJSONString(syncVO), ItemSync.class);
        itemSync.setSyncStatus(SyncStatusEnum.UNSYNCED.getCode());
        ItemSync itemSyncExist = this.queryItemSyncByItemId(itemSync.getItemId(), itemSync.getOriId(), itemSync.getUserId());
        if (ObjectUtil.isEmpty(itemSyncExist)) {
            MakeSyncIdReqVO makeSyncIdReqVO = new MakeSyncIdReqVO();
            makeSyncIdReqVO.setOriId(itemSync.getOriId());
            makeSyncIdReqVO.setUserId(itemSync.getUserId());
            makeSyncIdReqVO.setItemId(itemSync.getItemId());
            itemSyncExist = makeSyncId(makeSyncIdReqVO);
        }
        syncVO.setSyncStatus(SyncStatusEnum.UNSYNCED.getCode());
        syncVO.setItemId(itemSync.getItemId());
        syncVO.setInventoryId(itemSync.getInventoryId());
        syncVO.setVersion(itemSync.getVersion());
        itemSync.setId(itemSyncExist.getId());
        return this.updateById(itemSync);
    }

    @Override
    public BatchItemSyncRspVO batchSyncUpload(BatchItemSyncReqVO batchVO) {
        BatchItemSyncRspVO rspVO = new BatchItemSyncRspVO();
        
        if (ObjectUtil.isEmpty(batchVO.getItems())) {
            return rspVO;
        }

        for (ItemSyncReqVO itemVO : batchVO.getItems()) {
            try {
                boolean result = switch (SyncOpsEnum.fromCode(itemVO.getSyncOps())) {
                    case ADD -> syncUploadAdd(itemVO);
                    case EDIT -> syncUploadEdit(itemVO);
                    case DESTROY -> syncDestroy(itemVO);
                    case DEFAULT -> false;
                };

                if (result) {
                    ItemSyncRspVO successItem = new ItemSyncRspVO();
                    successItem.setOriId(itemVO.getOriId());
                    successItem.setItemId(itemVO.getItemId());
                    successItem.setSyncStatus(itemVO.getSyncStatus());
                    successItem.setVersion(itemVO.getVersion());
                    rspVO.getSuccessList().add(successItem);
                    rspVO.setSuccessCount(rspVO.getSuccessCount() + 1);
                } else {
                    BatchItemSyncRspVO.FailItemSyncRspVO failItem = new BatchItemSyncRspVO.FailItemSyncRspVO();
                    failItem.setOriId(itemVO.getOriId());
                    failItem.setItemId(itemVO.getItemId());
                    failItem.setErrorMsg("同步操作失败");
                    rspVO.getFailList().add(failItem);
                    rspVO.setFailCount(rspVO.getFailCount() + 1);
                }
            } catch (Exception e) {
                BatchItemSyncRspVO.FailItemSyncRspVO failItem = new BatchItemSyncRspVO.FailItemSyncRspVO();
                failItem.setOriId(itemVO.getOriId());
                failItem.setItemId(itemVO.getItemId());
                failItem.setErrorMsg(e.getMessage());
                rspVO.getFailList().add(failItem);
                rspVO.setFailCount(rspVO.getFailCount() + 1);
            }
        }
        
        return rspVO;
    }

}
