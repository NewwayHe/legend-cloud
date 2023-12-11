/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.TransportBO;
import com.legendshop.product.dto.TransCityDTO;
import com.legendshop.product.dto.TransConstFeeDTO;
import com.legendshop.product.dto.TransFeeCalProductDTO;
import com.legendshop.product.dto.TransFeeDTO;
import com.legendshop.product.enums.TransCalFeeResultEnum;
import com.legendshop.product.enums.TransCityTypeEnum;
import com.legendshop.product.enums.TransTypeEnum;
import com.legendshop.product.service.AbstractTransportHandler;
import com.legendshop.product.service.TransCityService;
import com.legendshop.product.service.TransConstFeeService;
import com.legendshop.product.service.TransFeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 运费计算方式处理类
 *
 * @author legendshop
 */
@Service
@Slf4j
public class TransportFeeHandler extends AbstractTransportHandler {

	@Autowired
	private TransCityService transCityService;

	@Autowired
	private TransFeeService transFeeService;

	@Autowired
	private TransConstFeeService transConstFeeService;

	@Override
	public TransportBO getWithItems(TransportBO transportBO, Long transId) {
		List<TransFeeDTO> transFeeDTOS = transFeeService.getListDetailAreaByTransId(transId);
		transportBO.setTransFeeDTOList(transFeeDTOS);
		return transportBO;
	}

	@Override
	public R calculateTransFee(List<TransFeeCalProductDTO> calProductDTOList, TransportBO transport, Long cityId) {
		TransCityDTO cityDTO = transCityService.getByTransIdAndType(transport.getId(), cityId, TransCityTypeEnum.FREIGHT_CAL);
		if (null == cityDTO) {
			if (transport.getTransType().equals(TransTypeEnum.CONSTANT.value())) {
				List<TransConstFeeDTO> listAreaByTransId = transConstFeeService.getListAreaByTransId(transport.getId());
				if (null == listAreaByTransId) {
					return R.fail(TransCalFeeResultEnum.NOT_FOUNT_RULE.getCode(), TransCalFeeResultEnum.NOT_FOUNT_RULE.getMsg());
				}

				BigDecimal constantPrice = listAreaByTransId.get(0).getConstantPrice();
				return R.ok(constantPrice);
			}
			return R.fail(TransCalFeeResultEnum.NOT_SUPPORT_AREA.getCode(), TransCalFeeResultEnum.NOT_SUPPORT_AREA.getMsg(), calProductDTOList);
		}
		TransFeeDTO transFee = transFeeService.getTransFee(cityDTO.getParentId());
		if (null == transFee) {
			return R.fail(TransCalFeeResultEnum.NOT_FOUNT_RULE.getCode(), TransCalFeeResultEnum.NOT_FOUNT_RULE.getMsg());
		}
		double sum;
		//要对空指针做判断 newway
		if (transport.getTransType().equals(TransTypeEnum.NUMBER.value())) {
			sum = calProductDTOList.stream().filter(dto -> dto.getTotalCount() != null).mapToDouble(TransFeeCalProductDTO::getTotalCount).sum();
		} else if (transport.getTransType().equals(TransTypeEnum.WEIGHT.value())) {
			sum = calProductDTOList.stream().filter(dto -> dto.getTotalWeight() != null).mapToDouble(TransFeeCalProductDTO::getTotalWeight).sum();
		} else if (transport.getTransType().equals(TransTypeEnum.VOLUME.value())) {
			sum = calProductDTOList.stream().filter(dto -> dto.getTotalVolume() != null).mapToDouble(TransFeeCalProductDTO::getTotalVolume).sum();
		} else {
			return R.fail(TransCalFeeResultEnum.ERROR_RULE_SET.getCode(), TransCalFeeResultEnum.ERROR_RULE_SET.getMsg());
		}

		BigDecimal amount = getAmount(transFee.getFirstNum(), transFee.getFirstPrice(), transFee.getAddNum(), transFee.getAddPrice(), sum);
		return R.ok(amount);
	}

	/**
	 * 首件:a,首件运费af，续件c,续件运费cf
	 * x:商品数量/重量/体积
	 */
	private BigDecimal getAmount(double a, BigDecimal af, double c, BigDecimal cf, double x) {
		//公式：af + cf*((x-a)/c)
		if (x <= a) {
			return af;
		}
		double sub = NumberUtil.sub(x, a);
		double div = NumberUtil.div(sub, c);
		double ceil = Math.ceil(div);
		BigDecimal mul = NumberUtil.mul(cf, ceil);
		BigDecimal add = NumberUtil.add(af, mul);
		log.info("计算得出的运费{}", add);
		return add;
	}
}
