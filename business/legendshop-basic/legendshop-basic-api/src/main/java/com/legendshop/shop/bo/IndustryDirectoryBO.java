/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 行业目录(IndustryDirectory)BO
 *
 * @author legendshop
 * @since 2021-03-09 13:53:14
 */
@Data
public class IndustryDirectoryBO implements Serializable {

	private static final long serialVersionUID = -66773579323417672L;

	private Long id;

	/**
	 * 不能重复
	 */
	@Schema(description = "不能重复")
	private String name;

	private BigDecimal seq;

	private Date creteTime;

}
