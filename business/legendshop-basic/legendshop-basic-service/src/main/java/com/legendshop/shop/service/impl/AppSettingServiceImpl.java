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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.SysParamItemApi;
import com.legendshop.basic.api.SysParamsApi;
import com.legendshop.basic.dto.BatchUpdateSysParamDTO;
import com.legendshop.basic.dto.CategorySettingDTO;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.dto.SysParamValueItemDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.shop.enums.DecorateSettingEnum;
import com.legendshop.shop.service.AppSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author legendshop
 */
@Service
@RequiredArgsConstructor
public class AppSettingServiceImpl implements AppSettingService {

	private final SysParamsApi sysParamsApi;

	private final SysParamItemApi sysParamItemApi;

	private final ObjectMapper mapper = new ObjectMapper();


	@Override
	public CategorySettingDTO getCategorySetting() {
		// 获取分类设置
		R<List<SysParamItemDTO>> result = sysParamsApi.getSysParamItemsByParamName(SysParamNameEnum.DECORATE_SETTING.name());
		if (!result.success()) {
			return null;
		}
		//转换
		List<SysParamItemDTO> data = result.getData();
		CategorySettingDTO dto = new CategorySettingDTO();
		data.stream().forEach(a -> {
			if (Objects.equals(a.getKeyWord(), DecorateSettingEnum.GRADE_ONE.getKey())) {
				dto.setCategory(Integer.parseInt(a.getValue()));
			}
			if (Objects.equals(a.getKeyWord(), DecorateSettingEnum.HAS_IMAGE.getKey())) {
				dto.setSchema(Integer.parseInt(a.getValue()));
			}
			if (Objects.equals(a.getKeyWord(), DecorateSettingEnum.HAS_ADVERT.getKey())) {
				dto.setShowCatAdvert(Integer.parseInt(a.getValue()));
			}
			if (Objects.equals(a.getKeyWord(), DecorateSettingEnum.HAS_GOODS.getKey())) {
				dto.setGoods(Integer.parseInt(a.getValue()));
			}

		});
		return dto;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Void> updateCategorySetting(CategorySettingDTO categorySetting) {
		if (ObjectUtil.isNull(categorySetting)) {
			throw new BusinessException("配置值不能为空");
		}

		//读取配置，DECORATE_SETTING
		R<List<SysParamItemDTO>> items = sysParamsApi.getSysParamItemsByParamName(SysParamNameEnum.DECORATE_SETTING.name());

		//封装配置对象
		BatchUpdateSysParamDTO dto = new BatchUpdateSysParamDTO();
		dto.setName(SysParamNameEnum.DECORATE_SETTING.name());
		List<SysParamValueItemDTO> list = new ArrayList<>();

		items.getData().forEach(u -> {
			SysParamValueItemDTO itemDTO = new SysParamValueItemDTO();
			//设置分类级别
			if (u.getKeyWord().equals(DecorateSettingEnum.GRADE_ONE.getKey())) {
				itemDTO.setId(u.getId());
				itemDTO.setValue(String.valueOf(categorySetting.getCategory()));
			}
			//设置图片模式
			if (u.getKeyWord().equals(DecorateSettingEnum.HAS_IMAGE.getKey())) {
				itemDTO.setId(u.getId());
				itemDTO.setValue(String.valueOf(categorySetting.getSchema()));
			}
			//设置是否展示分类广告
			if (u.getKeyWord().equals(DecorateSettingEnum.HAS_NOT_ADVERT.getKey())) {
				itemDTO.setId(u.getId());
				itemDTO.setValue(String.valueOf(categorySetting.getShowCatAdvert()));
			}
			//设置是否展示商品
			if (u.getKeyWord().equals(DecorateSettingEnum.HAS_GOODS.getKey())) {
				itemDTO.setId(u.getId());
				itemDTO.setValue(String.valueOf(categorySetting.getGoods()));
			}
			list.add(itemDTO);
		});
		dto.setSysParamValueItemDTOS(list);
		return sysParamItemApi.updateValueOnlyById(dto);
	}
}
