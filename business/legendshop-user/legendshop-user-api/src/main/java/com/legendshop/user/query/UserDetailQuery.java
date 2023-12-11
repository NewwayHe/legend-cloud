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
 * 用户查询对象
 *
 * @author legendshop
 */
@Data
@Schema(description = "用户详情查询")
public class UserDetailQuery extends PageParams {

	private static final long serialVersionUID = -8493612248227344621L;
	@Schema(description = "用户Id")
	private Long userId;

	@Schema(description = "用户名称")
	private String username;

	@Schema(description = "用户手机")
	private String mobilePhone;

	@Schema(description = "用户状态")
	private Boolean lockFlag;


}
