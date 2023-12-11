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
import com.legendshop.user.dto.UserContactDTO;
import com.legendshop.user.query.UserContactQuery;
import com.legendshop.user.service.UserContactService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户提货信息管理相关接口
 *
 * @author legendshop
 */
@Tag(name = "用户提货信息")
@RestController
@RequestMapping("/p/contact")
@RequiredArgsConstructor
public class UserContactController {

	private final UserContactService userContactService;


	@ApiOperation(value = "【用户】获取提货信息列表", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/page")
	public R<PageSupport<UserContactDTO>> page(UserContactQuery query) {
		Long userId = SecurityUtils.getUserId();
		if (ObjectUtil.isNull(userId)) {
			return R.fail("用户id不能为空，请重新登录");
		}
		query.setUserId(userId);
		PageSupport<UserContactDTO> result = userContactService.queryPage(query);
		return R.ok(result);
	}


	@ApiOperation(value = "【用户】新增提货信息", produces = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping
	public R<Long> save(@Valid @RequestBody UserContactDTO dto) {
		Long userId = SecurityUtils.getUserId();
		if (!checkMaxNumber().getSuccess()) {
			return R.fail("保存提货信息最多为20个");
		}
		dto.setUserId(userId);
		return R.ok(userContactService.saveContact(dto));
	}


	@ApiOperation(value = "【用户】编辑提货信息", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping
	public R<Integer> update(@Validated(Update.class) @RequestBody UserContactDTO dto) {
		Long userId = SecurityUtils.getUserId();
		dto.setUserId(userId);
		if (userContactService.updateContact(dto) > 0) {
			return R.ok();
		}
		return R.fail("更新失败");
	}


	@ApiOperation(value = "【用户】删除提货信息", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(paramType = "path", name = "id", value = "id", dataType = "Long", required = true)
	@DeleteMapping("/{id}")
	public R del(@PathVariable Long id) {
		Long userId = SecurityUtils.getUserId();
		UserContactDTO dto = userContactService.getById(id);
		if (ObjectUtil.isNotEmpty(dto)) {
			if (ObjectUtil.isNotEmpty(userId) && userId.equals(dto.getUserId())) {
				if (userContactService.deleteUserAddress(userId, id)) {
					return R.ok();
				}
			}
		}
		return R.fail("删除失败");
	}


	@ApiOperation(value = "【用户】设置默认提货信息", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam(paramType = "query", name = "id", value = "id", dataType = "Long", required = true)
	@PostMapping("/set/default")
	public R<Integer> setDefault(@RequestParam Long id) {
		Long userId = SecurityUtils.getUserId();
		UserContactDTO dto = userContactService.getById(id);
		if (ObjectUtil.isNotEmpty(dto) && userId.equals(dto.getUserId())) {
			if (userContactService.updateDefaultUserContact(id, userId) > 0) {
				return R.ok();
			}
		}
		return R.fail("操作失败");
	}


	@ApiOperation(value = "【用户】检查最大提货信息数", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/checkMaxNumber")
	public R checkMaxNumber() {
		Long userId = SecurityUtils.getUserId();
		Long maxNumber = userContactService.getUserContactCount(userId);
		if (maxNumber >= 20) {
			return R.fail("提货信息已达上限，请删除不常用的提货信息");
		} else {
			return R.ok();
		}
	}
}
