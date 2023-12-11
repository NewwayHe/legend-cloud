/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 公用的产品类目(Category)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_category")
public class Category extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -87233466436009590L;

	/**
	 * 产品类目ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "CATEGORY_SEQ")
	private Long id;


	/**
	 * 父节点
	 */
	@Column(name = "parent_id")
	private Long parentId;


	/**
	 * 产品类目名称
	 */
	@Column(name = "name")
	private String name;


	/**
	 * 类目图标
	 */
	@Column(name = "icon")
	private String icon;


	/**
	 * 产品类目类型,参见ProductTypeEnum
	 */
	@Column(name = "type")
	private String type;


	/**
	 * 排序
	 */
	@Column(name = "seq")
	private Integer seq;


	/**
	 * 默认是1，表示正常状态,0为下线状态
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 分类层级
	 */
	@Column(name = "grade")
	private Integer grade;


	/**
	 * 推荐内容
	 */
	@Column(name = "recomm_con")
	private String recommCon;


	/**
	 * 退换货有效时间
	 */
	@Column(name = "return_valid_period")
	private Integer returnValidPeriod;

	/**
	 * 父类名称
	 */
	@Transient
	private String parentName;

	/**
	 * 子分类
	 */
	@Transient
	private List<Category> childrenList;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Category) {
			Category node = (Category) obj;
			return this.id.equals(node.getId());
		}
		return false;
	}

	public void addChildren(Category Children) {
		if (childrenList == null) {
			childrenList = new ArrayList<Category>();
		}
		if (this.id.equals(Children.getParentId())) {
			childrenList.add(Children);
		}
	}

}
