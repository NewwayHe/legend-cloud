/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.product.bo.TransportBO;
import com.legendshop.product.dto.TransCityDTO;
import com.legendshop.product.dto.TransFeeCalProductDTO;
import com.legendshop.product.enums.TransCalFeeResultEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 运费模板责任链抽象处理类
 *
 * @author legendshop
 */
@Slf4j
public abstract class AbstractTransportHandler {

	/**
	 * 统一校验地区限售，运费规则
	 *
	 * @param cityDTO
	 * @param object
	 * @return
	 */
	public R validateParam(TransCityDTO cityDTO, Object object) {
		if (null == cityDTO) {
			log.info("不支持配送至该区域！");
			return R.fail(TransCalFeeResultEnum.NOT_SUPPORT_AREA.getMsg());
		}
		if (null == object) {
			return R.fail(TransCalFeeResultEnum.NOT_FOUNT_RULE.getMsg());
		}
		return R.ok();
	}

	/**
	 * 统一校验地区限售
	 *
	 * @param cityDTO
	 * @return
	 */
	public R validateParam(TransCityDTO cityDTO) {
		if (null == cityDTO) {
			return R.fail(TransCalFeeResultEnum.NOT_SUPPORT_AREA.getCode(), TransCalFeeResultEnum.NOT_SUPPORT_AREA.getMsg());
		}
		return R.ok();
	}

	/**
	 * 获取运费模板以及设置的规则项
	 *
	 * @param transportBO
	 * @param transId
	 * @return
	 */
	public abstract TransportBO getWithItems(TransportBO transportBO, Long transId);


	/**
	 * 计算运费
	 *
	 * @param calProductDTOList
	 * @param transport
	 * @param cityId
	 * @return
	 */
	public abstract R calculateTransFee(List<TransFeeCalProductDTO> calProductDTOList, TransportBO transport, Long cityId);
}
