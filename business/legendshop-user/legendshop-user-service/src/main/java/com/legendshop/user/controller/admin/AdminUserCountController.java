/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.data.api.BaiDuCountApi;
import com.legendshop.data.dto.DataUserAmountDTO;
import com.legendshop.data.dto.UserDataViewsDTO;
import com.legendshop.user.bo.RegisterCountBO;
import com.legendshop.user.dto.*;
import com.legendshop.user.query.*;
import com.legendshop.user.service.UserCountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "用户数据统计")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin/count", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUserCountController {

	@Autowired
	private final UserCountService userCountService;

	@Autowired
	private BaiDuCountApi baiDuCountApi;

	@Operation(summary = "【后台】获取新增用户数据")
	@Parameter(name = "source", description = "来源", required = true)
	@GetMapping("/amount/new")
	public R<UserDataNewDTO> getNewUserData(String source) {
		return R.ok(userCountService.getNewUserData(source));
	}

	@Operation(summary = "【后台】获取累计用户数据")
	@GetMapping("/amount/total")
	public R<UserDataAllDTO> getTotalUserData(String source) {
		return R.ok(userCountService.getTotalUserData(source));
	}

	@Operation(summary = "【后台】获取用户浏览数据")
	@GetMapping("/amount/view")
	public R<UserDataViewsDTO> getUserViewData(String source) {
		if (source == null) {
			source = "";
		}
		return baiDuCountApi.getPvUv(source);
	}

	@Operation(summary = "【后台】获取消费用户数据")
	@GetMapping("/amount/consumer")
	public R<UserDataConsumerDTO> getConsumerData(String source) {
		return R.ok(userCountService.getConsumerData(source));
	}

	@Operation(summary = "【后台】获取地区分布统计用户数据")
	@GetMapping("/area/data")
	public R<List<UserDataAreaDTO>> getDistributedArea() {
		return R.ok(userCountService.getDistributedArea());
	}

	@Operation(summary = "【后台】获取地区分布统计用户数据分页")
	@GetMapping("/area/data/page")
	public R<PageSupport<UserDataAreaDTO>> getDistributedAreaPage(UserDataAreaQuery userDataAreaQuery) {
		userDataAreaQuery.setPageSize(5);
		return R.ok(userCountService.getDistributedAreaPage(userDataAreaQuery));
	}

	@Operation(summary = "【后台】获取地区分布统计用户数据导出")
	@GetMapping("/area/data/excel")
	@ExportExcel(name = "地区分布统计", sheet = "地区分布统计")
	public List<UserDataAreaDTO> getDistributedAreaExcel(UserDataAreaQuery userDataAreaQuery) {
		return userCountService.getDistributedAreaExcel(userDataAreaQuery);
	}

	@Operation(summary = "【后台】获取等级分布统计用户数据")
	@GetMapping("/grade/data")
	public R<List<UserDataGradeDTO>> getDistributedGrade() {
		return R.ok(userCountService.getDistributedGrade());
	}

	@Operation(summary = "【后台】获取用户购买力排行数据")
	@GetMapping("/purchasing/data")
	public R<List<UserDataPurchasingPowerDTO>> getPurchasingPower() {
		return R.ok(userCountService.getPurchasingPower());
	}

	@Operation(summary = "【后台】获取店铺销售排行数据")
	@GetMapping("/shop/data")
	public R<List<UserDataShopSaleDTO>> getShopSaleData() {
		return R.ok(userCountService.getShopSaleData());
	}

	@Operation(summary = "【后台】获取用戶数量统计折线图数据")
	@GetMapping("/amount/line")
	public R<List<DataUserAmountDTO>> getUserAmountLine(UserCountAmountQuery amountQuery) {
		return R.ok(userCountService.getUserAmountLine(amountQuery));
	}

	@Operation(summary = "【后台】获取用戶数量统计分页数据")
	@GetMapping("/amount/page")
	public R<PageSupport<DataUserAmountDTO>> getUserAmountPage(UserCountAmountQuery amountQuery) {
		return R.ok(userCountService.getUserAmountPage(amountQuery));
	}

	@Operation(summary = "【后台】导出excel用戶数量统计分页数据")
	@GetMapping("/amount/page/excel")
	@ExportExcel(name = "用戶数量统计", sheet = "用戶数量统计")
	public List<DataUserAmountDTO> getUserAmountPageExcel(UserCountAmountQuery amountQuery) {
		return userCountService.getUserAmountPageExcel(amountQuery);
	}

	@Operation(summary = "【后台】获取用戶购买力排行柱状图数据")
	@GetMapping("/purchasing/pic")
	public R<List<UserDataPurchasingPageDTO>> getUserPurchasingPic(UserPurchasingQuery purchasingQuery) {
		return R.ok(userCountService.getPurchasingPic(purchasingQuery));
	}

	@Operation(summary = "【后台】获取用戶购买力排行分页数据")
	@GetMapping("/purchasing/page")
	public R<PageSupport<UserDataPurchasingPageDTO>> getUserPurchasingPage(UserPurchasingQuery purchasingQuery) {
		return R.ok(userCountService.getPurchasingPage(purchasingQuery));
	}

	@Operation(summary = "【后台】导出Excel用戶购买力排行分页数据")
	@GetMapping("/purchasing/page/excel")
	@ExportExcel(name = "用戶购买力排行", sheet = "用戶购买力排行")
	public List<UserDataPurchasingPageDTO> getUserPurchasingPagePageExcel(UserPurchasingQuery purchasingQuery) {
		return userCountService.getPurchasingPageExcel(purchasingQuery);
	}

	@Operation(summary = "【后台】获取用戶登录历史数据")
	@GetMapping("/login/page")
	public R<PageSupport<LoginHistoryDTO>> getLoginHistory(UserCountLoginQuery loginQuery) {
		return R.ok(userCountService.getLoginHistoryPage(loginQuery));
	}

	@Operation(summary = "【后台】获取用戶登录历史统计数据")
	@GetMapping("/login/count")
	public R<PageSupport<LoginHistoryCountDTO>> getLoginHistoryCount(UserCountLoginQuery loginQuery) {
		return R.ok(userCountService.getLoginHistoryCountPage(loginQuery));
	}

	@Operation(summary = "【后台】获取短信发送历史统计数据")
	@GetMapping("/sms/count")
	public R<PageSupport<UserDataSmsDTO>> getSmsHistory(UserCountSmsQuery smsQuery) {
		return R.ok(userCountService.getSmsHistory(smsQuery));
	}

	@Operation(summary = "【后台】获取首页用户信息（注册用户数量和入驻商家数量）")
	@GetMapping("/register")
	public R<RegisterCountBO> getRegisterCount() {
		return R.ok(userCountService.getRegisterCount());
	}

	@Operation(summary = "【后台】获取店铺销售排行分页统计数据")
	@GetMapping("/shop/sale/page")
	public R<PageSupport<UserDataShopSalePageDTO>> getShopSalePage(UserCountShopSaleQuery saleQuery) {
		return R.ok(userCountService.getShopSalePage(saleQuery));
	}

	@Operation(summary = "【后台】获取店铺销售排行柱状图统计数据")
	@GetMapping("/shop/sale/pic")
	public R<List<UserDataShopSalePageDTO>> getShopSalePic(UserCountShopSaleQuery saleQuery) {
		return R.ok(userCountService.getShopSalePic(saleQuery));
	}

	@Operation(summary = "【后台】获取店铺销售排行分页Excel统计数据")
	@GetMapping("/shop/sale/page/excel")
	@ExportExcel(name = "店铺销售排行", sheet = "店铺销售排行")
	public List<UserDataShopSalePageDTO> getShopSalePageExcel(UserCountShopSaleQuery saleQuery) {
		return userCountService.getShopSalePageExcel(saleQuery);
	}
}
