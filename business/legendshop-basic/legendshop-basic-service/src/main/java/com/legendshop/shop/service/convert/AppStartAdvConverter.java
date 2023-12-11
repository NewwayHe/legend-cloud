/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.convert;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.shop.dto.AppStartAdvDTO;
import com.legendshop.shop.entity.AppStartAdv;
import org.mapstruct.Mapper;

import java.util.HashMap;

/**
 * @author legendshop
 */
@Mapper
public interface AppStartAdvConverter extends BaseConverter<AppStartAdv, AppStartAdvDTO> {

	@Override
	default AppStartAdv from(AppStartAdvDTO arg0) {
		AppStartAdv advSortImg = new AppStartAdv();
		advSortImg.setId(arg0.getId());
		advSortImg.setName(arg0.getName());
		advSortImg.setDescription(arg0.getDescription());
		advSortImg.setUrl(JSONUtil.toJsonStr(arg0.getUrl()));
		advSortImg.setDescription(arg0.getDescription());
		advSortImg.setStatus(arg0.getStatus());
		advSortImg.setCreateTime(arg0.getCreateTime());
		advSortImg.setImgUrl(arg0.getImgUrl());
		return advSortImg;
	}

	@Override
	default AppStartAdvDTO to(AppStartAdv arg0) {
		if (arg0 == null) {
			return null;
		}
		AppStartAdvDTO advSortImg = new AppStartAdvDTO();
		advSortImg.setId(arg0.getId());
		advSortImg.setName(arg0.getName());
		advSortImg.setDescription(arg0.getDescription());
		advSortImg.setUrl(JSONUtil.toBean(arg0.getUrl(), new TypeReference<HashMap<String, Object>>() {
		}, true));
		advSortImg.setDescription(arg0.getDescription());
		advSortImg.setStatus(arg0.getStatus());
		advSortImg.setCreateTime(arg0.getCreateTime());
		advSortImg.setImgUrl(arg0.getImgUrl());
		return advSortImg;
	}
}
