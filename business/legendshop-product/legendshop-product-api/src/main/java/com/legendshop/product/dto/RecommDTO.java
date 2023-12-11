/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 分类导航
 *
 * @author legendshop
 */
@Data
public class RecommDTO implements Serializable {


	/**
	 * 分类ID
	 */
	private Long categoryId;

	/**
	 * 图片
	 */
	private String advPic;

	/**
	 * 图片文件
	 */
	private MultipartFile advPicFile;

	/**
	 * 链接
	 */
	private String advLink;

	/**
	 * 品牌IDJ集合
	 */
	String[] brandIds;

}
