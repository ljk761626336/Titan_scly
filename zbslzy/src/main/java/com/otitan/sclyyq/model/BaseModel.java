package com.otitan.sclyyq.model;


import com.otitan.sclyyq.model.modelview.IBaseModel;

/**
 * Created by otitan_li on 2018/6/14.
 * BaseModel
 */

public class BaseModel implements IBaseModel {


    public String getXC_ID() {
        return XC_ID;
    }

    public void setXC_ID(String XC_ID) {
        this.XC_ID = XC_ID;
    }

    private String XC_ID="";


    @Override
    public void setXCID(String ID) {
        setXC_ID(ID);
    }

    @Override
    public String getXCID() {
        return getXC_ID();
    }
}
