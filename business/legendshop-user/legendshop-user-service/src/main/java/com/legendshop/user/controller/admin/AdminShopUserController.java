/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.admin;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.api.MessageApi;
import com.legendshop.basic.api.WxApi;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.BasicBatchUpdateStatusDTO;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.shop.api.ShopDetailApi;
import com.legendshop.shop.dto.ShopDetailDTO;
import com.legendshop.user.bo.ShopUserDetailBO;
import com.legendshop.user.dto.ShopUserDTO;
import com.legendshop.user.query.ShopUserQuery;
import com.legendshop.user.service.ShopUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 后台商家管理控制器
 *
 * @author legendshop
 */
@Tag(name = "商家用户管理")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin/shop/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminShopUserController {

	final ShopUserService shopUserService;
	final MessageApi messageApi;
	final ShopDetailApi shopDetailApi;
	final WxApi wxApi;


	@GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('admin_user_shopUser_page')")
	@Operation(summary = "【后台】分页查询商家用户列表")
	public R<PageSupport<ShopUserDTO>> page(ShopUserQuery shopUserQuery) {
		return R.ok(shopUserService.page(shopUserQuery));
	}


	@GetMapping("/{shopUserId}")
	@PreAuthorize("@pms.hasPermission('admin_shop_user_get')")
	@Operation(summary = "【后台】查询商家用户资料")
	@Parameter(name = "shopUserId", description = "商家用户Id", required = true)
	public R<ShopUserDetailBO> getShopUserDetail(@PathVariable Long shopUserId) {
		return this.shopUserService.getShopUserDetail(shopUserId);
	}


	@PutMapping("/updateLoginPassword")
	@PreAuthorize("@pms.hasPermission('admin_shop_user_updateLoginPassword')")
	@Operation(summary = "【后台】更新商家用户登录密码")
	@Parameter(name = "id", description = "用户Id")
	public R<Void> updateLoginPassword(@RequestBody ShopUserDTO shopUserDTO) {
		return R.process(shopUserService.updateLoginPassword(shopUserDTO), "更新用户密码失败");
	}

	@PutMapping(value = "/updateStatus/{id}")
	@PreAuthorize("@pms.hasPermission('admin_shop_user_updateStatus')")
	@Operation(summary = "【后台】修改在线状态")
	@Parameter(name = "id", description = "用户Id")
	public R<Void> updateStatus(@PathVariable Long id) {
		return R.process(this.shopUserService.updateStatus(id), "更新用户状态失败！");
	}


	@PostMapping(value = "/sendMessage")
	@PreAuthorize("@pms.hasPermission('admin_shop_user_sendMessage')")
	@Operation(summary = "【后台】发送商家用户站内信")
	@Parameters({
			@Parameter(name = "shopUserId", description = "商家用户Id"),
			@Parameter(name = "content", description = "站内信内容")
	})
	public R<Void> sendMessage(@RequestParam Long shopUserId, @RequestParam String content) {

		//敏感字审核
		R<Void> result = wxApi.checkContent(content);
		if (!result.success()) {
			return result;
		}

		R<ShopDetailDTO> shopDetailResult = shopDetailApi.getByUserId(shopUserId);
		if (!shopDetailResult.success()) {
			throw new BusinessException("调用店铺信息服务接口失败");
		}
		ShopDetailDTO shopDetailDTO = shopDetailResult.getData();
		if (ObjectUtil.isEmpty(shopDetailDTO)) {
			throw new BusinessException("该用户未申请入驻商家");
		}
		Long adminUserId = SecurityUtils.getAdminUser().getUserId();
		return messageApi.sendToShop(adminUserId, shopDetailDTO.getId(), content);
	}

	/**
	 * 批量更新状态
	 *
	 * @param basicBatchUpdateStatusDTO
	 * @return
	 */
	@PutMapping("/batch/update/status")
	@SystemLog("批量更新状态")
	@PreAuthorize("@pms.hasPermission('admin_shop_user_batch_update_status')")
	@Operation(summary = "【后台】批量更新状态")
	public R<Void> batchUpdateStatus(@RequestBody @Valid BasicBatchUpdateStatusDTO basicBatchUpdateStatusDTO) {
		return R.process(shopUserService.batchUpdateStatus(basicBatchUpdateStatusDTO), "批量更新失败");
	}
}
