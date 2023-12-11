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
import com.legendshop.product.dto.TransFeeCalProductDTO;
import com.legendshop.product.enums.TransCalFeeResultEnum;
import com.legendshop.product.enums.TransCityTypeEnum;
import com.legendshop.product.service.AbstractTransportHandler;
import com.legendshop.product.service.TransCityService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 包邮处理类
 *
 * @author legendshop
 */
@Service
public class TransportFreePostageHandler extends AbstractTransportHandler {

	@Autowired
	private TransCityService transCityService;

	@Resource(name = "transportConstantHandler")
	private AbstractTransportHandler transportHandler;


	@Override
	public TransportBO getWithItems(TransportBO transportBO, Long transId) {
		if (transportBO.getFreeFlag()) {
			List<TransCityDTO> cityList = transCityService.getCityList(transId, -1L);
			transportBO.setArea(transCityService.getDetailAreaNames(cityList));
			transportBO.setTransCityDTOList(cityList);
			return transportBO;
		}
		return this.transportHandler.getWithItems(transportBO, transId);
	}

	@Override
	public R calculateTransFee(List<TransFeeCalProductDTO> calProductDTOList, TransportBO transport, Long cityId) {
		if (transport.getFreeFlag()) {
			TransCityDTO cityDTO = transCityService.getByTransIdAndType(transport.getId(), cityId, TransCityTypeEnum.AREA_LIMIT);
			if (null == cityDTO) {
				return R.fail(TransCalFeeResultEnum.NOT_SUPPORT_AREA.getCode(), TransCalFeeResultEnum.NOT_SUPPORT_AREA.getMsg(), calProductDTOList);
			}
			return R.ok(BigDecimal.ZERO);
		}
		//不满足条件交由下一个责任链处理
		return this.transportHandler.calculateTransFee(calProductDTOList, transport, cityId);
	}
}
