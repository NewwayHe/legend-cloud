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
 * 短信查询DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "短信查询DTO")
public class SmsLogQuery extends PageParams {

	private static final long serialVersionUID = 4575308453707573866L;

	@Schema(description = "电话号码")
	private String mobilePhone;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "发送时间")
	private String createTime;

	@Schema(description = "渠道")
	private String channelType;

}
