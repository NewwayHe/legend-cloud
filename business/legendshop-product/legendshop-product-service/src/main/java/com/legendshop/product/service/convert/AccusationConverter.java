/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.product.bo.AccusationBO;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.dto.AccusationDTO;
import com.legendshop.product.dto.AccusationUserTypeDTO;
import com.legendshop.product.entity.Accusation;
import com.legendshop.product.excel.IllegalExportDTO;
import org.mapstruct.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface AccusationConverter extends BaseConverter<Accusation, AccusationDTO> {


	AccusationBO toBO(Accusation accusation);


	/**
	 * 组装成为Entity
	 *
	 * @param accusationDTO
	 * @param userTypeDTO
	 * @return
	 */
	default Accusation fromDTO(AccusationDTO accusationDTO, AccusationUserTypeDTO userTypeDTO) {
		Accusation accusation = from(accusationDTO);
		accusation.setUserId(userTypeDTO.getUserId());
		accusation.setUserName(userTypeDTO.getUserName());
		accusation.setUserType(userTypeDTO.getUserType());

		accusation.setCreateTime(new Date());
		accusation.setUpdateTime(new Date());
		if (CollUtil.isNotEmpty(accusationDTO.getPicList())) {
			accusation.setPic(StrUtil.join(",", accusationDTO.getPicList()));
		}
		return accusation;
	}

	/**
	 * ProductBO to IllegalExportDTO
	 *
	 * @param productPage
	 * @return
	 */
	List<IllegalExportDTO> convert2IllegalExportDTO(List<ProductBO> productPage);
}
