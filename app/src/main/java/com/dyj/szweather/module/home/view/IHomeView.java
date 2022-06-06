package com.dyj.szweather.module.home.view;

import com.dyj.szweather.base.BaseView;
import com.dyj.szweather.bean.AirQuality;
import com.dyj.szweather.bean.DailyFeel;
import com.dyj.szweather.bean.PictureGirl;
import com.dyj.szweather.bean.WeatherDay;
import com.dyj.szweather.bean.WeatherHours;
import com.dyj.szweather.bean.WeatherNow;

import java.util.List;

/**
 * @author ：Dyj
 * @date ：Created in 2022/5/27 21:03
 * @description：IHomeView
 * @modified By：
 * @version: 1.0
 */
public interface IHomeView extends BaseView {

    void showWeatherNow(WeatherNow now);

    void showWeatherDay(List<WeatherDay> daily);

    void showWeatherHours(List<WeatherHours> hourly);

    void showAirQuality(AirQuality now);

    void showDailyFeel(List<DailyFeel> daily);

    void showSunRise(String sunrise,String sunset);

    void showMoonRise(String moonrise,String moonset);

    void closeRefresh();
}
