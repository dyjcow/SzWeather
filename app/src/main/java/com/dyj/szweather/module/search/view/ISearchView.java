package com.dyj.szweather.module.search.view;

import com.dyj.szweather.base.BaseBean;
import com.dyj.szweather.base.BaseView;
import com.dyj.szweather.bean.CitySearch;
import com.dyj.szweather.bean.PopularCity;

import java.util.List;

/**
 * @author yinxiaolong
 * @describe
 * @data: :
 */
public interface ISearchView extends BaseView {
    void setToDefault();
    void showLists(BaseBean<List<CitySearch>> o);
    void addToDB(int position);
    void hidePopView();
    void showPopCity(BaseBean<List<PopularCity>> popCity);
    void addPopCityToDB(int position ,BaseBean<List<PopularCity>> popCity);
    void showSearchError();
    void showGetLocationError();
}
