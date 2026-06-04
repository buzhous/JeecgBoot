package org.jeecg.modules.app.controller.give;

import cn.hutool.core.util.ObjectUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.service.IAppUserService;
import org.jeecg.modules.app.utils.AppAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/app/active")
@Slf4j
public class AppActiveController extends JeecgController<AppUser, IAppUserService> {

    @Autowired
    private IAppUserService userService;


    // 新用户赠送内容
    @GetMapping(value = "/newUser")
    public Result<Object> info(HttpServletRequest request) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error("用户信息错误！");
        }
        return Result.OK(AppAuthUtil.getUserId(), AppAuthUtil.getUserInfo());
    }

    // 检测粘贴版内容是否解释符合物品
    @GetMapping(value = "pasteboard")
    public Result<Object> pasteboard(HttpServletRequest request) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error("用户信息错误！");
        }
        return Result.OK(AppAuthUtil.getUserId(), AppAuthUtil.getUserInfo());
    }

}
