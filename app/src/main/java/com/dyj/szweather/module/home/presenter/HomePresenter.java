package com.dyj.szweather.module.home.presenter;

import com.dyj.szweather.base.BaseBean;
import com.dyj.szweather.base.BaseObserver;
import com.dyj.szweather.base.BasePresenter;
import com.dyj.szweather.bean.AirQuality;
import com.dyj.szweather.bean.CitySearch;
import com.dyj.szweather.bean.DailyFeel;
import com.dyj.szweather.bean.PictureGirl;
import com.dyj.szweather.bean.WeatherDay;
import com.dyj.szweather.bean.WeatherHours;
import com.dyj.szweather.bean.WeatherNow;
import com.dyj.szweather.common.GlobalConstant;
import com.dyj.szweather.http.API;
import com.dyj.szweather.module.home.view.IHomeView;
import com.dyj.szweather.util.MyUtil;
import com.dyj.szweather.util.SpUtil;
import com.tamsiree.rxkit.view.RxToast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：Dyj
 * @date ：Created in 2022/5/27 21:08
 * @description：HomePresenter
 * @modified By：
 * @version: 1.0
 */
public class HomePresenter extends BasePresenter<IHomeView> {


    public HomePresenter(IHomeView baseView) {
        super(baseView);
    }

    public void getWeatherNow(String location){
        addDisposable(apiServer.getWeatherNow(location, API.KEY), new BaseObserver<BaseBean<WeatherNow>>(baseView,false) {

            @Override
            public void onSuccess(BaseBean<WeatherNow> o) {
                baseView.showWeatherNow(o.now);
            }

            @Override
            public void onError(String msg) {
                RxToast.showToast("error");
            }
        });
    }

    public void getWeatherHours(String location){
        addDisposable(apiServer.getWeatherHours(location, API.KEY), new BaseObserver<BaseBean<List<WeatherHours>>>(baseView,false) {

            @Override
            public void onSuccess(BaseBean<List<WeatherHours>> o) {
                baseView.showWeatherHours(o.hourly);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    public void getWeatherDay(String location){
        addDisposable(apiServer.getWeatherDay(location, API.KEY), new BaseObserver<BaseBean<List<WeatherDay>>>(baseView,false) {

            @Override
            public void onSuccess(BaseBean<List<WeatherDay>> o) {
                baseView.showWeatherDay(o.daily);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    public void getAirQuality(String location){
        addDisposable(apiServer.getAirQuality(location, API.KEY), new BaseObserver<BaseBean<AirQuality>>(baseView,false) {

            @Override
            public void onSuccess(BaseBean<AirQuality> o) {
                baseView.showAirQuality(o.now);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    public void getDailyFeel(String location){
        final String type = "3,9,13";
        addDisposable(apiServer.getDailyFeel(type, location, API.KEY), new BaseObserver<BaseBean<List<DailyFeel>>>(baseView,false) {

            @Override
            public void onSuccess(BaseBean<List<DailyFeel>> o) {
                baseView.showDailyFeel(o.daily);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    public void getSunRise(String location,String date){
        addDisposable(apiServer.getSunRise(location,date, API.KEY), new BaseObserver<BaseBean<?>>(baseView,false) {

            @Override
            public void onSuccess(BaseBean<?> o) {
                baseView.showSunRise(o.sunrise,o.sunset);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    public void getMoonRise(String location,String date){
        addDisposable(apiServer.getMoonRise(location,date,API.KEY),new BaseObserver<BaseBean<?>>(baseView,false){

            @Override
            public void onSuccess(BaseBean<?> o) {
                baseView.showMoonRise(o.moonrise,o.moonset);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    public void refresh(String location){
        getWeatherNow(location);
        getWeatherHours(location);
        getWeatherDay(location);
        getDailyFeel(location);
        getAirQuality(location);
        baseView.closeRefresh();
    }
    

}
