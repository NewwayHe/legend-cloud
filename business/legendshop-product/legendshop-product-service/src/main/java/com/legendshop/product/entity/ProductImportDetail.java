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
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_import_detail")
public class ProductImportDetail implements GenericEntity<Long> {

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "productImportDetail_SEQ")
	private Long id;


	@Column(name = "import_id")
	private Long importId;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "cate_name")
	private String cateName;

	@Column(name = "brand")
	private String brand;

	@Column(name = "pic")
	private String pic;

	@Column(name = "detail_pic")
	private String detailPic;

	@Column(name = "sku_pic")
	private String skuPic;

	@Column(name = "delivery_type")
	private String deliveryType;


	@Column(name = "transport_name")
	private String transportName;

	@Column(name = "brief")
	private String brief;

	@Column(name = "sku_name")
	private String skuName;

	@Column(name = "specification")
	private String specification;

	@Column(name = "specification_pic")
	private String specificationPic;


	@Column(name = "cost_price")
	private BigDecimal costPrice;

	@Column(name = "price")
	private BigDecimal price;


	@Column(name = "stocks")
	private Integer stocks;

	@Column(name = "party_code")
	private String partyCode;

	@Column(name = "model_id")
	private Integer modelId;

	@Column(name = "properties")
	private String properties;

	@Column(name = "fail_reason")
	private String failReason;

	@Column(name = "result")
	private Boolean result;


	/**
	 * 操作时间
	 */
	@Column(name = "create_time")
	private Date createTime;
}
