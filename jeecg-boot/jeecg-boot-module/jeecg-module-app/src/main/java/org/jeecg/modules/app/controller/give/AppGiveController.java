package org.jeecg.modules.app.controller.give;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.app.entity.ItemInfo;
import org.jeecg.modules.app.entity.ItemUserInventory;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.exception.AppException;
import org.jeecg.modules.app.service.IItemInfoService;
import org.jeecg.modules.app.service.IItemUserInventoryService;
import org.jeecg.modules.app.service.IUserAmountService;
import org.jeecg.modules.app.bean.vo.item.ItemGiveVO;
import org.jeecg.modules.app.bean.vo.login.UserInfoVO;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.service.IAppUserService;
import org.jeecg.modules.app.utils.AppAuthUtil;
import org.jeecg.modules.app.service.IUserGiveRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/app/give")
public class AppGiveController {

    @Autowired
    private IAppUserService userService;

    @Autowired
    private IItemUserInventoryService iItemUserInventoryService;

    @Autowired
    private IItemInfoService iItemInfoService;

    @Autowired
    private IUserGiveRecordService iUserGiveRecordService;

    @GetMapping(value = "/findUsers")
    public Result<List<UserInfoVO>> square(@RequestParam(required = false) String username) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }

        // 查找用户
        List<AppUser> list = userService.findUserListByUsername(username);
        List<UserInfoVO> listUsers = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            for (AppUser user : list) {
                UserInfoVO userVO = JSONUtil.toBean(JSONUtil.toJsonStr(user), UserInfoVO.class);
                listUsers.add(userVO);
            }
        }

        return Result.OK(listUsers);
    }

    @PostMapping(value = "/giveUser")
    public Result<?> giveUser(@RequestBody ItemGiveVO itemGiveVO) {
        // 检查当前登录用户
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }

        // 检查赠送用户信息
        String userId = loginUser.getId();
        String receiveUserId = itemGiveVO.getReceiveUserId();
        AppUser receiveUser = userService.getUserInfoById(receiveUserId);

        String inventoryId = itemGiveVO.getInventoryId();

        if (StrUtil.isEmpty(inventoryId)) {
            return Result.error("物品不能为空");
        }
        if (ObjectUtil.isEmpty(receiveUser)) {
            return Result.error("赠送用户信息错误！");
        }
        if (receiveUserId.equals(userId)) {
            return Result.error("不能赠送给自己");
        }

        // 检查物品库存
        ItemUserInventory itemUserRel = iItemUserInventoryService.getById(inventoryId);
        if (ObjectUtil.isEmpty(itemUserRel)) {
            return Result.error("库存不存在");
        }
        ItemInfo itemInfo = iItemInfoService.queryItemInfo(itemUserRel.getItemId());
        if (ObjectUtil.isEmpty(itemInfo)) {
            return Result.error("物品不存在");
        }

        // 检查物品数量
        Integer quantity = itemGiveVO.getQuantity();
        if (ObjectUtil.isEmpty(quantity)) {
            return Result.error("物品数量不能为空");
        }
        if (quantity <= 0) {
            return Result.error("物品数量不能小于等于0");
        }
        if (quantity > itemUserRel.getQuantity()) {
            return Result.error("物品数量不能大于库存数量");
        }

        return Result.OK(iUserGiveRecordService.giveUser(loginUser, itemGiveVO));
    }

    // 领取
    @PostMapping(value = "/receive")
    public Result<?> receive(@RequestBody ItemGiveVO itemGiveVO) {
        // 检查当前登录用户
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }

        // 检查赠送用户信息
        String userId = loginUser.getId();
        String giveUserId = itemGiveVO.getGiveUserId();
        AppUser giveUser = userService.getUserInfoById(giveUserId);

        if (ObjectUtil.isEmpty(giveUser)) {
            return Result.error("赠送用户信息错误！");
        }
        if (giveUserId.equals(userId)) {
            return Result.error("不能赠送给自己");
        }

        return Result.OK(iUserGiveRecordService.giveUser(loginUser, itemGiveVO));
    }

    // 拒绝，放弃领取
    @PostMapping(value = "/reject")
    public Result<?> reject(@RequestBody ItemGiveVO itemGiveVO) {
        // 检查当前登录用户
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        // 检查赠送用户信息
        String userId = loginUser.getId();
        String giveUserId = itemGiveVO.getGiveUserId();
        AppUser giveUser = userService.getUserInfoById(giveUserId);

        if (ObjectUtil.isEmpty(giveUser)) {
            return Result.error("赠送用户信息错误！");
        }
        if (giveUserId.equals(userId)) {
            return Result.error("不能赠送给自己");
        }
        return Result.OK(iUserGiveRecordService.giveUser(loginUser, itemGiveVO));
    }

    // 拒绝领取物品取回
    @PostMapping(value = "/retrieve")
    public Result<?> retrieve(@RequestBody ItemGiveVO itemGiveVO) {
        // 检查当前登录用户
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        // 检查赠送用户信息
        String userId = loginUser.getId();
        String giveUserId = itemGiveVO.getGiveUserId();
        AppUser giveUser = userService.getUserInfoById(giveUserId);

        if (ObjectUtil.isEmpty(giveUser)) {
            return Result.error("赠送用户信息错误！");
        }
        if (giveUserId.equals(userId)) {
            return Result.error("不能赠送给自己");
        }
        return Result.OK(iUserGiveRecordService.giveUser(loginUser, itemGiveVO));
    }


}
