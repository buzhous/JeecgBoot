package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.entity.UserAmount;
import org.jeecg.modules.app.entity.user.AppUser;


public interface IUserAmountService extends IService<UserAmount> {

    UserAmount queryByUserId(String userId);

    boolean createUserAmount(AppUser appUser);

}
