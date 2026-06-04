package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.bean.vo.ruins.ItemRuinsListVO;
import org.jeecg.modules.app.entity.ItemRuins;
import org.jeecg.modules.app.entity.ItemSync;


public interface IItemRuinsService extends IService<ItemRuins> {

    IPage<ItemRuinsListVO> queryNotPickupRuinsPage(Integer pageNo, Integer pageSize);

    ItemRuins queryRuinByItemId(String itemId, String oriId);

    ItemRuins queryRuinByRuinsId(String ruinsId, String itemId);

    boolean dropToRuins(ItemSync itemSync);

    boolean pickup(String runsId, String itemId, String userId, Long userCouponId);

}
