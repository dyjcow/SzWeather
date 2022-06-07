package com.dyj.szweather.module.citymanager.view;

import com.dyj.szweather.base.BaseView;
import com.dyj.szweather.bean.CityDB;

import java.util.List;

/**
 * @author: yzy
 * @date: 2022/5/28
 */
public interface ICManagerView extends BaseView {
    void setCMessage(List<CityDB> list);
}
