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
 * 违规商品列表
 *
 * @author legendshop
 */
@Data
@Schema(description = "违规商品列表BO")
public class IllegalProductBO implements Serializable {


	private static final long serialVersionUID = -9020160199235148995L;

	/**
	 * 商品id
	 */
	@Schema(description = "商品id")
	private Long id;

	/**
	 * 商品图片
	 */
	@Schema(description = "商品图片")
	private String pic;

	/**
	 * 标题
	 */
	@Schema(description = "标题")
	private String name;

	/**
	 * 库存
	 */
	@Schema(description = "库存")
	private Integer stocks;

	/**
	 * 价格
	 */
	@Schema(description = "价格")
	private String price;

	/**
	 * 品牌名称
	 */
	@Schema(description = "品牌名称")
	private String brandName;

	/**
	 * 下架时间
	 */
	@Schema(description = "下架时间")
	private String handleTime;

	/**
	 * 违规下架原因
	 */
	@Schema(description = "违规下架原因")
	private String handleInfo;


}
