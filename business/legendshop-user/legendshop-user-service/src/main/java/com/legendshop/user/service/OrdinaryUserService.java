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
import com.legendshop.common.core.dto.BasicBatchUpdateStatusDTO;
import com.legendshop.common.core.service.BaseService;
import com.legendshop.user.bo.OrdinaryUserBO;
import com.legendshop.user.dto.OrdinaryUserDTO;
import com.legendshop.user.dto.UserInfo;
import com.legendshop.user.dto.VerifyOrdinaryUserDTO;
import com.legendshop.user.query.OrdinaryUserQuery;

import java.util.List;

/**
 * @author legendshop
 */
public interface OrdinaryUserService extends BaseService<OrdinaryUserDTO> {

	/**
	 * 登录构建UserInfo
	 *
	 * @param username
	 * @return
	 */
	R<UserInfo> getUserInfo(String username);

	/**
	 * 登录构建UserInfo
	 *
	 * @param username
	 * @return
	 */
	String getMobileByUsername(String username);

	/**
	 * 分页查询用户信息(多表查询)
	 *
	 * @param ordinaryUserQuery
	 * @return
	 */
	PageSupport<OrdinaryUserBO> pageTwo(OrdinaryUserQuery ordinaryUserQuery);


	/**
	 * 检查手机号是否已存在
	 *
	 * @param mobile 手机号
	 * @return true 如果手机号存在；否则返回 false
	 */
	boolean isMobileExist(String mobile);

	/**
	 * 根据手机号查询用户信息
	 *
	 * @param mobile 手机号
	 * @return 匹配手机号的用户信息
	 */
	OrdinaryUserDTO getByMobile(String mobile);


	/**
	 * 通过userId获取
	 *
	 * @param userId
	 * @return
	 */
	OrdinaryUserDTO getByUserId(Long userId);

	/**
	 * 根据手机号列表查询用户信息
	 *
	 * @param mobileList 手机号列表
	 * @return 匹配手机号的用户信息列表
	 */
	List<OrdinaryUserDTO> queryByMobile(List<String> mobileList);

	/**
	 * 重置密码
	 *
	 * @param ordinaryUserDTO
	 * @return 操作结果
	 */
	R<Void> updatePassword(VerifyOrdinaryUserDTO ordinaryUserDTO);

	/**
	 * 修改号码
	 *
	 * @param userId
	 * @param mobile
	 * @return
	 */
	int updateMobileByUserId(Long userId, String mobile);

	boolean updateLoginPassword(OrdinaryUserDTO userDTO);

	boolean updateStatus(Long userId);

	R<Long> register(VerifyOrdinaryUserDTO userDTO);

	R<OrdinaryUserDTO> bindThirdParty(String mobilePhone, String thirdPartyIdentifier);

	R<Void> updateMobilePhone(VerifyOrdinaryUserDTO dto);

	R<Void> updateAvatar(Long userId, String avatar);

	List<OrdinaryUserDTO> getByIds(List<Long> userIds);

	boolean batchUpdateStatus(BasicBatchUpdateStatusDTO basicBatchUpdateStatusDTO);

	OrdinaryUserDTO getUser(String identifier);

	List<Long> getUserIds(Integer off, Integer size);

	/**
	 * 查询所用用户
	 *
	 * @return
	 */
	PageSupport<OrdinaryUserDTO> queryAllUser(OrdinaryUserQuery ordinaryUserQuery);

	/**
	 * 模糊查询用户id
	 *
	 * @param mobile
	 * @return
	 */
	List<Long> getByLikeMobile(String mobile);

	/**
	 * 修改用户锁定状态
	 *
	 * @param username
	 * @param status
	 * @return
	 */
	R updateStatusByUserName(String username, Boolean status);
}
