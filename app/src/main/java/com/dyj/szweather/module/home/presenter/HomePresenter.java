package com.dyj.szweather.module.home.presenter;

import com.dyj.szweather.base.BaseBean;
import com.dyj.szweather.base.BaseObserver;
import com.dyj.szweather.base.BasePresenter;
import com.dyj.szweather.bean.PictureGirl;
import com.dyj.szweather.common.GlobalConstant;
import com.dyj.szweather.module.home.view.IHomeView;
import com.dyj.szweather.util.SpUtil;

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

    public static List<PictureGirl> list;

    public HomePresenter(IHomeView baseView) {
        super(baseView);
    }

    public void getPIc(int position){
        if (!SpUtil.getBoolean(GlobalConstant.IS_LOAD)){
            String url = "https://www.mxnzp.com/api/image/girl/list/random";
            addDisposable(apiServer.getPic(url), new BaseObserver<BaseBean<List<PictureGirl>>>(baseView, true) {

                @Override
                public void onSuccess(BaseBean<List<PictureGirl>> o) {
                    list = new ArrayList<>();
                    list.addAll(o.data);
                    baseView.showPic(list.get(position).getImageUrl());
                    SpUtil.setBoolean(GlobalConstant.IS_LOAD,true);
                }

                @Override
                public void onError(String msg) {

                }
            });
        }else {
            baseView.showPic(list.get(position).getImageUrl());
        }

    }
}
