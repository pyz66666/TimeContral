package com.example.myapplication.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.myapplication.R;
import com.example.myapplication.mvp.model.entity.AppDataBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StRecyclerViewAdapter extends BaseQuickAdapter<AppDataBean, BaseViewHolder> {


    public StRecyclerViewAdapter( @Nullable List<AppDataBean> data) {
        super(R.layout.st_recy_layout, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, AppDataBean appDataBean) {
            baseViewHolder.setImageDrawable(R.id.app_icon,appDataBean.getIcon());
            baseViewHolder.setText(R.id.use_time,appDataBean.getUseTime());
            baseViewHolder.setText(R.id.use_count,appDataBean.getUseCount());
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClick.click(appDataBean);
                }
            });
    }

    OnItemClick mOnItemClick ;

    public void setOnItemClick(OnItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    public interface OnItemClick{
        void click(AppDataBean appDataBean);
    }
}
