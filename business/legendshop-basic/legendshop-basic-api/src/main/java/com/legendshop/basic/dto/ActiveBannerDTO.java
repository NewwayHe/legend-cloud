/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * (ActiveBanner)实体类
 *
 * @author legendshop
 */
@Data
public class ActiveBannerDTO implements Serializable {


	private static final long serialVersionUID = -5433211543296729962L;
	/**
	 * 主键ID
	 */
	private Long id;


	/**
	 * 图片路径
	 */
	private String imageFile;


	/**
	 * 图片链接
	 */
	private String url;


	/**
	 * 序号
	 */
	private Integer seq;


	/**
	 * banner图类型 ActiveBannerTypeEnum
	 */
	private String bannerType;

}
