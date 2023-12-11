/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.convert;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.order.bo.PreSellOrderBO;
import com.legendshop.order.dto.PreSellOrderDTO;
import com.legendshop.order.entity.PreSellOrder;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface PreSellOrderConverter extends BaseConverter<PreSellOrder, PreSellOrderDTO> {

	/**
	 * to bo
	 *
	 * @param preSellOrderDTO
	 * @return
	 */
	PreSellOrderBO convert2BO(PreSellOrderDTO preSellOrderDTO);


	/**
	 * PreSellOrderDTO to PreSellOrderBO
	 *
	 * @param preSellOrderDTOList
	 * @return
	 */
	List<PreSellOrderBO> convert2BO(List<PreSellOrderDTO> preSellOrderDTOList);
}
