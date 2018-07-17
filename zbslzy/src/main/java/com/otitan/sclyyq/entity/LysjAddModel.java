package com.otitan.sclyyq.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LysjAddModel implements Serializable {
    //服务器数据库id
    private String XJ_ID = "";
    //事件名称
    private String XJ_SJMC="";
    //设备编号
    private String XJ_SBBH="";
    //发生地点
    private String XJ_ZPDZ = "";
    //上报时间
    private String addTime = "";
    //事件描述
    private String addContent = "";
    //上报人员
    private String addPeople = "";
    //上报者手机号
    private String tel = "";
    //经度
    private Double lon = 0.0;
    //纬度
    private Double lat = 0.0;
    //用户id
    private String userId = "";
    //备注
    private String addRemark = "";
    //附件
    private List<Attachment> attachmentList = new ArrayList<>();

}
