package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.entity.user.AppUser;

import java.util.List;


public interface IAppUserService extends IService<AppUser> {

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    AppUser getUserInfoById(String userId);

    /**
     * 更新用户头像
     *
     * @param userId 用户ID
     * @param avatar 头像
     * @return 是否更新成功
     */
    boolean updateAvatar(String userId, String avatar);

    /**
     * 修改密码
     *
     * @param appUser 用户信息
     * @return Object
     */
    Boolean changePassword(AppUser appUser);



    AppUser getUserInfoByAccount(String account);


    List<AppUser> findUserListByUsername(String username);

    AppUser findUserByUsername(String username);

    boolean updateUserInfo(AppUser appUser);
}
