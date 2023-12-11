/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.enums;

/**
 * @author legendshop
 */
public enum BaseStatusEnum {
	SUCCESS(1, Boolean.TRUE, "正常"),
	FAIL(0, Boolean.FALSE, "错误"),

	;

	private Integer code;

	private Boolean status;

	private String description;

	BaseStatusEnum(Integer code, Boolean status, String description) {
		this.code = code;
		this.status = status;
		this.description = description;
	}

	public Integer code() {
		return code;
	}

	public Boolean status() {
		return status;
	}

	public String description() {
		return description;
	}

}
