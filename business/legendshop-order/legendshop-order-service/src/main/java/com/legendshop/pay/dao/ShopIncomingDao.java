/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.pay.entity.ShopIncoming;

import java.util.List;

/**
 * 商家进件表(ShopIncoming)表数据库访问层
 *
 * @author legendshop
 * @since 2021-03-12 09:27:58
 */
public interface ShopIncomingDao extends GenericDao<ShopIncoming, Long> {

	/**
	 * 根据商家ID获取进件信息
	 *
	 * @param shopId
	 * @return
	 */
	ShopIncoming getByShopId(Long shopId);

	/**
	 * 根据商家请求号获取进件信息
	 *
	 * @param requestNo
	 * @return
	 */
	ShopIncoming getByRequestNo(String requestNo);

	/**
	 * 根据商家ID获取进件信息
	 *
	 * @param shopIds
	 * @return
	 */
	List<ShopIncoming> getByShopId(List<Long> shopIds);

	/**
	 * 获取商户编号
	 *
	 * @param shopId
	 * @return
	 */
	String getMerchantNoByShopId(Long shopId);
}
