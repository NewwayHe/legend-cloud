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

import java.util.Date;

/**
 * 店铺运费规则(TransRule)实体类
 *
 * @author legendshop
 * @since 2020-09-08 17:00:50
 */
@Data
@Entity
@Table(name = "ls_trans_rule")
public class TransRule implements GenericEntity<Long> {

	private static final long serialVersionUID = -87801984083275495L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "transRule_SEQ")
	private Long id;


	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 1：叠加运算 2：按最高值计算
	 */
	@Column(name = "type")
	private Integer type;


	@Column(name = "rec_date")
	private Date recDate;

}
