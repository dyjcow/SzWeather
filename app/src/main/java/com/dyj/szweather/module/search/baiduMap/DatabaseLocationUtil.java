package com.dyj.szweather.module.search.baiduMap;

import static com.dyj.szweather.util.ActivityUtil.actionSecondStart;

import android.content.ContentValues;

import com.baidu.location.BDLocation;
import com.dyj.szweather.bean.CityDB;
import com.dyj.szweather.module.main.activity.MainActivity;
import com.dyj.szweather.module.search.activity.SearchActivity;
import com.tamsiree.rxkit.view.RxToast;

import org.litepal.LitePal;

import java.util.Locale;

/**
 * @author yinxiaolong
 * @describe
 * @data: :
 */
public class DatabaseLocationUtil {


 public static void addLocationCityToCityDB(BDLocation bdLocation){
        CityDB cityDB=new CityDB();
        cityDB.setLocation(String.format(Locale.US,"%.2f",bdLocation.getLongitude()) +","+ String.format(Locale.US,"%.2f",bdLocation.getLatitude()) );
        cityDB.setCityName(bdLocation.getDistrict().substring(0,bdLocation.getDistrict().length()-1));
        cityDB.setCityAdm2(bdLocation.getCity());
        cityDB.setIsLocationCity("1");
        cityDB.save();
    }

    public static void upDataLocationCityBD(BDLocation bdLocation){
        CityDB cityDB=new CityDB();
        cityDB.setLocation(String.format(Locale.US,"%.2f",bdLocation.getLongitude()) +","+ String.format(Locale.US,"%.2f",bdLocation.getLatitude()) );
        cityDB.setCityName(bdLocation.getDistrict().substring(0,bdLocation.getDistrict().length()-1));
        cityDB.setCityAdm2(bdLocation.getCity());
        cityDB.setIsLocationCity("1");
        cityDB.updateAll("isLocationCity = ? or cityName =?","1",bdLocation.getDistrict().substring(0,bdLocation.getDistrict().length()-1));
    }


}
