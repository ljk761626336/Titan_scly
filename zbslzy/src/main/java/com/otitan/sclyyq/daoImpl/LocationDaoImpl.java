package com.otitan.sclyyq.daoImpl;

import android.content.Context;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.otitan.sclyyq.dao.ILocationDao;
import com.otitan.sclyyq.BaseActivity;

/**
 * Created by li on 2017/3/14.
 *
 * 定位工具类
 */

public class LocationDaoImpl implements ILocationDao {

    @Override
    public void initLocation(Context context, LocationClient client, BaseActivity.MyLocationListenner listenner) {
        client = new LocationClient(context);
        client.registerLocationListener(listenner);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 设置定位模式
        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
        option.setOpenGps(true);
        // 打开gps
        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
        option.setCoorType("bd09ll"); // 设置坐标类型bd09ll gcj02
        option.setScanSpan(1000);
        client.setLocOption(option);
        client.start();
    }
}
