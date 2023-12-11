/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.user.bo.CustomerBillBO;
import com.legendshop.user.dto.CustomerBillCreateDTO;
import com.legendshop.user.dto.CustomerBillDTO;
import com.legendshop.user.dto.CustomerBillGroupDTO;
import com.legendshop.user.query.CustomerBillQuery;

import java.util.List;

/**
 * 客户账单服务.
 *
 * @author legendshop
 */
public interface CustomerBillService {

	/**
	 * 客户账单分页查询
	 *
	 * @param expensesRecordQuery
	 * @return
	 */
	PageSupport<CustomerBillDTO> queryPage(CustomerBillQuery expensesRecordQuery);

	/**
	 * 客户账单逻辑删除
	 *
	 * @param id
	 * @return
	 */
	int updateDelFlag(Long id);

	/**
	 * 批量逻辑删除客户账单
	 *
	 * @param ids
	 * @return
	 */
	int batchUpdateDelFlag(List<Long> ids);

	/**
	 * 保存账单记录
	 *
	 * @param customerBillCreateDTO
	 * @return
	 */
	void save(CustomerBillCreateDTO customerBillCreateDTO);

	void save(List<CustomerBillCreateDTO> customerBillCreateList);


	/**
	 * 获取根据时间分组的账单列表
	 *
	 * @param customerBillQuery
	 * @return
	 */
	PageSupport<CustomerBillGroupDTO> queryPageGroupByDate(CustomerBillQuery customerBillQuery);

	/**
	 * 获取账单记录详情
	 *
	 * @param id
	 * @return
	 */
	R<CustomerBillBO> getDetailById(Long id);

	/**
	 * 获取关联业务单号相同的账单记录
	 *
	 * @param id
	 * @param relatedBizOrderNo
	 * @return
	 */
	List<CustomerBillBO> getRelatedBizOrderList(Long id, String relatedBizOrderNo);
}
