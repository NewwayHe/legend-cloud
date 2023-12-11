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
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageParams;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.product.api.ProductApi;
import com.legendshop.product.api.ProductCommentStatisticsApi;
import com.legendshop.product.bo.HotSellProductBO;
import com.legendshop.product.bo.ProductCommentStatisticsBO;
import com.legendshop.shop.bo.LogisticsCommentStatisticsBO;
import com.legendshop.shop.dao.FavoriteShopDao;
import com.legendshop.shop.dto.BatchFavoriteShopDTO;
import com.legendshop.shop.dto.FavoriteShopDTO;
import com.legendshop.shop.dto.HotProductDTO;
import com.legendshop.shop.dto.ShopCommentStatisticsDTO;
import com.legendshop.shop.entity.FavoriteShop;
import com.legendshop.shop.enums.ShopExceptionConstant;
import com.legendshop.shop.query.ShopFavoriteQuery;
import com.legendshop.shop.service.FavoriteShopService;
import com.legendshop.shop.service.LogisticsCommentStatisticsService;
import com.legendshop.shop.service.ShopCommentStatisticsService;
import com.legendshop.shop.service.convert.FavoriteShopConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 店铺收藏服务.
 *
 * @author legendshop
 */
@Service
public class FavoriteShopServiceImpl implements FavoriteShopService {

	@Autowired
	private FavoriteShopDao favoriteShopDao;

	@Autowired
	private FavoriteShopConverter favoriteShopConverter;

	@Autowired
	private ProductApi productApi;

	@Autowired
	private LogisticsCommentStatisticsService logisticsCommentStatisticsService;

	@Autowired
	private ShopCommentStatisticsService shopCommStatService;

	@Autowired
	private ProductCommentStatisticsApi productCommentStatisticsApi;

	@Override
	public FavoriteShopDTO getById(Long id) {
		return favoriteShopConverter.to(favoriteShopDao.getById(id));
	}

	@Override
	public void deleteById(Long id) {
		favoriteShopDao.deleteById(id);
	}

	@Override
	public Long save(FavoriteShopDTO favoriteShop) {
		return favoriteShopDao.save(favoriteShopConverter.from(favoriteShop));
	}

	@Override
	public void update(FavoriteShopDTO favoriteShop) {
		favoriteShopDao.update(favoriteShopConverter.from(favoriteShop));
	}

	@Override
	public Boolean deleteByUserId(Long id, Long userId) {
		return favoriteShopDao.deleteByUserId(id, userId);
	}

	@Override
	public Boolean isExists(Long userId, Long shopId) {
		return favoriteShopDao.isExists(userId, shopId);
	}


	@Override
	public void deleteByUserIdByIds(Long userId, String ids) {
		List<Long> idList = JSONUtil.toList(JSONUtil.parseArray(userId), Long.class);
		favoriteShopDao.deleteByUserIdByIds(userId, idList);
	}

	@Override
	public void deleteAllByUserId(Long userId) {
		favoriteShopDao.deleteAllByUserId(userId);
	}

	@Override
	public Integer deleteByShopIdAndUserId(Long shopId,
										   Long userId) {
		int result = favoriteShopDao.deleteByShopIdAndUserId(shopId, userId);
		return result;
	}

	@Override
	public R<Integer> userFavoriteCount(Long userId) {
		return R.ok(Optional.ofNullable(favoriteShopDao.userFavoriteCount(userId)).orElse(0));
	}


	@Override
	public PageSupport<FavoriteShopDTO> query(PageParams pageParams, Long userId) {
		return favoriteShopConverter.page(favoriteShopDao.querySimplePage(pageParams, userId));
	}

