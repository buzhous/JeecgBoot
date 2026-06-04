package org.jeecg.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;


@Data
@TableName("tbl_item_user_inventory")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(name = "用户物品库存", description = "用户物品库存")
public class ItemUserInventory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id")
    private String id;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private java.util.Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "修改时间")
    private java.util.Date updateTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "获取时间")
    private java.util.Date getTime;

    @Schema(description = "物品Id")
    private String itemId;

    @Schema(description = "用户Id")
    private String userId;

    @Schema(description = "库存数量")
    private Integer quantity;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "是否丢弃：0否，1是")
    private Integer isDrop;

    @Schema(description = "是否同步")
    private Integer isSync;

    @TableField(exist = false)
    private ItemInfo itemInfo;

}