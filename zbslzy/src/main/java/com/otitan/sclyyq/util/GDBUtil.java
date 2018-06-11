package com.otitan.sclyyq.util;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.ags.FeatureServiceInfo;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geodatabase.GeodatabaseFeatureTableEditErrors;
import com.esri.core.map.CallbackListener;
import com.esri.core.tasks.geodatabase.GenerateGeodatabaseParameters;
import com.esri.core.tasks.geodatabase.GeodatabaseStatusCallback;
import com.esri.core.tasks.geodatabase.GeodatabaseStatusInfo;
import com.esri.core.tasks.geodatabase.GeodatabaseSyncTask;
import com.esri.core.tasks.geodatabase.SyncGeodatabaseParameters;
import com.otitan.sclyyq.BaseActivity;
import com.otitan.sclyyq.MyApplication;
import com.otitan.sclyyq.R;
import com.titan.baselibrary.util.ProgressDialogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * 数据同步工具类（舍去）
 */
public class GDBUtil {
    // static String DEFAULT_FEATURE_SERVICE_URL;
    private static String DEFAULT_GDB_PATH = "/otms/";
    private static final String DEFAULT_BASEMAP_FILENAME = "/ArcGIS/samples/OfflineEditor/SanFrancisco.tpk";
    static final String DEFAULT_LAYERIDS = "0";
    static final String DEFAULT_RETURN_ATTACHMENTS = "true";
    static final String DEFAULT_SYNC_MODEL = "perLayer";
    protected static final String TAG = "GDBUtil";
    private static GeodatabaseSyncTask gdbTask;
    private static String fsUrl;
    private static String gdbFileName;
    private static String basemapFileName = Environment.getExternalStorageDirectory().getPath() + DEFAULT_BASEMAP_FILENAME;
    private static int[] layerIds = {0};
    private static String fileName = "";
    private static String yzl = "营造林数据";
    private static String ldgl = "林地数据";
    private static String lmcf = "采伐数据";

