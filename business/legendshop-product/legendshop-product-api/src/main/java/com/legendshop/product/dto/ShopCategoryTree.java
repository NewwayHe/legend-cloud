/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.legendshop.product.bo.ShopCategoryBO;
import com.legendshop.user.dto.TreeNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 类目的树形结构
 *
 * @author legendshop
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Schema(description = "店铺类目树")
public class ShopCategoryTree extends TreeNode implements Serializable {

	private static final long serialVersionUID = -4906125379389134725L;

	/**
	 * 产品类目名称
	 */
	@Schema(description = "产品类目名称")
	private String name;

	/**
	 * 产品类目类型
	 * {@link com.legendshop.product.enums.ProductTypeEnum}
	 */
	@Schema(description = "商品类型Enum E.实物商品、V:虚拟商品")
	private String type;

	/**
	 * 排序
	 */
	@Schema(description = "排序")
	private Integer seq;

	/**
	 * 状态：默认是1，表示正常状态,0为下线状态
	 */
	@Schema(description = "状态：默认是1，表示正常状态,0为下线状态")
	private Integer status;

	/**
	 * 分类层级
	 */
	@Schema(description = "分类层级")
	private Integer grade;

	public ShopCategoryTree(ShopCategoryBO shopCategoryBO) {
		this.id = shopCategoryBO.getId();
		this.parentId = shopCategoryBO.getParentId();
		this.parentName = shopCategoryBO.getParentName();
		this.seq = shopCategoryBO.getSeq();
		this.name = shopCategoryBO.getName();
		this.status = shopCategoryBO.getStatus();
		this.grade = shopCategoryBO.getGrade();
	}
}
