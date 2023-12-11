/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "商品访问记录DTO")
public class SearchHistoryDTO implements Serializable {

	private static final long serialVersionUID = -5061793675384393194L;
	/**
	 * id
	 */
	@Schema(description = "ID")
	private Long id;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 用户ip
	 */
	@Schema(description = "用户ip")
	private String remoteIP;

	/**
	 * 搜索词
	 */
	@Schema(description = "搜索词")
	private String word;

	/**
	 * user-agent
	 */
	@Schema(description = "user-agent")
	private String userAgent;

	/**
	 * 搜索时间
	 */
	@Schema(description = "搜索时间")
	private Date createTime;

	/**
	 * 来源
	 */
	@Schema(description = "来源")
	private String source;

}
