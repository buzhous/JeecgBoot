package org.jeecg.modules.app.controller.square;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.exception.AppException;
import org.jeecg.modules.app.bean.vo.square.ItemTopicVO;
import org.jeecg.modules.app.service.IAppSquareService;
import org.jeecg.modules.app.utils.AppAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "主题广场")
@Slf4j
@RestController
@RequestMapping("/app/square")
public class AppSquareController {

    @Autowired
    private IAppSquareService iAppSquareService;


    @Operation(summary = "主题列表")
    @GetMapping("/topics")
    public Result<IPage<ItemTopicVO>> topics(
            @RequestParam(required = false) String searchKey,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        return Result.OK(iAppSquareService.getTopicPage(searchKey, page, size));
    }


}