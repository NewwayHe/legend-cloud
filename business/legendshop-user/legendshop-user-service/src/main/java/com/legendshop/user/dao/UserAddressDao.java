/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao;


import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.user.bo.UserAddressBO;
import com.legendshop.user.dto.UserAddressDTO;
import com.legendshop.user.entity.UserAddress;
import com.legendshop.user.query.UserAddressQuery;

import java.util.List;

/**
 * 用户订单地址Dao
 *
 * @author legendshop
 */
public interface UserAddressDao extends GenericDao<UserAddress, Long> {

	List<UserAddress> getUserAddress(String shopName);

	Long getMaxNumber(String userName);

	int updateDefaultUserAddress(Long addrId, Long userId);

	long getUserAddressCount(Long userId);


	PageSupport<UserAddressDTO> queryPage(UserAddressQuery userAddressQuery);

	int updateOtherDefault(Long addrId, Long userId, String commonAddr);

	/**
	 * 查询用户默认收货地址
	 *
	 * @param userId
	 * @return
	 */
	UserAddressBO getDefaultAddress(Long userId);


	/**
	 * 根据ID查询用户收货地址信息
	 *
	 * @param id
	 * @return
	 */
	UserAddressBO getAddressInfo(Long id);

	/**
	 * 下单-获取用户最新的收货地址
	 *
	 * @param userId
	 * @return
	 */
	List<UserAddressBO> getUserAddressForOrder(Long userId);

	List<UserAddress> queryByUserId(Long userId);
}
