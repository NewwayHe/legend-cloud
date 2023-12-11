/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.query;


import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 品牌搜索DTO
 *
 * @author legendshop
 */
@Data
@Accessors(chain = true)
@Schema(description = "品牌查询参数")
public class BrandQuery extends PageParams {

	private static final long serialVersionUID = -2187860906007046598L;


	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;

	/**
	 * 品牌id列表
	 */
	@Schema(description = "品牌id列表")
	private List<Long> idList;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long shopUserId;


	/**
	 * 商家ID
	 */
	@Schema(description = "商家ID")
	private Long shopId;


	/**
	 * 品牌名称
	 */
	@Schema(description = "品牌名称")
	private String brandName;


	/**
	 * 顺序
	 */
	@Schema(description = "顺序")
	private Integer seq;

	/**
	 * 品牌状态：{@link com.legendshop.product.enums.BrandStatusEnum}
	 */
	@Schema(description = "品牌状态")
	private Integer status;


	/**
	 * 审核操作状态
	 * {@link com.legendshop.common.biz.enums.OpstatusEnum}
	 */
	@Schema(description = "审核操作状态")
	private Integer opStatus;

	/**
	 * 用户名
	 */
	@Schema(description = "用户名")
	private String userName;

	/**
	 * 是否推荐
	 */
	@Schema(description = "是否推荐")
	private Integer commend;

	/**
	 * 商家名称
	 */
	@Schema(description = "商家名称")
	private String shopName;


}
