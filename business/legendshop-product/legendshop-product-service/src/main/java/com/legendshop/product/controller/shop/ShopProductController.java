/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.shop;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.ValidList;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.dto.ShopUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.ProductAccusationBO;
import com.legendshop.product.bo.ProductAuditBO;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.*;
import com.legendshop.product.entity.ProductImport;
import com.legendshop.product.enums.PreSellPayType;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.product.enums.ProductStatusEnum;
import com.legendshop.product.excel.ProductExportDTO;
import com.legendshop.product.excel.ProductRecycleBinExportDTO;
import com.legendshop.product.excel.StockExportDTO;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.query.SkuQuery;
import com.legendshop.product.query.StockLogQuery;
import com.legendshop.product.service.*;
import com.legendshop.product.utils.ProductStatusChecker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 店铺商品管理
 *
 * @author legendshop
 */
@Tag(name = "商品管理")
@RestController
@RequestMapping(value = "/s/product", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private StockLogService stockLogService;

	@Autowired
	private DraftProductService draftProductService;

	@Autowired
	private SkuService skuService;

	@Autowired
	private ProductStatusChecker productStatusChecker;

	@Autowired
	private ProductImportService productImportService;

	/**
	 * 商品详情
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('s_product_product_get')")
	@Parameter(name = "id", description = "商品ID", required = true)
	@Operation(summary = "【商家】商品详情")
	public R getById(@PathVariable Long id) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		ProductDTO productDTO = productService.getShopProductById(id, shopId);
		if (productDTO != null && ProductDelStatusEnum.PROD_NORMAL.getValue().equals(productDTO.getDelStatus())) {
			return R.ok(productDTO);
		}
		return R.fail("商品不存在");
	}

	/**
	 * 分页查询，以下是URL和说明： newway
	 * status:
	 * 状态：【-2：商家永久删除 -1：商家删除  0：下架 1：上架   2：违规  】
	 * op_status:
	 * 审核操作状态【0：待审核 1：审核通过 -1：拒绝】
	 * <p>
	 * ------------------------------正常商品列表菜单
	 * 被删除商品：
	 * http://192.168.0.128:9999/product/s/product/page?name=&shopId=&brandId=&status=-1&opStatus=1&pageSize=10&curPage=1&sort=
	 * status=-1（必须）
	 * opStatus=1(不要传递这个参数，因为审核拒绝的也会被删除)
	 * <p>
	 * 违规商品
	 * http://192.168.0.128:9999/product/s/product/page?name=&shopId=&brandId=&status=2&opStatus=1&pageSize=10&curPage=1&sort=
	 * status=2（必须）
	 * opStatus=1(不要传递这个参数，审核拒绝的也会变成违规商品)
	 * <p>
	 * 下架商品：
	 * http://192.168.0.128:9999/product/s/product/page?name=&shopId=&brandId=&status=0&opStatus=1&pageSize=10&curPage=1&sort=
	 * status=0（必须）
	 * opStatus=1（必须）
	 * <p>
	 * 上架商品：
	 * http://192.168.0.128:9999/product/s/product/page?name=&shopId=&brandId=&status=1&opStatus=1&pageSize=10&curPage=1&sort=
	 * <p>
	 * status=1（必须）
	 * opStatus=1（必须）
	 * <p>
	 * <p>
	 * 所有商品：
	 * http://192.168.0.128:9999/product/s/product/page?name=&shopId=&brandId=&status=3&opStatus=1&pageSize=10&curPage=1&sort=
	 * <p>
	 * status=3（必须）
	 * opStatus=1（必须）
	 * <p>
	 * <p>
	 * ------------------------------商品审核列表菜单
	 * 所有审核商品：
	 * http://192.168.0.128:9999/product/s/product/page?name=&shopId=&brandId=&opStatus=&pageSize=10&curPage=1&sort=
	 * opStatus=
	 * <p>
	 * <p>
	 * 待审核商品：
	 * http://192.168.0.128:9999/product/s/product/page?name=&shopId=&brandId=&opStatus=0&pageSize=10&curPage=1&sort=
	 * opStatus=0
	 * <p>
	 * <p>
	 * 审核通过商品
	 * http://192.168.0.128:9999/product/s/product/page?name=&shopId=&brandId=&opStatus=1&pageSize=10&curPage=1&sort=
	 * opStatus=1
	 * <p>
	 * <p>
	 * 审核拒绝商品：
	 * http://192.168.0.128:9999/product/s/product/page?name=&shopId=&brandId=&opStatus=-1&pageSize=10&curPage=1&sort=
	 * opStatus=-1
	 * <p>
	 * <p>
	 * -----------------------------------违规商品菜单（应该取消，增加商品回收站的菜单）
	 * 违规商品：
	 * http://192.168.0.128:9999/product/s/product/page?name=&brandId=&status=2&pageSize=10&curPage=1&sort=
	 * status=2
	 * <p>
	 * <p>
	 * http://192.168.0.128:9999/product/admin/product/page?name=&shopId=&brandId=&opStatus=1&status=1&pageSize=10&curPage=1&sort=
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('s_product_product_page')")
	@Operation(summary = "【商家】商品分页查询")
	public R<PageSupport<ProductBO>> page(ProductQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(productService.getProductPage(query));
	}

	/**
	 * 分页查询
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/auditPage")
	@Operation(summary = "【商家】商品审核分页查询")
	public R<PageSupport<ProductAuditBO>> wauditPage(ProductQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(productService.auditPage(query));
	}

	/**
	 * 库存预警分页查询
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/stocksPage")
	@PreAuthorize("@pms.hasPermission('s_product_product_stocksPage')")
	@Operation(summary = "【商家】库存预警分页查询")
	public R<PageSupport<SkuBO>> stocksPage(ProductQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(productService.stocksPage(query));
	}

	/**
	 * 库存列表
	 *
	 * @param query
	 * @return
	 */
	@PostMapping("/stocksList")
	@PreAuthorize("@pms.hasPermission('s_product_product_stocksList')")
	@Operation(summary = "【商家】库存列表")
	public R<List<SkuBO>> stocksList(@RequestBody SkuQuery query) {
		return R.ok(productService.stocksList(query));
	}

	/**
	 * 根据skuIdList查询库存列表
	 *
	 * @param skuIdList
	 * @return
	 */
	@GetMapping("/queryStocksListBySkuIdList")
	@PreAuthorize("@pms.hasPermission('s_product_product_queryStocksListBySkuIdList')")
	@Operation(summary = "【商家】根据skuIdList查询库存列表")
	@Parameter(name = "skuIdList", description = "skuId集合", required = true)
	public R<List<SkuBO>> queryStocksListBySkuIdList(@RequestParam List<Long> skuIdList) {
		return R.ok(productService.queryStocksListBySkuIdList(skuIdList));
	}

	/**
	 * 库存预警导出
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/stocksExport")
	@PreAuthorize("@pms.hasPermission('s_product_product_stocksExport')")
	@Operation(summary = "【商家】库存预警导出")
	@ExportExcel(name = "商品库存列表", sheet = "商品库存列表")
	public List<StockExportDTO> stocksExport(@Valid ProductQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return productService.stocksExport(query);

	}

	/**
	 * 库存日志分页查询
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/stocksLogPage")
	@PreAuthorize("@pms.hasPermission('s_product_product_stocksLogPage')")
	@Operation(summary = "【商家】库存日志分页查询")
	public R<PageSupport<StockLogDTO>> stocksLogPage(@Valid StockLogQuery query) {
		return R.ok(stockLogService.loadStockLog(query));
	}

	/**
	 * 导出商品信息
	 *
	 * @param query
	 * @param response
	 * @return
	 */
	@GetMapping(value = "/export")
	@PreAuthorize("@pms.hasPermission('s_product_product_export')")
	@Operation(summary = "【商家】导出商品信息")
	@ExportExcel(name = "商品列表", sheet = "商品列表")
	public List<ProductExportDTO> export(@Valid ProductQuery query, HttpServletResponse response) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return productService.findExportProd(query);
	}

	/**
	 * 导出excel商品信息
	 *
	 * @param query
	 * @return
	 */
	@GetMapping(value = "/recycleBin/export")
	@Operation(summary = "【后台】导出商品回收站信息")
	@ExportExcel(name = "商品回收站列表", sheet = "商品回收站列表")
	public List<ProductRecycleBinExportDTO> exportRecycleBin(@Valid ProductQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return productService.findExportRecycleBinProd(query);
	}

	/**
	 * 批量更新商品状态
	 *
	 * @param productDTO
	 * @return
	 */
	@PutMapping("/batchUpdateStatus")
	@SystemLog("批量更新商品状态")
	@PreAuthorize("@pms.hasPermission('s_product_product_batchUpdateStatus')")
	@Operation(summary = "【商家】批量更新商品状态")
	public R batchUpdateStatus(@RequestBody @Valid ProductBranchDTO productDTO) {
		productDTO.setShopId(SecurityUtils.getShopUser().getShopId());
		return productService.batchUpdateStatus(productDTO);
	}

	/**
	 * 批量更新商品删除状态
	 *
	 * @param productDTO
	 * @return
	 */
	@PutMapping("/batchUpdateDelStatus")
	@SystemLog("批量更新商品删除状态")
	@PreAuthorize("@pms.hasPermission('s_product_product_batchUpdateDelStatus')")
	@Operation(summary = "【商家】批量更新商品删除状态")
	public R batchUpdateDelStatus(@RequestBody @Valid ProductBatchDelDTO productDTO) {
		return productService.batchUpdateDelStatus(productDTO, SecurityUtils.getShopUser().getShopId());
	}

	/**
	 * 批量更新库存
	 *
	 * @param ids   skuId列表
	 * @param stock 库存数（会覆盖原有的销售库存数量,实际库存会根据新旧数据的差值进行增减）
	 * @return
	 */
	@PutMapping("/batchUpdateStock/{stock}")
	@SystemLog("批量更新库存")
	@Parameters({
			@Parameter(name = "stock", description = "库存数", required = true),
			@Parameter(name = "ids", description = "skuId数组", required = true)
	})
	@Operation(summary = "【商家】批量更新库存")
	@PreAuthorize("@pms.hasPermission('s_product_product_batchUpdateStock')")
	public R batchUpdateStock(@RequestBody @NotNull(message = "skuId不能为空") List<Long> ids, @PathVariable @Min(value = 0, message = "库存不能小于0") Integer stock) {
		return productService.batchUpdateStock(ids, stock);
	}

	/**
	 * 矢量更新sku库存
	 *
	 * @param skuDTOList sku列表
	 * @return
	 */
	@PutMapping("/batchUpdateSkuStock")
	@SystemLog("矢量更新sku库存")
	@Parameters({
			@Parameter(name = "flag", description = "库存数", required = true),

	})
	@Operation(summary = "【商家】矢量更新sku库存")
	@PreAuthorize("@pms.hasPermission('s_product_product_batchUpdateSkuStock')")
	public R batchUpdateSkuStock(@Valid @RequestBody @NotNull(message = "skuId不能为空") ValidList<SkuUpdateSkuDTO> skuDTOList) {
		return productService.batchUpdateSku(skuDTOList);
	}

	/**
	 * 商品发布
	 */
	@PostMapping
	@SystemLog(type = SystemLog.LogType.SHOP, value = "商品发布")
	@PreAuthorize("@pms.hasPermission('s_product_product_add')")
	@Operation(summary = "【商家】商品发布")
	public R save(@Valid @RequestBody ProductDTO productDTO) {
		productDTO.setId(null);
		productDTO.setShopId(SecurityUtils.getShopUser().getShopId());
		productDTO.setShopUserId(SecurityUtils.getShopUser().getUserId());
		return productService.save(productDTO);
	}

	@PostMapping("/insert/batch/product")
	@Operation(summary = "【商家】商品批量发布")
	public R<Void> batchInsertProduct(@RequestParam("file") MultipartFile file) throws IOException {
		ShopUserDetail shopUser = SecurityUtils.getShopUser();
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(null);
		productDTO.setShopId(SecurityUtils.getShopUser().getShopId());
		productDTO.setShopUserId(SecurityUtils.getShopUser().getUserId());
		return productImportService.batchInsert(shopUser.getUsername(), file, productDTO);
	}

	@PostMapping("/batchInsertProductFailPage")
	@Operation(summary = "【商家】商品批量发布错误列表")
	public R<PageSupport<ProductImport>> batchInsertProductFailPage(@RequestBody ProductQuery query) {

		query.setShopId(SecurityUtils.getShopUser().getShopId());

		return productImportService.page(query);
	}

	@GetMapping("/getInsertProductFailPage/{importId}")
	@ExportExcel(name = "商品批量发布错误列表", sheet = "商品批量发布错误列表")
	@Operation(summary = "【商家】商品批量发布错误列表")
	public List<ProductImportErrorDetailDTO> getInsertProductFailPage(@PathVariable Long importId) {

		return productImportService.getInsertProductFailPage(importId);
	}

	@GetMapping("/template")
	@ExportExcel(name = "批量发布列表", sheet = "批量发布列表")
	@Operation(summary = "【商家】下载批量发布列表模板")
	public List<ImportDTO> template() {
		return this.productImportService.template();
	}

	/**
	 * 商品修改
	 */
	@SystemLog(type = SystemLog.LogType.SHOP, value = "商品修改")
	@PutMapping
	@Operation(summary = "【商家】商品修改")
	@PreAuthorize("@pms.hasPermission('s_product_product_update')")
	public R update(@Valid @RequestBody ProductDTO productDTO) {
		productDTO.setShopId(SecurityUtils.getShopUser().getShopId());
		productDTO.setShopUserId(SecurityUtils.getShopUser().getUserId());
		return draftProductService.update(productDTO);
	}

	@PostMapping("/updateProductList")
	@Operation(summary = "【商家】商品批量编辑")
	public R<Void> updateProductList(@RequestBody ProductVO productVO) {
		return productService.batchEditProduct(productVO.getProductDTOList());

	}

	@GetMapping("/activity/page")
	@Operation(summary = "【商家】商品营销活动分页查询（营销活动选择商品弹框）")
	public R<PageSupport<ProductBO>> activityPage(ProductQuery query) {
		Assert.notNull(query.getActivityBegTime(), "活动起始时间不能为空");
		if (ObjectUtil.isEmpty(query.getActivityEndTime())) {
			//24：00：00 时前端传null
			query.setActivityEndTime(DateUtil.endOfDay(query.getActivityBegTime()));
		}
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		query.setStatus(ProductStatusEnum.PROD_ONLINE.getValue());
		query.setOpStatus(OpStatusEnum.PASS.getValue());
		query.setActivityEndTime(DateUtil.offsetSecond(query.getActivityEndTime(), -1));
		query.setPayPctType(PreSellPayType.FULL_AMOUNT.value());
		return R.ok(productService.queryActivityInfoPage(query));
	}


	@GetMapping("/integral/page")
	@PreAuthorize("@pms.hasPermission('s_product_activity_product_queryIntegralInfoPage')")
	@Operation(summary = "【商家】商品列表（积分商品活动选择商品弹框）s_product_activity_product_queryIntegralInfoPage")
	public R<PageSupport<ProductBO>> queryIntegralInfoPage(ProductQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		query.setStatus(ProductStatusEnum.PROD_ONLINE.getValue());
		query.setOpStatus(OpStatusEnum.PASS.getValue());
		query.setPreSellFlag(false);
		return R.ok(productService.queryIntegralInfoPage(query));
	}

	@Operation(summary = "【商家】查询sku信息")
	@PostMapping("/queryBySkuIds")
	public R<List<SkuDTO>> queryBySkuIds(@RequestBody List<Long> skuIds) {
		return R.ok(skuService.queryBySkuIds(skuIds));
	}


	/**
	 * 分页查询
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/accusationPage")
	@Operation(summary = "【商家】商品违规分页查询")
	public R<PageSupport<ProductAccusationBO>> accusationPage(ProductQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(productService.accusationPage(query));
	}

	/**
	 * 库存预警分页查询
	 *
	 * @param query
	 * @return
	 */
	@GetMapping("/stocksArmPage")
	@PreAuthorize("@pms.hasPermission('s_product_product_stocksPage')")
	@Operation(summary = "【商家】首页库存预警分页查询")
	public R<PageSupport<SkuBO>> stocksArmPage(ProductQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(productService.stocksArmPage(query));
	}

	/**
	 * 商品阅览前需要设置token
	 *
	 * @param productId 商品Id
	 * @return
	 */
	@GetMapping("/preview")
	@Operation(summary = "【商家】商品预览")
	@Parameter(name = "productId", description = "商品id", required = true)
	public R<String> preview(@Valid @NonNull Long productId) {
		ShopUserDetail shopUser = SecurityUtils.getShopUser();
		String key = shopUser.getShopId() + ":" + productId;
		String token = productStatusChecker.setToken(key);
		return R.ok(key + ":" + token);
	}

}
