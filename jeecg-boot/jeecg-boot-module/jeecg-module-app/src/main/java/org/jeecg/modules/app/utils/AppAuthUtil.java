package org.jeecg.modules.app.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.jeecg.modules.app.entity.user.AppUser;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
public class AppAuthUtil {

    public static String getUserId() {
        HttpServletRequest request2 = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        return request2.getHeader("x-uid");
    }

    public static AppUser getUserInfo() {
        String uid = AppAuthUtil.getUserId();
        AppUser appUser = new AppUser();
        appUser.setId(uid);
        return appUser;
    }

}
