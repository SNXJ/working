package com.badou.mworking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.badou.mworking.R;
import com.badou.mworking.base.AppApplication;
import com.badou.mworking.base.MyBaseAdapter;
import com.badou.mworking.model.Exam;
import com.badou.mworking.model.MainIcon;
import com.badou.mworking.model.Notice;
import com.badou.mworking.model.Task;
import com.badou.mworking.model.Train;
import com.badou.mworking.net.RequestParams;
import com.badou.mworking.util.SP;

import java.util.List;

/**
 * 功能描述:  主页面adapter
 */
public class MainGridAdapter extends MyBaseAdapter {

    public MainGridAdapter(Context context, List<Object> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.adapter_main_grid, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        MainIcon mainIcon = (MainIcon) getItem(position);
        holder.imageView.setTag(mainIcon.getMainIconId());
        holder.imageView.setImageResource(R.drawable.icon_default);
        int mainIconId = mainIcon.getResId();
        holder.imageView.setImageDrawable(mContext.getResources().getDrawable(mainIconId));
        holder.textView.setText(mainIcon.getName() + "");
        setIconUnreadNum(holder.tvUnreadNum, (String) holder.imageView.getTag());
        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView tvUnreadNum;

        public ViewHolder(View view) {
            imageView = (ImageView) view
                    .findViewById(R.id.iv_adapter_main_grid);
            textView = (TextView) view
                    .findViewById(R.id.tv_adapter_main_name);
            tvUnreadNum = (TextView) view.findViewById(R.id.tv_main_item_unreadNum);
        }
    }

    /**
     * 显示未读数量
     *
     * @param tv  要显示的控件
     * @param tag tag名称
     */
    private void setIconUnreadNum(TextView tv, String tag) {
        int num = 0;
        // 为了解决换帐号登录的问题
        String userNum = ((AppApplication) mContext.getApplicationContext())
                .getUserInfo().getUserNumber();
        if (RequestParams.CHK_UPDATA_PIC_NOTICE.equals(tag)) {//通知公告
            num = SP.getIntSP(mContext, SP.DEFAULTCACHE, userNum + Notice.CATEGORY_KEY_UNREAD_NUM, 0);
        } else if (RequestParams.CHK_UPDATA_PIC_TRAIN.equals(tag)) {//微培训
            num = SP.getIntSP(mContext, SP.DEFAULTCACHE, userNum + Train.CATEGORY_KEY_UNREAD_NUM, 0);
        } else if (RequestParams.CHK_UPDATA_PIC_EXAM.equals(tag)) {//在线考试
            num = SP.getIntSP(mContext, SP.DEFAULTCACHE, userNum + Exam.CATEGORY_KEY_UNREAD_NUM, 0);
        } else if (RequestParams.CHK_UPDATA_PIC_TASK.equals(tag)) {//任务签到
            num = SP.getIntSP(mContext, SP.DEFAULTCACHE, userNum + Task.CATEGORY_KEY_UNREAD_NUM, 0);
        }
        //主页没有聊天模块，默认这个数量就没有了
//		else if (RequestParams.CHK_UPDATA_PIC_CHAT.equals(tag)) { //聊天
//			num = SP.getIntSP(mContext, userNum+ChattingFragment.KEY_SP_UNREAD_NUM, 0);
//		}
        if (num == 0) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(num + "");
            // 如果是两位数的话，换一个背景
            if (num > 9) {
                tv.setBackgroundResource(R.drawable.icon_chat_unread_long);
            } else {
                tv.setBackgroundResource(R.drawable.icon_chat_unread);
            }

        }
    }
}