package com.otitan.sclyyq.mview;

import android.content.SharedPreferences;
import android.view.View;

import com.esri.android.map.MapView;
import com.esri.android.map.TiledLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.otitan.sclyyq.entity.MyLayer;
import com.otitan.sclyyq.entity.Row;

import java.util.HashMap;
import java.util.List;

/**
 * Created by li on 2017/5/5.
 *
 * 图层控制接口
 */

public interface ILayerControlView extends IBaseView{

    View getParChildView();

    TiledLayer getTitleLayer();

    TiledLayer getDxtLayer();

    TiledLayer getImgLayer();

    void addImageLayer(String path);

    String getImgLayerPath();

    MapView getMapView();

    List<MyLayer> getLayerNameList();

    View getImgeLayerView();

    View getTckzView();

    HashMap<String, Boolean> getLayerCheckBox();

    List<Row> getSysLayerData();

    List<String> getLayerKeyList();

    SharedPreferences getSharedPreferences();
}
