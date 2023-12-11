/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.shop.dto.ContactInformationDTO;
import com.legendshop.shop.entity.ContactInformation;

/**
 * 商家微信联系方式存储表(ShopCustomerInformation)表数据库访问层
 *
 * @author legendshop
 * @since 2021-12-27 09:30:26
 */
public interface ContactInformationDao extends GenericDao<ContactInformation, Long> {

	/**
	 * 根据商家id查询商家微信联系方式
	 *
	 * @param shopId
	 * @return
	 */
	ContactInformationDTO getByShopId(Long shopId);
}
