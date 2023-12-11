/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.activity.dao.CouponDao;
import com.legendshop.activity.dao.CouponProductDao;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.dto.CouponProductDTO;
import com.legendshop.activity.entity.CouponProduct;
import com.legendshop.activity.mq.producer.CouponProducerService;
import com.legendshop.activity.service.CouponProductService;
import com.legendshop.activity.service.convert.CouponProductConverter;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.dto.ProductItemDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券商品.
 *
 * @author legendshop
 */
@Service
@AllArgsConstructor
@Slf4j
public class CouponProductServiceImpl implements CouponProductService {

	private final CouponProductDao couponProductDao;

	private final CouponDao couponDao;

	private final CouponProducerService couponProducerService;

	private final CouponProductConverter converter;

	@Override
	public int delete(Long skuId, Long couponId) {
		return couponProductDao.deleteBySkuIds(couponId, Collections.singletonList(skuId));
	}

	@Override
	public int updateCouponProduct(CouponProductDTO couponProductDTO) {
		return couponProductDao.update(converter.from(couponProductDTO));
	}

	@Override
	public List<CouponProductDTO> queryByCouponId(Long couponId) {
		return converter.to(couponProductDao.queryByCouponId(couponId));
	}

	@Override
	public List<ProductItemDTO> queryInfoByCouponId(Long couponId) {
		return couponProductDao.queryInfoByCouponId(couponId);
	}


	@Override
	public Integer updateStatus(List<Long> ids, Integer status) {
		return couponProductDao.updateStatus(ids, status);
	}

	@Override
	public Integer updateInvalidStatus(List<Long> ids) {
		return couponProductDao.updateInvalidStatus(ids);
	}

	@Override
	public Integer updateStatus(List<Long> ids, Integer status, Integer oldStatus) {
		return couponProductDao.updateStatus(ids, status, oldStatus);
	}

	@Override
	public Long update(CouponDTO couponDTO) {
		Long couponId = couponDTO.getId();
		List<Long> selectSkuId = couponDTO.getSelectSkuId();
		List<Long> unSelectSkuId = couponDTO.getUnSelectSkuId();
		/*判断有无优惠券id，没有则生成一个*/
		if (ObjectUtil.isEmpty(couponId)) {
			couponId = couponDao.createId();
			/*发送延时队列，如果15min不保存优惠券，就删除优惠券商品*/
			couponProducerService.deleteCouponProduct(couponId);
		}
		if (CollUtil.isNotEmpty(selectSkuId)) {
			List<CouponProduct> couponProductList = new ArrayList<>();
			/*查询数据库已选的sku*/
			List<CouponProduct> originSelectSkus = couponProductDao.queryAllBySkuIds(couponId, selectSkuId);
			if (CollUtil.isNotEmpty(originSelectSkus)) {
				List<Long> collect = originSelectSkus.stream().map(CouponProduct::getSkuId).collect(Collectors.toList());
				//移除所有已保存的，剩下的就去新增
				selectSkuId.removeAll(collect);
			}
			//数据库没有直接新增
			if (CollUtil.isNotEmpty(selectSkuId)) {
				for (Long skuId : selectSkuId) {
					CouponProduct couponProduct = new CouponProduct();
					couponProduct.setCouponId(couponId);
					couponProduct.setSkuId(skuId);
					couponProductList.add(couponProduct);
				}
				couponProductDao.save(couponProductList);
			}
		}
		if (CollUtil.isNotEmpty(unSelectSkuId)) {
			/*删除*/
			couponProductDao.deleteBySkuIds(couponId, unSelectSkuId);
		}
		return couponId;
	}

	@Override
	public R copyCouponProduct(Long oldCouponId) {
		Long couponId = couponDao.createId();
		List<CouponProduct> couponProductList = couponProductDao.queryByCouponId(oldCouponId);
		if (CollUtil.isNotEmpty(couponProductList)) {
			for (CouponProduct couponProduct : couponProductList) {
				couponProduct.setId(null);
				couponProduct.setCouponId(couponId);
			}
			couponProductDao.save(couponProductList);
		}
		return R.ok(couponId);
	}
}
