package com.badou.mworking.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.badou.mworking.R;
import com.badou.mworking.base.MyBaseAdapter;
import com.badou.mworking.entity.category.Category;
import com.badou.mworking.entity.category.CategoryDetail;
import com.badou.mworking.entity.category.Plan;
import com.badou.mworking.util.GsonUtil;
import com.badou.mworking.util.TimeTransfer;

/**
 * Created by badou1 on 2015/7/29.
 */
public class PlanAdapter extends MyBaseAdapter<Category> {


    public PlanAdapter(Context context){
        super(context);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        ViewHolder holder;
        Plan plan=(Plan)getItem(position);
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.adapter_plan_item,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        int size=mContext.getResources().getDimensionPixelSize(R.dimen.offset_lless);
        if(position==0){
            convertView.setPadding(0,size,0,0);
        }else{
            convertView.setPadding(0,0,0,0);
        }

        // 图标资源，默认为已读
        int iconResId=R.drawable.plan_default;
        holder.iconImageView.setImageResource(iconResId);

        holder.unreadTextView.setTextColor(mContext.getResources().getColor(R.color.color_text_grey));
       //holder.unreadTextView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        switch (plan.getRead()) {
            case 0:
                holder.unreadTextView.setText(R.string.category_expired);
                break;
            case 1:
                holder.unreadTextView.setText("已完成");
        }

        if(plan.isTop()){
            holder.topImageView.setVisibility(View.VISIBLE);
        }else{
            holder.topImageView.setVisibility(View.INVISIBLE);
        }
        holder.subjectTextView.setText(plan.getSubject());//现在这些都没有？
       // holder.dateTextView.setText("++"+categoryDetail.getPlan()+"2"+"#");
       holder.dateTextView.setText(plan.getStage()+"X"+plan.getType());//階段計劃描述
      //  plan.getType()为什么没有值
       // holder.unreadTextView.setText("2"+plan.getPlanNow());//顯示過期時間*/

       holder.unreadTextView.setText("剩余:天结束");

        //holder.dateTextView.setText(TimeTransfer.long2StringDetailDate(mContext, plan.getTime()));
        return convertView;
    }

    static class ViewHolder {
        TextView subjectTextView;
        TextView dateTextView;
        ImageView iconImageView;
        TextView unreadTextView;
        ImageView topImageView;

        public ViewHolder(View view) {
            topImageView = (ImageView) view.findViewById(R.id.iv_adapter_notice_top);
            subjectTextView = (TextView) view.findViewById(R.id.tv_adapter_notice_subject);//大標題
            dateTextView = (TextView) view.findViewById(R.id.tv_adapter_notice_date);//小標題
            iconImageView = (ImageView) view.findViewById(R.id.iv_adapter_notice_icon);//圖標
            unreadTextView = (TextView) view.findViewById(R.id.tv_adapter_notice_unread);//狀態
        }
    }
}