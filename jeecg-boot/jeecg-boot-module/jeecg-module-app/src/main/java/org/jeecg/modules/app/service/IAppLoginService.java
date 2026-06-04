package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.bean.dto.login.UserTokenDTO;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.bean.vo.login.UserLoginReqVO;


public interface IAppLoginService extends IService<AppUser> {

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @return 是否发送成功
     */
    boolean sendSmsCode(String phone);

    /**
     * 用户登录
     *
     * @param vo 登录请求参数
     * @return 用户登录令牌
     */
    UserTokenDTO login(UserLoginReqVO vo);


    AppUser queryUserByUsername(String username);

    AppUser queryUserByPhone(String phone);

    boolean cacheUserInfoByPhone(AppUser appUser);

    boolean cacheUserInfoByUsername(AppUser appUser);

    String cacheAndRefreshToken(AppUser appUser, String token);

    boolean clearAccessToken(AppUser appUser);

    AppUser queryUserLogin(UserLoginReqVO vo);

    AppUser registerByPhone(UserLoginReqVO vo);


    UserTokenDTO createNewToken(AppUser appUser);

    boolean verifySmsCode(String phone, String smsCode);

}
