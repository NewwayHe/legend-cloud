/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.shop;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.BrandBO;
import com.legendshop.product.dto.BrandDTO;
import com.legendshop.product.enums.BrandStatusEnum;
import com.legendshop.product.query.BrandQuery;
import com.legendshop.product.service.BrandService;
import com.legendshop.product.service.ProductPropertyAggBrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "品牌管理")
@RestController
@RequestMapping(value = "/s/brand", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopBrandController {
	@Autowired
	private BrandService brandService;

	@Autowired
	private ProductPropertyAggBrandService aggBrandService;

	/**
	 * 查询所有的在线品牌
	 */
	@GetMapping("/allOnline")
	@PreAuthorize("@pms.hasPermission('s_product_brand_listAllOnLine')")
	@Operation(summary = "【商家】查询所有在线的品牌")
	public R<List<BrandBO>> queryAllOnline(String brandName) {
		return R.ok(brandService.getAllOnline(SecurityUtils.getShopUser().getShopId(), brandName));
	}

	/**
	 * 品牌分页查询
	 */
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('s_product_brand_page')")
	@Operation(summary = "【商家】品牌分页查询")
	public R<PageSupport<BrandBO>> page(BrandQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(brandService.queryPage(query));
	}

	/**
	 * 根据id删除品牌
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('s_product_brand_del')")
	@Operation(summary = "【商家】根据id删除品牌")
	@Parameter(name = "id", description = "品牌ID", required = true)
	@SystemLog(type = SystemLog.LogType.SHOP, value = "根据id删除品牌")
	public R delById(@PathVariable("id") Long id) {
		return brandService.deleteById(id);
	}

	/**
	 * 根据id查询品牌
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('s_product_brand_get')")
	@Operation(summary = "【商家】根据id查询品牌")
	@Parameter(name = "id", description = "品牌ID", required = true)
	public R<BrandBO> getById(@PathVariable("id") Long id) {
		return R.ok(brandService.getById(id));
	}

	/**
	 * 保存品牌
	 *
	 * @param brandDTO
	 * @return
	 */
	@PostMapping
	@SystemLog(type = SystemLog.LogType.SHOP, value = "保存品牌")
	@PreAuthorize("@pms.hasPermission('s_product_brand_add')")
	@Operation(summary = "【商家】保存品牌")
	public R save(@Valid @RequestBody BrandDTO brandDTO) {
		brandDTO.setShopId(SecurityUtils.getShopUser().getShopId());
		//店铺提交的，是下线状态，审核通过后就上线
		brandDTO.setOpStatus(OpStatusEnum.WAIT.getValue());
		brandDTO.setStatus(BrandStatusEnum.BRAND_OFFLINE.value());
		brandDTO.setCommendFlag(false);
		return brandService.save(brandDTO);
	}

	/**
	 * 更新品牌
	 *
	 * @param brandDTO
	 * @return
	 */
	@PutMapping
	@Operation(summary = "【商家】更新品牌")
	@PreAuthorize("@pms.hasPermission('s_product_brand_update')")
	@SystemLog(type = SystemLog.LogType.SHOP, value = "更新品牌")
	public R<String> update(@Valid @RequestBody BrandDTO brandDTO) {
		BrandBO originBrand = brandService.getById(brandDTO.getId());
		//如果审核状态不是拒绝状态，商家不可以更新
		if (OpStatusEnum.DENY.getValue().compareTo(originBrand.getOpStatus()) != 0) {
			return R.fail("审核状态不是拒绝，商家不可以编辑");
		}
		//店铺提交的，先下线，审核通过后就上线
		brandDTO.setOpStatus(OpStatusEnum.WAIT.getValue());
		brandDTO.setStatus(BrandStatusEnum.BRAND_OFFLINE.value());
		return brandService.update(brandDTO);
	}

	/**
	 * 根据类目id查找关联的品牌列表
	 */
	@GetMapping("queryByCategory/{id}")
	@PreAuthorize("@pms.hasPermission('s_product_brand_queryBrandListByCategory')")
	@Parameter(name = "id", description = "类目ID", required = true)
	@Operation(summary = "【商家】根据类目id查找关联的品牌列表")
	public R<List<BrandDTO>> queryBrandListByCategory(@PathVariable Long id) {
		return R.ok(aggBrandService.queryBrandListByCategory(id));
	}
}
