package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.bean.vo.init.InitVO;
import org.jeecg.modules.app.entity.user.AppUser;


public interface IAppInitService extends IService<AppUser> {

    /**
     * 初始化用户
     *
     * @param loginUser 登录用户
     * @param deviceId 设备ID
     * @return 初始化VO
     */
    InitVO initUser(AppUser loginUser, String deviceId);

}
