/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.product.dto.TransFreeDTO;

import java.util.List;

/**
 * 条件包邮(TransFree)表服务接口
 *
 * @author legendshop
 * @since 2020-09-04 15:14:05
 */
public interface TransFreeService {

	/**
	 * 根据id获取
	 *
	 * @param id
	 * @return
	 */
	TransFreeDTO getById(Long id);

	/**
	 * 保存
	 *
	 * @param transFreeDTO
	 * @return
	 */
	Long save(TransFreeDTO transFreeDTO);

	/**
	 * 删除模板下设置的条件包邮
	 *
	 * @param transId
	 */
	void delByTransId(Long transId);

	/**
	 * 保存条件包邮以及选择支持的区域
	 *
	 * @param transFreeDTOList
	 * @param transportId
	 */
	void saveWithCityList(List<TransFreeDTO> transFreeDTOList, Long transportId);

	/**
	 * 查询模板下设置的条件包邮带详细区域
	 *
	 * @param transId
	 * @return
	 */
	List<TransFreeDTO> getListDetailAreaByTransId(Long transId);

	/**
	 * 查询模板下设置的条件包邮带区域
	 *
	 * @param transId
	 * @return
	 */
	List<TransFreeDTO> getListAreaByTransId(Long transId);
}
