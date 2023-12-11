/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.api.LocationApi;
import com.legendshop.basic.enums.LocationGradeEnum;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.common.region.core.IpInfoDTO;
import com.legendshop.common.region.service.RegionSearcherService;
import com.legendshop.user.bo.UserAddressBO;
import com.legendshop.user.dao.UserAddressDao;
import com.legendshop.user.dao.UserDetailDao;
import com.legendshop.user.dto.UserAddressDTO;
import com.legendshop.user.entity.UserAddress;
import com.legendshop.user.entity.UserDetail;
import com.legendshop.user.query.UserAddressQuery;
import com.legendshop.user.service.UserAddressService;
import com.legendshop.user.service.convert.UserAddressConverter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户地址服务.
 *
 * @author legendshop
 */
@Slf4j
@Service
public class UserAddressServiceImpl extends BaseServiceImpl<UserAddressDTO, UserAddressDao, UserAddressConverter> implements UserAddressService {

	@Autowired
	private UserAddressDao userAddressDao;

	@Autowired
	private UserAddressConverter userAddressConverter;

	@Autowired
	private RegionSearcherService regionSearcherService;

	@Autowired
	private LocationApi locationApi;

	@Autowired
	private UserDetailDao userDetailDao;


	@Override
	public Long getUserAddressCount(Long userId) {
		return userAddressDao.getUserAddressCount(userId);
	}


	@Override
	public int updateDefaultUserAddress(Long addrId, Long userId) {
		return userAddressDao.updateDefaultUserAddress(addrId, userId);
	}


	@Override
	public PageSupport<UserAddressDTO> queryPage(UserAddressQuery userAddressQuery) {
		return userAddressDao.queryPage(userAddressQuery);
	}


	@Override
	public int updateOtherDefault(Long addrId, Long userId, String commonAddr) {
		return userAddressDao.updateOtherDefault(addrId, userId, commonAddr);
	}

	/**
	 * 保存或者更新,且设置为默认地址
	 **/
	@Override
	public Long saveUserAddressAndDefault(UserAddressDTO userAddress, Long userId) {
		if (ObjectUtil.isNotEmpty(userAddress.getId())) {
			update(userAddress);
			Long addrId = userAddress.getId();
			if (addrId != null) {
				userAddressDao.updateDefaultUserAddress(addrId, userId);
			}
			return addrId;
		} else {//保存且设置为默认地址
			Long addrId = userAddressDao.save(userAddressConverter.from(userAddress));
			if (addrId != null) {
				userAddressDao.updateDefaultUserAddress(addrId, userId);
			}
			return addrId;
		}
	}

	@Override
	public Long getMaxNumber(String userName) {
		return userAddressDao.getMaxNumber(userName);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long saveAddress(UserAddressDTO address) {
		address.setCreateTime(DateUtil.date());
		Long id = userAddressDao.save(userAddressConverter.from(address));
		if (id > 0 && address.getCommonFlag()) {
			//检查当前保存的地址是否选择设置为默认
			userAddressDao.updateOtherDefault(id, address.getUserId(), "0");
			UserDetail userDetail = userDetailDao.getUserDetailById(address.getUserId());
			userDetail.setProvinceId(address.getProvinceId());
			userDetail.setCityId(address.getCityId());
			userDetailDao.update(userDetail);
		}
		return id;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateAddress(UserAddressDTO address) {
		address.setUpdateTime(DateUtil.date());
		if (ObjectUtil.isEmpty(address.getAreaId())) {
			address.setAreaId(String.valueOf(address.getCityId()));
		}
		int i = userAddressDao.update(userAddressConverter.from(address));
		if (i > 0 && address.getCommonFlag()) {
			//检查当前保存的地址是否选择设置为默认
			userAddressDao.updateOtherDefault(address.getId(), address.getUserId(), "0");
			UserDetail userDetail = userDetailDao.getUserDetailById(address.getUserId());
			userDetail.setProvinceId(address.getProvinceId());
			userDetail.setCityId(address.getCityId());
			userDetailDao.update(userDetail);
		}
		return i;
	}

	@Override
	public UserAddressBO getDefaultAddress(Long userId) {
		return userAddressDao.getDefaultAddress(userId);
	}


	@Override
	public UserAddressBO getAddressInfo(Long id) {
		return userAddressDao.getAddressInfo(id);
	}

	@Override
	public UserAddressBO getUserAddressForOrder(Long userId, Long addressId) {

		if (ObjectUtil.isNull(addressId)) {
			UserAddressBO defaultAddress = this.getDefaultAddress(userId);
			if (ObjectUtil.isNotNull(defaultAddress)) {
				return defaultAddress;
			}
			List<UserAddressBO> userAddressForOrder = userAddressDao.getUserAddressForOrder(userId);
			if (CollectionUtil.isNotEmpty(userAddressForOrder)) {
				return userAddressForOrder.get(0);
			}
		} else {
			return this.getAddressInfo(addressId);
		}
		return null;
	}

	@Override
	public boolean deleteUserAddress(Long userId, Long id) {
		return this.deleteById(id);
	}

	@Override
	public UserAddressBO getCommonAddress(Long userId, HttpServletRequest request) {
		//用户有登录，则获取其默认地址
		if (ObjectUtil.isNotNull(userId)) {
			UserAddressBO userAddressBO = userAddressDao.getDefaultAddress(userId);
			if (ObjectUtil.isNotEmpty(userAddressBO)) {
				return userAddressBO;
			}
		}
		//如果用户没有登陆或者默认地址为空，则通过其ip地址获取所在区域
		String clientIP = JakartaServletUtil.getClientIP(request);
		//检查ip地址是否合法
		if (Validator.isIpv4(clientIP)) {
			//获取区域新信息
			IpInfoDTO ipInfoDTO = regionSearcherService.binarySearch(clientIP);
			String province = ipInfoDTO.getProvince();
			String city = ipInfoDTO.getCity();
			if (ObjectUtil.isNotEmpty(province) && ObjectUtil.isNotEmpty(city)) {
				log.info("获取用户当前位置地址，省份ID: {}，城市ID: {}", province, city);
				Long provinceId = locationApi.getIdByName(province, LocationGradeEnum.PROVINCE.getValue()).getData();
				Long cityId = locationApi.getIdByName(city, LocationGradeEnum.CITY.getValue()).getData();
				if (ObjectUtil.isNotNull(provinceId) && ObjectUtil.isNotNull(cityId)) {
					UserAddressBO userAddressBO = new UserAddressBO();
					userAddressBO.setProvinceId(provinceId);
					userAddressBO.setProvinceName(province);
					userAddressBO.setCityId(cityId);
					userAddressBO.setCityName(city);
					return userAddressBO;
				}
			}
			//如果通过ip没法获取到地址，则使用默认地址
			return setDefaultAddress();
		}
		//如果ip不合法，则使用默认地址
		//如果通过ip没法获取到地址，则使用默认地址
		return setDefaultAddress();
	}

	@Override
	public List<UserAddress> queryByUserId(Long userId) {
		return userAddressDao.queryByUserId(userId);
	}

	/**
	 * 设置默认地址
	 */
	private UserAddressBO setDefaultAddress() {
		UserAddressBO userAddressBO = new UserAddressBO();
		userAddressBO.setProvinceId(19L);
		userAddressBO.setProvinceName("广东省");
		userAddressBO.setCityId(199L);
		userAddressBO.setCityName("广州市");
		return userAddressBO;
	}
}
