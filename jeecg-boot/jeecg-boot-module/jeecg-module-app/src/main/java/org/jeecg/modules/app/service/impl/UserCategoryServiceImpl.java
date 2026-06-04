package org.jeecg.modules.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.bean.vo.category.UserCategoryVO;
import org.jeecg.modules.app.entity.setting.UserCategory;
import org.jeecg.modules.app.mapper.UserCategoryMapper;
import org.jeecg.modules.app.service.IUserCategoryService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
public class UserCategoryServiceImpl extends ServiceImpl<UserCategoryMapper, UserCategory> implements IUserCategoryService {


    @Override
    public IPage<UserCategoryVO> queryCategories(String userId) {
        LambdaQueryWrapper<UserCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCategory::getUserId, userId);
        Page<UserCategory> page = new Page<UserCategory>(1, 999);
        IPage<UserCategory> pageList = this.page(page, queryWrapper);

        IPage<UserCategoryVO> voPage = new Page<>(page.getPages(), page.getSize());
        // 转换为VO列表
        voPage.setRecords(pageList.getRecords().stream().map(userCategory -> {
            UserCategoryVO vo = new UserCategoryVO();
            BeanUtil.copyProperties(userCategory, vo);
            vo.setUserId(null);
            return vo;
        }).collect(Collectors.toList()));

        return voPage;
    }

    @Override
    public boolean addUserCategory(UserCategoryVO add) {
        UserCategory userCategory = new UserCategory();
        BeanUtil.copyProperties(add, userCategory);
        userCategory.setCreateTime(DateUtil.date());
        userCategory.setUpdateTime(DateUtil.date());

        LambdaQueryWrapper<UserCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCategory::getUserId, userCategory.getUserId());
        queryWrapper.eq(UserCategory::getName, userCategory.getName());
        queryWrapper.last("limit 1");

        UserCategory existingCategory = this.getOne(queryWrapper);
        if (existingCategory != null) {
            add.setId(existingCategory.getId());
            return true;
        }
        boolean result = this.save(userCategory);
        if (!result) {
            return false;
        }
        add.setId(userCategory.getId());
        return true;
    }

    @Override
    public boolean updateUserCategory(UserCategoryVO update) {
        if (ObjectUtil.isEmpty(update.getId()) || ObjectUtil.isEmpty(update.getUserId())) {
            return false;
        }
        LambdaQueryWrapper<UserCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCategory::getUserId, update.getUserId());
        queryWrapper.eq(UserCategory::getId, update.getId());
        queryWrapper.last("limit 1");
        UserCategory existingCategory = this.getOne(queryWrapper);
        if (ObjectUtil.isEmpty(existingCategory)) {
            return false;
        }
        UserCategory userCategory = new UserCategory();
        BeanUtil.copyProperties(update, userCategory);
        userCategory.setUpdateTime(DateUtil.date());
        return this.updateById(userCategory);
    }

    @Override
    public boolean deleteUserCategory(String id, String userId) {
        LambdaQueryWrapper<UserCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCategory::getId, id);
        queryWrapper.eq(UserCategory::getUserId, userId);
        return this.remove(queryWrapper);
    }

}
