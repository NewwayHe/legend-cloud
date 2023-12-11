/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.controller.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.activity.api.CouponApi;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.data.cache.util.CacheManagerUtil;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.common.security.utils.UserTokenUtil;
import com.legendshop.order.dto.CartChangePromotionDTO;
import com.legendshop.order.dto.CartItemDTO;
import com.legendshop.order.dto.CartParam;
import com.legendshop.order.dto.ShopCartDTO;
import com.legendshop.order.service.CartService;
import com.legendshop.order.service.impl.handler.CartHandler;
import com.legendshop.order.util.CartUtil;
import com.legendshop.order.vo.ShopCartViewVO;
import com.legendshop.product.api.ProductApi;
import com.legendshop.product.api.SkuApi;
import com.legendshop.product.api.VitLogApi;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.CartVitLogDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.enums.ProductStatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 购物车控制类
 * 登录和未登录都请求这里
 *
 * @author legendshop
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "购物车接口")
public class UserCartController {

	private final Map<String, CartHandler> cartHandler;
	private final CartService cartService;
	private final ProductApi productApi;
	private final SkuApi skuCLient;
	private final CouponApi couponApi;
	private final UserTokenUtil userTokenUtil;
	private CacheManagerUtil cacheManagerUtil;
	private final VitLogApi vitLogApi;

	/**
	 * 查询我的购物车
	 *
	 * @return
	 */
	@GetMapping("/list")
	@Operation(summary = "获取用户购物车信息", description = "获取用户购物车信息")
	@SystemLog("获取用户购物车信息")
	public R<ShopCartViewVO> list(Long addressId) {
		Long userId = SecurityUtils.getUserId();
		List<CartItemDTO> cartItemDTOList = cartHandler(userId).queryCartItems(userId);

		final List<CartChangePromotionDTO> list = cacheManagerUtil.getCache(CacheConstants.CART_PROMOTION_ITEMS, userId);
		//没有缓存，初始化用户商品项促销活动信息
		if (list != null) {
			cartItemDTOList.forEach(u -> {
				System.out.println(u.getSkuId());
				Optional<CartChangePromotionDTO> item = list.stream().filter(i -> i.getSkuId().equals(u.getSkuId())).findFirst();
				item.ifPresent(cartChangePromotionDTO -> u.setMarketingId(cartChangePromotionDTO.getMarketingId()));
			});
		}


		return R.ok(cartService.buildShopCartVO(userId, addressId, cartItemDTOList));
	}

	/**
	 * shopId获取商家可领取的优惠券totalCount
	 *
	 * @param shopId
	 * @return
	 */
	@GetMapping("/{shopId}/coupon/list")
	@Operation(summary = "根据商家id获取可领取优惠券列表", description = "根据商家id获取可领取优惠券列表")
	public R<List<CouponDTO>> couponList(@PathVariable Long shopId) {
		return couponApi.listReceivable(CouponQuery.builder().shopId(shopId).build());
	}

	/**
	 * 获取购物车的数量
	 *
	 * @return
	 */
	@GetMapping("/count")
	@Operation(summary = "获取购物车商品数量", description = "获取所有购物车商品数量")
	public R<Integer> productCount(HttpServletRequest request) {
		//此方法是开放权限的，不能用上下文获取用户 newway
		Long userId = userTokenUtil.getUserId(request);
		List<CartItemDTO> shopCartItems = cartHandler(userId).queryCartItems(userId);
		if (CollectionUtil.isEmpty(shopCartItems)) {
			return R.ok(0);
		}
		return R.ok(shopCartItems.size());
	}

