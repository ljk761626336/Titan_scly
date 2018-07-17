package com.otitan.sclyyq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otitan.sclyyq.R;
import com.otitan.sclyyq.entity.EventList;
import com.titan.baselibrary.listener.CancleListener;

public class EventListDialog extends Dialog {

    private EditText sjmcEdit, sjmsEdit, telEdit, addrEdit, bzEdit;
    private TextView jdText, wdText;



    private EventList eventList;
    public EventListDialog(@NonNull Context context, int themeResId, EventList eventList) {
        super(context, themeResId);
        this.eventList = eventList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_jjxxsb);

        initview();
    }


    /*初始化控件*/
    private void initview() {
        ImageView close = findViewById(R.id.xxsb_close);
        close.setOnClickListener(new CancleListener(this));

        TextView title = findViewById(R.id.jjxxsb_title);
        title.setText("信息详情");

        sjmsEdit = findViewById(R.id.txt_sjms);
        sjmsEdit.setText(eventList.getXJ_MSXX());
        sjmsEdit.setEnabled(false);
        sjmcEdit = findViewById(R.id.txt_sjmc);
        sjmcEdit.setText(eventList.getXJ_SJMC());
        sjmcEdit.setEnabled(false);
        telEdit = findViewById(R.id.txt_tel);
        addrEdit = findViewById(R.id.txt_addr);
        addrEdit.setText(eventList.getXJ_XXDZ());
        addrEdit.setEnabled(false);
        bzEdit = findViewById(R.id.txt_beizhu);
        bzEdit.setText(eventList.getREMARK());
        bzEdit.setEnabled(false);

        jdText = findViewById(R.id.txt_jd);
        jdText.setText(String.valueOf(eventList.getXJ_JD()));

        wdText = findViewById(R.id.txt_wd);
        wdText.setText(String.valueOf(eventList.getXJ_WD()));

        LinearLayout picLayout = findViewById(R.id.jjxxsb_zp);
        LinearLayout audioLayout = findViewById(R.id.jjxxsb_yp);
        LinearLayout videoLayout = findViewById(R.id.jjxxsb_sp);
        LinearLayout upLayout = findViewById(R.id.jjxxsb_btn);
        picLayout.setVisibility(View.GONE);
        audioLayout.setVisibility(View.GONE);
        videoLayout.setVisibility(View.GONE);
        upLayout.setVisibility(View.GONE);
    }
}
