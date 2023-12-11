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
import org.springframework.stereotype.Component;

/**
 * 用户发票搜索参数
 *
 * @author legendshop
 */
@Schema(description = "用户发票搜索参数")
@Data
@Component
public class UserInvoiceQuery extends PageParams {

	@Schema(description = "用户Id")
	private Long userId;

	@Schema(description = "发票类型，NORMAL:增值税普票 DEDICATED:增值税专票")
	private String type;

	@Schema(description = "普票类型，PERSONAL:个人，COMPANY:单位")
	private String titleType;

	@Schema(description = "是否已开具")
	private Boolean hasInvoiceFlag;

	@Schema(description = "订单号")
	private String orderNumber;
}
