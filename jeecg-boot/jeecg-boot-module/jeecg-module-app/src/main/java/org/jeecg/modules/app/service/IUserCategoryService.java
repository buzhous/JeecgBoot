package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.bean.vo.category.UserCategoryVO;
import org.jeecg.modules.app.entity.setting.UserCategory;


public interface IUserCategoryService extends IService<UserCategory> {

    IPage<UserCategoryVO> queryCategories(String userId);

    boolean addUserCategory(UserCategoryVO addVO);

    boolean updateUserCategory(UserCategoryVO update);

    boolean deleteUserCategory(String id, String userId);
}
