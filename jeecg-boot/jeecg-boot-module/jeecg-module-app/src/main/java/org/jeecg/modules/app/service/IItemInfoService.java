package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.bean.vo.item.ItemInfoCreateVO;
import org.jeecg.modules.app.entity.ItemInfo;


public interface IItemInfoService extends IService<ItemInfo> {

    /**
     * 查询物品信息
     *
     * @param itemId 物品ID
     * @return 物品信息
     */
    ItemInfo queryItemInfo(String itemId);

    /**
     * 查找物品Id
     *
     * @param userId 用户ID
     * @param oriId 原始物品ID
     * @return 物品信息
     */
    ItemInfo queryItemInfoByOriId(String userId, String oriId);

    /**
     * 创建物品信息
     *
     * @param createVO 创建VO
     * @return 是否创建成功
     */
    boolean createItemInfo(ItemInfoCreateVO createVO);

    /**
     * 创建物品信息（临时）
     *
     * @param itemInfoCreateVO 创建VO
     * @return 是否创建成功
     */
    boolean createItemInfoTmp(ItemInfoCreateVO itemInfoCreateVO);

    boolean updateItemInfo(ItemInfoCreateVO createVO);

}
