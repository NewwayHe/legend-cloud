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
import java.util.Date;

/**
 * 预售商品表(PreSellProduct)实体类
 *
 * @author legendshop
 * @since 2020-08-18 10:14:14
 */
@Data
@Entity
@Table(name = "ls_pre_sell_product")
public class PreSellProduct extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -17764555272510000L;

	/**
	 * 主键
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "presellProduct_SEQ")
	private Long id;

	/**
	 * 商品id
	 */
	@Column(name = "product_id")
	private Long productId;

	/**
	 * 支付方式,0:全额,1:定金
	 */
	@Column(name = "pay_pct_type")
	private Integer payPctType;

	/**
	 * 预售开始时间
	 */
	@Column(name = "pre_sale_start")
	private Date preSaleStart;

	/**
	 * 预售结束时间
	 */
	@Column(name = "pre_sale_end")
	private Date preSaleEnd;

	/**
	 * 预售发货开始时间
	 */
	@Column(name = "pre_delivery_time")
	private Date preDeliveryTime;

	/**
	 * 预售发货结束时间
	 */
	@Column(name = "pre_delivery_endtime")
	private Date preDeliveryEndTime;

	/**
	 * 预售支付百分比
	 */
	@Column(name = "pay_pct")
	private BigDecimal payPct;

	/**
	 * 定金支付开始时间
	 */
	@Column(name = "deposit_start")
	private Date depositStart;

	/**
	 * 定金支付结束时间
	 */
	@Column(name = "deposit_end")
	private Date depositEnd;

	/**
	 * 尾款支付开始时间
	 */
	@Column(name = "final_m_start")
	private Date finalMStart;

	/**
	 * 尾款支付结束时间
	 */
	@Column(name = "final_m_end")
	private Date finalMEnd;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

}
