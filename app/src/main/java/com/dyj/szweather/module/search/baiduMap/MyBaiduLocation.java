package com.dyj.szweather.module.search.baiduMap;

import static com.dyj.szweather.app.App.getContext;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dyj.szweather.bean.CityDB;
import com.tamsiree.rxkit.view.RxToast;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.List;


/**
 * @author yinxiaolong
 * @describe：定位实现
 * @data: :
 */
public class MyBaiduLocation {

    //public static CityDB locationCity=new CityDB();
    private volatile static MyBaiduLocation baiduLocation;

    /**
     * 获取单例对象
     * @return baiduLocation
     */
    public static MyBaiduLocation getInstance(){
        if (baiduLocation==null){
            synchronized (MyBaiduLocation.class){
                baiduLocation=new MyBaiduLocation();
            }
        }
        return baiduLocation;
    }

    public  void   getLocation() {
            LocationClient mLocationClient = null;
        try {
            mLocationClient =new LocationClient(getContext());
            mLocationClient.registerLocationListener(new MyLocationListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
            LocationClientOption option=new LocationClientOption();
            option.setIsNeedAddress(true);
            option.setEnableSimulateGps(true);
            option.setIsNeedLocationDescribe(true);
            option.setIsNeedLocationPoiList(true);
            option.setNeedNewVersionRgc(true);
            option.setOpenGps(true);
           // option.setScanSpan(5000);
            option.setCoorType("bd09ll");
            option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        assert mLocationClient != null;
        mLocationClient.setLocOption(option);
            mLocationClient.start();
    }


    //回调方法
    // TODO: 2022/7/7 setButtonText的方法不用回调了，只在MainActivity中完成定位 ，后续 SearchActivity中只负责在数据库中查询显示，不进行定位操作
    class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //先检查数据库中是否有定位城市
            if (bdLocation.getDistrict()!=null) {//表示定位成功
               if(LitePal.where("isLocationCity=?","1").find(CityDB.class).size()==0){
                DatabaseLocationUtil.addLocationCityToCityDB(bdLocation);
                }else {
                    DatabaseLocationUtil.upDataLocationCityBD(bdLocation);
                }
                //DatabaseLocationUtil.addLocationCityToCityDB(bdLocation);
                EventBus.getDefault().post(bdLocation.getDistrict());
            }else {
                RxToast.error("定位失败");
            }
        }
    }
}
