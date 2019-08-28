/*




package com.hjucook.recommend.service.handler.target;

import com.hjucook.recommend.mapper.target.DimTargetTagRelationMapper;
import com.hjucook.recommend.model.dto.target.TagModel;
import com.hjucook.recommend.model.dto.target.TargetModel;
import com.hjucook.recommend.model.entity.target.DimTargetTagRelation;
import com.hjucook.recommend.service.util.TargetKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

*/
/**
 * 物品画像标签来自人工标签
 *
 * @author zhengjian
 * @date 2018-12-13 13:49
 *//*

@Service
public class TargetLabourPortrayalHandler extends BasePortrayalHandler{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DimTargetTagRelationMapper dimTargetTagRelationMapper;

    private static final double CONTENT_P = 0.7;
    private static final double TITLE_P = 5;

    @Override
    public List<TagModel> listTag(String targetKey) {
        return listTagFromDb(TargetKeyUtil.getTargetType(targetKey), TargetKeyUtil.getTargetId(targetKey));
    }

    @Override
    public List<TargetModel> listTarget(String tagKey, String targetType) {
        return listTargetFromDb(tagKey, targetType);
    }

    */
/**
     * 通过数据库获取问题标签并计算权重
     * @param targetType
     * @param targetId
     * @return
     *//*

    private List<TagModel> listTagFromDb(String targetType, Integer targetId) {
        List<DimTargetTagRelation> dimTargetTagRelations = dimTargetTagRelationMapper.listByTarget(targetId, targetType);
        if (dimTargetTagRelations.size() > 0) {
            List<TagModel> tagModels = dimTargetTagRelations.stream().map(v -> {
                double weight = getWeight(v.getContentHitTimes(), v.getTitleHitTimes());
                return new TagModel(v.getTagId().toString(), weight);
            }).sorted(Comparator.comparing(TagModel::getWeight).reversed()).collect(Collectors.toList());
            return normalization(tagModels);
        }
        return new ArrayList<>();
    }

    */
/**
     * 通过数据库获取标签相关文体并得到标签在问题中占有权重
     * @param tagKey
     * @return
     *//*

    private List<TargetModel> listTargetFromDb(String tagKey, String targetType) {
        List<DimTargetTagRelation> dimTargetTagRelations = dimTargetTagRelationMapper.listByTag(Integer.valueOf(tagKey), targetType);
        if (dimTargetTagRelations.size() > 0){
            return dimTargetTagRelations.stream().map(v -> {
                double weight = getWeight(v.getContentHitTimes(), v.getTitleHitTimes());
                return new TargetModel(TargetKeyUtil.createKey(v.getTargetType(), v.getTargetId()), weight);
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    */
/**
     * 归一化处理
     * (x - min) / (max - min)
     * @param tagModels
     * @return
     *//*

    private List<TagModel> normalization(List<TagModel> tagModels) {
        if (Objects.nonNull(tagModels) && tagModels.size() > 0) {
            double w = 0.0;
            for(TagModel tagModel : tagModels){
                w += tagModel.getWeight();
            }
            final double allWeight = w;
            tagModels.forEach(v -> {
                double weight = v.getWeight();
                double newWeight = weight / allWeight;
                v.setWeight(newWeight);
            });
        }
        return tagModels;
    }

    private double getWeight(Integer contentHitTimes, Integer titleHitTimes) {
        return contentHitTimes * CONTENT_P + titleHitTimes * TITLE_P;
    }
}
*/
