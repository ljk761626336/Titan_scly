package com.otitan.sclyyq.mview;
import com.esri.android.map.TiledLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.otitan.sclyyq.entity.MyLayer;

import java.util.List;

/**
 * Created by li on 2017/6/1.
 * 加载图层接口
 */

public interface ILayerView extends IBaseView {

    //获取电子底图
    TiledLayer getBaseTitleLayer();
    //获取影像底图
    TiledLayer getImgTitleLayer();
    //获取地形图底图
    TiledLayer getDxtTitleLayer();
    //获取加载的小班图层列表
    List<MyLayer> getLayerList();


}
