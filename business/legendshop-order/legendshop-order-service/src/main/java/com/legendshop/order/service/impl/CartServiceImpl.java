/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.activity.api.CouponApi;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.data.cache.util.CacheManagerUtil;
import com.legendshop.order.dao.CartDao;
import com.legendshop.order.dto.CartChangePromotionDTO;
import com.legendshop.order.dto.CartItemDTO;
import com.legendshop.order.dto.ShopCartDTO;
import com.legendshop.order.dto.SubmitOrderSkuDTO;
import com.legendshop.order.service.CartService;
import com.legendshop.order.service.convert.CartConverter;
import com.legendshop.order.vo.ShopCartViewVO;
import com.legendshop.product.api.TransFeeCalculateApi;
import com.legendshop.product.dto.TransFeeCalProductDTO;
import com.legendshop.product.dto.TransFeeCalculateDTO;
import com.legendshop.product.enums.TransCalFeeResultEnum;
import com.legendshop.user.api.UserAddressApi;
import com.legendshop.user.bo.UserAddressBO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.legendshop.common.core.constant.CacheConstants.CART_ITEMS;
import static com.legendshop.common.core.constant.CacheConstants.CART_ITEMS_TMP;

/**
 * 购物车服务实现
 *
 * @author legendshop
 */
@Service
@AllArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

	private final CartDao cartDao;
	private final ObjectMapper objectMapper;
	private final CouponApi couponApi;
	private final CartConverter cartConverter;
	private final CacheManagerUtil cacheManagerUtil;
	private final UserAddressApi userAddressApi;
	private final TransFeeCalculateApi transFeeCalculateApi;

	@Override
	public ShopCartViewVO buildShopCartVO(Long userId, Long addressId, List<CartItemDTO> cartItemDTOList) {
		if (CollUtil.isEmpty(cartItemDTOList)) {
			return null;
		}
		ShopCartViewVO shopCartViewVO = new ShopCartViewVO();
		//店铺分组
		Map<Long, List<CartItemDTO>> shopCartMap = cartItemDTOList.stream().collect(Collectors.groupingBy(CartItemDTO::getShopId));
		List<ShopCartDTO> shopCarts = new ArrayList<>();
		UserAddressBO defaultAddress;
		R<UserAddressBO> userAddressBOR;
		if (ObjectUtil.isNull(addressId)) {
			userAddressBOR = this.userAddressApi.getDefaultAddress(userId);
		} else {
			userAddressBOR = userAddressApi.getAddressInfo(addressId);
		}
		if (!userAddressBOR.getSuccess()) {
			throw new BusinessException(userAddressBOR.getMsg());
		}
		defaultAddress = userAddressBOR.getData();
		int selectCount = 0;
		BigDecimal totalPrice = BigDecimal.ZERO;
		BigDecimal discountAmount = BigDecimal.ZERO;
		TransFeeCalculateDTO transFeeCalculateDTO = new TransFeeCalculateDTO();
		for (Long shopId : shopCartMap.keySet()) {
			//获取所有项
			List<CartItemDTO> cartItemList = shopCartMap.get(shopId);
			//获取所有选中的购物车集合
			List<CartItemDTO> selectCartItems = cartItemList.stream().filter(CartItemDTO::getSelectFlag).collect(Collectors.toList());
			//如果有默认地址，计算是否限售
			if (ObjectUtil.isNotNull(defaultAddress)) {
				transFeeCalculateDTO.setCityId(defaultAddress.getCityId());
				transFeeCalculateDTO.setShopId(shopId);
				transFeeCalculateDTO.setCalProductDTOS(cartConverter.convert2TransFeeCalProductDtoList(cartItemList));
				R transFeeResult = transFeeCalculateApi.calTransFee(transFeeCalculateDTO);
				//处理区域限售
				regionalSales(transFeeResult, cartItemList);
			}


			for (CartItemDTO item : selectCartItems) {
				selectCount += item.getTotalCount();
				totalPrice = NumberUtil.add(totalPrice, item.getActualTotalPrice());
				discountAmount = NumberUtil.add(discountAmount, item.getDiscountAmount());
			}

			String shopName = cartItemList.get(0).getShopName();
			//计算是否有优惠券可以使用
			R<List<CouponDTO>> r = couponApi.listReceivable(CouponQuery.builder().shopId(shopId).build());
			if (!r.getSuccess()) {
				throw new BusinessException(r.getMsg());
			}
			Boolean haveCouponFlag = CollUtil.isNotEmpty(r.getData());
			ShopCartDTO shopCartDTO = new ShopCartDTO().buildShopCartDTO(shopId, shopName, haveCouponFlag, cartItemList);
			shopCarts.add(shopCartDTO);
		}


		shopCartViewVO.setDefaultUserAddress(defaultAddress);
		shopCartViewVO.setTotalPrice(totalPrice);
		shopCartViewVO.setDiscountAmount(discountAmount);
		shopCartViewVO.setSelectCount(selectCount);
		shopCartViewVO.setShopCartList(shopCarts);

		//如果金额小于o重新赋值
		int i = totalPrice.compareTo(BigDecimal.ZERO);
		if (i < 0) {
			shopCartViewVO.setTotalPrice(BigDecimal.ZERO);
		}
		return shopCartViewVO;
	}

	/**
	 * 处理区域限售
	 *
	 * @param calTransFeeResult
	 * @param cartItemList
	 */
	private List<CartItemDTO> regionalSales(R calTransFeeResult, List<CartItemDTO> cartItemList) {
		if (!calTransFeeResult.success() && calTransFeeResult.getCode() == TransCalFeeResultEnum.NOT_SUPPORT_AREA.getCode()) {
			// 处理拆分为微服务后，强转失败问题。TODO 待研究更好的解决方案 https://blog.csdn.net/keygod1/article/details/84070982
			List<TransFeeCalProductDTO> resultData = new ArrayList<>();
			List data = (List) calTransFeeResult.getData();
			for (Object o : data) {
				TransFeeCalProductDTO transFeeCalProductDTO = objectMapper.convertValue(o, TransFeeCalProductDTO.class);
				resultData.add(transFeeCalProductDTO);
			}
			cartItemList.forEach(sku -> {
				resultData.forEach(resultSku -> {
					if (sku.getProductId().equals(resultSku.getProductId())) {
						sku.setRegionalSalesFlag(Boolean.TRUE);
						log.info("###### 区域限售商品 {} ##### ", JSONUtil.toJsonStr(sku));
					} else {
						sku.setRegionalSalesFlag(Boolean.FALSE);
					}
				});
			});
		}

		return cartItemList;
	}

	@Override
	public List<CartItemDTO> queryInvalidProductList(Long userId) {
		return cartDao.queryInvalidProductList(userId);
	}

	@Override
	public void cleanInvalidProduct(Long userId) {
		cartDao.cleanInvalidProduct(userId);
	}

	@Override
	@CacheEvict(cacheNames = CART_ITEMS, key = "#userId")
	public void batchClean(Long userId, List<SubmitOrderSkuDTO> skuList) {
		List<Long> skuIds = skuList.stream().filter(SubmitOrderSkuDTO::getStatusFlag).map(SubmitOrderSkuDTO::getSkuId).collect(Collectors.toList());
		if (CollUtil.isEmpty(skuIds)) {
			return;
		}
		//批量清除购物车项
		cartDao.batchClean(userId, skuIds);

		// 还需要清除购物车促销缓存
		List<CartChangePromotionDTO> list = cacheManagerUtil.getCache(CacheConstants.CART_PROMOTION_ITEMS, userId);
		//没有缓存，初始化用户商品项促销活动信息
		if (list == null) {
			return;
		}

		//如果已存在SKU的促销活动选择，则删除
		list.removeIf(u -> skuIds.contains(u.getSkuId()));

		cacheManagerUtil.putCache(CacheConstants.CART_PROMOTION_ITEMS, userId, list);
	}

	@Override
	public R<Void> mergeCart(Long userId, String userKey) {
		// 获取缓存中的购物车信息
		List<CartItemDTO> cartItemList = cacheManagerUtil.getCache(CART_ITEMS_TMP, userKey);
		if (CollUtil.isEmpty(cartItemList)) {
			return R.ok();
		}
		// 将用户ID放入
		cartItemList.forEach(e -> e.setUserId(userId));

		//数据库查询
		List<CartItemDTO> dbCartItemList = cartDao.queryByUserId(userId);
		merge(dbCartItemList, cartItemList);
		cacheManagerUtil.evictCache(CART_ITEMS_TMP, userKey);
		return R.ok();
	}

	/**
	 * 合并cooKie购物车项目
	 *
	 * @param dbCartItemList
	 * @param cooKieCartItemList
	 * @return
	 */
	private void merge(List<CartItemDTO> dbCartItemList, List<CartItemDTO> cooKieCartItemList) {
		Map<Long, CartItemDTO> map = new HashMap<>(dbCartItemList.size() + cooKieCartItemList.size());
		// 将dbCartItemList元素放入Map，以skuId为key
		for (CartItemDTO db : dbCartItemList) {
			map.put(db.getSkuId(), db);
		}
		// 循环cooKieCartItemList，存在则修改，不存在则添加
		for (CartItemDTO cooKie : cooKieCartItemList) {
			CartItemDTO db = map.get(cooKie.getSkuId());
			//新增数据库
			if (ObjectUtil.isNull(db)) {
				map.put(cooKie.getSkuId(), cooKie);
				//保存数据库
				cooKie.setSelectFlag(Boolean.FALSE);
				cartDao.save(cartConverter.convertShoppingCart(cooKie));
			} else {
				//如果有一方为选中状态，则都为选中
				if (db.getSelectFlag() || cooKie.getSelectFlag()) {
					db.setSelectFlag(Boolean.TRUE);
				}
				db.setTotalCount(db.getTotalCount() + cooKie.getTotalCount());
				//修改数据库
				cartDao.update(cartConverter.convertShoppingCart(db));
			}
		}
	}
}
