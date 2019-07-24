package com.hjucook.recommend.model.entity.target;

import java.util.Date;

public class DimTargetAutoTagRelation {
    /**
     * id
     */
    private Integer id;

    /**
     * 文体id
     */
    private Integer targetId;

    /**
     * 文体类型
     */
    private String targetType;


    private String tagName;

    private Double weight;

    /**
     * 是否有效：0 无效 1有效
     */
    private Byte status;

    /**
     * 创建日期
     */
    private Date gmtCreate;

    /**
     * 更新日期
     */
    private Date gmtModified;

    /**
     * id
     * @return id id
     */
    public Integer getId() {
        return id;
    }

    /**
     * id
     * @param id id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 文体id
     * @return target_id 文体id
     */
    public Integer getTargetId() {
        return targetId;
    }

    /**
     * 文体id
     * @param targetId 文体id
     */
    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    /**
     * 文体类型
     * @return target_type 文体类型
     */
    public String getTargetType() {
        return targetType;
    }

    /**
     * 文体类型
     * @param targetType 文体类型
     */
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * 是否有效：0 无效 1有效
     * @return status 是否有效：0 无效 1有效
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 是否有效：0 无效 1有效
     * @param status 是否有效：0 无效 1有效
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 创建日期
     * @return gmt_create 创建日期
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 创建日期
     * @param gmtCreate 创建日期
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 更新日期
     * @return gmt_modified 更新日期
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 更新日期
     * @param gmtModified 更新日期
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
}