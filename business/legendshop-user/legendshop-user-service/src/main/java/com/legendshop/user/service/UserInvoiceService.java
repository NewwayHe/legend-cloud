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
import com.legendshop.user.bo.UserInvoiceBO;
import com.legendshop.user.dto.UserInvoiceDTO;
import com.legendshop.user.dto.UserInvoiceDetailDTO;
import com.legendshop.user.query.UserInvoiceQuery;

import java.util.List;

/**
 * 发票服务
 *
 * @author legendshop
 */
public interface UserInvoiceService {


	/**
	 * 保存发票
	 *
	 * @param invoice
	 * @return
	 */
	Long saveInvoice(UserInvoiceDTO invoice);

	/**
	 * 更新发票
	 *
	 * @param invoice
	 * @return
	 */
	Integer updateInvoice(UserInvoiceDTO invoice);

	/**
	 * 设为默认
	 *
	 * @param invoiceId
	 * @param userId
	 * @return
	 */
	int updateDefaultInvoice(Long invoiceId, Long userId);

	/**
	 * 根据ID删除发票
	 *
	 * @param id
	 * @return
	 */
	R deleteById(Long id);

	/**
	 * 获取用户默认发票
	 *
	 * @param userId
	 * @return
	 */
	UserInvoiceDTO getDefaultInvoice(Long userId);


	/**
	 * @param id
	 * @return
	 */
	UserInvoiceDTO getById(Long id);

	/**
	 * 改变用户默认发票的状态
	 *
	 * @param userId
	 * @return
	 */
	boolean removeDefaultInvoiceStatus(Long userId);

	/**
	 * 获取用户发票列表
	 *
	 * @param query
	 * @return
	 */
	PageSupport<UserInvoiceDTO> queryPage(UserInvoiceQuery query);

	/**
	 * 根据商家支持开具的发票类型获取默认或最新一条发票信息
	 *
	 * @param userId          用户ID
	 * @param invoiceTypeList 商家支持开具的发票类型
	 * @return
	 */
	UserInvoiceBO getUserInvoiceForOrder(Long userId, List<String> invoiceTypeList);

	/**
	 * 获取发票信息
	 *
	 * @param invoiceId
	 * @return
	 */
	UserInvoiceBO getInvoiceBoById(Long invoiceId);

	/**
	 * 分页查询订单发票信息
	 *
	 * @param query
	 * @return
	 */
	PageSupport<UserInvoiceBO> queryUserInvoiceOrderById(UserInvoiceQuery query);

	/**
	 * 获取用户发票详情
	 *
	 * @param id
	 * @return
	 */
	R<UserInvoiceDetailDTO> getDetail(Long id);
}
