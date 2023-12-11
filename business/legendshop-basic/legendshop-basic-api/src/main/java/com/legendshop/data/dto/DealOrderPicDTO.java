/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "运营概况成交统计")
public class DealOrderPicDTO {

	@Schema(description = "成交用户数")
	private Integer dealOrderUser;

	@Schema(description = "成交订单数")
	private Integer dealOrderNum;

	@Schema(description = "成交订单金额")
	private BigDecimal dealOrderAmount;

	@Schema(description = "时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createTime;

}
