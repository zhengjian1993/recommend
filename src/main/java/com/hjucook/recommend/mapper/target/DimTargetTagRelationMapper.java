package com.hjucook.recommend.mapper.target;

import com.hjucook.recommend.model.entity.target.DimTargetTagRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zheng
 */
@Repository
public interface DimTargetTagRelationMapper {
    /**
     * 根据文体获取
     * @param targetId
     * @param targetType
     * @return
     */
    List<DimTargetTagRelation> listByTarget(@Param("targetId") Integer targetId,
                                            @Param("targetType") String targetType);

    /**
     * 根据标签id获取
     * @param tagId
     * @param targetType
     * @return
     */
    List<DimTargetTagRelation> listByTag(@Param("tagId") Integer tagId,
                                         @Param("targetType") String targetType);

    /**
     * 随机查询
     * @param num
     * @return
     */
    List<DimTargetTagRelation> randomArticle(@Param("num") Integer num);

}