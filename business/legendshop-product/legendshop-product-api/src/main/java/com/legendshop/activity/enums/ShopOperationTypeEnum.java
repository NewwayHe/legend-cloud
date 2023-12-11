/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.enums;

/**
 * 商家营销活动操作类型
 *
 * @author legendshop
 */
public enum ShopOperationTypeEnum {

	/**
	 * 发布
	 */
	SAVE("发布"),

	/**
	 * 修改
	 */
	UPDATE("修改"),

	/**
	 * 结束
	 */
	END("结束"),
	/**
	 * 审核失败
	 */
	AUDIT_FILED("审核失败"),

	/**
	 * 到期
	 */
	EXPIRE("到期"),

	/**
	 * 下线
	 */
	OFFLINE("下线"),

	/**
	 * 删除
	 */
	DELETE("删除"),

	/**
	 * 退回
	 */
	SEND_BACK("退回"),

	/**
	 * 支付成功
	 */
	PAY_SUCCESS("支付成功"),
	;

	private String type;

	ShopOperationTypeEnum(String type) {
		this.type = type;
	}

	public String value() {
		return type;
	}

}
