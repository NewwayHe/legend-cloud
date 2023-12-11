/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * @author legendshop
 */
public class WalletUtil {

	private static final Snowflake snowflake = IdUtil.createSnowflake(1, 1);

	public static String sign(Map<String, Object> params) {
		return MD5.create().digestHex(JSON.toJSONString(params)).toUpperCase();
	}

	public static boolean verification(Map<String, Object> params, String signature) {
		return sign(params).equals(signature);
	}

	public static long snowflakeId() {
		return snowflake.nextId();
	}
}
