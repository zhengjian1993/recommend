package com.hjucook.recommend.service.util;

import com.hjucook.recommend.common.enumeration.TimeScoreEnum;
import com.hjucook.recommend.model.dto.target.SortModel;
import com.hjucook.recommend.model.entity.buried.TargetClick;
import com.hjucook.recommend.model.entity.buried.TargetDuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 计算热度
 *
 * @author zhengjian
 * @date 2018-12-25 13:50
 */
public class ScoreUtil {
    /**
     * 规定时间内产生几次行为刷新用户推荐池。
     */
    private static final int HIT_TIMES = 3;

    private static final String COLLECT_KEYWORD = "collect";
    private static final String SHARE_KEYWORD = "share";
    private static final String PRAISE_KEYWORD = "praise";
    private static final String CLICK_KEYWORD = "click";

    /**
     * 根据记录算分
     * 并没有去除停留时间短的或者没有停留时间的点击
     * @param targetClicks 点击记录
     * @param targetDurations 停留时间集合
     * @param timeScoreEnum 时间周期
     * @return 分数map
     */
    public static Map<String, SortModel> getScoreMap(List<TargetClick> targetClicks, List<TargetDuration> targetDurations, TimeScoreEnum timeScoreEnum) {
        Map<String, Integer> targetDurationMap = new HashMap<>(128);
        if (Objects.nonNull(targetDurations) && targetDurations.size() > 0) {
            targetDurations.forEach(v -> targetDurationMap.put(v.getTargetId().toString(), v.getDuration()));
        }
        //文体id - 文体分数对象
        Map<String, SortModel> targetScoreMap = new HashMap<>(128);
        if (targetClicks.size() > 0) {
            targetClicks.forEach(v -> {
                String key = v.getTargetId().toString();
                double weight = calculateScore(v, timeScoreEnum, targetDurationMap.get(key));
                MapUtil.injectMap(targetScoreMap, key, weight);
            });
        }
        return targetScoreMap;
    }

    /**
     * 验证24小时内有没有限定的记录产生
     * @param targetClicks
     * @return
     */
    public static boolean checkClicksAllow(List<TargetClick> targetClicks) {
        boolean allow = false;
        int i = 0;
        long now = System.currentTimeMillis();
        for (TargetClick targetClick : targetClicks) {
            if (now - targetClick.getGmtCreate().getTime() > TimeScoreEnum.HOUR.getCardinalMillis() || i > HIT_TIMES) {
                break;
            } else {
                i++;
            }
        }
        if (i >= HIT_TIMES) {
            allow = true;
        }
        return allow;
    }

    /**
     * 计算文体得分
     * @param targetClick 点击事件
     * @param timeScoreEnum 时间分母
     * @param duration 停留的时间（毫秒）
     * @return 分数
     */
    private static double calculateScore(TargetClick targetClick, TimeScoreEnum timeScoreEnum, Integer duration) {
        Long millis = targetClick.getGmtCreate().getTime();
        String name = targetClick.getName();
        int k = 1;
        if (name.contains(COLLECT_KEYWORD) || name.contains(SHARE_KEYWORD) || name.contains(PRAISE_KEYWORD)) {
            k = 2;
        }
        long now = System.currentTimeMillis();
        long diff = now - millis;
        double initDuration = Objects.isNull(duration) || duration <= 0 ? 1 : Math.pow((double) duration/1000, timeScoreEnum.getDurationExponent());
        double cardinal = (double) diff / timeScoreEnum.getCardinalMillis();
        return k * initDuration / Math.pow(cardinal, timeScoreEnum.getTimeExponent());
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }

}
