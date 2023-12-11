/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;


import com.legendshop.common.core.service.BaseService;
import com.legendshop.shop.dto.AdvertiseCountDTO;
import com.legendshop.shop.excel.AdvertiseCountExportDTO;
import com.legendshop.shop.query.AdvertiseQuery;

import java.util.List;

/**
 * (AdvertiseCount)表服务接口
 *
 * @author legendshop
 * @since 2022-04-27 17:21:40
 */
public interface AdvertiseCountService extends BaseService<AdvertiseCountDTO> {


	void updateClick(AdvertiseCountDTO dto);

	void updatePut(AdvertiseCountDTO dto);

	/**
	 * 举报报表
	 *
	 * @param query
	 * @return
	 */
	List<AdvertiseCountDTO> queryAdvertiseCountReport(AdvertiseQuery query);

	/**
	 * 举报报表导出
	 *
	 * @param query
	 * @return
	 */
	List<AdvertiseCountExportDTO> queryAdvertiseCountReportExport(AdvertiseQuery query);


}
