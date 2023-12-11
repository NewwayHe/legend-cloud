/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.product.dto.TransFeeDTO;

import java.util.List;

/**
 * 运输费用服务
 *
 * @author legendshop
 */
public interface TransFeeService {


	/**
	 * 根据id获取
	 *
	 * @param id
	 * @return
	 */
	TransFeeDTO getTransFee(Long id);

	/**
	 * 保存
	 *
	 * @param transFeeDTO
	 * @return
	 */
	Long saveTransFee(TransFeeDTO transFeeDTO);

	/**
	 * 批量添加
	 *
	 * @param transFees
	 */
	void batchAdd(List<TransFeeDTO> transFees);

	/**
	 * 删除模板下设置的运费计算
	 *
	 * @param transId
	 */
	void delByTransId(Long transId);


	/**
	 * 保存运费计算以及选择支持的区域
	 *
	 * @param transFeeDTOList
	 * @param transportId
	 */
	void saveWithCityList(List<TransFeeDTO> transFeeDTOList, Long transportId);


	/**
	 * 查询模板下设置的运费计算带详细区域
	 *
	 * @param transId
	 * @return
	 */
	List<TransFeeDTO> getListDetailAreaByTransId(Long transId);

	/**
	 * 查询模板下设置的运费计算带区域
	 *
	 * @param transId
	 * @return
	 */
	List<TransFeeDTO> getListAreaByTransId(Long transId);
}
