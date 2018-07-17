package com.otitan.sclyyq.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.otitan.sclyyq.BaseActivity;
import com.otitan.sclyyq.R;
import com.otitan.sclyyq.util.BussUtil;

/**
 * 林木采伐管理系统
 */
public class LmcfActivity extends BaseActivity {
    @Override
    public void setReportDialog(Dialog dialog) {

    }

    @Override
    public Dialog getReportDialog() {
        return null;
    }
    private View parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        parentView = getLayoutInflater().inflate(R.layout.activity_lmcf, null);
        super.onCreate(savedInstanceState);
        setContentView(parentView);

        Context mContext = LmcfActivity.this;
        ImageView topview = (ImageView) parentView.findViewById(R.id.topview);
        topview.setBackground(mContext.getResources().getDrawable(R.drawable.share_top_lmcf_s));

        activitytype = getIntent().getStringExtra("name");
        proData = BussUtil.getConfigXml(mContext,activitytype);
    }

    @Override
    public View getParentView() {
        return parentView;
    }
}
