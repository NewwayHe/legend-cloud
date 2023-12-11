/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.enums;


import com.legendshop.common.core.enums.IntegerEnum;

/**
 * 退款及退货的状态
 *
 * @author legendshop
 */
public enum OrderRefundReturnStatusEnum implements IntegerEnum {


	/* 商家拒绝申请 */
	REJECTED_APPLY(-3),

	/* 用户超时未发货，系统取消售后 */
	CANCEL_APPLY(-2),

	/* 用户取消售后 */
	UNDO_APPLY(-1),

/** ------ 卖家处理状态 ------ */

	/**
	 * 卖家待审核状态
	 */
	SELLER_WAIT_AUDIT(0),
	/**
	 * 卖家同意
	 */
	SELLER_AGREE(1),
	/**
	 * 卖家不同意
	 */
	SELLER_DISAGREE(-1),
	/**
	 * 撤回
	 **/
	SELLER_WITHDRAW(2),
/** ------ 申请状态 ------ */

	/**
	 * 待卖家处理
	 **/
	APPLY_WAIT_SELLER(1),
	/**
	 * 待管理员处理
	 **/
	APPLY_WAIT_ADMIN(2),
	/**
	 * 已完成
	 **/
	APPLY_FINISH(3),
	/**
	 * 已取消
	 **/
	APPLY_CANCEL(-1),
	/**
	 * 平台拒绝
	 **/
	APPLY_ADMIN_REJECT(-4),

/** ------ 物流状态 ------ */

	/**
	 * 待发货
	 */
	LOGISTICS_WAIT_DELIVER(1),
	/**
	 * 待签收
	 */
	LOGISTICS_RECEIVING(3),
	/**
	 * 待收货
	 */
	LOGISTICS_WAIT_RECEIVE(5),
	/**
	 * 未收到  此状态目前不用
	 */
	LOGISTICS_UNRECEIVED(6),
	/**
	 * 已收货
	 */
	LOGISTICS_RECEIVED(7),

/** ------ 订单的退款退货状态 ------ */
	/**
	 * 退款撤销
	 */
	ORDER_UNDO_APPLY(-1),
	/**
	 * 默认,代表没有发起退款,退货
	 */
	ORDER_NO_REFUND(0),
	/**
	 * 处理中 1
	 */
	ORDER_REFUND_PROCESSING(1),
	/**
	 * 处理完成 2
	 */
	ORDER_REFUND_FINISH(2),

/** ------ 订单项的退款退货状态 ------ */
	/**
	 * 退款撤销
	 */
	ITEM_UNDO_APPLY(-1),
	/**
	 * 未发起退款
	 */
	ITEM_NO_REFUND(0),
	/**
	 * 处理中
	 */
	ITEM_REFUND_PROCESSING(1),
	/**
	 * 处理完成
	 */
	ITEM_REFUND_FINISH(2),
	/**
	 * 已结束
	 **/
	ITEM_REFUND_OVER(3),


/**------- 退款单 handle_success_status 的处理退款状态: 0:退款处理中 1:退款成功 -1:退款失败----------**/
	/**
	 * 处理中
	 */
	HANDLE_PROCESSS(0),

	/**
	 * 退款成功
	 */
	HANDLE_SUCCESS(1),

	/***退款失败**/
	HANDLE_FAIL(-1),

/** ------ 订金的退款退货状态 ------ */
	/**
	 * 不需要退订金，无需处理
	 **/
	DEPOSIT_NO_REFUND(0),

	/**
	 * 需要退订金，处理中
	 */
	DEPOSIT_REFUND_PROCESSING(1),

	/**
	 * 订金退款成功
	 */
	DEPOSIT_REFUND_FINISH(2),

	/**
	 * 订金退款失败
	 */
	DEPOSIT_REFUND_FALSE(-2);

	private Integer status;

	OrderRefundReturnStatusEnum(Integer status) {
		this.status = status;
	}

	@Override
	public Integer value() {
		return status;
	}

}
