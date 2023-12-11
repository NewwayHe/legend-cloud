/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.logistics.kuaidi100.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author legendshop
 * @Author: api.kuaidi100.com
 * @Date: 2020-07-14 16:54
 */
public class SignUtils {

	/**
	 * 快递100加密方式统一为MD5后转大写
	 *
	 * @param msg
	 * @return
	 */
	public static String sign(String msg) {
		return DigestUtils.md5Hex(msg).toUpperCase();
	}
}
