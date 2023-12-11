/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import com.legendshop.basic.dto.CategorySettingDTO;
import com.legendshop.common.core.constant.R;

/**
 * 移动端基础设置
 *
 * @author legendshop
 */
public interface AppSettingService {


	/**
	 * 获取分类设置
	 */
	CategorySettingDTO getCategorySetting();

	/**
	 * 保存分类设置
	 *
	 * @return
	 */
	R<Void> updateCategorySetting(CategorySettingDTO categorySetting);

}



