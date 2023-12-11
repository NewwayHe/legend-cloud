/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
@Data
public class DistributorsOrderRecordDTO implements Serializable {


	@Schema(description = "用户id")
	private List<Long> idList;

	@Schema(description = "搜索完成订单 开始时间")
	private Date completeStartTime;

	@Schema(description = "搜索完成订单 结束时间")
	private Date completeEndTime;

	@Schema(description = " 消费金额")
	private BigDecimal ConsumeAmount;


	@Schema(description = " 消费次数")
	private Long ConsumeNum;


}
