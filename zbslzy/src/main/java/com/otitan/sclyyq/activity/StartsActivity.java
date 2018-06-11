package com.otitan.sclyyq.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.otitan.sclyyq.MyApplication;
import com.otitan.sclyyq.R;
import com.otitan.sclyyq.dialog.MobileInputDialog;
import com.otitan.sclyyq.entity.MobileInfo;
import com.otitan.sclyyq.service.RetrofitHelper;
import com.otitan.sclyyq.util.BussUtil;
import com.otitan.sclyyq.util.ToastUtil;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.titan.versionupdata.VersionUpdata;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 启动开始页面，系统选择
 */
public class StartsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.rl_yzl)
    RelativeLayout mRl_yzl;
    @BindView(R.id.rl_ld)
    RelativeLayout mRl_ld;
    @BindView(R.id.rl_lmcf)
    RelativeLayout mRl_lmcf;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starts);

        ButterKnife.bind(this);

        mContext = StartsActivity.this;

        initView();

        /*初始化数据*/
        initData();
		/*获取设备内存*/
        //getAvailableMemorySize();

        checkVersion();
    }

    private void initView() {
        mRl_yzl.setOnClickListener(this);
        mRl_ld.setOnClickListener(this);
        mRl_lmcf.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_yzl: // 营造林管理系统
                ProgressDialogUtil.startProgressDialog(mContext);
                Intent intent_yzl = new Intent(mContext, YzlActivity.class);
                intent_yzl.putExtra("name", "yzl");
                startActivity(intent_yzl);
                break;
            case R.id.rl_ld: // 林地管理系统
                ProgressDialogUtil.startProgressDialog(mContext);
                Intent intent_ld = new Intent(mContext, LdActivity.class);
                intent_ld.putExtra("name", "ld");
                startActivity(intent_ld);
                break;
            case R.id.rl_lmcf: // 林木采伐系统
                ProgressDialogUtil.startProgressDialog(mContext);
                Intent intent_lmcf = new Intent(mContext, LmcfActivity.class);
                intent_lmcf.putExtra("name", "lmcf");
                startActivity(intent_lmcf);
                break;
        }
    }

    private void initData() {
        if (MyApplication.getInstance().netWorkTip()) {
            boolean flag = MyApplication.sharedPreferences.getBoolean(MyApplication.macAddress, false);
            if (flag) {
                //new MyAsyncTask().execute("selMobileSysInfo");
                selMobileSysInfo(MyApplication.macAddress, MyApplication.mobileXlh, MyApplication.mobileType);
            }
        }
    }

    /**
     * 异步事件
     */
    class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(final String... params) {
            if (params[0].equals("selMobileSysInfo")) {

            }
            return null;
        }
    }

    /**
     * 发送数据到后台
     */
    private void selMobileSysInfo(String sbh, String xlh, String type){
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().selMobileSysInfo(sbh);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.setToast(mContext,"网络错误");
            }

            @Override
            public void onNext(String result) {
                if (result != null) {

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String json = jsonObject.optString("ds");
                        List<MobileInfo> MobileInfos = new Gson().fromJson(json, new TypeToken<List<MobileInfo>>() {}.getType());
                        if(MobileInfos.get(0).getSYZNAME().equals("")){
                            // 弹出设备使用者录入信息窗口
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    showMobileSysInfoView();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 弹出录入设备使用者信息窗口
     */
    private void showMobileSysInfoView() {
        MobileInputDialog mobileInputDialog = new MobileInputDialog(mContext,R.style.Dialog);
        BussUtil.setDialogParams(mContext, mobileInputDialog, 0.5, 0.6);
    }

    /**
     * 获取手机内部剩余存储空间
     */
    private double getAvailableMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        long sss = (availableBlocks * blockSize) / (1024 * 1024 * 1024);
        Log.d("=============", sss + "G");
        return sss;
    }

    /**
     * 版本更新检查
     */
    private void checkVersion(){
        new StartsActivity.UpdataThread().start();
    }

    /**
     * 检查版本更新
     */
    private class UpdataThread extends Thread{

        @Override
        public void run() {
            super.run();
            if(MyApplication.getInstance().netWorkTip()){
                // 获取当前版本号 是否是最新版本
                String updateurl = mContext.getResources().getString(R.string.updateurl);
                boolean flag = new VersionUpdata(StartsActivity.this).checkVersion(updateurl);
                if(flag){
                    //提示更新
                }else{
                    //已是最新版本
                }
            }
        }
    }

}
