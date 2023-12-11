/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.data.dto.BaiduViewDTO;
import com.legendshop.data.entity.BaiduView;
import com.legendshop.data.query.BaiduViewQuery;

import java.util.Date;
import java.util.List;

/**
 * 百度（移动）统计记录(BaiduView)表数据库访问层
 *
 * @author legendshop
 * @since 2021-06-19 13:54:05
 */
public interface BaiduViewDao extends GenericDao<BaiduView, Long> {

	/**
	 * 根据归档时间获取记录
	 *
	 * @param archiveTime
	 * @return
	 */
	BaiduView getByArchiveTime(String archiveTime);

	/**
	 * 根据归档时间获取记录
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	BaiduView getAllByArchiveTime(Date startDate, Date endDate);

	/**
	 * 获取折线图数据
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
