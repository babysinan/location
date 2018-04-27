package com.huang.entity;


import java.io.Serializable;
import java.util.List;

/**
 * location
 * @author 
 */
public class Location   implements Serializable {
    private Integer id;

    /**
     * code
     */
    private String code;

    private String parentCode;

    /**
     * 名称
     */
    private String name;

    private String fullName;

    private Double lat;

    private Double lng;

    private String lang;

    /**
     * 数据状态 用于记录启用，禁用或者删除
     */
    private Integer dataStatus;

    private Double createTime;

    private List<Location> childs;
    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Double getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Double createTime) {
        this.createTime = createTime;
    }

    public List<Location> getChilds() {
        return childs;
    }

    public void setChilds(List<Location> childs) {
        this.childs = childs;
    }
}