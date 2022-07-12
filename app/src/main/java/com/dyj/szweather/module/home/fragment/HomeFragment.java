package com.dyj.szweather.module.home.fragment;

import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dyj.szweather.R;
import com.dyj.szweather.base.BaseFragment;
import com.dyj.szweather.bean.AirQuality;
import com.dyj.szweather.bean.DailyFeel;
import com.dyj.szweather.bean.WeatherDay;
import com.dyj.szweather.bean.WeatherHours;
import com.dyj.szweather.bean.WeatherNow;
import com.dyj.szweather.databinding.HomeFragmentHomeBinding;
import com.dyj.szweather.databinding.HomeLayoutAirQualityBinding;
import com.dyj.szweather.databinding.HomeLayoutDailyfeelBinding;
import com.dyj.szweather.databinding.HomeLayoutFifteenBinding;
import com.dyj.szweather.databinding.HomeLayoutHoursBinding;
import com.dyj.szweather.databinding.HomeLayoutSunMoonBinding;
import com.dyj.szweather.databinding.HomeLayoutTodayBriefInfoBinding;
import com.dyj.szweather.module.home.adapter.FifteenAdapter;
import com.dyj.szweather.module.home.adapter.HoursAdapter;
import com.dyj.szweather.module.home.presenter.HomePresenter;
import com.dyj.szweather.module.home.view.IHomeView;
import com.dyj.szweather.module.main.activity.MainActivity;
import com.dyj.szweather.util.MyUtil;
import com.tamsiree.rxui.view.dialog.RxDialogSure;

import java.util.List;


/**
 * @author ：Dyj
 * @date ：Created in 2022/5/27 20:10
 * @description：HomeFragment
 * @modified By：
 * @version: 1.0
 */
public class HomeFragment extends BaseFragment<HomePresenter, HomeFragmentHomeBinding> implements IHomeView {

    private HomeLayoutAirQualityBinding airQualityBinding ;

    private HomeLayoutDailyfeelBinding dailyfeelBinding;

    private HomeLayoutFifteenBinding fifteenBinding;

    private HomeLayoutHoursBinding hoursBinding;

    private HomeLayoutSunMoonBinding sunMoonBinding;

    private HomeLayoutTodayBriefInfoBinding todayBriefInfoBinding;

    HoursAdapter hoursAdapter;

    FifteenAdapter fifteenAdapter;





    private final String location;

    private  String cityName;

    public HomeFragment(String location) {
        this.location = location;
    }

    public HomeFragment(String location, String cityName) {
        this.location = location;
        this.cityName = cityName;
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
        airQualityBinding = HomeLayoutAirQualityBinding.bind(getBinding().getRoot());
        dailyfeelBinding = HomeLayoutDailyfeelBinding.bind(getBinding().getRoot());
        fifteenBinding = HomeLayoutFifteenBinding.bind(getBinding().getRoot());
        hoursBinding = HomeLayoutHoursBinding.bind(getBinding().getRoot());
        sunMoonBinding = HomeLayoutSunMoonBinding.bind(getBinding().getRoot());
        todayBriefInfoBinding = HomeLayoutTodayBriefInfoBinding.bind(getBinding().getRoot());
        presenter.getWeatherNow(location);
        presenter.getWeatherHours(location);
        presenter.getWeatherDay(location);
        presenter.getAirQuality(location);
        presenter.getDailyFeel(location);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        getBinding().refreshLayout.setOnRefreshListener( v -> {
            presenter.refresh(location);
        });

    }



