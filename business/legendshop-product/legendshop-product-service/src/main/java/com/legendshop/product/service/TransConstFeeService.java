/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.product.dto.TransConstFeeDTO;

import java.util.List;

/**
 * 固定运费(TransConstFee)表服务接口
 *
 * @author legendshop
 * @since 2020-09-07 14:43:46
 */
public interface TransConstFeeService {


	/**
	 * 根据id获取
	 *
	 * @param id
	 * @return
	 */
	TransConstFeeDTO getById(Long id);

	/**
	 * 保存
	 *
	 * @param transConstFeeDTO
	 * @return
	 */
	Long save(TransConstFeeDTO transConstFeeDTO);


	/**
	 * 保存固定运费以及选择支持的区域
	 *
	 * @param transConstFeeDTOList
	 * @param transportId
	 */
	void saveWithCityList(List<TransConstFeeDTO> transConstFeeDTOList, Long transportId);


	/**
	 * 删除模板下设置的固定运费
	 *
	 * @param transId
	 */
	void delByTransId(Long transId);

	/**
	 * 查询模板下设置的固定运费
	 *
	 * @param transId
	 * @return
	 */
	List<TransConstFeeDTO> getListAreaByTransId(Long transId);

	/**
	 * 查询模板下设置的固定运费带详细区域
	 *
	 * @param transId
	 * @return
	 */
	List<TransConstFeeDTO> getListDetailAreaByTransId(Long transId);

}
