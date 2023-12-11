/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 装修弹层商品BO
 *
 * @author legendshop
 */
@Data
@Schema(description = "装修弹层商品BO")
public class DecorateProductBO implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = 7571919886736103222L;


	@Schema(description = "商品ID")
	private Long id;


	@Schema(description = "商品名称")
	private String name;


	@Schema(description = "销售价范围")
	private String price;


	@Schema(description = "商品图片")
	private String pic;

	@Schema(description = "所属店铺ID")
	private Long shopId;

	@Schema(description = "所属店铺名称")
	private String siteName;

}
