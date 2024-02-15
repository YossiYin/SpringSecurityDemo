package com.yen.config.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 配置登录用JWT相关信息
 *
 * @author Yhx
 * @date 2024/2/14 18:27
 */
@Configuration
public class JWTConfig {

    /**
     * jwt密钥/签名
     * 会使用此密钥生成和解密jwt
     * 服务端才有的密钥，用于校验jwt的合法性
     * 默认使用 HS256(HmacSHA256)算法
     */
    @Getter
    public static String KEY;

    /**
     * 过期时间（持续多少*秒*后过期）
     */
    @Getter
    public static Long EXPIRE_TIME;

    @Value("${jwt.key}")
    public void setKEY(String KEY) {
        JWTConfig.KEY = KEY;
    }

    @Value("${jwt.expire-time}")
    public void setExpireTime(Long expireTime) {
        EXPIRE_TIME = expireTime;
    }
}
