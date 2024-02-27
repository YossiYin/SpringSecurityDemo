package com.yen.util;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * 特点：
 * 1.全程只使用StringRedisTemplate进行操作
 * 2.对象的序列化与反序列化只使用Jackson
 * 3.支持泛型无需强转
 *
 * @author Yhx
 * @date 2024/2/14 22:31
 */
@Component
public class RedisUtil {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * jackson的转换器
     */
    private final static ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 通过key获取对象
     * 如果查不到值则返回null
     *
     * @param key       钥匙
     * @param valueType 值类型
     * @return {@link T}
     * @throws JsonProcessingException json处理异常
     */
    public <T> T get(String key, Class<T> valueType) throws JsonProcessingException {
        String json = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isEmpty(json)){
          return null;
        }
        // 将字符串转为对象
        return objectMapper.readValue(json, valueType);
    }

    /**
     * 存入Redis
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间/持续时间（默认1天）
     * @param unit    单位（默认分钟）
     * @throws JsonProcessingException json处理异常
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) throws JsonProcessingException {
        // 将对象转为json字符串
        String json = objectMapper.writeValueAsString(value);
        stringRedisTemplate.opsForValue().set(key,json,timeout,unit);
    }
    public void set(String key, Object value, long timeout) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(value);
        stringRedisTemplate.opsForValue().set(key,json,timeout,TimeUnit.MINUTES);
    }
    public void set(String key, Object value) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(value);
        stringRedisTemplate.opsForValue().set(key,json,1,TimeUnit.DAYS);
    }

    /**
     * 通过key删除
     *
     * @param key 键
     */
    public boolean remove(String key) {
        return  stringRedisTemplate.delete(key);
    }
}
