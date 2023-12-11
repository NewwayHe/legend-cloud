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
import com.legendshop.shop.bo.AdvertiseBO;
import com.legendshop.shop.dto.AdvertiseDTO;
import com.legendshop.shop.entity.Advertise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashMap;
import java.util.List;

/**
 * (Advertise)转换器
 *
 * @author legendshop
 * @since 2022-04-27 15:23:37
 */
@Mapper
public interface AdvertiseConverter extends BaseConverter<Advertise, AdvertiseDTO> {
	/**
	 * 转成BO对象
	 *
	 * @param
	 * @return
	 */
	@Mapping(target = "url", expression = "java(cn.hutool.json.JSONUtil.toBean(advertise.getUrl(), new cn.hutool.core.lang.TypeReference<java.util.HashMap<String, Object>>() {}, true))")
	AdvertiseBO advertiseBO(Advertise advertise);

	List<AdvertiseBO> advertiseBO(List<Advertise> advertise);


	@Override
	default Advertise from(AdvertiseDTO arg0) {
		if (arg0 == null) {
			return null;
		}

		Advertise advertise = new Advertise();

		advertise.setId(arg0.getId());
		advertise.setTitle(arg0.getTitle());
		advertise.setAdvertiseUesrType(arg0.getAdvertiseUesrType());
		advertise.setSource(arg0.getSource());
		advertise.setAdvertisePage(arg0.getAdvertisePage());
		advertise.setAdvertiseFrequency(arg0.getAdvertiseFrequency());
		advertise.setUrl(JSONUtil.toJsonStr(arg0.getUrl()));
		advertise.setPhotos(arg0.getPhotos());
		advertise.setSeq(arg0.getSeq());
		advertise.setStatus(arg0.getStatus());
		advertise.setStartTime(arg0.getStartTime());
		advertise.setEndTime(arg0.getEndTime());
		advertise.setClickLimit(arg0.getClickLimit());
		advertise.setCreateTime(arg0.getCreateTime());
		advertise.setUpdateTime(arg0.getUpdateTime());
		advertise.setCount(arg0.getCount());

		return advertise;
	}

	@Override
	default AdvertiseDTO to(Advertise arg0) {
		if (arg0 == null) {
			return null;
		}

		AdvertiseDTO advertiseDTO = new AdvertiseDTO();

		advertiseDTO.setId(arg0.getId());
		advertiseDTO.setTitle(arg0.getTitle());
		advertiseDTO.setAdvertiseUesrType(arg0.getAdvertiseUesrType());
		advertiseDTO.setSource(arg0.getSource());
		advertiseDTO.setAdvertisePage(arg0.getAdvertisePage());
		advertiseDTO.setAdvertiseFrequency(arg0.getAdvertiseFrequency());
		advertiseDTO.setUrl(JSONUtil.toBean(arg0.getUrl(), new TypeReference<HashMap<String, Object>>() {
		}, true));
		advertiseDTO.setPhotos(arg0.getPhotos());
		advertiseDTO.setSeq(arg0.getSeq());
		advertiseDTO.setStatus(arg0.getStatus());
		advertiseDTO.setStartTime(arg0.getStartTime());
		advertiseDTO.setEndTime(arg0.getEndTime());
		advertiseDTO.setClickLimit(arg0.getClickLimit());
		advertiseDTO.setCreateTime(arg0.getCreateTime());
		advertiseDTO.setUpdateTime(arg0.getUpdateTime());
		advertiseDTO.setCount(arg0.getCount());

		return advertiseDTO;
	}
}

