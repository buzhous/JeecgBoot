package org.jeecg.modules.app.controller.gift;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.exception.AppException;
import org.jeecg.modules.app.bean.vo.gift.ItemGiftListVO;
import org.jeecg.modules.app.bean.vo.gift.ItemGiftReqVO;
import org.jeecg.modules.app.bean.vo.gift.ItemGiftRspVO;
import org.jeecg.modules.app.bean.vo.gift.ReceiveGiftReqVO;
import org.jeecg.modules.app.bean.vo.login.UserInfoVO;
import org.jeecg.modules.app.entity.ItemGift;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.service.IAppUserService;
import org.jeecg.modules.app.service.IItemGiftService;
import org.jeecg.modules.app.utils.AppAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Tag(name = "物品赠送")
@RestController
@RequestMapping("/app/gift")
public class ItemGiftController {

    @Autowired
    private IItemGiftService itemGiftService;

    @Autowired
    private IAppUserService userService;

    @Operation(summary = "查找用户")
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

    @Operation(summary = "创建赠送")
    @PostMapping(value = "/create")
    public Result<ItemGiftRspVO> createGift(@RequestBody ItemGiftReqVO reqVO) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        ItemGiftRspVO result = itemGiftService.createGift(reqVO, loginUser.getId());
        return Result.OK(result);
    }

    @Operation(summary = "领取赠送")
    @PostMapping(value = "/receive")
    public Result<Boolean> receiveGift(@RequestBody ReceiveGiftReqVO reqVO) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        boolean result = itemGiftService.receiveGift(reqVO, loginUser.getId());
        return Result.OK(result);
    }

    @Operation(summary = "撤回赠送")
    @PostMapping(value = "/cancel")
    public Result<Boolean> cancelGift(@RequestParam String giftId) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        boolean result = itemGiftService.cancelGift(giftId, loginUser.getId());
        return Result.OK(result);
    }

    @Operation(summary = "赠送记录列表")
    @GetMapping(value = "/list")
    public Result<Page<ItemGiftListVO>> list(@RequestParam(defaultValue = "1") Integer pageNo,
                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                             @RequestParam(defaultValue = "1") Integer type) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        Page<ItemGift> page = new Page<>(pageNo, pageSize);
        Page<ItemGiftListVO> result = itemGiftService.queryGiftList(page, loginUser.getId(), type);
        return Result.OK(result);
    }

    @Operation(summary = "赠送详情")
    @GetMapping(value = "/detail")
    public Result<ItemGift> detail(@RequestParam String giftId) {
        ItemGift itemGift = itemGiftService.getById(giftId);
        if (ObjectUtil.isEmpty(itemGift)) {
            throw new AppException(ExceptionEnum.GIFT_NOT_EXIST);
        }
        return Result.OK(itemGift);
    }

    @Operation(summary = "通过兑换码查询赠送")
    @GetMapping(value = "/queryByCode")
    public Result<ItemGift> queryByCode(@RequestParam String giftCode) {
        ItemGift itemGift = itemGiftService.getGiftByCode(giftCode);
        if (ObjectUtil.isEmpty(itemGift)) {
            throw new AppException(ExceptionEnum.GIFT_NOT_EXIST);
        }
        return Result.OK(itemGift);
    }

}