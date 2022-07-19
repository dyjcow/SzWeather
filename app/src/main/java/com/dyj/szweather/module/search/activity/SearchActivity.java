package com.dyj.szweather.module.search.activity;

import static com.dyj.szweather.util.ActivityUtil.actionSecondStart;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.dyj.szweather.R;
import com.dyj.szweather.base.BaseActivity;
import com.dyj.szweather.base.BaseBean;
import com.dyj.szweather.bean.CityDB;
import com.dyj.szweather.bean.CitySearch;
import com.dyj.szweather.bean.PopularCity;
import com.dyj.szweather.databinding.ActivitySearchBinding;
import com.dyj.szweather.module.main.activity.MainActivity;
import com.dyj.szweather.module.search.adapter.PopAdapter;
import com.dyj.szweather.module.search.baiduMap.DatabaseLocationUtil;
import com.dyj.szweather.module.search.baiduMap.MyBaiduLocation;
import com.dyj.szweather.module.search.presenter.SearchPresenter;
import com.dyj.szweather.module.search.view.ISearchView;
import com.dyj.szweather.util.MyUtil;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;
import com.tamsiree.rxkit.view.RxToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author   :yinxiaolong
 * @describe :
 * @data  :2022/5/30 16:46
 */
public class SearchActivity extends BaseActivity<SearchPresenter, ActivitySearchBinding> implements ISearchView {
    private ArrayAdapter<String> searchListAdapter;
    private List<String> list;
    private BaseBean<List<CitySearch>> o;

    @Override
    protected SearchPresenter createPresenter() {
        list = new ArrayList<>();
        return new SearchPresenter(this,list);//传入list 进行初始化
    }

    @Override
    protected void initView() {
        Window window = this.getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        EventBus.getDefault().register(this);
        permissionGet();
        MyBaiduLocation.getInstance().getLocation();
        presenter = createPresenter();
        getBinding().cityAddEdittextSearch.addTextChangedListener(presenter);//加入edittext 监听
        getBinding().locationCity.setOnClickListener(v ->{
            List<CityDB> locationLists;
            if ((locationLists=LitePal.where("isLocationCity=?", "1").find(CityDB.class)).size()!=0){
            getBinding().locationCity.setText(locationLists.get(0).getCityName());
            RxToast.error(MyUtil.getString(R.string.City_existed));
            }
            else
            {
                MyBaiduLocation.isClick=true;
                MyBaiduLocation.getInstance().getLocation();
                startActivity(new Intent(SearchActivity.this,MainActivity.class));
            }
        } );

        getBinding().cityAddBtnSearch.setOnClickListener(v -> setToDefault());
        getBinding().cityAddBtnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void initData() {
        searchListAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_dropdown_item_1line, list);
        getBinding().listview.setAdapter(searchListAdapter);
        getBinding().listview.setOnItemClickListener((parent, view, position, id) -> {
            //获取到 数据存入数据库
                addToDB(position);
        });
        presenter.getPopCity();
    }


    /**
     * @author   :yinxiaolong
     * @describe : 隐藏搜索的城市列表
     * @data  :2022/5/30 16:45
     */
    @Override
    public void setToDefault() {
        list.clear();
        searchListAdapter.notifyDataSetChanged();
        getBinding().popCity.setVisibility(View.VISIBLE);
    }

    /**
     * @author   :yinxiaolong
     * @describe : 搜索列表改变
     * @data  :2022/5/30 16:47
     */
    @Override
    public void showLists(BaseBean<List<CitySearch>> o) {
        searchListAdapter.notifyDataSetChanged();
        this.o = o;
    }

