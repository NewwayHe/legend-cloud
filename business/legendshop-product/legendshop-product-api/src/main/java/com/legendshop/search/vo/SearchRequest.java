/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.vo;

import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.product.enums.ProductStatusEnum;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 搜索页面前端的请求参数
 *
 * @author legendshop
 */
@Data
@Schema(description = "ES搜索请求参数")
public class SearchRequest {

	/**
	 * 搜索条件
	 */
	@Schema(description = "搜索条件")
	private String key;

	/**
	 * 当前页
	 */
	@Schema(description = "当前页")
	private Integer curPage;

	@Schema(description = "每页返回记录数")
	private Integer pageSize;

	/**
	 * 排序条件
	 */
	@Schema(description = "排序条件")
	private String sortBy;

	/**
	 * 升序降序
	 */
	@Schema(description = "升序降序")
	private Boolean descending;

	/**
	 * 过滤条件
	 */
	@Schema(description = "参数过滤条件")
	private String ev;

	/**
	 * 每页大小，不从页面接收，而是固定大小
	 */
	@Schema(description = "每页大小，不从页面接收，而是固定大小")
	private static final Integer DEFAULT_SIZE = 20;

	/**
	 * 默认页
	 */
	@Schema(description = "默认页")
	private static final Integer DEFAULT_PAGE = 1;

	/**
	 * 分类Id
	 */
	@Schema(description = "分类Id")
	private Long categoryId;

	/**
	 * 店铺分类Id
	 */
	@Schema(description = "店铺分类Id")
	private Long shopCategoryId;

	/**
	 * 品牌Id
	 */
	@Schema(description = "品牌Id")
	private List<Long> brandId;

	/**
	 * 商家Ids
	 */
	@Schema(description = "商家Ids")
	private Long shopId;

	/**
	 * 价格区间
	 */
	@Schema(description = "价格区间")
	@DecimalMin(value = "0.00", inclusive = false, message = "价格不能小于0")
	@Digits(integer = 6, fraction = 2, message = "价格保留2位小数且不能大于6位数")
	private List<BigDecimal> priceInterval;

	/**
	 * 分销类型 0、非分销商品  1、分销商品
	 */
	private Integer distType;

	/**
	 * 商品状态：{@link com.legendshop.product.enums.ProductStatusEnum}
	 */
	@Schema(description = "商品状态: -2：永久删除；-1：删除；0：下线；1：上线；2：违规；3：全部")
	private Integer status;

	/**
	 * 审核操作状态 {@link com.legendshop.basic.enums.OpStatusEnum}
	 */
	@Schema(description = "审核状态 -1：拒绝；0：待审核 ；1：通过")
	private Integer opStatus;

	/**
	 * 删除操作状态
	 * {@link ProductDelStatusEnum}
	 */
	@Schema(description = "删除操作状态 -2：永久删除；-1：删除；1：正常；")
	private Integer delStatus;

	/**
	 * 店铺状态
	 * {@link ShopDetailStatusEnum}
	 */
	@Schema(description = "店铺状态")
	private Integer shopStatus;

	public SearchRequest() {
		this.status = ProductStatusEnum.PROD_ONLINE.getValue();
		this.opStatus = OpStatusEnum.PASS.getValue();
		this.delStatus = ProductDelStatusEnum.PROD_NORMAL.value();
		this.shopStatus = ShopDetailStatusEnum.ONLINE.getStatus();
	}


	public Integer getCurPage() {
		if (curPage == null) {
			return DEFAULT_PAGE;
		}
		// 获取页码时做一些校验，不能小于1
		return Math.max(DEFAULT_PAGE, curPage);
	}

	public Integer getSize() {
		if (null != pageSize && pageSize > 0) {
			return pageSize;
		}

		return DEFAULT_SIZE;
	}
}
