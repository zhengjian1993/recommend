package com.hjucook.recommend.model.dto.target;

import java.io.Serializable;

/**
 * 简单文体类
 *
 * @author zhengjian
 * @date 2018-12-13 15:07
 */
public class TargetModel implements Serializable{
    private static final long serialVersionUID = 3899859396137461869L;
    private String key;
    private Double weight;

    public TargetModel(String key, Double weight) {
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
        return "TargetModel{" +
                "key='" + key + '\'' +
                ", weight=" + weight +
                '}';
    }
}
