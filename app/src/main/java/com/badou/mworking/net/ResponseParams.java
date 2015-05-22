package com.badou.mworking.net;

/**
 * Created by yee on 3/7/14.
 */
public class ResponseParams {
	/**
	 * overall
	 */
	public static final String USER_ID = "uid";//用户id
	public static final String USER_ACCESS = "access";//用户权限
	public static final String USER_TAG = "tag";//用户tag
	public static final String USER_NAME = "name";//用户名称
	public static final String USER_PHONE = "phone";//手机号
	public static final String USER_DESC = "desc";//推送信息
	
	/**
	 * notice
	 */
	public static final String NOTICE_TYPE = "subtype";//类型
	/**
	 * training
	 */
	public static final String TRAIN_URL = "url";//pdf下载地址
	public static final String TRAIN_TAG = "tag";//分类
	public static final String TRAIN_FEEDBACK_COUNT = "cnt";//点赞数
	/**
	 * 课件点赞和评论
	 */
	public static final String COMMENT_NUM = "ccnt";//评论数量
	public static final String RATING_NUM = "mcnt";//点赞数量
	public static final String ECNT = "ecnt";//评分人数
	public static final String EVAL = "eval";//评分总分
	/**
	 * comment
	 */
	public static final String COMMENT_RESULT = "result";//同事圈评论
	public static final String COMMENT_USERNAME = "employee_id";//员工号
	public static final String COMMENT_CONTENT = "info";
	public static final String COMMENT_TIME = "ts";//时间戳
	/**
	 * check update
	 */
	public static final String CHECKUPDATE_NEW = "new";//是否有更新
	public static final String CHECKUPDATE_INFO = "desc";//消息
	public static final String CHECKUPDATE_URL = "url";//下载地址
	public static final String CHECKUPDATE_NEWVER = "newver";//
	public static final String CHECKUPDATE_MD5 = "md5";//md5
	

	/**
	 * task
	 * */
	public static final String TASK_URL = "url";//
	public static final String TASK_DEADTIME = "deadtime";//截止时间
	public static final String TASK_SUBTYPE = "subtype";//分类 
	public static final String TASK_TAG = "tag";//分类
	public static final String TASK_RESOURCE_TIME = "resourcetime";//
	public static final String TASK_IS_FINISHED = "isfinished";//是否完成
	public static final String TASK_DETAIL_SUBJECT = "subject";//详细标题
	public static final String TASK_DETAIL_DEPARTMENT = "department";//部门
	public static final String TASK_DETAIL_TYPE = "type";//
	public static final String TASK_DETAIL_LONGITUDE = "longitude";//经度
	public static final String TASK_DETAIL_LATITUDE = "latitude";//纬度
	public static final String TASK_DETAIL_PLACE = "place";//地点
	public static final String TASK_DETAIL_COMMENT = "comment";//内容
	public static final String TASK_DETAIL_DEADLINE = "deadline";//截止日期
	public static final String TASK_DETAIL_TASK = "task";//任务
	public static final String TASK_DETAIL_STARTLINE= "startline";//任务
	public static final String TASK_PHOTO= "photo";//任务photo
	public static final String C= "c";//是否完成
	public static final String P= "p";//签到图片的URL
	public static final String OFFLINE = "offline";//总数
	public static final String IMG = "img";

	public static final String TRAIN_READ = "read";//已读
	public static final String CONTENT = "content";//内容
	public static final String M = "m";        // m 为 1 代表已点赞， 否则没有点赞
	public static final String E = "e";        // e 课件评分
	public static final String EXAM_CONTENT = "content";//内容
	public static final String EXAM_S = "s";   //考试得分
	public static final String EXAM_T = "t";   //考试总分
	public static final String EXAM_D = "d";   //是否批阅完成


	/**
	 * question
	 * */

	public static final String QUESTION_CONTENT = "content";//内容
	public static final String QUESTION_ID = "id";//id
	public static final String QUESTION_STATUS = "status";//
	public static final String QUESTION_DEPARTMENT = "department";//部门
	public static final String QUESTION_COMPANY = "company";//企业
	public static final String QUESTION_REPLY_NO = "reply_no";//回复数
	public static final String QUESTION_UPDATE_TS = "update_ts";//更新时间
	public static final String QUESTION_QID = "qid";//评论id
	public static final String QUESTION_PUBLISH_TS = "publish_ts";//发布时间
	public static final String QUESTION_TYPE = "type";//类型
	public static final String QUESTION_EMPLOYEE_ID = "employee_id";//员工号
	public static final String QUESTION_ERRCODE = "errcode";//错误码
	public static final String QUESTION_DETAIL_ANSWER_CONTENT = "c";
	public static final String QUESTION_DETAIL_EMPLOYEE = "e";//员工号
	public static final String QUESTION_DETAIL_TIME = "t";//时间
	public static final String QUESTION_IMG_URL = "imgurl";//头像地址
	public static final String QUESTION_PIC_URL="picurl";//图片地址
	public static final String QUESTION_CREDIT_NUM="credit_no";//点赞数量
	public static final String QUESTION_CIRCLE_LV ="circle_lv";//点赞数量

	/**
	 * categorys
	 */
	public static final String CATEGORY_SUBJECT = "subject";//标题
	public static final String CATEGORY_DEPARTMENT = "department";//部门
	public static final String CATEGORY_RID = "rid";//资源id
	public static final String CATEGORY_TIME = "ts";//资源时间戳
	public static final String CATEGORY_TAG = "tag";//类别tag
	public static final String CATEGORY_NAME = "name";//名称
	public static final String CATEGORY_TOP = "top"; // 是否置顶
	public static final String CATEGORY_URL = "url";//网页地址
	public static final String CATEGORY_SUBTYPE = "subtype";//类型

	/**
	 * knowledge library
	 */
	public static final String KNOWLEDGE_LIBRARY_IMG = "img";//图片地址
	public static final String KNOWLEDGE_LIBRARY_DESC = "desc";//信息
	public static final String KNOWLEDGE_LIBRARY_URL = "url";//网页地址
	
	public static final String EXPER_IS_NEW_USER = "newuser";//是否是新注册的用户 (0:新用户;1:老用户)
	
	//===========2014-12-16=======begin===========
	public static final String NEWCNT = "newcnt";//未读数

	
	
}