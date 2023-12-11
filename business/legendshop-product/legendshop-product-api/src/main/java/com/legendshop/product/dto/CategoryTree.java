/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.legendshop.product.bo.CategoryBO;
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
@Schema(description = "平台类目树")
public class CategoryTree extends TreeNode implements Serializable {

	private static final long serialVersionUID = -4906125379389134725L;

	/**
	 * 产品类目名称
	 */
	@Schema(description = "产品类目名称")
	private String name;


	/**
	 * 类目图标
	 */
	@Schema(description = "类目图标")
	private String icon;


	/**
	 * 产品类目类型
	 * {@link com.legendshop.product.enums.ProductTypeEnum}
	 */
	@Schema(description = "商品类型 E.实物商品、V:虚拟商品")
	private String type;

	/**
	 * 排序
	 */
	@Schema(description = "排序")
	private Integer seq;

	/**
	 * 状态：1，表示正常状态,0为下线状态
	 */
	@Schema(description = "状态：1，表示正常状态,0为下线状态")
	private Integer status;

	/**
	 * 是否有子分类
	 */
	@Schema(description = "是否有子分类")
	private Boolean hasChildren;

	/**
	 * 分类层级
	 */
	@Schema(description = "分类层级")
	private Integer grade;

	@Schema(description = "可选")
	private Boolean optional;

	public CategoryTree(CategoryBO categoryBO) {
		this.id = categoryBO.getId();
		this.parentId = categoryBO.getParentId();
		this.parentName = categoryBO.getParentName();
		this.seq = categoryBO.getSeq();
		this.icon = categoryBO.getIcon();
		this.hasChildren = categoryBO.getHasChildren();
		this.name = categoryBO.getName();
		this.type = categoryBO.getType();
		this.status = categoryBO.getStatus();
		this.grade = categoryBO.getGrade();
		this.optional = categoryBO.getOptional();
	}
}
