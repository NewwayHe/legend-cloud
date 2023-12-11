/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
public class WalletSignatureDTO implements Serializable {

	private Long serialNo;

	private Long userId;

	private BigDecimal amount;

	private Date createTime;

	private Date updateTime;

	public WalletSignatureDTO(UserWalletDetailsDTO dto) {
		this.serialNo = dto.getSerialNo();
		this.userId = dto.getUserId();
		this.amount = dto.getAmount();
		this.createTime = dto.getCreateTime();
		this.updateTime = dto.getUpdateTime();
	}

	public WalletSignatureDTO() {
	}

	public WalletSignatureDTO(Long serialNo, Long userId, BigDecimal amount, Integer state, Date createTime, Date updateTime, String signature) {
		this.serialNo = serialNo;
		this.userId = userId;
		this.amount = amount;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
}
