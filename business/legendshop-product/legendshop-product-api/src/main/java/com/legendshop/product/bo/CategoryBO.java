/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 产品类目BO
 *
 * @author legendshop
 */
@Data
@Schema(description = "产品类目BO")
public class CategoryBO implements Serializable {


	private static final long serialVersionUID = -8745768597277462019L;
	/**
	 * 产品类目ID
	 */
	@Schema(description = "产品类目ID")
	private Long id;


	/**
	 * 父节点
	 */
	@Schema(description = "父节点")
	private Long parentId;

	/**
	 * 父节点名称
	 */
	@Schema(description = "父节点名称")
	private String parentName;

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
	@Schema(description = "产品类目类型：E实物商品 V虚拟商品")
	private String type;


	/**
	 * 排序
	 */
	@Schema(description = "排序")
	private Integer seq;


	/**
	 * 分类状态，表示正常状态,0为下线状态
	 */
	@Schema(description = "分类状态，表示正常状态,0为下线状态")
	private Integer status;

	/**
	 * 是否有子分类
	 */
	@Schema(description = "是否有子分类")
	private Boolean hasChildren;

	/**
	 * 类目关联ID
	 */
	@Schema(description = "类目关联ID")
	private Long aggId;

	/**
	 * 类目关联名称
	 */
	@Schema(description = "类目关联名称")
	private String aggName;

	/**
	 * 记录时间
	 */
	@Schema(description = "记录时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;


	/**
	 * 分类层级
	 */
	@Schema(description = "分类层级")
	private Integer grade;


	/**
	 * 推荐内容
	 */
	@Schema(description = "推荐内容")
	private String recommCon;


	/**
	 * 退换货有效时间
	 */
	@Schema(description = "退换货有效时间")
	private Integer returnValidPeriod;

	/**
	 * 子分类
	 */
	@Schema(description = "子分类")
	private List<CategoryBO> childrenList;

	/**
	 * true：不可选  false: 可选
	 */
	@Schema(description = "可选")
	private Boolean optional;

	/**
	 * Adds the children.
	 *
	 * @param categoryBO the category dto
	 */
	public void addChildren(CategoryBO categoryBO) {
		if (childrenList == null) {
			childrenList = new ArrayList<CategoryBO>();
		}
		childrenList.add(categoryBO);
	}
}
