/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.badou.mworking.R;
import com.badou.mworking.base.BaseBackActionBarActivity;
import com.badou.mworking.database.EMChatResManager;
import com.badou.mworking.domain.EMChatCreateGroupUseCase;
import com.badou.mworking.entity.emchat.Department;
import com.badou.mworking.entity.emchat.EMChatEntity;
import com.badou.mworking.entity.emchat.Role;
import com.badou.mworking.entity.user.UserInfo;
import com.badou.mworking.net.BaseSubscriber;
import com.badou.mworking.util.SPHelper;
import com.badou.mworking.util.ToastUtil;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chatuidemo.adapter.MessageAdapter;
import com.easemob.chatuidemo.adapter.PickContactsAdapter;
import com.easemob.chatuidemo.adapter.PickContactsAutoCompleteAdapter;
import com.badou.mworking.entity.emchat.User;
import com.easemob.chatuidemo.widget.Sidebar;
import com.easemob.exceptions.EaseMobException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class GroupPickContactsActivity extends BaseBackActionBarActivity {
    private StickyListHeadersListView listView;
    /**
     * 是否为一个新建的群组
     */
    protected boolean isCreatingNewGroup;

    private PickContactsAdapter contactAdapter;
    /**
     * group中一开始就有的成员
     */
    private List<String> exitingMembers;
    private CheckBox selectedFilterCheckBox;
    private TextView selectedNumberTextView;
    private ImageView selectedAllImageView;
    private TextView selectedAllTextView;
    private boolean isAllSelected = false;
    private SwipeRefreshLayout swipeRefreshLayout;

    ImageButton clearSearch;
    boolean isTargeted = false;
    AutoCompleteTextView query;

    List<Department> departments;
    List<Role> roles;
    List<User> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_pick_contacts);
        setActionbarTitle(R.string.title_name_emchat_contact);


        listView = (StickyListHeadersListView) findViewById(R.id.list);
        ((Sidebar) findViewById(R.id.sidebar)).setListView(listView);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getAdapter().getItem(position);
                if (!exitingMembers.contains(user.getUsername())) {
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                    checkBox.toggle();
                }

            }
        });

        initSearch();
        initHeader();

        setRightText(R.string.emchat_contact_title_right, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<String> members = contactAdapter.getToBeAddMembers();
                if (exitingMembers.size() > 0) {
                    save(members.toArray(new String[members.size()]));
                } else if (members.size() > 0) {
                    createGroup(members);
                } else {
                    ToastUtil.showToast(mContext, R.string.group_member_empty);
                }
            }
        });

        departments = EMChatResManager.getDepartments();
        roles = EMChatResManager.getRoles();
        contacts = EMChatResManager.getContacts();
        Map<Long, Department> departmentMap = new HashMap<>(departments.size());
        for (Department department : departments) {
            departmentMap.put(department.getId(), department);
        }
        Map<Integer, Role> roleMap = new HashMap<>(roles.size());
        for (Role role : roles) {
            roleMap.put(role.getId(), role);
        }

        String groupId = getIntent().getStringExtra("groupId");
        if (groupId == null) {// 创建群组
            isCreatingNewGroup = true;
        } else {
            // 获取此群组的成员列表
            EMGroup group = EMGroupManager.getInstance().getGroup(groupId);
            exitingMembers = group.getMembers();
        }
        if (exitingMembers == null)
            exitingMembers = new ArrayList<>();
        for (int ii = 0; ii < contacts.size(); ii++) {
            if (contacts.get(ii).getUsername().equals(EMChatManager.getInstance().getCurrentUser()) || exitingMembers.contains(contacts.get(ii).getUsername())) {
                contacts.remove(ii);
                ii--;
            } else {
                contacts.get(ii).setTag(departmentMap, roleMap);
            }
        }

        initContactList(contacts);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MainActivity.initContactsFromServer(mContext, new MainActivity.OnUpdatingListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete() {
                        SPHelper.setContactLastUpdateTime(mContext, Calendar.getInstance().getTimeInMillis());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void initHeader() {
        selectedFilterCheckBox = (CheckBox) findViewById(R.id.filter_selected);
        selectedFilterCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                contactAdapter.setSelected(b);
                if (b && contactAdapter.getCount() > 0) {
                    setAllSelectedStatus(1);
                } else {
                    setAllSelectedStatus(contactAdapter.getAllSelectedStatus());
                }
            }
        });
        selectedNumberTextView = (TextView) findViewById(R.id.selected_number);
        selectedNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedFilterCheckBox.toggle();
            }
        });
        selectedAllImageView = (ImageView) findViewById(R.id.select_all);
        selectedAllTextView = (TextView) findViewById(R.id.select_text);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllSelected = !isAllSelected;
                contactAdapter.selectAll(isAllSelected);
                setAllSelectedStatus(isAllSelected ? 1 : -1);
            }
        };
        selectedAllImageView.setOnClickListener(onClickListener);
        selectedAllTextView.setOnClickListener(onClickListener);
        setSelectedNumber(0);
    }

    private void setAllSelectedStatus(int status) {
        switch (status) {
            case -1:
                isAllSelected = false;
                selectedAllImageView.setImageResource(R.drawable.dx_checkbox_off);
                selectedAllTextView.setText(R.string.select_all);
                break;
            case 0:
                isAllSelected = false;
                selectedAllImageView.setImageResource(R.drawable.dx_checkbox_half);
                selectedAllTextView.setText(R.string.select_all);
                break;
            case 1:
                isAllSelected = true;
                selectedAllImageView.setImageResource(R.drawable.dx_checkbox_on);
                selectedAllTextView.setText(R.string.unselect_all);
                break;
        }
    }

    private void setSelectedNumber(int number) {
        String numberStr = number + "";
        SpannableString spannableString = new SpannableString(String.format(getString(R.string.filter_selected), number));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_blue)), 2, 2 + numberStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        selectedNumberTextView.setText(spannableString);
    }

    private void initSearch() {
        //搜索框
        query = (AutoCompleteTextView) findViewById(R.id.query);
        query.setHint(R.string.search);
        query.setThreshold(0);
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });
    }

    void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void initContactList(final List<User> contacts) {
        query.setAdapter(new PickContactsAutoCompleteAdapter(mContext, contacts, departments, roles, new ArrayList<>()));
        query.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isTargeted = true;
                Object object = adapterView.getAdapter().getItem(i);
                if (object instanceof Role) {
                    Role role = (Role) object;
                    contactAdapter.setFilter(role, PickContactsAdapter.TYPE_ROLE);
                    query.setText(role.getName());
                } else if (object instanceof Department) {
                    Department department = (Department) object;
                    contactAdapter.setFilter(department, PickContactsAdapter.TYPE_DEPARTMENT);
                    query.setText(department.getName());
                } else if (object instanceof User) {
                    User user = (User) object;
                    contactAdapter.setFilter(user, PickContactsAdapter.TYPE_USER);
                    query.setText(user.getNick());
                }
            }
        });
        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                handler.removeCallbacks(searchRunnable);
                if (!isTargeted) {
                    if (editable.length() == 0) {
                        contactAdapter.setFilter(null, PickContactsAdapter.TYPE_ALL);
                    } else {
                        clearSearch.setVisibility(View.VISIBLE);
                        handler.postDelayed(searchRunnable, 1000);
                    }
                }
                isTargeted = false;
                selectedFilterCheckBox.setChecked(false);
            }
        });

        // 对list进行排序
        Collections.sort(contacts, new Comparator<User>() {
            @Override
            public int compare(User lhs, User rhs) {
                int headerResult = lhs.getHeader().compareTo(rhs.getHeader());
                if(headerResult == 0) { // 先按首字母比较，若相同，则按nick比较
                    return (lhs.getNick().compareTo(rhs.getNick()));
                }else{
                    return headerResult;
                }
            }
        });

        contactAdapter = new PickContactsAdapter(this, contacts, exitingMembers);
        listView.setAdapter(contactAdapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(query.getWindowToken(), 0);
                return false;
            }
        });
        contactAdapter.setOnSelectedCountChangeListener(new PickContactsAdapter.OnSelectedCountChangeListener() {
            @Override
            public void onSelectedCountChange(int count) {
                setSelectedNumber(count);
                setAllSelectedStatus(contactAdapter.getAllSelectedStatus());
            }
        });
        contactAdapter.setOnDataSetChangedListener(new PickContactsAdapter.OnDataSetChangedListener() {
            @Override
            public void onDataSetChanged() {
                if (contactAdapter.getCount() > 0) {
                    findViewById(R.id.none_result_view).setVisibility(View.GONE);
                } else {
                    if (selectedFilterCheckBox.isChecked()) {

                    } else {
                        findViewById(R.id.none_result_view).setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        // 隐藏软键盘
        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mActivity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (mActivity.getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    Handler handler = new Handler();

    Runnable searchRunnable = new Runnable() {
        @Override
        public void run() {
            contactAdapter.setFilter(query.getText().toString(), PickContactsAdapter.TYPE_TAG);
        }
    };

    public void save(final String[] members) {
        setResult(RESULT_OK, new Intent().putExtra("newmembers", members));
        finish();
    }

    private void createGroup(final List<String> members) {
        String st1 = getResources().getString(R.string.Is_to_create_a_group_chat);
        final String st2 = getResources().getString(R.string.Failed_to_create_groups);
        //新建群组
        mProgressDialog.setMessage(st1);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        final String name = UserInfo.getUserInfo().getName();
        String groupName = name + "发起的聊天";
        new EMChatCreateGroupUseCase(groupName, groupName, "欢迎信息", members).execute(new BaseSubscriber<EMChatCreateGroupUseCase.Response>(mContext) {
            @Override
            public void onResponseSuccess(final EMChatCreateGroupUseCase.Response data) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String groupId = data.getGroupid();
                        try {
                            EMGroup group = EMGroupManager.getInstance().getGroupFromServer(groupId);
                            EMGroupManager.getInstance().createOrUpdateLocalGroup(group);
                            //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
                            EMConversation conversation = EMChatManager.getInstance().getConversation(groupId);
                            //创建一条文本消息
                            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                            //如果是群聊，设置chattype,默认是单聊
                            message.setChatType(EMMessage.ChatType.GroupChat);
                            message.setAttribute(MessageAdapter.KEY_HELLO_MESSAGE, "1");
                            //设置消息body
                            StringBuilder body = new StringBuilder(name);
                            body.append("邀请了");
                            for (String member : members) {
                                body.append(EMChatEntity.getUserNick(member));
                                body.append("、");
                            }
                            body.deleteCharAt(body.length() - 1);
                            TextMessageBody txtBody = new TextMessageBody(body.toString());
                            message.addBody(txtBody);
                            //设置接收人
                            message.setReceipt(groupId);
                            //把消息加入到此会话对象中
                            conversation.addMessage(message);
                            //发送消息
                            EMChatManager.getInstance().sendMessage(message, null);
                        } catch (EaseMobException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(ChatActivity.getGroupIntent(mContext, groupId));
                                finish();
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mProgressDialog.dismiss();
            }
        });


/*        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用sdk创建群组方法
                String name = ((AppApplication) mContext.getApplicationContext()).getUserInfo().name;
                String groupName = name + "发起的聊天";
                String desc = "";
                try {
                    EMGroup emGroup = EMGroupManager.getInstance().createPrivateGroup(groupName, desc, members, true, 200);
                    //获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
                    EMConversation conversation = EMChatManager.getInstance().getConversation(emGroup.getGroupId());
                    //创建一条文本消息
                    EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                    //如果是群聊，设置chattype,默认是单聊
                    message.setChatType(EMMessage.ChatType.GroupChat);
                    message.setAttribute(MessageAdapter.KEY_HELLO_MESSAGE, "1");
                    //设置消息body
                    StringBuilder body = new StringBuilder(name);
                    body.append("邀请了");
                    for (int ii = 0; ii < members.length; ii++) {
                        User user = AppApplication.getInstance().getContactList().get(members[ii]);
                        body.append(user == null ? members[ii] : user.getNick());
                        body.append("、");
                    }
                    body.deleteCharAt(body.length() - 1);
                    TextMessageBody txtBody = new TextMessageBody(body.toString());
                    message.addBody(txtBody);
                    //设置接收人
                    message.setReceipt(emGroup.getGroupId());
                    //把消息加入到此会话对象中
                    conversation.addMessage(message);
                    //发送消息
                    EMChatManager.getInstance().sendMessage(message, null);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mProgressDialog.dismiss();
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                } catch (final EaseMobException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mProgressDialog.dismiss();
                            Toast.makeText(mContext, st2 + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();*/
    }
}