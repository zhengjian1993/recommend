package com.hjucook.recommend.model.entity.buried;

/**
 * 统计每天问题点击
 *
 * @author zhengjian
 * @date 2018-12-12 15:02
 */
public class TargetDayClick {
    private Integer days;
    private Integer targetId;
    private String targetType;
    private Integer sum;

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }
}
