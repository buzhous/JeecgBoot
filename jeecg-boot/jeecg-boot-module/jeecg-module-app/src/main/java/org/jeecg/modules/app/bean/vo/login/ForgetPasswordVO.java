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
@Schema(description = "忘记密码请求")
public class ForgetPasswordVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "登录类型：1账号密码，2手机号，3一键登录", example = "1")
    private Integer type;

    @Schema(description = "账号", example = "user123")
    private String username;

    @Schema(description = "密码", example = "password123")
    private String password;

    private String password2;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Schema(description = "短信验证码", example = "123456")
    private String smsCode;

    @Schema(description = "IMEI", example = "861234567890123")
    private String imei;

}
