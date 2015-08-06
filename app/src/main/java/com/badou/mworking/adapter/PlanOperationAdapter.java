package com.badou.mworking.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.badou.mworking.R;
import com.badou.mworking.base.MyBaseAdapter;
import com.badou.mworking.entity.category.CategoryDetail;
import com.badou.mworking.entity.category.EntryOperation;
import com.badou.mworking.entity.category.Plan;
import com.badou.mworking.entity.category.PlanOperation;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by badou1 on 2015/7/30.
 */
public class PlanOperationAdapter extends MyBaseAdapter<PlanOperation> {
    public PlanOperationAdapter(Context context) {
        super(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_entry_operation_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CategoryDetail categoryDetail = getItem(position).getCategoryDetail();
           CategoryDetail.Config config=categoryDetail.getPlan().getconfig();
        holder.mIndexTextView.setText("这是测试");
        holder.mSubjectTextView.setText("dhntru8uygyugy");


        holder.mIndexTextView.setText("第"+config.getStages()+"阶段");

        System.out.print("+++++config.getStages()+++++++" + config.getStages());
        System.out.print("+++++categoryDetail.getSubject()+++++++" +categoryDetail.getSubject());
        System.out.print("+++++config.getDesc()+++++++" +config.getDesc());

        holder.mSubjectTextView.setText(categoryDetail.getSubject());
        holder.mContentTextView.setText(categoryDetail.getFmt());
      //  holder.mContentTextView.setText(config.getSubject()+"XX"+config.getDesc()+"plan"+categoryDetail.getPlan());

        //判断
        holder.mCheckImageView.setImageResource(R.drawable.url);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.index_text_view)
        TextView mIndexTextView;
        @Bind(R.id.subject_text_view)
        TextView mSubjectTextView;
        @Bind(R.id.content_text_view)
        TextView mContentTextView;
        @Bind(R.id.check_image_view)
        ImageView mCheckImageView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
