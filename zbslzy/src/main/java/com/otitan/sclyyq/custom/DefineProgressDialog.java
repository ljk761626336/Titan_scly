package com.otitan.sclyyq.custom;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.otitan.sclyyq.R;

/**
 * 自定义进度弹窗（舍去）
 */
public class DefineProgressDialog extends ProgressDialog {

	private String message;
	
	public DefineProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public DefineProgressDialog(Context context, String meg) {
		super(context);
		this.message = meg;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progressbar);
		TextView define_progress_msg = (TextView) findViewById(R.id.id_tv_loadingmsg1);
		define_progress_msg.setText(message);
	}

}
