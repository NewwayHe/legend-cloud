/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.bo;

import com.legendshop.basic.dto.PayTypeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 构建收银台参数基类
 * * @author legendshop
 */
@Data
public class CreatePayBO implements Serializable {


	/**
	 * 业务订单号集合
	 */
	@Schema(description = "业务订单号")
	private List<String> businessOrderNumberList;

	/**
	 * 支付金额
	 */
	@Schema(description = "支付金额")
	private BigDecimal amount;

	/**
	 * 支付主题
	 */
	@Schema(description = "支付主题集合")
	private List<String> subjectList;


	/**
	 * 订单超时未支付自动取消的倒数结束时间
	 */
	@Schema(description = "订单超时未支付自动取消的倒数结束时间")
	private Long orderCancelCountdownEndTime;

	/**
	 * 已启用的支付方式
	 */
	@Schema(description = "已启用的支付方式,如果支付方式列表为空，确认支付按钮置灰")
	private List<PayTypeDTO> payTypeList;


}
