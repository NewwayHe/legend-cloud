/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.legendshop.basic.api.SysParamItemApi;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.dto.SysParamsDTO;
import com.legendshop.basic.enums.SysParamGroupEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.shop.dao.ShopParamItemDao;
import com.legendshop.shop.dao.ShopParamsDao;
import com.legendshop.shop.dto.ShopParamItemDTO;
import com.legendshop.shop.dto.ShopParamsDTO;
import com.legendshop.shop.entity.ShopParamItem;
import com.legendshop.shop.entity.ShopParams;
import com.legendshop.shop.service.ShopParamItemService;
import com.legendshop.shop.service.ShopParamsService;
import com.legendshop.shop.service.convert.ShopParamsConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商家主配置(ShopParams)表服务实现类
 *
 * @author legendshop
 * @since 2020-11-03 11:00:08
 */
@Service
@AllArgsConstructor
public class ShopParamsServiceImpl extends BaseServiceImpl<ShopParamsDTO, ShopParamsDao, ShopParamsConverter> implements ShopParamsService {

	private final ShopParamsDao shopParamsDao;
	private final ShopParamItemDao shopParamItemDao;
	private final ShopParamItemService shopParamItemService;
	private final SysParamsApi sysParamsApi;
	private final SysParamItemApi sysParamItemApi;
	private final ShopParamsConverter converter;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Void> initShopParams(Long shopId) {
		List<SysParamsDTO> sysParamsDTOS = sysParamsApi.getByGroup(SysParamGroupEnum.SHOP).getData();
		List<ShopParams> shopParamsList = new ArrayList<>();
		List<ShopParams> shopParamsList1 = shopParamsDao.queryByShopId(shopId);
		List<Long> shopParamsIds = shopParamsList1.stream().map(ShopParams::getId).collect(Collectors.toList());
		shopParamsDao.deleteById(shopParamsIds);
		if (CollectionUtil.isNotEmpty(shopParamsIds)) {
			shopParamItemDao.deleteByShopId(shopParamsIds);
		}
		if (CollUtil.isNotEmpty(sysParamsDTOS)) {
			for (SysParamsDTO sysParamsDTO : sysParamsDTOS) {
				ShopParams shopParams = new ShopParams();
				BeanUtil.copyProperties(sysParamsDTO, shopParams, CopyOptions.create().ignoreNullValue());
				Date date = new Date();
				shopParams.setCreateTime(date);
				shopParams.setUpdateTime(null);
				shopParams.setId(shopParamsDao.createId());
				shopParams.setShopId(shopId);
				List<SysParamItemDTO> data = sysParamItemApi.getListByParentId(sysParamsDTO.getId()).getData();
				List<ShopParamItem> item = new ArrayList<>();
				if (CollUtil.isNotEmpty(data)) {
					for (SysParamItemDTO paramItemDTO : data) {
						ShopParamItem shopParamItem = new ShopParamItem();
						BeanUtil.copyProperties(paramItemDTO, shopParamItem, CopyOptions.create().ignoreNullValue());
						shopParamItem.setCreateTime(date);
						shopParamItem.setUpdateTime(null);
						shopParamItem.setParentId(shopParams.getId());
						item.add(shopParamItem);
					}
				}
				shopParamItemDao.save(item);
				shopParamsList.add(shopParams);
			}
			shopParamsDao.saveWithId(shopParamsList);
		}
		return R.ok();
	}

	@Override
	public <T> T getConfigDtoByParamName(Long shopId, String name, Class<T> clazz) {
		ShopParamsDTO dto = getByName(shopId, name);
		return shopParamItemService.getConfigDtoByParentId(dto.getId(), clazz);
	}

	@Override
	public ShopParamsDTO getByName(Long shopId, String name) {
		return converter.to(shopParamsDao.getByName(shopId, name));
	}

	@Override
	public List<ShopParamItemDTO> getShopParamItemsByParamName(String name, Long shopId) {
		return shopParamItemDao.queryByParentParamName(name, shopId);
	}
}
