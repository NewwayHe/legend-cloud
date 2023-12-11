/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import com.legendshop.shop.dao.NewsPositionDao;
import com.legendshop.shop.dto.NewsPositionDTO;
import com.legendshop.shop.service.NewsPositionService;
import com.legendshop.shop.service.convert.NewsPositionConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章位置中间表
 *
 * @author legendshop
 */
@Service
public class NewsPositionServiceImpl implements NewsPositionService {

	@Autowired
	private NewsPositionDao newsPositionDao;

	@Autowired
	private NewsPositionConverter converter;

	@Override
	public Long saveNewsPosition(NewsPositionDTO newsPosition) {
		return newsPositionDao.save(converter.from(newsPosition));
	}

	@Override
	public NewsPositionDTO getNewsPositionyById(Long id) {
		return converter.to(newsPositionDao.getById(id));
	}

	@Override
	public List<NewsPositionDTO> getNewsPositionList() {
		return converter.to(newsPositionDao.queryAll());
	}

	@Override
	public int delete(Long id) {
		return newsPositionDao.deleteById(id);
	}

	@Override
	public int updateNewsPosition(NewsPositionDTO newsPosition) {
		return newsPositionDao.update(converter.from(newsPosition));
	}

	@Override
	public List<NewsPositionDTO> getNewsPositionByNewsId(Long newsId) {
		return converter.to(newsPositionDao.getNewsPositionByNewsId(newsId));
	}

}
