/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.legendshop.product.bo.SkuBO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author legendshop
 */
@Data
public class ActivityProductDTO implements Serializable {

	private static final long serialVersionUID = -7399055874580583630L;


	/**
	 * 商品规格
	 */
	private String specification;

	/**
	 * sku集合
	 */
	private List<SkuBO> skuBOS;

	/**
	 * 活动id
	 */
	private Long activityId;

	/**
	 * 营销类型  {@link com.legendshop.product.enums.SkuActiveTypeEnum}
	 */
	private String skuType;

	/**
	 * 前端选中的skuId
	 */
	private Long skuId;
}
