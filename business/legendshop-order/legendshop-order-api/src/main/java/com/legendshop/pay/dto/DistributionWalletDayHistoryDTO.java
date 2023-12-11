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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户佣金钱包每日记录(DistributionWalletDayHistory)DTO
 *
 * @author legendshop
 * @since 2022-02-24 10:56:11
 */
@Data
@Schema(description = "用户佣金钱包每日记录DTO")
public class DistributionWalletDayHistoryDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 709004843603578847L;

	/**
	 * 主键id
	 */
	@Schema(description = "主键id")
	private Long id;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 业务类型
	 */
	@Schema(description = "业务类型")
	private String businessType;

	/**
	 * 当日金额
	 */
	@Schema(description = "当日金额")
	private BigDecimal commission;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	private Date updateTime;

	@Schema(description = "用户钱包明细list")
	List<DistributionWithdrawDetailDTO> walletDetailDTOList;
}
