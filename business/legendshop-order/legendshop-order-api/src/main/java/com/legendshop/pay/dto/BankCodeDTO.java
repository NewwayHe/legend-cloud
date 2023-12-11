/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 银行编码(BankCode)DTO
 *
 * @author legendshop
 * @since 2021-04-07 09:56:30
 */
@Data
@Schema(description = "银行编码DTO")
public class BankCodeDTO implements Serializable {

	private static final long serialVersionUID = 509571174130915719L;

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
