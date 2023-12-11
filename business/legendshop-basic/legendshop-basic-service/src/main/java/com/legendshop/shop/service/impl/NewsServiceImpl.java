/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.shop.bo.NewsBO;
import com.legendshop.shop.dao.NewsCategoryDao;
import com.legendshop.shop.dao.NewsDao;
import com.legendshop.shop.dto.NewsDTO;
import com.legendshop.shop.query.NewsCategoryQuery;
import com.legendshop.shop.query.NewsQuery;
import com.legendshop.shop.service.NewsService;
import com.legendshop.shop.service.convert.NewsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 新闻服务
 *
 * @author legendshop
 */
@Service
public class NewsServiceImpl extends BaseServiceImpl<NewsDTO, NewsDao, NewsConverter> implements NewsService {

	@Autowired
	private NewsDao newsDao;
	@Autowired
	private NewsCategoryDao newsCategoryDao;
	@Autowired
	private NewsConverter converter;

	@Override
	@Cacheable(value = "NewsList", key = "'catid_' + #catid")
	public List<NewsDTO> getNewsByCatId(Long catid) {
		return converter.to(newsDao.getNewsBycatId(catid));
	}

	@Override
	@Cacheable(value = "OnlineNewsList", key = "'catid_' + #newsQuery.newsCategoryId + '_pageSize_' + #newsQuery.pageSize")
	public List<NewsDTO> getOnlineNews(NewsQuery newsQuery) {
		return newsDao.getOnlineNews(newsQuery);
	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = "OnlineNewsList", allEntries = true),
			@CacheEvict(value = "NewsList", allEntries = true)
	})
	public R save(NewsDTO dto) {
		return super.save(dto);
	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = "OnlineNewsList", allEntries = true),
			@CacheEvict(value = "NewsList", allEntries = true)
	})
	public Boolean update(NewsDTO dto) {
		return super.update(dto);
	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = "OnlineNewsList", allEntries = true),
			@CacheEvict(value = "NewsList", allEntries = true)
	})
	public Boolean deleteById(Long id) {
		return super.deleteById(id);
	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = "OnlineNewsList", allEntries = true),
			@CacheEvict(value = "NewsList", allEntries = true)
	})
	public Boolean delete(NewsDTO newsDTO) {
		return super.delete(newsDTO);
	}

	/**
	 * 帮助文章的分页查询
	 *
	 * @param newsQuery
	 * @return
	 */
	@Override
	public PageSupport<NewsBO> page(NewsQuery newsQuery) {
		return newsDao.queryPageList(newsQuery);
	}

	/**
	 * 修改帮助文章状态
	 *
	 * @param status
	 * @param id
	 * @return
	 */
	@Override
	@Caching(evict = {
			@CacheEvict(value = "OnlineNewsList", allEntries = true),
			@CacheEvict(value = "NewsList", allEntries = true)
	})
	public int updateStatus(Integer status, Long id) {
		return newsDao.updateStatus(status, id);
	}

	@Override
	public NewsDTO getNewsAndDisPlay(Long id) {
		return newsDao.getNewsAndDisPlay(id);
	}

	@Override
	public PageSupport<NewsDTO> getCatNews(NewsCategoryQuery query) {
		return newsDao.getCatNews(query);
	}

	@Override
	public NewsDTO getNewsByDisplay(Long id, Integer displayPage) {
		return newsDao.getNewsByDisplay(id, displayPage);
	}

	@Override
	public PageSupport<NewsDTO> getByWord(NewsCategoryQuery query) {
		return newsDao.getByWord(query);
	}
}
