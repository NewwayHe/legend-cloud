/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 第三方分页请求
 *
 * @author legendshop
 */
@Data
public class BasePageRequest implements Serializable {

	/**
	 * 页码
	 */
	private Integer curPage;

	/**
	 * 每页数量
	 */
	private Integer pageSize;

	public BasePageRequest() {
		this.curPage = 1;
		this.pageSize = 20;
	}

}
