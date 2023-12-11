/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 商家配置项(ShopParamItem)实体类
 *
 * @author legendshop
 * @since 2020-11-03 11:03:08
 */
@Data
@Entity
@Table(name = "ls_shop_param_item")
public class ShopParamItem extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 617027114726489880L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "shopParamItem_SEQ")
	private Long id;


	@Column(name = "parent_id")
	private Long parentId;

	/**
	 * 键
	 */
	@Column(name = "key_word")
	private String keyWord;

	/**
	 * 值
	 */
	@Column(name = "value")
	private String value;

	/**
	 * 描述
	 */
	@Column(name = "des")
	private String des;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;


	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 对于java类型
	 */
	@Column(name = "data_type")
	private String dataType;

}
