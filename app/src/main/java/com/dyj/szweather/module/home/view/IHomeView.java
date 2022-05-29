package com.dyj.szweather.module.home.view;

import com.dyj.szweather.base.BaseView;
import com.dyj.szweather.bean.PictureGirl;

import java.util.List;

/**
 * @author ：Dyj
 * @date ：Created in 2022/5/27 21:03
 * @description：IHomeView
 * @modified By：
 * @version: 1.0
 */
public interface IHomeView extends BaseView {
    void showPic(String url);

    void getGEO(String msg);
}
