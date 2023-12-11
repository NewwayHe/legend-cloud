/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.BaseService;
import com.legendshop.shop.dto.AppStartAdvDTO;
import com.legendshop.shop.dto.AppStartAdvOnlineDTO;
import com.legendshop.shop.query.AppStartAdvQuery;

import java.util.List;

/**
 * APP启动广告服务.
 *
 * @author legendshop
 */
public interface AppStartAdvService extends BaseService<AppStartAdvDTO> {

	AppStartAdvDTO getAppStartAdv(Long id);

	int deleteAppStartAdv(Long id);

	Long saveAppStartAdv(AppStartAdvDTO appStartAdv);

	R updateAppStartAdv(AppStartAdvDTO appStartAdv);


	/**
	 * 获取所有记录总数
	 *
	 * @return
	 */
	int getAllCount();

	/**
	 * 检查name是否已存在
	 */
	boolean checkNameIsExits(String name);

	String getName(Long id);

	/**
	 * 获取已上线的广告
	 *
	 * @return
	 */
	List<AppStartAdvOnlineDTO> getOnlines();


	/**
	 * 启动广告分页查询
	 *
	 * @param appStartAdvQuery
	 * @return
	 */
	PageSupport<AppStartAdvDTO> page(AppStartAdvQuery appStartAdvQuery);

	/**
	 * 修改启动广告状态
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	boolean updateStatus(Long id, Integer status);

	/**
	 * 获取随机一条app启动广告
	 *
	 * @return
	 */
	AppStartAdvDTO getRandomAppStartAdv();

	/**
	 * 删除启动广告
	 *
	 * @param id
	 * @return
	 */
	R deleteAppAdv(Long id);
}
