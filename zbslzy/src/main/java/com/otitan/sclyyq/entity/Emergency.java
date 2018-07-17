package com.otitan.sclyyq.entity;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by otitan_li on 2018/4/10.
 * 紧急事件现场信息
 */
@Entity
public class Emergency implements Serializable {

    private static final long serialVersionUID = 794029065729708714L;

    @Id(autoincrement = true)
    private long ID;
    /**
     * 经度
     */
    private String XJ_JD = "";
    /**
     * 纬度
     */
    private String XJ_WD = "";
    /**
     * 事件名称
     */
    private String XJ_SJMC = "";
    /**
     * 描述信息
     */
    private String XJ_MSXX = "";
    /**
     * 现场照片
     */
    private String XJ_ZPDZ;
    /**
     * 设备编号
     */
    private String XJ_SBBH = "";
    /**
     * 备注
     */
    private String REMARK = "";
    /**
     * 视频
     */
    private String XJ_SPDZ;
    /**
     * 音频
     */
    private String XJ_YPDZ;
    /**
     * 详细地址
     */
    private String XJ_XXDZ = "";
    /**
     * 巡检类型
     */
    private String XJ_LX;
    /**
     * 巡查路线的ID
     */
    private String XC_ID = "";
    @Generated(hash = 1244330531)
    public Emergency(long ID, String XJ_JD, String XJ_WD, String XJ_SJMC,
            String XJ_MSXX, String XJ_ZPDZ, String XJ_SBBH, String REMARK,
            String XJ_SPDZ, String XJ_YPDZ, String XJ_XXDZ, String XJ_LX,
            String XC_ID) {
        this.ID = ID;
        this.XJ_JD = XJ_JD;
        this.XJ_WD = XJ_WD;
        this.XJ_SJMC = XJ_SJMC;
        this.XJ_MSXX = XJ_MSXX;
        this.XJ_ZPDZ = XJ_ZPDZ;
        this.XJ_SBBH = XJ_SBBH;
        this.REMARK = REMARK;
        this.XJ_SPDZ = XJ_SPDZ;
        this.XJ_YPDZ = XJ_YPDZ;
        this.XJ_XXDZ = XJ_XXDZ;
        this.XJ_LX = XJ_LX;
        this.XC_ID = XC_ID;
    }
    @Generated(hash = 2133455553)
    public Emergency() {
    }
    public long getID() {
        return this.ID;
    }
    public void setID(long ID) {
        this.ID = ID;
    }
    public String getXJ_JD() {
        return this.XJ_JD;
    }
    public void setXJ_JD(String XJ_JD) {
        this.XJ_JD = XJ_JD;
    }
    public String getXJ_WD() {
        return this.XJ_WD;
    }
    public void setXJ_WD(String XJ_WD) {
        this.XJ_WD = XJ_WD;
    }
    public String getXJ_SJMC() {
        return this.XJ_SJMC;
    }
    public void setXJ_SJMC(String XJ_SJMC) {
        this.XJ_SJMC = XJ_SJMC;
    }
    public String getXJ_MSXX() {
        return this.XJ_MSXX;
    }
    public void setXJ_MSXX(String XJ_MSXX) {
        this.XJ_MSXX = XJ_MSXX;
    }
    public String getXJ_ZPDZ() {
        return this.XJ_ZPDZ;
    }
    public void setXJ_ZPDZ(String XJ_ZPDZ) {
        this.XJ_ZPDZ = XJ_ZPDZ;
    }
    public String getXJ_SBBH() {
        return this.XJ_SBBH;
    }
    public void setXJ_SBBH(String XJ_SBBH) {
        this.XJ_SBBH = XJ_SBBH;
    }
    public String getREMARK() {
        return this.REMARK;
    }
    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }
    public String getXJ_SPDZ() {
        return this.XJ_SPDZ;
    }
    public void setXJ_SPDZ(String XJ_SPDZ) {
        this.XJ_SPDZ = XJ_SPDZ;
    }
    public String getXJ_YPDZ() {
        return this.XJ_YPDZ;
    }
    public void setXJ_YPDZ(String XJ_YPDZ) {
        this.XJ_YPDZ = XJ_YPDZ;
    }
    public String getXJ_XXDZ() {
        return this.XJ_XXDZ;
    }
    public void setXJ_XXDZ(String XJ_XXDZ) {
        this.XJ_XXDZ = XJ_XXDZ;
    }
    public String getXJ_LX() {
        return this.XJ_LX;
    }
    public void setXJ_LX(String XJ_LX) {
        this.XJ_LX = XJ_LX;
    }
    public String getXC_ID() {
        return this.XC_ID;
    }
    public void setXC_ID(String XC_ID) {
        this.XC_ID = XC_ID;
    }


    //    XJ_ID	CHAR(36)	N
//    XJ_SJMC	NVARCHAR2(200)	Y			事件名称
//    XJ_SBBH	NVARCHAR2(200)	Y			设备编号
//    XJ_ZPDZ	CHAR(36)	Y			照片地址
//    XJ_MSXX	NVARCHAR2(500)	Y			描述信息
//    XJ_SCRQ	DATE	Y 日期
//    XJ_JD	NUMBER	Y			经度
//    XJ_WD	NUMBER	Y			纬度
//    REMARK	NVARCHAR2(200)	Y			备注
//    XJ_SPDZ	CHAR(36)	Y			视频地址
//    XJ_YPDZ	CHAR(36)	Y			音频地址
//    XJ_XXDZ	NVARCHAR2(200)	Y			详细地址




}
