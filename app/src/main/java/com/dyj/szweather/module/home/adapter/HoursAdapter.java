package com.dyj.szweather.module.home.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dyj.szweather.R;
import com.dyj.szweather.bean.WeatherHours;
import com.dyj.szweather.databinding.HomeLayoutHoursBinding;
import com.dyj.szweather.databinding.ItemHoursForecastBinding;
import com.dyj.szweather.util.LogUtil;
import com.dyj.szweather.util.MyUtil;
import com.dyj.szweather.util.ToastUtil;
import com.dylanc.viewbinding.brvah.BaseViewHolderUtilKt;
import com.tamsiree.rxkit.view.RxToast;

import java.util.List;

/**
 * @author ：Dyj
 * @date ：Created in 2022/5/27 20:12
 * @description：HomeAdapter
 * @modified By：
 * @version: 1.0
 */
public class HoursAdapter extends BaseQuickAdapter<WeatherHours, BaseViewHolder> {

    public HoursAdapter(int layoutResId, @Nullable List<WeatherHours> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, WeatherHours weatherHours) {
        ItemHoursForecastBinding binding = BaseViewHolderUtilKt.getBinding(baseViewHolder,ItemHoursForecastBinding::bind);
        binding.tvHoursTime.setText(MyUtil.split(weatherHours.getFxTime()));
        binding.ivHours.setImageResource(MyUtil.getWeatherIcon(getContext(),weatherHours.getIcon()));
        binding.tvTemp.setText(String.format("%s℃", weatherHours.getTemp()));
    }
}