	/**
	 * 添加购物车
	 *
	 * @param cartParam
	 * @return
	 */
	@PostMapping("/add")
	@Operation(summary = "添加购物车", description = "添加购物车")
	public R add(@Valid @RequestBody CartParam cartParam, HttpServletRequest request) {
		ProductDTO productDTO = productApi.getDtoByProductId(cartParam.getProductId()).getData();
		SkuBO skuBO = skuCLient.getSkuById(cartParam.getSkuId()).getData();
		if (!ProductStatusEnum.PROD_ONLINE.getValue().equals(productDTO.getStatus())) {
			return R.fail("商品不存在或已下线");
		}
		Long userId = userTokenUtil.getUserId(request);
		List<CartItemDTO> cartItems = cartHandler(userId).queryCartItems(userId);
		if (CollUtil.isNotEmpty(cartItems)) {
			CartItemDTO itemDTO = cartItems.stream().filter(item -> item.getSkuId().equals(cartParam.getSkuId())).findFirst().orElse(null);
			if (ObjectUtil.isNotNull(itemDTO)) {
				// 如果不为空，则覆盖原先的物料URL
				if (ObjectUtil.isNotEmpty(cartParam.getMaterialUrl())) {
					itemDTO.setMaterialUrl(cartParam.getMaterialUrl());
				}
				itemDTO.setTotalCount(itemDTO.getTotalCount() + cartParam.getCount());
				cartHandler(userId).update(userId, itemDTO);

				CartVitLogDTO logDTO = new CartVitLogDTO();
				logDTO.setShopId(productDTO.getShopId());
				logDTO.setProductId(cartParam.getProductId());
				logDTO.setProductName(productDTO.getName());
				logDTO.setUserId(SecurityUtils.getUserId());
				logDTO.setSkuId(cartParam.getSkuId());
				logDTO.setCount(cartParam.getCount());
				logDTO.setSource(request.getHeader(RequestHeaderConstant.SOURCE_KEY));
				vitLogApi.saveProdCartView(logDTO);
				return R.ok(Boolean.TRUE);
			}
		}
		cartHandler(userId).save(userId, new CartItemDTO(productDTO, skuBO, cartParam));
		CartVitLogDTO logDTO = new CartVitLogDTO();
		logDTO.setShopId(productDTO.getShopId());
		logDTO.setProductId(cartParam.getProductId());
		logDTO.setProductName(productDTO.getName());
		logDTO.setUserId(SecurityUtils.getUserId());
		logDTO.setSkuId(cartParam.getSkuId());
		logDTO.setCount(cartParam.getCount());
		logDTO.setSource(request.getHeader(RequestHeaderConstant.SOURCE_KEY));
		vitLogApi.saveProdCartView(logDTO);
		return R.ok(Boolean.TRUE);
	}


	@PostMapping("/changePromotion")
	@Operation(summary = "购物车单品修改促销活动", description = "购物车单品修改促销活动")
	public R<Boolean> cartChangePromotion(@Valid @RequestBody CartChangePromotionDTO cartChangePromotionDTO) {
		Long userId = SecurityUtils.getUserId();

		//判断SKU是否已添加到购物车
		Boolean isExistSku = false;

		List<CartItemDTO> cartItems = cartHandler(userId).queryCartItems(userId);
		if (CollUtil.isNotEmpty(cartItems)) {
			CartItemDTO existItemDTO = cartItems.stream().filter(item -> item.getSkuId().equals(cartChangePromotionDTO.getSkuId())).findFirst().orElse(null);
			if (existItemDTO != null) {
				//如果购物车里面存在该SKU，才能修改商品促销活动信息
				isExistSku = true;
			}
		}
		if (!isExistSku) {
			throw new BusinessException("当前购物车不存在当前商品！");
		}

		List<CartChangePromotionDTO> list = cacheManagerUtil.getCache(CacheConstants.CART_PROMOTION_ITEMS, userId);
		//没有缓存，初始化用户商品项促销活动信息
		if (list == null) {
			list = new ArrayList<CartChangePromotionDTO>();
		}

		//如果已存在SKU的促销活动选择，则删除，再更新
		Optional<CartChangePromotionDTO> item = list.stream().filter(u -> u.getSkuId().equals(cartChangePromotionDTO.getSkuId())).findFirst();
		if (item.isPresent()) {
			list.remove(item.get());
		}

		//添加缓存
		list.add(cartChangePromotionDTO);
		cacheManagerUtil.putCache(CacheConstants.CART_PROMOTION_ITEMS, userId, list);
		//返回操作完成
		return R.ok(true);
	}


