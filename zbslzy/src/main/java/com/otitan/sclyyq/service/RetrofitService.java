package com.otitan.sclyyq.service;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by li on 2017/5/5.
 * 接口 retrofitservice 连接后台数据库
 */

public interface RetrofitService {


    @GET("addMacAddress")
    Observable<String> addMacAddress(@Query("sbh") String sbh, @Query("xlh") String xlh);

    @GET("addMoblieSysInfo")
    Observable<String> addMoblieSysInfo(@Query("sysname") String sysname, @Query("tel") String tel,@Query("dw") String dw, @Query("retime") String retime,@Query("sbmc") String sbmc, @Query("sbh") String sbh,@Query("bz") String bz);

    @GET("selMobileInfo")
    Observable<String> selMobileSysInfo(@Query("sbh") String sbh);
    /*提交疫源疫病信息*/
    @GET("addYyybJcdwData")
    Observable<String> addYyybJcdwData(@Query("json") String json);
    /*获取监测单位数据*/
    @GET("getYyybJcdwData")
    Observable<String> getJcdwData();
    /*分页获取运输台账数据*/
    @GET("getYszInfo")
    Observable<String> getYszData(@Query("pageIndex") String pageIndex);

    /*木材经营 许可证查询*/
    @GET("getXkznsInfo")
    Observable<String> getXkzData(@Query("id") int id);

    /*木材经营 企业信息添加*/
    @GET("addQyInfo")
    Observable<String> addQyData(@Query("json") String json);

    /*传坐标*/
    @GET("uPLonLat")
    Observable<String> uPLonLat(@Query("SBH") String SBH,@Query("LON") String LON,@Query("LAT") String LAT,@Query("time") String time);

}
