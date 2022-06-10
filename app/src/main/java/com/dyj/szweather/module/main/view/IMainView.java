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

    /**
     * 从数据库获取更新城市数据，放入到vp中
     */
    void getCity();

    /**
     * @param url 背景图 url
     */
    void showPic(String url);

    /**
     * 供fragment调用设置
     *
     * @param time 后端最新的数据统计时间
     */
    void showTime(String time);

    /**
     * 供fragment调用，展示城市名称
     */
    void showCity();

    /**
     * 供fragment调用，隐藏城市名称
     */
    void hideCity();
}
