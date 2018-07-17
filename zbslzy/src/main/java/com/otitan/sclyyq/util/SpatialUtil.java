package com.otitan.sclyyq.util;

import com.esri.core.geometry.SpatialReference;

/**
 * Created by otitan_li on 2018/5/10.
 * SpatialUtil 投影转换工具
 */

public class SpatialUtil {

    public static SpatialReference getDefaultSpatialReference(){
        return SpatialReference.create(3857);
    }

    public static SpatialReference getSpatialWgs4326(){
        return SpatialReference.create(4326);
    }

    public static SpatialReference getSpatialWgs3857(){
        return SpatialReference.create(3857);
    }

    public static SpatialReference getSpatialWgs2343(){
        return SpatialReference.create(2343);
    }


}
