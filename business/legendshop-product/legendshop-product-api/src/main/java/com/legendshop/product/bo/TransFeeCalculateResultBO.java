/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;

import com.legendshop.product.dto.TransFeeCalProductDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 运费计算结果
 *
 * @author legendshop
 */
@Data
public class TransFeeCalculateResultBO implements Serializable {

	private static final long serialVersionUID = 7991781929810542908L;


	/**
	 * 成功返回
	 * 该订单中店铺需要的运费
	 */
	private BigDecimal transFee;


	/**
	 * 失败返回
	 * 不支持区域限售的商品
	 */
	private List<TransFeeCalProductDTO> calProductDTOList;
}
