/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.product.dto.PreSellProductDTO;

/**
 * 预售商品表(PreSellProduct)表服务接口
 *
 * @author legendshop
 * @since 2020-08-18 10:14:32
 */
public interface PreSellProductService {
	/**
	 * 根据ID获取预售商品信息的方法。
	 *
	 * @param id 预售商品ID
	 * @return 预售商品信息
	 */
	PreSellProductDTO getById(Long id);

	/**
	 * 根据ID删除预售商品信息的方法。
	 *
	 * @param id    预售商品ID
	 * @param prodId 商品ID
	 * @return 删除操作影响的行数
	 */
	int deleteById(Long id, Long prodId);

	/**
	 * 根据ID删除预售商品信息的方法。
	 *
	 * @param id 预售商品ID
	 * @return 删除操作影响的行数
	 */
	int deleteById(Long id);

	/**
	 * 保存预售商品信息的方法。
	 *
	 * @param presellProductDTO 预售商品信息
	 * @return 保存后的预售商品ID
	 */
	Long save(PreSellProductDTO presellProductDTO);

	/**
	 * 更新预售商品信息的方法。
	 *
	 * @param presellProductDTO 预售商品信息
	 * @return 更新操作影响的行数
	 */
	int update(PreSellProductDTO presellProductDTO);

	/**
	 * 根据方案名称和店铺ID获取预售商品信息的方法。
	 *
	 * @param schemeName 方案名称
	 * @param shopId     店铺ID
	 * @return 预售商品信息
	 */
	PreSellProductDTO getById(String schemeName, Long shopId);

	/**
	 * 根据预售商品ID获取预售商品信息的方法。
	 *
	 * @param preSellProdId 预售商品ID
	 * @return 预售商品信息
	 */
	PreSellProductDTO getByProductId(Long preSellProdId);
}
