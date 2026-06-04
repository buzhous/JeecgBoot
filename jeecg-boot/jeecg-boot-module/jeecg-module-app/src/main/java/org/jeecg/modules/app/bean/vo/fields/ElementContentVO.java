package org.jeecg.modules.app.bean.vo.fields;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElementContentVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private String id;

    @Schema(description = "文本")
    private String text;

    @Schema(description = "富文本")
    private String rich;

    @Schema(description = "路径")
    private String path;

    @Schema(description = "地址")
    private String url;

    @Schema(description = "等级")
    private String level;

    @Schema(description = "颜色")
    private String color;

    @Schema(description = "日期")
    private String date;

    @Schema(description = "价格")
    private Double price;

    @Schema(description = "标签组")
    private List<ElementTagVO> tags;

    @Schema(description = "属性组")
    private List<ElementAttributeVO> attributes;

    @Schema(description = "多媒体组")
    private List<ElementMediaVO> medias;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "短名")
    private String shortName;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "经度")
    private String latitude;

    @Schema(description = "维度")
    private String longitude;

    @Schema(description = "值")
    private String value;

    @Schema(description = "排序")
    private Integer sort;

}