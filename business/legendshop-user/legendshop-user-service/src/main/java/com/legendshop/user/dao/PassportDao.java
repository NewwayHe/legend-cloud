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
import com.legendshop.user.entity.Passport;
import com.legendshop.user.entity.PassportItem;

import java.util.List;

/**
 * @author legendshop
 */
public interface PassportDao extends GenericDao<Passport, Long> {

	/**
	 * 根据userId得到
	 *
	 * @param userId
	 * @return
	 */
	Passport getByUserId(Long userId);

	Passport getByUserIdAndType(Long userId, String type);

	Passport getPassByIdentifier(String identifier);

	String getOpenIdByUserId(Long userId, String source);

	boolean clearPassportItem(Long userId, String source);

	PassportItem authInfo(Long userId, String type, String source);

	/**
	 * 根据用户ids获取
	 *
	 * @param userIds
	 * @param source
	 * @return
	 */
	List<String> getOpensByUserIds(List<Long> userIds, String source);
}
