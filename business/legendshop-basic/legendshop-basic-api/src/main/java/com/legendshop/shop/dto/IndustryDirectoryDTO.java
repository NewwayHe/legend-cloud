/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 行业目录(IndustryDirectory)DTO
 *
 * @author legendshop
 * @since 2021-03-09 13:53:14
 */
@Data
@Schema(description = "行业目录DTO")
public class IndustryDirectoryDTO implements Serializable {

	private static final long serialVersionUID = 625841134422017276L;

	private Long id;

	/**
	 * 目录名称（不能重复）
	 */
	@Schema(description = "目录名称")
	private String name;

	@Schema(description = "排序")
	private BigDecimal seq;

	@Schema(description = "使用状态")
	private Boolean state;

	@Schema(description = "创建时间")
	private Date createTime;

}
