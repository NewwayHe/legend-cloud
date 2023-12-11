/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.query;


import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Schema(description = "订单发票query")
public class OrderInvoiceQuery extends PageParams {

	@Schema(description = "商家ID", hidden = true)
	private Long shopId;

	@Schema(description = "订单号")
	private String orderNumber;

	@Schema(description = "发票类型，NORMAL:增值税普票 DEDICATED:增值税专票  UserInvoiceTypeEnum")
	private String type;

	@Schema(description = "个人普票：发票抬头信息 公司普票：发票抬头信息 增值税专票：公司名称")
	private String company;


	@Schema(description = "是否已开具")
	private Boolean hasInvoiceFlag;

	@Schema(description = "下单时间")
	private String createTime;

	@Schema(description = "用户名")
	private String userName;
}
