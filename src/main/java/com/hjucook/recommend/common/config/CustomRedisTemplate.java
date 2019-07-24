package com.hjucook.recommend.common.config;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationUtils;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 定制的redis服務
 *
 * @author zhengjian
 * @date 2018-12-18 13:26
 */
public class CustomRedisTemplate<K, V> {

    private RedisTemplate<K, V> redisTemplate;

    public CustomRedisTemplate(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 右边重新插入集合，并设定过期时间
     * @param key
     * @param values
     * @param timeout
     * @param unit
     */
    public void rightPushAll(K key, List<V> values, final long timeout, final TimeUnit unit) {
        byte[] cacheKey = rawKey(key);
        byte[][] rawValues = rawValues(values);
        long rawTimeout = TimeoutUtils.toMillis(timeout, unit);
        redisTemplate.execute(connection -> {
            connection.del(cacheKey);
            connection.rPush(cacheKey, rawValues);
            return connection.expire(cacheKey, rawTimeout);
        }, true);
    }

    /**
     * 左边弹出并插入另一个集合
     * @param popKey
     * @param pushKey
     * @param num
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<V> leftPopAndLeftPush(K popKey, K pushKey, final int num) {
        byte[] cachePopKey = rawKey(popKey);
        byte[] cachePushKey = rawKey(pushKey);
        return redisTemplate.execute(connection -> {
            List<byte[]> result = connection.lRange(cachePopKey, 0, num - 1);
            if (null == result || result.size() == 0) {
                return null;
            }
            connection.lTrim(cachePopKey, num, -1);
            connection.sAdd(cachePushKey, rawValues(result));
            return SerializationUtils.deserialize(result, valueSerializer());
        }, true);
    }

    @SuppressWarnings("unchecked")
    private byte[] rawKey(Object key) {
        Assert.notNull(key, "non null key required");
        if (key instanceof byte[]) {
            return (byte[]) key;
        }
        return keySerializer().serialize(key);
    }

    private byte[][] rawValues(List<byte[]> values) {
        Assert.notEmpty(values, "Values must not be 'null' or empty.");
        Assert.noNullElements(values.toArray(), "Values must not contain 'null' value.");
        byte[][] rawValues = new byte[values.size()][];
        int i = 0;
        for (byte[] value : values) {
            rawValues[i++] = value;
        }
        return rawValues;
    }

    private byte[][] rawValues(Collection<V> values) {

        Assert.notEmpty(values, "Values must not be 'null' or empty.");
        Assert.noNullElements(values.toArray(), "Values must not contain 'null' value.");

        byte[][] rawValues = new byte[values.size()][];
        int i = 0;
        for (V value : values) {
            rawValues[i++] = rawValue(value);
        }

        return rawValues;
    }


    @SuppressWarnings("unchecked")
    private byte[] rawValue(Object value) {

        if (value instanceof byte[]) {
            return (byte[]) value;
        }

        return valueSerializer().serialize(value);
    }

    private RedisSerializer keySerializer() {
        return redisTemplate.getKeySerializer();
    }

    private RedisSerializer valueSerializer() {
        return redisTemplate.getValueSerializer();
    }



}
