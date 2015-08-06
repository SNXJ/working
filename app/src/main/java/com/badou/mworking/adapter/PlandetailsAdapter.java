package com.badou.mworking.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.badou.mworking.R;
import com.badou.mworking.base.MyBaseAdapter;
import com.badou.mworking.entity.category.Category;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by badou1 on 2015/7/31.
 */
public class PlandetailsAdapter extends MyBaseAdapter<Category> {


    public PlandetailsAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.item_plandetail_list, viewGroup, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.indexTextView.setText("第" + position+1 + "阶段");
        holder.tvTitle.setText("阶段XXXXXX");
        //��������ж�ͼ��
        holder.tvState.setText("已完成");
        return convertView;
    }
    static class ViewHolder {
        @Bind(R.id.index_text_view)
        TextView indexTextView;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_state)
        TextView tvState;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

