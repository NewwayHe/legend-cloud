/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.query;

import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 帮助文章查询的DTO
 *
 * @author legendshop
 */
@Schema(description = "帮助文章查询参数")
@Setter
@Getter
public class NewsQuery extends PageParams {

	private static final long serialVersionUID = 1804154735553013566L;

	@Schema(description = "栏目id")
	private Long newsCategoryId;

	@Schema(description = "标题")
	private String newsTitle;

	@Schema(description = "状态")
	private Integer status;

	/**
	 * 文章位置
	 */
	@Schema(description = "文章位置")
	private Integer position;

	/**
	 * 文章标签
	 */
	@Schema(description = "文章标签")
	private String newsTags;

	/**
	 * 发表时间
	 */
	@Schema(description = "发表时间")
	private Date createTime;

	/**
	 * 类型 1：内链  2外链
	 */
	@Schema(description = "类型 1：内链  2外链")
	private Integer type;

	/**
	 * 路径
	 */
	@Schema(description = "路径")
	private String url;

	/**
	 * 开始时间
	 */
	@Schema(description = "开始时间")
	private String beginTime;

	/**
	 * 结束时间
	 */
	@Schema(description = "结束时间")
	private String endTime;


}
