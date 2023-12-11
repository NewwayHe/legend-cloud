/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

/**
 * 推送通知类型
 *
 * @author legendshop
 */
public enum SysParamNameEnum {


	//---------------消息推送------------------------------------------------
	/**
	 * 商品到货通知
	 */
	PROD_ARRIVAL,

	/**
	 * 商品库存预警通知
	 */
	PROD_STOCKS,
	/**
	 * 验证码
	 */
	VAL_CODE,

	/**
	 * 降价通知
	 */
	PROD_PRICE_DOWN,

	/**
	 * 订单发货通知
	 */
	CONSIGNMENT,

	/**
	 * 退货
	 */
	REFUND,

	/**
	 * 退款
	 */
	REFUND_MONEY,

	/**
	 * 店铺审核未通过
	 */
	AUDIT_FAILED,

	/**
	 * 店铺审核通过
	 */
	AUDIT_SUCCESS,

	/**
	 * 店铺下线
	 */
	SHOP_OFF,

	/**
	 * 消息推送,文章评论通知（通知用户）
	 */
	ARTICLE_COMMENT_NOTIFY_TO_USER,

	/**
	 * 消息推送,商品问答通知（通知用户）
	 */
	PROD_CONSULT_NOTIFY_TO_USER,

	/**
	 * 消息推送,商品问答通知（通知商家）
	 */
	PROD_CONSULT_NOTIFY_TO_SHOP,

	/**
	 * 消息推送,商家入驻通知（通知平台）
	 */
	SHOP_ENTER_NOTIFY_TO_ADMIN,

	/**
	 * 消息推送，商品审核（通知平台）
	 */
	PRODUCT_AUDIT_ADMIN,

	/**
	 * 消息推送,每天通知商家未处理订单和售后订单 （通知商家）
	 */
	EVERY_DAY_NOTIFY_TO_SHOP,
	//------------------订单----------------------------------------------

	/**
	 * 消息推送，订单评论删除（通知用户）
	 */
	ORDER_COMMENT_DELETE_USER,

	/**
	 * 消息推送，订单评论审核（通知用户）
	 */
	ORDER_COMMENT_AUDIT_USER,

	/**
	 * 消息推送，订单评论审核（通知商家）
	 */
	ORDER_COMMENT_AUDIT_SHOP,

	/**
	 * 消息推送，订单支付通知（通知用户）
	 */
	ORDER_PLACE_SUCCESS_TO_USER,

	/**
	 * 消息推送，订单支付成功（通知用户）
	 */
	ORDER_PAY_SUCCESS_TO_USER,
	/**
	 * 消息推送，订单评论回复通知
	 */
	ORDER_COMMENT_REPLY,

	/**
	 * 订单设置
	 */
	ORDER_SETTING,

	/**
	 * 分销设置
	 */
	DISTRIBUTION_SETTING,

	/**
	 * 消息推送，订单退款通知
	 */
	ORDER_REFUND_TO_SHOP,

	/**
	 * 消息推送，订单退款通知
	 */
	ORDER_REFUND_TO_USER,

	/**
	 * 消息推送，商家审核订单退款（通知用户）
	 */
	REFUND_AUDIT_TO_USER,

	/**
	 * 消息推送，用户重复提交售后异常(通知用户)
	 */
	REFUND_REPEAT_TO_USER,

	/**
	 * 消息推送，商家同意订单退款（通知系统）
	 */
	REFUND_PASS_TO_ADMIN,

	/**
	 * 消息推送，用户退货发货通知商家收货（通知商家）
	 */
	REFUND_SHIP_TO_SHOP,

	/**
	 * 消息推送，订单提醒发货
	 */
	ORDER_REMIND_DELIVERY,

	/**
	 * 消息推送，后台审核通过售后（通知商家）
	 */
	CONFIRM_REFUND_TO_SHOP,

	/**
	 * 消息推送，后台审核通过售后（通知用户）
	 */
	CONFIRM_REFUND_TO_USER,

	/**
	 * 消息推送，商家发货后通知用户
	 */
	ORDER_SHIP_TO_USER,


	/**
	 * 消息推送，文章审核通知（通知用户）
	 */
	ARTICLE_REVIEW_NOTIFY_TO_USER,

	/**
	 * 消息推送，后台回复用户反馈（通知用户）
	 */
	FEEDBACK_REPLY,

	/**
	 * 订单取消原因
	 */
	ORDER_CANCEL_REASON,

	/**
	 * 订单退货原因
	 */
	ORDER_REASON_GOODS,

	/**
	 * 订单退款原因
	 */
	ORDER_REFUND_REASON,

	/**
	 * 消息推送，活动审核通知（通知店铺）
	 */
	ACTIVITY_REVIEW_NOTIFY_TO_SHOP,

	/**
	 * 消息推送，预售尾款支付通知（通知用户）
	 */
	PRE_SELL_ORDER_FINAL_PAYMENT_TO_USER,

	//------------------支付宝---------------------------------------------
	ALI_PAY,

	//------------------支付宝转帐---------------------------------------------
	ALI_PAY_TRANSFER,

	//-------------------微信----------------------------------------------
	/**
	 * 微信公众平台（公众号）
	 */
	WX_MP,

	/**
	 * 微信公众平台（小程序）
	 */
	WX_MINI_PRO,

	/**
	 * 微信商户平台
	 */
	WX_PAY,

	/**
	 * 微信开放平台（网站应用）
	 */
	WX_WEB,

	/**
	 * 微信开放平台（移动应用）
	 */
	WX_APP,

	/**
	 * 微信客服二维码
	 */
	WX_CUSTOMER,

	// -------------------- 易宝支付 --------------------

	/**
	 * 易宝支付
	 */
	YEEPAY,

	/**
	 * 易宝微信
	 */
	YEEPAY_WX_PAY,

	/**
	 * 易宝支付宝
	 */
	YEEPAY_ALI_PAY,

	/**
	 * 易宝银联
	 */
	YEEPAY_UNION_PAY,


	//----------------支付--------------------------------------
	/**
	 * 模拟支付
	 */
	SIMULATE_PAY,


	/**
	 * 免付
	 */
	FREE_PAY,

	//-----------------------------------------系统配置--------------------------------


	/**
	 * 账单结算周期类型
	 */
	BILL_PERIOD_TYPE,

	/**
	 * 平台佣金比例
	 */
	BILL_COMMISSION_RATE,


	/**
	 * 审核团购活动
	 */
	ACTIVITY_GROUP_NEED_AUDIT,

	/**
	 * 物流接口设置
	 */
	LOGISTICS,

	/**
	 * 优惠券退还配置
	 */
	COUPON_REFUND_SETTING,

	/**
	 * 分销最高等级
	 */
	MAX_DISTRIBUTION_GRADE,

	/**
	 * 平台钱包设置
	 */
	WALLET_SETTING,

	/**
	 * 高德地图
	 */
	A_MAP,

	/**
	 * 文章设置
	 */
	ARTICLE_SETTING,

	/**
	 * 类目展示
	 */
	DECORATE_SETTING,

	/**
	 * 百度移动统计
	 */
	BAIDU_MOBILE_STATISTICS,

	/**
	 * 支付设置
	 */
	PAY_SETTING,

	//------------------自提点相关----------------------------------------------

	/**
	 * 自提点备货通知
	 */
	SINCE_MENTION_STOCK_UP;
}
