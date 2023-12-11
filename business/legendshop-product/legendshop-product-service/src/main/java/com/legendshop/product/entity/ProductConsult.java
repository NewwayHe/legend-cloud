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
 * 商品咨询(ProdComm)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_product_consult")
public class ProductConsult implements GenericEntity<Long> {


	private static final long serialVersionUID = 1221245171852812L;
	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PROD_COMM_SEQ")
	private Long id;


	/**
	 * 商品ID
	 */
	@Column(name = "product_id")
	private Long productId;


	/**
	 * 商家ID
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 咨询类型 1: 商品咨询, 2:库存配送, 3:售后咨询'
	 */
	@Column(name = "point_type")
	private Integer pointType;


	/**
	 * 咨询内容
	 */
	@Column(name = "content")
	private String content;


	/**
	 * 回答
	 */
	@Column(name = "answer")
	private String answer;


	/**
	 * 咨询时间
	 */
	@Column(name = "rec_date")
	private Date recDate;


	/**
	 * ip 来源
	 */
	@Column(name = "post_ip")
	private Integer postIp;


	/**
	 * 回答时间
	 */
	@Column(name = "answer_time")
	private Date answerTime;

	/**
	 * 提问人
	 */
	@Column(name = "ask_user_id")
	private Long askUserId;


	/**
	 * 回复状态
	 */
	@Column(name = "reply_sts")
	private Integer replySts;

	/**
	 * 删除状态 0：保留 1：删除
	 */
	@Column(name = "del_sts")
	private Integer delSts;


	/**
	 * 状态 0：下线 1：上线
	 */
	@Column(name = "status")
	private Integer status;
	/**
	 * 回复人名称
	 */
	@Column(name = "reply_name")
	private String replyName;
}
