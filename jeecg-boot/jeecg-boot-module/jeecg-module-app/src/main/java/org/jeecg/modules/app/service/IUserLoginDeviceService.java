package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.entity.UserLoginDevice;


public interface IUserLoginDeviceService extends IService<UserLoginDevice> {

    UserLoginDevice queryLoginDevice(String userId);

    boolean updateLoginDevice(UserLoginDevice loginDevice);

    boolean createLoginDevice(UserLoginDevice loginDevice);
}
