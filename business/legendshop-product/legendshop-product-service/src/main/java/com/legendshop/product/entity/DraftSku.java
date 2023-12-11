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

import java.math.BigDecimal;

/**
 * 单品SKU草稿表(DraftSku)实体类
 *
 * @author legendshop
 * @since 2022-05-08 10:11:56
 */
@Data
@Entity
@Table(name = "ls_draft_sku")
public class DraftSku extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -39637258562294749L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "draftSku_SEQ")
	private Long id;

	/**
	 * 单品ID
	 */
	@Column(name = "sku_id")
	private Long skuId;

	/**
	 * 商品ID
	 */
	@Column(name = "product_id")
	private Long productId;

	/**
	 * 商品规格属性组合（中文）
	 */
	@Column(name = "cn_properties")
	private String cnProperties;

	/**
	 * 商品规格属性id组合（颜色，大小，等等，可通过类目API获取某类目下的销售属性）,e.g 223:673;224:689
	 */
	@Column(name = "properties")
	private String properties;

	/**
	 * 用户自定义的销售属性，key:value 格式
	 */
	@Column(name = "user_properties")
	private String userProperties;

	/**
	 * 原价
	 */
	@Column(name = "original_price")
	private BigDecimal originalPrice;

	/**
	 * 成本价
	 */
	@Column(name = "cost_price")
	private BigDecimal costPrice;

	/**
	 * 销售价
	 */
	@Column(name = "price")
	private BigDecimal price;

	/**
	 * SKU名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 商家编码
	 */
	@Column(name = "party_code")
	private String partyCode;

	/**
	 * 商品条形码
	 */
	@Column(name = "model_id")
	private String modelId;

	/**
	 * sku图片
	 */
	@Column(name = "pic")
	private String pic;

	/**
	 * 物流体积
	 */
	@Column(name = "volume")
	private Double volume;

	/**
	 * 物流重量(千克)
	 */
	@Column(name = "weight")
	private Double weight;


}
