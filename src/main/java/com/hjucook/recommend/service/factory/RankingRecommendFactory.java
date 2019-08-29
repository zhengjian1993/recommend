package com.hjucook.recommend.service.factory;

import com.hjucook.recommend.common.enumeration.TimeScoreEnum;
import com.hjucook.recommend.mapper.buried.BuriedMapper;
import com.hjucook.recommend.model.dto.target.SortModel;
import com.hjucook.recommend.model.entity.buried.TargetClick;
import com.hjucook.recommend.model.entity.buried.TargetDuration;
import com.hjucook.recommend.model.vo.response.TargetResponse;
import com.hjucook.recommend.service.util.ScoreUtil;
import com.hjucook.recommend.service.util.TargetKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 排行榜
 *
 * @author zhengjian
 * @date 2018-12-25 9:52
 */
@Service
public class RankingRecommendFactory extends BaseRecommendFactory {
    @Autowired
    private BuriedMapper buriedMapper;

    private static final int BURIED_LIMIT = 10000;
    private static final int RANKING_TIMEOUT = 7 * 12 * 60 * 60;

    /**
     * 控制初始化排行榜加载
     */
    private static final boolean IS_POST_CONSTRUCT = true;

    private static final boolean IS_TIMER_BUILD = true;

    @PostConstruct
    private void init() {
        if (IS_POST_CONSTRUCT) {
            build();
        }
    }

    @Scheduled(cron = "0 0 4 * * ?")
    private void timer() {
        if (IS_TIMER_BUILD) {
            build();
        }
    }

    @Override
    public List<TargetResponse> recommend(Integer userId, Integer recommendNum) {
        List<String> targets = new ArrayList<>();
        int index = getNextBeginReadIndex(userId);
        int lastIndex = -1;
        List<String> originalTargets = listTargets(index, recommendNum);
        if (Objects.nonNull(originalTargets) && originalTargets.size() > 0) {
            Set<String> alreadyTargets = userRepetitionHandler.getAlreadyTargets(userId);
            int num = 0;
            int i = 0;
            for (String target : originalTargets) {
                if (!alreadyTargets.contains(target)) {
                    targets.add(target);
                    num++;
                }
                i++;
                if (num >= recommendNum) {
                    break;
                }
            }
            lastIndex = index + i - 1;
        }
        updateLastReadIndex(userId, lastIndex);
        userRepetitionHandler.pushAllAlreadyTargets(targets, userId);
        return toTargetResponses(targets);
    }


    private void build() {
        String lockKey = TargetKeyUtil.getBuildLockRedisKey(this.getClass() + "build");
        if(!lock(lockKey)) {
            logger.info("排行榜已加锁，退出");
            return;
        }
        logger.info("开始创建排行榜数据");
        List<TargetClick> targetClicks = buriedMapper.listTargetClick(null, BURIED_LIMIT);
        List<TargetDuration> targetDurations = buriedMapper.listTargetDuration(null, BURIED_LIMIT);
        Map<String, SortModel> targetScoreMap = ScoreUtil.getScoreMap(targetClicks, targetDurations, TimeScoreEnum.DAY);
        List<String> targets = targetScoreMap.values().stream()
                .sorted(Comparator.comparing(SortModel::getWeight).reversed())
                .map(SortModel::getKey)
                .collect(Collectors.toList());
        //logger.info("排行榜创建数据：" + targets.toString());
        cacheTargets(targets);
        deleteAllReadIndex();
        unlock(lockKey);
    }

    /**
     * 缓存排行榜列表
     * @param targets targetKey集合
     */
    private void cacheTargets(List<String> targets) {
        String key = TargetKeyUtil.getRankingListRedisKey();
        redisTemplate.delete(key);
        redisTemplate.opsForList().rightPushAll(key, targets);
        redisTemplate.expire(key, RANKING_TIMEOUT, TimeUnit.SECONDS);
    }

    private List<String> listTargets(Integer index, Integer num) {
        if (Objects.isNull(num) || num <= 0) {
            return new ArrayList<>();
        }
        String key = TargetKeyUtil.getRankingListRedisKey();

        return redisTemplate.opsForList().range(key, index, index + num * 2);
    }

    private int getNextBeginReadIndex(Integer userId) {
        String index = redisTemplate.opsForValue().get(TargetKeyUtil.getRankingReadIndexRedisKey(userId));
        return StringUtils.isNotBlank(index) ? Integer.valueOf(index) + 1 : 0;
    }

    private void updateLastReadIndex(Integer userId, int index) {
        String lastReadIndexKey = TargetKeyUtil.getRankingReadIndexRedisKey(userId);
        redisTemplate.opsForValue().set(lastReadIndexKey, String.valueOf(index));
        redisTemplate.expire(lastReadIndexKey, TimeScoreEnum.DAY.getCardinalMillis(), TimeUnit.MILLISECONDS);
    }

    private void deleteAllReadIndex() {
        Set<String> keys = redisTemplate.keys(TargetKeyUtil.getRankingReadIndexPrefix() + ":*");
        if (Objects.nonNull(keys)) {
            redisTemplate.delete(keys);
        }
    }
}
