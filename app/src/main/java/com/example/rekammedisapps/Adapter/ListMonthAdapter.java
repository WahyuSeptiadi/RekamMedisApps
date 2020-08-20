package com.example.rekammedisapps.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rekammedisapps.Model.MonthModel;
import com.example.rekammedisapps.R;

import java.util.ArrayList;

public class ListMonthAdapter extends RecyclerView.Adapter<ListMonthAdapter.ViewHolder> {
    private Activity mActivity;
    private ArrayList<MonthModel> monthModelArrayList;

    public ListMonthAdapter(Activity mActivity, ArrayList<MonthModel> monthModelArrayList){
        this.mActivity = mActivity;
        this.monthModelArrayList = monthModelArrayList;
    }

    @NonNull
    @Override
    public ListMonthAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_list_month_calendar, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListMonthAdapter.ViewHolder holder, int position) {
        MonthModel monthModel = monthModelArrayList.get(position);

        holder.et_bulan.setText(String.valueOf(monthModel.getBulan()));
        holder.et_tahun.setText(String.valueOf(monthModel.getTahun()));
    }

    @Override
    public int getItemCount() {
        return monthModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView et_bulan, et_tahun;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            et_bulan = itemView.findViewById(R.id.tv_lmc_bulan);
            et_tahun = itemView.findViewById(R.id.tv_lmc_tahun);
        }
    }
}
