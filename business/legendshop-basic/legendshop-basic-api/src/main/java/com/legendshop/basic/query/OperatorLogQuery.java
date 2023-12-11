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
 * 操作日志表(OperatorLog)Query分页查询对象
 *
 * @author legendshop
 * @since 2023-08-29 14:23:30
 */
@Data
public class OperatorLogQuery extends PageParams {

	private static final long serialVersionUID = -15855642353239728L;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;

	/**
	 * 用户名称
	 */
	@Schema(description = "用户名称")
	private String userName;

	/**
	 * 用户类型：UserTypeEnum
	 */
	@Schema(description = "用户类型：UserTypeEnum")
	private String userType;

	/**
	 * 事件类型：EventTypeEnum
	 */
	@Schema(description = "事件类型：EventTypeEnum")
	private String eventType;
}
