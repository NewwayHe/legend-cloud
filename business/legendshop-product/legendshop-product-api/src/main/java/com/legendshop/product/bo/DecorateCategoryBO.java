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
import java.util.ArrayList;
import java.util.List;

/**
 * 装修弹层分类BO
 *
 * @author legendshop
 */
@Data
@Schema(description = "装修弹层分类BO")
public class DecorateCategoryBO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4994274590288509946L;


	@Schema(description = "分类ID")
	private Long id;


	@Schema(description = "分类层级")
	private Integer grade;


	@Schema(description = "分类名称")
	private String name;


	@Schema(description = "父类ID")
	private Long parentId;

	@Schema(description = "子分类")
	private List<DecorateCategoryBO> childrenList;


	public void addChildren(DecorateCategoryBO categoryDto) {
		if (childrenList == null) {
			childrenList = new ArrayList<>();
		}
		childrenList.add(categoryDto);
	}

}
