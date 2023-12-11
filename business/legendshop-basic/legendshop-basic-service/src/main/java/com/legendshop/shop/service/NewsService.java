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
import com.legendshop.common.core.service.BaseService;
import com.legendshop.shop.bo.NewsBO;
import com.legendshop.shop.dto.NewsDTO;
import com.legendshop.shop.query.NewsCategoryQuery;
import com.legendshop.shop.query.NewsQuery;

import java.util.List;

/**
 * 文章的服务.
 *
 * @author legendshop
 */
public interface NewsService extends BaseService<NewsDTO> {


	List<NewsDTO> getNewsByCatId(Long catid);


	/**
	 * 获取上线帮助文章
	 *
	 * @param newsQuery
	 * @return
	 */
	List<NewsDTO> getOnlineNews(NewsQuery newsQuery);


	/**
	 * 帮助文章的分页查询
	 *
	 * @param newsQuery
	 * @return
	 */
	PageSupport<NewsBO> page(NewsQuery newsQuery);

	/**
	 * 修改帮助文章状态
	 *
	 * @param status
	 * @param id
	 * @return
	 */
	int updateStatus(Integer status, Long id);

	/**
	 * 根据id查询帮助文章和页面显示状态
	 *
	 * @param id
	 * @return
	 */
	NewsDTO getNewsAndDisPlay(Long id);

	/**
	 * 通过栏目Id获取该栏目下所有文章
	 *
	 * @param query
	 * @return
	 */
	PageSupport<NewsDTO> getCatNews(NewsCategoryQuery query);

	/**
	 * 查看文章详情
	 *
	 * @param id
	 * @return
	 */
	NewsDTO getNewsByDisplay(Long id, Integer displayPage);

	/**
	 * 通过关键字搜索帮助文章
	 *
	 * @param query
	 * @return
	 */
	PageSupport<NewsDTO> getByWord(NewsCategoryQuery query);
}
