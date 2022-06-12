package com.dyj.szweather.module.citymanager.presenter;

import com.dyj.szweather.base.BasePresenter;
import com.dyj.szweather.bean.CityDB;
import com.dyj.szweather.bean.CitySearch;
import com.dyj.szweather.module.citymanager.view.ICManagerView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: yzy
 * @date: 2022/5/28
 */
public class CManagerPresenter extends BasePresenter<ICManagerView> {


    public CManagerPresenter(ICManagerView baseView) {
        super(baseView);
    }

    public void getCityList(){
        List<CityDB> list = new ArrayList<>();
        if(!list.isEmpty()){
            list.clear();
        }
        list = LitePal.findAll(CityDB.class);
        baseView.setCMessage(list);
    }
}
