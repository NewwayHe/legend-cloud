/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 微信小程序检查敏感字返回值
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgSecCheckBO implements Serializable {

	private static final long serialVersionUID = -44392049632948091L;

	/**
	 * 错误码
	 * 40001:invalid credential, access_token is invalid or not latest rid
	 * 87014:risky content rid
	 * 0:ok
	 */
	@Schema(description = "错误码")
	private Integer errCode;

	/**
	 * 错误信息
	 */
	@Schema(description = "错误信息")
	private String errMsg;
}
