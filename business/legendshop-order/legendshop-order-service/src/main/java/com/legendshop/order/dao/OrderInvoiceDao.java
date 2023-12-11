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
import com.legendshop.order.dto.OrderInvoiceDTO;
import com.legendshop.order.dto.OrderItemDTO;
import com.legendshop.order.entity.OrderInvoice;
import com.legendshop.order.excel.OrderInvoiceExportDTO;
import com.legendshop.order.query.OrderInvoiceQuery;

import java.util.List;

/**
 * 发票订单信息Dao.
 *
 * @author legendshop
 */
public interface OrderInvoiceDao extends GenericDao<OrderInvoice, Long> {

	PageSupport<OrderInvoiceDTO> queryPage(OrderInvoiceQuery orderInvoiceQuery);

	/**
	 * 导出
	 *
	 * @param query
	 * @return
	 */
	List<OrderInvoiceExportDTO> orderInvoiceExport(OrderInvoiceQuery query);

	/**
	 * 查询订单详细下的所有商品图片
	 *
	 * @return
	 */
	List<OrderItemDTO> queryUserInvoiceOrderPics();
}