    /**
     * @author   :yinxiaolong
     * @describe : 搜索列表的城市添加到数据库
     * @data  :2022/5/30 16:48
     */
    @Override
    public void addToDB(int position) {
        if (presenter.check()) {
               MyBaiduLocation.locationCity= MyBaiduLocation.locationCity.substring(0, MyBaiduLocation.locationCity.length() - 1);
                List<CityDB> cityDBList=LitePal.where("location=?",o.location.get(position).getId()).find(CityDB.class);
                //1:检查数据库中是否有该城市 2：检查是否是定位城市
            if (!MyBaiduLocation.locationCity.equals(o.location.get(position).getName())) {
                if ((cityDBList.size() == 0)) {//定位城市
                    CityDB cityDB = new CityDB();
                    cityDB.setLocation(o.location.get(position).getId());
                    cityDB.setCityName(o.location.get(position).getName());
                    cityDB.setCityAdm2(o.location.get(position).getAdm2());
                    cityDB.save();
                    finish();
                    // TODO: 2022/6/2 把MainActivity 改为 要跳转到的首页activity
                    actionSecondStart(MainActivity.class, cityDB.getLocation(), cityDB.getCityName());
                } else {
                    Toast.makeText(this, MyUtil.getString(R.string.City_existed), Toast.LENGTH_SHORT).show();
                }
            }else {
                //是定位城市并且 并不存在定位城市
                if (LitePal.where("isLocationCity=?","1").find(CityDB.class).size()==0){

                    CityDB cityDB = new CityDB();
                    cityDB.setLocation(o.location.get(position).getId());
                    cityDB.setCityName(o.location.get(position).getName());
                    cityDB.setCityAdm2(o.location.get(position).getAdm2());
                    cityDB.setIsLocationCity("1");
                    cityDB.save();
                    finish();
                    // TODO: 2022/6/2 把MainActivity 改为 要跳转到的首页activity
                    actionSecondStart(MainActivity.class, cityDB.getLocation(), cityDB.getCityName());
                }else {
                    Toast.makeText(this, MyUtil.getString(R.string.City_existed), Toast.LENGTH_SHORT).show();
                }
            }

            }else{
                RxToast.error(MyUtil.getString(R.string.city_is_full));

            }

    }


    /**
     * @author   :yinxiaolong
     * @describe : 在搜索时隐藏 热门城市界面
     * @data  :2022/5/30 16:49
     */
    @Override
    public void hidePopView() {
        if (getBinding().popCity.getVisibility() == View.VISIBLE) {
            getBinding().popCity.setVisibility(View.GONE);
        }
    }

    /**
     * @author   :yinxiaolong
     * @describe : 展示热门城市
     * @data  :2022/5/30 16:50
     */
    @Override
    public void showPopCity(BaseBean<List<PopularCity>> popCity) {
        PopAdapter popAdapter = new PopAdapter(popCity, this);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        getBinding().popCityRecyclerView.setLayoutManager(layoutManager);
        getBinding().popCityRecyclerView.setAdapter(popAdapter);
    }

    /**
     * @author   :yinxiaolong
     * @describe : 在点击热门城市时，将其加入到数据库
     * @data  :2022/5/30 16:50
     */
    @Override
    public void addPopCityToDB(int position, BaseBean<List<PopularCity>> popCity) {
        if (presenter.check()){
        List<CityDB> cityDBList = LitePal.where("location==?", popCity.topCityList.get(position).getId()).find(CityDB.class);
        if (cityDBList == null || cityDBList.size() == 0) {
            CityDB cityDB = new CityDB();
            cityDB.setLocation(popCity.topCityList.get(position).getId());
            cityDB.setCityName(popCity.topCityList.get(position).getName());
            cityDB.setCityAdm2(popCity.topCityList.get(position).getAdm2());
            cityDB.save();
            finish();
            // TODO: 2022/6/2 把MainActivity 改为 要跳转到的首页activity
            actionSecondStart(MainActivity.class, cityDB.getLocation(), cityDB.getCityName());
        } else {
            Toast.makeText(this, MyUtil.getString(R.string.City_existed), Toast.LENGTH_SHORT).show();
        }
        }else {
            Toast.makeText(this, MyUtil.getString(R.string.city_is_full), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showSearchError() {
        RxToast.showToast(MyUtil.getString(R.string.city_not_existe));
    }

    @Override
    public void showGetLocationError() {
        RxToast.showToast(MyUtil.getString(R.string.getLocation_fail));
    }


    /**
     * @author   :yinxiaolong
     * @describe : 定位 危险权限的申请
     * @data  :2022/5/30 16:51
     */
    public void permissionGet(){
        PermissionX.init(SearchActivity.this)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                    @Override
                    public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList, boolean beforeRequest) {
                        scope.showRequestReasonDialog(deniedList,MyUtil.getString(R.string.permission_must),"我已明白");
                    }
                }).onForwardToSettings(new ForwardToSettingsCallback() {
            @Override
            public void onForwardToSettings(@NonNull ForwardScope scope, @NonNull List<String> deniedList) {
                scope.showForwardToSettingsDialog(deniedList,MyUtil.getString(R.string.permission_hand),MyUtil.getString(R.string.i_know));
            }
        }).request(new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                if (allGranted){
                    MyBaiduLocation.getInstance().getLocation();
                }else {
                    RxToast.normal(MyUtil.getString(R.string.deny));
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MyBaiduLocation.getInstance().getLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onGetLocation(String cityName){
        if (Locale.getDefault()== Locale.CHINA) {
            getBinding().locationCity.setText(cityName);
        }else {
           presenter.changeLocationLang(cityName);
        }
    }
    @Override
    public void setLocationText(String cityName){
        getBinding().locationCity.setText(cityName);
    }
}


