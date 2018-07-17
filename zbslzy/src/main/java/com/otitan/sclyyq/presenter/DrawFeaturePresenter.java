package com.otitan.sclyyq.presenter;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.otitan.sclyyq.BaseActivity;
import com.otitan.sclyyq.R;
import com.otitan.sclyyq.entity.ActionMode;
import com.otitan.sclyyq.mview.ILayerView;
import com.otitan.sclyyq.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 小班绘制
 */
public class DrawFeaturePresenter {

    private MapView mapView;
    private ILayerView layerView;
    private BaseActivity baseActivity;
    private SimpleMarkerSymbol pointSymbol;
    private SimpleLineSymbol lineSymbol;
    private SimpleFillSymbol fillSymbol;
    private int graphicID = -1;
    //采集状态 false没开始 true采集中
    private boolean collectStatu = false;
    private Button startBtn;
    private List<Point> pointList = new ArrayList<>();

    public DrawFeaturePresenter(BaseActivity baseActivity) {
        this.mapView = baseActivity.getMapView();
        this.layerView = baseActivity;
        this.baseActivity = baseActivity;
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        startBtn = baseActivity.dotPainting.findViewById(R.id.add_start);
        if (pointSymbol == null) {
            pointSymbol = new SimpleMarkerSymbol(Color.RED, 5, SimpleMarkerSymbol.STYLE.CIRCLE);
        }
        if (lineSymbol == null) {
            lineSymbol = new SimpleLineSymbol(Color.RED, 4);
        }
        if (fillSymbol == null) {
            fillSymbol = new SimpleFillSymbol(Color.RED);
        }
    }

    /**
     * 开始/结束采集
     */
    public void start() {
        if (!collectStatu) {
            addPoint();
            startBtn.setText("结束");
            collectStatu = true;
        } else {
            end();
            startBtn.setText("开始");
            collectStatu = false;
        }
    }

    /**
     * 结束采集
     */
    private void end() {
        if (baseActivity.actionMode == ActionMode.MODE_NULL) {
            if (baseActivity.layerType == Geometry.Type.POINT) {
                if (pointList.size() == 1) {
                    baseActivity.basePresenter.addFeaturePoint(pointList.get(0));
                }
            } else if (baseActivity.layerType == Geometry.Type.POLYLINE) {
                Polyline polyline = getPolyline(pointList);
                if (checkedGeometry(polyline, Geometry.Type.POLYLINE)) {
                    baseActivity.basePresenter.addFeatureLine(polyline);
                } else {
                    ToastUtil.setToast(baseActivity, "勾绘图班无效，请重新勾绘");
                }
            } else if (baseActivity.layerType == Geometry.Type.POLYGON) {
                Polygon polygon = getPolygon(pointList);
                if (checkedGeometry(polygon, Geometry.Type.POLYGON)) {
                    baseActivity.basePresenter.addFeaturePolygon(polygon);
                } else {
                    ToastUtil.setToast(baseActivity, "勾绘图班无效，请重新勾绘");
                }
            }
        }
        statuClear();
        mapView.invalidate();
    }

    /**
     * 新增点
     */
    public void addPoint() {
        try {
            if (!collectStatu) {
                startBtn.setText("结束");
                collectStatu = true;
            }
            if (baseActivity.getLayerList().isEmpty()) {
                ToastUtil.setToast(baseActivity, "未加载空间数据,请在图层控制中加载数据");
                return;
            }
            if (mapView == null || !mapView.isLoaded()) {
                ToastUtil.setToast(baseActivity, "地图状态错误，请加载完成后重试");
                return;
            }
            Envelope env = new Envelope();
            mapView.getExtent().queryEnvelope(env);
            switch (baseActivity.layerType) {
                case POINT:
                    refreshDraw(env.getCenter());
                    break;
                case POLYLINE:
                    refreshDraw(getPolyline(pointList));
                    break;
                case POLYGON:
                    pointList.add(env.getCenter());
                    if (pointList.size() == 1) {
                        refreshDraw(env.getCenter());
                    } else if (pointList.size() == 2) {
                        refreshDraw(getPolyline(pointList));
                    } else if (pointList.size() > 2) {
                        refreshDraw(getPolygon(pointList));
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e("tag", "添加点出错：" + e);
            ToastUtil.setToast(baseActivity, "添加点出错：" + e);
        }
    }

    /**
     * 撤销
     */
    public void undo() {
        try {
            if (pointList != null && !pointList.isEmpty()) {
                pointList.remove(pointList.size() - 1);
                if (graphicID != -1) {
                    baseActivity.getGraphicLayer().removeGraphic(graphicID);
                }
                switch (pointList.size()) {
                    case 0:
                        graphicID = -1;
                        break;
                    case 1:
                        refreshDraw(pointList.get(0));
                        break;
                    case 2:
                        refreshDraw(getPolyline(pointList));
                        break;
                    default:
                        refreshDraw(getPolygon(pointList));
                        break;
                }
            } else {
                ToastUtil.setToast(baseActivity, "当前无可撤销的点");
            }
        } catch (Exception e) {
            Log.e("tag", "撤销异常:" + e);
            ToastUtil.setToast(baseActivity, "撤销异常:" + e);
        }

    }

    /**
     * 清除
     */
    public void clear() {
        if (graphicID != -1) {
            layerView.getGraphicLayer().removeGraphic(graphicID);
        }
        statuClear();
    }

    /**
     * 取消
     */
    public void cancel() {
        collectStatu = false;
        startBtn.setText("开始");
        statuClear();
        baseActivity.dotPainting.setVisibility(View.GONE);
    }

    /**
     * 状态清除
     */
    private void statuClear() {
        pointList.clear();
        graphicID = -1;
        baseActivity.clear();
    }

    /**
     * 刷新绘制图班
     */
    private void refreshDraw(Geometry geometry) {
        Graphic graphic = null;
        switch (geometry.getType()) {
            case POINT:
                graphic = new Graphic(geometry, pointSymbol);
                break;
            case POLYLINE:
                graphic = new Graphic(geometry, lineSymbol);
                break;
            case POLYGON:
                graphic = new Graphic(geometry, fillSymbol);
                break;
        }
        if (graphicID != -1) {
            layerView.getGraphicLayer().removeGraphic(graphicID);
        }
        if (graphic != null) {
            graphicID = layerView.getGraphicLayer().addGraphic(graphic);
        }
    }


    /**
     * 点集合转为polyline
     */
    private Polyline getPolyline(List<Point> pointList) {
        Polyline line = new Polyline();
        if (pointList == null || pointList.isEmpty() || pointList.size() == 1) {
            return line;
        }
        for (int i = 0; i < pointList.size(); i++) {
            if (i == 0) {
                line.startPath(pointList.get(0));
            } else {
                line.lineTo(pointList.get(i));
            }
        }
        return line;
    }

    /**
     * 点集合转为Polygon
     */
    private Polygon getPolygon(List<Point> pointList) {
        Polygon polygon = new Polygon();
        if (pointList == null || pointList.size() < 2) {
            return polygon;
        }
        for (int i = 0; i < pointList.size(); i++) {
            if (i == 0) {
                polygon.startPath(pointList.get(0));
            } else {
                polygon.lineTo(pointList.get(i));
            }
        }
        return polygon;
    }

    /**
     * 检查图班是否合法
     */
    private boolean checkedGeometry(Geometry geometry, Geometry.Type type) {
        return geometry.isValid() && geometry.getType() == type;
    }
}
