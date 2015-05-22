/* 
 * 文件名: ChatInfoActivity.java
 * 包路径: com.badou.mworking
 * 创建描述  
 *        创建人：葛建锋
 *        创建日期：2014年9月18日 下午3:47:22
 *        内容描述：
 * 修改描述  
 *        修改人：葛建锋 
 *        修改日期：2014年9月18日 下午3:47:22 
 *        修改内容:
 * 版本: V1.0   
 */
package com.badou.mworking;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.badou.mworking.adapter.ChatInfoAdapter;
import com.badou.mworking.base.AppApplication;
import com.badou.mworking.base.BaseNoTitleActivity;
import com.badou.mworking.model.ChatInfo;
import com.badou.mworking.net.Net;
import com.badou.mworking.net.ServiceProvider;
import com.badou.mworking.net.volley.VolleyListener;
import com.badou.mworking.util.SP;
import com.badou.mworking.util.ToastUtil;
import com.badou.mworking.widget.SwipeBackLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 类: <code> ChatInfoActivity </code> 功能描述: 聊天页面 创建人: 葛建锋 创建日期: 2014年9月18日
 * 下午3:47:22 开发环境: JDK7.0
 */
public class ChatInfoActivity extends BaseNoTitleActivity implements
		OnClickListener {

	private TextView submitTv; // 提交按钮
	private EditText contentEt; // 内容输入框

	private PullToRefreshListView pullListView;
	private String whom = "";
	private String img = "";
	private String myHeadImgUrl = "";
	private ArrayList<ChatInfo> chatInfoList;
	private ChatInfoAdapter mAdapter;
	public static final String KEY_whom = "whom";
	public static final String KEY_img = "img";
	public static final String KEY_NAME = "name";
	
	private SwipeBackLayout layout;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.chatinfoactivity);
		//页面滑动关闭
		layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(R.layout.base, null);
		layout.attachToActivity(this);
		whom = getIntent().getStringExtra(KEY_whom);
		img = getIntent().getStringExtra(KEY_img);
		chatInfoList = new ArrayList<ChatInfo>();
		try {
			myHeadImgUrl = SP.getStringSP(mContext,SP.DEFAULTCACHE, ((AppApplication) mContext
					.getApplicationContext()).getUserInfo().getUserId(), "");
		} catch (Exception e) {
			myHeadImgUrl = "";
			e.printStackTrace();
		}
		initWiget();
		initAction(this);

		initListener();
		pullListView.setRefreshing();
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void setAdapterData() {
		if (mAdapter == null) {
			mAdapter = new ChatInfoAdapter(mContext, chatInfoList, whom, img,
					myHeadImgUrl);
			pullListView.setAdapter(mAdapter);
		}
	}

	/**
	 * 功能描述: 控件初始化
	 */
	private void initWiget() {
		submitTv = (TextView) findViewById(R.id.tv_comment_submit);
		contentEt = (EditText) findViewById(R.id.et_comment_content);
		contentEt.setHint("我来说两句...");
		pullListView = (PullToRefreshListView) findViewById(R.id.chat_lv);
		pullListView.setMode(Mode.BOTH);
		submitTv.setOnClickListener(this);
		submitTv.setEnabled(false);
		submitTv.setBackgroundColor(getResources().getColor(R.color.color_grey));
	}

	protected void initListener() {
		pullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				ServiceProvider.dogetChatInfo(mContext, whom,
						new VolleyListener(mContext) {

							@Override
							public void onResponse(Object responseObject) {
								if (pullListView != null) {
									pullListView.onRefreshComplete();
								}
								JSONObject responseJson = (JSONObject) responseObject;
								int code = responseJson.optInt(Net.CODE);
								if (code==Net.LOGOUT) {
									AppApplication.logoutShow(mContext);
									return;
								}
								if (Net.SUCCESS != code) {
									ToastUtil.showNetExc(mContext);
									return;
								}
								JSONArray arrJson = responseJson
										.optJSONArray(Net.DATA);
								chatInfoList = new ArrayList<ChatInfo>();
								try {
									for (int i = 0; i < arrJson.length(); i++) {

										String jo = (String) arrJson.get(i);
										JSONObject jsonObject = new JSONObject(
												jo);
										chatInfoList.add(new ChatInfo(
												jsonObject));

									}
								} catch (JSONException e) {
									System.out.println(e.toString());
								}
								
								setAdapterData();
								mAdapter.setdata(chatInfoList);
								mAdapter.notifyDataSetChanged();
								/** 发送成功后滚动到底部 **/
								ListView lv = pullListView.getRefreshableView();
								lv.setSelection(lv.getBottom());
							}

							@Override
							public void onErrorResponse(VolleyError error) {
								if (pullListView != null) {
									pullListView.onRefreshComplete();
								}
								super.onErrorResponse(error);
							}
						});
			}
		});

		// 字符改变监听
		contentEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// 文本改变监听
				int length = contentEt.getText().toString().trim().length();

				if (length == 0) {
					submitTv.setEnabled(false);
					submitTv.setBackgroundColor(getResources().getColor(
							R.color.color_grey));
				} else {
					submitTv.setEnabled(true);
					submitTv.setBackgroundResource(R.drawable.comment_send_blue);
				}

			}
		});
	}

	/**
	 * c初始化action 布局
	 * 
	 * @param onclick
	 */
	private void initAction(OnClickListener onclick) {
		ImageView ivLeft = (ImageView) this
				.findViewById(R.id.iv_actionbar_left);
		TextView tvTitle = (TextView) this
				.findViewById(R.id.txt_actionbar_title);
		ImageView ivRight = (ImageView) this
				.findViewById(R.id.iv_actionbar_right);
		ivLeft.setOnClickListener(onclick);
		ivLeft.setImageResource(R.drawable.title_bar_back_normal);
		tvTitle.setText(getIntent().getStringExtra(KEY_NAME) + "");
		ivRight.setOnClickListener(onclick);
		ivRight.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_comment_submit:
			submitChatMsg();
			// 隐藏输入法
			InputMethodManager imm = (InputMethodManager) getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			// 显示或者隐藏输入法
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			break;

		case R.id.iv_actionbar_left:
			finish();
			break;
		default:
			break;
		}
	}

	private void submitChatMsg() {
		final String msg = contentEt.getText().toString().trim();
		contentEt.setText("");

		ServiceProvider.doSendChat(mContext, msg, whom, new VolleyListener(
				mContext) {

			@Override
			public void onResponse(Object responseObject) {
				JSONObject responseJson = (JSONObject) responseObject;
				int code = responseJson.optInt(Net.CODE);
				if (code==Net.LOGOUT) {
					AppApplication.logoutShow(mContext);
					return;
				}
				if (Net.SUCCESS != code) {
					ToastUtil.showToast(mContext, R.string.send_fail);
					return;
				}

				ChatInfo chatInfo = new ChatInfo();
				chatInfo.setContent(msg);
				chatInfo.setTs(System.currentTimeMillis() / 1000);
				chatInfoList.add(chatInfo);
				mAdapter.notifyDataSetChanged();
				/** 发送成功后滚动到底部 **/
				ListView lv = pullListView.getRefreshableView();
				lv.setSelection(lv.getBottom());
			}

			@Override
			public void onErrorResponse(VolleyError error) {
				super.onErrorResponse(error);
			}
		});
	}

}