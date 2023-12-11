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
import com.legendshop.common.core.dto.BasicBatchUpdateStatusDTO;
import com.legendshop.user.bo.OrdinaryUserBO;
import com.legendshop.user.dto.OrdinaryUserDTO;
import com.legendshop.user.entity.OrdinaryUser;
import com.legendshop.user.query.OrdinaryUserQuery;

import java.util.List;

/**
 * 普通用户表
 *
 * @author legendshop
 */
public interface OrdinaryUserDao extends GenericDao<OrdinaryUser, Long> {

	boolean isUserExists(Long userId);

	OrdinaryUser getUser(String identifier);


	PageSupport<OrdinaryUser> page(OrdinaryUserQuery ordinaryUserQuery);

	OrdinaryUser getByMobile(String mobile);

	List<OrdinaryUser> queryByMobile(List<String> mobileList);

	/**
	 * 判断手机是否存在
	 */
	boolean isMobileExist(String mobile);

	/**
	 * 修改号码
	 *
	 * @param userId
	 * @param mobile
	 * @return
	 */
	int updateMobileByUserId(Long userId, String mobile);

	String getMobileByUserId(Long userId);

	String getMobileByUsername(String username);

	boolean updateLoginPassword(OrdinaryUserDTO ordinaryUserDTO);

	boolean updateStatus(Long userId);

	int updateStatusByUserName(String userName, Boolean status);

	int updateAvatar(Long userId, String avatar);

	boolean batchUpdateStatus(BasicBatchUpdateStatusDTO basicBatchUpdateStatusDTO);

	List<Long> getUserIds(Integer off, Integer size);

	/**
	 * 多表联合查询普通用户分页
	 *
	 * @param ordinaryUserQuery
	 * @return
	 */
	PageSupport<OrdinaryUserBO> pageTwo(OrdinaryUserQuery ordinaryUserQuery);

	/**
	 * 用户端查询用户分页
	 *
	 * @param ordinaryUserQuery
	 * @return
	 */
	PageSupport<OrdinaryUserDTO> queryAllUser(OrdinaryUserQuery ordinaryUserQuery);

	/**
	 * 根据手机号模糊查询用户id
	 *
	 * @param mobile
	 * @return
	 */
	List<OrdinaryUser> getByLikeMobile(String mobile);
}
