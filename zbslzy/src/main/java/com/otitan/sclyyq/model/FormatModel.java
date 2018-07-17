package com.otitan.sclyyq.model;

import android.annotation.SuppressLint;


import com.otitan.sclyyq.model.modelview.IFormatModel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by otitan_li on 2018/6/11.
 * FormatModel 时间格式 数字格式化
 */

public class FormatModel implements IFormatModel {
    @Override
    public String decimalFormat(double value) {
        DecimalFormat format = new DecimalFormat("0.000000");
        return format.format(value);
    }

    @Override
    public String decimalFormatThree(double value) {
        DecimalFormat format = new DecimalFormat("0.000");
        return format.format(value);
    }

    @Override
    public String decimalFormatTwo(double value) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(value);
    }

    @Override
    public String dateFormat(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    @Override
    public String dateFormatTwo(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }

    @Override
    public String dateFormatThree(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    @Override
    public String decimalTodegree(double value) {
        int du = (int) Math.floor(value);
        double fff = (value-du)*60;
        int fen =(int) Math.floor(fff);
        String miao = decimalFormatThree((fff - fen)*60);
        return du+"°"+fen+"'"+miao+"\"";
    }

    @Override
    public Date parseFormat(String value) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return format.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }
}
