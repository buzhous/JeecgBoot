package org.jeecg.modules.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.app.bean.dto.login.UserTokenDTO;
import org.jeecg.modules.app.entity.SendSmsLog;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.exception.AppException;
import org.jeecg.modules.app.bean.vo.login.UserLoginReqVO;
import org.jeecg.modules.app.constant.UserConstant;
import org.jeecg.modules.app.mapper.AppUserMapper;
import org.jeecg.modules.app.service.IAppLoginService;
import org.jeecg.modules.app.service.ISendSmsLogService;
import org.jeecg.modules.app.service.IUserAmountService;
import org.jeecg.modules.app.utils.AliyunSmsHelper;
import org.jeecg.modules.app.utils.SecurityTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AppLoginServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements IAppLoginService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    ISendSmsLogService iSendSmsLogService;

    @Autowired
    private IUserAmountService iUserAmountService;


    @Override
    public AppUser queryUserByUsername(String username) {
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AppUser::getUsername, username);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public AppUser queryUserByPhone(String phone) {
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AppUser::getPhone, phone);
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean cacheUserInfoByPhone(AppUser appUser) {
        if (appUser == null || appUser.getPhone() == null) {
            return false;
        }
        String cacheKey = UserConstant.USER_INFO_PHONE_CACHE + appUser.getPhone();
        redisUtil.set(cacheKey, JSON.toJSONString(appUser), UserConstant.USER_INFO_TTL);
        return true;
    }

    public AppUser queryCacheUserInfoByPhone(String phone) {
        if (phone == null) {
            return null;
        }
        String cacheKey = UserConstant.USER_INFO_PHONE_CACHE + phone;
        Object result = redisUtil.get(cacheKey);
        if (result != null) {
            return JSON.parseObject(String.valueOf(result), AppUser.class);
        }
        return null;
    }

    @Override
    public boolean cacheUserInfoByUsername(AppUser appUser) {
        if (appUser == null || appUser.getUsername() == null) {
            return false;
        }
        String cacheKey = UserConstant.USER_INFO_USERNAME_CACHE + appUser.getUsername();
        redisUtil.set(cacheKey, JSON.toJSONString(appUser), UserConstant.USER_INFO_TTL);
        return true;
    }

    public AppUser queryCacheUserInfoByUsername(String username) {
        if (username == null) {
            return null;
        }
        String cacheKey = UserConstant.USER_INFO_USERNAME_CACHE + username;
        Object result = redisUtil.get(cacheKey);
        if (result != null) {
            return JSON.parseObject(String.valueOf(result), AppUser.class);
        }
        return null;
    }

    @Override
    public String cacheAndRefreshToken(AppUser appUser, String token) {
        if (appUser == null || appUser.getId() == null || token == null) {
            return null;
        }
        String cacheKey = UserConstant.USER_INFO_TOKEN_CACHE + appUser.getId();
        Object result = redisUtil.get(cacheKey);
        if (result != null) {
            redisUtil.expire(cacheKey, SecurityTokenUtil.EXPIRE_TIME_SECOND);
            return String.valueOf(result);
        }
        redisUtil.set(cacheKey, token, SecurityTokenUtil.EXPIRE_TIME_SECOND);
        return token;
    }

    @Override
    public boolean clearAccessToken(AppUser appUser) {
        if (appUser == null || appUser.getId() == null) {
            return false;
        }
        String cacheKey = UserConstant.USER_INFO_TOKEN_CACHE + appUser.getId();
        redisUtil.del(cacheKey);
        return true;
    }

    // 15

    // ====================================================================
    // 用户登陆数据查询
    // ====================================================================
    @Override
    public AppUser queryUserLogin(UserLoginReqVO vo) {
        AppUser appUser = new AppUser();
        if (vo.getType() != null && vo.getType().equals(1)) {
            // 账号密码登陆
            appUser = queryCacheUserInfoByUsername(vo.getUsername());
            if (ObjectUtil.isEmpty(appUser)) {
                appUser = queryUserByUsername(vo.getUsername());
                if (appUser != null) {
                    cacheUserInfoByUsername(appUser);
                }
            }
        } else {
            // 手机号登陆
            appUser = queryCacheUserInfoByPhone(vo.getPhone());
            if (ObjectUtil.isEmpty(appUser)) {
                appUser = queryUserByPhone(vo.getUsername());
                if (appUser != null) {
                    cacheUserInfoByPhone(appUser);
                }
            }
        }
        // 额外数据处理
        // ...
        return appUser;
    }

    // ====================================================================
    // 用户手机号登陆注册
    // ====================================================================
    @Override
    public AppUser registerByPhone(UserLoginReqVO vo) {
        if (StrUtil.isEmpty(vo.getPhone()) || StrUtil.isEmpty(vo.getSmsCode())) {
            return null;
        }
        // 检查是否存在相同手机号
        AppUser appUser = queryCacheUserInfoByPhone(vo.getPhone());
        if (appUser != null) {
            return appUser;
        }
        // 检查是否存在相同账户名
        AppUser appUser2 = queryUserByUsername(vo.getPhone());
        if (appUser2 != null) {
            return appUser2;
        }
        // 开始注册
        return registerUser(vo);
    }

    @Override
    public UserTokenDTO login(UserLoginReqVO vo) {
        if (vo.getType() != null && vo.getType().equals(1)) {
            if (StrUtil.isEmpty(vo.getUsername()) || StrUtil.isEmpty(vo.getPassword())) {
                throw new AppException(ExceptionEnum.PASSWORD_ERROR);
            }
            AppUser appUser = queryUserLogin(vo);
            String password = appUser.getPassword();
            String passwordEncode = PasswordUtil.encrypt(vo.getUsername(), vo.getPassword(), appUser.getSalt());
            if (!password.equals(passwordEncode)) {
                throw new AppException(ExceptionEnum.PASSWORD_NOT_MATCH);
            }
        }

        AppUser appUser = queryUserLogin(vo);
        if (vo.getType() != null && vo.getType().equals(2) && ObjectUtil.isEmpty(appUser)) {
            // 手机号登陆直接注册
            appUser = this.registerByPhone(vo);
        }

        // 账号登录+手机号注册
        if (vo.getType() != null && vo.getType().equals(1) && ObjectUtil.isEmpty(appUser)) {
            // 账号登陆
            //appUser = loginService.registerByPhone(vo);
        }

        if (ObjectUtil.isEmpty(appUser) || StrUtil.isEmpty(appUser.getId())) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }

        // 验证通过，获取token
        UserTokenDTO tokenDTO = this.createNewToken(appUser);

        // 缓存token
        this.cacheAndRefreshToken(appUser, tokenDTO.getToken());

        return tokenDTO;
    }

    public AppUser registerUser(UserLoginReqVO loginVO) {
        AppUser appUser = new AppUser();
        String nickname = RandomUtil.randomString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 10);
        appUser.setNickname(nickname);
        // TODO 临时使用手机号作为用户名
        appUser.setUsername(loginVO.getPhone());
        appUser.setPhone(loginVO.getPhone());
        appUser.setRegisterAt(DateUtil.date());
        appUser.setStatus(1);
        appUser.setIsLock(0);
        appUser.setIsDel(0);
        boolean result = this.save(appUser);
        if (result) {
            // 缓存用户信息-手机号
            if (StrUtil.isNotEmpty(appUser.getPhone())) {
                cacheUserInfoByPhone(appUser);
            }
            // 缓存用户信息-用户账号
            if (StrUtil.isNotEmpty(appUser.getUsername())) {
                cacheUserInfoByUsername(appUser);
            }
            // 创建用户资金账号
            iUserAmountService.createUserAmount(appUser);
            return appUser;
        }
        return null;
    }

    // ====================================================================

    @Override
    public UserTokenDTO createNewToken(AppUser appUser) {
        UserTokenDTO tokenDTO = new UserTokenDTO();

        long expireTime = SecurityTokenUtil.expireTime();
        tokenDTO.setUid(appUser.getId());
        tokenDTO.setImei(appUser.getId());
        tokenDTO.setExpireTime(DateUtil.date(expireTime));

        String newToken = SecurityTokenUtil.createJwtToken(BeanUtil.beanToMap(tokenDTO));
        tokenDTO.setToken(newToken);

        return tokenDTO;
    }

    @Override
    public boolean sendSmsCode(String phone) {
        if (!Validator.isMobile(phone)) {
            return false;
        }
        String cacheKey = UserConstant.LOGIN_SMS_CODE_CACHE + phone;
        String number = RandomUtil.randomNumbers(6);
        // 5分钟有效
        Object result = redisUtil.get(cacheKey);
        if (result != null) {
            // 不重发
            //return true;
        }
        redisUtil.set(cacheKey, number, UserConstant.LOGIN_SMS_CODE_TTL);

        try {
            // 发送验证码
            //String captcha = RandomUtil.randomNumbers(6);
            JSONObject obj = new JSONObject();
            obj.put("code", number);
            obj.put("min", 5);
            //boolean sendResult = AliyunSmsHelper.sendSms(phone, obj, AliyunSmsEnum.LOGIN_TEMPLATE_CODE);
            boolean sendResult = AliyunSmsHelper.sendSms(phone, obj);

            // 发送短信日志
            SendSmsLog smsLog = new SendSmsLog();
            smsLog.setPhone(phone);
            smsLog.setSmsCode(number);
            smsLog.setSendAt(DateUtil.date());
            smsLog.setSendStatus(sendResult ? 1 : 0);
            iSendSmsLogService.saveSmsLog(smsLog);

            if (!sendResult) {
                redisUtil.del(cacheKey);
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        // 保存发送记录
        // ...

        return true;
    }

    @Override
    public boolean verifySmsCode(String phone, String smsCode) {
        if (!Validator.isMobile(phone)) {
            return false;
        }
        // TODO 验证码白名单
        if ((phone.equals("13650962253")) && smsCode.equals("888888")) {
            return true;
        }
        String cacheKey = UserConstant.LOGIN_SMS_CODE_CACHE + phone;
        // 5分钟有效
        Object result = redisUtil.get(cacheKey);
        if (result == null) {
            return false;
        }
        if (smsCode.equals(String.valueOf(result))) {
            // 仅校验一次，移除验证码
            redisUtil.del(cacheKey);
            return true;
        }
        return false;
    }


}
