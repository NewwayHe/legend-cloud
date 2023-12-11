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
import com.legendshop.basic.query.DecoratePageQuery;
import com.legendshop.shop.entity.DecoratePage;

/**
 * 装修页面(DecoratePage)表数据库访问层
 *
 * @author legendshop
 */
public interface DecoratePageDao extends GenericDao<DecoratePage, Long> {


	/**
	 * 获取分页列表
	 *
	 * @param decoratePageQuery
	 * @return
	 */
	PageSupport<DecoratePage> queryPageList(DecoratePageQuery decoratePageQuery);

	/**
	 * 获取分页排序列表
	 *
	 * @param decoratePageQuery
	 * @return
	 */
	PageSupport<DecoratePage> queryPageListDesc(DecoratePageQuery decoratePageQuery);

	/**
	 * 将使用中的页面更新为未使用
	 *
	 * @param source
	 * @param category
	 * @return
	 */
	boolean updatePageToUnUse(String source, String category);

	/**
	 * 获取使用中的首页装修页面
	 *
	 * @param source 页面来源
	 * @return
	 */
	DecoratePage getUsedIndexPage(String source);

	/**
	 * 根据ID、来源获取页面内容
	 *
	 * @param pageId 页面ID
	 * @param source 来源
	 * @return
	 */
	DecoratePage getPosterPageById(Long pageId, String source);

	DecoratePage getUsedCenterPage(String source);

	/**
	 * 获取使用中的分销中心装修
	 *
	 * @param source
	 * @return
	 */
	DecoratePage getUsedDistributionCenterPage(String source);

	/**
	 * 获取首页装修数据
	 *
	 * @return
	 */
	DecoratePage getUsedIndexPag();
}
