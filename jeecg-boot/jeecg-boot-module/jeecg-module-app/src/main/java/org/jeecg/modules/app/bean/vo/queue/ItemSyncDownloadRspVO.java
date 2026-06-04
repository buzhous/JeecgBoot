package org.jeecg.modules.app.bean.vo.queue;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.app.entity.ItemSync;
import org.jeecg.modules.app.bean.vo.fields.ElementAttributeVO;
import org.jeecg.modules.app.bean.vo.fields.ElementTagVO;
import org.jeecg.modules.app.bean.vo.fields.ExtendDataVO;
import org.jeecg.modules.app.bean.vo.fields.FieldElementVO;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "队列同步下载响应对象")
public class ItemSyncDownloadRspVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "初始ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String oriId;

    @Schema(description = "物品ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String itemId;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "物品名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "等级")
    private Integer level;

    @Schema(description = "价格")
    private Double price;

    @Schema(description = "物品描述")
    private String description;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "分类ID")
    private String categoryId;

    @Schema(description = "标签组")
    private List<ElementTagVO> tags;

    @Schema(description = "属性列表")
    private List<ElementAttributeVO> attributes;

    @Schema(description = "组件列表")
    private List<FieldElementVO> fields;

    @Schema(description = "扩展VO")
    private ExtendDataVO extendData;

    @Schema(description = "队列ID")
    private String queueId;

    @Schema(description = "版本", requiredMode = Schema.RequiredMode.REQUIRED)
    private String version;

    @Schema(description = "同步状态")
    private Integer syncStatus;

    // ===================== 自动转换：JSON字符串 → 对象（空值安全）=====================
    public void setExtendData(String extendDataJson) {
        if (extendDataJson == null || extendDataJson.isEmpty()) {
            this.extendData = null;
            return;
        }
        this.extendData = JSONUtil.toBean(extendDataJson, ExtendDataVO.class, true);
    }

    // ===================== 自动转换：JSON字符串 → List（空值安全）=====================
    public void setFields(String fieldsJson) {
        if (fieldsJson == null || fieldsJson.isEmpty()) {
            this.fields = null;
            return;
        }
        this.fields = JSONUtil.toBean(fieldsJson, new TypeReference<List<FieldElementVO>>() {

        }, true);
    }

    public void setTags(String tagsJson) {
        if (tagsJson == null || tagsJson.isEmpty()) {
            this.tags = null;
            return;
        }
        this.tags = JSONUtil.toBean(tagsJson, new TypeReference<List<ElementTagVO>>() {
        }, true);
    }

    public void setAttributes(String attributesJson) {
        if (attributesJson == null || attributesJson.isEmpty()) {
            this.attributes = null;
            return;
        }
        this.attributes = JSONUtil.toBean(attributesJson, new TypeReference<List<ElementAttributeVO>>() {
        }, true);
    }

    public static ItemSyncDownloadRspVO convertToVO(ItemSync itemSync) {
        ItemSyncDownloadRspVO rsp = BeanUtil.copyProperties(itemSync, ItemSyncDownloadRspVO.class);
        // 强制 JSON 解析
        rsp.setExtendData(itemSync.getExtendData());
        rsp.setFields(itemSync.getFields());
        rsp.setTags(itemSync.getTags());
        rsp.setAttributes(itemSync.getAttributes());
        return rsp;
    }

}