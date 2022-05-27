package com.dyj.szweather.module.main.activity;

import com.dyj.szweather.base.BaseActivity;
import com.dyj.szweather.common.GlobalConstant;
import com.dyj.szweather.databinding.ActivityMainBinding;
import com.dyj.szweather.module.home.animation.ZoomOutPageTransformer;
import com.dyj.szweather.module.main.presenter.MainPresenter;
import com.dyj.szweather.module.main.adapter.MainViewPagerAdapter;
import com.dyj.szweather.module.main.view.IMainView;
import com.dyj.szweather.util.SpUtil;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

public class MainActivity extends BaseActivity<MainPresenter, ActivityMainBinding> implements IMainView {

    private MainViewPagerAdapter mainViewPagerAdapter;
    /**
     * 初始化presenter，也是与Activity的绑定
     *
     * @return 返回new的Presenter层的值
     */
    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    /**
     * 载入view的一些操作
     */
    @Override
    protected void initView() {
        //沉浸式状态栏设置
        UltimateBarX.statusBarOnly(this)
                .light(true)
                .transparent()
                .apply();
        //每次开启app，设置该字段为false
        SpUtil.setBoolean(GlobalConstant.IS_LOAD,false);
        mainViewPagerAdapter = new MainViewPagerAdapter(this);
        getBinding().vpMain.setPageTransformer(new ZoomOutPageTransformer());
        getBinding().vpMain.setAdapter(mainViewPagerAdapter);
    }

    /**
     * 载入数据操作
     */
    @Override
    protected void initData() {

    }


    /**
     * 返回时候回到 0 位置
     */
    @Override
    public void onBackPressed() {
        if (getBinding().vpMain.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            getBinding().vpMain.setCurrentItem(getBinding().vpMain.getCurrentItem() - 1);
        }
    }

}