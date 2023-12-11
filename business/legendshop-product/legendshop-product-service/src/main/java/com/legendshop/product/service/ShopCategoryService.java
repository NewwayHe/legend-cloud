/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.ShopCategoryBO;
import com.legendshop.product.dto.ShopCategoryDTO;
import com.legendshop.product.dto.ShopCategoryTree;

import java.util.List;
import java.util.Set;

/**
 * 商家的商品类目服务
 *
 * @author legendshop
 */
public interface ShopCategoryService {

	ShopCategoryDTO getById(Long id);

	R deleteById(Long id);

	/**
	 * 保存店铺分类
	 *
	 * @param shopCategoryDTO
	 * @return
	 */
	R save(ShopCategoryDTO shopCategoryDTO);

	R update(ShopCategoryDTO shopCategoryDTO);

	/**
	 * 根据shopId 查找分类 根据父级ID获取店铺的所有类目,状态 1上线，2下线，3全部
	 **/
	List<ShopCategoryBO> queryByShopId(Long shopId, int status);

	R updateStatus(Integer status, Long id);

	List<ShopCategoryTree> filterShopCategory(Set<ShopCategoryBO> all);
}
