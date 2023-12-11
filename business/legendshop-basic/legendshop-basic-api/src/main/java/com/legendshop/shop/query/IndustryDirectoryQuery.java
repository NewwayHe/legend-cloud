/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 行业目录(IndustryDirectory)Query分页查询对象
 *
 * @author legendshop
 * @since 2021-03-09 13:53:14
 */
@Data
public class IndustryDirectoryQuery implements Serializable {

	private static final long serialVersionUID = 887523859228360568L;

	private Integer pageSize;

	private Integer curPage;

	@Schema(description = "搜索名称")
	private String name;

	/**
	 * 是否启用（1：启用，0：失效）
	 */
	@Schema(description = "是否启用（1：启用，0：失效）")
	private Boolean state;

}
