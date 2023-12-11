/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 银行编码(BankCode)BO
 *
 * @author legendshop
 * @since 2021-04-07 09:56:30
 */
@Data
public class BankCodeBO implements Serializable {

	private static final long serialVersionUID = -97308395740392638L;

	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;

	/**
	 * 总行名称
	 */
	@Schema(description = "总行名称")
	private String name;

	/**
	 * 总行简称
	 */
	@Schema(description = "总行简称")
	private String abbreviation;

	/**
	 * 总行编码
	 */
	@Schema(description = "总行编码")
	private String code;

}
