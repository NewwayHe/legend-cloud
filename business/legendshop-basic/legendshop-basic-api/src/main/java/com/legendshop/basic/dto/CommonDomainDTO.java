/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 域名dto
 *
 * @author legendshop
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonDomainDTO {

	/**
	 * 用户Pc端前端域名
	 */
	private String userPcDomainName;

	/**
	 * 用户移动端前端域名
	 */
	private String userMobileDomainName;
}
