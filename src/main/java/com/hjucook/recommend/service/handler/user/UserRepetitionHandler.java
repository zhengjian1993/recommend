package com.hjucook.recommend.service.handler.user;

import com.hjucook.recommend.common.enumeration.TimeScoreEnum;
import com.hjucook.recommend.service.util.TargetKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户去重处理
 *
 * @author zhengjian
 * @date 2018-12-28 15:57
 */
@Service
public class UserRepetitionHandler {
    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    /**
     * 获取已推荐list
     * @param userId 用户id
     * @return 已推荐targetKey集合
     */
    public Set<String> getAlreadyTargets(Integer userId) {
        Set<String> alreadyTargets = redisTemplate.opsForSet().members(TargetKeyUtil.getAlreadyRecommendListRedisKey(userId));
        if (Objects.nonNull(alreadyTargets)) {
            return alreadyTargets;
        }
        return new HashSet<>();
    }

    /**
     * 添加去重
     * @param targets
     * @param userId
     */
    public void pushAllAlreadyTargets(List<String> targets, Integer userId) {
        if (targets.size() > 0) {
            String key = TargetKeyUtil.getAlreadyRecommendListRedisKey(userId);
            String[] array = targets.toArray(new String[1]);
            redisTemplate.opsForSet().add(key, array);
            redisTemplate.expire(key, TimeScoreEnum.WEEK.getCardinalMillis(), TimeUnit.MILLISECONDS);
        }
    }


    /**
     * 删除已推荐的list存储
     * @param userId 用户id
     */
    public void deleteAlreadyTargets(Integer userId) {
        redisTemplate.delete(TargetKeyUtil.getAlreadyRecommendListRedisKey(userId));
    }
}
