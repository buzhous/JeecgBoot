package org.jeecg.modules.app.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.app.bean.vo.tag.UserTagVO;
import org.jeecg.modules.app.entity.setting.UserTag;
import org.jeecg.modules.app.mapper.UserTagMapper;
import org.jeecg.modules.app.service.IUserTagService;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Service
public class UserTagServiceImpl extends ServiceImpl<UserTagMapper, UserTag> implements IUserTagService {


    @Override
    public IPage<UserTagVO> queryTags(String userId) {
        LambdaQueryWrapper<UserTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTag::getUserId, userId);
        Page<UserTag> page = new Page<UserTag>(1, 999);
        IPage<UserTag> pageList = this.page(page, queryWrapper);

        IPage<UserTagVO> voPage = new Page<>(page.getPages(), page.getSize());
        // 转换为VO列表
        voPage.setRecords(pageList.getRecords().stream().map(userTag -> {
            UserTagVO vo = new UserTagVO();
            BeanUtil.copyProperties(userTag, vo);
            vo.setUserId(null);
            return vo;
        }).collect(Collectors.toList()));

        return voPage;
    }

    @Override
    public boolean addUserTag(UserTagVO add) {
        UserTag userTag = new UserTag();
        BeanUtil.copyProperties(add, userTag);
        userTag.setCreateTime(DateUtil.date());
        userTag.setUpdateTime(DateUtil.date());

        LambdaQueryWrapper<UserTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTag::getUserId, userTag.getUserId());
        queryWrapper.eq(UserTag::getName, userTag.getName());
        queryWrapper.last("limit 1");

        UserTag existingTag = this.getOne(queryWrapper);
        if (existingTag != null) {
            add.setId(existingTag.getId());
            return true;
        }
        boolean result = this.save(userTag);
        if (!result) {
            return false;
        }
        add.setId(userTag.getId());
        return true;
    }

    @Override
    public boolean updateUserTag(UserTagVO update) {
        if (ObjectUtil.isEmpty(update.getId()) || ObjectUtil.isEmpty(update.getUserId())) {
            return false;
        }
        LambdaQueryWrapper<UserTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTag::getUserId, update.getUserId());
        queryWrapper.eq(UserTag::getId, update.getId());
        queryWrapper.last("limit 1");

        UserTag existingTag = this.getOne(queryWrapper);
        if (existingTag == null) {
            return false;
        }
        UserTag userTag = new UserTag();
        BeanUtil.copyProperties(update, userTag);
        userTag.setUpdateTime(DateUtil.date());
        return this.updateById(userTag);
    }

    @Override
    public boolean deleteUserTag(String id, String userId) {
        LambdaQueryWrapper<UserTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTag::getId, id);
        queryWrapper.eq(UserTag::getUserId, userId);
        return this.remove(queryWrapper);
    }

}
