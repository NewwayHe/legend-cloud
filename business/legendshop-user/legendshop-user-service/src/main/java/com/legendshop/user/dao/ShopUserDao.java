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
import com.legendshop.user.dto.ShopUserDTO;
import com.legendshop.user.entity.ShopUser;
import com.legendshop.user.query.ShopUserQuery;

import java.util.Date;
import java.util.List;

/**
 * 商城用户dao
 *
 * @author legendshop
 */
public interface ShopUserDao extends GenericDao<ShopUser, Long> {
	/**
	 * 根据商铺ID获取商铺用户列表
	 *
	 * @param shopId 商铺ID
	 * @return 商铺用户列表
	 */
	List<ShopUser> getShopUseByShopId(Long shopId);

	/**
	 * 根据商铺ID和姓名查找唯一的商铺用户
	 *
	 * @param shopId 商铺ID
	 * @param name   姓名
	 * @return 唯一的商铺用户
	 */
	ShopUser getShopUser(Long shopId, String name);

	/**
	 * 查询用户的权限列表
	 *
	 * @param shopUserId 商铺用户ID
	 * @return 用户的权限列表
	 */
	List<String> queryPerm(Long shopUserId);

	/**
	 * 根据商铺角色ID获取商铺用户列表
	 *
	 * @param shopRoleId 商铺角色ID
	 * @return 商铺用户列表
	 */
	List<ShopUser> getShopUseRoleId(Long shopRoleId);

	/**
	 * 分页查询商铺用户
	 *
	 * @param curPageNO 分页页码
	 * @param shopId    商铺ID
	 * @return 分页的商铺用户列表
	 */
	PageSupport<ShopUser> queryShopUser(String curPageNO, Long shopId);

	/**
	 * 分页查询商铺用户
	 *
	 * @param shopUserQuery 商铺用户查询对象
	 * @return 分页的商铺用户列表
	 */
	PageSupport<ShopUser> page(ShopUserQuery shopUserQuery);

	/**
	 * 根据用户名获取商铺用户
	 *
	 * @param account 用户名
	 * @return 商铺用户对象
	 */
	ShopUser getByUsername(String account);

	/**
	 * 判断手机号是否存在
	 *
	 * @param mobile 手机号
	 * @return 存在返回 true，否则返回 false
	 */
	boolean isMobileExist(String mobile);

	/**
	 * 通过手机号获取商铺用户数据
	 *
	 * @param mobile 手机号
	 * @return 商铺用户对象
	 */
	ShopUser getByMobile(String mobile);

	/**
	 * 判断用户名是否存在
	 *
	 * @param userName 用户名
	 * @return 存在返回 true，否则返回 false
	 */
	boolean isUserExist(String userName);

	/**
	 * 根据用户ID获取手机号
	 *
	 * @param userId 用户ID
	 * @return 用户手机号
	 */
	String getMobileByUserId(Long userId);

	/**
	 * 更新用户手机号
	 *
	 * @param id     用户ID
	 * @param mobile 手机号
	 * @return 更新成功返回 true，否则返回 false
	 */
	boolean updateMobileByUserId(Long id, String mobile);


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
	boolean updateAvatar(Long userId, String avatar);

	/**
	 * 查询新入驻商家数量
	 *
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @return Integer-入驻商家数量
	 */
	Integer getNewShopData(Date startDate, Date endDate);

	/**
	 * 根据店铺ID来查询商家信息
	 *
	 * @param shopId 店铺ID
	 * @return
	 */
	ShopUser getByShopId(Long shopId);

	/**
	 * 锁定用户
	 *
	 * @param username
	 * @param status
	 * @return
	 */
	int updateStatusByUserName(String username, Boolean status);
}
