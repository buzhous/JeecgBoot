package org.jeecg.modules.app.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.entity.UserConfig;
import org.jeecg.modules.app.mapper.UserConfigMapper;
import org.jeecg.modules.app.service.IUserConfigService;
import org.springframework.stereotype.Service;

/**
 * @Description: 用户配置
 * @Author: jeecg-boot
 * @Date: 2025-04-05
 * @Version: V1.0
 */
@Service
public class UserConfigServiceImpl extends ServiceImpl<UserConfigMapper, UserConfig> implements IUserConfigService {

    @Override
    public UserConfig queryConfigByUserId(String userId) {
        QueryWrapper<UserConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserConfig::getUserId, userId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }


    @Override
    public boolean createUserConfig(UserConfig config) {
        if (StrUtil.isEmpty(config.getUserId())) {
            return false;
        }
        UserConfig userConfig = this.queryConfigByUserId(config.getUserId());
        if (ObjectUtil.isEmpty(userConfig)) {
            config.setIsRefreshTag(1); // 默认需要刷新
            config.setIsSyncOnlineItem(1); // 默认需要刷新
            return this.save(config);
        }
        return true;
    }

    @Override
    public boolean updateUserConfig(UserConfig config) {
        if (StrUtil.isEmpty(config.getId())) {
            return false;
        }
        return this.updateById(config);
    }

}
