/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.shop.dao.ContactInformationDao;
import com.legendshop.shop.dto.ContactInformationDTO;
import com.legendshop.shop.service.ContactInformationService;
import com.legendshop.shop.service.convert.ShopCustomerInformationConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 商家微信联系方式存储表(ShopCustomerInformation)表服务实现类
 *
 * @author legendshop
 * @since 2021-12-27 09:30:27
 */
@Service
@AllArgsConstructor
public class ContactInformationServiceImpl implements ContactInformationService {

	private final ContactInformationDao contactInformationDao;

	private final ShopCustomerInformationConverter converter;


	@Override
	public ContactInformationDTO getById(Long id) {
		return converter.to(contactInformationDao.getById(id));
	}

	@Override
	public ContactInformationDTO getByShopId(Long shopUserId) {
		if (ObjectUtil.isNull(shopUserId)) {
			shopUserId = 0L;
		}
		return contactInformationDao.getByShopId(shopUserId);
	}


	/**
	 * 新增客服
	 *
	 * @param contactInformationDTO
	 * @return
	 */
	@Override
	public R save(ContactInformationDTO contactInformationDTO) {
		Long shopUserId;
		if (SecurityUtils.getBaseUser().getUserType().equals(UserTypeEnum.ADMIN.getLoginType())) {
			shopUserId = 0L;
			contactInformationDTO.setAdminFlag(true);
		} else {
			//获取商家id
			shopUserId = SecurityUtils.getShopUser().getShopId();
			if (ObjectUtil.isNull(shopUserId)) {
				return R.fail("当前商家不存在，请重试！");
			}
			contactInformationDTO.setAdminFlag(false);

		}
		//查询商家联系方式
		ContactInformationDTO existContactInformationDTO = contactInformationDao.getByShopId(shopUserId);

		if (ObjectUtil.isNull(existContactInformationDTO)) {
			contactInformationDTO.setShopId(shopUserId);
			contactInformationDTO.setCreateTime(new Date());
			contactInformationDTO.setOpenFlag(true);
			return R.ok(contactInformationDao.save(converter.from(contactInformationDTO)), "保存成功！");
		}
		return R.ok(this.update(contactInformationDTO), "修改成功!");
	}


	private int update(ContactInformationDTO contactInformationDTO) {
		contactInformationDTO.setUpdateTime(new Date());
		return contactInformationDao.update(converter.from(contactInformationDTO));
	}

	@Override
	public int deleteById(Long id) {
		return contactInformationDao.deleteById(id);
	}


}
