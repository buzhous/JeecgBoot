package org.jeecg.modules.app.controller.init;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.app.bean.vo.init.InitVO;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.service.IAppInitService;
import org.jeecg.modules.app.service.IAppUserService;
import org.jeecg.modules.app.utils.AppAuthUtil;
import org.jeecg.modules.app.utils.AppRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/app/init")
@Slf4j
public class AppInitController {

    @Autowired
    private IAppInitService iAppInitService;


    @GetMapping
    public Result<Object> init(HttpServletRequest request) {
        String deviceId = AppRequestUtil.getDeviceId(request);
        if (StrUtil.isEmpty(deviceId)) {
            return Result.error("设备标识为空！");
        }
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error("用户信息不存在！");
        }

        // 登录初始化操作
        InitVO initVO = iAppInitService.initUser(loginUser, deviceId);

        return Result.OK(initVO);
    }


}
