/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.pojo;

import lombok.Data;

/**
 * @author legendshop
 */
@Data
public class HttpResult {
	private int status;
	private String body;
	private String error;

	public HttpResult() {
	}

	public HttpResult(int status, String body, String error) {
		this.status = status;
		this.body = body;
		this.error = error;
	}
}
