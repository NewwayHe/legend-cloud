/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.user.dto.ShopSubUserDTO;
import com.legendshop.user.dto.UserInfo;
import com.legendshop.user.query.ShopSubUserQuery;

import java.util.List;

/**
 * 商家用户服务
 *
 * @author legendshop
 */
public interface ShopSubUserService {

	List<ShopSubUserDTO> getShopSubUseByShopId(Long shopId);

	/**
	 * 查询所以店铺子账号
	 *
	 * @return
	 */
	List<ShopSubUserDTO> queryAllShopSubUser();

	ShopSubUserDTO getShopSubUser(Long id);

	R<Long> saveShopSubUser(ShopSubUserDTO shopUserDto);

	R<Void> updateShopSubUser(ShopSubUserDTO shopUserDto);

	R<Void> updatePassword(Long id, String newPassword);

	ShopSubUserDTO getByName(String username);

	List<ShopSubUserDTO> getShopSubUseRoleId(Long shopRoleId);

	PageSupport<ShopSubUserDTO> queryUserPage(ShopSubUserQuery query);

	R<UserInfo> getUserInfo(String username);

	int delete(Long id);

	int updateStatus(Long id);

	/**
	 * 查询该店铺子账号
	 *
	 * @return
	 */
	List<ShopSubUserDTO> queryShopSubAccount(Long shopUserId);

	/**
	 * 修改用户锁定状态
	 *
	 * @param username
	 * @param status
	 * @return
	 */
	R updateStatusByUserName(String username, Boolean status);
}
