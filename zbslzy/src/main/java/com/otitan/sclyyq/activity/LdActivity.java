package com.otitan.sclyyq.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.otitan.sclyyq.BaseActivity;
import com.otitan.sclyyq.R;
import com.otitan.sclyyq.util.BussUtil;

/**
 * 林地管理系统
 */
public class LdActivity extends BaseActivity {

    private View parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        parentView = getLayoutInflater().inflate(R.layout.activity_ld, null);
        super.onCreate(savedInstanceState);
        setContentView(parentView);

        Context mContext = LdActivity.this;
        ImageView topview = (ImageView) parentView.findViewById(R.id.topview);
        topview.setBackground(mContext.getResources().getDrawable(R.drawable.share_top_ld_s));

        activitytype = getIntent().getStringExtra("name");
        proData = BussUtil.getConfigXml(mContext,activitytype);
    }

    @Override
    public View getParentView() {
        return parentView;
    }
}
