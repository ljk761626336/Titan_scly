package com.otitan.sclyyq.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.otitan.sclyyq.BaseActivity;
import com.otitan.sclyyq.R;
import com.otitan.sclyyq.util.BussUtil;
import com.otitan.sclyyq.util.PadUtil;

/**
 * Created by li on 2016/5/26.
 * 营造林页面
 * （营造林管理系统）
 */
public class YzlActivity extends BaseActivity {
	@Override
	public void setReportDialog(Dialog dialog) {

	}

	@Override
	public Dialog getReportDialog() {
		return null;
	}
	private View parentView;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (PadUtil.isPad(this)){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		parentView = getLayoutInflater().inflate(R.layout.activity_base, null);
		super.onCreate(savedInstanceState);
		setContentView(parentView);

		Context mContext = YzlActivity.this;
		ImageView topview = parentView.findViewById(R.id.topview);
//		topview.setBackground(mContext.getResources().getDrawable(R.drawable.share_top_yzl));
//		topview.setBackground(mContext.getResources().getDrawable(R.drawable.share_top));

		activitytype = getIntent().getStringExtra("name");
		activitytype = "yzl";
		proData = BussUtil.getConfigXml(mContext,activitytype);
		
	}

	@Override
	public View getParentView() {
		return parentView;
	}

	@Override
	public void onBackPressed() {
		Intent launcherIntent = new Intent(Intent.ACTION_MAIN);
		launcherIntent.addCategory(Intent.CATEGORY_HOME);
		startActivity(launcherIntent);
	}
}
