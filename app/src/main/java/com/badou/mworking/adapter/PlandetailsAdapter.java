package com.badou.mworking.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.badou.mworking.R;
import com.badou.mworking.base.MyBaseAdapter;
import com.badou.mworking.entity.category.Category;
import com.badou.mworking.entity.category.CategoryDetail;
import com.badou.mworking.entity.category.PlanOperation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by badou1 on 2015/7/31.
 */
public class PlandetailsAdapter extends MyBaseAdapter<PlanOperation> {

    CategoryDetail categoryDetail;
    public PlandetailsAdapter( CategoryDetail categoryDetail,Context context) {
        super(context);
        this.categoryDetail = categoryDetail;
    }

    @Override
    public int getCount() {
        return categoryDetail.getPlan().getconfig().getStages().size();
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
       // CategoryDetail categoryDetail = getItem(position).getCategoryDetail();
        int index=position+1;
        holder.indexTextView.setText("第" + index + "阶段");
        String subject=categoryDetail.getPlan().getconfig().getStages().get(position).getSubject();
        holder.tvTitle.setText(subject);

      List<CategoryDetail.Stage> stages= categoryDetail.getPlan().getconfig().getStages();
        for(CategoryDetail.Stage stage:stages){
           System.out.print( stage.getSubject());
        }
       // holder.tvState.setText(categoryDetail.getPlan().getconfig().getDesc());
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

