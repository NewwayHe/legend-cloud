/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.shop.dao.ShopCommentStatisticsDao;
import com.legendshop.shop.dto.ShopCommentStatisticsDTO;
import com.legendshop.shop.service.ShopCommentStatisticsService;
import com.legendshop.shop.service.convert.ShopCommentStatisticsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 店铺评分统计服务
 *
 * @author legendshop
 */
@Service("shopCommStatService")
public class ShopCommentStatisticsServiceImpl implements ShopCommentStatisticsService {

	@Autowired
	private ShopCommentStatisticsDao shopCommStatDao;

	@Autowired
	private ShopCommentStatisticsConverter converter;

	@Override
	public ShopCommentStatisticsDTO getById(Long id) {
		return converter.to(shopCommStatDao.getById(id));
	}

	@Override
	public ShopCommentStatisticsDTO getByShopId(Long shopId) {
		return converter.to(shopCommStatDao.getShopCommStatByShopId(shopId));
	}

	@Override
	public int deleteById(Long id) {
		return shopCommStatDao.deleteById(id);
	}

	@Override
	public Long save(ShopCommentStatisticsDTO shopCommStatDTO) {
		if (ObjectUtil.isNotEmpty(shopCommStatDTO.getId())) {
			update(shopCommStatDTO);
			return shopCommStatDTO.getId();
		}
		return shopCommStatDao.save(converter.from(shopCommStatDTO));
	}

	@Override
	public int update(ShopCommentStatisticsDTO shopCommStatDTO) {
		return shopCommStatDao.update(converter.from(shopCommStatDTO));
	}

	@Override
	public ShopCommentStatisticsDTO getShopCommStatByShopIdForUpdate(Long shopId) {
		return converter.to(shopCommStatDao.getShopCommStatByShopIdForUpdate(shopId));
	}

	@Override
	public void saveShopCommStat(ShopCommentStatisticsDTO shopCommStat) {
		shopCommStatDao.save(converter.from(shopCommStat));
	}

	@Override
	public void updateShopCommStat(Integer score, Integer count, Long shopId) {
		shopCommStatDao.updateShopCommStat(score, count, shopId);
	}
}
