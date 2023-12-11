/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.user;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.validator.group.Update;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.user.bo.UserInvoiceBO;
import com.legendshop.user.dto.UserInvoiceDTO;
import com.legendshop.user.dto.UserInvoiceDetailDTO;
import com.legendshop.user.query.UserInvoiceQuery;
import com.legendshop.user.service.UserInvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户发票管理相关接口
 *
 * @author legendshop
 */
@Tag(name = "用户发票")
@RestController
@RequestMapping(value = "/p/user/invoice", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserInvoiceController {


	@Autowired
	private UserInvoiceService userInvoiceService;


	@Operation(summary = "【用户】获取用户发票列表")
	@GetMapping("/page")
	public R<PageSupport<UserInvoiceDTO>> userInvoiceList(UserInvoiceQuery query) {
		Long userId = SecurityUtils.getUserId();
		query.setUserId(userId);
		PageSupport<UserInvoiceDTO> result = userInvoiceService.queryPage(query);
		return R.ok(result);
	}


	@Operation(summary = "【用户】新增保存用户发票")
	@PostMapping("/save")
	public R<Long> save(@Valid @RequestBody UserInvoiceDTO userInvoiceDto) {

		Long userId = SecurityUtils.getUserId();
		userInvoiceDto.setUserId(userId);
		return R.ok(userInvoiceService.saveInvoice(userInvoiceDto));
	}


	@Operation(summary = "【用户】编辑保存用户发票")
	@PutMapping
	public R<Integer> update(@Validated(Update.class) @RequestBody UserInvoiceDTO userInvoiceDto) {
		Long userId = SecurityUtils.getUserId();
		userInvoiceDto.setUserId(userId);
		if (userInvoiceService.updateInvoice(userInvoiceDto) > 0) {
			return R.ok();
		}
		return R.fail("更新失败");
	}


	@Operation(summary = "【用户】加载用户发票")
	@Parameter(name = "id", description = "id", required = true)
	@GetMapping
	public R<UserInvoiceDTO> get(@RequestParam Long id) {
		return R.ok(userInvoiceService.getById(id));
	}


	@Operation(summary = "【用户】删除发票")
	@Parameter(name = "id", description = "id", required = true)
	@DeleteMapping("/{id}")
	public R del(@PathVariable Long id) {
		return userInvoiceService.deleteById(id);
	}


	@Operation(summary = "【用户】设置为默认发票")
	@Parameter(name = "id", description = "id", required = true)
	@PostMapping("/set/default")
	public R<Integer> setDefault(@RequestParam Long id) {

		Long userId = SecurityUtils.getUserId();
		int result = userInvoiceService.updateDefaultInvoice(id, userId);
		if (result <= 0) {
			R.fail("设置失败");
		}
		return R.ok();
	}

	@Operation(summary = "【用户】获取用户订单发票列表")
	@GetMapping(value = "/queryUserInvoiceOrderById")
	public R<PageSupport<UserInvoiceBO>> queryUserInvoiceOrderById(UserInvoiceQuery query) {
		Long userId = SecurityUtils.getUserId();
		query.setUserId(userId);
		return R.ok(this.userInvoiceService.queryUserInvoiceOrderById(query));
	}

	@Operation(summary = "【用户】获取用户发票详情")
	@GetMapping(value = "/getDetail")
	public R<UserInvoiceDetailDTO> getDetail(@RequestParam(value = "id") Long id) {
		return userInvoiceService.getDetail(id);
	}
}
