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

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "操作日志查询")
public class UserOperationLogQuery extends PageParams implements Serializable {

	private static final long serialVersionUID = -2519406893182987104L;

	@Schema(description = "用户手机号")
	private String mobile;

	@Schema(description = "用户名")
	private String username;

	@Schema(description = "按钮编码")
	private String buttonCode;

	@Schema(description = "页面编码")
	private String pageCode;

	@Schema(description = "操作开始时间")
	private Date startTime;

	@Schema(description = "操作结束时间")
	private Date endTime;

}
