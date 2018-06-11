package com.otitan.sclyyq.util;

import com.esri.core.geometry.CompositeGeographicTransformation;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by otitan_li on 2018/5/10.
 * GeometryEngineUtil 投影转换
 */

public class GeometryEngineUtil {

    public static Point getSpatialPoint(Point point){
        Point pp = (Point) GeometryEngine.project(point, SpatialUtil.getSpatialWgs4326(),SpatialUtil.getDefaultSpatialReference());
        return pp;
    }

    public static Point getSpatialPoint2343(Point point){
        Point pp = (Point) GeometryEngine.project(point, SpatialUtil.getDefaultSpatialReference(),SpatialUtil.getSpatialWgs2343());
        return pp;
    }

    public static Point getSpatialPoint4326(Point point){
        Point pp = (Point) GeometryEngine.project(point, SpatialUtil.getSpatialWgs2343(),SpatialUtil.getDefaultSpatialReference());
        return pp;
    }

    public static Polyline getSpatialLine(Polyline polyline){
        Polyline pp = (Polyline) GeometryEngine.project(polyline, SpatialUtil.getSpatialWgs4326(),SpatialUtil.getSpatialWgs3857());
        return pp;
    }

    public static Polyline getSpatialLine2343(Polyline polyline){
        Polyline pp = (Polyline) GeometryEngine.project(polyline, SpatialUtil.getDefaultSpatialReference(),SpatialUtil.getSpatialWgs2343());
        return pp;
    }

    public static Polygon getSpatialDefalutPolygon(Polygon polygon){
        Polygon pp = (Polygon) GeometryEngine.project(polygon, SpatialUtil.getSpatialWgs2343(),SpatialUtil.getDefaultSpatialReference());
        return pp;
    }

    public static Polygon getSpatialPolygon2343(Polygon polygon){
        Polygon pp = (Polygon) GeometryEngine.project(polygon, SpatialUtil.getDefaultSpatialReference(),SpatialUtil.getSpatialWgs2343());
        return pp;
    }

    public static Polygon getSpatialPolygon4326(Polygon polygon){
        Polygon pp = (Polygon) GeometryEngine.project(polygon,SpatialUtil.getSpatialWgs2343(),SpatialUtil.getSpatialWgs4326());
        return pp;
    }

    public static Geometry getSpatialGeometry(Geometry geometry){
        Geometry pp = (Geometry) GeometryEngine.project(geometry, SpatialUtil.getSpatialWgs4326(),SpatialUtil.getSpatialWgs3857());
        return pp;
    }

}
