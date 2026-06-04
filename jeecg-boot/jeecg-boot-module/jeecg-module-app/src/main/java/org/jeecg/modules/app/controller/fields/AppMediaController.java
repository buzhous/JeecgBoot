package org.jeecg.modules.app.controller.fields;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.common.util.oss.OssBootUtil;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.exception.AppException;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.service.IAppMediaService;
import org.jeecg.modules.app.utils.AppAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "媒体管理")
@Slf4j
@RestController
@RequestMapping("/app/media")
public class AppMediaController {

    @Autowired
    private IAppMediaService iAppMediaService;


    // 前台上传按队列，单个逐步上传获取文件地址
    @Operation(summary = "多媒体上传-单个")
    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile multipartFile) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            throw new AppException(ExceptionEnum.USER_INFO_NOT_EXIST);
        }
        Result<?> result = new Result<>();
        try {
            String url = OssBootUtil.upload(multipartFile, "upload/oss");
            if (oConvertUtils.isEmpty(url)) {
                throw new JeecgBootException("上传文件失败! ");
            }
            // 返回阿里云原生域名前缀URL
            String urlOss = OssBootUtil.getOriginalUrl(url);


            return Result.OK();
        } catch (Exception ex) {
            log.info(ex.getMessage(), ex);
            result.error500("上传失败");
        }
        return result;
    }


}
