package org.jeecg.modules.app.controller.settings;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.entity.UserConfig;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.service.IUserConfigService;
import org.jeecg.modules.app.utils.AppAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户设置")
@Slf4j
@RestController
@RequestMapping("/app/user/config")
public class AppUserConfigController {

    @Autowired
    private IUserConfigService iUserConfigService;


    @Operation(summary = "保存用户配置")
    @PostMapping("/save")
    public Result<?> save(@RequestBody UserConfig config) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error(ExceptionEnum.USER_INFO_NOT_EXIST.getMsg());
        }

        UserConfig userConfig = iUserConfigService.queryConfigByUserId(loginUser.getId());
        if (ObjectUtil.isEmpty(userConfig)) {
            userConfig = new UserConfig();
            userConfig.setUserId(loginUser.getId());
            iUserConfigService.createUserConfig(userConfig);
        }
        UserConfig updateConfig = new UserConfig();
        updateConfig.setId(userConfig.getId());

        if (config.getIsRefreshTag() != null) {
            updateConfig.setIsRefreshTag(config.getIsRefreshTag());
        }

        if (config.getIsSyncOnlineItem() != null) {
            updateConfig.setIsSyncOnlineItem(config.getIsSyncOnlineItem());
        }

        if (config.getIsDeviceFirst() != null) {
            updateConfig.setIsDeviceFirst(config.getIsDeviceFirst());
        }

        boolean result = iUserConfigService.updateUserConfig(updateConfig);

        return Result.OK(result);
    }

}
