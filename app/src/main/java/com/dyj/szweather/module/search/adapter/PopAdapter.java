package com.dyj.szweather.module.search.adapter;



import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dyj.szweather.R;
import com.dyj.szweather.base.BaseBean;
import com.dyj.szweather.bean.CityDB;
import com.dyj.szweather.bean.PopularCity;
import com.dyj.szweather.module.search.activity.SearchActivity;
import com.google.android.material.button.MaterialButton;

import org.litepal.LitePal;
import java.util.List;

/**
 * @author yinxiaolong
 * @describe
 * @data: :
 */
public class PopAdapter extends RecyclerView.Adapter<PopAdapter.PopViewHolder> {
    private final BaseBean<List<PopularCity>> popCity;
    private final SearchActivity activity;

    public PopAdapter(BaseBean<List<PopularCity>> popCity,SearchActivity activity) {
        this.popCity =popCity;
        this.activity=activity;
    }

    @NonNull
    @Override
    public PopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.popcity_item,parent,false);
        return new PopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopViewHolder holder, @SuppressLint("RecyclerView") int position) {
    holder.button.setText(popCity.topCityList.get(position).getName());
    if (LitePal.where("location==?",popCity.topCityList.get(position).getId()).find(CityDB.class)==null||LitePal.where("location==?",popCity.topCityList.get(position).getId()).find(CityDB.class).size()!=0)
    {
        holder.button.setSelected(true);
    }
    holder.button.setOnClickListener(v -> activity.addPopCityToDB(position,popCity));
    }

    @Override
    public int getItemCount() {
        return popCity.topCityList.size();
    }

    public static class PopViewHolder extends RecyclerView.ViewHolder {
        MaterialButton button;
        public PopViewHolder(@NonNull View itemView) {
            super(itemView);
            button=itemView.findViewById(R.id.button);
        }
    }

}
