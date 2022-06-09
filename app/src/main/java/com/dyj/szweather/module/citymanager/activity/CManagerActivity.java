package com.dyj.szweather.module.citymanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.ims.RegistrationManager;

import com.dyj.szweather.R;
import com.dyj.szweather.base.BaseActivity;
import com.dyj.szweather.bean.CityDB;
import com.dyj.szweather.databinding.ActivityCmanagerBinding;
import com.dyj.szweather.module.citymanager.adapter.CityAdapter;
import com.dyj.szweather.module.citymanager.adapter.MyItemTouchHelperCallback;
import com.dyj.szweather.module.citymanager.presenter.CManagerPresenter;
import com.dyj.szweather.module.citymanager.view.ICManagerView;
import com.dyj.szweather.module.main.activity.MainActivity;
import com.dyj.szweather.module.search.activity.SearchActivity;
import com.dyj.szweather.util.ActivityUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class CManagerActivity extends BaseActivity<CManagerPresenter, ActivityCmanagerBinding> implements ICManagerView{

    RecyclerView mRecyclerView;
    private CityAdapter mAdapter;

    @Override
    protected CManagerPresenter createPresenter() {
        return new CManagerPresenter(this);
    }

    @Override
    protected void initView() {
        mRecyclerView = getBinding().cityRv;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {
        getBinding().cityFabAdd.setOnClickListener(v -> ActivityUtil.startActivity(SearchActivity.class));
        getBinding().cityIvBack.setOnClickListener(v -> onBackPressed());
    }

    /**
     * 将城市列表信息传入adapter
     * @param list
     */
    @Override
    public void setCMessage(List<CityDB> list) {
        mAdapter = new CityAdapter(list);
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * 第一次启动时获取城市信息
     */
    @Override
    protected void onResume() {
        super.onResume();
        presenter.getCityList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        List<CityDB> nList = new ArrayList<>();
        for (CityDB cityDB : mAdapter.getList()){
            CityDB nCity = new CityDB();
            nCity.setLocation(cityDB.getLocation());
            nCity.setCityName(cityDB.getCityName());
            nCity.setCityAdm2(cityDB.getCityAdm2());
            nList.add(nCity);
        }
        LitePal.deleteAll(CityDB.class);
        LitePal.saveAll(nList);
        super.onPause();
    }

}