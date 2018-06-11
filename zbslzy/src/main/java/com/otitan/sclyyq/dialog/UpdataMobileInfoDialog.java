package com.otitan.sclyyq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.otitan.sclyyq.MyApplication;
import com.otitan.sclyyq.R;
import com.otitan.sclyyq.service.Webservice;
import com.otitan.sclyyq.util.ToastUtil;
import com.titan.baselibrary.listener.CancleListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by li on 2017/6/1.
 * 修改设备信息
 */

public class UpdataMobileInfoDialog extends Dialog {

    private Context mContext;

    public UpdataMobileInfoDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public UpdataMobileInfoDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected UpdataMobileInfoDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_mobileinfo);

        final JSONObject res = getMobileInfo(new JSONObject());
        final EditText nameTxt = (EditText) findViewById(R.id.mobile_name_text);
        final EditText telTxt = (EditText) findViewById(R.id.mobile_tel_text);
        final EditText addressTxt = (EditText) findViewById(R.id.mobile_dz_text);
        final EditText timeTxt = (EditText) findViewById(R.id.mobile_time_text);
        final EditText mb_nameTxt = (EditText) findViewById(R.id.sb_name_text);
        final EditText bzTxt = (EditText) findViewById(R.id.beizhu_text);
        TextView sbhTxt = (TextView) findViewById(R.id.sbh_text);
        sbhTxt.setText(MyApplication.macAddress);
        try {
            nameTxt.setText(res.getString("NAME"));
            telTxt.setText(res.getString("TEL"));
            addressTxt.setText(res.getString("SYSDW"));
            timeTxt.setText(res.getString("TIME"));
            mb_nameTxt.setText(res.getString("SBMC"));
            bzTxt.setText(res.getString("BZ"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Button button = (Button) findViewById(R.id.mobile_info_btn_sure);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Webservice webservice = new Webservice(mContext);
                try {
                    String result = webservice.updateMobileSysInfo(res
                                    .getString("ID"), nameTxt.getText().toString(),
                            telTxt.getText().toString(), addressTxt
                                    .getText().toString(), mb_nameTxt
                                    .getText().toString(), bzTxt.getText()
                                    .toString());
                    if (result.equals("true")) {
                        ToastUtil.setToast(mContext, "更新成功");
                        dismiss();
                    } else {
                        ToastUtil.setToast(mContext, "更新失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ImageView close = (ImageView) findViewById(R.id.mobile_info_close);
        close.setOnClickListener(new CancleListener(this));

    }

    /** 解析json数据 */
    public JSONObject getMobileInfo(JSONObject res) {
        try {
            final Webservice webservice = new Webservice(mContext);
            String result = webservice
                    .getMobileSysInfo(MyApplication.macAddress);
            if (result.equals(Webservice.netException)) {
                ToastUtil.setToast(mContext, Webservice.netException);
            } else if (result.equals("无数据")) {
                ToastUtil.setToast(mContext, "无数据");
            } else {
                JSONObject object = new JSONObject(result);
                res = object.optJSONArray("ds").optJSONObject(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
}
