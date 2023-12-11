/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.common;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.dto.ShopUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.common.security.utils.UserTokenUtil;
import com.legendshop.shop.bo.ShopDetailBO;
import com.legendshop.shop.dto.SearchShopDTO;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.shop.dto.ShopListDTO;
import com.legendshop.shop.dto.ShopSelectDTO;
import com.legendshop.shop.query.SearchShopQuery;
import com.legendshop.shop.query.ShopDetailQuery;
import com.legendshop.shop.service.ShopDetailService;
import com.legendshop.shop.vo.ShopInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商城详细信息控制器.
 *
 * @author legendshop
 */
@RestController
@Tag(name = "商城详细信息")
@RequestMapping(value = "/shopDetail", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopDetailController {

	@Autowired
	private ShopDetailService shopDetailService;

	@Autowired
	private UserTokenUtil userTokenUtil;

	@Autowired
	private HttpServletRequest request;

	@PostMapping("/getById")
	public R<ShopDetailDTO> getById(@RequestParam Long id) {
		return R.ok(shopDetailService.getById(id));
	}

	@PostMapping("/queryByIds")
	public R<List<ShopDetailDTO>> queryByIds(List<Long> shopIds) {
		return R.ok(shopDetailService.queryByIds(shopIds));
	}

	@PostMapping("/getByUserId")
	public R<ShopDetailDTO> getByUserId(Long userId) {
		return R.ok(shopDetailService.getByUserId(userId));
	}


	@GetMapping
	@Operation(summary = "【商家】获取当前登录商家店铺信息")
	public R<ShopInfoVO> getShopInfo() {
		ShopUserDetail shopUser = SecurityUtils.getShopUser();
		if (null == shopUser) {
			return R.fail("用户登录失效，请重新登录！");
		}
		Long shopId = shopUser.getShopId();
		ShopInfoVO shopInfoVO = this.shopDetailService.getShopInfoVO(shopId);
		return R.ok(shopInfoVO);
	}


	/**
	 * 获取商家店铺统计信息 评分
	 *
	 * @param shopId
	 * @return
	 */
	@GetMapping("/getUserShop")
	public R<ShopDetailBO> getUserShop(@RequestParam(required = false) Long userId, @RequestParam Long shopId) {
		return shopDetailService.getUserShop(userId, shopId);
	}

	@PostMapping("/updateBuys")
	public R<Boolean> updateBuys(Long shopId, Integer buys) {
		return shopDetailService.updateBuys(shopId, buys);
	}

	@GetMapping("/queryAll")
	public R<List<ShopDetailDTO>> queryAll() {
		return shopDetailService.queryAll();
	}

	@PostMapping("/searchShopEs")
	public R<PageSupport<SearchShopDTO>> searchShopEs(@RequestBody SearchShopQuery query) {
		return R.ok(shopDetailService.searchShop(query));
	}

	@PostMapping("/searchAllShop")
	public Long searchAllShop(@RequestBody SearchShopQuery query) {
		return shopDetailService.searchAllShop(query);
	}

	/**
	 * 查询商城详细信息列表.
	 *
	 * @param shopDetailQuery
	 * @return
	 */
	@GetMapping("/query")
	@Operation(summary = "查询商城详细信息", description = "查询商城详细信息")
	public R<PageSupport<ShopDetailDTO>> query(ShopDetailQuery shopDetailQuery) {
		PageSupport<ShopDetailDTO> ps = shopDetailService.getShopDetailPage(shopDetailQuery);
		return R.ok(ps);
	}


	/**
	 * 查看商城详细信息详情.
	 *
	 * @param id
	 * @return the string
	 */
	@GetMapping(value = "/load/{id}")
	@Parameter(name = "id", description = "店铺ID", required = true)
	@Operation(summary = "查看商城详细信息详情", description = "查看商城详细信息详情")
	public R<ShopDetailDTO> load(@PathVariable Long id) {
		return R.ok(shopDetailService.getById(id));
	}

	/**
	 * 更新商城详细信息
	 *
	 * @param shopId
	 * @param requireAudit
	 * @return
	 */
	@Operation(summary = "更新商城详细信息", description = "更新商城详细信息")
	@Parameters({
			@Parameter(name = "shopId", description = "店铺ID", required = true),
			@Parameter(name = "requireAudit", description = "商品是否需要审核 1:是 0:否 为空则采用平台总设置", required = true)
	})
	@PostMapping(value = "/update")
	@ResponseBody
	public R update(Long shopId, Integer requireAudit) {
		ShopDetailDTO shop = shopDetailService.getById(shopId);
		if (shop == null) {
			return R.fail("获取商城详细信息失败");
		}
		shop.setProdRequireAudit(requireAudit);
		return null;
	}

	/**
	 * 查询下拉框选择店铺
	 *
	 * @return
	 */
	@Operation(summary = "查询下拉框选择店铺", description = "查询下拉框选择店铺")
	@GetMapping("/querySelect2")
	public R<PageSupport<ShopSelectDTO>> querySelect2(ShopDetailQuery shopDetailQuery) {
		PageSupport<ShopSelectDTO> ps = shopDetailService.querySelect2(shopDetailQuery);
		return R.ok(ps);
	}

	/**
	 * 店铺搜索
	 *
	 * @return
	 */

	@Operation(summary = "店铺搜索", description = "店铺搜索")
	@GetMapping("/searchShop")
	public R<PageSupport<SearchShopDTO>> searchShop(SearchShopQuery query) {
		query.setUserId(userTokenUtil.getUserId(request));
		PageSupport<SearchShopDTO> ps = shopDetailService.searchShop(query);
		return R.ok(ps);
	}

	@Operation(summary = "根据后台选择店铺显示店铺列表", description = "根据后台选择店铺显示店铺列表")
	@PostMapping("/getShopList")
	public R<List<ShopListDTO>> getShopList(@RequestBody List<Long> ids) {
		List<ShopListDTO> shopList = shopDetailService.queryShopList(ids);
		return R.ok(shopList);
	}


	@GetMapping("/getUserShopES")
	public R<ShopDetailBO> getUserShopEs(@RequestParam(value = "userId", required = false) Long userId, @RequestParam(value = "shopId") Long shopId) {
		return R.ok(shopDetailService.getUserShopEs(userId, shopId));
	}

}
