/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import com.legendshop.shop.dto.ShopCategoryCommDTO;

import java.util.List;

/**
 * 常用分类
 *
 * @author legendshop
 */
public interface ShopCategoryCommService {

	/**
	 * 根据商家ID获取商家类目关联列表
	 *
	 * @param shopId 商家ID
	 * @return 商家类目关联列表
	 */
	List<ShopCategoryCommDTO> getShopCategoryCommListByShopId(Long shopId);

	/**
	 * 删除商家类目关联信息
	 *
	 * @param id 商家类目关联ID
	 * @return 删除操作结果
	 */
	int delShopCategoryComm(Long id);

	/**
	 * 保存商家类目关联信息
	 *
	 * @param shopCategoryCommDTO 商家类目关联DTO
	 * @return 保存后的商家类目关联ID
	 */
	Long saveShopCategoryComm(ShopCategoryCommDTO shopCategoryCommDTO);

	/**
	 * 获取指定商家和类目的关联信息
	 *
	 * @param categoryId 类目ID
	 * @param shopId     商家ID
	 * @return 商家类目关联信息
	 */
	ShopCategoryCommDTO getShopCategoryComm(Long categoryId, Long shopId);

}
