package com.dyj.szweather.module.main.activity;

import static com.dyj.szweather.R.*;
import static com.dyj.szweather.R.id.*;
import static com.dyj.szweather.util.ActivityUtil.getIntentData;
import static com.dyj.szweather.util.ActivityUtil.getIntentSecondData;

import android.annotation.SuppressLint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.dyj.szweather.R;
import com.dyj.szweather.base.BaseActivity;
import com.dyj.szweather.bean.CityDB;
import com.dyj.szweather.databinding.MainActivityMainBinding;
import com.dyj.szweather.module.citymanager.activity.CManagerActivity;
import com.dyj.szweather.module.home.fragment.HomeFragment;
import com.dyj.szweather.module.main.adapter.MainViewPagerAdapter;
import com.dyj.szweather.module.main.animation.DepthPageTransformer;
import com.dyj.szweather.module.main.presenter.MainPresenter;
import com.dyj.szweather.module.main.view.IMainView;
import com.dyj.szweather.module.search.activity.SearchActivity;
import com.dyj.szweather.util.ActivityUtil;
import com.dyj.szweather.util.DisplayUtil;
import com.tamsiree.rxkit.view.RxToast;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseActivity<MainPresenter, MainActivityMainBinding> implements IMainView {

    private int mCurIndex = 0;

    private static final int OVER_TIME = 2000;

    List<CityDB> list = new ArrayList<>();

    String location;
    String cityName;

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
        location = getIntentData();
        cityName = getIntentSecondData();
        setSupportActionBar(getBinding().mainMenuEbook);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        String PIC_URL = "https://www.lxtlovely.top/getpic.php?rand=true";
        showPic(PIC_URL);

        list = LitePal.findAll(CityDB.class);
        if (list.isEmpty()) ActivityUtil.startActivity(SearchActivity.class,true);
    }

    /**
     * 载入数据操作
     */
    @Override
    protected void initData() {

    }

    /**
     * {@inheritDoc}
     * <p>
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        getCity();
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
            RxToast.showToast(getResources().getString(string.double_quit) + getResources().getString(string.app_name));
            mExitTime = System.currentTimeMillis();
        } else {
            ActivityUtil.closeAllActivity();
        }
    }

    private void showCity(int num){
        if (mCurIndex > list.size() - 1) {
            mCurIndex = list.size() - 1;
        }
        getBinding().llRound.removeAllViews();
        int size = DisplayUtil.dp2px(6f);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size,size);
        layoutParams.leftMargin = 8;
        layoutParams.rightMargin = 8;
        int i = 0;
        while (i < num){
            View view = new View(this);
            view.setBackgroundResource(drawable.main_roll_icon);
            view.setEnabled(false);
            getBinding().llRound.addView(view,layoutParams);
            i++;
        }
        getBinding().llRound.getChildAt(mCurIndex).setEnabled(true);
        if (list.size() <= 1) getBinding().llRound.setVisibility(View.GONE);
        else getBinding().llRound.setVisibility(View.VISIBLE);
    }



    @Override
    public void getCity() {
        list.clear();
        list = LitePal.findAll(CityDB.class);
        List<HomeFragment> homeFragmentList = new ArrayList<>();
        int i = 0;
        for (CityDB city : list){
            HomeFragment homeFragment = new HomeFragment(city.getLocation(),city.getCityName());
            homeFragmentList.add(homeFragment);
            if (city.getCityName().equals(cityName)) mCurIndex = i;
            i++;
        }
        mainViewPagerAdapter = new MainViewPagerAdapter(this,homeFragmentList);
        getBinding().vpMain.setPageTransformer(new DepthPageTransformer());
        getBinding().vpMain.setAdapter(mainViewPagerAdapter);
        getBinding().vpMain.setCurrentItem(mCurIndex);
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
                //更新城市名
                getBinding().tvCity.setText(list.get(mCurIndex).getCityName());
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
        getBinding().tvCity.setText(list.get(mCurIndex).getCityName());
    }

    @Override
    public void hideCity() {
        getBinding().tvCity.setVisibility(View.GONE);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     *
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     *
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     *
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     *
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toorbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case menu_main_search:
                ActivityUtil.startActivity(SearchActivity.class,false);
                break;
            case menu_main_cityamanage:
                ActivityUtil.startActivity(CManagerActivity.class,false);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}