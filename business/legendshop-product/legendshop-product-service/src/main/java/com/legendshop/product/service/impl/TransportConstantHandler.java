/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.TransportBO;
import com.legendshop.product.dto.TransCityDTO;
import com.legendshop.product.dto.TransConstFeeDTO;
import com.legendshop.product.dto.TransFeeCalProductDTO;
import com.legendshop.product.enums.TransCalFeeResultEnum;
import com.legendshop.product.enums.TransCityTypeEnum;
import com.legendshop.product.enums.TransTypeEnum;
import com.legendshop.product.service.AbstractTransportHandler;
import com.legendshop.product.service.TransCityService;
import com.legendshop.product.service.TransConstFeeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 固定运费处理类
 *
 * @author legendshop
 */
@Service
public class TransportConstantHandler extends AbstractTransportHandler {

	@Autowired
	private TransCityService transCityService;

	@Autowired
	private TransConstFeeService transConstFeeService;

	@Resource(name = "transportConditionFreeHandler")
	private AbstractTransportHandler transportHandler;

	@Override
	public TransportBO getWithItems(TransportBO transportBO, Long transId) {
		if (transportBO.getTransType().equals(TransTypeEnum.CONSTANT.value())) {
			List<TransConstFeeDTO> transConstFeeDTOS = transConstFeeService.getListDetailAreaByTransId(transId);
			transportBO.setTransConstFeeDTOList(transConstFeeDTOS);
			return transportBO;
		}
		return this.transportHandler.getWithItems(transportBO, transId);
	}

	@Override
	public R calculateTransFee(List<TransFeeCalProductDTO> calProductDTOList, TransportBO transport, Long cityId) {

		if (transport.getTransType().equals(TransTypeEnum.CONSTANT.value())) {
			Long transId = transport.getId();
			TransCityDTO cityDTO = transCityService.getByTransIdAndType(transId, cityId, TransCityTypeEnum.CONSTANT_FREIGHT);
			if (null == cityDTO) {
				return R.fail(TransCalFeeResultEnum.NOT_SUPPORT_AREA.getCode(), TransCalFeeResultEnum.NOT_SUPPORT_AREA.getMsg(), calProductDTOList);
			}
			TransConstFeeDTO constFeeDTO = transConstFeeService.getById(cityDTO.getParentId());
			if (null == constFeeDTO) {
				return R.fail(TransCalFeeResultEnum.NOT_FOUNT_RULE.getCode(), TransCalFeeResultEnum.NOT_FOUNT_RULE.getMsg());
			}
			BigDecimal constantPrice = constFeeDTO.getConstantPrice();
			return R.ok(constantPrice);
		}
		return this.transportHandler.calculateTransFee(calProductDTOList, transport, cityId);
	}
}
