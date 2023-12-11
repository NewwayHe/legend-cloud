/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
public class PaySettlementDTO extends BaseDTO {

	private static final long serialVersionUID = -5356270413821632678L;
	private Long id;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;

	/**
	 * 结算单，内部结算单
	 */
	@Schema(description = "结算单，内部结算单")
	private String paySettlementSn;

	/**
	 * 账单状态
	 */
	@Schema(description = "账单状态")
	private Integer state;

	/**
	 * 款项类型
	 */
	@Schema(description = "款项类型")
	private String useType;

	/**
	 * 支付来源
	 */
	@Schema(description = "支付来源")
	private String paySource;

	/**
	 * 清算时间
	 */
	@Schema(description = "清算时间")
	private Date clearingTime;
}
