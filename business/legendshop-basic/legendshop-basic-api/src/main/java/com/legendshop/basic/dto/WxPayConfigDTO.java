/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
public class WxPayConfigDTO implements Serializable {

	private static final long serialVersionUID = -694746201921576479L;
	private String mchId;
	private String mchKey;
	private String token;
	private String partnerKey;
	private String refundFile;
	private String refundFlag;
	private String enabled;

}
