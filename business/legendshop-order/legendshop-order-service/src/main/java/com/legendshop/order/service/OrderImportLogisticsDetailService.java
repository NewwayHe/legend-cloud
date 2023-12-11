/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service;

import com.legendshop.common.core.service.BaseService;
import com.legendshop.order.dto.OrderImportLogisticsDetailDTO;
import com.legendshop.order.excel.OrderImportLogisticsExportDTO;

import java.util.List;

/**
 * (OrderImportLogisticsDetail)表服务接口
 *
 * @author legendshop
 * @since 2022-04-25 14:12:17
 */
public interface OrderImportLogisticsDetailService extends BaseService<OrderImportLogisticsDetailDTO> {

	/**
	 * 商品订单导入数据 导出查看
	 *
	 * @param importId
	 * @return
	 */
	List<OrderImportLogisticsExportDTO> exportByImportId(Long importId);

}
