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
 * @author legendshop
 * @version 1.0.0
 * @title ExportExcelTaskQuery
 * @date 2022/4/26 16:01
 * @description：
 */
@Data
@Schema(description = "导出Excel数据任务查询对象")
public class ExportExcelTaskQuery extends PageParams {

	private static final long serialVersionUID = -1392676582650077511L;

	@Schema(description = "用户类型(U:普通用户 S:商家 A:平台)", hidden = true)
	private String userType;

	@Schema(description = "用户ID", hidden = true)
	private Long userId;

	@Schema(description = "业务类型", hidden = true)
	private String businessType;
}
