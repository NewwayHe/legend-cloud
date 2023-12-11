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
 * 商家主配置(ShopParams)实体类
 *
 * @author legendshop
 * @since 2020-11-03 11:00:06
 */
@Data
@Entity
@Table(name = "ls_shop_params")
public class ShopParams extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -62944604714179785L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "shopParams_SEQ")
	private Long id;

	/**
	 * 商家id
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 名称ShopParamNameEnum
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 描述
	 */
	@Column(name = "des")
	private String des;

	/**
	 * 类型ShopParamTypeEnum
	 */
	@Column(name = "type")
	private String type;

	/**
	 * 分组ShopParamGroupEnum
	 */
	@Column(name = "group_by")
	private String groupBy;

	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;

	/**
	 * 创建时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;

}
