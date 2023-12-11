/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 属性图片Dto.
 *
 * @author legendshop
 */
@Data
public class PropertyValueImgDTO implements Serializable {

	private static final long serialVersionUID = -8139241876442901490L;
	/**
	 * 属性值id
	 */
	private Long valueId;

	/**
	 * 图片路径 集合
	 */
	private List<String> imgList;

	/**
	 * 属性值名称
	 */
	private String valueName;

	/**
	 * 属性ID
	 */
	private Long propId;

	/**
	 * 属性值图片
	 */
	private String valueImage;

	/**
	 * 排序
	 */
	private Long seq;
}
