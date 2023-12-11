/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.api.SystemConfigApi;
import com.legendshop.basic.dto.CommonPropertiesDTO;
import com.legendshop.basic.dto.SettingDTO;
import com.legendshop.basic.dto.SystemConfigDTO;
import com.legendshop.basic.enums.DecoratePageSourceEnum;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.basic.properties.CommonProperties;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.api.CategoryApi;
import com.legendshop.product.api.ProductApi;
import com.legendshop.product.api.ProductGroupApi;
import com.legendshop.product.bo.ProductGroupRelationBO;
import com.legendshop.product.dto.CategoryTree;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.product.enums.ProductStatusEnum;
import com.legendshop.product.query.ProductGroupRelationQuery;
import com.legendshop.search.api.SearchProductApi;
import com.legendshop.search.dto.ProductDocumentDTO;
import com.legendshop.search.vo.SearchResult;
import com.legendshop.shop.bo.ShopDetailBO;
import com.legendshop.shop.service.DecoratePageService;
import com.legendshop.shop.service.ShopDecoratePageService;
import com.legendshop.shop.service.ShopDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 首页控制层
 *
 * @author legendshop
 */
@Tag(name = "装修页面渲染")
@RestController
@AllArgsConstructor
public class UserIndexController {

	private final DecoratePageService decoratePageService;

	private final SearchProductApi searchProductApi;

	private final SystemConfigApi systemConfigApi;

	private final CategoryApi categoryApi;

	private final ShopDecoratePageService shopDecoratePageService;

	private final ShopDetailService shopDetailService;

	private final ProductApi productApi;

	private final ProductGroupApi productGroupApi;

	private final CommonProperties commonProperties;

	@Operation(summary = "【用户】获取PC首页装修数据")
	@GetMapping("/pc/index")
	@SystemLog("【用户】获取PC首页装修数据")
	public R<String> getPcUsedIndexPage() {
		return decoratePageService.getUsedIndexPage(DecoratePageSourceEnum.PC.value());
	}


	@Operation(summary = "【用户】获取MOBILE首页装修数据")
	@SystemLog("【用户】获取MOBILE首页装修数据")
	@GetMapping("/mobile/index")
	public R<String> getMobileUsedIndexPage() {
		return decoratePageService.getUsedIndexPage(DecoratePageSourceEnum.MOBILE.value());
	}

	@Operation(summary = "[用户]获取PC端主题颜色")
	@GetMapping("/pc/theme/color")
	public R<String> pcThemeColor() {
		return decoratePageService.getThemeColor(DecoratePageSourceEnum.PC.value());
	}


	@Operation(summary = "[用户]获取移动端主题颜色")
	@GetMapping("/mobile/theme/color")
	public R<String> mobileThemeColor() {
		return decoratePageService.getThemeColor(DecoratePageSourceEnum.MOBILE.value());
	}


	@Operation(summary = "【用户】获取PC海报页内容")
	@Parameters({
			@Parameter(name = "pageId", description = "页面ID", required = true),
	})
	@PostMapping("/showPcPosterPage")
	public R<JSONObject> showPcPosterPage(@RequestParam Long pageId) {
		return decoratePageService.getPosterPageById(pageId, DecoratePageSourceEnum.PC.value());
	}


	@Operation(summary = "【用户】获取MOBILE海报页内容")
	@Parameters({
			@Parameter(name = "pageId", description = "页面ID", required = true),
	})
	@PostMapping("/showMobilePosterPage")
	public R<JSONObject> showMobilePosterPage(@RequestParam Long pageId) {
		return decoratePageService.getPosterPageById(pageId, DecoratePageSourceEnum.MOBILE.value());
	}


	@Operation(summary = "【用户】获取MOBILE店铺装修首页内容")
	@Parameters({
			@Parameter(name = "shopId", description = "商家ID", required = true)
	})
	@PostMapping("/show/shop/mobile/indexPage")
	public R<JSONObject> showShopMobileIndexPage(@RequestParam Long shopId) {
		return shopDecoratePageService.getUsedIndexPage(shopId, DecoratePageSourceEnum.MOBILE.value());
	}

