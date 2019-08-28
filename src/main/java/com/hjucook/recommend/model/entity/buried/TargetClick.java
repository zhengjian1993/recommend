package com.hjucook.recommend.model.entity.buried;

import java.util.Date;

/**
 * 用户点击
 *
 * @author zhengjian
 * @date 2018-12-27 16:53
 */
public class TargetClick {
    private Integer targetId;
    private String name;
    private Date gmtCreate;

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}
