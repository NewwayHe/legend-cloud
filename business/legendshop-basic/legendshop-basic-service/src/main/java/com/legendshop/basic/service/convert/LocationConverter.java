/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.convert;

import com.legendshop.basic.dto.CityDTO;
import com.legendshop.basic.dto.LocationDTO;
import com.legendshop.basic.dto.ProvinceDTO;
import com.legendshop.basic.entity.Location;
import com.legendshop.common.core.service.BaseConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 地区表(Location)转换器
 *
 * @author legendshop
 * @since 2020-10-14 18:18:07
 */
@Mapper
public interface LocationConverter extends BaseConverter<Location, LocationDTO> {

	/**
	 * Location to ProvinceDTO
	 *
	 * @param province
	 * @return
	 */
	@Mapping(target = "children", ignore = true)
	ProvinceDTO convert2ProvinceDTO(Location province);

	/**
	 * List<Location> to List<CityDTO>
	 *
	 * @param city
	 * @return
	 */
	List<CityDTO> convert2CityDTO(List<Location> city);

	/**
	 * Location to CityDTO
	 *
	 * @param city
	 * @return
	 */
	CityDTO convert2CityDTO(Location city);
}
