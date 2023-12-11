/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.query;

import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.user.enums.PassType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Schema(description = "第三方通行证查询")
public class PassportQuery {

	@Schema(description = "用户Id")
	private Long userId;

	@Schema(description = "通行证类型")
	private PassType passType;

	@Schema(description = "请求来源")
	private VisitSourceEnum source;

}
