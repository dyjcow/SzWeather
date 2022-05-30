package com.dyj.szweather.module.citymanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dyj.szweather.R;
import com.dyj.szweather.bean.CityDB;
import com.dyj.szweather.databinding.ItemCityBinding;

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
                mItemClickListener.onItemClick(v);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.MyViewHolder holder, int position) {

        CityDB city = list.get(position);
        holder.cityName.setText(city.getCityName()+"-"+city.getLocation());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
//        1.数据交换，2.刷新
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

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView cityName;
        public MyViewHolder(ItemCityBinding binding,@NonNull View itemView) {

            super(itemView);
            cityName = binding.itemNameTv;
        }
    }
}
