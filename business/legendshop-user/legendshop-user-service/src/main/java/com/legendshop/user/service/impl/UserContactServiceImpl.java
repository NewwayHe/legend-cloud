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
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.user.bo.UserAddressBO;
import com.legendshop.user.bo.UserContactBO;
import com.legendshop.user.dao.UserContactDao;
import com.legendshop.user.dto.UserContactDTO;
import com.legendshop.user.entity.UserContact;
import com.legendshop.user.query.UserContactQuery;
import com.legendshop.user.service.UserContactService;
import com.legendshop.user.service.convert.UserContactConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户提货信息
 *
 * @author legendshop
 */
@Slf4j
@Service
public class UserContactServiceImpl extends BaseServiceImpl<UserContactDTO, UserContactDao, UserContactConverter> implements UserContactService {

	@Autowired
	private UserContactDao userContactDao;

	@Autowired
	private UserContactConverter userContactConverter;


	@Override
	public Long getUserContactCount(Long userId) {
		return userContactDao.getUserContactCount(userId);
	}


	@Override
	public int updateDefaultUserContact(Long contactId, Long userId) {
		return userContactDao.updateDefaultUserContact(contactId, userId);
	}

	@Override
	public PageSupport<UserContactDTO> queryPage(UserContactQuery query) {
		return userContactDao.queryPage(query);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long saveContact(UserContactDTO dto) {
		dto.setCreateTime(DateUtil.date());
		dto.setUpdateTime(DateUtil.date());
		Long id = userContactDao.save(userContactConverter.from(dto));
		if (id > 0 && dto.getDefaultFlag()) {
			//检查当前保存的提货信息是否选择设置为默认
			userContactDao.updateOtherDefault(id, dto.getUserId(), "0");
		}
		return id;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateContact(UserContactDTO dto) {
		dto.setUpdateTime(DateUtil.date());
		int i = userContactDao.update(userContactConverter.from(dto));
		if (i > 0 && dto.getDefaultFlag()) {
			//检查当前保存的地址是否选择设置为默认
			userContactDao.updateOtherDefault(dto.getId(), dto.getUserId(), "0");
		}
		return i;
	}

	@Override
	public UserContactBO getDefaultUserContact(Long userId) {
		return userContactConverter.entityToBo(userContactDao.getDefaultUserContact(userId));
	}


	@Override
	public UserContactBO getUserContactInfo(Long id) {
		return userContactConverter.entityToBo(userContactDao.getUserContactInfo(id));
	}

	@Override
	public UserContactBO getUserContactForOrder(Long userId, Long contactId) {

		if (ObjectUtil.isNull(contactId)) {
			UserContactBO defaultUserContact = this.getDefaultUserContact(userId);
			if (ObjectUtil.isNotNull(defaultUserContact)) {
				return defaultUserContact;
			}
			List<UserContact> userContactForOrder = userContactDao.getUserContactForOrder(userId);
			if (CollectionUtil.isNotEmpty(userContactForOrder)) {
				return userContactConverter.entityToBo(userContactForOrder.get(0));
			}
		} else {
			return this.getUserContactInfo(contactId);
		}
		return null;
	}

	@Override
	public boolean deleteUserAddress(Long userId, Long id) {
		return this.deleteById(id);
	}


	@Override
	public List<UserContact> queryByUserId(Long userId) {
		return userContactDao.queryByUserId(userId);
	}

	@Override
	public UserContactBO getBoById(Long id) {
		return userContactConverter.entityToBo(userContactDao.getById(id));
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
