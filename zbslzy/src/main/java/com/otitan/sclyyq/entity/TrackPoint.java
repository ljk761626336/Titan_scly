package com.otitan.sclyyq.entity;

public class TrackPoint {
    //服务器数据库id
    private String id = "";
    //经度
    private Double Longitude = 0.0;
    //纬度
    private Double Latitude = 0.0;
    //上传时间
    private String UpTime = "";
    //用户ID
    private String UserId = "";
    //点类型 0-起点，1-过程点，2-终点
    private int PointType = 1;
    //设备号
    private String sbh = "";
    //坐标系统编码
    private int Wkid = 3857;
    //备注
    private String remark = "";
    //备用字段
    private String remark1 = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public String getUpTime() {
        return UpTime;
    }

    public void setUpTime(String upTime) {
        UpTime = upTime;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getPointType() {
        return PointType;
    }

    public void setPointType(int pointType) {
        PointType = pointType;
    }

    public String getSbh() {
        return sbh;
    }

    public void setSbh(String sbh) {
        this.sbh = sbh;
    }

    public int getWkid() {
        return Wkid;
    }

    public void setWkid(int wkid) {
        Wkid = wkid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }
}
