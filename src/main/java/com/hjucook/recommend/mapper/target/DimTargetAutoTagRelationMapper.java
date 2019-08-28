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
     * @return
     */
    List<DimTargetAutoTagRelation> listByTarget(@Param("targetId") Integer targetId);

    /**
     * 根据标签id获取
     * @param tagName
     * @return
     */
    List<DimTargetAutoTagRelation> listByTag(@Param("tagName") String tagName);

}