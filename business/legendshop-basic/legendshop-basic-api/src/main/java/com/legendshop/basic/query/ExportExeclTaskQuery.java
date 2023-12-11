/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 导出详情记录表分页查询对象
 *
 * @author legendshop
 * @since 2021-12-14 19:10:01
 */
@Data
public class ExportExeclTaskQuery extends PageParams {

	private static final long serialVersionUID = 229070405188866913L;

	@Schema(description = "业务名称")
	private String businessName;

	@Schema(description = "导出用户Id")
	private Long userId;

}
