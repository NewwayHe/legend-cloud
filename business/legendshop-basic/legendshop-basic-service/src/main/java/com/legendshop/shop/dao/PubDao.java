/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.shop.dto.PubDTO;
import com.legendshop.shop.entity.Pub;
import com.legendshop.shop.query.PubQuery;

/**
 * 公告Dao.
 *
 * @author legendshop
 */
public interface PubDao extends GenericDao<Pub, Long> {


	/**
	 * Gets the pub list page.
	 */
	PageSupport<Pub> getPubListPage(PubQuery pubQuery);

	/**
	 * 根据类型获取最新一条公告
	 *
	 * @return
	 */
	Pub getNewestPubByType(Integer type);

	/**
	 * 根据类型获取公告列表
	 *
	 * @return
	 */
	PageSupport<PubDTO> queryPubPageListByType(PubQuery pubQuery);

	Integer userUnreadMsg(Long userId);
}
