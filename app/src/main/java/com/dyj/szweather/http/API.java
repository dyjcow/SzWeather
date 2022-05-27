package com.dyj.szweather.http;

import com.dyj.szweather.R;
import com.dyj.szweather.base.BaseBean;
import com.dyj.szweather.bean.AirQuality;
import com.dyj.szweather.bean.CitySearch;
import com.dyj.szweather.bean.DailyFeel;
import com.dyj.szweather.bean.MoonRise;
import com.dyj.szweather.bean.PictureGirl;
import com.dyj.szweather.bean.PopularCity;
import com.dyj.szweather.bean.WeatherDay;
import com.dyj.szweather.bean.WeatherHours;
import com.dyj.szweather.bean.WeatherNow;
import com.dyj.szweather.util.MyUtil;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * @author ：Dyj
 * @date ：Created in 2022/5/25 15:38
 * @description：接口文件
 * @modified By：
 * @version: 1.0
 */
public class API {
    static final String BASE_URL = MyUtil.getString(R.string.url);

    public static final String KEY = MyUtil.getString(R.string.key);

    public interface SZApi {

        /**
         * 获取当前实时天气
         *
         * @param location 位置代码或者经纬度信息
         * @param key 常量key
         * @return 对应 observable
         */
        @GET("v7/weather/now")
        Observable<BaseBean<WeatherNow>> getWeatherNow(@Query("location") String location,@Query("key") String key);

        /**
         * 获取未来7天的天气信息
         *
         * @param location 位置代码或者经纬度信息
         * @param key 常量key
         * @return 对应 observable
         */
        @GET("v7/weather/7d")
        Observable<BaseBean<List<WeatherDay>>> getWeatherDay(@Query("location") String location,@Query("key") String key);

        /**
         * 获取未来24小时天气情况
         *
         * @param location 位置代码或者经纬度信息
         * @param key 常量key
         * @return 对应 observable
         */
        @GET("v7/weather/24h")
        Observable<BaseBean<List<WeatherHours>>> getWeatherHours(@Query("location") String location,@Query("key") String key);

        /**
         * 生活指数
         *
         * @param type 生活指数类型
         * @param location 位置代码或者经纬度信息
         * @param key 常量key
         * @return 对应 observable
         */
        @GET("v7/indices/1d")
        Observable<BaseBean<List<DailyFeel>>> getDailyFeel(@Query("type") String type,@Query("location") String location,@Query("key") String key);

        /**
         * 城市搜索
         *
         * @param location 位置代码或者经纬度信息
         * @param key 常量key
         * @return 对应 observable
         */
        @GET("v2/city/lookup")
        Observable<BaseBean<List<CitySearch>>> getCitySearch(@Query("location") String location,@Query("key") String key);

        /**
         * 获取热门城市
         *
         * @param number 热门城市数量
         * @param range 城市范围，world是世界，cn是中国
         * @param key 常量key
         * @return 对应 observable
         */
        @GET("v2/city/top")
        Observable<BaseBean<List<PopularCity>>> getPopularCity(@Query("number") String number,@Query("range") String range,@Query("key") String key);

        /**
         * 日出日落
         *
         * @param location 位置代码或者经纬度信息
         * @param date 日期，最多可选择未来60天（包含今天）的数据。日期格式为yyyyMMdd
         * @param key 常量key
         * @return 对应 observable
         */
        @GET("v7/astronomy/sun")
        Observable<BaseBean<?>> getSunRise(@Query("location") String location,@Query("date") String date,@Query("key") String key);

        /**
         * 月升月落
         *
         * @param location 位置代码或者经纬度信息
         * @param date 日期，最多可选择未来60天（包含今天）的数据。日期格式为yyyyMMdd
         * @param key 常量key
         * @return 对应 observable
         */
        @GET("v7/astronomy/moon")
        Observable<BaseBean<List<MoonRise>>> getMoonRise(@Query("location") String location,@Query("date") String date,@Query("key") String key);

        /**
         * 获取空气质量
         *
         * @param location 位置代码或者经纬度信息
         * @param key 常量key
         * @return 对应 observable
         */
        @GET("v7/air/now")
        Observable<BaseBean<List<AirQuality>>> getAirQuality(@Query("location") String location,@Query("key") String key);


        /**
         * 背景图片
         *
         * @param url 要放入完整路径
         * @return 对应 observable
         */
        @Headers({"app_id:kvq0nvszkwmqqqbh","app_secret:N0V3S20vM0lCd1dzZkZJWFpaalRkdz09"})
        @GET
        Observable<BaseBean<List<PictureGirl>>> getPic(@Url String url);

    }
}
