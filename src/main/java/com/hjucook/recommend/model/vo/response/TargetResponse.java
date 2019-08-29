package com.hjucook.recommend.model.vo.response;

import java.io.Serializable;

/**
 * 返回
 *
 * @author zhengjian
 * @date 2019-01-09 16:30
 */
public class TargetResponse implements Serializable{
    private static final long serialVersionUID = 4682847987943289961L;
    private Integer targetId;
    private Double weight;

    public TargetResponse(Integer targetId) {
        this.targetId = targetId;
        this.weight = 0.0;
    }

    public TargetResponse(Integer targetId, Double weight) {
        this.targetId = targetId;
        this.weight = weight;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
