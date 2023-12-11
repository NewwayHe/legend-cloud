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
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (OrderImportLogistics)DTO
 *
 * @author legendshop
 * @since 2022-04-25 14:14:40
 */
@Data
@Schema(description = "DTO")
public class OrderImportLogisticsDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 507928215584932947L;

	private Long id;

	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 总条数
	 */
	@Schema(description = "总条数")
	private Integer count;

	/**
	 * 成功条数
	 */
	@Schema(description = "成功条数")
	private Integer success;

	/**
	 * 错误条数
	 */
	@Schema(description = "错误条数")
	private Integer fail;

	/**
	 * 操作人
	 */
	@Schema(description = "操作人")
	private String operator;

	/**
	 * 操作时间
	 */
	@Schema(description = "操作时间")
	private Date createTime;

}
