package org.jeecg.modules.app.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.service.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/app/fund")
@Slf4j
public class AppFundController extends JeecgController<AppUser, IAppUserService> {

    @Autowired
    private IAppUserService userService;


//    // 获取账号金币钱和灵石
//    @GetMapping(value = "/account")
//    public Result<Object> account(HttpServletRequest request) {
//        AppUser loginUser = AppAuthUtil.getUserInfo();
//        if (ObjectUtil.isEmpty(loginUser)) {
//            return Result.error("用户信息错误！");
//        }
//        return Result.OK(AppAuthUtil.getUserId(), AppAuthUtil.getUserInfo());
//    }

}
