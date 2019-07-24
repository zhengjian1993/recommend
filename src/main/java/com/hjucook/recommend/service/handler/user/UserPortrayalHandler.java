package com.hjucook.recommend.service.handler.user;

import com.hjucook.recommend.common.enumeration.TimeScoreEnum;
import com.hjucook.recommend.mapper.buried.BuriedMapper;
import com.hjucook.recommend.model.dto.target.SortModel;
import com.hjucook.recommend.model.dto.target.TagModel;
import com.hjucook.recommend.model.entity.buried.TargetClick;
import com.hjucook.recommend.model.entity.buried.TargetDuration;
import com.hjucook.recommend.service.handler.target.BasePortrayalHandler;
import com.hjucook.recommend.service.util.MapUtil;
import com.hjucook.recommend.service.util.ScoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户画像处理
 *
 * @author zhengjian
 * @date 2018-12-12 15:12
 */
@Service
public class UserPortrayalHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BuriedMapper buriedMapper;

    private static final int BURIED_LIMIT = 1000;

    /**
     * 根据用户id创造用户标签画像
     * @param userId 用户id
     * @param basePortrayalHandler 物品画像处理器
     * @return 人物标签集合
     */
    public List<TagModel> getUserPortrayal(Integer userId, BasePortrayalHandler basePortrayalHandler) {
        List<TargetClick> targetClicks = buriedMapper.listTargetClick(userId, BURIED_LIMIT);
        List<TargetDuration> targetDurations = buriedMapper.listTargetDuration(userId, BURIED_LIMIT);
        return getUserPortrayal(targetClicks, targetDurations, true, basePortrayalHandler);
    }

    /**
     * 创造用户标签画像
     * @param targetClicks 点击事件集合
     * @param targetDurations 停留时间集合
     * @param isChecksAllow 是否检查有资格构建人物画像
     * @param basePortrayalHandler 物品画像处理器
     * @return 人物标签集合
     */
    public List<TagModel> getUserPortrayal(List<TargetClick> targetClicks, List<TargetDuration> targetDurations,
                                           boolean isChecksAllow, BasePortrayalHandler basePortrayalHandler) {
        if (isChecksAllow && !ScoreUtil.checkClicksAllow(targetClicks)) {
            return new ArrayList<>();
        }

        //文体id - 文体分数对象
        Map<String, SortModel> targetScoreMap = ScoreUtil.getScoreMap(targetClicks, targetDurations, TimeScoreEnum.MINUTE);
        //标签id - 标签分数对象
        Map<String, SortModel> tagScoreMap = new HashMap<>(256);
        targetScoreMap.forEach((k,v) -> {
            List<TagModel> tagModels = basePortrayalHandler.listTag(k);
            tagModels.forEach(tag -> {
                //文体分数 * 自身每一个标签权重
                String key = tag.getKey();
                double weight = tag.getWeight() * v.getWeight();
                MapUtil.injectMap(tagScoreMap, key, weight);
            });
        });
        //tag分数按从高到低排序
        return tagScoreMap.values().stream()
                .sorted(Comparator.comparing(SortModel::getWeight).reversed())
                //去排序的前一半，后续可能会使用蓄水池采样的方法
                .limit(tagScoreMap.size() / 2)
                .map(v -> new TagModel(v.getKey(), v.getWeight()))
                .collect(Collectors.toList());
    }
}
