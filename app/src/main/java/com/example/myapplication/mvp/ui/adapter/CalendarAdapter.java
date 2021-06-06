package com.example.myapplication.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.myapplication.R;
import com.example.myapplication.mvp.model.entity.MemoRandumBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

import java.util.List;



public class CalendarAdapter extends BaseQuickAdapter<MemoRandumBean, BaseViewHolder> {

    Context mContext ;

    public CalendarAdapter(@Nullable List<MemoRandumBean> data, Context context) {
        super(R.layout.calendar_list_layout, data);
        mContext = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, MemoRandumBean memoRandumBean) {
        baseViewHolder.setText(R.id.cal_title,memoRandumBean.getTitle());
        baseViewHolder.setText(R.id.cal_place,memoRandumBean.getPlace());
        baseViewHolder.setText(R.id.cal_remark,memoRandumBean.getRemark());
        baseViewHolder.setText(R.id.cal_start_time,memoRandumBean.getStartTime());
        baseViewHolder.setText(R.id.cal_end_time,memoRandumBean.getEndTime());
        ImageView  imageView = baseViewHolder.getView(R.id.calendar_img);
        if(memoRandumBean.getCondition() == 0){
            imageView.setImageResource(R.mipmap.unimprotant);
        }else {
            imageView.setImageResource(R.mipmap.important);
        }
        TextView delete = baseViewHolder.getView(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClick.delete(memoRandumBean);
            }
        });
    }

    OnItemClick mOnItemClick ;

    public void setOnItemClick(OnItemClick onItemClick){
        this.mOnItemClick = onItemClick;
    }


    public interface OnItemClick {

        void delete(MemoRandumBean memoRandumBean);

    }
}
