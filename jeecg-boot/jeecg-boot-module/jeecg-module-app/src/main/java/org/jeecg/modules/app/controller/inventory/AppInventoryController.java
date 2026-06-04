package org.jeecg.modules.app.controller.inventory;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.vo.inventory.ItemInventoryVO;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.service.IItemUserInventoryService;
import org.jeecg.modules.app.utils.AppAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "用户物品")
@Slf4j
@RestController
@RequestMapping("/app/inventory")
public class AppInventoryController {

    @Autowired
    private IItemUserInventoryService iItemUserInventoryService;

    @Operation(summary = "用户物品列表")
    @GetMapping(value = "/list")
    public Result<Object> list(@RequestParam(required = false) Integer isSync) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error(ExceptionEnum.USER_INFO_NOT_EXIST.getMsg());
        }
        List<ItemInventoryVO> inventoryList = iItemUserInventoryService.queryUserItems(loginUser.getId());
        return Result.OK(inventoryList);
    }

}