	@Operation(summary = "【用户】获取MOBILE个人中心装修数据")
	@GetMapping("/mobile/user/center")
	public R<String> getMobileUsedCenterPage() {
		return decoratePageService.getUsedCenterPage(DecoratePageSourceEnum.MOBILE.value());
	}

	@Operation(summary = "【用户】是否开启个人中心装修")
	@GetMapping("/mobile/enable/user/center")
	public R<Boolean> enableUserCenter() {
		return decoratePageService.enableUserCenter(DecoratePageSourceEnum.MOBILE.value());
	}

	@Operation(summary = "【用户】获取MOBILE分销中心装修数据")
	@GetMapping("/mobile/user/distribution/center")
	public R<String> getMobileUsedDistributionCenterPage() {
		return decoratePageService.getUsedDistributionCenterPage(DecoratePageSourceEnum.MOBILE.value());
	}


	@Operation(summary = "【用户】获取PC店铺装修首页内容")
	@Parameters({
			@Parameter(name = "shopId", description = "商家ID", required = true)
	})
	@PostMapping("/show/shop/pc/indexPage")
	public R<JSONObject> showShopPcIndexPage(@RequestParam Long shopId) {
		return shopDecoratePageService.getUsedIndexPage(shopId, DecoratePageSourceEnum.PC.value());
	}

	@Operation(summary = "【用户】获取商品分组商品")
	@Parameters({
			@Parameter(name = "productGroupId", description = "商品分组ID", required = true),
			@Parameter(name = "pageCount", description = "获取数量", required = true)
	})
	@PostMapping("/groupProduct")
	public R<List<ProductDTO>> getProductByGroup(@RequestParam Long productGroupId, @RequestParam(defaultValue = "10") Integer pageSize) {
		return productApi.getProductListByGroupId(productGroupId, pageSize);
	}

	@Operation(summary = "【用户】查看分组下的商品列表")
	@PostMapping("/groupProduct/page")
	public R<PageSupport<ProductGroupRelationBO>> queryProductList(@RequestBody ProductGroupRelationQuery relationQuery) {
		return productGroupApi.queryProductList(relationQuery);
	}

	@Operation(summary = "【用户】获取分类下的商品")
	@Parameters({
			@Parameter(name = "categoryId", description = "分类ID", required = true),
			@Parameter(name = "pageSize", description = "获取数量", required = true)
	})
	@GetMapping("/categoryProduct")
	public R<List<ProductDocumentDTO>> categoryProduct(@RequestParam Long categoryId, @RequestParam Integer pageSize) {
		return searchProductApi.searchProductByCategoryId(categoryId, pageSize);
	}

	@Operation(summary = "【用户】获取分类下的商品")
	@Parameters({
			@Parameter(name = "categoryId", description = "分类ID", required = true),
			@Parameter(name = "pageSize", description = "获取数量", required = true)
	})
	@GetMapping("/shopCategoryProduct")
	public R<List<ProductDocumentDTO>> shopCategoryProduct(@RequestParam Long categoryId, @RequestParam Integer pageSize) {
		return searchProductApi.searchShopProductByCategoryId(categoryId, pageSize);
	}


	@Operation(summary = "【用户】获取分组内的商品")
	@Parameters({
			@Parameter(name = "prodGroupId", description = "商品分组ID", required = true),
			@Parameter(name = "curPageNO", description = "当前页码")
	})
	@PostMapping("/groupProdList")
	public R<SearchResult<ProductDocumentDTO>> groupProdList(@RequestParam Long prodGroupId, Integer curPageNO) {
		return searchProductApi.getProductPageListByGroup(prodGroupId, curPageNO);
	}


	@Deprecated
	@Operation(summary = "【用户】获取商城基本信息")
	@GetMapping("/systemConfig")
	public R<SystemConfigDTO> systemConfig() {
		R<SystemConfigDTO> systemConfig = systemConfigApi.getSystemConfig();
		return systemConfig;
	}


