package com.otitan.sclyyq.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.otitan.sclyyq.BaseActivity;
import com.otitan.sclyyq.R;
import com.otitan.sclyyq.util.BussUtil;
/**
 * Created by li on 2016/5/26.
 * 营造林页面
 * （营造林管理系统）
 */
public class YzlActivity extends BaseActivity {
	
	private View parentView;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		parentView = getLayoutInflater().inflate(R.layout.activity_yzl, null);
		super.onCreate(savedInstanceState);
		setContentView(parentView);

		Context mContext = YzlActivity.this;
		ImageView topview = (ImageView) parentView.findViewById(R.id.topview);
//		topview.setBackground(mContext.getResources().getDrawable(R.drawable.share_top_yzl));
		topview.setBackground(mContext.getResources().getDrawable(R.drawable.share_top));

		activitytype = getIntent().getStringExtra("name");
		activitytype = "yzl";
		proData = BussUtil.getConfigXml(mContext,activitytype);
		
	}

	@Override
	public View getParentView() {
		return parentView;
	}

}
