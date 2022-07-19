package com.dyj.szweather.module.search.presenter;


import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.dyj.szweather.base.BaseBean;
import com.dyj.szweather.base.BaseObserver;
import com.dyj.szweather.base.BasePresenter;
import com.dyj.szweather.bean.CityDB;
import com.dyj.szweather.bean.CitySearch;
import com.dyj.szweather.bean.PopularCity;
import com.dyj.szweather.module.search.view.ISearchView;
import org.litepal.LitePal;
import java.util.List;
import java.util.Locale;

/**
 * @author yinxiaolong
 * @describe
 * @data: :
 */
public class SearchPresenter extends BasePresenter<ISearchView> implements android.text.TextWatcher{

    private final List<String> lists;

    public SearchPresenter(ISearchView baseView, List<String> list) {
        super(baseView);

        this.lists = list;
    }

    public boolean check(){
        return LitePal.findAll(CityDB.class).size() < 10;
    }

    public void getPopCity(){
        addDisposable(apiServer.getPopularCity("20", "cn"), new BaseObserver<BaseBean<List<PopularCity>>>(baseView) {
            @Override
            public void onSuccess(BaseBean<List<PopularCity>> o) {
                baseView.showPopCity(o);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }

    public void changeLocationLang(String location){
        addDisposable(apiServer.getCitySearch(location), new BaseObserver<BaseBean<List<CitySearch>>>(baseView) {


            @Override
            public void onSuccess(BaseBean<List<CitySearch>> o) {
                baseView.setLocationText(o.location.get(0).getName());
                CityDB cityDB = new CityDB();
                cityDB.setLocation(o.location.get(0).getId());
                cityDB.setCityName(o.location.get(0).getName());
                cityDB.setCityAdm2(o.location.get(0).getAdm2());
                cityDB.setIsLocationCity("1");
                cityDB.updateAll("location=?",location);//更新数据库
            }

            @Override
            public void onError(String msg) {

            }
        });
    }
    //城市搜索
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        if (text.length() == 0) {
            baseView.setToDefault();//baseView为activity实例
        } else {
            baseView.hidePopView();//隐藏热门城市
            //搜索
            addDisposable(apiServer.getCitySearch(text), new BaseObserver<BaseBean<List<CitySearch>>>(baseView) {
                @Override
                public void onSuccess(BaseBean<List<CitySearch>> o) {

                        lists.clear();
                        for (int i = 0; i < o.location.size(); i++) {
                            lists.add(o.location.get(i).getCountry()+o.location.get(i).getAdm1() +o.location.get(i).getName() );
                        }
                        baseView.showLists( o);
                }
                @Override
                public void onError(String msg) {
                    baseView.showSearchError();
                    baseView.setToDefault();
                }
            });
        }
    }
}