    @Override
    public void showWeatherNow(WeatherNow now) {
        getBinding().tvTemp.setText(now.getTemp());
        getBinding().tvCity.setText(cityName);
        getBinding().tvCond.setText(now.getText());
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null){
            activity.showTime(MyUtil.split(now.getObsTime()));
            getBinding().nslHome.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    // 可以先判断ScrollView中的mView是不是在屏幕中可见
                    Rect scrollBounds = new Rect();
                    getBinding().nslHome.getHitRect(scrollBounds);
                    if (!getBinding().tvCity.getLocalVisibleRect(scrollBounds)) {
                        activity.showCity();
                    }else {
                        activity.hideCity();
                    }
                }
            });
        }

        todayBriefInfoBinding.tvFeelTemp.setText(String.format("%s°C", now.getFeelsLike()));
        todayBriefInfoBinding.tvHumidity.setText(String.format("%s%%", now.getHumidity()));
        if (MyUtil.getNowLanguage().equals("en")) {
            todayBriefInfoBinding.tvWindScale.setText(String.format("%s%s", now.getWindDir(), now.getWindScale()));
        }else {
            todayBriefInfoBinding.tvWindScale.setText(String.format("%s%s级", now.getWindDir(), now.getWindScale()));
        }
        todayBriefInfoBinding.tvPressure.setText(String.format("%shpa", now.getPressure()));
    }

    @Override
    public void showWeatherDay(List<WeatherDay> daily) {
        int min = Integer.parseInt(daily.get(0).getTempMin());
        int max = Integer.parseInt(daily.get(0).getTempMax()) ;
        for (WeatherDay day : daily){
            min = Math.min(Integer.parseInt(day.getTempMin()),min);
            max = Math.max(Integer.parseInt(day.getTempMax()),max);
        }
        fifteenAdapter = new FifteenAdapter(R.layout.item_forecast15,daily);
        fifteenAdapter.setRange(min,max);
        fifteenBinding.rvForecast15.setAdapter(fifteenAdapter);
        fifteenBinding.rvForecast15.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        WeatherDay today = daily.get(0);
        String  currentTime = MyUtil.getNowTime();
        sunMoonBinding.sunView.setTimes(today.getSunrise(),today.getSunset(),currentTime);
        sunMoonBinding.moonView.setTimes(today.getMoonrise(),today.getMoonset(),currentTime);
        sunMoonBinding.tvMoonPhrase.setText(today.getMoonPhase());
    }

    @Override
    public void showWeatherHours(List<WeatherHours> hourly) {
        hoursAdapter = new HoursAdapter(R.layout.item_hours_forecast,hourly);
        hoursBinding.rvForecastHours.setAdapter(hoursAdapter);
        hoursBinding.rvForecastHours.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
    }

    @Override
    public void showAirQuality(AirQuality now) {
        airQualityBinding.airConditionView.setValue(Integer.parseInt(now.getAqi()),now.getCategory());
        airQualityBinding.tvTodayCo.setText(now.getCo());
        airQualityBinding.tvTodayNo2.setText(now.getNo2());
        airQualityBinding.tvTodayO3.setText(now.getO3());
        airQualityBinding.tvTodayPm10.setText(now.getPm10());
        airQualityBinding.tvTodayPm25.setText(now.getPm2p5());
        airQualityBinding.tvTodaySo2.setText(now.getSo2());
    }

    @Override
    public void showDailyFeel(List<DailyFeel> daily) {
        RxDialogSure rxDialogSure = new RxDialogSure(requireContext());
        rxDialogSure.setLogo(R.drawable.ic_logo);
        rxDialogSure.setSureListener(v1 -> rxDialogSure.cancel());

        dailyfeelBinding.imgHealth.setOnClickListener(v->{
            rxDialogSure.setContent(daily.get(1).getText());
            rxDialogSure.show();
        });
        dailyfeelBinding.imgWear.setOnClickListener(v->{
            rxDialogSure.setContent(daily.get(0).getText());
            rxDialogSure.show();
        });
        dailyfeelBinding.imgMakeup.setOnClickListener(v->{
            rxDialogSure.setContent(daily.get(2).getText());
            rxDialogSure.show();
        });
        dailyfeelBinding.tvHealthValue.setText(daily.get(1).getCategory());
        dailyfeelBinding.tvWearValue.setText(daily.get(0).getCategory());
        dailyfeelBinding.tvMakeupValue.setText(daily.get(2).getCategory());
        dailyfeelBinding.tvMakeupValue.setSelected(true);
    }

    @Override
    public void showSunRise(String sunrise, String sunset) {

    }

    @Override
    public void showMoonRise(String moonrise, String moonset) {

    }

    @Override
    public void closeRefresh() {
        getBinding().refreshLayout.finishRefresh(1000);
    }
}
