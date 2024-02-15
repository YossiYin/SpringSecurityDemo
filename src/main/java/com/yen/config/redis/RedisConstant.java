package com.yen.config.redis;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * redis常量
 * 部分字段可在配置文件中修改
 *
 * @author Yhx
 * @date 2024/2/14 22:12
 */
@Configuration
public class RedisConstant {
    /**
     * 用户信息缓存KEY
     */
    public static final String USER_CACHE_KEY = "user:cache:";
    /**
     * 用户信息缓存时间,单位分钟
     */
    @Getter
    public static Long USER_CACHE_TTL_MINUTES;

    @Value("${redis-constant.user-cache-ttl-minutes}")
    public void setUserCacheTtlMinutes(Long userCacheTtlMinutes) {
        USER_CACHE_TTL_MINUTES = userCacheTtlMinutes;
    }


}
