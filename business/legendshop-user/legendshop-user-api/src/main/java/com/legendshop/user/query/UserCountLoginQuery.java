/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Schema(description = "用户登录历史查询")
public class UserCountLoginQuery extends PageParams {

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private String userId;

	/**
	 * 用户昵称
	 */
	@Schema(description = "用户昵称")
	private String nickName;

	/**
	 * 开始时间
	 */
	@Schema(description = "时间")
	private String startDate;

	/**
	 * 结束时间
	 */
	@Schema(description = "时间")
	private String endDate;

}
