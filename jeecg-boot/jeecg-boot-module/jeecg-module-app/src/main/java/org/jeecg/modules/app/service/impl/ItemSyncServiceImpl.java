package org.jeecg.modules.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.enums.SyncStatusEnum;
import org.jeecg.modules.app.bean.exception.AppException;
import org.jeecg.modules.app.bean.vo.inventory.ItemInventoryVO;
import org.jeecg.modules.app.bean.vo.item.ItemInfoCreateVO;
import org.jeecg.modules.app.bean.vo.queue.ItemSyncDownloadReqVO;
import org.jeecg.modules.app.bean.vo.queue.ItemSyncDownloadRspVO;
import org.jeecg.modules.app.bean.vo.queue.ItemSyncReqVO;
import org.jeecg.modules.app.bean.vo.queue.MakeSyncIdReqVO;
import org.jeecg.modules.app.entity.ItemSync;
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

    // 根据queueId查询物品同步记录方法
    @Override
    public ItemSync getItemSyncByQueueId(String queueId) {
        LambdaQueryWrapper<ItemSync> queryWrapper = new QueryWrapper<ItemSync>().lambda();
        queryWrapper.eq(ItemSync::getQueueId, queueId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    // 根据oriId查询物品同步记录方法
    @Override
    public ItemSync getItemSyncByOriId(String oriId, String queueId) {
        LambdaQueryWrapper<ItemSync> queryWrapper = new QueryWrapper<ItemSync>().lambda();
        queryWrapper.eq(ItemSync::getOriId, oriId);
        queryWrapper.eq(ItemSync::getQueueId, queueId);
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
            ItemSync itemSync = this.getItemSyncByOriId(syncVO.getOriId(), syncVO.getQueueId());
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
        itemSync.setQueueId(IdUtil.getSnowflakeNextIdStr());

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
        // 不更新队列ID
        syncReqVO.setQueueId(null);

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
        syncReqVO.setSyncStatus(SyncStatusEnum.SUCCESS.getCode());
        syncReqVO.setVersion(itemSyncExist.getVersion() + 1);
        syncReqVO.setQueueId(itemSyncExist.getQueueId());
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
        // 不更新队列ID
        syncReqVO.setQueueId(null);

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
        syncReqVO.setSyncStatus(SyncStatusEnum.SUCCESS.getCode());
        syncReqVO.setVersion(itemSyncExist.getVersion() + 1);
        syncReqVO.setQueueId(itemSyncExist.getQueueId());
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
    public boolean syncDestroy(ItemSyncReqVO syncVO) {
        // 同步销毁状态
        ItemSync itemSyncExist = this.queryItemSyncByItemId(syncVO.getItemId(),
                syncVO.getOriId(), syncVO.getUserId());
        if (ObjectUtil.isEmpty(itemSyncExist)) {
            throw new AppException(ExceptionEnum.ITEM_NOT_EXIST);
        }
        if (itemSyncExist.getStatus().equals(SyncStatusEnum.DESTROYED.getCode())) {
            throw new AppException(ExceptionEnum.ITEM_DESTROYED);
        }
        // 校对丢弃数量是否与库存数量相等
        //

        ItemSync itemSyncUpdate = new ItemSync();
        itemSyncUpdate.setId(itemSyncExist.getId());
        itemSyncUpdate.setStatus(SyncStatusEnum.DESTROYED.getCode());
        itemSyncUpdate.setVersion(syncVO.getVersion());
        this.updateById(itemSyncUpdate);

        // 线上物品丢入殷墟
        return itemRuinsService.dropToRuins(itemSyncExist);
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
        // 性能风险项
        ItemSync itemSync = JSON.parseObject(JSON.toJSONString(syncVO), ItemSync.class);
        itemSync.setSyncStatus(SyncStatusEnum.UNSYNCED.getCode());
        // 根据itemId、oriId更新同步状态
        ItemSync itemSyncExist = this.queryItemSyncByItemId(itemSync.getItemId(), itemSync.getOriId(), itemSync.getUserId());
        // 不存在则创建物品同步记录
        if (ObjectUtil.isEmpty(itemSyncExist)) {
            MakeSyncIdReqVO makeSyncIdReqVO = new MakeSyncIdReqVO();
            makeSyncIdReqVO.setOriId(itemSync.getOriId());
            makeSyncIdReqVO.setUserId(itemSync.getUserId());
            makeSyncIdReqVO.setItemId(itemSync.getItemId());
            itemSyncExist = makeSyncId(makeSyncIdReqVO);
        }
        // 更新物品状态返回
        syncVO.setSyncStatus(SyncStatusEnum.UNSYNCED.getCode());
        syncVO.setQueueId(itemSync.getId().toString());
        syncVO.setVersion(itemSync.getVersion());
        // 补全更新Id
        itemSync.setId(itemSyncExist.getId());
        return this.updateById(itemSync);
    }

}
