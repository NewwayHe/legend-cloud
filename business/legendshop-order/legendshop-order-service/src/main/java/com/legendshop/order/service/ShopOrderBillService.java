/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service;


import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.order.dto.ShopOrderBillDTO;
import com.legendshop.order.enums.PayTypeEnum;
import com.legendshop.order.enums.ShopOrderBillStatusEnum;
import com.legendshop.order.excel.ShopOrderBillExportDTO;
import com.legendshop.order.query.ShopOrderBillOrderQuery;
import com.legendshop.order.query.ShopOrderBillQuery;

import java.util.Date;
import java.util.List;

/**
 * 商家结算服务.
 *
 * @author legendshop
 */
public interface ShopOrderBillService {

	/**
	 * Gets the shop order bill.
	 *
	 * @param id the id
	 * @return the shop order bill
	 */
	ShopOrderBillDTO getById(Long id);


	/**
	 * Save shop order bill.
	 *
	 * @param shopOrderBill the shop order bill
	 * @return the long
	 */
	Long saveShopOrderBill(ShopOrderBillDTO shopOrderBill);

	/**
	 * Update shop order bill.
	 *
	 * @param shopOrderBill the shop order bill
	 */
	void updateShopOrderBill(ShopOrderBillDTO shopOrderBill);


	/**
	 * 获取当前期结算的月份 例如：201512.
	 *
	 * @return the shop bill month
	 */
	String getShopBillMonth();

	/**
	 * Gets the last shop order bill by shop id.
	 *
	 * @param shopId the shop id
	 * @return the last shop order bill by shop id
	 */
	ShopOrderBillDTO getLastShopOrderBillByShopId(Long shopId);


	/**
	 * 保存结算单并更新订单状态和退货单为结束状态.
	 *
	 * @param shopOrderBill 结算单
	 * @param billSubIds    要结算的订单Id列表
	 * @param billRefundId  要结算的退货单Id列表
	 */
	void generateShopBill(ShopOrderBillDTO shopOrderBill, List<Long> billSubIds, List<Long> billRefundId);

	/**
	 * 结算账期分页查询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<ShopOrderBillDTO> getShopOrderBillPage(ShopOrderBillQuery query);


	/**
	 * 查找系统是否有现成的结算单
	 */
	boolean getShopOrderBill(Long shopId, Date startDate, Date endDate);


	/**
	 * 账单结算
	 *
	 * @param orderBillStatusEnum
	 * @param id
	 * @param payDate
	 * @param payContent
	 * @return
	 */
	R<Void> payBill(ShopOrderBillStatusEnum orderBillStatusEnum, Long id, String payDate, String payContent, PayTypeEnum payTypeEnum);

	/**
	 * 账单结算订单列表
	 *
	 * @param shopOrderBillOrderQuery
	 * @return
	 */
	PageSupport getShopOrderBillOrderPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery);

	/**
	 * 账单结算订单列表导出
	 *
	 * @param shopOrderBillOrderQuery
	 * @return
	 */
	List exportOrderPage(ShopOrderBillOrderQuery shopOrderBillOrderQuery);

	/**
	 * 结算账单订单列表导出
	 *
	 * @param query
	 * @return
	 */
	List<ShopOrderBillExportDTO> exportShopBillPeriod(ShopOrderBillQuery query);

	/**
	 * 商家确认账单
	 *
	 * @param id     账单ID
	 * @param shopId
	 * @return
	 */
	R<Void> shopConfirm(Long id, Long shopId);

	/**
	 * 平台审核账单
	 *
	 * @param id
	 * @return
	 */
	R<Void> adminCheck(Long id);

	/**
	 * 获取首页本期结算统计
	 *
	 * @param shopId
	 * @return ShopOrderBillDTO
	 */
	ShopOrderBillDTO getShopOrderBillCount(Long shopId);

	/**
	 * 账单结算定时任务处理
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	R<Void> calculateBillJobHandle(Date startDate, Date endDate);
}
