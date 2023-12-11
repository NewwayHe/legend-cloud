/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.entity;


import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 单品SKU表(Sku)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_sku")
public class Sku extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -68956786813331804L;

	/**
	 * 单品ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SKU_SEQ")
	private Long id;


	/**
	 * 商品ID
	 */
	@Column(name = "product_id")
	private Long productId;


	/**
	 * 销售属性组合（中文）
	 */
	@Column(name = "cn_properties")
	private String cnProperties;

	/**
	 * sku的销售属性组合字符串（颜色，大小，等等，可通过类目API获取某类目下的销售属性）,格式是p1:v1;p2:v2
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
	 * 销售价
	 */
	@Column(name = "price")
	private BigDecimal price;

	/**
	 * 成本价
	 */
	@Column(name = "cost_price")
	private BigDecimal costPrice;

	/**
	 * 虚拟库存（商品在付款减库存的状态下，该sku上未付款的订单数量）
	 */
	@Column(name = "stocks")
	private Integer stocks;

	/**
	 * 实际库存
	 */
	@Column(name = "actual_stocks")
	private Integer actualStocks;

	/**
	 * 已经销售数量
	 */
	@Column(name = "buys")
	private Integer buys;

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


	/**
	 * sku营销活动类型 SkuActiveTypeEnum
	 */
	@Column(name = "sku_type")
	private String skuType;


	/**
	 * 是否被选了积分商品
	 */
	@Column(name = "integral_flag")
	private Boolean integralFlag;

	/**
	 * 是否被选了积分抵扣商品
	 */
	@Column(name = "integral_deduction_flag")
	private Boolean integralDeductionFlag;


	/**
	 * 检查sku的每个属性和属性值Id是否为数字，否则报错
	 * 如果没有properties则不保存到数据库.
	 *
	 * @return true, if successful
	 */
	public boolean checkProperties() {
		String properties = getProperties();
		if (StrUtil.isNotBlank(properties)) {
			String[] skuStrs = properties.split(";");
			for (int i = 0; i < skuStrs.length; i++) {
				String[] skuItems = skuStrs[i].split(":");
				Long.valueOf(skuItems[0]);
				Long.valueOf(skuItems[1]);
			}
			return true;
		} else {
			return true;
		}

	}

}
