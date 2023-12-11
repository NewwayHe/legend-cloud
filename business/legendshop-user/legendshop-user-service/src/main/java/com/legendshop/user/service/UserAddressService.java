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
import com.legendshop.common.core.service.BaseService;
import com.legendshop.user.bo.UserAddressBO;
import com.legendshop.user.dto.UserAddressDTO;
import com.legendshop.user.entity.UserAddress;
import com.legendshop.user.query.UserAddressQuery;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户地址服务.
 *
 * @author legendshop
 */
public interface UserAddressService extends BaseService<UserAddressDTO> {


	/**
	 * 获取特定用户的地址数量。
	 *
	 * @param id 用户ID
	 * @return 地址数量
	 */
	Long getUserAddressCount(Long id);

	/**
	 * 更新特定用户的默认地址。
	 *
	 * @param addrId 地址ID
	 * @param userId 用户ID
	 * @return 更新操作结果：成功（1），失败（0）
	 */
	int updateDefaultUserAddress(Long addrId, Long userId);

	/**
	 * 分页查询用户的地址列表。
	 *
	 * @param userAddressQuery 用户地址查询条件
	 * @return 分页支持对象，包含用户地址列表
	 */
	PageSupport<UserAddressDTO> queryPage(UserAddressQuery userAddressQuery);

	/**
	 * 更新除当前地址外的其他默认地址的标识。
	 *
	 * @param addrId     当前地址ID，除此地址外的地址将被更新
	 * @param userId     用户ID
	 * @param commonAddr 是否默认地址：0 - 非默认，1 - 默认
	 * @return 更新操作结果：成功（1），失败（0）
	 */
	int updateOtherDefault(Long addrId, Long userId, String commonAddr);

	/**
	 * 保存用户地址并将其设置为默认地址。
	 *
	 * @param userAddress 用户地址DTO
	 * @param userId      用户ID
	 * @return 新增的地址ID
	 */
	Long saveUserAddressAndDefault(UserAddressDTO userAddress, Long userId);


	Long getMaxNumber(String userName);


	/**
	 * 新增收货地址
	 *
	 * @param address
	 * @return
	 */
	Long saveAddress(UserAddressDTO address);

	/**
	 * 编辑收货地址
	 *
	 * @param address
	 * @return
	 */
	int updateAddress(UserAddressDTO address);

	/**
	 * 获取用户默认地址
	 *
	 * @param userId
	 * @return
	 */
	UserAddressBO getDefaultAddress(Long userId);

	/**
	 * 根据ID获取用户收货地址信息
	 *
	 * @param id
	 * @return
	 */
	UserAddressBO getAddressInfo(Long id);

	/**
	 * 获取收货地址给下单用
	 *
	 * @param userId    用户ID
	 * @param addressId 用户选择的地址ID
	 * @return
	 */
	UserAddressBO getUserAddressForOrder(Long userId, Long addressId);

	/**
	 * 删除用户收货地址
	 *
	 * @param userId
	 * @param id
	 * @return
	 */
	boolean deleteUserAddress(Long userId, Long id);

	/**
	 * 获取用户的默认收货地址
	 *
	 * @param userId
	 * @param request
	 * @return
	 */
	UserAddressBO getCommonAddress(Long userId, HttpServletRequest request);

	List<UserAddress> queryByUserId(Long userId);
}
