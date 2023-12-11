/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.convert;

import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.user.bo.CustomerBillBO;
import com.legendshop.user.dto.CustomerBillCreateDTO;
import com.legendshop.user.dto.CustomerBillDTO;
import com.legendshop.user.entity.CustomerBill;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author legendshop
 */
@Mapper
public interface CustomerBillConverter extends BaseConverter<CustomerBill, CustomerBillDTO> {

	/**
	 * 将客户账单创建DTO转换为实体对象
	 *
	 * @param customerBillCreateDTO 客户账单创建DTO
	 * @return 转换后的实体对象
	 */
	CustomerBill toEntity(CustomerBillCreateDTO customerBillCreateDTO);

	/**
	 * 将客户账单创建DTO列表转换为实体对象列表
	 *
	 * @param customerBillCreateList 客户账单创建DTO列表
	 * @return 转换后的实体对象列表
	 */
	List<CustomerBill> toEntity(List<CustomerBillCreateDTO> customerBillCreateList);

	/**
	 * 将客户账单实体对象转换为业务对象
	 *
	 * @param customerBill 客户账单实体对象
	 * @return 转换后的业务对象
	 */
	CustomerBillBO toBO(CustomerBill customerBill);

	/**
	 * 将客户账单实体对象列表转换为业务对象列表
	 *
	 * @param customerBills 客户账单实体对象列表
	 * @return 转换后的业务对象列表
	 */
	List<CustomerBillBO> toBOList(List<CustomerBill> customerBills);
}
