package com.dyj.szweather.module.search.baiduMap;

import static com.dyj.szweather.app.App.getContext;
import static com.dyj.szweather.util.ActivityUtil.actionSecondStart;
import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dyj.szweather.bean.CityDB;
import com.dyj.szweather.module.main.activity.MainActivity;
import com.dyj.szweather.module.search.activity.SearchActivity;
import com.tamsiree.rxkit.view.RxToast;

import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * @author yinxiaolong
 * @describe：定位实现
 * @data: :
 */
public class MyBaiduLocation {

    SearchActivity activity;
    public  void   getLocation(SearchActivity activity) {
        this.activity=activity;
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
            option.setScanSpan(5000);
            option.setCoorType("bd09ll");
            option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        assert mLocationClient != null;
        mLocationClient.setLocOption(option);
            mLocationClient.start();
    }

    public void permissionGet(SearchActivity activity){
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.
                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.
                permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.
                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(activity, permissions, 1);
        }
    }
    //回调方法
    class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getDistrict()!=null){
            activity.getBinding().locationCity.setText(bdLocation.getDistrict());
            activity.getBinding().locationCity.setOnClickListener(v -> {
                if ( LitePal.where("CityName==?",bdLocation.getDistrict()).find(CityDB.class).size()==0){
                CityDB cityDB =new CityDB();
//                cityDB.setLocation(bdLocation.getAdCode());
                cityDB.setLocation(String.format(Locale.US,"%.2f",bdLocation.getLongitude()) +","+ String.format(Locale.US,"%.2f",bdLocation.getLatitude()) );
                cityDB.setCityName(bdLocation.getDistrict());
                cityDB.setCityAdm2(bdLocation.getCity());
                cityDB.save();
                    activity.finish();
                    // TODO: 2022/6/2 把MainActivity 改为 要跳转到的首页activity
                    actionSecondStart(MainActivity.class, cityDB.getLocation(), cityDB.getCityName());
                }else {
                    RxToast.showToast("城市已存在");
                }
            });
        }else {
                activity.showGetLocationError();
            }
        }
    }
}
