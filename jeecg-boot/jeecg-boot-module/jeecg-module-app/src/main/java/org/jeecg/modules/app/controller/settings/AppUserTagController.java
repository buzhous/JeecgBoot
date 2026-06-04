package org.jeecg.modules.app.controller.settings;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.vo.tag.UserTagVO;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.service.IUserTagService;
import org.jeecg.modules.app.utils.AppAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户标签")
@Slf4j
@RestController
@RequestMapping("/app/user/tag")
public class AppUserTagController {

    @Autowired
    private IUserTagService userTagService;

    @Operation(summary = "查询用户标签列表")
    @GetMapping("/list")
    public Result<?> list(
            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "999") Integer pageSize) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error(ExceptionEnum.USER_INFO_NOT_EXIST.getMsg());
        }
        IPage<UserTagVO> pageList = userTagService.queryTags(loginUser.getId());
        return Result.OK(pageList);
    }

    @Operation(summary = "添加用户标签")
    @PostMapping("/add")
    public Result<?> add(@RequestBody UserTagVO addVO) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error(ExceptionEnum.USER_INFO_NOT_EXIST.getMsg());
        }
        addVO.setId(null);
        addVO.setUserId(loginUser.getId());
        boolean result = userTagService.addUserTag(addVO);
        if (!result) {
            return Result.error(ExceptionEnum.DATA_UPDATE_ERROR.getMsg());
        }
        return Result.OK("添加成功", addVO.getId());
    }

    @Operation(summary = "编辑用户标签")
    @PostMapping("/edit")
    public Result<?> edit(@RequestBody UserTagVO addVO) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error(ExceptionEnum.USER_INFO_NOT_EXIST.getMsg());
        }
        if (ObjectUtil.isEmpty(addVO.getId())) {
            return Result.error(ExceptionEnum.REQUEST_PARAM_ERROR.getMsg());
        }
        addVO.setUserId(loginUser.getId());
        boolean result = userTagService.updateUserTag(addVO);
        if (!result) {
            return Result.error(ExceptionEnum.DATA_UPDATE_ERROR.getMsg());
        }
        return Result.OK("编辑成功", addVO.getId());
    }

    @Operation(summary = "删除用户标签")
    @PostMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error(ExceptionEnum.USER_INFO_NOT_EXIST.getMsg());
        }
        boolean result = userTagService.deleteUserTag(id, loginUser.getId());
        if (!result) {
            return Result.error(ExceptionEnum.DATA_DELETE_ERROR.getMsg());
        }
        return Result.OK("删除成功", result);
    }

}
