/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import com.legendshop.shop.dao.ContactInformationDao;
import com.legendshop.shop.dto.ContactInformationDTO;
import com.legendshop.shop.entity.ContactInformation;
import org.springframework.stereotype.Repository;

/**
 * 商家微信联系方式存储表(ShopCustomerInformation)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2021-12-27 09:30:26
 */
@Repository
public class ContactInformationDaoImpl extends GenericDaoImpl<ContactInformation, Long> implements ContactInformationDao {

	@Override
	public ContactInformationDTO getByShopId(Long shopId) {
		String sql = "select s.shop_name as shopName,c.* FROM ls_contact_information c LEFT JOIN ls_shop_detail s on s.id = c.shop_id  WHERE shop_id = ?";
		return get(sql, ContactInformationDTO.class, shopId);
	}
}
