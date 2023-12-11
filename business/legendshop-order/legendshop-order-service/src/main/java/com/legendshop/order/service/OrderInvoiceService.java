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
import com.legendshop.order.dto.OrderInvoiceDTO;
import com.legendshop.order.excel.OrderInvoiceExportDTO;
import com.legendshop.order.query.OrderInvoiceQuery;

import java.util.List;

/**
 * 发票订单信息表服务.
 *
 * @author legendshop
 */
public interface OrderInvoiceService {

	OrderInvoiceDTO getById(Long id);

	Boolean deleteById(Long id);

	Long save(OrderInvoiceDTO orderInvoiceDTO);

	Boolean update(OrderInvoiceDTO orderInvoiceDTO);

	/**
	 * 订单发票分页列表
	 *
	 * @param orderInvoiceQuery
	 * @return
	 */
	PageSupport<OrderInvoiceDTO> queryPage(OrderInvoiceQuery orderInvoiceQuery);

	/**
	 * 订单发票导出
	 *
	 * @param query
	 * @return
	 */
	List<OrderInvoiceExportDTO> orderInvoiceExport(OrderInvoiceQuery query);
}
