package org.jeecg.modules.app.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import org.jeecg.modules.app.constant.AuthConstant;

import java.util.Map;

public class SecurityTokenUtil {

    /**
     * 要解析 jwt 令牌的签名秘钥
     */
    static String JWT_SECRET = "ea2becdf7a5c5c381c1afb1535c168135c0a7f19";
    /**
     * 过期时间，默认12个月
     */
    static Long EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30 * 12;
    /**
     * 过期时间，单位秒，默认12个月
     */
    public static Long EXPIRE_TIME_SECOND = 60 * 60 * 24 * 30 * 12L;

    public static Long expireTime() {
        return System.currentTimeMillis() + EXPIRE_TIME;
    }

    public static Long expireTime(Long time) {
        return time + EXPIRE_TIME;
    }

    public static String createJwtToken(Map<String, Object> map) {
        // 一年有效期
        //long expireTime = System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30 * 12;
        // map.put("uid", "c381c1afb");
        // map.put("imei", "c381c1afb");
        //map.put("expire_time", expireTime);
        return JWTUtil.createToken(map, JWT_SECRET.getBytes());
    }

    public static JSONObject parseJwtToken(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            // ================================================================================

            System.out.println(jwt.getPayload().getClaim(AuthConstant.JWT_UID_HEADER));

            if (jwt.getPayload().getClaim(AuthConstant.JWT_UID_HEADER) == null
                    || jwt.getPayload().getClaim("imei") == null
                    || jwt.getPayload().getClaim("expireTime") == null) {
                return null;
            }
            // ================================================================================
            String expireTimeStr = jwt.getPayload().getClaim("expireTime").toString();
            if (expireTimeStr == null) {
                return null;
            }
            if (NumberUtil.isNumber(expireTimeStr)) {
                expireTimeStr = expireTimeStr + "000";
                expireTimeStr = DateUtil.date(Long.parseLong(expireTimeStr)).toString();
            }
            long currentTime = System.currentTimeMillis();
            long expireTime = DateUtil.parseDateTime(expireTimeStr).getTime();
            if (currentTime >= expireTime) {
                return null;
            }
            // ================================================================================
            // 判断imei和uid格式
            // ...
            // ================================================================================
            return jwt.getPayload().getClaimsJson();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {

//        Map<String, Object> map = new HashMap<>();
//        String userId = "b1535c168135c0a7f19";
//        map.put("uid", userId);
//        map.put("imei", "5c381c1afb1535c168135c0a7");
//        String token = LoginUtil.createJwtToken(map);
//        Object jwt = LoginUtil.parseJwtToken(token);
//        System.out.println(jwt);

        String number = RandomUtil.randomNumbers(6);
        System.out.println(number);

    }

}
