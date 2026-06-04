package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.entity.UserGiveRecord;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.bean.vo.item.ItemGiveVO;
import org.jeecg.modules.app.bean.vo.inventory.ItemInventoryVO;
import org.springframework.transaction.annotation.Transactional;


public interface IUserGiveRecordService extends IService<UserGiveRecord> {

    boolean createGiveUserItemRecord(UserGiveRecord userGiveRecord);

    UserGiveRecord queryGiveUserItemRecord(String userId, String recordId);

    UserGiveRecord queryReceiveUserItemRecord(String userId, String recordId);

    boolean updateGiveRecordStatus(UserGiveRecord userGiveRecord);

    @Transactional
    ItemInventoryVO createUserInventoryItem(AppUser loginUser, ItemInventoryVO inventoryVO);

    @Transactional
    boolean giveUser(AppUser loginUser, ItemGiveVO itemGiveVO);

    @Transactional
    boolean receiveGiveItem(AppUser loginUser, ItemGiveVO itemGiveVO);

    // 拒绝领取，平台暂存7天，未领回，则作废处理
    @Transactional
    boolean giveUpGiveItem(AppUser loginUser, ItemGiveVO itemGiveVO);

    // 用户赠送物品取回
    @Transactional
    boolean retrieveGiveItem(AppUser loginUser, ItemGiveVO itemGiveVO);

}
