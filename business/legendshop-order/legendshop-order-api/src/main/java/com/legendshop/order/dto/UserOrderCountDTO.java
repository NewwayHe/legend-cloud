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

/**
 * @author legendshop
 */
@Data
@Schema(description = "用户订单数量统计DTO")
public class UserOrderCountDTO {

	@Schema(description = "订单状态> 1:没有付款 , 5:已经付款,但卖家没有发货 , 10:发货，导致实际库存减少 , 15:投递完成，待用户确认收货 , 20:交易成功（确认收货），购买数增加1 , 25:交易失败.,还原库存")
	private Integer status;

	@Schema(description = "数量")
	private Integer count;
}
