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
import lombok.Data;

/**
 * 物流评分统计表(LogisticsCommentStatistics)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_logistics_comment_statistics")
public class LogisticsCommentStatistics implements GenericEntity<Long> {

	private static final long serialVersionUID = -51817082407640624L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "LOGISTICS_COMMENT_STATISTICS_SEQ")
	private Long id;


	/**
	 * 物流公司ID
	 */
	@Column(name = "logistics_company_id")
	private Long logisticsCompanyId;


	/**
	 * 点评次数
	 */
	@Column(name = "count")
	private Integer count;


	/**
	 * 总分数
	 */
	@Column(name = "score")
	private Integer score;

}
