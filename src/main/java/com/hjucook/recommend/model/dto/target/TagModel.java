package com.hjucook.recommend.model.dto.target;

import java.io.Serializable;

/**
 * 文体标签
 *
 * @author zhengjian
 * @date 2018-12-13 13:52
 */
public class TagModel implements Serializable{
    private static final long serialVersionUID = 8007170710427071737L;
    private String key;
    private Double weight;

    public TagModel(String key, Double weight) {
        this.key = key;
        this.weight = weight;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "TagModel{" +
                "key='" + key + '\'' +
                ", weight=" + weight +
                '}';
    }
}
