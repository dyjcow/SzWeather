package com.dyj.szweather.module.citymanager.adapter;

import android.content.ContentValues;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dyj.szweather.R;
import com.dyj.szweather.bean.CityDB;
import com.dyj.szweather.databinding.ItemCityBinding;
import com.dyj.szweather.module.main.activity.MainActivity;
import com.dyj.szweather.util.ActivityUtil;
import com.dyj.szweather.util.LogUtil;
import com.dyj.szweather.util.ToastUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: yzy
 * @date: 2022/5/28
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> implements ItemTouchMoveListener{
    private List<CityDB> list;

    public CityAdapter(List<CityDB> list){
        this.list = list;
    }
    public ItemClickListener mItemClickListener;
    @NonNull
    @Override
    public CityAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCityBinding binding = ItemCityBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        MyViewHolder holder = new MyViewHolder(binding,binding.getRoot());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtil.actionSecondStart(MainActivity.class,list.get(holder.getLayoutPosition()).getLocation(),list.get(holder.getLayoutPosition()).getCityName());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.MyViewHolder holder, int position) {

        CityDB city = list.get(position);
        holder.cityName.setText(String.format("%s-%s", city.getCityName(), city.getCityAdm2()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(list, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public boolean onItemRemove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        return true;
    }

    /**
     * 更新列表到数据库
     */
    @Override
    public void refreshList() {
        LitePal.deleteAll(CityDB.class);
        LitePal.saveAll(list);
    }

    public List<CityDB> getList() {
        return list;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView cityName;
        public MyViewHolder(ItemCityBinding binding,@NonNull View itemView) {

            super(itemView);
            cityName = binding.itemNameTv;
        }
    }
}
