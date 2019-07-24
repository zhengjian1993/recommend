package com.hjucook.recommend.service.impl;

import com.hjucook.recommend.model.entity.buried.TargetClick;
import com.hjucook.recommend.model.vo.response.RecommendResponse;
import com.hjucook.recommend.model.vo.response.TargetResponse;
import com.hjucook.recommend.service.RecommendService;
import com.hjucook.recommend.service.factory.BaseRecommendFactory;
import com.hjucook.recommend.service.factory.TagRecommendFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 推荐服务
 *
 * @author zhengjian
 * @date 2018-12-12 15:08
 */
@Service
public class RecommendServiceImpl implements RecommendService{
    @Autowired
    private TagRecommendFactory tagRecommendFactory;
    @Autowired
    private BaseRecommendFactory rankingRecommendFactory;
    @Autowired
    private BaseRecommendFactory otherRecommendFactory;
    @Autowired
    private BaseRecommendFactory synergyRecommendFactory;

    @Override
    public RecommendResponse articleRecommend(Integer userId, Integer num) {
        int articleNum = 2 * num / 3;
        RecommendResponse recommendResponse = new RecommendResponse();
        List<TargetResponse> tagTargets = tagRecommendFactory.recommend(userId, articleNum);
        recommendResponse.addTagTargets(tagTargets);
        int rankingNum = num - tagTargets.size();
        List<TargetResponse> rankingTargets = rankingRecommendFactory.recommend(userId, rankingNum);
        recommendResponse.addRankingTargets(rankingTargets);
        if (rankingTargets.size() == 0) {
            List<TargetResponse> otherTargets = otherRecommendFactory.recommend(userId, rankingNum);
            recommendResponse.addOtherTargets(otherTargets);
        }
        return recommendResponse;
    }

    @Override
    public List<TargetResponse> articleRecommend(List<TargetClick> targetClicks) {
        return tagRecommendFactory.recommend(targetClicks);
    }

}
