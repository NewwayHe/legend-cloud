/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.order.dto.ShopOrderBillDTO;
import com.legendshop.order.entity.ShopOrderBill;
import com.legendshop.order.excel.ShopOrderBillExportDTO;
import com.legendshop.order.query.ShopOrderBillQuery;

import java.util.Date;
import java.util.List;

/**
 * 商家订单结算Dao.
 *
 * @author legendshop
 */
public interface ShopOrderBillDao extends GenericDao<ShopOrderBill, Long> {

	/**
	 * Gets the last shop order bill by shop id.
	 *
	 * @param shopId the shop id
	 * @return the last shop order bill by shop id
	 */
	ShopOrderBill getLastShopOrderBillByShopId(Long shopId);

	/**
	 * 结算账期分页查询
	 *
	 * @param query
	 * @return
	 */
	PageSupport<ShopOrderBill> getShopOrderBillPage(ShopOrderBillQuery query);

	/**
	 * 查询该结算单是否已经存在
	 *
	 * @param shopId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	boolean getShopOrderBill(Long shopId, Date startDate, Date endDate);


	/**
	 * 账单结算
	 *
	 * @param status
	 * @param id
	 * @param payDate
	 * @param payContent
	 * @return
	 */
	int payBill(Integer status, Long id, String payDate, String payContent);

	ShopOrderBill getShopOrderBillById(Long id);

	/**
	 * 结算账单订单列表导出
	 *
	 * @param query
	 * @return
	 */
	List<ShopOrderBillExportDTO> exportShopBillPeriod(ShopOrderBillQuery query);

	/**
	 * 商家确认账单更新状态
	 *
	 * @param id
	 * @param shopId
	 * @return
	 */
	int shopConfirm(Long id, Long shopId);

	/**
	 * 平台审核更新状态
	 *
	 * @param id
	 * @return
	 */
	int adminCheck(Long id);

	/**
	 * 获取首页本次结算统计
	 *
	 * @param shopId
	 * @return ShopOrderBillDTO
	 */
	ShopOrderBillDTO getshopOrderBillCount(Long shopId);

}
