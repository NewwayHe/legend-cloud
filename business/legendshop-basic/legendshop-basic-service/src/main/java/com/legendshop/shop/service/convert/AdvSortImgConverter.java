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
import com.legendshop.shop.dto.AdvSortImgDTO;
import com.legendshop.shop.entity.AdvSortImg;
import org.mapstruct.Mapper;

import java.util.HashMap;

/**
 * (AdvSortImg)转换器
 *
 * @author legendshop
 * @since 2021-07-09 15:28:05
 */
@Mapper
public interface AdvSortImgConverter extends BaseConverter<AdvSortImg, AdvSortImgDTO> {

	@Override
	default AdvSortImg from(AdvSortImgDTO arg0) {
		AdvSortImg advSortImg = new AdvSortImg();
		advSortImg.setId(arg0.getId());
		advSortImg.setAdvImgName(arg0.getAdvImgName());
		advSortImg.setAdvPath(arg0.getAdvPath());
		advSortImg.setAdvUrl(JSONUtil.toJsonStr(arg0.getAdvUrl()));
		advSortImg.setCategoryId(arg0.getCategoryId());
		advSortImg.setStatus(arg0.getStatus());
		advSortImg.setCreateTime(arg0.getCreateTime());
		advSortImg.setUpdateTime(arg0.getUpdateTime());
		return advSortImg;
	}

	@Override
	default AdvSortImgDTO to(AdvSortImg arg0) {
		if (arg0 == null) {
			return null;
		}
		AdvSortImgDTO advSortImg = new AdvSortImgDTO();
		advSortImg.setId(arg0.getId());
		advSortImg.setAdvImgName(arg0.getAdvImgName());
		advSortImg.setAdvPath(arg0.getAdvPath());
		advSortImg.setAdvUrl(JSONUtil.toBean(arg0.getAdvUrl(), new TypeReference<HashMap<String, Object>>() {
		}, true));
		advSortImg.setCategoryId(arg0.getCategoryId());
		advSortImg.setStatus(arg0.getStatus());
		advSortImg.setCreateTime(arg0.getCreateTime());
		advSortImg.setUpdateTime(arg0.getUpdateTime());
		return advSortImg;
	}
}

