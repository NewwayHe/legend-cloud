/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.contant;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-07-15 17:43
 */
public class ApiInfoConstant {
	/**
	 * 查询url
	 */
	public static final String QUERY_URL = "https://poll.kuaidi100.com/poll/query.do";
	/**
	 * 订阅url
	 */
	public static final String SUBSCRIBE_URL = "https://poll.kuaidi100.com/poll";
	/**
	 * 订阅SCHEMA
	 */
	public static final String SUBSCRIBE_SCHEMA = "json";
	/**
	 * 智能单号识别url
	 */
	public static final String AUTO_NUM_URL = "http://www.kuaidi100.com/autonumber/auto?num=%s&key=%s";
	/**
	 * 电子面单html url
	 */
	public static final String ELECTRONIC_ORDER_HTML_URL = "http://poll.kuaidi100.com/eorderapi.do";
	/**
	 * 电子面单html方法
	 */
	public static final String ELECTRONIC_ORDER_HTML_METHOD = "getElecOrder";
	/**
	 * 电子面单获取图片 url
	 */
	public static final String ELECTRONIC_ORDER_PIC_URL = "https://poll.kuaidi100.com/printapi/printtask.do";
	/**
	 * 电子面单获取图片
	 */
	public static final String ELECTRONIC_ORDER_PIC_METHOD = "getPrintImg";
	/**
	 * 电子面单打印 url
	 */
	public static final String ELECTRONIC_ORDER_PRINT_URL = "https://poll.kuaidi100.com/printapi/printtask.do";
	/**
	 * 电子面单打印方法
	 */
	public static final String ELECTRONIC_ORDER_PRINT_METHOD = "eOrder";
	/**
	 * 菜鸟淘宝账号授权
	 */
	public static final String AUTH_THIRD_URL = "https://poll.kuaidi100.com/printapi/authThird.do";
	/**
	 * 云打印url
	 */
	public static final String CLOUD_PRINT_URL = "http://poll.kuaidi100.com/printapi/printtask.do?method=%s&t=%s&key=%s&sign=%s&param=%s";
	/**
	 * 自定义打印方法
	 */
	public static final String CLOUD_PRINT_CUSTOM_METHOD = "printOrder";
	/**
	 * 附件打印方法
	 */
	public static final String CLOUD_PRINT_ATTACHMENT_METHOD = "imgOrder";
	/**
	 * 复打方法
	 */
	public static final String CLOUD_PRINT_OLD_METHOD = "printOld";
	/**
	 * 复打方法
	 */
	public static final String SEND_SMS_URL = "http://apisms.kuaidi100.com:9502/sms/send.do";
	/**
	 * 商家寄件
	 */
	public static final String B_ORDER_URL = "https://order.kuaidi100.com/order/borderbestapi.do";
	/**
	 * 商家寄件查询运力
	 */
	public static final String B_ORDER_QUERY_TRANSPORT_CAPACITY_METHOD = "querymkt";
	/**
	 * 商家寄件下单
	 */
	public static final String B_ORDER_SEND_METHOD = "bOrderBest";
	/**
	 * 商家寄件获取验证码
	 */
	public static final String B_ORDER_CODE_METHOD = "getCode";
	/**
	 * 商家寄件取消
	 */
	public static final String B_ORDER_CANCEL_METHOD = "cancelBest";
	/**
	 * 云平台通用请求url
	 */
	public static final String CLOUD_NORMAL_URL = "http://cloud.kuaidi100.com/api";
}
