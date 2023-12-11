/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.utils;

import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.product.enums.ProductStatusEnum;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 检查商品的状态
 *
 * @author legendshop
 */
@Component
@AllArgsConstructor
public class ProductStatusChecker {

	final RedisTemplate<String, String> redisTemplate;

	/**
	 * 检查商品的状态是否正常,条件是：
	 * status = 1
	 * op_status = 1
	 * del_status = 1
	 */
	public boolean isProductNormal(ProductBO productBO) {
		if (productBO == null) {
			return false;
		}

		return ProductStatusEnum.PROD_ONLINE.getValue().equals(productBO.getStatus())
				&& OpStatusEnum.PASS.getValue().equals(productBO.getOpStatus())
				&& ProductDelStatusEnum.PROD_NORMAL.getValue().equals(productBO.getDelStatus());
	}


	/**
	 * 后台或者商家管理员检查该商品是否存在
	 *
	 * @param productBO 商品
	 * @param token     token
	 * @return
	 */
	public boolean isProductNormal(ProductBO productBO, String token) {
		if (productBO == null || token == null) {
			return false;
		}
		String key = token.substring(0, token.lastIndexOf(":"));
		String tokenValue = token.substring(token.lastIndexOf(":") + 1);
		String result = getToken(key);

		return result != null && result.equals(tokenValue);
	}

	/**
	 * 设置token，1个小时有效
	 *
	 * @param key = userId:productId
	 * @param
	 */
	public String setToken(String key) {
		String uuidToken = UUID.randomUUID().toString();
		redisTemplate.opsForValue().set(key, uuidToken, 3600L, TimeUnit.SECONDS);
		return uuidToken;
	}

	/**
	 * 预览前先要获取token，如果token存在的话，忽略商品的状态的检查
	 *
	 * @return 商品ID
	 */
	public String getToken(String token) {
		return redisTemplate.opsForValue().get(token);
	}

}
