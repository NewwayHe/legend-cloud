/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (OrderImportLogisticsDetail)DTO
 *
 * @author legendshop
 * @since 2022-04-25 14:16:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "订单物流信息导入")
public class OrderImportLogisticsDetailDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 956653034654140944L;

	private Long id;

	/**
	 * 导入信息Id
	 */
	@Schema(description = "导入信息Id")
	private Long importId;

	/**
	 * 订单Id
	 */
	@Schema(description = "订单Id")
	private Long orderId;


	/**
	 * 收货人
	 */
	@Schema(description = "收货人")
	private String nikeName;


	/**
	 * 收货人手机号
	 */
	@Schema(description = "收货人手机号")
	private String mobile;
	/**
	 * 订单号
	 */
	@Schema(description = "订单号")
	private String number;

	/**
	 * 物流公司Id
	 */
	@Schema(description = "物流公司Id")
	private Long logisticsCompanyId;

	/**
	 * 物流公司名称
	 */
	@Schema(description = "物流公司名称")
	private String logisticsCompany;

	/**
	 * 物流公司编码
	 */
	@Schema(description = "物流公司编码")
	private String companyCode;

	/**
	 * 物流单号
	 */
	@Schema(description = "物流单号")
	private String logisticsNumber;

	/**
	 * 推送结果
	 */
	@Schema(description = "推送结果")
	private Boolean result;

	/**
	 * 错误描述
	 */
	@Schema(description = "错误描述")
	private String failReason;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	public OrderImportLogisticsDetailDTO(String number, String nickeName, String mobile, String logisticsCompany, String logisticsNumber, String failReason, Boolean result, Date createTime) {
		this.number = number;
		this.nikeName = nickeName;
		this.mobile = mobile;
		this.logisticsCompany = logisticsCompany;
		this.logisticsNumber = logisticsNumber;
		this.failReason = failReason;
		this.result = result;
		this.createTime = createTime;
	}

}
