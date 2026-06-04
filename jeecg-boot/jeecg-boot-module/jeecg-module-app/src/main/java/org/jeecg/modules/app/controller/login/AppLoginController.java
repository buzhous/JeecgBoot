package org.jeecg.modules.app.controller.login;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.IOUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.app.bean.dto.login.UserTokenDTO;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.exception.AppException;
import org.jeecg.modules.app.bean.vo.login.UserLoginReqVO;
import org.jeecg.modules.app.bean.vo.login.UserLoginRespVO;
import org.jeecg.modules.app.service.IAppLoginService;
import org.jeecg.modules.app.utils.AppAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Tag(name = "用户登录", description = "用户登录")
@RestController
@RequestMapping("/app/login")
public class AppLoginController {

    @Autowired
    private IAppLoginService loginService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Operation(summary = "发送短信验证码")
    @GetMapping(value = "/sendSms")
    public Result<Boolean> sendSms(String phone) {
        if (!Validator.isMobile(phone)) {
            throw new AppException(ExceptionEnum.PHONE_ERROR);
        }
        boolean result = loginService.sendSmsCode(phone);
        if (!result) {
            return Result.error("发送失败！");
        }
        return Result.OK(true);
    }

    @Operation(summary = "用户登录")
    @PostMapping(value = "/signin")
    public Result<UserLoginRespVO> signin(@RequestBody UserLoginReqVO loginReqVO) {
        if (loginReqVO.getType() == null) {
            loginReqVO.setType(1);
        }
        if (loginReqVO.getType() != null && loginReqVO.getType().equals(2)) {
            if (!Validator.isMobile(loginReqVO.getPhone())) {
                throw new AppException(ExceptionEnum.PHONE_ERROR);
            }
            if (StrUtil.isEmpty(loginReqVO.getSmsCode())) {
                throw new AppException(ExceptionEnum.SMS_CODE_ERROR);
            }
            if (!loginService.verifySmsCode(loginReqVO.getPhone(), loginReqVO.getSmsCode())) {
                throw new AppException(ExceptionEnum.SMS_CODE_ERROR);
            }
        }
        UserTokenDTO tokenDTO = loginService.login(loginReqVO);
        UserLoginRespVO respVO = new UserLoginRespVO();
        respVO.setAccessToken(tokenDTO.getToken());
        respVO.setExpireTime(tokenDTO.getExpireTime());
        return Result.OK(respVO);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<UserLoginRespVO> logout() {
        AppUser appUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isNotEmpty(appUser)) {
            loginService.clearAccessToken(appUser);
        }
        return Result.OK("退出成功");
    }

    // 读取文件中的协议内容
    // http://localhost:8080/api/app/login/agreement/privacy-policy.html
    @GetMapping("/agreement/{arg}")
    public String showAgreement(@PathVariable(name = "arg") String arg) throws IOException {
        String agreement = arg != null ? arg : "user-agreement.html";
        Resource resource = resourceLoader.getResource("classpath:agreements/" + agreement);
        try (InputStream inputStream = resource.getInputStream()) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
    }

}