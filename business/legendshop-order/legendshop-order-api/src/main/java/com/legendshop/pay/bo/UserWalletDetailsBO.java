/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.bo;

import com.legendshop.pay.enums.WalletBusinessTypeEnum;
import com.legendshop.pay.enums.WalletOperationTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户钱包收支记录详情(UserWalletDetails)BO
 *
 * @author legendshop
 * @since 2021-03-13 14:44:01
 */
@Data
public class UserWalletDetailsBO implements Serializable {

	private static final long serialVersionUID = 933421620224261398L;

	private Long id;

	/**
	 * 用户Id
	 */
	@Schema(description = "用户Id")
	private Long userId;

	/**
	 * 流水号
	 */
	@Schema(description = "流水号")
	private Long serialNo;

	/**
	 * 业务编码
	 */
	@Schema(description = "业务编码")
	private Long businessId;

	/**
	 * 业务类型
	 */
	@Schema(description = "业务类型")
	private WalletBusinessTypeEnum businessType;

	/**
	 * 操作金额
	 */
	@Schema(description = "操作金额")
	private BigDecimal amount;

	/**
	 * 操作类型
	 */
	@Schema(description = "操作类型")
	private WalletOperationTypeEnum operationType;

	/**
	 * 收支签名；MD5(时间+金额+用户ID+流水号)
	 */
	@Schema(description = "收支签名；MD5(时间+金额+用户ID+流水号)")
	private String signature;

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

}
