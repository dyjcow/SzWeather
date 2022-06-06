package com.dyj.szweather.module.main.activity;

import android.view.View;
import android.widget.LinearLayout;

import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.dyj.szweather.R;
import com.dyj.szweather.base.BaseActivity;
import com.dyj.szweather.bean.CityDB;
import com.dyj.szweather.databinding.MainActivityMainBinding;
import com.dyj.szweather.module.home.fragment.HomeFragment;
import com.dyj.szweather.module.main.adapter.MainViewPagerAdapter;
import com.dyj.szweather.module.main.animation.DepthPageTransformer;
import com.dyj.szweather.module.main.presenter.MainPresenter;
import com.dyj.szweather.module.main.view.IMainView;
import com.dyj.szweather.util.ActivityUtil;
import com.dyj.szweather.util.DisplayUtil;
import com.tamsiree.rxkit.view.RxToast;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<MainPresenter, MainActivityMainBinding> implements IMainView {

    private int mCurIndex = 0;

    private static final int OVER_TIME = 2000;

    List<CityDB> list = new ArrayList<>();

    /**
     * 保存用户按返回键的时间
     */
    private long mExitTime = 0;


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

        String PIC_URL = "https://www.lxtlovely.top/getpic.php?rand=true";
        showPic(PIC_URL);

        CityDB cityDB = new CityDB();
        cityDB.setCityAdm2("北京");
        cityDB.setLocation("101010100");

        for (int i = 0;i < 10;i++){
            list.add(cityDB);
        }
        getCity(list);
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
//        if (getBinding().vpMain.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed();
//        } else {
//            // Otherwise, select the previous step.
//            getBinding().vpMain.setCurrentItem(getBinding().vpMain.getCurrentItem() - 1);
//        }
        if ((System.currentTimeMillis() - mExitTime) > OVER_TIME) {
            RxToast.showToast(getResources().getString(R.string.double_quit) + getResources().getString(R.string.app_name));
            mExitTime = System.currentTimeMillis();
        } else {
            ActivityUtil.closeAllActivity();
        }
    }

    private void showCity(int num){

        getBinding().llRound.removeAllViews();
        int size = DisplayUtil.dp2px(6f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size,size);
        layoutParams.leftMargin = 8;
        layoutParams.rightMargin = 8;
        int i = 0;
        while (i < num){
            View view = new View(this);
            view.setBackgroundResource(R.drawable.main_roll_icon);
            view.setEnabled(false);
            getBinding().llRound.addView(view,layoutParams);
            i++;
        }
        getBinding().llRound.getChildAt(mCurIndex).setEnabled(true);
    }



    @Override
    public void getCity(List<CityDB> list) {
        List<HomeFragment> homeFragmentList = new ArrayList<>();
        for (CityDB city : list){
            HomeFragment homeFragment = new HomeFragment(city.getLocation(),city.getCityAdm2());
            homeFragmentList.add(homeFragment);
        }
        mainViewPagerAdapter = new MainViewPagerAdapter(this,homeFragmentList);
        getBinding().vpMain.setPageTransformer(new DepthPageTransformer());
        getBinding().vpMain.setAdapter(mainViewPagerAdapter);
        getBinding().vpMain.setCurrentItem(mCurIndex);
//        getBinding().vpMain.setOffscreenPageLimit(2);
        getBinding().vpMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            /**
             * This method will be invoked when a new page becomes selected. Animation is not
             * necessarily complete.
             *
             * @param position Position index of the new selected page.
             */
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                getBinding().llRound.getChildAt(mCurIndex).setEnabled(false);
                getBinding().llRound.getChildAt(position).setEnabled(true);
                mCurIndex = position;
            }
        });
        showCity(list.size());
    }

    @Override
    public void showPic(String url) {
        Glide.with(this).load(url).into(getBinding().imgBc);
    }

    @Override
    public void showTime(String time) {
        getBinding().tvTime.setText(time);
    }

    @Override
    public void showCity() {
        getBinding().tvCity.setVisibility(View.VISIBLE);
        getBinding().tvCity.setText(list.get(mCurIndex).getCityAdm2());
    }

    @Override
    public void hideCity() {
        getBinding().tvCity.setVisibility(View.GONE);
    }
}