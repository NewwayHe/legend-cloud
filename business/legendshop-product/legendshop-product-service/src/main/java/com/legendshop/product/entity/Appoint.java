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

import java.util.Date;

/**
 * 商品预约上架(Appoint)实体类
 *
 * @author legendshop
 * @since 2020-08-12 18:43:01
 */
@Data
@Entity
@Table(name = "ls_appoint")
public class Appoint extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 412894260942886418L;

	/**
	 * 主键ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "appoint_SEQ")
	private Long id;

	/**
	 * 商品名称
	 */
	@Column(name = "product_id")
	private Long productId;

	/**
	 * 上架时间
	 */
	@Column(name = "on_sell_date")
	private Date onSellDate;

	/**
	 * 上架标识：是否定时执行了上架操作
	 */
	@Column(name = "on_sell_flag")
	private Boolean onSellFlag;

}
