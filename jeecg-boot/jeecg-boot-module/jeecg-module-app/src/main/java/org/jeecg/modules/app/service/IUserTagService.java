package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.bean.vo.tag.UserTagVO;
import org.jeecg.modules.app.entity.setting.UserTag;


public interface IUserTagService extends IService<UserTag> {

    /**
     * 查询用户标签列表
     */
    IPage<UserTagVO> queryTags(String userId);

    /**
     * 添加用户标签列表
     */
    boolean addUserTag(UserTagVO addVO);

    boolean updateUserTag(UserTagVO update);

    boolean deleteUserTag(String id, String userId);
}
