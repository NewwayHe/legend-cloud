/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.BaseService;
import com.legendshop.shop.dto.ShopParamItemDTO;
import com.legendshop.shop.dto.ShopParamsDTO;

import java.util.List;

/**
 * 商家主配置(ShopParams)表服务接口
 *
 * @author legendshop
 * @since 2020-11-03 11:00:08
 */
public interface ShopParamsService extends BaseService<ShopParamsDTO> {

	/**
	 * 初始化店铺配置
	 *
	 * @param shopId
	 * @return
	 */
	R<Void> initShopParams(Long shopId);

	/**
	 * 根据主配置的name得到配置的dto
	 *
	 * @param shopId
	 * @param name
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	<T> T getConfigDtoByParamName(Long shopId, String name, Class<T> clazz);

	/**
	 * 根据店铺ID和名称获取
	 *
	 * @param shopId
	 * @param name
	 * @return
	 */
	ShopParamsDTO getByName(Long shopId, String name);

	List<ShopParamItemDTO> getShopParamItemsByParamName(String name, Long shopId);
}
