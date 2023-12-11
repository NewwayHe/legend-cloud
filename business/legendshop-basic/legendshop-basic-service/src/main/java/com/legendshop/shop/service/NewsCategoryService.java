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
import com.legendshop.shop.dto.NewsCategoryDTO;
import com.legendshop.shop.dto.NewsDTO;
import com.legendshop.shop.query.NewsCategoryQuery;

import java.util.List;

/**
 * 帮助栏目服务
 *
 * @author legendshop
 */
public interface NewsCategoryService extends BaseService<NewsCategoryDTO> {

	/**
	 * 查询所有的帮助栏目
	 *
	 * @return
	 */
	List<NewsCategoryDTO> getNewsCategoryList();

	/**
	 * 帮助栏目分页查询
	 *
	 * @param newsCategoryQuery
	 * @return
	 */
	PageSupport<NewsCategoryDTO> page(NewsCategoryQuery newsCategoryQuery);

	/**
	 * 修改帮助栏目
	 *
	 * @param status
	 * @param id
	 * @return
	 */
	int updateStatus(Integer status, Long id);

	/**
	 * 首页栏目分页查询
	 *
	 * @param newsCategoryQuery
	 * @return
	 */
	List<NewsCategoryDTO> pageWithChildren(NewsCategoryQuery newsCategoryQuery);

	boolean deleteNewsFalg(Long id);

	/**
	 * 通过显示页面端获取栏目列表
	 *
	 * @param displayPage
	 * @return
	 */
	List<NewsCategoryDTO> queryByDisplayPage(Integer displayPage);

	/**
	 * 获取用户端和全部端显示的栏目列表
	 *
	 * @param displayPage
	 * @return
	 */
	List<NewsCategoryDTO> queryUseAndAll(Integer displayPage, String word);

	/**
	 * 查询树结构帮助类目及文章
	 *
	 * @param query
	 * @return
	 */
	List<NewsCategoryDTO> queryNewsCategoryTree(NewsCategoryQuery query);

	/**
	 * 校验文章所在栏目
	 *
	 * @param newsDTO
	 * @return
	 */
	R<NewsCategoryDTO> checkDisplayPage(NewsDTO newsDTO);

	/**
	 * 分页获取用户端和全部端显示的栏目列表
	 *
	 * @param
	 * @param
	 * @return
	 */
	PageSupport<NewsCategoryDTO> queryUsePage(NewsCategoryQuery query);


}
