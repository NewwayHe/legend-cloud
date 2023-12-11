/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 搜索重建索引日志(SearchRebuildIndexLog)实体类
 *
 * @author legendshop
 * @since 2022-02-18 10:50:55
 */
@Data
@Entity
@Table(name = "ls_search_rebuild_index_log")
public class SearchRebuildIndexLog extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -24031117016165727L;

	/**
	 * 主键
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "searchRebuildIndexLog_SEQ")
	private Long id;

	/**
	 * IndexTypeEnum
	 */
	@Column(name = "index_type")
	private String indexType;


	/**
	 * Create 10 ，Delete 20
	 */
	@Column(name = "target_method")
	private String targetMethod;

	/**
	 * 如果是营销索引，会有不同的营销活动类型，具体见ActivityEsTypeEnum，其它没有
	 */
	@Column(name = "target_type")
	private Integer targetType;

	/**
	 * 对应类型的主键ID
	 */
	@Column(name = "target_id")
	private String targetId;

	/**
	 * 待执行 10 ，已执行完成 20
	 */
	@Column(name = "status")
	private Integer status;

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

	/**
	 * 备注信息
	 */
	@Column(name = "remark")
	private String remark;

}
