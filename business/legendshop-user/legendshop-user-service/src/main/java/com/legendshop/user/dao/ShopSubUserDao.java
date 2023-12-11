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
import com.legendshop.user.entity.ShopSubUser;
import com.legendshop.user.query.ShopSubUserQuery;

/**
 * @author legendshop
 */
public interface ShopSubUserDao extends GenericDao<ShopSubUser, Long> {
	PageSupport<ShopSubUser> queryUserPage(ShopSubUserQuery query);

	/**
	 * 查询员工账号
	 *
	 * @param userAccount 用户账户
	 * @param shopUserId  商家id
	 * @return
	 */
	ShopSubUser getUserAccount(String userAccount, Long shopUserId);

	/**
	 * 锁定用户
	 *
	 * @param userId
	 * @param status
	 * @return
	 */
	int updateStatusByUserName(Long userId, Boolean status);
}
