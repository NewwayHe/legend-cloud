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
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_search_history")
public class SearchHistoryEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -1638878105579877695L;

	/**
	 * id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SEARCH_LOG_SEQ")
	private Long id;

	/**
	 * 用户id
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 用户ip
	 */
	@Column(name = "remote_ip")
	private String remoteIP;

	/**
	 * 搜索词
	 */
	@Column(name = "word")
	private String word;

	/**
	 * user-agent
	 */
	@Column(name = "user_agent")
	private String userAgent;

	/**
	 * 搜索时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 来源
	 */
	@Column(name = "source")
	private String source;

}