    // request and download geodatabase from the server
    public static void downloadData(final BaseActivity activity,String activityType) {
        Log.i(TAG, "downloadData");
        ToastUtil.setToast(activity, "下载数据");
        ProgressDialogUtil.startProgressDialog(activity,"下载中...");
        if(activityType.equals("yzl")){
            fileName = yzl;
            DEFAULT_GDB_PATH = DEFAULT_GDB_PATH+yzl;
            fsUrl = activity.getResources().getString(R.string.online_yzl);
        }else if(activityType.equals("ld")){
            fileName = ldgl;
            DEFAULT_GDB_PATH = DEFAULT_GDB_PATH+ldgl;
            fsUrl = activity.getResources().getString(R.string.online_ldgl);
        }else if(activityType.equals("lmcf")){
            fileName = lmcf;
            DEFAULT_GDB_PATH = DEFAULT_GDB_PATH+ldgl;
            fsUrl = activity.getResources().getString(R.string.onlin_cfsj);
        }
        final String offlineFileName = fileName + ".geodatabase";
        //fsUrl = onlineServerPath.replace("MapServer", "FeatureServer");

        try {
            ResourcesManager.getInstance(activity);
             String folder = MyApplication.resourcesManager.getRootFolderPath() + DEFAULT_GDB_PATH;
            File file = new File(folder);
            if(!file.exists()){
                boolean ff = file.mkdirs();
                Log.d("======",""+ff);
            }
            String path = MyApplication.resourcesManager.getFolderPath(DEFAULT_GDB_PATH);
            gdbFileName = path + "/" + offlineFileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        showProgress(activity, true);
        final MapView mapView = activity.mapView;

        if(isGeoDatabaseLocal()){
            ToastUtil.setToast(activity,"数据已存在无需重新下载");
        }else{
            downloadGeodatabase(activity, mapView);
        }
    }

    private static void downloadGeodatabase(final BaseActivity activity, final MapView mapView) {

        gdbTask = new GeodatabaseSyncTask(fsUrl, null);
        gdbTask.fetchFeatureServiceInfo(new CallbackListener<FeatureServiceInfo>() {

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "", e);
                ToastUtil.setToast(activity, "下载出错,数据请求被拒绝,检查网络");
                ProgressDialogUtil.stopProgressDialog(activity);
                showProgress(activity, false);
            }

            @Override
            public void onCallback(FeatureServiceInfo fsInfo) {
                boolean state = fsInfo.isSyncEnabled();
                if (state) {
                    requestGdbInOneMethod(gdbTask, activity, mapView, fsInfo);
                }else{
                    ToastUtil.setToast(activity,"服务不支持数据同步");
                    ProgressDialogUtil.stopProgressDialog(activity);
                }
            }
        });
    }

    /**
     * 'All-in-one' method.
     */
    public static void requestGdbInOneMethod(
            GeodatabaseSyncTask geodatabaseSyncTask,
            final BaseActivity activity, final MapView mapView,
            FeatureServiceInfo fsInfo) {

        GenerateGeodatabaseParameters params = new GenerateGeodatabaseParameters(
                fsInfo, mapView.getExtent(), mapView.getSpatialReference(),
                null, true);
        params.setOutSpatialRef(mapView.getSpatialReference());
        // gdb complete callback
        CallbackListener<String> gdbResponseCallback = new CallbackListener<String>() {

            @Override
            public void onCallback(String obj) {
                // update UI
                Log.i(TAG, "geodatabase is: " + obj);
                // activity.mDatabaseInitialized = true;

                showProgress(activity, false);

                // remove all the feature layers from map and add a feature
                // layer from the downloaded geodatabase
                for (Layer layer : mapView.getLayers()) {
                    if (layer instanceof ArcGISFeatureLayer)
                        mapView.removeLayer(layer);
                }
                Geodatabase geodatabase;
                try {
                    geodatabase = new Geodatabase(obj);
                    ProgressDialogUtil.stopProgressDialog(activity);
                    ToastUtil.setToast(activity, "数据下载完成");
                    for (GeodatabaseFeatureTable gdbFeatureTable : geodatabase
                            .getGeodatabaseTables()) {
                        if (gdbFeatureTable.hasGeometry())
                            mapView.addLayer(new FeatureLayer(gdbFeatureTable));
                    }
                    // activity.clearSelection();
                    // activity.onlineData = false;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "", e);
                System.out.println("111" + e.getMessage());
                ToastUtil.setToast(activity, e.getMessage());
                showProgress(activity, false);
                ProgressDialogUtil.stopProgressDialog(activity);
            }

        };

        GeodatabaseStatusCallback statusCallback = new GeodatabaseStatusCallback() {

            @Override
            public void statusUpdated(GeodatabaseStatusInfo status) {
                // showMessage(activity, status.getStatus().toString());
                String str = status.getStatus().toString();
                if (str.equals("InProgress")) {
                    ToastUtil.setToast(activity, "数据正在下载中...");
                } else if (str.equals("Completed")) {
                    ToastUtil.setToast(activity, "数据后台请求完成,请等待几分钟");
                }
            }
        };

        // single method does it all!
        geodatabaseSyncTask.generateGeodatabase(params, gdbFileName, false,
                statusCallback, gdbResponseCallback);
    }

    // upload and synchronize local geodatabase to the server
    public static void synchronize(final BaseActivity activity,String activityType) {

        ToastUtil.setToast(activity, "数据上传");
        ProgressDialogUtil.startProgressDialog(activity,"上传中...");
        if(activityType.equals("yzl")){
            fileName = yzl;
            DEFAULT_GDB_PATH = DEFAULT_GDB_PATH+yzl;
            fsUrl = activity.getResources().getString(R.string.online_yzl);
        }else if(activityType.equals("ld")){
            fileName = ldgl;
            DEFAULT_GDB_PATH = DEFAULT_GDB_PATH+ldgl;
            fsUrl = activity.getResources().getString(R.string.online_ldgl);
        }else if(activityType.equals("lmcf")){
            fileName = lmcf;
            DEFAULT_GDB_PATH = DEFAULT_GDB_PATH+lmcf;
            fsUrl = activity.getResources().getString(R.string.onlin_cfsj);
        }

        try {
            final String offlineFileName = fileName + ".geodatabase";
            DEFAULT_GDB_PATH = DEFAULT_GDB_PATH +"/"+ offlineFileName;
            gdbFileName = MyApplication.resourcesManager.getFolderPath(DEFAULT_GDB_PATH);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if(!isGeoDatabaseLocal()){
            ToastUtil.setToast(activity,"同步数据不存在,请检查数据文件");
            return;
        }

        showProgress(activity, true);
        gdbTask = new GeodatabaseSyncTask(fsUrl, null);
        gdbTask.fetchFeatureServiceInfo(new CallbackListener<FeatureServiceInfo>() {

            @Override
            public void onError(Throwable e) {
                ProgressDialogUtil.stopProgressDialog(activity);
                ToastUtil.setToast(activity, "出现错误" + e.getMessage());
                showProgress(activity, false);
            }

            @Override
            public void onCallback(FeatureServiceInfo objs) {
                if (objs.isSyncEnabled()) {
                    doSyncAllInOne(activity);
                }
            }
        });
    }

    /**
     * All-in-one method used...
     *
     * @throws Exception
     */
    private static void doSyncAllInOne(final BaseActivity activity) {

        try {
            // create local geodatabase
            Geodatabase gdb = new Geodatabase(gdbFileName);

            // get sync parameters from geodatabase
            final SyncGeodatabaseParameters syncParams = gdb.getSyncParameters();

            CallbackListener<Map<Integer, GeodatabaseFeatureTableEditErrors>> syncResponseCallback = new CallbackListener<Map<Integer, GeodatabaseFeatureTableEditErrors>>() {

                @Override
                public void onCallback(
                        Map<Integer, GeodatabaseFeatureTableEditErrors> objs) {
                    showProgress(activity, false);
                    if (objs != null) {
                        if (objs.size() > 0) {
                            ToastUtil.setToast(activity, "上传过程中出现错误");
                        } else {
                            ToastUtil.setToast(activity, "数据上传成功");
                        }

                    } else {
                        ToastUtil.setToast(activity, "数据上传成功");
                    }
                    ProgressDialogUtil.stopProgressDialog(activity);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "", e);
                    ToastUtil.setToast(activity, "上传过程中出现错误");
                    showProgress(activity, false);
                    ProgressDialogUtil.stopProgressDialog(activity);
                }

            };
            GeodatabaseStatusCallback statusCallback = new GeodatabaseStatusCallback() {

                @Override
                public void statusUpdated(GeodatabaseStatusInfo status) {
                    String str = status.getStatus().toString();
                    if (str.equals("InProgress")) {
                        ToastUtil.setToast(activity, "数据正在上传中...");
                    } else if (str.equals("Completed")) {
                        ToastUtil.setToast(activity, "数据上传完成");
                    }
                    // showMessage(activity, status.getStatus().toString());
                }
            };

            // start sync...
            gdbTask.syncGeodatabase(syncParams, gdb, statusCallback,syncResponseCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getGdbPath() {
        return gdbFileName;
    }

    public static void setGdbPath(String gdbPath) {
        GDBUtil.gdbFileName = gdbPath;
    }

    public static boolean isBasemapLocal() {
        File file = new File(basemapFileName);
        return file.exists();
    }

    public static boolean isGeoDatabaseLocal() {
        File file = new File(gdbFileName);
        return file.exists();
    }

    public static void showProgress(final BaseActivity activity, final boolean b) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                activity.setProgressBarIndeterminateVisibility(b);
            }
        });
    }

    static void showMessage(final BaseActivity activity, final String message) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String getFsUrl() {
        return fsUrl;
    }

    public static String getBasemapFileName() {
        return basemapFileName;
    }

    public static int[] getLayerIds() {
        return GDBUtil.layerIds;
    }

}
