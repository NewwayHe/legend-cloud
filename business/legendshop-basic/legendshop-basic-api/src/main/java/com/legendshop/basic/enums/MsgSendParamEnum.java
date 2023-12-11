/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.enums;

import com.legendshop.common.core.enums.StringEnum;
import lombok.AllArgsConstructor;

/**
 * 消息模板替换data
 *
 * @author legendshop
 */
@AllArgsConstructor
public enum MsgSendParamEnum implements StringEnum {

	/**
	 * 商品名称
	 */
	@Deprecated
	PROD_NAME("prodName"),

	/**
	 * 商品名称
	 */
	PRODUCT_NAME("productName"),

	/**
	 * 退款订单编号
	 */
	REFUND_SN("refundSn"),

	/**
	 * 订单编号
	 */
	ORDER_NUMBER("orderNumber"),

	/**
	 * 原因
	 */
	REASON("reason"),

	/**
	 * 反馈内容
	 */
	FEEDBACK_CONTENT("feedbackContent"),

	/**
	 * 反馈内容回复
	 */
	FEEDBACK_REPLY_CONTENT("feedbackReplyContent"),

	/**
	 * 文章标题
	 */
	ARTICLE_TITLE("articleTitle"),

	/**
	 * 活动名称
	 */
	ACTIVITY_NAME("activityName"),

	/**
	 * 活动类型
	 */
	ACTIVITY_TYPE("activityType"),

	/**
	 * 状态
	 */
	STATUS("status"),

	/**
	 * 内容
	 */
	CONTENT("content"),

	/**
	 * 订单ID
	 */
	ORDER_ID("orderId"),

	/**
	 * 首部内容（用于微信公众号）
	 */
	FIRST("first"),

	/**
	 * 参数1（用于微信公众号）
	 */
	KEYWORD1("keyword1"),

	/**
	 * 参数2（用于微信公众号）
	 */
	KEYWORD2("keyword2"),

	/**
	 * 参数3（用于微信公众号）
	 */
	KEYWORD3("keyword3"),

	/**
	 * 参数4（用于微信公众号）
	 */
	KEYWORD4("keyword4"),

	/**
	 * 参数5（用于微信公众号）
	 */
	KEYWORD5("keyword5"),

	/**
	 * 备注
	 */
	REMARK("remark"),

	/**
	 * 店铺名称
	 */
	SHOP_NAME("shopName"),

	/**
	 * 商家未处理订单数量
	 */
	UNTREATED_ORDERS("untreatedOrders"),

	/**
	 * 商家未处理售后订单数量
	 */
	UNTREATED_ORDER_REFUNDS("untreatedOrderRefunds"),
	;

	private String value;

	@Override
	public String value() {
		return value;
	}
}
