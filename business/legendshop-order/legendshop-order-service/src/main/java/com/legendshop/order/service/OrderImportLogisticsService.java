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
import com.legendshop.order.dto.OrderImportLogisticsDTO;
import com.legendshop.order.dto.WaitDeliveryOrderDTO;
import com.legendshop.order.query.OrderImportLogisticsQuery;

import java.util.List;

/**
 * (OrderImportLogistics)表服务接口
 *
 * @author legendshop
 * @since 2022-04-25 14:13:35
 */
public interface OrderImportLogisticsService {


	/**
	 * 商家端的订单列表中支持批量操作分页查询
	 *
	 * @return
	 */
	PageSupport<OrderImportLogisticsDTO> page(OrderImportLogisticsQuery query);

	/**
	 * 下载物流导入模板
	 *
	 * @param shopId
	 * @return
	 */
	List<WaitDeliveryOrderDTO> template(Long shopId);

	Long save(OrderImportLogisticsDTO importLogisticsDTO);
}
