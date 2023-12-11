/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.excel;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品导出dto
 *
 * @author legendshop
 */
@Data
public class SkuExportDTO implements Serializable {

	/**
	 * 副标题
	 */
	private String skuName;

	/**
	 * 规格项1
	 */
	private String firstProperties;

	/**
	 * 规格值1
	 */
	private String firstPropertiesValue;

	/**
	 * 规格项2
	 */
	private String secondProperties;

	/**
	 * 规格值2
	 */
	private String secondPropertiesValue;

	/**
	 * 规格项3
	 */
	private String thirdProperties;

	/**
	 * 规格值3
	 */
	private String thirdPropertiesValue;

	/**
	 * 规格项4
	 */
	private String fourthProperties;

	/**
	 * 规格值4
	 */
	private String fourthPropertiesValue;

	/**
	 * SKU售价
	 */
	private String skuPrice;

	/**
	 * 可售库存
	 */
	private String stocks;

	/**
	 * 商家编码
	 */
	private String partyCode;

	/**
	 * 商品条形码
	 */
	private String modelId;

	/**
	 * 是否上架
	 */
	private String status;

	/**
	 * 备注
	 */
	private String remark;

}
