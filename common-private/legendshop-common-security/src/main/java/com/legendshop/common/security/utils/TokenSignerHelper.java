/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.security.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author legendshop
 */
@Slf4j
public class TokenSignerHelper {

	private static final String SIGNER_KEY = "uKPF5oZcWB7B0FQ3izE/gg==";

	// 过期时间 10分钟
	private static final Long EXPIRE_SECONDS = 10 * 60 * 1000L;

	// 账号名称
	public static final String LEGENDSHOP_ANONYMOUS_ADMIN = "legendshopAnonymousAdmin";

	// 启动启动时，默认生产 128 密匙 [ 分布式不可用，会导致每个服务生成的密钥不一致，无法解密 ]
	//private static final byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

	private static final byte[] key = Base64.decode(SIGNER_KEY);

	public static String getToken() {
		Map<String, Object> map = new HashMap<>();
		map.put("token", LEGENDSHOP_ANONYMOUS_ADMIN);
		map.put("validTime", new Date().getTime());
		return SecureUtil.aes(key).encryptBase64(JSONUtil.toJsonStr(map));
	}

	public static boolean checkToken(String token) {
		try {
			Long now = new Date().getTime();
			String decodeToken = SecureUtil.aes(key).decryptStr(token);
			Map<String, Object> tokenMap = JSONUtil.toBean(decodeToken, new TypeReference<HashMap<String, Object>>() {
			}, true);
			token = (String) tokenMap.get("token");
			Long validTime = (Long) tokenMap.get("validTime");
			if (validTime <= 0 || validTime - EXPIRE_SECONDS > now || now - validTime > EXPIRE_SECONDS) {
				log.error("Token Expired");
				return false;
			}

			if (StringUtils.isBlank(token) || !token.equals(LEGENDSHOP_ANONYMOUS_ADMIN)) {
				log.error("Token Invalidation");
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
