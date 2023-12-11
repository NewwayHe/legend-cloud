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
import com.legendshop.shop.dto.NewsCategoryDTO;
import com.legendshop.shop.entity.NewsCategory;
import com.legendshop.shop.query.NewsCategoryQuery;

import java.util.List;

/**
 * 新闻分类Dao.
 *
 * @author legendshop
 */
public interface NewsCategoryDao extends GenericDao<NewsCategory, Long> {


	/**
	 * 帮助栏目分页查询
	 *
	 * @param newsCategoryQuery
	 * @return
	 */
	PageSupport<NewsCategory> page(NewsCategoryQuery newsCategoryQuery);

	/**
	 * 修改帮助栏目
	 *
	 * @param status
	 * @param id
	 * @return
	 */
	int updateStatus(Integer status, Long id);

	/**
	 * 通过显示页面端获取栏目列表
	 *
	 * @param displayPage
	 * @return
	 */
	List<NewsCategory> queryByDisplayPage(Integer displayPage);

	/**
	 * 获取用户端和全部端显示的栏目列表
	 *
	 * @param displayPage
	 * @return
	 */
	List<NewsCategory> queryUseAndAll(Integer displayPage, String word);

	/**
	 * 查询树结构中的帮助类目列表
	 *
	 * @param query
	 * @return
	 */
	List<NewsCategoryDTO> queryNewsCategoryTree(NewsCategoryQuery query);

	/**
	 * 分页获取用户端和全部端显示的栏目列表
	 *
	 * @param
	 * @return
	 */
	PageSupport<NewsCategory> queryUsePage(NewsCategoryQuery query);

	/**
	 * 分页获取用户端和全部端显示的栏目列表和文章
	 *
	 * @param
	 * @return
	 */
	PageSupport<NewsCategoryDTO> queryNewsCategoryPage(NewsCategoryQuery query);


}
