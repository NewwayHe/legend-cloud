/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.request.cloud;

import com.legendshop.common.logistics.kuaidi100.utils.SignUtils;
import lombok.Data;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-10-27 15:45
 */
@Data
public class CloudBaseReq {
	/**
	 * 用户授权key
	 */
	private String secret_key;
	/**
	 * 接口编号
	 */
	private String secret_code;
	/**
	 * 加密签名：md5(secret_key+secret_secret)转大写
	 */
	private String secret_sign;

	private String secret_secret;

	public String getSecret_sign() {
		return SignUtils.sign(secret_key + secret_secret);
	}
}
