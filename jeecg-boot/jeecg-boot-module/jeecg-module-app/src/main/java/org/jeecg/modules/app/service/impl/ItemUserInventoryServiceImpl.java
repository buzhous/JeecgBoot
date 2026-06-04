package org.jeecg.modules.app.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.bean.enums.InventoryStatusEnum;
import org.jeecg.modules.app.bean.vo.inventory.ItemInventoryVO;
import org.jeecg.modules.app.entity.ItemInfo;
import org.jeecg.modules.app.entity.ItemUserInventory;
import org.jeecg.modules.app.mapper.ItemUserInventoryMapper;
import org.jeecg.modules.app.service.IItemInfoService;
import org.jeecg.modules.app.service.IItemUserInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ItemUserInventoryServiceImpl extends ServiceImpl<ItemUserInventoryMapper, ItemUserInventory> implements IItemUserInventoryService {


    @Autowired
    IItemInfoService iItemInfoService;


    @Override
    public ItemUserInventory queryUserItem(String userId, String itemId) {
        if (StrUtil.isEmpty(userId) || StrUtil.isEmpty(itemId)) {
            return null;
        }
        QueryWrapper<ItemUserInventory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ItemUserInventory::getUserId, userId);
        queryWrapper.lambda().eq(ItemUserInventory::getItemId, itemId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean createItemUserInventory(ItemInventoryVO itemInventoryVO) {
        if (StrUtil.isEmpty(itemInventoryVO.getItemId()) || StrUtil.isEmpty(itemInventoryVO.getUserId())) {
            return false;
        }
        ItemInfo itemInfo = iItemInfoService.getById(itemInventoryVO.getItemId());
        if (itemInfo == null) {
            return false;
        }
        ItemUserInventory itemUserInventory = new ItemUserInventory();
        itemUserInventory.setUserId(itemInventoryVO.getUserId());
        itemUserInventory.setItemId(itemInventoryVO.getItemId());
        itemUserInventory.setQuantity(itemInventoryVO.getQuantity() != null ? itemInventoryVO.getQuantity() : 0);
        itemUserInventory.setGetTime(DateUtil.date());
        itemUserInventory.setStatus(InventoryStatusEnum.NORMAL.getCode());
        boolean result = this.save(itemUserInventory);
        itemInventoryVO.setId(itemUserInventory.getId());
        return result;
    }

    @Override
    public boolean createItemUserInventoryTmp(ItemInventoryVO itemInventoryVO) {
        if (StrUtil.isEmpty(itemInventoryVO.getItemId()) || StrUtil.isEmpty(itemInventoryVO.getUserId())) {
            return false;
        }
        ItemInfo itemInfo = iItemInfoService.getById(itemInventoryVO.getItemId());
        if (itemInfo == null) {
            return false;
        }
        ItemUserInventory itemUserInventory = new ItemUserInventory();
        itemUserInventory.setUserId(itemInventoryVO.getUserId());
        itemUserInventory.setItemId(itemInventoryVO.getItemId());
        itemUserInventory.setQuantity(itemInventoryVO.getQuantity() != null ? itemInventoryVO.getQuantity() : 0);
        itemUserInventory.setGetTime(DateUtil.date());
        itemUserInventory.setStatus(InventoryStatusEnum.TEMPORARY.getCode());
        boolean result = this.save(itemUserInventory);
        itemInventoryVO.setId(itemUserInventory.getId());
        return result;
    }

    @Override
    public boolean updateItemUserInventory(ItemInventoryVO inventoryVO) {
        if (StrUtil.isEmpty(inventoryVO.getItemId()) || StrUtil.isEmpty(inventoryVO.getUserId())) {
            return false;
        }
        ItemInfo itemInfo = iItemInfoService.getById(inventoryVO.getItemId());
        if (itemInfo == null) {
            return false;
        }
        // 查询用户是否有线上物品
        ItemUserInventory itemUserInventory = this.queryUserItem(inventoryVO.getUserId(), inventoryVO.getItemId());
        if (itemUserInventory == null) {
            return false;
        }

        ItemUserInventory inventoryUpdate = new ItemUserInventory();
        inventoryUpdate.setId(itemUserInventory.getId());
        inventoryUpdate.setUserId(inventoryVO.getUserId());
        inventoryUpdate.setItemId(inventoryVO.getItemId());
        if (inventoryVO.getQuantity() != null) {
            inventoryUpdate.setQuantity(inventoryVO.getQuantity());
        }
        inventoryUpdate.setGetTime(DateUtil.date());
        inventoryUpdate.setStatus(InventoryStatusEnum.NORMAL.getCode());
        boolean result = this.updateById(inventoryUpdate);

        // 补充库存Id
        inventoryVO.setId(inventoryUpdate.getId());

        return result;
    }

    @Override
    public List<ItemUserInventory> queryItemUserInventoryList(String userId, Integer isSync) {
        if (StrUtil.isEmpty(userId)) {
            return null;
        }
        QueryWrapper<ItemUserInventory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ItemUserInventory::getUserId, userId);
        // 未丢弃物品
        queryWrapper.lambda().eq(ItemUserInventory::getIsDrop, 0);
        queryWrapper.lambda().ge(ItemUserInventory::getQuantity, 1);

        if (isSync == 0 || isSync == 1) {
            queryWrapper.lambda().eq(ItemUserInventory::getIsSync, isSync);
        } else {
            // 查询没有限制
        }

        queryWrapper.last("limit 99999");
        return this.list(queryWrapper);
    }

    // 检测用户是否有线上物品
    @Override
    public boolean checkExistItem(String userId) {
        if (StrUtil.isEmpty(userId)) {
            return false;
        }
        QueryWrapper<ItemUserInventory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ItemUserInventory::getUserId, userId);
        // 未丢弃物品
        queryWrapper.lambda().eq(ItemUserInventory::getIsDrop, 0);
        queryWrapper.lambda().ge(ItemUserInventory::getQuantity, 1);
        queryWrapper.last("limit 1");
        return this.count(queryWrapper) >= 1;
    }

    // 检测用户是否有线上物品
    @Override
    public boolean updateItemSync(ItemUserInventory inventory) {
        if (StrUtil.isEmpty(inventory.getUserId())) {
            return false;
        }
        QueryWrapper<ItemUserInventory> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ItemUserInventory::getUserId, inventory.getUserId());
        // 未丢弃物品
        queryWrapper.lambda().ge(ItemUserInventory::getIsDrop, 0);
        queryWrapper.lambda().ge(ItemUserInventory::getIsSync, 0);
        queryWrapper.lambda().orderByDesc(ItemUserInventory::getGetTime);

        inventory.setIsSync(1);

        return this.update(inventory, queryWrapper);
    }

    @Override
    public boolean updateItemById(ItemUserInventory inventory) {
        if (StrUtil.isEmpty(inventory.getId())) {
            return false;
        }
        return this.updateById(inventory);
    }

    @Override
    public List<ItemInventoryVO> queryUserItems(String userId) {
        List<ItemUserInventory> itemInventories = queryItemUserInventoryList(userId, 0);
        if (itemInventories == null) {
            return new ArrayList<>();
        }

        List<ItemInfo> itemInfoList = getItemInfoList(itemInventories);
        //Map<String, List<ItemInfo>> itemInfoMap = itemInfoList.stream().collect(Collectors.groupingBy(ItemInfo::getId));
        //Map<String, List<Category>> itemCateMap = getItemCategoryMap(itemInfoList);

        List<ItemInventoryVO> items = new ArrayList<>();
        for (ItemUserInventory inventory : itemInventories) {
            ItemInventoryVO itemInfoVO = new ItemInventoryVO();
            itemInfoVO.setId(inventory.getId());
            itemInfoVO.setItemId(inventory.getItemId());
            itemInfoVO.setQuantity(inventory.getQuantity());
            itemInfoVO.setStatus(inventory.getStatus());
            //itemInfoVO.setGetTime(inventory.getGetTime());
        }
        return items;
    }


    //
    private List<ItemInfo> getItemInfoList(List<ItemUserInventory> itemInventories) {
        List<String> itemIds = itemInventories.stream().map(ItemUserInventory::getItemId).collect(Collectors.toList());
        List<ItemInfo> itemInfoList = iItemInfoService.listByIds(itemIds);
        return itemInfoList;
    }

    @Override
    public boolean removeUserItem(String userId, String inventoryId) {
        LambdaQueryWrapper<ItemUserInventory> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ItemUserInventory::getUserId, userId);
        queryWrapper.eq(ItemUserInventory::getId, inventoryId);
        ItemUserInventory inventory = this.getOne(queryWrapper);
        if (inventory != null) {
            return this.removeById(inventoryId);
        }
        return false;
    }

}
