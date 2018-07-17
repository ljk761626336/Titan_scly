package com.otitan.sclyyq.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lling.photopicker.PhotoPickerActivity;
import com.otitan.sclyyq.BaseActivity;
import com.otitan.sclyyq.MyApplication;
import com.otitan.sclyyq.R;
import com.otitan.sclyyq.adapter.Recyc_imageAdapter;
import com.otitan.sclyyq.db.DataBaseHelper;
import com.otitan.sclyyq.entity.Emergency;
import com.otitan.sclyyq.entity.Image;
import com.otitan.sclyyq.model.EventReportModel;
import com.otitan.sclyyq.model.FormatModel;
import com.otitan.sclyyq.mview.IBaseView;
import com.otitan.sclyyq.service.RetrofitHelper;
import com.otitan.sclyyq.util.PadUtil;
import com.otitan.sclyyq.util.ToastUtil;
import com.titan.baselibrary.timepaker.TimePopupWindow;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.titan.baselibrary.util.Util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EventReportActivity2 extends AppCompatActivity implements View.OnClickListener, Recyc_imageAdapter.PicOnclick {

    private Context mContext;
    //事件编号 上报时间 事件内容 上报人员 备注 事件名称 详细地址
    private EditText edtNumber, edtTime, edtContent, edtPeople, edtRemark, edtName, edtAddress;
    //现场照片
    private TextView tvXczp;
    //巡查id
    private String xcId = "";
    private RecyclerView picRecyc;
    private IBaseView baseView;
    private FormatModel formatModel = new FormatModel();
    private ArrayList<Image> images = new ArrayList<>();
    private ArrayList<String> picList = new ArrayList<>();
    private EventReportModel reportModel;
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA
    };

    @Override
    protected void onStart() {
        super.onStart();
        // 缺少权限时, 进入权限配置页面
        if (new com.titan.baselibrary.permission.PermissionsChecker(this).lacksPermissions(PERMISSIONS)) {
            com.titan.baselibrary.permission.PermissionsActivity.startActivityForResult(this, com.titan.baselibrary.permission.PermissionsActivity.PERMISSIONS_REQUEST_CODE, PERMISSIONS);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (PadUtil.isPad(this)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.dialog_lysjadd);
        baseView = BaseActivity.baseView;
        reportModel = new EventReportModel(baseView);
        initView();
        if (getIntent().getStringExtra("id")!=null){
            xcId = getIntent().getStringExtra("id");
        }
    }

    public void initView() {
        Toolbar toolbar = findViewById(R.id.sjadd_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        edtNumber = findViewById(R.id.lysj_number);
        edtTime = findViewById(R.id.lysj_time);
        edtContent = findViewById(R.id.lysj_content);
        edtPeople = findViewById(R.id.lysj_people);
        edtRemark = findViewById(R.id.lysj_remark);
        edtName = findViewById(R.id.sjadd_sjmc);
        edtAddress = findViewById(R.id.sjadd_address);
        tvXczp = findViewById(R.id.sjadd_xczp);
        edtTime.setOnClickListener(this);
        tvXczp.setOnClickListener(this);
        edtAddress.setText(baseView.getCurAddStr());
        picRecyc = findViewById(R.id.sjadd_img);

        TextView tvSave = findViewById(R.id.lysj_save);
        TextView tvCancel = findViewById(R.id.lysj_cancle);
        tvSave.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        edtNumber.setText(getCurTime(0, System.currentTimeMillis()));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lysj_time:
                timeSelect();
                break;
            case R.id.lysj_save:
                save();
                break;
            case R.id.sjadd_xczp:
                selectImg();
                break;
        }
    }


    private void selectImg() {
        if (picList.size() <= 9 && !picList.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.alertDialog_AppTheme);
            builder.setTitle("重新选择会覆盖之前的图片");
            builder.setMessage("是否重新选择");
            builder.setCancelable(true);
            builder.setPositiveButton("重新选择", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(EventReportActivity2.this, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);//是否显示相机
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);//选择模式（默认多选模式）
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);//最大照片张数
                    startActivityForResult(intent, 1);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(16f);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(16f);
        }
        if (picList.isEmpty()) {
            Intent intent = new Intent(EventReportActivity2.this, PhotoPickerActivity.class);
            intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);//是否显示相机
            intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);//选择模式（默认多选模式）
            intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);//最大照片张数
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void setPicOnclick(View item, int position) {
        Intent intent = new Intent(EventReportActivity2.this, ImageBrowseActivity.class);
        intent.putStringArrayListExtra("images", picList);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data == null||data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT) == null) {
                return;
            }
            picList.clear();
            picList.addAll(data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT));
            loadPic();
        }
    }

    private void loadPic() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        picRecyc.setLayoutManager(layoutManager);
        Recyc_imageAdapter adapter = new Recyc_imageAdapter(mContext, picList, MyApplication.screen.getWidthPixels() / 4);
        picRecyc.setAdapter(adapter);
        adapter.setPicOnclick(this);
    }

    /**
     * 上报事件
     */
    private void save() {
        if (!checkContent()) {
            return;
        }

        ProgressDialogUtil.startProgressDialog(this);
        Emergency emergency = new Emergency();
        emergency.setXC_ID(xcId);
        emergency.setXJ_JD(formatModel.decimalFormat(baseView.getGpsPoint().getX()));
        emergency.setXJ_WD(formatModel.decimalFormat(baseView.getGpsPoint().getY()));
        emergency.setXJ_SBBH(MyApplication.macAddress);
        emergency.setXJ_SJMC(edtName.getText().toString().trim());
        emergency.setXJ_MSXX(edtContent.getText().toString().trim());
        emergency.setREMARK(edtRemark.getText().toString().trim());
        emergency.setXJ_XXDZ(edtAddress.getText().toString().trim());
        emergency.setXC_ID(xcId);
        String pictxt = "";
        if (picList.size() == 1) {
            pictxt = picList.get(0);
        } else if (picList.size() > 1) {
            pictxt = picList.get(0);
            for (int i = 1; i < picList.size(); i++) {
                pictxt = pictxt + "," + picList.get(i);
            }
        }
        if (!RetrofitHelper.getInstance(mContext).networkMonitor.isConnected()) {
            ProgressDialogUtil.stopProgressDialog(this);
            emergency.setXJ_ZPDZ(pictxt);
            ToastUtil.setToast(mContext, "网络未连接数据保存到本地");
            //保存数据
            boolean state = DataBaseHelper.addUnResportData(mContext, emergency);
            if (state) {
                ToastUtil.setToast(mContext, "保存成功");
                finish();
            } else {
                ToastUtil.setToast(mContext, "保存失败");
            }
            return;
        }
        for (String pic : picList) {
            String base = Util.picToString(pic);
            Image image = new Image();
            image.setBase(base);
            image.setName(new File(pic).getName());
            images.add(image);
        }
        emergency.setXJ_ZPDZ(pictxt);


        if (MyApplication.getInstance().netWorkTip()) {
            List list = new ArrayList();
            list.add(emergency);
//            reportModel.senInofToServer(objToJson(list), "", this);
        }
    }

    /**
     * 时间选择
     */
    private void timeSelect() {
        TimePopupWindow window = new TimePopupWindow(mContext, TimePopupWindow.Type.ALL);
        window.setCyclic(true);
        window.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                edtTime.setText(getCurTime(1, date.getTime()));
            }
        });
        window.showAtLocation(edtTime, Gravity.BOTTOM, 0, 0, new Date(), false);
    }

    /**
     * 获取当前时间
     *
     * @param type 获取的时间类型 0事件编号 1正常时间
     * @param data 时间戳
     * @return 格式化后的时间
     */
    private String getCurTime(int type, long data) {
        SimpleDateFormat dateFormat;
        if (type == 0) {
            dateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        }
        return dateFormat.format(data);
    }

    /**
     * 对象转json
     *
     * @param obj 需要转换的对象
     * @return 转换后的json
     */
    private String objToJson(Object obj) {
        String result = "";
        if (obj != null) {
            Gson gson = new Gson();
            result = gson.toJson(obj);
        }
        return result;
    }

    /**
     * 检测内容是否为空
     *edtName, edtAddress
     * @return true表示不为空 false存在空值
     */
    private boolean checkContent() {
        boolean flag = true;
        if (TextUtils.isEmpty(edtName.getText())) {
            ToastUtil.setToast(mContext, "事件名称不能为空");
            flag = false;
        } else if (TextUtils.isEmpty(edtNumber.getText())) {
            ToastUtil.setToast(mContext, "事件编号不能为空");
            flag = false;
        } else if (TextUtils.isEmpty(edtTime.getText())) {
            ToastUtil.setToast(mContext, "上报时间不能为空");
            flag = false;
        } else if (TextUtils.isEmpty(edtAddress.getText())) {
            ToastUtil.setToast(mContext, "详细地址不能为空");
            flag = false;
        } else if (TextUtils.isEmpty(edtContent.getText())) {
            ToastUtil.setToast(mContext, "事件描述不能为空");
            flag = false;
        } else if (TextUtils.isEmpty(edtPeople.getText())) {
            ToastUtil.setToast(mContext, "上报人员不能为空");
            flag = false;
        }
        return flag;
    }
}
