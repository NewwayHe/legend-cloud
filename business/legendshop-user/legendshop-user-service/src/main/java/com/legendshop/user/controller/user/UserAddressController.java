/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.user;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.validator.group.Update;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.user.bo.UserAddressBO;
import com.legendshop.user.dto.UserAddressDTO;
import com.legendshop.user.query.UserAddressQuery;
import com.legendshop.user.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户地址管理相关接口
 *
 * @author legendshop
 */
@Tag(name = "用户收货地址")
@RestController
@RequestMapping(value = "/p/address", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserAddressController {

	@Autowired
	private UserAddressService userAddressService;

	private final HttpServletRequest request;

	/**
	 * 获取收货地址列表.
	 */
	@Operation(summary = "【用户】获取收货地址列表")
	@GetMapping("/page")
	public R<PageSupport<UserAddressDTO>> myList(UserAddressQuery query) {
		Long userId = SecurityUtils.getUserId();
		if (ObjectUtil.isNull(userId)) {
			return R.fail("用户id不能为空，请重新登录");
		}
		query.setUserId(userId);
		PageSupport<UserAddressDTO> result = userAddressService.queryPage(query);
		return R.ok(result);
	}

	/**
	 * 新增收货地址.
	 *
	 * @param address the address
	 */
	@Operation(summary = "【用户】新增收货地址")
	@PostMapping
	public R<Long> save(@Valid @RequestBody UserAddressDTO address) {
		Long userId = SecurityUtils.getUserId();
		if (!checkMaxNumber().getSuccess()) {
			return R.fail("保存地址最多为20个");
		}
		address.setUserId(userId);
		return R.ok(userAddressService.saveAddress(address));
	}

	/**
	 * 编辑收货地址.
	 *
	 * @param address the address
	 */
	@Operation(summary = "【用户】编辑收货地址")
	@PutMapping
	public R<Integer> update(@Validated(Update.class) @RequestBody UserAddressDTO address) {
		Long userId = SecurityUtils.getUserId();
		address.setUserId(userId);
		if (userAddressService.updateAddress(address) > 0) {
			return R.ok();
		}
		return R.fail("更新失败");
	}


	/**
	 * 加载收货地址
	 *
	 * @param id the addr id
	 * @return the result dto< UserAddressDTO dto>
	 */
	@Operation(summary = "【用户】加载收货地址")
	@Parameter(name = "id", description = "id", required = true)
	@GetMapping
	public R<UserAddressBO> get(@RequestParam Long id) {
		return R.ok(userAddressService.getAddressInfo(id));
	}


	/**
	 * 删除地址.
	 *
	 * @param id the addr id
	 * @return the result dto< string>
	 */
	@Operation(summary = "【用户】删除地址")
	@Parameter(name = "id", description = "id", required = true)
	@DeleteMapping("/{id}")
	public R del(@PathVariable Long id) {
		Long userId = SecurityUtils.getUserId();
		UserAddressDTO userAddress = userAddressService.getById(id);
		if (ObjectUtil.isNotEmpty(userAddress)) {
			if (ObjectUtil.isNotEmpty(userId) && userId.equals(userAddress.getUserId())) {
				if (userAddressService.deleteUserAddress(userId, id)) {
					return R.ok();
				}
			}
		}
		return R.fail("删除失败");
	}


	/**
	 * 设置默认收货地址.
	 *
	 * @param id the addr id
	 * @return the result dto< string>
	 */
	@Operation(summary = "【用户】设置默认收货地址")
	@Parameter(name = "id", description = "id", required = true)
	@PostMapping("/set/default")
	public R<Integer> setDefault(@RequestParam Long id) {
		Long userId = SecurityUtils.getUserId();
		UserAddressDTO userAddress = userAddressService.getById(id);
		if (ObjectUtil.isNotEmpty(userAddress) && userId.equals(userAddress.getUserId())) {
			if (userAddressService.updateDefaultUserAddress(id, userId) > 0) {
				return R.ok();
			}
		}
		return R.fail("操作失败");
	}


	/**
	 * 检查最大地址数
	 *
	 * @param
	 * @return true, if check max number
	 */
	@Operation(summary = "【用户】检查最大地址数")
	@GetMapping("/checkMaxNumber")
	public R checkMaxNumber() {
		Long userId = SecurityUtils.getUserId();
		Long MaxNumber = userAddressService.getUserAddressCount(userId);
		if (MaxNumber >= 20) {
			return R.fail("收货地址已达上限，请删除不常用的地址");
		} else {
			return R.ok();
		}
	}
}
