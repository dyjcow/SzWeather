package com.dyj.szweather.module.home.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dyj.szweather.R;
import com.dyj.szweather.bean.WeatherDay;
import com.dyj.szweather.databinding.ItemForecast15Binding;
import com.dyj.szweather.util.MyUtil;
import com.dylanc.viewbinding.brvah.BaseViewHolderUtilKt;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author ：Dyj
 * @date ：Created in 2022/6/8 19:26
 * @description：15天天气适配器
 * @modified By：
 * @version: 1.0
 */
public class FifteenAdapter extends BaseQuickAdapter<WeatherDay, BaseViewHolder> {

    private int mMin = 0;
    private int mMax = 0;

    List<String> week = new ArrayList<>();

    public FifteenAdapter(int layoutResId, @Nullable List<WeatherDay> data) {
        super(layoutResId, data);
        week.add(MyUtil.getString(R.string.sunday));
        week.add(MyUtil.getString(R.string.monday));
        week.add(MyUtil.getString(R.string.tuesday));
        week.add(MyUtil.getString(R.string.wednesday));
        week.add(MyUtil.getString(R.string.thursday));
        week.add(MyUtil.getString(R.string.friday));
        week.add(MyUtil.getString(R.string.saturday));
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, WeatherDay weatherDay) {
        ItemForecast15Binding binding = BaseViewHolderUtilKt.getBinding(baseViewHolder,ItemForecast15Binding::bind);
        String[] dates = weatherDay.getFxDate().split("-");
        String date = dates[1] + "-" + dates[2];
        binding.tvDate.setText(date);
        binding.tvWeek.setText(getWeekDay(weatherDay));
        binding.tvDayDesc.setText(weatherDay.getTextDay());
        binding.ivDay.setImageResource(MyUtil.getWeatherIcon(getContext(),weatherDay.getIconDay()));
        binding.ivNight.setImageResource(MyUtil.getWeatherIcon(getContext(),weatherDay.getIconNight()));
        binding.tvNightDesc.setText(weatherDay.getTextNight());
        binding.tvWind.setText(weatherDay.getWindDirDay());
        if (MyUtil.getNowLanguage().equals("en")){
            binding.tvWindScale.setText(String.format("L:%s", weatherDay.getWindScaleDay()));
        }else {
            binding.tvWindScale.setText(String.format("%s级", weatherDay.getWindScaleDay()));
        }
        binding.tempChart.setData(mMin,mMax,prevDay(weatherDay),weatherDay,nextDay(weatherDay));
    }

    private String getWeekDay(WeatherDay weatherDay){
        int position = getItemPosition(weatherDay);
        if (position == 0) return MyUtil.getString(R.string.today);
        else if (position == 1) return MyUtil.getString(R.string.tomorrow);
        else {
            String[] dateArray = weatherDay.getFxDate().split("-");
            Calendar calendar = Calendar.getInstance();
            //月份是从0开始的，所以要减去1
            calendar.set(Integer.parseInt(dateArray[0]),Integer.parseInt(dateArray[1])-1,Integer.parseInt(dateArray[2]));
            int now = calendar.get(Calendar.DAY_OF_WEEK)-1;
            if (now < 0) now = 0;
            return week.get(now);
        }
    }

    public void setRange(int mMin ,int mMax){
        this.mMin = mMin;
        this.mMax = mMax;
        notifyDataSetChanged();
    }

    private WeatherDay prevDay(WeatherDay nowDay){
        int position = getItemPosition(nowDay);
        if (getItemPosition(nowDay) == 0)
            return null;
        else  return getData().get(position - 1);
    }

    private WeatherDay nextDay(WeatherDay nowDay){
        int position = getItemPosition(nowDay);
        if (getItemPosition(nowDay) == getData().size() - 1)
            return null;
        else  return getData().get(position + 1);
    }
}
