package org.jeecg.modules.app.bean.vo.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 物品信息VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "物品信息响应对象")
public class ItemInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "物品ID", example = "ITEM789")
    private String id;

    @Schema(description = "图标", example = "phone-icon.png")
    private String itemIcon;

    @Schema(description = "物品编码", example = "ITEM001")
    private String itemCode;

    @Schema(description = "物品名称", example = "智能手机")
    private String itemName;

    @Schema(description = "等级", example = "5")
    private Integer itemLevel;

    @Schema(description = "价格", example = "5999.99")
    private Double itemPrice;

    @Schema(description = "属性列表", example = "[\"防水\",\"5G\"]")
    private List<String> attribute;

    // 10 默认，20全类型
    @Schema(description = "模板类型", example = "10")
    private Integer tempType;

    @Schema(description = "物品描述", example = "这是一款高性能智能手机")
    private String description;

    @Schema(description = "特殊描述", example = "限量版")
    private String specialDesc;

    @Schema(description = "提示语", example = "请妥善保管")
    private String tips;

    @Schema(description = "定位", example = "办公室")
    private String location;

    @Schema(description = "文件列表", example = "[\"file1.jpg\",\"file2.pdf\"]")
    private List<String> fileList;

    @Schema(description = "图片列表", example = "[\"img1.jpg\",\"img2.jpg\"]")
    private List<String> imageList;

    @Schema(description = "视频列表", example = "[\"video1.mp4\",\"video2.mp4\"]")
    private List<String> videoList;
}
