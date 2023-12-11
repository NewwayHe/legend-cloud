/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统通知子类型
 *
 * @author legendshop
 */
@Getter
@AllArgsConstructor
public enum MsgSendTypeEnum {


	//-----------------身份验证-----------//
	/**
	 * 验证
	 */
	VAL(10, "验证", ParentMsgSendTypeEnum.OTHER),

	/**
	 * 注册验证
	 */
	REGISTER_VAL(11, "注册验证", ParentMsgSendTypeEnum.OTHER),

	/**
	 * 忘记密码
	 */
	FORGET_PASSWORD_VAL(12, "忘记密码", ParentMsgSendTypeEnum.OTHER),

	/**
	 * 绑定手机
	 */
	BIND_MOBILE_PHONE_VAL(13, "绑定手机", ParentMsgSendTypeEnum.OTHER),

	/**
	 * 修改密码
	 */
	MODIFY_LOGIN_PASSWORD_VAL(14, "修改密码", ParentMsgSendTypeEnum.OTHER),


	//-----------------订单-----------//

	/**
	 * 订单评论通知
	 */
	ORDER_COMMENT(21, "订单评论通知", ParentMsgSendTypeEnum.ORDER),

	/**
	 * 用户提醒商家 发货提醒
	 */
	PROD_REMIND_DELIVERY(31, "商品发货提醒", ParentMsgSendTypeEnum.ORDER),

	/**
	 * 订单发货通知
	 */
	ORDER_SHIP(25, "订单发货通知", ParentMsgSendTypeEnum.ORDER),

	/**
	 * 订单支付通知
	 */
	ORDER_TO_PAY(27, "订单支付通知", ParentMsgSendTypeEnum.ORDER),

	/**
	 * 订单下单通知
	 */
	ORDER_TO_PLACE(29, "订单下单通知", ParentMsgSendTypeEnum.ORDER),


	/**
	 * 订单支付微信公众号通知
	 */
	ORDER_TO_PAY_WX_MP(26, "订单支付通知", ParentMsgSendTypeEnum.ORDER),
	//-----------------售后-----------//

	/**
	 * 售后订单重复提交
	 */
	ORDER_REFUND_REPEAT(40, "售后订单重复提交", ParentMsgSendTypeEnum.AFTER_SALE),

	/**
	 * 订单退款通知
	 */
	ORDER_REFUND(41, "订单退款通知", ParentMsgSendTypeEnum.AFTER_SALE),


	//-----------------商品-----------//
	/**
	 * 到货通知
	 */
	PROD_ARRIVAL(30, "商品到货通知", ParentMsgSendTypeEnum.PRODUCT),

	PROD_AUDIT(130, "商品审核通知", ParentMsgSendTypeEnum.PRODUCT),
	/**
	 * 商品库存预警
	 */
	PROD_REMIND_STOCKS(32, "商品库存预警提醒", ParentMsgSendTypeEnum.PRODUCT),


	//-----------------系统-----------//
	/**
	 * 反馈意见回复通知
	 */
	FEEDBACK_REPLY(90, "反馈意见回复通知", ParentMsgSendTypeEnum.OTHER),
	/**
	 * 平台通知
	 */
	ADMIN_NOTIFY(91, "平台通知", ParentMsgSendTypeEnum.OTHER),

	/**
	 * 文章评论通知
	 */
	ARTICLE_COMMENT_NOTIFY(92, "文章评论通知", ParentMsgSendTypeEnum.OTHER),

	/**
	 * 文章审核通知
	 */
	ARTICLE_REVIEW_NOTIFY(95, "文章审核通知", ParentMsgSendTypeEnum.OTHER),

	/**
	 * 活动审核通知
	 */
	ACTIVITY_REVIEW_NOTIFY(97, "活动审核通知", ParentMsgSendTypeEnum.OTHER),

	/**
	 * 用户商品问答通知
	 */
	PROD_CONSULT_NOTIFY(98, "商品问答通知", ParentMsgSendTypeEnum.PRODUCT),

	/**
	 * 商家入驻通知
	 */
	SHOP_ENTER_NOTIFY(103, "商家入驻通知", ParentMsgSendTypeEnum.OTHER),

	ORDER_NOTIFY(93, "订单类型", ParentMsgSendTypeEnum.ORDER),

	/**
	 *
	 */
	AOTO_SINCE_MENTION_STOCK_UP(106, "自动备货完成", ParentMsgSendTypeEnum.ORDER),
	;

	private Integer value;
	private String name;
	private ParentMsgSendTypeEnum parentMsgSendTypeEnum;


	public static String getTypeName(Integer value) {
		for (MsgSendTypeEnum msgSendTypeEnum : MsgSendTypeEnum.values()) {
			if (msgSendTypeEnum.getValue().equals(value)) {
				return msgSendTypeEnum.getName();
			}
		}
		return null;
	}

	/**
	 * 获取父类型
	 *
	 * @param value
	 * @return
	 */
	public static Integer getParentType(Integer value) {
		for (MsgSendTypeEnum msgSendTypeEnum : MsgSendTypeEnum.values()) {
			if (msgSendTypeEnum.getValue().equals(value)) {
				return msgSendTypeEnum.getParentMsgSendTypeEnum().getValue();
			}
		}
		return null;
	}

}
