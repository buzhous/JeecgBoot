package org.jeecg.modules.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.enums.ItemStatusEnum;
import org.jeecg.modules.app.bean.exception.AppException;
import org.jeecg.modules.app.bean.vo.item.ItemInfoCreateVO;
import org.jeecg.modules.app.entity.ItemInfo;
import org.jeecg.modules.app.mapper.ItemInfoMapper;
import org.jeecg.modules.app.service.IItemInfoService;
import org.jeecg.modules.app.utils.FieldElementUtil;
import org.springframework.stereotype.Service;


@Service
public class ItemInfoServiceImpl extends ServiceImpl<ItemInfoMapper, ItemInfo> implements IItemInfoService {


    @Override
    public ItemInfo queryItemInfo(String itemId) {
        return this.getById(itemId);
    }

    @Override
    public ItemInfo queryItemInfoByOriId(String userId, String oriId) {
        LambdaQueryWrapper<ItemInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ItemInfo::getOriId, oriId);
        queryWrapper.eq(ItemInfo::getUserId, userId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean createItemInfo(ItemInfoCreateVO createVO) {
        if (ObjectUtil.isEmpty(createVO.getUserId()) || ObjectUtil.isEmpty(createVO.getOriId())) {
            throw new AppException(ExceptionEnum.REQUEST_PARAM_ERROR);
        }
        ItemInfo itemInfo = this.queryItemInfoByOriId(createVO.getUserId(), createVO.getOriId());
        if (ObjectUtil.isNotEmpty(itemInfo)) {
            // TODO 存在修改本地恢复风险
            createVO.setItemId(String.valueOf(itemInfo.getId()));
            return true;
        }
        // 解析数据，生成物品
        ItemInfo item = BeanUtil.copyProperties(createVO, ItemInfo.class);
        item.setCreateTime(DateUtil.date());
        item.setUpdateTime(DateUtil.date());
        // 创建物品信息
        boolean result = this.saveOrUpdate(item);
        createVO.setItemId(String.valueOf(item.getId()));
        return result;
    }

    @Override
    public boolean createItemInfoTmp(ItemInfoCreateVO itemInfoCreateVO) {
        // 创建线上物品
        itemInfoCreateVO.setOriId(itemInfoCreateVO.getOriId());
        itemInfoCreateVO.setUserId(itemInfoCreateVO.getUserId());
        itemInfoCreateVO.setStatus(ItemStatusEnum.TEMPORARY.getCode());
        itemInfoCreateVO.setCategory("0");
        return this.createItemInfo(itemInfoCreateVO);
    }

    @Override
    public boolean updateItemInfo(ItemInfoCreateVO createVO) {
        if (ObjectUtil.isEmpty(createVO.getUserId()) || ObjectUtil.isEmpty(createVO.getItemId())) {
            throw new AppException(ExceptionEnum.REQUEST_PARAM_ERROR);
        }
        ItemInfo itemInfoExist = this.queryItemInfo(createVO.getItemId());
        if (ObjectUtil.isEmpty(itemInfoExist)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        // 解析数据，生成物品
        ItemInfo itemInfo = FieldElementUtil.convertToJsonFields(createVO, new ItemInfo());
        itemInfo.setId(itemInfoExist.getId());
        itemInfo.setCreateTime(DateUtil.date());
        itemInfo.setUpdateTime(DateUtil.date());
        itemInfo.setStatus(ItemStatusEnum.NORMAL.getCode());
        // 创建物品信息
        boolean result = this.updateById(itemInfo);
        createVO.setItemId(String.valueOf(itemInfo.getId()));
        return result;
    }

}
