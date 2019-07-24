package com.hjucook.recommend.service.handler.target;

import com.hjucook.recommend.mapper.target.DimTargetAutoTagRelationMapper;
import com.hjucook.recommend.model.dto.target.TargetModel;
import com.hjucook.recommend.model.dto.target.TagModel;
import com.hjucook.recommend.model.entity.target.DimTargetAutoTagRelation;
import com.hjucook.recommend.service.util.TargetKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物品画像标签来自百度引擎打标
 *
 * @author zhengjian
 * @date 2018-12-13 13:49
 */
@Service
public class TargetAutoPortrayalHandler extends BasePortrayalHandler{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DimTargetAutoTagRelationMapper dimTargetAutoTagRelationMapper;

    @Override
    public List<TagModel> listTag(String targetKey) {
        return listTagFromDb(TargetKeyUtil.getTargetType(targetKey), TargetKeyUtil.getTargetId(targetKey));
    }

    @Override
    public List<TargetModel> listTarget(String tagKey, String targetType) {
        return listTargetFromDb(tagKey, targetType);
    }

    /**
     * 通过数据库获取问题标签并计算权重
     * @param targetType
     * @param targetId
     * @return
     */
    private List<TagModel> listTagFromDb(String targetType, Integer targetId) {
        List<DimTargetAutoTagRelation> dimTargetAutoTagRelations = dimTargetAutoTagRelationMapper.listByTarget(targetId, targetType);
        if (dimTargetAutoTagRelations.size() > 0) {
            return dimTargetAutoTagRelations.stream()
                    .map(v -> new TagModel(v.getTagName(), v.getWeight()))
                    .sorted(Comparator.comparing(TagModel::getWeight).reversed()).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 通过数据库获取标签相关文体并得到标签在问题中占有权重
     * @param tagKey
     * @return
     */
    private List<TargetModel> listTargetFromDb(String tagKey, String targetType) {
        List<DimTargetAutoTagRelation> dimTargetAutoTagRelations = dimTargetAutoTagRelationMapper.listByTag(tagKey, targetType);
        if (dimTargetAutoTagRelations.size() > 0){
            return dimTargetAutoTagRelations.stream()
                    .map(v -> new TargetModel(TargetKeyUtil.createKey(v.getTargetType(), v.getTargetId()), v.getWeight()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


}
