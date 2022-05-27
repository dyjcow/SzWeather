package com.dyj.szweather.module.home.fragment;

import com.bumptech.glide.Glide;
import com.dyj.szweather.base.BaseFragment;
import com.dyj.szweather.bean.PictureGirl;
import com.dyj.szweather.databinding.FragmentHomeBinding;
import com.dyj.szweather.module.home.presenter.HomePresenter;
import com.dyj.szweather.module.home.view.IHomeView;
import com.dyj.szweather.util.SpUtil;
import com.dyj.szweather.util.ToastUtil;

import java.util.List;

/**
 * @author ：Dyj
 * @date ：Created in 2022/5/27 20:10
 * @description：HomeFragment
 * @modified By：
 * @version: 1.0
 */
public class HomeFragment extends BaseFragment<HomePresenter, FragmentHomeBinding> implements IHomeView {
    private int position;

    public HomeFragment(int position) {
        this.position = position;
    }


    /**
     * 创建 presenter
     *
     * @return presenter
     */
    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }

    /**
     * 初始化布局
     */
    @Override
    protected void initView() {
        presenter.getPIc(position);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

    }


    @Override
    public void showPic(String url) {
        Glide.with(this).load(url).into(getBinding().imgBc);
    }
}
