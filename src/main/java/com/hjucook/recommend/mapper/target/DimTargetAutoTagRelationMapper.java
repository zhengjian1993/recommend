package com.hjucook.recommend.mapper.target;

import com.hjucook.recommend.model.entity.target.DimTargetAutoTagRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zheng
 */
@Repository
public interface DimTargetAutoTagRelationMapper {
    /**
     * 根据文体获取
     * @param targetId
     * @param targetType
     * @return
     */
    List<DimTargetAutoTagRelation> listByTarget(@Param("targetId") Integer targetId,
                                                @Param("targetType") String targetType);

    /**
     * 根据标签id获取
     * @param tagName
     * @param targetType
     * @return
     */
    List<DimTargetAutoTagRelation> listByTag(@Param("tagName") String tagName,
                                         @Param("targetType") String targetType);

    /**
     * 随机查询
     * @param num
     * @return
     */
    List<DimTargetAutoTagRelation> randomArticle(@Param("num") Integer num);

}