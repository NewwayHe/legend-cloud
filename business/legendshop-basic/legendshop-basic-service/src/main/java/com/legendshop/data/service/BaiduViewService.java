/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.data.dto.BaiduViewDTO;
import com.legendshop.data.query.BaiduViewQuery;

import java.util.Date;
import java.util.List;

/**
 * 百度（移动）统计记录(BaiduView)表服务接口
 *
 * @author legendshop
 * @since 2021-06-19 13:54:05
 */
public interface BaiduViewService {

	/**
	 * 按开始和结束时间进行百度统计归档
	 *
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	R baiduStatisticsArchive(Date startTime, Date endTime);

	/**
	 * 获取列表数据
	 *
	 * @param query
	 * @return
	 */
	List<BaiduViewDTO> getFlowPic(BaiduViewQuery query);

	/**
	 * 获取分页数据
	 *
	 * @param query
	 * @return
	 */
	PageSupport<BaiduViewDTO> getFlowPage(BaiduViewQuery query);
}
