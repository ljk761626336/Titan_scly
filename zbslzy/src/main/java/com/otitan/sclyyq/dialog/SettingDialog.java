package com.otitan.sclyyq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.otitan.sclyyq.presenter.GpsCollectPresenter;
import com.otitan.sclyyq.BaseActivity;
import com.otitan.sclyyq.MyApplication;
import com.otitan.sclyyq.R;
import com.otitan.sclyyq.mview.IBaseView;
import com.otitan.sclyyq.util.BussUtil;
import com.otitan.sclyyq.util.ToastUtil;
import com.titan.baselibrary.listener.CancleListener;
import com.titan.versionupdata.VersionUpdata;

/**
 * Created by li on 2017/5/31.
 * 系统设置dialog
 */

public class SettingDialog extends Dialog {

    private Context mContext;
    private View gpsCaijiInclude;
    private IBaseView iBaseView;
    private GpsCollectPresenter gpsCollectPresenter;
    private BaseActivity baseActivity;

    public SettingDialog(@NonNull Context context, @StyleRes int themeResId, IBaseView baseView,
                         GpsCollectPresenter gpsCollectPresenter, BaseActivity baseActivity) {
        super(context, themeResId);
        this.mContext = context;
        this.iBaseView = baseView;
        this.gpsCaijiInclude = iBaseView.getGpsCaijiInclude();
        this.gpsCollectPresenter = gpsCollectPresenter;
        this.baseActivity = baseActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settings);
        setCanceledOnTouchOutside(false);

        CheckBox xsxjlx = (CheckBox) findViewById(R.id.xsxjlx);
        xsxjlx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean check) {
                gpsCollectPresenter.setTravel(check);
                MyApplication.sharedPreferences.edit().putBoolean("zongji", check).apply();
            }
        });

        TextView gpsset = (TextView) findViewById(R.id.gpsset);
        gpsset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                GpsSetDialog setDialog = new GpsSetDialog(mContext, R.style.Dialog);
                BussUtil.setDialogParams(mContext, setDialog, 0.5, 0.5);
            }
        });

        TextView version = (TextView) findViewById(R.id.version_check);
        double code = new VersionUpdata((BaseActivity) mContext).getVersionCode(mContext);
        version.setText("版本更新   " + code);
        version.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new UpdataThread().start();
            }
        });

        final CheckBox gpsCjlx = (CheckBox) findViewById(R.id.lxcj);
        if (gpsCaijiInclude.getVisibility() == View.VISIBLE) {
            gpsCjlx.setChecked(true);
        }
        gpsCjlx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    gpsCollectPresenter.showCollectionType(gpsCaijiInclude, SettingDialog.this);
                } else {
                    gpsCaijiInclude.setVisibility(View.GONE);
                }
            }
        });

        ImageView close = (ImageView) findViewById(R.id.settings_close);
        close.setOnClickListener(new CancleListener(this));

        final CheckBox addPoint = findViewById(R.id.add_mode_point);
        final CheckBox addLine = findViewById(R.id.add_mode_line);
        if (baseActivity.addModel == 0) {
            addLine.setChecked(true);
            addPoint.setChecked(false);
        } else {
            addLine.setChecked(false);
            addPoint.setChecked(true);
        }

        addPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseActivity.addModel = addPoint.isChecked() ? 1 : 0;
                addLine.setChecked(false);
            }
        });
        addLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseActivity.addModel = addPoint.isChecked() ? 0 : 1;
                addPoint.setChecked(false);
                baseActivity.dotPainting.setVisibility(View.GONE);
            }
        });

    }

    /*检查版本更新*/
    private class UpdataThread extends Thread {

        @Override
        public void run() {
            super.run();
            if (MyApplication.getInstance().netWorkTip()) {
                // 获取当前版本号 是否是最新版本
                String updateurl = mContext.getResources().getString(R.string.updateurl);
                boolean flag = new VersionUpdata((BaseActivity) mContext).checkVersion(updateurl);
                if (!flag) {
                    ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.setToast(mContext, "已是最新版本");
                        }
                    });
                }
            }else{
                ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.setToast(mContext, "网络错误，请检查网络连接");
                    }
                });
            }
        }
    }

}
