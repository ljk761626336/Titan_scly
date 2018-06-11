package com.otitan.sclyyq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.otitan.sclyyq.MyApplication;
import com.otitan.sclyyq.util.BussUtil;
import com.otitan.sclyyq.R;
import com.otitan.sclyyq.service.RetrofitHelper;
import com.otitan.sclyyq.util.ToastUtil;
import com.titan.baselibrary.listener.CancleListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by li on 2017/5/31.
 * 设备信息录入dialog
 */

public class MobileInputDialog extends Dialog {

    private Context mContext;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public MobileInputDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public MobileInputDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected MobileInputDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_mobileinfo);
        setCanceledOnTouchOutside(false);

        final EditText nameTxt = (EditText) findViewById(R.id.mobile_name_text);
        final EditText telTxt = (EditText) findViewById(R.id.mobile_tel_text);
        final EditText addressTxt = (EditText) findViewById(R.id.mobile_dz_text);
        TextView timeTxt = (TextView) findViewById(R.id.mobile_time_text);

        timeTxt.setText(format.format(new Date()));
        final EditText sb_nameTxt = (EditText) findViewById(R.id.sb_name_text);
        sb_nameTxt.setText(MyApplication.mobileType);
        TextView sbhTxt = (TextView) findViewById(R.id.sbh_text);
        sbhTxt.setText(MyApplication.macAddress);
        final EditText bzTxt = (EditText) findViewById(R.id.beizhu_text);

        Button button = (Button) findViewById(R.id.mobile_info_btn_sure);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (TextUtils.isEmpty(nameTxt.getText())) {
                    ToastUtil.setToast(mContext, "请输入使用者姓名");
                    return;
                }
                if (TextUtils.isEmpty(telTxt.getText())) {
                    ToastUtil.setToast(mContext, "请输入手机号");
                    return;
                }
                if (!BussUtil.checkTelNumber(telTxt.getText().toString())) {
                    ToastUtil.setToast(mContext, "请输入正确的11位手机号");
                    return;
                }
                if (TextUtils.isEmpty(addressTxt.getText())) {
                    ToastUtil.setToast(mContext, "请输入单位名称");
                    return;
                }
                if (TextUtils.isEmpty(sb_nameTxt.getText())) {
                    ToastUtil.setToast(mContext, "请输入设备名称");
                    return;
                }

//                String resl = webservice.addMobileSysInfo(nameTxt.getText().toString(), telTxt.getText().toString(),
//                        addressTxt.getText().toString(), format.format(new Date()),
//                        sb_nameTxt.getText().toString(),MyApplication.macAddress, bzTxt.getText().toString());
                sendDataToServer(nameTxt.getText().toString(), telTxt.getText().toString(),
                        addressTxt.getText().toString(), format.format(new Date()),
                        sb_nameTxt.getText().toString(),MyApplication.macAddress, bzTxt.getText().toString());
//                if (resl.equals("true")) {
//                    MyApplication.sharedPreferences.edit().putBoolean(MyApplication.macAddress, true).apply();
//                    ToastUtil.setToast(mContext, "用户信息录入成功");
//                    dismiss();
//                }
            }
        });

        ImageView close = (ImageView) findViewById(R.id.mobile_info_close);
        close.setOnClickListener(new CancleListener(this));
    }

    /**发送数据到后台*/
    private void sendDataToServer(String sysname,String tel,String dw,String retime,String sbmc,String sbh,String bz){
        Observable<String> observable = RetrofitHelper.getInstance(mContext).getServer().addMoblieSysInfo(sysname,tel,dw,retime,sbmc,sbh,bz);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        if (s.equals("true")) {
                            MyApplication.sharedPreferences.edit().putBoolean(MyApplication.macAddress, true).apply();
                            ToastUtil.setToast(mContext, "用户信息录入成功");
                            dismiss();
                        }
                    }
                });
    }
}
