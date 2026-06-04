package org.jeecg.modules.app.controller.user;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.exception.AppException;
import org.jeecg.modules.app.bean.vo.login.ForgetPasswordVO;
import org.jeecg.modules.app.bean.vo.login.UserInfoVO;
import org.jeecg.modules.app.service.IAppUserService;
import org.jeecg.modules.app.utils.AppAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Tag(name = "用户信息")
@RestController
@RequestMapping("/app/user")
public class AppUserController extends JeecgController<AppUser, IAppUserService> {

    @Autowired
    private IAppUserService iAppUserService;

    @Operation(summary = "获取用户信息")
    @GetMapping(value = "/info")
    public Result<UserInfoVO> info(HttpServletRequest request) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.ACCESS_DENIED);
        }
        AppUser appUser = iAppUserService.getUserInfoById(loginUser.getId());
        if (ObjectUtil.isEmpty(appUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        UserInfoVO userVO = JSONUtil.toBean(JSONUtil.toJsonStr(appUser), UserInfoVO.class);
        return Result.OK("", userVO);
    }

    @Operation(summary = "更新用户头像")
    @PostMapping(value = "/updateAvatar")
    public Result<Boolean> updateAvatar(@RequestBody AppUser appUser) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        boolean isUpdateSuccess = iAppUserService.updateAvatar(loginUser.getId(), appUser.getAvatar());
        if (!isUpdateSuccess) {
            throw new AppException(ExceptionEnum.DATA_UPDATE_ERROR);
        }
        return Result.OK(true);
    }

    @Operation(summary = "更新用户信息")
    @PostMapping(value = "/updateUser")
    public Result<Boolean> updateUser(@RequestBody AppUser appUser) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        appUser.setId(loginUser.getId());
        boolean isUpdateSuccess = iAppUserService.updateUserInfo(appUser);
        if (!isUpdateSuccess) {
            throw new AppException(ExceptionEnum.DATA_UPDATE_ERROR);
        }
        return Result.OK(true);
    }

    @Operation(summary = "忘记密码")
    @PostMapping(value = "/forgetPassword")
    public Result<Boolean> forgetPassword(@RequestBody ForgetPasswordVO vo) {
        AppUser appUser = AppAuthUtil.getUserInfo();
        if (appUser.getId() == null) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        if (!vo.getPassword().equals(vo.getPassword2())) {
            throw new AppException(ExceptionEnum.PASSWORD_NOT_MATCH);
        }
        AppUser appUserUpdate = new AppUser();
        appUserUpdate.setId(appUser.getId());
        appUserUpdate.setUsername(appUser.getUsername());
        appUserUpdate.setPassword(vo.getPassword());
        boolean result = iAppUserService.changePassword(appUserUpdate);
        return Result.OK(result);
    }

}
