package com.otitan.sclyyq.activity;

import com.otitan.sclyyq.BaseActivity;
import com.otitan.sclyyq.R;
import com.otitan.sclyyq.util.BussUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
/**
 * Created by li on 2016/5/26.
 * 湿地资源页面
 */
public class SdzyActivity extends BaseActivity {
	@Override
	public void setReportDialog(Dialog dialog) {

	}

	@Override
	public Dialog getReportDialog() {
		return null;
	}
	View parentview;
	Context mContext;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		parentview = getLayoutInflater().inflate(R.layout.activity_sdzy, null);
		super.onCreate(savedInstanceState);
		setContentView(parentview);
		mContext = SdzyActivity.this;
		/*变更系统背景*/
		ImageView topview = (ImageView) parentview.findViewById(R.id.topview);
		topview.setBackground(mContext.getResources().getDrawable(R.drawable.share_top_sdzy));
		
		activitytype = getIntent().getStringExtra("name");
		/*获取配置的数据*/
		proData = BussUtil.getConfigXml(mContext,activitytype);
		
	}

	@Override
	public View getParentView() {
		return parentview;
	}

}
