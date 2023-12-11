/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.legendshop.common.core.dto.BaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品预约上架(Appoint)DTO
 *
 * @author legendshop
 * @since 2020-08-12 18:25:38
 */
@Data
public class AppointDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 950451249772410445L;

	/**
	 * 商品名称
	 */
	private Long productId;

	/**
	 * 上架时间
	 */
	private Date onSellDate;

	/**
	 * 上架标识
	 */
	private Integer onSellFlag;

}
