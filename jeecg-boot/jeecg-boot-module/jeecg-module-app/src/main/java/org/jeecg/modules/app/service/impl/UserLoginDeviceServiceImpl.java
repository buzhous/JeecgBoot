package org.jeecg.modules.app.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.entity.UserLoginDevice;
import org.jeecg.modules.app.mapper.UserLoginDeviceMapper;
import org.jeecg.modules.app.service.IUserLoginDeviceService;
import org.springframework.stereotype.Service;


@Service
public class UserLoginDeviceServiceImpl extends ServiceImpl<UserLoginDeviceMapper, UserLoginDevice> implements IUserLoginDeviceService {


    @Override
    public UserLoginDevice queryLoginDevice(String userId) {
        QueryWrapper<UserLoginDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserLoginDevice::getUserId, userId);
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean updateLoginDevice(UserLoginDevice loginDevice) {
        if (loginDevice.getUserId() == null) {
            return false;
        }
        QueryWrapper<UserLoginDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserLoginDevice::getUserId, loginDevice.getUserId());
        return this.update(queryWrapper);
    }

    @Override
    public boolean createLoginDevice(UserLoginDevice loginDevice) {
        if (loginDevice.getUserId() == null || loginDevice.getDeviceId() == null) {
            return false;
        }
        loginDevice.setLoginTime(DateUtil.date());
        return this.save(loginDevice);
    }

}
