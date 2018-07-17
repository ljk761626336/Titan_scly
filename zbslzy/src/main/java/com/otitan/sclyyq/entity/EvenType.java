package com.otitan.sclyyq.entity;

import java.io.Serializable;

/**
 * Created by otitan_li on 2018/6/28.
 * EvenType
 */

public class EvenType implements Serializable{

    private static final long serialVersionUID = 4967543107984964486L;

    public String getSJ_DL() {
        return SJ_DL;
    }

    public void setSJ_DL(String SJ_DL) {
        this.SJ_DL = SJ_DL;
    }

    public String getSJ_XL() {
        return SJ_XL;
    }

    public void setSJ_XL(String SJ_XL) {
        this.SJ_XL = SJ_XL;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    private String SJ_DL;
    private String SJ_XL;
    private String REMARK;
}
