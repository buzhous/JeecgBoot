package org.jeecg.modules.app.bean.vo.login;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息")
public class UserInfoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private String id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "真实姓名")
    private String realname;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "生日")
    private String birthday;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "封面")
    private String thumb;

    @Schema(description = "语言")
    private String language;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "省")
    private String province;

    @Schema(description = "国家")
    private String country;

}

