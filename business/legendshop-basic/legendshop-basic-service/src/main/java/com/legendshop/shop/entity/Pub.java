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
 * 商城公告表(Pub)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_pub")
public class Pub extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 927765417106763538L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "PUB_SEQ")
	private Long id;


	/**
	 * 发布用户ID
	 */
	@Column(name = "admin_user_id")
	private Long adminUserId;


	/**
	 * 发布者名称
	 */
	@Column(name = "admin_user_name")
	private String adminUserName;


	/**
	 * 标题
	 */
	@Column(name = "title")
	private String title;


	/**
	 * 内容
	 */
	@Column(name = "content")
	private String content;


	/**
	 * 开始有效时间
	 */
	@Column(name = "start_time")
	private Date startTime;


	/**
	 * 结束时间
	 */
	@Column(name = "end_time")
	private Date endTime;


	/**
	 * 状态，1:上线，0：下线
	 */
	@Column(name = "status")
	private Integer status;


	/**
	 * 公告类型:0:买家,1:卖家
	 */
	@Column(name = "type")
	private Integer type;

}
