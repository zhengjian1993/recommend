package com.hjucook.recommend.service;

import com.hjucook.recommend.model.entity.buried.TargetClick;
import com.hjucook.recommend.model.vo.response.RecommendResponse;
import com.hjucook.recommend.model.vo.response.TargetResponse;

import java.util.List;

/**
 * 推荐服务
 *
 * @author zhengjian
 * @date 2018-12-12 15:08
 */
public interface RecommendService {
    /**
     * 自动获取用户记录推荐
     * @param userId
     * @param recommendNum
     * @return
     */
    RecommendResponse articleRecommend(Integer userId, Integer recommendNum);

}
