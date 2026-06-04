package org.jeecg.modules.app.controller.queue;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.app.bean.enums.ExceptionEnum;
import org.jeecg.modules.app.bean.enums.SyncOpsEnum;
import org.jeecg.modules.app.bean.enums.SyncStatusEnum;
import org.jeecg.modules.app.bean.vo.queue.*;
import org.jeecg.modules.app.entity.ItemSync;
import org.jeecg.modules.app.entity.user.AppUser;
import org.jeecg.modules.app.service.IItemSyncService;
import org.jeecg.modules.app.utils.AppAuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Tag(name = "物品同步")
@RestController
@RequestMapping("/app/queue/itemSync")
public class ItemSyncController extends JeecgController<ItemSync, IItemSyncService> {

    @Autowired
    private IItemSyncService itemSyncService;

    @Operation(summary = "同步队列列表-全量")
    @GetMapping(value = "/listAll")
    public Result<IPage<ItemSyncListVO>> listAll(HttpServletRequest req) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error(ExceptionEnum.USER_INFO_NOT_EXIST.getMsg());
        }
        // TODO 安全检测
        // ...
        QueryWrapper<ItemSync> queryWrapper = QueryGenerator.initQueryWrapper(new ItemSync(), req.getParameterMap());
        Page<ItemSync> page = new Page<>(1, 99999);
        // 查询未同步列表
        queryWrapper.lambda().eq(ItemSync::getUserId, loginUser.getId());
        queryWrapper.lambda().in(ItemSync::getSyncStatus,
                SyncStatusEnum.UNSYNCED.getCode(), SyncStatusEnum.SUCCESS.getCode());
        IPage<ItemSync> pageList = itemSyncService.page(page, queryWrapper);

        IPage<ItemSyncListVO> pageListVO = new Page<>(1, 99999);
        List<ItemSyncListVO> listVO = BeanUtil.copyToList(pageList.getRecords(), ItemSyncListVO.class);
        pageListVO.setRecords(listVO);
        pageListVO.setTotal(pageList.getTotal());
        pageListVO.setCurrent(pageList.getCurrent());
        pageListVO.setSize(pageList.getSize());

        return Result.OK(pageListVO);
    }

    @Operation(summary = "同步队列列表")
    @GetMapping(value = "/list")
    public Result<IPage<ItemSyncListVO>> queryPageList(HttpServletRequest req) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error(ExceptionEnum.USER_INFO_NOT_EXIST.getMsg());
        }

        QueryWrapper<ItemSync> queryWrapper = QueryGenerator.initQueryWrapper(new ItemSync(), req.getParameterMap());
        Page<ItemSync> page = new Page<>(1, 99999);
        // 查询未同步列表
        queryWrapper.lambda().eq(ItemSync::getUserId, loginUser.getId());
        queryWrapper.lambda().eq(ItemSync::getSyncStatus, SyncStatusEnum.UNSYNCED.getCode());
        IPage<ItemSync> pageList = itemSyncService.page(page, queryWrapper);

        IPage<ItemSyncListVO> pageListVO = new Page<>(1, 99999);
        List<ItemSyncListVO> listVO = BeanUtil.copyToList(pageList.getRecords(), ItemSyncListVO.class);
        pageListVO.setRecords(listVO);
        pageListVO.setTotal(pageList.getTotal());
        pageListVO.setCurrent(pageList.getCurrent());
        pageListVO.setSize(pageList.getSize());

        return Result.OK(pageListVO);
    }

    @Operation(summary = "生成同步ID")
    @PostMapping("/makeSyncId")
    public Result<?> makeSyncId(@RequestBody MakeSyncIdReqVO syncReqVO) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error(ExceptionEnum.USER_INFO_NOT_EXIST.getMsg());
        }
        syncReqVO.setUserId(loginUser.getId());
        ItemSync itemSync = itemSyncService.makeSyncId(syncReqVO);
        return Result.OK(BeanUtil.copyProperties(itemSync, MakeSyncIdRspVO.class));
    }

    @Operation(summary = "同步上传")
    @PostMapping("/upload")
    public Result<ItemSyncRspVO> syncUpload(@RequestBody ItemSyncReqVO syncReqVO) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error(ExceptionEnum.USER_INFO_NOT_EXIST.getMsg());
        }
        if (StrUtil.isEmpty(syncReqVO.getSyncOps())) {
            return Result.error(ExceptionEnum.REQUEST_PARAM_ERROR.getMsg());
        }
        // 设置用户ID
        syncReqVO.setUserId(loginUser.getId());
        boolean result = false;
        switch (SyncOpsEnum.valueOf(syncReqVO.getSyncOps())) {
            case ADD:
                result = itemSyncService.syncUploadAdd(syncReqVO);
                if (!result) {
                    return Result.error("同步新增失败！");
                }
                break;
            case EDIT:
                result = itemSyncService.syncUploadEdit(syncReqVO);
                if (!result) {
                    return Result.error("同步修改失败！");
                }
                break;
            case DESTROY:
                result = itemSyncService.syncDestroy(syncReqVO);
                if (!result) {
                    return Result.error("同步销毁失败！");
                }
                break;
            default:
                return Result.error("同步上传失败！");
        }
        ItemSyncRspVO rspVO = new ItemSyncRspVO();
        rspVO.setOriId(syncReqVO.getOriId());
        rspVO.setItemId(syncReqVO.getItemId());
        rspVO.setSyncStatus(syncReqVO.getSyncStatus());
        rspVO.setQueueId(syncReqVO.getQueueId());
        rspVO.setVersion(syncReqVO.getVersion());
        return Result.OK(rspVO);
    }

    @Operation(summary = "同步销毁")
    @PostMapping("/destroy")
    public Result<ItemSyncRspVO> syncDestroy(@RequestBody ItemSyncReqVO syncReqVO) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error(ExceptionEnum.USER_INFO_NOT_EXIST.getMsg());
        }
        if (StrUtil.isEmpty(syncReqVO.getItemId()) || StrUtil.isEmpty(syncReqVO.getOriId())) {
            return Result.error(ExceptionEnum.REQUEST_PARAM_ERROR.getMsg());
        }
        // 设置用户ID
        syncReqVO.setUserId(loginUser.getId());
        boolean result = itemSyncService.syncDestroy(syncReqVO);
        if (!result) {
            return Result.error("同步销毁失败！");
        }
        ItemSyncRspVO rspVO = new ItemSyncRspVO();
        rspVO.setOriId(syncReqVO.getOriId());
        rspVO.setItemId(syncReqVO.getItemId());
        rspVO.setSyncStatus(syncReqVO.getSyncStatus());
        rspVO.setQueueId(syncReqVO.getQueueId());
        rspVO.setVersion(syncReqVO.getVersion());
        return Result.OK(rspVO);
    }

    @Operation(summary = "同步下载")
    @PostMapping("/download")
    public Result<ItemSyncDownloadRspVO> syncDownload(@RequestBody ItemSyncDownloadReqVO syncVO) {
        AppUser loginUser = AppAuthUtil.getUserInfo();
        if (ObjectUtil.isEmpty(loginUser)) {
            return Result.error(ExceptionEnum.USER_INFO_NOT_EXIST.getMsg());
        }
        syncVO.setUserId(loginUser.getId());
        ItemSyncDownloadRspVO downloadRspVO = itemSyncService.syncDownload(syncVO);
        return Result.OK(downloadRspVO);
    }

}