	@Operation(summary = "【用户】获取商城基本信息")
	@GetMapping("/setting")
	public R<SettingDTO> setting() {
		SettingDTO settingDTO = new SettingDTO();

		R<SystemConfigDTO> systemConfigResult = systemConfigApi.getSystemConfig();
		SystemConfigDTO configDTO = systemConfigResult.getData();

		settingDTO.setSystemConfig(configDTO);

		settingDTO.setServerNowDate(System.currentTimeMillis());
		settingDTO.setThemesColor(decoratePageService.getThemeColor(DecoratePageSourceEnum.MOBILE.value()).getData());
		settingDTO.setPcThemesColor(decoratePageService.getThemeColor(DecoratePageSourceEnum.PC.value()).getData());

		CommonPropertiesDTO commonPropertiesDTO = new CommonPropertiesDTO();
		BeanUtil.copyProperties(commonProperties, commonPropertiesDTO);
		settingDTO.setUrlProperties(commonPropertiesDTO);
		settingDTO.setTabbar(decoratePageService.getUsedIndexPag().getData());
		return R.ok(settingDTO);
	}


	@Operation(summary = "【用户】获取首页展示分类列表")
	@GetMapping("/categoryTreeList")
	public R<List<CategoryTree>> categoryTreeList() {
		return categoryApi.getTreeById(CommonConstants.CATEGORY_TREE_ROOT_ID);
	}


	@Operation(summary = "【用户】根据分组ID获取商品列表")
	@Parameters({
			@Parameter(name = "prodGroupId", description = "商品分组ID", required = true),
			@Parameter(name = "curPageNO", description = "当前页码")
	})
	@PostMapping("/getProductPageList")
	public R<SearchResult<ProductDocumentDTO>> getProductPageList(@RequestParam Long prodGroupId, @RequestParam Integer curPageNO) {
		return searchProductApi.getProductPageListByGroup(prodGroupId, curPageNO);
	}

	@Operation(summary = "【用户】获取购物车角标数量(未实现)")
	@PostMapping("/shopCarCount")
	public R<Integer> shopCarCount() {
		return R.ok(0);
	}

	@Operation(summary = "【用户】获取PC店铺装修海报页内容")
	@Parameters({
			@Parameter(name = "shopId", description = "商家ID", required = true),
			@Parameter(name = "pageId", description = "页面ID", required = true),
	})
	@PostMapping("/show/shop/pc/posterPage")
	public R<JSONObject> showShopPcPosterPage(@RequestParam Long shopId, @RequestParam Long pageId) {
		return shopDecoratePageService.getPosterPageById(shopId, pageId, DecoratePageSourceEnum.PC.value());
	}


	@Operation(summary = "【用户】获取mobile店铺装修海报页内容")
	@Parameters({
			@Parameter(name = "shopId", description = "商家ID", required = true),
			@Parameter(name = "pageId", description = "页面ID", required = true)
	})
	@PostMapping("/show/shop/mobile/posterPage")
	public R<JSONObject> showShopMobilePosterPage(@RequestParam Long shopId, @RequestParam Long pageId) {
		return shopDecoratePageService.getPosterPageById(shopId, pageId, DecoratePageSourceEnum.MOBILE.value());
	}


	@Operation(summary = "【用户】获取店铺相关信息")
	@Parameter(name = "shopId", description = "商家ID", required = true)
	@PostMapping("/shopInfo")
	public R<ShopDetailBO> shopInfo(@RequestParam Long shopId) {
		Long userId = SecurityUtils.getUserId();
		return shopDetailService.getUserShop(userId, shopId);
	}


	@Operation(summary = "【用户】获取服务器时间")
	@GetMapping("/serverNowDate")
	public R<Long> serverNowDate() {
		return R.ok(System.currentTimeMillis());
	}


	@Operation(summary = "【用户】获取商品集合信息")
	@Parameter(name = "productIdList", description = "商品ID集合", required = true)
	@SystemLog("【用户】获取商品集合信息")
	@PostMapping("/productList")
	public R<List<ProductDTO>> productList(@RequestParam List<Long> productIdList) {

		List<ProductDTO> result = productApi.queryAllByIds(productIdList).getData()
				.stream()
				.filter(a -> Objects.equals(a.getStatus(), ProductStatusEnum.PROD_ONLINE.getValue())
						&& Objects.equals(a.getOpStatus(), OpStatusEnum.PASS.getValue())
						&& Objects.equals(a.getDelStatus(), ProductDelStatusEnum.PROD_NORMAL.getValue()))
				.collect(Collectors.toList());
		return R.ok(result);
	}


}
