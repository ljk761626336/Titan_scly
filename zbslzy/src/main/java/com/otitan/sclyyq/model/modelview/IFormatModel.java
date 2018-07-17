package com.otitan.sclyyq.model.modelview;

import java.util.Date;

/**
 * Created by otitan_li on 2018/6/11.
 * IFormatModel
 */

public interface IFormatModel {

    /*格式化double 类型数据 保留小数点为6位*/
    String decimalFormat(double value);

    /*格式化double 类型数据 保留小数点为3位*/
    String decimalFormatThree(double value);

    /*格式化double 类型数据 保留小数点为2位*/
    String decimalFormatTwo(double value);

    /*格式化时间 格式为"yyyy-MM-dd HH:mm"*/
    String dateFormat(Date date);

    /*格式化时间 格式为"yyyyMMddHHmmss"*/
    String dateFormatTwo(Date date);

    /*格式化时间 格式为"yyyy-MM-dd HH:mm:ss"*/
    String dateFormatThree(Date date);

    /*小数格式经纬度转为度分秒格式*/
    String decimalTodegree(double value);
    /**/
    Date parseFormat(String value);

}
