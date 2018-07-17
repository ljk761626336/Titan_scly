package com.otitan.sclyyq.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.lling.photopicker.PhotoPickerActivity;
import com.otitan.sclyyq.BaseActivity;
import com.otitan.sclyyq.MyApplication;
import com.otitan.sclyyq.R;
import com.otitan.sclyyq.activity.ImageBrowseActivity;
import com.otitan.sclyyq.adapter.Recyc_imageAdapter;
import com.otitan.sclyyq.db.DataBaseHelper;
import com.otitan.sclyyq.entity.Audio;
import com.otitan.sclyyq.entity.Emergency;
import com.otitan.sclyyq.entity.EvenType;
import com.otitan.sclyyq.entity.Image;
import com.otitan.sclyyq.entity.Video;
import com.otitan.sclyyq.model.FormatModel;
import com.otitan.sclyyq.model.modelview.IEventReportModel;
import com.otitan.sclyyq.mview.IEventReportView;
import com.otitan.sclyyq.service.RetrofitHelper;
import com.titan.baselibrary.customview.MyRadioGroup;
import com.titan.baselibrary.listener.CancleListener;
import com.titan.baselibrary.util.ToastUtil;
import com.titan.baselibrary.util.Util;
import com.titan.medialibrary.activity.AudioRecorderActivity;
import com.titan.medialibrary.activity.VideoRecorderActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by li on 2017/7/6.
 * 紧急信息上报
 */

