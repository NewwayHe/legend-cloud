/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.query;


import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 银行编码(BankCode)Query分页查询对象
 *
 * @author legendshop
 * @since 2021-04-07 09:56:30
 */
@Data
public class BankCodeQuery extends PageParams {

	private static final long serialVersionUID = -86531196358045913L;

	@Schema(description = "总行名称")
	private String name;

	@Schema(description = "总行编码")
	private String code;
}
