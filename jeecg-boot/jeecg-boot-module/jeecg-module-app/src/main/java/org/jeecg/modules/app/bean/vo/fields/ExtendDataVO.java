package org.jeecg.modules.app.bean.vo.fields;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 物品同步上传扩展VO
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(description = "物品同步上传扩展VO")
public class ExtendDataVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;




}

