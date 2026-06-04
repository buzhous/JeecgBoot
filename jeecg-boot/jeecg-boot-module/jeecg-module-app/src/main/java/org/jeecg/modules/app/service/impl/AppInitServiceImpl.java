package org.jeecg.modules.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.app.bean.vo.init.InitVO;
import org.jeecg.modules.app.entity.UserAmount;
import org.jeecg.modules.app.entity.UserConfig;
import org.jeecg.modules.app.entity.UserLoginDevice;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.mapper.AppUserMapper;
import org.jeecg.modules.app.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AppInitServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements IAppInitService {

    @Autowired
    private IUserAmountService iUserAmountService;

    @Autowired
    private IUserConfigService iUserConfigService;

    @Autowired
    private IUserLoginDeviceService iUserLoginDeviceService;


    private UserAmount queryOrCreateUserAmount(AppUser loginUser) {
        // 初始化资金账户 START ///
        UserAmount userAmount = iUserAmountService.queryByUserId(loginUser.getId());
        if (ObjectUtil.isEmpty(userAmount)) {
            // TODO 创建用户账户
            //iUserAmountService.createUserAmount(loginUser);
            //userAmount = iUserAmountService.queryByUserId(loginUser.getId());
        }
        return userAmount;
    }


    private UserConfig queryOrCreateUserConfig(AppUser loginUser) {
        UserConfig userConfig = iUserConfigService.queryConfigByUserId(loginUser.getId());
        if (ObjectUtil.isEmpty(userConfig)) {
            userConfig = new UserConfig();
            userConfig.setUserId(loginUser.getId());
            iUserConfigService.createUserConfig(userConfig);
            userConfig = iUserConfigService.queryConfigByUserId(loginUser.getId());
        }
        return userConfig;
    }

    private UserLoginDevice queryOrCreateLoginDevice(AppUser loginUser, String deviceId) {
        // 判断是否首次登录 deviceId
        UserLoginDevice loginDevice = iUserLoginDeviceService.queryLoginDevice(loginUser.getId());
        if (deviceId != null && loginDevice == null) {
            loginDevice = new UserLoginDevice();
            loginDevice.setUserId(loginUser.getId());
            loginDevice.setDeviceId(deviceId);
            iUserLoginDeviceService.createLoginDevice(loginDevice);
            loginDevice = iUserLoginDeviceService.queryLoginDevice(loginUser.getId());
            loginDevice.setIsFirst(1);
        }
        return loginDevice;
    }


    @Override
    public InitVO initUser(AppUser loginUser, String deviceId) {

        InitVO initVO = new InitVO();

        initVO.setIsFirstGift(0);
        initVO.setIsRefreshTag(1);
        //initVO.setIsSyncOnlineItem(0);

        // 初始化资金账户 START ///
        UserAmount userAmount = queryOrCreateUserAmount(loginUser);
        initVO.setFairyStoneAmount(userAmount.getFairyStoneAmount());
        initVO.setGoldCoinAmount(userAmount.getGoldCoinAmount());
        // 初始化资金账户 END ///

        // 初始化配置  START ///
        UserConfig userConfig = queryOrCreateUserConfig(loginUser);
        initVO.setIsRefreshTag(userConfig.getIsRefreshTag());
        //initVO.setIsSyncOnlineItem(userConfig.getIsSyncOnlineItem());
        // 初始化配置  END ///

        // 初始化配置登录设备信息
        UserLoginDevice loginDevice = queryOrCreateLoginDevice(loginUser, deviceId);


        return initVO;
    }

}
