package com.hjucook.recommend.model.vo.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 推荐结果
 *
 * @author zhengjian
 * @date 2018-12-27 14:11
 */
public class RecommendResponse {
    private List<TargetResponse> tagTargets;
    private List<TargetResponse> rankingTargets;
    private List<TargetResponse> otherTargets;

    public RecommendResponse() {
        this.tagTargets = new ArrayList<>();
        this.rankingTargets = new ArrayList<>();
        this.otherTargets = new ArrayList<>();
    }

    public List<TargetResponse> getTagTargets() {
        return tagTargets;
    }

    public void addTagTargets(List<TargetResponse> tagTargets) {
        if (Objects.nonNull(tagTargets) && tagTargets.size() > 0) {
            this.tagTargets.addAll(tagTargets);
        }
    }

    public List<TargetResponse> getRankingTargets() {
        return rankingTargets;
    }

    public void addRankingTargets(List<TargetResponse> rankingTargets) {
        if (Objects.nonNull(rankingTargets) && rankingTargets.size() > 0) {
            this.rankingTargets.addAll(rankingTargets);
        }
    }

    public List<TargetResponse> getOtherTargets() {
        return otherTargets;
    }

    public void addOtherTargets(List<TargetResponse> otherTargets) {
        if (Objects.nonNull(otherTargets) && otherTargets.size() > 0) {
            this.otherTargets.addAll(otherTargets);
        }
    }
}