	/**
	 * 1.操作购物车
	 * 2.换促销  默认选中状态
	 * 3.加减数量
	 */
	@PostMapping("/change")
	@Operation(summary = "新增、修改sku、购物车数量", description = "修改购物车，传递最终的值，不需要加减。")
	public R change(@Valid @RequestBody CartParam cartParam, HttpServletRequest request) {
		Long userId = SecurityUtils.getUserId();
		List<CartItemDTO> cartItems = cartHandler(userId).queryCartItems(userId);
		if (CollUtil.isNotEmpty(cartItems)) {
			//id不为空就是修改sku
			CartItemDTO itemDTO = cartItems.stream().filter(item -> item.getId().equals(cartParam.getId())).findFirst().orElse(null);
			if (ObjectUtil.isNotNull(itemDTO)) {
				if (cartParam.getCount() > itemDTO.getTotalCount()) {
					ProductDTO productDTO = productApi.getDtoByProductId(cartParam.getProductId()).getData();
					CartVitLogDTO logDTO = new CartVitLogDTO();
					logDTO.setShopId(productDTO.getShopId());
					logDTO.setProductId(cartParam.getProductId());
					logDTO.setProductName(productDTO.getName());
					logDTO.setUserId(SecurityUtils.getUserId());
					logDTO.setSkuId(cartParam.getSkuId());
					logDTO.setCount(cartParam.getCount() - itemDTO.getTotalCount());
					logDTO.setSource(request.getHeader(RequestHeaderConstant.SOURCE_KEY));
					vitLogApi.saveProdCartView(logDTO);
				}

				CartItemDTO existItemDTO = cartItems.stream().filter(item -> item.getSkuId().equals(cartParam.getSkuId())).findFirst().orElse(null);
				if (existItemDTO != null && !itemDTO.getId().equals(existItemDTO.getId())) {
					//如果购物车里面已经有了该SKU就合并,删除当前的sku，合并到旧有的sku，增加数量

					existItemDTO.setTotalCount(existItemDTO.getTotalCount() + cartParam.getCount());
					cartHandler(userId).update(userId, existItemDTO);

					cartHandler(userId).delete(userId, Arrays.asList(itemDTO.getId()));

				} else {
					itemDTO.setTotalCount(cartParam.getCount());
					itemDTO.setSkuId(cartParam.getSkuId());

					// 更新sku价格
					R<SkuBO> skuBOR = skuCLient.getSkuById(cartParam.getSkuId());
					if (!skuBOR.getSuccess() || ObjectUtil.isEmpty(skuBOR.getData())) {
						return R.fail("当前规格不存在！");
					}
					itemDTO.setPrice(skuBOR.getData().getPrice());

					cartHandler(userId).update(userId, itemDTO);
				}

				return R.ok(Boolean.TRUE);
			}
		}
		return R.ok(Boolean.TRUE);
	}


	@PostMapping("/select")
	@Operation(summary = "正选、反选、全选购物车", description = "正选、反选、全选购物车，传递购物车id")
	public R<Boolean> select(@RequestBody List<CartParam> cartParamList) {
		Long userId = SecurityUtils.getUserId();
		List<CartItemDTO> shopCartItems = cartHandler(userId).queryCartItems(userId);
		Map<Long, Boolean> idToNameMap = cartParamList.stream()
				.collect(Collectors.toMap(CartParam::getId, CartParam::getSelectFlag, (id, value) -> id));
		List<CartItemDTO> chooseCartItems = new ArrayList<>();
		for (CartItemDTO cartItemDTO : shopCartItems) {
			if (idToNameMap.containsKey(cartItemDTO.getId())) {
				cartItemDTO.setSelectFlag(idToNameMap.get(cartItemDTO.getId()));
				chooseCartItems.add(cartItemDTO);
			}
		}
		cartHandler(userId).update(userId, chooseCartItems);
		return R.ok();
	}

	/**
	 * 删除购物车项
	 *
	 * @param cartIds
	 * @return
	 */
	@DeleteMapping("/delete")
	@Operation(summary = "删除用户购物车商品", description = "传递购物车id，删除对应的购物车")
	public R<Boolean> delete(@RequestBody List<Long> cartIds) {
		Long userId = SecurityUtils.getUserId();
		cartHandler(userId).delete(userId, cartIds);
		return R.ok();
	}

	/**
	 * 删除所有购物车
	 *
	 * @return
	 */
	@DeleteMapping("/delete/all")
	@Operation(summary = "删除用户所有购物车商品", description = "删除用户所有购物车商品")
	public R<ShopCartViewVO> deleteAll() {
		Long userId = SecurityUtils.getUserId();
		cartHandler(userId).deleteAll(userId);
		return R.ok();
	}


	@GetMapping("/invalidProduct")
	@Operation(summary = "获取购物车失效商品列表", description = "获取购物车失效商品列表")
	public R<List<ShopCartDTO>> invalidProduct() {
		Long userId = SecurityUtils.getUserId();
		List<CartItemDTO> invalidCartItems = cartService.queryInvalidProductList(userId);
		if (CollUtil.isEmpty(invalidCartItems)) {
			return R.ok();
		}
		Map<Long, List<CartItemDTO>> shopCartItemDtoMap = invalidCartItems.stream().collect(Collectors.groupingBy(CartItemDTO::getShopId));
		List<ShopCartDTO> shopCartList = new ArrayList<>();
		shopCartItemDtoMap.forEach((shopId, cartList) -> shopCartList.add(new ShopCartDTO().buildShopCartDTO(shopId, cartList.get(0).getShopName(), cartList)));
		return R.ok(shopCartList);
	}


	@DeleteMapping("/cleanInvalidProduct")
	@Operation(summary = "清空失效商品", description = "清空失效商品")
	public R cleanInvalidProduct() {
		Long userId = SecurityUtils.getUserId();
		cartService.cleanInvalidProduct(userId);
		return R.ok();
	}

	/**
	 * 获取对应的处理类
	 *
	 * @param userId
	 * @return
	 */
	private CartHandler cartHandler(Long userId) {
		String beanName = CartUtil.getBeanName(userId);
		return cartHandler.get(beanName);
	}
}
