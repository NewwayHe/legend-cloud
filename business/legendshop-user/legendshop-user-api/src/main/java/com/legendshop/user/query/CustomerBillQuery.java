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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Schema(description = "账单记录query")
@Data
public class CustomerBillQuery extends PageParams implements Serializable {

	private static final long serialVersionUID = -5294345659826944627L;


	@Schema(description = "账单归属方用户ID")
	private Long ownerId;

	@Schema(description = "账单性质 收入:INCOME 支出:EXPENDITURE")
	private String mode;

	@Schema(description = "账单类型 商品交易:PAYMENT_GOODS 退款:REFUND 其他:OTHER")
	private String type;

	/**
	 * 交易说明
	 */
	@Schema(description = "交易说明")
	private String tradeExplain;

	@Schema(description = "开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startDate;
//	@DateTimeFormat(pattern="yyyy-MM-dd")
//	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")


	@Schema(description = "结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endDate;
//	@DateTimeFormat(pattern="yyyy-MM-dd")
//	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")


	/**
	 * 支付方式ID
	 * PayTypeEnum
	 */
	@Schema(description = "支付方式ID，WALLET_PAY：余额支付，ALI_PAY：支付宝，WX_PAY：微信，SIMULATE_PAY：模拟支付")
	private String payTypeId;
}
