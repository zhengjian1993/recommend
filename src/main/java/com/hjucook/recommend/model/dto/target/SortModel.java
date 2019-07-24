package com.hjucook.recommend.model.dto.target;

/**
 * ${DESCRIPTION}
 *
 * @author zhengjian
 * @date 2018-12-13 16:10
 */
public class SortModel {
    private String key;
    private Double weight;

    public SortModel(String key, Double weight) {
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
        return "SortModel{" +
                "key='" + key + '\'' +
                ", weight=" + weight +
                '}';
    }
}
