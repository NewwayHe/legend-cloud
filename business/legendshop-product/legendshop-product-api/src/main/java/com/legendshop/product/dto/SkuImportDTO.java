/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author legendshop
 */
@Data
public class SkuImportDTO implements Serializable {

	/**
	 * 商品名称
	 */
	@ExcelProperty(index = 0)
	@Schema(description = "商品名称")
	private String productName;

	@ExcelProperty(index = 1)
	@Schema(description = "类目名称")
	private String cateName;

	@ExcelProperty(index = 2)
	@Schema(description = "品牌")
	private String brand;

	@ExcelProperty(index = 3)
	@Schema(description = "商品图片")
	private String pic;

	@ExcelProperty(index = 4)
	@Schema(description = "商品详情图片")
	private String detailPic;

	@ExcelProperty(index = 5)
	@Schema(description = "商品详情文字")
	private String detailText;

	@ExcelProperty(index = 6)
	@Schema(description = "快递类型")
	private String deliveryType;

	@ExcelProperty(index = 7)
	@Schema(description = "运费模板")
	private String transportName;

	@ExcelProperty(index = 8)
	@Schema(description = "商品描述")
	private String brief;

	@ExcelProperty(index = 9)
	@Schema(description = "SKU名称")
	private String skuName;

	@ExcelProperty(index = 10)
	@Schema(description = " 规格参数")
	private String specification;

	@ExcelProperty(index = 11)
	@Schema(description = "sku规格图片")
	private String specificationPic;

	@ExcelProperty(index = 12)
	@Schema(description = "成本价")
	private String costPrice;

	@ExcelProperty(index = 13)
	@Schema(description = "销售价格")
	private String price;

	@ExcelProperty(index = 14)
	@Schema(description = "原价")
	private String originalPrice;

	@ExcelProperty(index = 15)
	@Schema(description = "销售库存")
	private String stocks;

	@ExcelProperty(index = 16)
	@Schema(description = "商家编码")
	private String partyCode;


	@ExcelProperty(index = 17)
	@Schema(description = "商品条形码")
	private String modelId;

	@ExcelProperty(index = 18)
	@Schema(description = "参数属性")
	private String properties;


	private List<String> picUrl;

	private List<String> detailPicUrl;

	private List<String> specificationUrl;
	private List<String> specificationId;
	private List<String> specificationValueId;
	private List<Long> propertiesIdList;
	;
	private List<Long> propertiesValueIdList;
	/**
	 * (全局商城)一级分类
	 */
	@Schema(description = "(全局商城)一级分类")
	private Long globalFirstCatId;

	/**
	 * (全局商城)二级分类
	 */
	@Schema(description = "(全局商城)二级分类")
	private Long globalSecondCatId;

	/**
	 * (全局商城)三级分类
	 */
	@Schema(description = "(全局商城)三级分类")
	private Long globalThirdCatId;

	private ProductPropertyDTO productPropertyDTO;
	/**
	 * 品牌id
	 */
	private Long brandId;

	/**
	 * sku规格id
	 */
	private String propertiesId;

	/**
	 * 主规格id
	 */
	private String propertiesMainId;


	/**
	 * sku属性id
	 */
	private String userProperties;

	private Long transportId;
	private ProductPropertyImageDTO productPropertyImageDTO;
	private Integer num;
}
