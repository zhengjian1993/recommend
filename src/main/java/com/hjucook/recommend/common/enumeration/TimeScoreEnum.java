package com.hjucook.recommend.common.enumeration;

/**
 * 时间分数枚举
 *
 * @author zhengjian
 * @date 2018-12-28 15:13
 */
public enum  TimeScoreEnum {
    /**
     * 分钟
     */
    MINUTE(60 * 1000, 0.2, 0.3),
    HOUR(60 * 60 * 1000, 0.3, 0.4),
    DAY(24 * 60 * 60 * 1000, 0.75, 0.5),
    WEEK(7 * 24 * 60 * 60 * 1000, 0.75, 0.5),
    ;
    private Integer cardinalMillis;
    private Double timeExponent;
    private Double durationExponent;

    TimeScoreEnum(Integer cardinalMillis, Double timeExponent, Double durationExponent) {
        this.cardinalMillis = cardinalMillis;
        this.timeExponent = timeExponent;
        this.durationExponent = durationExponent;
    }

    public Integer getCardinalMillis() {
        return cardinalMillis;
    }

    public void setCardinalMillis(Integer cardinalMillis) {
        this.cardinalMillis = cardinalMillis;
    }

    public Double getTimeExponent() {
        return timeExponent;
    }

    public void setTimeExponent(Double timeExponent) {
        this.timeExponent = timeExponent;
    }

    public Double getDurationExponent() {
        return durationExponent;
    }

    public void setDurationExponent(Double durationExponent) {
        this.durationExponent = durationExponent;
    }
}
