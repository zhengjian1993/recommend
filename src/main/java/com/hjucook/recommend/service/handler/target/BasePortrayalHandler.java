package com.hjucook.recommend.service.handler.target;

import com.hjucook.recommend.model.dto.target.TagModel;
import com.hjucook.recommend.model.dto.target.TargetModel;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author zhengjian
 * @date 2019-01-09 15:38
 */
public abstract class BasePortrayalHandler {
    /**
     * 根据文体key获取标签集合
     * @param targetKey 问题key
     * @return
     */
    public abstract List<TagModel> listTag(String targetKey);

    /**
     * 根据标签key获取文体集合
     * @param tagKey 标签key
     * @param targetType 文体类型，如果为null，则获取所有
     * @return
     */
    public abstract List<TargetModel> listTarget(String tagKey, String targetType);
}
