package com.dyj.szweather.module.search.activity;

import static com.dyj.szweather.util.ActivityUtil.actionSecondStart;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.didichuxing.doraemonkit.util.ToastUtils;
import com.dyj.szweather.base.BaseActivity;
import com.dyj.szweather.base.BaseBean;
import com.dyj.szweather.bean.CityDB;
import com.dyj.szweather.bean.CitySearch;
import com.dyj.szweather.bean.PopularCity;
import com.dyj.szweather.databinding.ActivitySearchBinding;
import com.dyj.szweather.module.main.activity.MainActivity;
import com.dyj.szweather.module.search.adapter.PopAdapter;
import com.dyj.szweather.module.search.baiduMap.MyBaiduLocation;
import com.dyj.szweather.module.search.presenter.SearchPresenter;
import com.dyj.szweather.module.search.view.ISearchView;
import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.List;
/**
 * @author   :yinxiaolong
 * @describe :
 * @data  :2022/5/30 16:46
 */
public class SearchActivity extends BaseActivity<SearchPresenter, ActivitySearchBinding> implements ISearchView {
    private ArrayAdapter<String> searchListAdapter;
    private List<String> list;
    private BaseBean<List<CitySearch>> o;
    private final MyBaiduLocation myBaiduLocation = new MyBaiduLocation();

    @Override
    protected SearchPresenter createPresenter() {
        list = new ArrayList<>();
        return new SearchPresenter(this, getBinding().cityAddEdittextSearch, list);//传入list 进行初始化
    }

    @Override
    protected void initView() {
        presenter = createPresenter();
        getBinding().cityAddEdittextSearch.addTextChangedListener(presenter);//加入edittext 监听
        myBaiduLocation.permissionGet(this);//获取权限 -->在回调方法中进行定位
        myBaiduLocation.getLocation(this);
        getBinding().locationCity.setOnClickListener(v -> myBaiduLocation.getLocation(this));
        getBinding().cityAddBtnSearch.setOnClickListener(v -> setToDefault());
        getBinding().cityAddBtnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void initData() {
        searchListAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_dropdown_item_1line, list);
        getBinding().listview.setAdapter(searchListAdapter);
        getBinding().listview.setOnItemClickListener((parent, view, position, id) -> {
            //获取到 数据存入数据库
            if (presenter.check()) {
                addToDB(position);
            }
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
        if (presenter.check()){
        List<CityDB> cityDBList = LitePal.where("location==?", o.location.get(position).getId()).find(CityDB.class);
        if (cityDBList == null || cityDBList.size() == 0) {
            CityDB cityDB = new CityDB();
            cityDB.setLocation(o.location.get(position).getId());
            cityDB.setCityName(o.location.get(position).getName());
            cityDB.setCityAdm2(o.location.get(position).getAdm2());
            cityDB.save();
            finish();
            // TODO: 2022/6/2 把MainActivity 改为 要跳转到的首页activity
            actionSecondStart(MainActivity.class, cityDB.getLocation(), cityDB.getCityName());
        } else {
            Toast.makeText(this, "城市以存在请勿重复添加", Toast.LENGTH_SHORT).show();
        }
        }else {
            Toast.makeText(this, "城市数量上限", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "城市以存在请勿重复添加", Toast.LENGTH_SHORT).show();
        }
        }else {
            Toast.makeText(this, "城市数量上限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showSearchError() {
        ToastUtils.showShort("城市不存在");
    }

    @Override
    public void showGetLocationError() {
        ToastUtils.showShort("定位失败");
    }


    /**
     * @author   :yinxiaolong
     * @describe : 定位 危险权限的申请
     * @data  :2022/5/30 16:51
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }
                myBaiduLocation.getLocation(this);
            } else {
                Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}