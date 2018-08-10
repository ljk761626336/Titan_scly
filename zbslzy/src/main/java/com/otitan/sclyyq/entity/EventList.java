package com.otitan.sclyyq.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class EventList implements Serializable {
    private static final long serialVersionUID = -2504610284492104032L;
    private Long ID;
    /**
     * id
     */
    private String XJ_ID = "";
    /**
     * 设备编号
     */
    private String XJ_SBBH = "";
    /**
     * 照片地址
     */
    private List XJ_ZPDZ;
    /**
     * 视频地址
     */
    private List XJ_SPDZ;
    /**
     * 音频地址
     */
    private List XJ_YPDZ;
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
     * 备注
     */
    private String REMARK = "";
    /**
     * 详细地址
     */
    private String XJ_XXDZ = "";
    /**
     * 巡检类型
     */
    private String XJ_LX = "";
    /**
     * 巡查路线的ID
     */
    private String XC_ID = "";
    /**
     * 日期
     */
    private String XJ_SCRQ = "";
    /**
     * 总记录数
     */
    private int totalCount;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getXJ_JD() {
        return XJ_JD;
    }

    public void setXJ_JD(String XJ_JD) {
        this.XJ_JD = XJ_JD;
    }

    public String getXJ_WD() {
        return XJ_WD;
    }

    public void setXJ_WD(String XJ_WD) {
        this.XJ_WD = XJ_WD;
    }

    public String getXJ_ID() {
        return XJ_ID;
    }

    public void setXJ_ID(String XJ_ID) {
        this.XJ_ID = XJ_ID;
    }

    public String getXJ_SBBH() {
        return XJ_SBBH;
    }

    public void setXJ_SBBH(String XJ_SBBH) {
        this.XJ_SBBH = XJ_SBBH;
    }

    public List getXJ_ZPDZ() {
        return XJ_ZPDZ;
    }

    public void setXJ_ZPDZ(List XJ_ZPDZ) {
        this.XJ_ZPDZ = XJ_ZPDZ;
    }

    public List getXJ_SPDZ() {
        return XJ_SPDZ;
    }

    public void setXJ_SPDZ(List XJ_SPDZ) {
        this.XJ_SPDZ = XJ_SPDZ;
    }

    public List getXJ_YPDZ() {
        return XJ_YPDZ;
    }

    public void setXJ_YPDZ(List XJ_YPDZ) {
        this.XJ_YPDZ = XJ_YPDZ;
    }

    public String getXJ_SJMC() {
        return XJ_SJMC;
    }

    public void setXJ_SJMC(String XJ_SJMC) {
        this.XJ_SJMC = XJ_SJMC;
    }

    public String getXJ_MSXX() {
        return XJ_MSXX;
    }

    public void setXJ_MSXX(String XJ_MSXX) {
        this.XJ_MSXX = XJ_MSXX;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getXJ_XXDZ() {
        return XJ_XXDZ;
    }

    public void setXJ_XXDZ(String XJ_XXDZ) {
        this.XJ_XXDZ = XJ_XXDZ;
    }

    public String getXJ_LX() {
        return XJ_LX;
    }

    public void setXJ_LX(String XJ_LX) {
        this.XJ_LX = XJ_LX;
    }

    public String getXC_ID() {
        return XC_ID;
    }

    public void setXC_ID(String XC_ID) {
        this.XC_ID = XC_ID;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getXJ_SCRQ() {
        return XJ_SCRQ;
    }

    public void setXJ_SCRQ(String XJ_SCRQ) {
        this.XJ_SCRQ = XJ_SCRQ;
    }
}
