package com.hjucook.recommend.model.entity.buried;

/**
 * 停留时间
 *
 * @author zhengjian
 * @date 2018-12-12 15:53
 */
public class TargetDuration {
    private Integer targetId;
    private Integer duration;

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
