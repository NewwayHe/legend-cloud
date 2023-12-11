/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.enums.DisplayPageEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.shop.dao.NewsCategoryDao;
import com.legendshop.shop.dao.NewsDao;
import com.legendshop.shop.dto.NewsCategoryDTO;
import com.legendshop.shop.dto.NewsDTO;
import com.legendshop.shop.entity.News;
import com.legendshop.shop.entity.NewsCategory;
import com.legendshop.shop.query.NewsCategoryQuery;
import com.legendshop.shop.query.NewsQuery;
import com.legendshop.shop.service.NewsCategoryService;
import com.legendshop.shop.service.NewsService;
import com.legendshop.shop.service.convert.NewsCategoryConverter;
import com.legendshop.shop.service.convert.NewsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 新闻分类服务
 *
 * @author legendshop
 */
@Service
public class NewsCategoryServiceImpl extends BaseServiceImpl<NewsCategoryDTO, NewsCategoryDao, NewsCategoryConverter> implements NewsCategoryService {

	@Autowired
	private NewsCategoryDao newsCategoryDao;

	@Autowired
	private NewsCategoryConverter converter;

	@Autowired
	private NewsService newsService;

	@Autowired
	private NewsDao newsDao;

	@Autowired
	private NewsConverter newConverter;

	@Override
	public List<NewsCategoryDTO> getNewsCategoryList() {
		return converter.to(newsCategoryDao.queryAll());
	}

	/**
	 * 帮助栏目分页查询
	 *
	 * @param newsCategoryQuery
	 * @return
	 */
	@Override
	public PageSupport<NewsCategoryDTO> page(NewsCategoryQuery newsCategoryQuery) {
		return converter.page(newsCategoryDao.page(newsCategoryQuery));
	}

	/**
	 * 修改帮助栏目
	 *
	 * @param status
	 * @param id
	 * @return
	 */
	@Override
	public int updateStatus(Integer status, Long id) {
		return newsCategoryDao.updateStatus(status, id);
	}


	@Override
	public List<NewsCategoryDTO> pageWithChildren(NewsCategoryQuery newsCategoryQuery) {
		List<NewsCategoryDTO> list = converter.to(newsCategoryDao.queryByDisplayPage(DisplayPageEnum.USER.value()));
		//只取前五个栏目
		if (list.size() > 5) {
			list = list.subList(0, 5);
		}
		list.forEach(c -> {
			newsCategoryQuery.setPageSize(2);
			c.setItems(newsDao.getCatNews(newsCategoryQuery).getResultList());
		});
		return list;
	}

	@Override
	public boolean deleteNewsFalg(Long id) {
		if (newsService.getNewsByCatId(id).size() == 0) {
			if (newsCategoryDao.deleteById(id) > 0) {
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public List<NewsCategoryDTO> queryByDisplayPage(Integer displayPage) {
		return converter.to(newsCategoryDao.queryByDisplayPage(displayPage));
	}

	@Override
	public List<NewsCategoryDTO> queryUseAndAll(Integer displayPage, String word) {
		return converter.to(newsCategoryDao.queryUseAndAll(displayPage, word));
	}

	@Override
	public List<NewsCategoryDTO> queryNewsCategoryTree(NewsCategoryQuery query) {
		List<NewsCategoryDTO> newsCategoryList = newsCategoryDao.queryNewsCategoryTree(query);
		if (CollUtil.isEmpty(newsCategoryList)) {
			return newsCategoryList;
		}

		NewsQuery newsQuery = new NewsQuery();
		newsQuery.setPageSize(query.getNewsPageSize());
		for (NewsCategoryDTO newsCategoryDTO : newsCategoryList) {
			newsQuery.setNewsCategoryId(newsCategoryDTO.getId());
			newsCategoryDTO.setItems(newsService.getOnlineNews(newsQuery));
		}
		return newsCategoryList;
	}

	@Override
	public R<NewsCategoryDTO> checkDisplayPage(NewsDTO newsDTO) {
		NewsCategory newsCategory = newsCategoryDao.getById(newsDTO.getNewsCategoryId());
		if (ObjectUtil.isEmpty(newsCategory)) {
			return R.fail("帮助栏目不存在, 请从新确认");
		}
		if (!DisplayPageEnum.ALL_DISPLAY.getValue().equals(newsCategory.getDisplayPage()) && !newsCategory.getDisplayPage().equals(newsDTO.getDisplayPage())) {
			if (DisplayPageEnum.USER.getValue().equals(newsDTO.getDisplayPage())) {
				return R.fail("该帮助栏目不支持,用户端帮助中心");
			}
			if (DisplayPageEnum.SHOP.getValue().equals(newsDTO.getDisplayPage())) {
				return R.fail("该帮助栏目不支持,商家端帮助中心");
			}
			if (DisplayPageEnum.ALL_DISPLAY.getValue().equals(newsDTO.getDisplayPage())) {
				return R.fail("该帮助栏目不支持,同时选择商家帮助中心和用户帮助中心");
			}
		}
		return R.ok();
	}

	@Override
	public PageSupport<NewsCategoryDTO> queryUsePage(NewsCategoryQuery query) {

		PageSupport<NewsCategoryDTO> newsPage = newsCategoryDao.queryNewsCategoryPage(query);


		List<NewsCategoryDTO> resultList = newsPage.getResultList();
		if (CollUtil.isEmpty(resultList)) {
			return newsPage;
		}
		//获取栏目id
		List<Long> ids = resultList.stream().map(NewsCategoryDTO::getId).collect(Collectors.toList());

		List<News> newsList = newsDao.queryNews(ids);
		List<NewsDTO> newsTo = newConverter.to(newsList);
		Map<Long, List<NewsDTO>> newsMap = newsTo.stream().collect(Collectors.groupingBy(NewsDTO::getNewsCategoryId));
		for (NewsCategoryDTO newsCategoryDTO : resultList) {
			newsCategoryDTO.setItems(Optional.ofNullable(newsMap.get(newsCategoryDTO.getId())).orElse(Collections.emptyList()));
			if (newsCategoryDTO.getItems().size() > 3) {
				List<NewsDTO> items = newsCategoryDTO.getItems();
				items = items.subList(0, 3);
				newsCategoryDTO.setStatus(2);
				newsCategoryDTO.setItems(items);
				newsCategoryDTO.setStatus(2);
			}
		}
		return newsPage;
	}
}
