package com.hjucook.recommend.service.factory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.hjucook.recommend.common.enumeration.TimeScoreEnum;
import com.hjucook.recommend.model.dto.target.SortModel;
import com.hjucook.recommend.model.dto.target.TagModel;
import com.hjucook.recommend.model.dto.target.TargetModel;
import com.hjucook.recommend.model.entity.buried.TargetClick;
import com.hjucook.recommend.model.vo.response.TargetResponse;
import com.hjucook.recommend.service.handler.target.BasePortrayalHandler;
import com.hjucook.recommend.service.handler.user.UserPortrayalHandler;
import com.hjucook.recommend.service.util.MapUtil;
import com.hjucook.recommend.service.util.TargetKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 标签推荐 （核心）
 *
 * @author zhengjian
 * @date 2018-12-13 14:50
 */
@Service
public class TagRecommendFactory extends BaseRecommendFactory {

    @Resource(name = "targetAutoPortrayalHandler")
    private BasePortrayalHandler basePortrayalHandler;
    @Autowired
    private UserPortrayalHandler userPortrayalHandler;

    /**
     * 推荐量小于时，进行重置去重操作
     */
    private static final int REBUILD_LOWER_NUM = 50;

    private static final int DEFAULT_RECOMMEND_NUM = 10;

    /**
     * 推荐几次后重置用户推荐池
     */
    private static final int RECOMMEND_REBUILD_NUM = 5;

    private final ExecutorService executorService = new ThreadPoolExecutor(5, 9,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(32),
            new ThreadFactoryBuilder().setNameFormat("build用户推荐列表").build());


    /**
     * 推荐
     * @param userId 用户id
     * @return targetKey集合
     */
    @Override
    public List<TargetResponse> recommend(Integer userId, Integer recommendNum) {
        if (Objects.isNull(recommendNum) || recommendNum <=0 ) {
            recommendNum = DEFAULT_RECOMMEND_NUM;
        }
        String popKey = TargetKeyUtil.getRecommendListRedisKey(userId);
        String pushKey = TargetKeyUtil.getAlreadyRecommendListRedisKey(userId);
        List<String> targets = customRedisTemplate.leftPopAndLeftPush(popKey,
                pushKey, recommendNum);
        if (Objects.nonNull(targets) && targets.size() > 0) {
            increaseNumAndRebuild(userId);
            logger.info("user-id：" + userId + "推荐一次");
            return toTargetResponses(targets);
        }
        executorService.execute(() -> build(userId));
        return new ArrayList<>();
    }

    /**
     * 根据行为记录获取推荐结果
     * @param targetClicks  行为记录
     * @return 推荐结果
     */
    public List<TargetResponse> recommend(List<TargetClick> targetClicks) {
        List<TagModel> tagModels = userPortrayalHandler.getUserPortrayal(targetClicks, null, false, basePortrayalHandler);
        List<SortModel> targets = getUserTargets(tagModels, null);
        return targets.stream()
                .limit(100)
                .map(TargetKeyUtil::getTargetResponse)
                .collect(Collectors.toList());
    }

    /**
     * 创造人物推荐池
     * @param userId 用户id
     */
    private void build(Integer userId) {
        String lockKey = TargetKeyUtil.getBuildLockRedisKey(String.valueOf(userId));
        if (!lock(lockKey)) {
            logger.info("人物推荐池已加锁，退出 user-id = " + userId);
            return;
        }

        logger.info("开始创建人物推荐池 user-id = " + userId);
        List<TagModel> tagModels = userPortrayalHandler.getUserPortrayal(userId, basePortrayalHandler);
        logger.debug("人物画像为：" + tagModels.toString());
        List<SortModel> targetModels = getUserTargets(tagModels, userId);
        if (tagModels.size() > 0) {
            List<String> targets = targetModels.stream().map(SortModel::getKey).collect(Collectors.toList());
            cacheTargets(targets, userId);
        }
        unlock(lockKey);
    }

    /**
     * 标签集合获取用户推荐结果
     * @param tagModels 标签集合
     * @param userId 用户id
     * @return 推荐结果
     */
    private List<SortModel> getUserTargets(List<TagModel> tagModels, Integer userId) {
        if (Objects.isNull(tagModels) || tagModels.size() == 0) {
            logger.info("用户创建推荐不满足条件 user-id = " + userId);
        } else {
            //文体id - 文体分数对象
            Map<String, SortModel> targetScoreMap = new HashMap<>(512);
            tagModels.forEach(tag -> {
                List<TargetModel> targetModels = basePortrayalHandler.listTarget(tag.getKey(), null);
                targetModels.forEach(target ->
                        MapUtil.injectMap(targetScoreMap, target.getKey(), tag.getWeight() * target.getWeight()));
            });
            return sortTargets(targetScoreMap, userId);
        }
        return new ArrayList<>();
    }


    /**
     * 排序去重重置
     * @param targetScoreMap key - 分数对象
     * @param userId 用户id 如果为null则不去重
     * @return 排好序的targetKey集合
     */
    private List<SortModel> sortTargets(Map<String, SortModel> targetScoreMap, Integer userId) {
        //如果不拿到本地直接redis中判断，则是以时间换空间
        boolean isWipeRepetition = Objects.nonNull(userId);
        Set<String> alreadyTargets = isWipeRepetition ? userRepetitionHandler.getAlreadyTargets(userId) : new HashSet<>();
        List<SortModel> targets = targetScoreMap.values().stream()
                .filter(v -> !alreadyTargets.contains(v.getKey()))
                .sorted(Comparator.comparing(SortModel::getWeight).reversed())
                .collect(Collectors.toList());
        if (isWipeRepetition && targets.size() < REBUILD_LOWER_NUM && alreadyTargets.size() > 0) {
            userRepetitionHandler.deleteAlreadyTargets(userId);
            return sortTargets(targetScoreMap, userId);
        }
        return targets;
    }

    /**
     * 缓存推荐列表
     * @param targets targetKey集合
     * @param userId 用户id
     */
    private void cacheTargets(List<String> targets, Integer userId) {
        if (targets.size() > 0) {
            String key = TargetKeyUtil.getRecommendListRedisKey(userId);
            customRedisTemplate.rightPushAll(key, targets, TimeScoreEnum.DAY.getCardinalMillis(), TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 累积推荐次数,到达一定次数会重新刷新推荐池
     * @param userId 用户id
     */
    private void increaseNumAndRebuild(Integer userId) {
        String key = TargetKeyUtil.getRecommendNumRedisKey(userId);
        Long num = redisTemplate.opsForValue().increment(key, 1);
        redisTemplate.expire(key, TimeScoreEnum.DAY.getCardinalMillis(), TimeUnit.MILLISECONDS);
        if (Objects.nonNull(num) && num % RECOMMEND_REBUILD_NUM == 0) {
            executorService.execute(() -> build(userId));
        }
    }

}
