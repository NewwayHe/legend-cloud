/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 装修页面BO
 *
 * @author legendshop
 */
@Data
@Schema(description = "装修页面BO")
public class DecoratePageBO implements Serializable {


	private static final long serialVersionUID = 7674443652537395206L;

	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;


	/**
	 * 页面名称
	 */
	@Schema(description = "页面名称")
	private String name;


	/**
	 * 页面类型 [INDEX：首页 POSTER：海报页] 参考枚举DecoratePageCategoryEnum
	 */
	@Schema(description = "页面类型 [INDEX：首页 POSTER：海报页]")
	private String category;


	/**
	 * 状态 [-1：草稿 0:已修改未发布 1:已发布 ]参考枚举DecoratePageStatusEnum
	 */
	@Schema(description = "状态 [-1：草稿 0:已修改未发布 1:已发布 ]")
	private Integer status;


	/**
	 * 是否已使用[1:使用中 0:未使用]参考枚举DecoratePageUseFlagEnum
	 */
	@Schema(description = "是否已使用")
	private Boolean useFlag;


	/**
	 * 可编辑的装修数据
	 */
	@Schema(description = "可编辑的装修数据")
	private String data;


	/**
	 * 已发布的装修数据
	 */
	@Schema(description = "已发布的装修数据")
	private String releaseData;

	/**
	 * 来源[pc：pc端 mobile：移动端]参考枚举DecoratePageSourceEnum
	 */
	@Schema(description = "来源[pc：pc端 mobile：移动端]")
	private String source;

	/**
	 * 封面图
	 */
	@Schema(description = "封面图")
	private String coverPicture;


	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date recDate;

	@Schema(description = "类型 1 官方  2 原创")
	private Integer type;
}
