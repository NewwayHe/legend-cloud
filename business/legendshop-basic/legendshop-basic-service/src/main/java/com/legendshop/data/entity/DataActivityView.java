/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.util.Date;

/**
 * (DataActivityView)实体类
 *
 * @author legendshop
 * @since 2021-07-07 16:14:20
 */
@Data
@Entity
@Table(name = "ls_data_activity_view")
public class DataActivityView implements GenericEntity<Long> {

	private static final long serialVersionUID = 927492065510814749L;

	/**
	 * id
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "dataActivityView_SEQ")
	private Long id;

	/**
	 * 活动id
	 */
	@Column(name = "activity_id")
	private Long activityId;

	/**
	 * 类型
	 */
	@Column(name = "type")
	private String type;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;

}
