package com.dyj.szweather.module.main.view;

import com.dyj.szweather.base.BaseView;
import com.dyj.szweather.bean.CityDB;
import com.dyj.szweather.bean.PictureGirl;

import java.util.List;

/**
 * @author ：Dyj
 * @date ：Created in 2022/5/26 10:26
 * @description：iii
 * @modified By：
 * @version: 1.0
 */
public interface IMainView extends BaseView {
    void getCity(List<CityDB> list);

    void showPic(String url);

    void showTime(String time);

    void showCity();

    void hideCity();
}
