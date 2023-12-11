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
import com.legendshop.shop.entity.ShopDecoratePage;
import com.legendshop.shop.query.ShopDecoratePageQuery;

/**
 * 移动端店铺主页装修Dao接口
 *
 * @author legendshop
 */
public interface ShopDecoratePageDao extends GenericDao<ShopDecoratePage, Long> {

	/**
	 * 查询店铺装修页面分页列表
	 *
	 * @param shopDecoratePageQuery
	 * @return
	 */
	PageSupport<ShopDecoratePage> queryPageList(ShopDecoratePageQuery shopDecoratePageQuery);

	/**
	 * 查询店铺装修页面分页排序列表
	 *
	 * @param shopDecoratePageQuery
	 * @return
	 */
	PageSupport<ShopDecoratePage> queryPageListDesc(ShopDecoratePageQuery shopDecoratePageQuery);

	/**
	 * 将使用中的页面更新为未使用
	 *
	 * @param shopId
	 * @return
	 */
	boolean updatePageToUnUse(Long shopId, String source);

	/**
	 * 将页面更新为默认
	 *
	 * @param shopId
	 * @param source
	 * @return
	 */
	boolean updateDefaultPage(Long shopId, String source, boolean defaultFlag);

	/**
	 * 获取使用中的首页装修页面
	 *
	 * @param shopId
	 * @param source 页面来源
	 * @return
	 */
	ShopDecoratePage getUsedIndexPage(Long shopId, String source);

	/**
	 * 根据ID、来源获取页面内容
	 *
	 * @param shopId
	 * @param pageId 页面ID
	 * @param source 来源
	 * @return
	 */
	ShopDecoratePage getPosterPageById(Long shopId, Long pageId, String source);

	ShopDecoratePage getByPageIdAndShopId(Long pageId, Long shopId);
}
