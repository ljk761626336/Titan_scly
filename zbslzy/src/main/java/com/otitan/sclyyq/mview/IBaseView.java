package com.otitan.sclyyq.mview;

import android.view.View;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

import java.util.Map;

/**
 * Created by li on 2017/5/9.
 * base基类接口
 */

public interface IBaseView {

    Callout getCallout();

    double getCurrentLon();

    double getCurrenLat();

    Point getCurrentPoint();

    GraphicsLayer getGraphicLayer();

    void removeGraphicLayer();

    void addGraphicLayer();

    SpatialReference getSpatialReference();

    View getGpsCaijiInclude();

    MapView getMapView();

    Envelope getCurrentEnvelope();

}
