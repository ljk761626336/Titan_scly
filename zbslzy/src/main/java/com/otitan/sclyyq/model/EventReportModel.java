package com.otitan.sclyyq.model;

import android.app.Dialog;

import com.otitan.sclyyq.activity.EventReportActivity;
import com.otitan.sclyyq.db.DataBaseHelper;
import com.otitan.sclyyq.model.modelview.IEventReportModel;
import com.otitan.sclyyq.mview.IBaseView;
import com.otitan.sclyyq.mview.IEventReportView;
import com.otitan.sclyyq.service.RetrofitHelper;
import com.titan.baselibrary.util.ProgressDialogUtil;
import com.titan.baselibrary.util.ToastUtil;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by otitan_li on 2018/6/14.
 * EventReportModel
 */

public class EventReportModel extends BaseModel implements IEventReportModel {

    private IBaseView reportView;

    public EventReportModel(IBaseView view){
        this.reportView = view;
    }


    @Override
    public void senInofToServer(String json, final String id,final EventReportActivity activity) {
        Observable<String> oberver = RetrofitHelper.getInstance(reportView.getActivity())
                .getServer().upRequisitionInfo(json);
        oberver.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ProgressDialogUtil.stopProgressDialog(activity);
                        ToastUtil.setToast(activity,"网络连接错误 " + throwable.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        ProgressDialogUtil.stopProgressDialog(activity);
                        if(s.equals("true")){
                            ToastUtil.setToast(activity, "上报成功");
                            if(id.equals("")){
                                DataBaseHelper.delUnResportData(reportView.getActivity(),id);
                            }
                            activity.finish();
                        }else{
                            ToastUtil.setToast(reportView.getActivity(), "上报失败,检查网络");
                        }
                    }
                });
    }

    @Override
    public void senInofToServer(String json,final String id,final Dialog dialog) {
        ProgressDialogUtil.startProgressDialog(reportView.getActivity());
        Observable<String> oberver = RetrofitHelper.getInstance(reportView.getActivity())
                .getServer().upRequisitionInfo(json);
        oberver.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtil.setToast(reportView.getActivity(),"网络连接错误");
                        ProgressDialogUtil.stopProgressDialog(reportView.getActivity());
                    }

                    @Override
                    public void onNext(String s) {
                        ProgressDialogUtil.stopProgressDialog(reportView.getActivity());
                        if(s.equals("true")){
                            ToastUtil.setToast(reportView.getActivity(), "上报成功");
                            if(id.equals("")){
                                DataBaseHelper.delUnResportData(reportView.getActivity(),id);
                            }
                            dialog.dismiss();
                        }else{
                            ToastUtil.setToast(reportView.getActivity(), "上报失败,检查网络");
                        }
                    }
                });
    }
}
