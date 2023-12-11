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
import com.legendshop.shop.dto.ContactInformationDTO;

/**
 * 微信联系方式存储表(ContactInformation)表服务接口
 *
 * @author legendshop
 * @since 2021-12-27 09:30:27
 */
public interface ContactInformationService {

	/**
	 * 保存微信联系方式
	 *
	 * @param contactInformationDTO
	 * @return
	 */
	R save(ContactInformationDTO contactInformationDTO);

	/**
	 * 删除微信联系方式
	 *
	 * @param id
	 * @return
	 */
	int deleteById(Long id);

	/**
	 * 根据id查询微信联系方式
	 *
	 * @param id
	 * @return
	 */
	ContactInformationDTO getById(Long id);

	/**
	 * 根据商家id查询微信联系方式
	 *
	 * @param shopId
	 * @return
	 */
	ContactInformationDTO getByShopId(Long shopId);
}
