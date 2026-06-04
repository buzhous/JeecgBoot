package org.jeecg.modules.app.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.entity.UserAmount;
import org.jeecg.modules.app.mapper.UserAmountMapper;
import org.jeecg.modules.app.service.IUserAmountService;
import org.jeecg.modules.app.entity.user.AppUser;
import org.springframework.stereotype.Service;


@Service
public class UserAmountServiceImpl extends ServiceImpl<UserAmountMapper, UserAmount> implements IUserAmountService {

    @Override
    public UserAmount queryByUserId(String userId) {
        QueryWrapper<UserAmount> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserAmount::getUserId, userId);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public boolean createUserAmount(AppUser appUser) {
        if (ObjectUtil.isEmpty(appUser) || StrUtil.isEmpty(appUser.getId())) {
            return false;
        }

        UserAmount userAmount = this.queryByUserId(appUser.getId());
        if (ObjectUtil.isNotEmpty(userAmount)) {
            return true;
        }

        userAmount = new UserAmount();
        userAmount.setGoldCoinAmount(0);
        userAmount.setFairyStoneAmount(0);
        userAmount.setUserId(appUser.getId());
        userAmount.setIsLock(0);

        if (userAmount.getIsLock() == 1) {
            userAmount.setIsLock(1);
            userAmount.setLockTime(DateUtil.date());
        }

        return this.save(userAmount);
    }

}
