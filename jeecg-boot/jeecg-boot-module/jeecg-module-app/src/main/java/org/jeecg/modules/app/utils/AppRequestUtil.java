package org.jeecg.modules.app.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class AppRequestUtil {

    public static String getDeviceId(HttpServletRequest request) {
        return request.getHeader("x-device-id");
    }


}
