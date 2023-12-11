/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import com.legendshop.shop.dto.NewsPositionDTO;

import java.util.List;

/**
 * 文章位置服务.
 *
 * @author legendshop
 */
public interface NewsPositionService {

	Long saveNewsPosition(NewsPositionDTO p);

	NewsPositionDTO getNewsPositionyById(Long id);

	List<NewsPositionDTO> getNewsPositionByNewsId(Long newsId);

	List<NewsPositionDTO> getNewsPositionList();

	int delete(Long id);

	int updateNewsPosition(NewsPositionDTO newsPosition);

}
