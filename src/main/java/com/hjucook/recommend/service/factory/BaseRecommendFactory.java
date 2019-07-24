package com.hjucook.recommend.service.factory;

import com.hjucook.recommend.common.config.CustomRedisTemplate;
import com.hjucook.recommend.common.enumeration.TimeScoreEnum;
import com.hjucook.recommend.model.vo.response.TargetResponse;
import com.hjucook.recommend.service.handler.user.UserRepetitionHandler;
import com.hjucook.recommend.service.util.TargetKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 推荐工厂
 *
 * @author zhengjian
 * @date 2018-12-25 11:15
 */
public abstract class BaseRecommendFactory {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected CustomRedisTemplate<String, String> customRedisTemplate;
    @Autowired
    protected RedisTemplate<String, String> redisTemplate;
    @Autowired
    protected UserRepetitionHandler userRepetitionHandler;

    /**
     * 枷锁
     * @param lockKey
     * @return
     */
    boolean lock(String lockKey) {
        Boolean isUnLock = redisTemplate.opsForValue().setIfAbsent(lockKey, String.valueOf(System.currentTimeMillis()));
        if (Objects.isNull(isUnLock) || !isUnLock){
            return false;
        }
        redisTemplate.expire(lockKey, TimeScoreEnum.HOUR.getCardinalMillis(), TimeUnit.MILLISECONDS);
        return true;
    }

    List<TargetResponse> toTargetResponses(List<String> targets) {
        if (Objects.nonNull(targets) && targets.size() > 0){
            return targets.stream()
                    .map(TargetKeyUtil::getTargetResponse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 解锁
     * @param lockKey
     */
    void unlock(String lockKey) {
        redisTemplate.delete(lockKey);
    }


    /**
     * 推荐
     * @param userId
     * @param recommendNum
     * @return
     */
    public abstract List<TargetResponse> recommend(Integer userId, Integer recommendNum);

}
