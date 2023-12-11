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
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Schema(description = "订单物流信息参数")
public class LogisticsCompanyQuery extends PageParams {

	private static final long serialVersionUID = -2355592508746000960L;


	@Schema(description = "物流id")
	private Long id;

	@Schema(description = "物流公司名称")
	private String name;

	@Schema(description = "订单id")
	private Long orderId;

	@Schema(description = "订单id")
	private String orderNumber;

	@Schema(description = "用户id", hidden = false)
	private String userId;

	@Schema(description = "所属店铺", hidden = false)
	private Long shopId;

	/**
	 * 公司编码
	 */
	@Schema(description = "物流公司编号根据快递100查询")
	private String companyCode;

	@NotBlank(message = "运单号不能为空")
	private String shipmentNumber;

}
