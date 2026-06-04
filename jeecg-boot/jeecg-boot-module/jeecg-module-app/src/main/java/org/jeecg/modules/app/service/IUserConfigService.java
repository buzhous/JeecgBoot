package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.entity.UserConfig;


public interface IUserConfigService extends IService<UserConfig> {


    UserConfig queryConfigByUserId(String userId);

    boolean createUserConfig(UserConfig config);

    boolean updateUserConfig(UserConfig config);
}
