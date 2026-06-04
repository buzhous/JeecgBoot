package org.jeecg.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.app.bean.vo.queue.ItemSyncDownloadReqVO;
import org.jeecg.modules.app.bean.vo.queue.ItemSyncDownloadRspVO;
import org.jeecg.modules.app.bean.vo.queue.ItemSyncReqVO;
import org.jeecg.modules.app.bean.vo.queue.MakeSyncIdReqVO;
import org.jeecg.modules.app.entity.ItemSync;
import org.springframework.transaction.annotation.Transactional;


public interface IItemSyncService extends IService<ItemSync> {

    /**
     * 根据itemId查询物品同步记录
     *
     * @param itemId 物品ID
     * @return 物品同步记录
     */
    ItemSync getItemSyncByItemId(String itemId);

    /**
     * 根据oriId查询物品同步记录
     *
     * @param oriId 原始物品ID
     * @param queueId 队列ID
     * @return 物品同步记录
     */
    ItemSync getItemSyncByOriId(String oriId, String queueId);

    /**
     * 根据oriId+userId查询物品同步记录
     *
     * @param oriId 原始物品ID
     * @param userId 用户ID
     * @return 物品同步记录
     */
    ItemSync getItemSyncByOriIdAndUserId(String oriId, String userId);

    /**
     * 检测线上物品ID是否存在
     *
     * @param syncVO 物品同步记录VO
     * @return 物品同步记录
     */
    ItemSync queryItemSync(MakeSyncIdReqVO syncVO);

    /**
     * 创建物品同步记录ID
     *
     * @param syncVO 物品同步记录VO
     * @return 物品同步记录
     */
    ItemSync makeSyncId(MakeSyncIdReqVO syncVO);

    /**
     * 物品新增上传
     */
    @Transactional(rollbackFor = Exception.class)
    boolean syncUploadAdd(ItemSyncReqVO syncReqVO);

    /**
     * 物品修改上传
     */
    @Transactional(rollbackFor = Exception.class)
    boolean syncUploadEdit(ItemSyncReqVO syncReqVO);

    /**
     * 物品销毁
     *
     * @param syncVO 物品同步记录VO
     * @return 是否销毁成功
     */
    boolean syncDestroy(ItemSyncReqVO syncVO);

    /**
     * 根据itemId+oriId+userId查询物品同步记录
     *
     * @param itemId 物品ID
     * @param oriId 原始物品ID
     * @param userId 用户ID
     * @return 物品同步记录
     */
    ItemSync queryItemSyncByItemId(String itemId, String oriId, String userId);

    /**
     * 获取下载物品数据
     */
    ItemSyncDownloadRspVO syncDownload(ItemSyncDownloadReqVO syncVO);

    /**
     * 从销毁物品记录同步物品
     */
    boolean syncFromRuins(ItemSyncReqVO syncVO);

    /**
     * 批量同步上传
     *
     * @param batchVO 批量同步请求
     * @return 批量同步响应
     */
    org.jeecg.modules.app.bean.vo.queue.BatchItemSyncRspVO batchSyncUpload(org.jeecg.modules.app.bean.vo.queue.BatchItemSyncReqVO batchVO);

}
