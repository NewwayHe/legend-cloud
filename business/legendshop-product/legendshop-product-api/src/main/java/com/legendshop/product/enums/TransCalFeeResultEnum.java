/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.enums;

/**
 * 运费计算异常返回
 *
 * @author legendshop
 */
public enum TransCalFeeResultEnum {
	/**
	 * 区域限售
	 */
	NOT_SUPPORT_AREA(-1, "不支持配送至该区域！"),

	/**
	 * 未找到计费规则
	 */
	NOT_FOUNT_RULE(-2, "未找到对应的计费规则！"),

	/**
	 * 计费方式配置不正确
	 */
	ERROR_RULE_SET(-3, "计费方式配置不正确！"),

	/**
	 * 运费模板不存在
	 */
	NOT_EXIT_TRANS_PORT(-4, "运费模板不存在！"),

	/**
	 * 空dto
	 */
	NULL_DTO(-5, "运费计算DTO不能为空！"),


	/**
	 * cityId为空
	 */
	NULL_CITY(-6, "请选择配送区域");

	private int code;

	private Object data;

	private String msg;

	TransCalFeeResultEnum(int code, Object data, String msg) {
		this.code = code;
		this.data = data;
		this.msg = msg;
	}

	TransCalFeeResultEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public Object getData() {
		return data;
	}

	public String getMsg() {
		return msg;
	}
}
