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
@Schema(description = "短信历史查询")
public class UserCountSmsQuery extends PageParams {

	@Schema(description = "用户id")
	private Long userId;

	@Schema(description = "用户id")
	private String nickName;

	@Schema(description = "手机号码")
	private String mobile;

	@Schema(description = "发送状态，-1：失败，1：成功，0：全部")
	private Integer status;

	@Schema(description = "开始时间")
	private String startDate;

	@Schema(description = "结束时间")
	private String endDate;

	private String success;

	private String fail;

}
