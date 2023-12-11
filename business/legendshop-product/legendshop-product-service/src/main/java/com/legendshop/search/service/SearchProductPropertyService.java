/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.service;

import com.legendshop.product.dto.ActivityProductDTO;
import com.legendshop.product.dto.ProductPropertyDTO;

import java.util.List;

/**
 * 商品规格参数搜索
 *
 * @author legendshop
 */
public interface SearchProductPropertyService {


	/**
	 * 获取属性和属性值
	 *
	 * @param activityProductDTO
	 * @return
	 */
	List<ProductPropertyDTO> getPropValListByProd(ActivityProductDTO activityProductDTO);

}
