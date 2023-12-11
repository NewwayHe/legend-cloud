/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.extern.slf4j.Slf4j;

/**
 * rsa加密
 *
 * @author legendshop
 */
@Slf4j
public class RSAUtils {

	/**
	 * 私钥
	 */
	private static final String PAY_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOn3Z74g5CJL792E4GVsj+j3REJrOJXV1f+4oFBy7w9gGnBzcQkVElpwlDWEINkaO6YkoFVZvLLenFqX11eqGk3ozjrFL+ZrRi86uY8E6NFCqLO5SAvbPMmsMCdcL4HZPOcavNITHu1QPfIk9VHlPA/RrAojMcMvJ4D6NZRznCLhAgMBAAECgYAPYLMBZKEJ/gq8eRTUpb+JtOLYKLr14jGoFqb20q8SyCLEvFm0Gp3zss2S/OW5z0cXYvXk1jqYWRhWvf8p5GTJFPJxNeipwuHGgfqEX0oM/6XTvA7hInuYKMUrSPGiVirSEKlLL6XZlGwVH69PGt54JLywrBOCW2Q5HJ2eXyzwoQJBAPu+mg2hNLq/W38oxO9yhw/5lrOPZVcShyQdhK86eFUAD4HaUrvaxFcW+6UPtMEeMdPI+LTLAQ87pgxlnxKRockCQQDt697ceMD9jB9+DYPCLH0mAX32JL6Qe8LpOSxe/Pv6YKpXZmJmvvYN4lEs/o7HIdGO8FOfLwQKMcac8UU8tcRZAkEA9iTcHf2EQCBARynGvNT/aEhC6JiJnsRX7KEMDgUeiQXBI9cjU9hIZ1rqe+7KbpSmZYw4WRWwmINbDP4DtjU8uQJARe9PUv8ru1u05hiS4kCxiLpnjDLA0TeP0346zLEQYEiJyacOxaTcLriXo+IvldkbACkka2YmidiXyQoij/XeAQJAH4A2fzsgX8nj1SbpGd6owoRz2uXwBJN57FK26fQm2ppj9/RKX40oqSoQbnA25nyomYIZb6ZsAGKqqgru/fPzpA==";
	/**
	 * 公钥
	 */
	private static final String PAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDp92e+IOQiS+/dhOBlbI/o90RCaziV1dX/uKBQcu8PYBpwc3EJFRJacJQ1hCDZGjumJKBVWbyy3pxal9dXqhpN6M46xS/ma0YvOrmPBOjRQqizuUgL2zzJrDAnXC+B2TznGrzSEx7tUD3yJPVR5TwP0awKIzHDLyeA+jWUc5wi4QIDAQAB";


	/**
	 * rsa加密算法
	 */
	private static final RSA rsa = new RSA(PAY_PRIVATE_KEY, PAY_PUBLIC_KEY);

	/**
	 * 解密
	 *
	 * @param coding
	 * @return
	 */
	public static String decrypt(String coding) {
		try {
			byte[] decrypt = rsa.decrypt(Base64.decode(coding), KeyType.PrivateKey);
			return StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
		} catch (Exception e) {
			return coding;
		}
	}

	/**
	 * 加密
	 *
	 * @param coding
	 * @return
	 */
	public static String encryption(String coding) {
		try {
			byte[] encrypt = rsa.encrypt(StrUtil.bytes(coding, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
			return Base64.encode(encrypt);
		} catch (Exception e) {
			return coding;
		}
	}
}
