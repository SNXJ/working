package com.badou.mworking.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.badou.mworking.entity.MessageCenter;
import com.badou.mworking.entity.user.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class MessageCenterResManager {

    /**
     * 数据库中添加一条消息
     */
    public static void insertItem(Context context, MessageCenter message) {
        MTrainingDBHelper mTrainingDBHelper = MTrainingDBHelper
                .getMTrainingDBHelper();
        SQLiteDatabase dbWriter = mTrainingDBHelper.getDatabase();
        UserInfo userInfo = UserInfo.getUserInfo();
        if (userInfo == null || TextUtils.isEmpty(userInfo.getAccount()))
            return;
        String userNum = userInfo.getAccount();
        dbWriter.insert(MTrainingDBHelper.TBL_NAME_MESSAGE_CENTER + userNum.replace("@", ""), null, message.getContentValue());
        mTrainingDBHelper.closeDatabase();
    }

    public static List<MessageCenter> getAllItem() {
        MTrainingDBHelper mTrainingDBHelper = MTrainingDBHelper.getMTrainingDBHelper();
        SQLiteDatabase dbReader = mTrainingDBHelper.getDatabase();
        UserInfo userInfo = UserInfo.getUserInfo();
        if (userInfo == null || TextUtils.isEmpty(userInfo.getAccount()))
            return new ArrayList<>();
        String userNum = userInfo.getAccount();
        List<MessageCenter> messageCenterList = new ArrayList<>();
        Cursor cursor = dbReader.query(MTrainingDBHelper.TBL_NAME_MESSAGE_CENTER + userNum.replace("@", ""),
                null, null, null, null, null, MTrainingDBHelper.MESSAGE_CENTER_TS + " DESC");
        while (cursor.moveToNext()) {
            messageCenterList.add(new MessageCenter(cursor));
        }
        cursor.close();
        mTrainingDBHelper.closeDatabase();
        return messageCenterList;
    }

    public static void deleteItem(Context context, MessageCenter message) {
        MTrainingDBHelper mTrainingDBHelper = MTrainingDBHelper
                .getMTrainingDBHelper();
        SQLiteDatabase dbWriter = mTrainingDBHelper.getDatabase();
        String userNum = UserInfo.getUserInfo().getAccount();
        dbWriter.delete(MTrainingDBHelper.TBL_NAME_MESSAGE_CENTER + userNum.replace("@", ""), MTrainingDBHelper.PRIMARY_ID + "= ?", new String[]{message.getId() + ""});
        mTrainingDBHelper.closeDatabase();
    }
}
