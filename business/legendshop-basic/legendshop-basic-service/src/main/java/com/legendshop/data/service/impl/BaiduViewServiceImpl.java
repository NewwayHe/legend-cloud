/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.data.dao.BaiduViewDao;
import com.legendshop.data.dto.BaiduViewDTO;
import com.legendshop.data.entity.BaiduView;
import com.legendshop.data.query.BaiduViewQuery;
import com.legendshop.data.service.BaiduViewService;
import com.legendshop.data.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 百度（移动）统计记录(BaiduView)表服务实现类
 *
 * @author legendshop
 * @since 2021-06-19 13:54:06
 */
@Service
public class BaiduViewServiceImpl implements BaiduViewService {

	@Autowired
	private DataService dataService;

	@Autowired
	private BaiduViewDao baiduViewDao;

	@Override
	public R baiduStatisticsArchive(Date startDate, Date endDate) {

		if (ObjectUtil.isEmpty(startDate) || ObjectUtil.isEmpty(endDate)) {
			return R.fail("时间不能为空");
		}

		if (startDate.after(endDate)) {
			return R.fail("开始时间不能大于结束时间");
		}

		do {

			String archiveTime = DateUtil.formatDate(startDate);
			BaiduView baiduView = baiduViewDao.getByArchiveTime(archiveTime);

			if (null != baiduView) {
				startDate = DateUtil.offsetDay(startDate, 1);
				continue;
			}


			Integer[] baiDuTongDataString = dataService.getBaiDuTongDataString(DateUtil.beginOfDay(startDate), DateUtil.endOfDay(startDate));
			Integer[] miniData = dataService.getBaiDuMobileTongDataString(DateUtil.beginOfDay(startDate), DateUtil.endOfDay(startDate), VisitSourceEnum.MINI.name());

			Integer h5Pv = Optional.ofNullable(baiDuTongDataString[0]).orElse(0);
			Integer h5Uv = Optional.ofNullable(baiDuTongDataString[1]).orElse(0);
			Integer miniPv = Optional.ofNullable(miniData[0]).orElse(0);
			Integer miniUv = Optional.ofNullable(miniData[1]).orElse(0);

			baiduView = new BaiduView();
			baiduView.setArchiveTime(archiveTime);
			baiduView.setH5Pv(h5Pv);
			baiduView.setH5Uv(h5Uv);
			baiduView.setMiniPv(miniPv);
			baiduView.setMiniUv(miniUv);
			baiduView.setTotalPv(h5Pv + miniPv);
			baiduView.setTotalUv(h5Uv + miniUv);
			baiduViewDao.save(baiduView);

			startDate = DateUtil.offsetDay(startDate, 1);

		} while (startDate.before(endDate));

		return R.ok();
	}

	@Override
	public List<BaiduViewDTO> getFlowPic(BaiduViewQuery query) {
		if ((query.getStartDate() == null && query.getEndDate() == null)) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			query.setStartDate(DateUtil.formatDate(s));
			query.setEndDate(DateUtil.formatDate(e));
		}
		return baiduViewDao.getFlowPic(query);
	}

	@Override
	public PageSupport<BaiduViewDTO> getFlowPage(BaiduViewQuery query) {
		if ((query.getStartDate() == null && query.getEndDate() == null)) {
			//未设置日期范围，默认七天
			Integer num = 7;
			Date[] date = getDate(num);
			Date s = date[0];
			Date e = date[1];
			query.setStartDate(DateUtil.formatDate(s));
			query.setEndDate(DateUtil.formatDate(e));
		}
		return baiduViewDao.getFlowPage(query);
	}

	/**
	 * 根据天数获取--前num天至昨天23点59分59秒的date数组。
	 *
	 * @param num 天数
	 * @return date[0]-开始时间  date[1]-结束时间
	 */
	private Date[] getDate(Integer num) {

		String now = DateUtil.now();
		Date date = DateUtil.parse(now);

		Date beginOfDay = DateUtil.beginOfDay(date);
		Date startDate = DateUtil.offset(beginOfDay, DateField.DAY_OF_MONTH, -num);
		Date endDate = DateUtil.offset(beginOfDay, DateField.SECOND, -1);

		Date[] array = new Date[2];
		array[0] = startDate;
		array[1] = endDate;

		return array;
	}
}
