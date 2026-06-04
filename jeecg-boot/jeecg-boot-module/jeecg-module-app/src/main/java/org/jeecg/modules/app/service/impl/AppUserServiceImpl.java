package org.jeecg.modules.app.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.constant.UserConstant;
import org.jeecg.modules.app.mapper.AppUserMapper;
import org.jeecg.modules.app.service.IAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserMapper, AppUser> implements IAppUserService {

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private AppUserMapper appUserMapper;


    @Override
    public AppUser getUserInfoById(String userId) {
        String cacheKey = UserConstant.USER_INFO_CACHE + userId;
        if (redisUtil.hasKey(cacheKey)) {
            //return (AppUser) redisUtil.get(cacheKey);
        }
        AppUser appUser = this.getById(userId);
        redisUtil.set(cacheKey, appUser, UserConstant.USER_INFO_TTL);
        return appUser;
    }

    @Override
    public AppUser getUserInfoByAccount(String account) {
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AppUser::getUsername, account);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public Boolean changePassword(AppUser appUser) {
        String salt = oConvertUtils.randomGen(8);
        appUser.setSalt(salt);
        String password = appUser.getPassword();
        String passwordEncode = PasswordUtil.encrypt(appUser.getUsername(), password, salt);
        appUser.setPassword(passwordEncode);
        this.appUserMapper.updateById(appUser);
        // 更新缓存
        redisUtil.del(UserConstant.USER_INFO_USERNAME_CACHE + appUser.getUsername());
        redisUtil.del(UserConstant.USER_INFO_CACHE + appUser.getId());
        if (appUser.getPhone() != null) {
            redisUtil.del(UserConstant.USER_INFO_PHONE_CACHE + appUser.getPhone());
        }
        return true;
    }

    @Override
    public List<AppUser> findUserListByUsername(String username) {
        if (StrUtil.isEmpty(username) || username.length() <= 3) {
            return new ArrayList<>();
        }
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().like(AppUser::getUsername, username);
        queryWrapper.last("limit 20");
        return this.list(queryWrapper);
    }

    @Override
    public AppUser findUserByUsername(String username) {
        if (username.length() <= 3) {
            return null;
        }
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(AppUser::getUsername, username);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean updateAvatar(String userId, String avatar) {
        AppUser appUser = new AppUser();
        appUser.setId(userId);
        appUser.setAvatar(avatar);
        return this.updateById(appUser);
    }

    @Override
    public boolean updateUserInfo(AppUser appUser) {
        return this.updateById(appUser);
    }

}
