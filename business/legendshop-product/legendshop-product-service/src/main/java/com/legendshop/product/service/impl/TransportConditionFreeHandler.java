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
import com.legendshop.product.dto.TransFreeDTO;
import com.legendshop.product.enums.TransCityTypeEnum;
import com.legendshop.product.enums.TransFreeTypeEnum;
import com.legendshop.product.service.AbstractTransportHandler;
import com.legendshop.product.service.TransCityService;
import com.legendshop.product.service.TransFreeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 条件包邮处理类
 *
 * @author legendshop
 */
@Service
public class TransportConditionFreeHandler extends AbstractTransportHandler {

	@Autowired
	private TransCityService transCityService;

	@Autowired
	private TransFreeService transFreeService;

	@Resource(name = "transportFeeHandler")
	private AbstractTransportHandler transportHandler;


	@Override
	public TransportBO getWithItems(TransportBO transport, Long transId) {
		if (transport.getConditionFreeFlag()) {
			List<TransFreeDTO> transFreeDTOS = transFreeService.getListDetailAreaByTransId(transId);
			transport.setTransFreeDTOList(transFreeDTOS);
		}
		return this.transportHandler.getWithItems(transport, transId);
	}

	@Override
	public R calculateTransFee(List<TransFeeCalProductDTO> calProductDTOList, TransportBO transport, Long cityId) {

		if (transport.getConditionFreeFlag()) {
			TransCityDTO cityDTO = transCityService.getByTransIdAndType(transport.getId(), cityId, TransCityTypeEnum.CONDITION_FREE);
			if (null == cityDTO) {
				return this.transportHandler.calculateTransFee(calProductDTOList, transport, cityId);
			}
			TransFreeDTO transFreeDTO = transFreeService.getById(cityDTO.getParentId());
			if (null == transFreeDTO) {
				return this.transportHandler.calculateTransFee(calProductDTOList, transport, cityId);
			}
			int dtoType = transFreeDTO.getType();
			if (dtoType == TransFreeTypeEnum.FULL_NUM.value()) { //满件包邮
				int sum = calProductDTOList.stream().mapToInt(TransFeeCalProductDTO::getTotalCount).sum();
				if (sum >= transFreeDTO.getNum()) {
					return R.ok(BigDecimal.ZERO);
				}
			} else { //满额包邮
				BigDecimal totalAmount = calProductDTOList.stream().map(TransFeeCalProductDTO::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
				if (totalAmount.compareTo(transFreeDTO.getPrice()) >= 0) {
					return R.ok(BigDecimal.ZERO);
				}
			}
		}
		return this.transportHandler.calculateTransFee(calProductDTOList, transport, cityId);
	}

//	public static void main(String[] args) {
//		double sum =  6;
//		Integer s = 7;
//		System.out.println(sum >= s);
//
//		BigDecimal bigDecimal = new BigDecimal("7");
//		BigDecimal b = new BigDecimal("7");
//		System.out.println(bigDecimal.compareTo(b));
//		System.out.println(bigDecimal.compareTo(b) >= 0);
//	}


}
