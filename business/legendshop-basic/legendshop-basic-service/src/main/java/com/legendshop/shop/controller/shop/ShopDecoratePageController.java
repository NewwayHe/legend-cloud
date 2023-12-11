/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.shop;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.shop.bo.ShopDecoratePageBO;
import com.legendshop.shop.dto.ShopDecoratePageDTO;
import com.legendshop.shop.query.ShopDecoratePageQuery;
import com.legendshop.shop.service.ShopDecoratePageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺装修页面(ShopDecoratePage)控制层
 *
 * @author legendshop
 */
@Tag(name = "店铺装修页面")
@RestController
@RequestMapping(value = "/s/decoratePage", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopDecoratePageController {


	@Autowired
	private ShopDecoratePageService shopDecoratePageService;

	@Operation(summary = "【商家】获取店铺装修页面分页列表")
	@GetMapping("/list")
	public R<PageSupport<ShopDecoratePageBO>> list(ShopDecoratePageQuery shopDecoratePageQuery) {

		Long shopId = SecurityUtils.getShopUser().getShopId();
		shopDecoratePageQuery.setShopId(shopId);
		PageSupport<ShopDecoratePageBO> result = shopDecoratePageService.queryPageListDesc(shopDecoratePageQuery);
		return R.ok(result);
	}

	@Operation(summary = "【商家】修改店铺装修默认状态")
	@PostMapping("/updateDefaultState")
	public R updateDefaultState(@RequestBody ShopDecoratePageQuery shopDecoratePageQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return shopDecoratePageService.updateDefaultState(shopId, shopDecoratePageQuery.getSource());
	}


	@Operation(summary = "【商家】保存店铺装修")
	@PostMapping("/save")
	public R save(@Valid @RequestBody ShopDecoratePageDTO shopDecoratePageDTO) {

		Long shopId = SecurityUtils.getShopUser().getShopId();
		shopDecoratePageDTO.setShopId(shopId);
		return shopDecoratePageService.save(shopDecoratePageDTO);
	}


	@Operation(summary = "【商家】发布店铺装修")
	@PutMapping("/release")
	public R release(@Valid @RequestBody ShopDecoratePageDTO shopDecoratePageDTO) {

		Long shopId = SecurityUtils.getShopUser().getShopId();
		shopDecoratePageDTO.setShopId(shopId);
		return shopDecoratePageService.release(shopDecoratePageDTO);
	}


	@Operation(summary = "【商家】发布店铺装修页面")
	@Parameter(name = "id", description = "页面ID", required = true)
	@PostMapping("/releasePage")
	public R releasePage(@RequestParam Long id) {
		return shopDecoratePageService.releasePage(id);
	}


	@Operation(summary = "【商家】设为首页")
	@Parameter(name = "id", description = "页面ID", required = true)
	@PostMapping("/use")
	public R use(@RequestParam Long id) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return shopDecoratePageService.usePage(shopId, id);
	}


	@Operation(summary = "【商家】复制页面")
	@Parameters({
			@Parameter(name = "id", description = "页面ID", required = true),
			@Parameter(name = "newName", description = "新页面名称", required = true)
	})
	@PostMapping("/clone")
	public R clone(@RequestParam Long id, @RequestParam String newName) {
		return shopDecoratePageService.clone(id, newName);
	}


	@Operation(summary = "【商家】删除页面")
	@Parameter(name = "id", description = "页面ID", required = true)
	@DeleteMapping("/{id}")
	public R delete(@PathVariable Long id) {
		return shopDecoratePageService.deleteById(id);
	}


	@Operation(summary = "【商家】修改页面名称")
	@Parameters({
			@Parameter(name = "id", description = "页面ID", required = true),
			@Parameter(name = "newName", description = "新页面名称", required = true)
	})
	@PostMapping("/updateName")
	public R updateName(@RequestParam Long id, @RequestParam String newName) {
		return shopDecoratePageService.updateName(id, newName);
	}


	@Operation(summary = "【商家】修改封面图")
	@Parameters({
			@Parameter(name = "id", description = "页面ID", required = true),
			@Parameter(name = "coverPicture", description = "封面图", required = true)
	})
	@PostMapping("/updateCoverPicture")
	public R updateCoverPicture(@RequestParam Long id, @RequestParam String coverPicture) {
		return shopDecoratePageService.updateCoverPicture(id, coverPicture);
	}

	@Operation(summary = "【商家】编辑页面")
	@Parameter(name = "id", description = "页面ID", required = true)
	@PostMapping("/edit")
	public R<ShopDecoratePageBO> edit(@RequestParam Long id) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		if (null == shopId) {
			return R.fail("请登录");
		}
		return shopDecoratePageService.edit(id, shopId);
	}

	@Operation(summary = "【商家】编辑页面")
	@Parameter(name = "id", description = "页面ID", required = true)
	@PostMapping("/show")
	public R<ShopDecoratePageBO> show(@RequestParam Long id) {
		return shopDecoratePageService.edit(id);
	}
}