	@Override
	public PageSupport<FavoriteShopDTO> queryFavoriteShopPageList(ShopFavoriteQuery shopFavoriteQuery) {
		PageSupport<FavoriteShopDTO> page = favoriteShopDao.queryPage(shopFavoriteQuery);
		List<FavoriteShopDTO> list = page.getResultList();
		list.forEach(s -> {
			List<HotSellProductBO> productBoList = productApi.queryHotSellProdByShopId(s.getShopId()).getData();
			List<HotProductDTO> productDTOList = convertHotProduct(productBoList);
			s.setItems(productDTOList);

			Double shopScore = 0d;
			Double prodScore = 0d;
			Double logisticsScore = 0d;
			//计算该店铺所有已评分物流的平均分
			LogisticsCommentStatisticsBO statBO = logisticsCommentStatisticsService.getLogisticsCommStatByShopId(s.getShopId());
			double dvyTypeAvg = 0d;
			if (statBO != null && statBO.getScore() != null && statBO.getCount() != null) {
				logisticsScore = statBO.getScore() / statBO.getCount().doubleValue();
				dvyTypeAvg = new BigDecimal(logisticsScore).setScale(1, RoundingMode.HALF_UP).doubleValue();
			}
			s.setDvyTypeAvg(dvyTypeAvg);

			//计算该店铺服务的平均分
			ShopCommentStatisticsDTO shopCommentStat = shopCommStatService.getByShopId(s.getShopId());
			double shopCommAvg = 0d;
			if (shopCommentStat != null && shopCommentStat.getScore() != null && shopCommentStat.getCount() != null) {
				shopScore = shopCommentStat.getScore() / shopCommentStat.getCount().doubleValue();
				shopCommAvg = new BigDecimal(shopScore).setScale(1, RoundingMode.HALF_UP).doubleValue();
			}
			s.setShopCommAvg(shopCommAvg);

			//计算该店铺所有已评分商品的平均分
			ProductCommentStatisticsBO prodCommentStat = productCommentStatisticsApi.getProductCommentStatByShopId(s.getShopId()).getData();
			double productCommentAvg = 0d;
			if (prodCommentStat != null && prodCommentStat.getScore() != null && prodCommentStat.getComments() != null) {
				prodScore = prodCommentStat.getScore() / prodCommentStat.getComments().doubleValue();
				productCommentAvg = new BigDecimal(prodScore).setScale(1, RoundingMode.HALF_UP).doubleValue();
			}
			s.setProductCommentAvg(productCommentAvg);

			//计算该店铺综合评分
			double v = (s.getDvyTypeAvg() + s.getShopCommAvg() + s.getProductCommentAvg()) / 3;
			s.setScore(BigDecimal.valueOf(v).setScale(1, RoundingMode.HALF_UP).doubleValue());
		});
		return page;
	}


	private List<HotProductDTO> convertHotProduct(List<HotSellProductBO> list) {
		List<HotProductDTO> productDTOList = new ArrayList<>();
		list.forEach(l -> {
			HotProductDTO p = new HotProductDTO();
			BeanUtils.copyProperties(l, p);
			productDTOList.add(p);
		});
		return productDTOList;
	}

	/**
	 * 店铺收藏
	 *
	 * @param favoriteShopDTO
	 * @return
	 */
	@Override
	public Boolean favoriteShopFlag(BatchFavoriteShopDTO favoriteShopDTO) {
		if (CollUtil.isEmpty(favoriteShopDTO.getShopIds())) {
			throw new BusinessException("店铺ID不能为空");
		}

		if (favoriteShopDTO.getCollectionFlag()) {
			FavoriteShop favoriteShop = favoriteShopDao.getByProperties(new EntityCriterion()
					.eq("userId", favoriteShopDTO.getUserId()).
							eq("shopId", favoriteShopDTO.getShopIds().get(0)));
			if (favoriteShop != null) {
				throw new BusinessException(ShopExceptionConstant.EXIST_FAVORITE_SHOP, "已经关注店铺！");
			}

			favoriteShop = new FavoriteShop();
			favoriteShop.setUserId(favoriteShopDTO.getUserId());
			favoriteShop.setShopId(favoriteShopDTO.getShopIds().get(0));
			favoriteShop.setRecDate(DateUtil.date());
			favoriteShopDao.save(favoriteShop);
			return true;
		} else if (!favoriteShopDTO.getCollectionFlag()) {
			favoriteShopDao.deleteByShopIdAndUserId(favoriteShopDTO.getShopIds(), favoriteShopDTO.getUserId());
			return true;
		}
		return false;
	}


	@Override
	public void deleteFavs(Long userId, Long[] selectedFavs) {
		favoriteShopDao.deletefavoriteShops(selectedFavs, userId);
	}
}
