package com.otitan.sclyyq.greendao;

import com.otitan.sclyyq.MyApplication;
import com.otitan.sclyyq.util.ResourcesManager;

import java.io.FileNotFoundException;

/**
 * Created by otitan_li on 2018/7/11.
 * GreenDaoManager
 */

public class GreenDaoManager {


    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private MyOpenHelper mSQLiOpenHelper;


    public GreenDaoManager(){
        try {
            String dbname = MyApplication.resourcesManager.getDataBase("db.sqlite");
            init(dbname);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 静态内部类，实例化对象使用
     */
    private static class SingleInstanceHolder {
        private static final GreenDaoManager INSTANCE = new GreenDaoManager();
    }

    /**
     * 对外唯一实例的接口
     *
     * @return
     */
    public static GreenDaoManager getInstance() {
        return SingleInstanceHolder.INSTANCE;
    }

    /**
     * 初始化数据
     */
    public void init(String dbname) {
        mSQLiOpenHelper = new MyOpenHelper(MyApplication.getInstance(),dbname,null);
        mDaoMaster = new DaoMaster(mSQLiOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoMaster getmDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

    public MyOpenHelper getmSQLiOpenHelper() {
        return mSQLiOpenHelper;
    }

    public void setmSQLiOpenHelper(MyOpenHelper mSQLiOpenHelper) {
        this.mSQLiOpenHelper = mSQLiOpenHelper;
    }


}
