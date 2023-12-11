/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dao;


import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.user.bo.UserInvoiceBO;
import com.legendshop.user.dto.OrderItemInvoiceDTO;
import com.legendshop.user.dto.UserInvoiceDetailDTO;
import com.legendshop.user.entity.UserInvoice;
import com.legendshop.user.query.UserInvoiceQuery;

import java.util.List;

/**
 * 发票Dao
 *
 * @author legendshop
 */
public interface UserInvoiceDao extends GenericDao<UserInvoice, Long> {


	/**
	 * 设置默认发票
	 *
	 * @param invoiceId
	 * @param userId
	 * @return
	 */
	int updateDefaultInvoice(Long invoiceId, Long userId);

	/**
	 * 查询默认发票
	 *
	 * @param userId
	 * @return
	 */
	UserInvoice getDefaultInvoice(Long userId);


	/**
	 * 清除默认发票状态
	 *
	 * @param userId
	 * @return
	 */
	int removeDefaultInvoiceStatus(Long userId);

	/**
	 * 查询用户发票分页列表
	 *
	 * @param query
	 * @return
	 */
	PageSupport<UserInvoice> queryPage(UserInvoiceQuery query);

	/**
	 * 根据商家支持开具的发票类型获取默认发票
	 *
	 * @param userId          用户ID
	 * @param invoiceTypeList 商家支持开具的发票类型
	 * @return
	 */
	UserInvoice getDefaultInvoiceForOrder(Long userId, List<String> invoiceTypeList);

	/**
	 * 根据商家支持开具的发票类型获取最新的发票
	 *
	 * @param userId          用户ID
	 * @param invoiceTypeList 商家支持开具的发票类型
	 * @return
	 */
	List<UserInvoice> getUserInvoiceForOrder(Long userId, List<String> invoiceTypeList);

	/**
	 * 分页查询用户订单发票信息
	 *
	 * @param query 查询对象
	 * @return
	 */
	PageSupport<UserInvoiceBO> queryUserInvoiceOrderById(UserInvoiceQuery query);

	/**
	 * 根据订单ID查询订单详细下的所有商品图片
	 *
	 * @param orderId 订单ID
	 * @return
	 */
	List<String> queryUserInvoiceOrderPics(Long orderId);

	/**
	 * 获取发票详情
	 *
	 * @param id
	 * @return
	 */
	UserInvoiceDetailDTO getDetail(Long id);

	/**
	 * 获取收货信息
	 *
	 * @param longList
	 * @return
	 */
	List<String> getAddress(List<Long> longList);

	/**
	 * 订单项信息
	 *
	 * @param
	 * @return
	 */
	List<OrderItemInvoiceDTO> getOrderItem(Long id);

}
