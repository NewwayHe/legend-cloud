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
import com.legendshop.user.bo.ShopUserDetailBO;
import com.legendshop.user.dto.ShopUserDTO;
import com.legendshop.user.dto.UserInfo;
import com.legendshop.user.dto.VerifyShopUserDTO;
import com.legendshop.user.dto.VerifyShopUserMobileDTO;
import com.legendshop.user.query.ShopUserQuery;

import java.util.List;

/**
 * 商家用户服务
 *
 * @author legendshop
 */
public interface ShopUserService extends BaseService<ShopUserDTO> {

	R<Void> save(VerifyShopUserDTO shopUserDTO);

	@Override
	ShopUserDTO getById(Long id);

	Long saveShopUser(ShopUserDTO shopUserDto);

	int updateShopUser(ShopUserDTO shopUserDto);

	ShopUserDTO getShopUserByNameAndShopId(String name, Long shopId);

	List<ShopUserDTO> getShopUseRoleId(Long shopRoleId);

	PageSupport<ShopUserDTO> queryShopUser(String curPageNO, Long shopId);

	PageSupport<ShopUserDTO> page(ShopUserQuery shopUserQuery);

	R<UserInfo> getUserInfo(String username);

	/**
	 * 判断手机号
	 */
	boolean isMobileExist(String mobile);

	/**
	 * 通过手机号获取用户数据
	 */
	ShopUserDTO getByMobile(String mobile);

	/**
	 * 重置密码
	 */
	boolean updatePassword(ShopUserDTO shopUserDTO, String password);

	R<Void> updateMobilePhone(VerifyShopUserMobileDTO dto);

	/**
	 * 更新登录密码
	 *
	 * @param shopUserDTO
	 * @return
	 */
	boolean updateLoginPassword(ShopUserDTO shopUserDTO);

	/**
	 * 更新锁定状态
	 *
	 * @param id
	 * @return
	 */
	boolean updateStatus(Long id);

	/**
	 * 查询商家用户资料
	 *
	 * @param shopUserId
	 * @return
	 */
	R<ShopUserDetailBO> getShopUserDetail(Long shopUserId);

	/**
	 * 批量更新状态
	 *
	 * @param basicBatchUpdateStatusDTO
	 * @return
	 */
	boolean batchUpdateStatus(BasicBatchUpdateStatusDTO basicBatchUpdateStatusDTO);

	/**
	 * 更新商家用户头像
	 *
	 * @param userId
	 * @param avatar
	 * @return
	 */
	R<Void> updateAvatar(Long userId, String avatar);

	/**
	 * 根据商家用户IDS查询商家
	 *
	 * @param ids
	 * @return
	 */
	List<ShopUserDTO> getByIds(List<Long> ids);

	/**
	 * 根据店铺ID来查询商家信息
	 *
	 * @param shopid 店铺ID
	 * @return
	 */
	ShopUserDTO getByShopId(Long shopid);

	/**
	 * 获取所有店铺
	 *
	 * @return
	 */
	List<ShopUserDTO> queryAll();

	/**
	 * 锁定用户
	 *
	 * @param username
	 * @param status
	 * @return
	 */
	R updateStatusByUserName(String username, Boolean status);
}
