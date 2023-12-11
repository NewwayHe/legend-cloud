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
 * 新闻(News)实体类BO
 *
 * @author legendshop
 */
@Schema(description = "帮助文章BO")
@Data
public class NewsBO implements Serializable {

	private static final long serialVersionUID = -6970180788623053051L;


	/**
	 * 帮助文章ID
	 */
	@Schema(description = "帮助文章ID")
	private Long id;


	/**
	 * 帮助栏目ID
	 */
	@Schema(description = "帮助栏目ID")
	private Long newsCategoryId;


	/**
	 * 栏目名称
	 */
	@Schema(description = "栏目名称")
	private String newsCategoryName;

	/**
	 * 帮助文章标题
	 */
	@Schema(description = "帮助文章标题")
	private String newsTitle;


	/**
	 * 帮助文章提要
	 */
	@Schema(description = "帮助文章提要")
	private String newsBrief;


	/**
	 * 帮助文章内容
	 */
	@Schema(description = "帮助文章内容")
	private String newsContent;


	/**
	 * 帮助文章状态，1：上线，0：下线
	 */
	@Schema(description = "帮助文章状态，1：上线，0：下线")
	private Integer status;


	/**
	 * 是否高亮,1:yes,0:no
	 */
	@Schema(description = "帮助文章ID")
	private Integer highLine;

	@Schema(description = "顺序")
	private Integer seq;

	/**
	 * 帮助文章位置[底部、头部、...]
	 */
	@Schema(description = "帮助文章ID")
	private Integer position;

	/**
	 * 发布时间
	 */
	@Schema(description = "发布时间")
	private Date createTime;


}
