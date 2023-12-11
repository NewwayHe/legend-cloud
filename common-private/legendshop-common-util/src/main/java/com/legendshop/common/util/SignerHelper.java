/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

/**
 * 签名帮助类
 *
 * @author legendshop
 */
@Slf4j
public class SignerHelper {

	/**
	 * 过期时间 10分钟
	 */
	private static final Long EXPIRE_SECONDS = 10 * 60 * 1000L;

	/**
	 * 加密数据
	 *
	 * @param signer the 加密参数
	 * @return sign
	 */
	public static String getSign(Map<String, Object> signer, String secret) {
		TreeMap<String, Object> map = MapUtil.sort(signer);
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if (null != entry.getKey()
					&& null != entry.getValue()
					&& entry.getValue().toString().length() > 0
					&& !"sign".equals(entry.getKey())
					&& !"key".equals(entry.getKey())
			) {
				sb.append(entry.getKey()).append("=>").append(entry.getValue()).append(";");
			}
		}
		sb.append("key").append("=>").append(secret).append(";");
		String md5 = SecureUtil.md5(sb.toString().toUpperCase()).toUpperCase();
		return SecureUtil.hmac(HmacAlgorithm.HmacSHA256, secret).digestHex(md5);
	}

	/**
	 * 检查签名
	 *
	 * @param signer the 加密参数
	 * @param secret the key
	 * @return boolean
	 */
	public static boolean checkSign(Map<String, Object> signer, String secret) {
		if (!signer.containsKey("sign") || !signer.containsKey("validTime")) {
			return false;
		}
		Long validTime = (Long) signer.get("validTime");
		long now = System.currentTimeMillis();
		if (validTime <= 0 || validTime > now || now - validTime > EXPIRE_SECONDS) {
			log.warn("时间校验不通过： 签名生成时间={}, 当前时间={}, 与当前时间相差 {}, 该数值必须在(0, {}]区间", validTime, now, now - validTime, EXPIRE_SECONDS);
			return false;
		}
		String afterSign = getSign(signer, secret);
		if (!signer.get("sign").equals(afterSign)) {
			log.warn("签名没有通过校验");
			return false;
		}
		return true;
	}


}