public class JjxxsbDialog extends Dialog implements Recyc_imageAdapter.PicOnclick, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private BaseActivity mContext;
    private JjxxsbDialog dialog;
    private static int width = 0;
    private ArrayList<Image> images = new ArrayList<>();
    private ArrayList<Video> videos = new ArrayList<>();
    private ArrayList<Audio> audios = new ArrayList<>();
    private IEventReportView reportView;
    private IEventReportModel reportModel;
    private FormatModel formatModel;

    private String bigValue = "";
    private String smallValue = "";
    private ArrayList<EvenType> types = new ArrayList<>();
    private Map<Integer, Boolean> hashMap = new HashMap<>();

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    private String audioPath="";
    private String videoPath="";

    private View glsslView, ysdzwView, rlhdView, qitaView;
    private EditText sjmcEdit, sjmsEdit, telEdit, addrEdit, bzEdit;
    private TextView jdText, wdText;
    private Spinner spinner;
    private TextView sureTextview, localTextview;

    private RecyclerView picRecyc;
    private TextView audeoRecyc;
    private VideoView videoView;
    private TextView picTextview, videoTextview, audeoTextview;

    private MyRadioGroup glssGroup, sjxzGroup, rlhdGroup;

    /*管理设置类view*/
    private RadioButton glzview, shaokaview, lwtview, xjzxview, jiebeiview, jiezhuangview;
    /*人类活动类*/
    private RadioButton nykfscview, slgcview, cscsview, yzcview, lyssview, nyczhview, jmdview;
    /*生境现状*/
    private RadioButton senlinview, guancongview, caodiview, nongtianview, shuiyuview;

    private EditText glssqitaEdit, ysdzwnameEdit, ysdzwshuliangEdit,
            sjxzqitaEdit, rlhdqitaEdit, rlhdBzEdit, qitavalueEdit;

    public static WindowManager.LayoutParams getDialogParams() {
        return dialogParams;
    }

    public void setDialogParams(WindowManager.LayoutParams dialogParams) {
        JjxxsbDialog.dialogParams = dialogParams;
    }

    public static WindowManager.LayoutParams dialogParams;

    public JjxxsbDialog(@NonNull BaseActivity context, @StyleRes int themeResId, IEventReportView baseView, IEventReportModel model) {
        super(context, themeResId);
        this.mContext = context;
        width = this.getWindow().getAttributes().width;
        this.reportView = baseView;
        this.reportModel = model;
        this.formatModel = new FormatModel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_jjxxsb);
        setCanceledOnTouchOutside(false);
        dialog = this;

        initview();
        initSpinnerAdapter();

    }

    /*初始化控件*/
    private void initview() {
        ImageView close = findViewById(R.id.xxsb_close);
        close.setOnClickListener(new CancleListener(this));

        spinner = findViewById(R.id.sjlx_spinner);
        glsslView = findViewById(R.id.glss_view);
        ysdzwView = findViewById(R.id.ysdzw_view);
        rlhdView = findViewById(R.id.rlhd_view);
        qitaView = findViewById(R.id.qita_view);

        sjmsEdit = findViewById(R.id.txt_sjms);
        sjmcEdit = findViewById(R.id.txt_sjmc);
        telEdit = findViewById(R.id.txt_tel);
        addrEdit = findViewById(R.id.txt_addr);
        addrEdit.setText(reportView.getCurAddStr());
        bzEdit = findViewById(R.id.txt_beizhu);

        jdText = findViewById(R.id.txt_jd);
        jdText.setText(formatModel.decimalFormat(reportView.getGpsPoint().getX()));

        wdText = findViewById(R.id.txt_wd);
        wdText.setText(formatModel.decimalFormat(reportView.getGpsPoint().getY()));

        glssGroup = findViewById(R.id.group_glss);
        sjxzGroup = findViewById(R.id.group_sjxz);
        rlhdGroup = findViewById(R.id.group_rlhd);

        /*图片*/
        picTextview = findViewById(R.id.xczp_pic);
        picTextview.setOnClickListener(this);
        picRecyc = findViewById(R.id.txt_xczp);

        /*音频*/
        audeoTextview = findViewById(R.id.xcyp_pic);
        audeoTextview.setOnClickListener(this);
        audeoRecyc = findViewById(R.id.txt_xcyp);

        /*视频*/
        videoTextview = findViewById(R.id.xcsp_pic);
        videoTextview.setOnClickListener(this);
        videoView = findViewById(R.id.txt_xcsp);

        sureTextview = findViewById(R.id.xxsb_save);
        sureTextview.setOnClickListener(this);

        localTextview = findViewById(R.id.xxsb_save_local);
        localTextview.setOnClickListener(this);

    }

    /*初始化 Spinner数值*/
    private void initSpinnerAdapter() {
        final String[] arrays = mContext.getResources().getStringArray(R.array.xinxicaiji);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, arrays);
        //设置下拉窗口样式
        adapter.setDropDownViewResource(R.layout.myspinner);
        spinner.setAdapter(adapter);
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bigValue = arrays[position];
                initSmallView(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /*根据大类选择展示小类视图*/
    private void initSmallView(int id) {
        if (id == 1) {
            //管理设施类
            glsslView.setVisibility(View.VISIBLE);
            ysdzwView.setVisibility(View.GONE);
            rlhdView.setVisibility(View.GONE);
            qitaView.setVisibility(View.GONE);

            glzview = glssGroup.findViewById(R.id.glss_glz);
            glzview.setOnCheckedChangeListener(this);
            shaokaview = glssGroup.findViewById(R.id.glss_shaoka);
            shaokaview.setOnCheckedChangeListener(this);
            lwtview = glssGroup.findViewById(R.id.glss_lwt);
            lwtview.setOnCheckedChangeListener(this);
            xjzxview = glssGroup.findViewById(R.id.glss_xjzx);
            xjzxview.setOnCheckedChangeListener(this);
            jiebeiview = glssGroup.findViewById(R.id.glss_jiebei);
            jiebeiview.setOnCheckedChangeListener(this);
            jiezhuangview = glssGroup.findViewById(R.id.glss_jiezhuang);
            jiezhuangview.setOnCheckedChangeListener(this);


            glssqitaEdit = glsslView.findViewById(R.id.glss_qita);
            glssqitaEdit.addTextChangedListener(new MyTextWatcher(id));
            glssqitaEdit.setOnFocusChangeListener(new MyOnFocusChangeListener(id));

            return;
        }
        if (id == 2) {
            //野生动植物类
            ysdzwView.setVisibility(View.VISIBLE);
            glsslView.setVisibility(View.GONE);
            rlhdView.setVisibility(View.GONE);
            qitaView.setVisibility(View.GONE);

            ysdzwnameEdit = ysdzwView.findViewById(R.id.ysdzw_name);
            ysdzwnameEdit.addTextChangedListener(new MyTextWatcher(id));
            ysdzwshuliangEdit = ysdzwView.findViewById(R.id.ysdzw_shuliang);
            ysdzwshuliangEdit.addTextChangedListener(new MyTextWatcher(id));

            senlinview = ysdzwView.findViewById(R.id.sjxz_senlin);
            senlinview.setOnCheckedChangeListener(this);
            guancongview = ysdzwView.findViewById(R.id.sjxz_guancong);
            guancongview.setOnCheckedChangeListener(this);
            caodiview = ysdzwView.findViewById(R.id.sjxz_caodi);
            caodiview.setOnCheckedChangeListener(this);
            nongtianview = ysdzwView.findViewById(R.id.sjxz_nongtian);
            nongtianview.setOnCheckedChangeListener(this);
            shuiyuview = ysdzwView.findViewById(R.id.sjxz_shuiyu);
            shuiyuview.setOnCheckedChangeListener(this);


            sjxzqitaEdit = ysdzwView.findViewById(R.id.sjxz_qita);
            sjxzqitaEdit.addTextChangedListener(new MyTextWatcher(id));
            sjxzqitaEdit.setOnFocusChangeListener(new MyOnFocusChangeListener(id));

            return;
        }

        if (id == 3) {
            //人类活动
            ysdzwView.setVisibility(View.GONE);
            glsslView.setVisibility(View.GONE);
            rlhdView.setVisibility(View.VISIBLE);
            qitaView.setVisibility(View.GONE);

            nykfscview = rlhdGroup.findViewById(R.id.rlhd_nykfsc);
            nykfscview.setOnCheckedChangeListener(this);
            slgcview = rlhdGroup.findViewById(R.id.rlhd_slgc);
            slgcview.setOnCheckedChangeListener(this);
            cscsview = rlhdGroup.findViewById(R.id.rlhd_cscs);
            cscsview.setOnCheckedChangeListener(this);
            yzcview = rlhdGroup.findViewById(R.id.rlhd_yzc);
            yzcview.setOnCheckedChangeListener(this);
            lyssview = rlhdGroup.findViewById(R.id.rlhd_lyss);
            lyssview.setOnCheckedChangeListener(this);
            nyczhview = rlhdGroup.findViewById(R.id.rlhd_nyczh);
            nyczhview.setOnCheckedChangeListener(this);
            jmdview = rlhdGroup.findViewById(R.id.rlhd_jmd);
            jmdview.setOnCheckedChangeListener(this);


            rlhdqitaEdit = rlhdView.findViewById(R.id.rlhd_qita);
            rlhdqitaEdit.addTextChangedListener(new MyTextWatcher(id));
            rlhdqitaEdit.setOnFocusChangeListener(new MyOnFocusChangeListener(id));

            rlhdBzEdit = rlhdView.findViewById(R.id.rlhd_beizhu);
            rlhdBzEdit.addTextChangedListener(new MyTextWatcher(id));
            rlhdBzEdit.setOnFocusChangeListener(new MyOnFocusChangeListener(id));
            return;
        }

        if (id == 4) {
            qitaView.setVisibility(View.VISIBLE);
            rlhdView.setVisibility(View.GONE);
            ysdzwView.setVisibility(View.GONE);
            glsslView.setVisibility(View.GONE);

            qitavalueEdit = qitaView.findViewById(R.id.qita_value);
            qitavalueEdit.addTextChangedListener(new MyTextWatcher(id));
            return;
        }
    }

    @Override
    public void setPicOnclick(View item, int position) {
        Intent intent = new Intent(mContext, ImageBrowseActivity.class);
        intent.putStringArrayListExtra("images", reportView.getPicList());
        intent.putExtra("position", position);
        mContext.startActivity(intent);
    }

    /**
     * 选择图片后加载图片
     */
    public void loadPhoto() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        picRecyc.setLayoutManager(layoutManager);
        Recyc_imageAdapter adapter = new Recyc_imageAdapter(mContext, reportView.getPicList(), MyApplication.screen.getWidthPixels() / 4);
        picRecyc.setAdapter(adapter);
        adapter.setPicOnclick(dialog);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xcyp_pic:
                startAudio();
                break;
            case R.id.xcsp_pic:
                startVideo();
                break;
            case R.id.xczp_pic:
                startPic();
                break;
            case R.id.xxsb_save:
                addOnline();
                break;
            case R.id.xxsb_save_local:
                addLocal();
                break;
        }
    }

    private void addOnline() {
        //网络连接后上报成功
        Emergency emergency = new Emergency();
        emergency.setXJ_JD(formatModel.decimalFormat(reportView.getGpsPoint().getX()));
        emergency.setXJ_WD(formatModel.decimalFormat(reportView.getGpsPoint().getY()));
        emergency.setXJ_MSXX(sjmsEdit.getText().toString().trim());
        emergency.setXJ_SJMC(sjmcEdit.getText().toString().trim());
        emergency.setREMARK(bzEdit.getText().toString().trim());
        emergency.setXJ_XXDZ(addrEdit.getText().toString().trim());
        emergency.setXJ_SBBH(MyApplication.macAddress);
        EvenType type = new EvenType();
        type.setSJ_XL(smallValue);
        type.setSJ_DL(bigValue);
        types.add(type);
        Gson gson = new Gson();
//        emergency.setXJ_LX(types);


        String pictxt = "";
        if (reportView.getPicList().size() == 1) {
            pictxt = reportView.getPicList().get(0);
        } else if (reportView.getPicList().size() > 1) {
            pictxt = reportView.getPicList().get(0);
            for (int i = 1; i < reportView.getPicList().size(); i++) {
                pictxt = pictxt + "," + reportView.getPicList().get(i);
            }
        }
        if (!RetrofitHelper.getInstance(mContext).networkMonitor.isConnected()) {
            emergency.setXJ_ZPDZ(pictxt);
            ToastUtil.setToast(mContext, "网络未连接数据保存到本地");
            //保存数据
            boolean state = DataBaseHelper.addUnResportData(mContext, emergency);
            if (state) {
                ToastUtil.setToast(mContext, "保存成功");
                JjxxsbDialog.this.dismiss();
            } else {
                ToastUtil.setToast(mContext, "保存失败");
            }
            return;
        }
        for (String pic : reportView.getPicList()) {
            String base = Util.picToString(pic);
            Image image = new Image();
            image.setBase(base);
            image.setName(new File(pic).getName());
            images.add(image);
        }
        String jsonimage = gson.toJson(images);


        File audioFile = new File(getAudioPath());
        if(audioFile.exists()){
            Audio audio = new Audio();
            String audiobase = Util.fileToString(audioPath);
            audio.setName(audioFile.getName());
            audio.setBase(audiobase);
            audios.add(audio);
        }

        String audiojson = gson.toJson(audios);

        File videoFile = new File(getVideoPath());
        if(videoFile.exists()){
            Video video = new Video();
            String videobase = Util.fileToString(videoPath);
            video.setName(videoFile.getName());
            video.setBase(videobase);
            videos.add(video);
        }
        String videojson = gson.toJson(videos);

        emergency.setXJ_ZPDZ(jsonimage);
        emergency.setXJ_YPDZ("[]");
        emergency.setXJ_SPDZ("[]");
        emergency.setXJ_LX("[]");

        String json = new Gson().toJson(emergency);
        if (MyApplication.getInstance().netWorkTip()) {
            reportModel.senInofToServer(json, "",this);
        }
    }

    private void addLocal() {
        Emergency emergency = new Emergency();
        emergency.setXJ_JD(formatModel.decimalFormat(reportView.getGpsPoint().getX()));
        emergency.setXJ_WD(formatModel.decimalFormat(reportView.getGpsPoint().getY()));
        emergency.setXJ_MSXX(sjmsEdit.getText().toString());
        emergency.setXJ_SJMC(sjmcEdit.getText().toString());
        emergency.setREMARK(addrEdit.getText().toString());
        emergency.setXJ_SBBH(MyApplication.macAddress);
        String pictxt = "";
        if (reportView.getPicList().size() == 1) {
            pictxt = reportView.getPicList().get(0);
        } else if (reportView.getPicList().size() > 1) {
            pictxt = reportView.getPicList().get(0);
            for (int i = 1; i < reportView.getPicList().size(); i++) {
                pictxt = pictxt + "," + reportView.getPicList().get(i);
            }
        }
        emergency.setXJ_ZPDZ(pictxt);
        //保存数据
        boolean state = DataBaseHelper.addUnResportData(mContext, emergency);
        if (state) {
            ToastUtil.setToast(mContext, "保存成功");
            JjxxsbDialog.this.dismiss();
        } else {
            ToastUtil.setToast(mContext, "保存失败");
        }
    }

    private void startAudio() {
        Intent intent = new Intent(mContext, AudioRecorderActivity.class);
        mContext.startActivityForResult(intent, BaseActivity.PICK_AUDIO);
    }

    private void startVideo() {
        Intent intent = new Intent(mContext, VideoRecorderActivity.class);
        mContext.startActivityForResult(intent, BaseActivity.PICK_VIDEO);
    }

    /**
     * 跳转到选择图片界面
     */
    public void startPic() {
        if (reportView.getPicList().size() != 0 && reportView.getPicList().size() != 9) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("重新选择会覆盖之前的图片");
            builder.setMessage("是否重新选择");
            builder.setCancelable(true);
            builder.setPositiveButton("重新选择", new OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(mContext, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);//是否显示相机
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);//选择模式（默认多选模式）
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);//最大照片张数
                    mContext.startActivityForResult(intent, BaseActivity.PICK_PHOTO);
                }
            });
            builder.setNegativeButton("取消", new OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(16);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(16);
        }
        if (reportView.getPicList().size() == 9) {
            ToastUtil.setToast(mContext, "照片最多只能选择9张");
            return;
        }
        if (reportView.getPicList().size() == 0) {
            Intent intent = new Intent(mContext, PhotoPickerActivity.class);
            intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);//是否显示相机
            intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);//选择模式（默认多选模式）
            intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);//最大照片张数
            mContext.startActivityForResult(intent, BaseActivity.PICK_PHOTO);
        }
    }

    public void showVideo() {
        videoView.setVideoPath(videoPath);
        videoView.start();
    }

    public void setAudioname() {
        audeoRecyc.setText(new File(audioPath).getName());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        boolean flag = buttonView.isChecked();
        switch (buttonView.getId()){
            case R.id.glss_glz://管理站
                smallValue = glzview.getText().toString().trim();
                break;
            case R.id.glss_shaoka://哨卡
                smallValue = shaokaview.getText().toString().trim();
                break;
            case R.id.glss_lwt://瞭望塔
                smallValue = lwtview.getText().toString().trim();
                break;
            case R.id.glss_xjzx://宣教中心
                smallValue = xjzxview.getText().toString().trim();
                break;
            case R.id.glss_jiebei://界碑
                smallValue = jiebeiview.getText().toString().trim();
                break;
            case R.id.glss_jiezhuang://界桩
                smallValue = jiezhuangview.getText().toString().trim();
                break;
            case R.id.sjxz_senlin://森林
                smallValue = senlinview.getText().toString().trim();
                break;
            case R.id.sjxz_guancong://灌丛
                smallValue = guancongview.getText().toString().trim();
                break;
            case R.id.sjxz_caodi://草地
                smallValue = caodiview.getText().toString().trim();
                break;
            case R.id.sjxz_nongtian://农田
                smallValue = nongtianview.getText().toString().trim();
                break;
            case R.id.sjxz_shuiyu://水域
                smallValue = shuiyuview.getText().toString().trim();
                break;
            case R.id.rlhd_nykfsc://能源开发生产
                smallValue = nykfscview.getText().toString().trim();
                break;
            case R.id.rlhd_slgc://水利工程
                smallValue = slgcview.getText().toString().trim();
                break;
            case R.id.rlhd_cscs://采石彩砂
                smallValue = cscsview.getText().toString().trim();
                break;
            case R.id.rlhd_yzc://养殖场
                smallValue = yzcview.getText().toString().trim();
                break;
            case R.id.rlhd_lyss://旅游设施
                smallValue = lyssview.getText().toString().trim();
                break;
            case R.id.rlhd_nyczh://农业垦殖
                smallValue = nyczhview.getText().toString().trim();
                break;
            case R.id.rlhd_jmd://居民点
                smallValue = jmdview.getText().toString().trim();
                break;
        }
    }

    class MyTextWatcher implements TextWatcher {

        private int position = 0;

        public MyTextWatcher(int id) {
            this.position = id;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (position == 1) {
                smallValue = glssqitaEdit.getText().toString().trim();
                return;
            }

            if (position == 2) {
                String txt1 = ysdzwnameEdit.getText().toString().trim();
                String txt2 = ysdzwshuliangEdit.getText().toString().trim();
                smallValue = txt1 + "," + txt2;

                smallValue =smallValue+","+ sjxzqitaEdit.getText().toString().trim();
                return;
            }

            if (position == 3) {
                String txt1 = rlhdqitaEdit.getText().toString().trim();
                String txt2 = rlhdBzEdit.getText().toString().trim();
                smallValue = txt1 + "," + txt2;
                return;
            }

            if (position == 4) {
                smallValue = qitavalueEdit.getText().toString().trim();
                return;
            }

        }
    }

    class MyOnFocusChangeListener implements View.OnFocusChangeListener {
        int position = 0;
        public MyOnFocusChangeListener(int id){
            this.position = id;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (position == 1 && hasFocus) {
                setGlssViewChecked();
                return;
            }

            if (position == 2 && hasFocus) {
                setSjxzViewChecked();
                return;
            }

            if (position == 3 && hasFocus) {
                setRlhdViewChecked();
                return;
            }

        }
    }

    private void setGlssViewChecked(){
        glzview.setChecked(false);
        shaokaview.setChecked(false);
        lwtview.setChecked(false);
        xjzxview.setChecked(false);
        jiebeiview.setChecked(false);
        jiezhuangview.setChecked(false);
    }

    private void setRlhdViewChecked(){
        nykfscview.setChecked(false);
        slgcview.setChecked(false);
        cscsview.setChecked(false);
        yzcview.setChecked(false);
        lyssview.setChecked(false);
        nyczhview.setChecked(false);
        jmdview.setChecked(false);
    }

    private void setSjxzViewChecked(){
        senlinview.setChecked(false);
        guancongview.setChecked(false);
        caodiview.setChecked(false);
        nongtianview.setChecked(false);
        shuiyuview.setChecked(false);
    }


}

