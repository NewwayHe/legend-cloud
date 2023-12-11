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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Schema(description = "第三方通行证查询")
public class PassportItemQuery {


	@Schema(description = "请求来源")
	private VisitSourceEnum source;

	@Schema(description = "第三方用户标识:(openId,aliId)")
	private String thirdPartyIdentifier;


}
