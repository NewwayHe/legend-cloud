/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.shop.dto.PubDTO;
import com.legendshop.shop.enums.ReceiverEnum;
import com.legendshop.shop.query.PubQuery;
import com.legendshop.shop.service.PubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 后台公告控制器
 *
 * @author legendshop
 */
@Tag(name = "商城公告")
@RestController
@RequestMapping(value = "/admin/pub", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminPubController {


	@Autowired
	private PubService pubService;

	@PreAuthorize("@pms.hasPermission('admin_pub_page')")
	@Operation(summary = "[后台]获取商家公告分页列表")
	@GetMapping("/page")
	public R<PageSupport<PubDTO>> query(PubQuery pubQuery) {
		PageSupport<PubDTO> ps = pubService.getPubListPage(pubQuery);
		return R.ok(ps);
	}


	@PreAuthorize("@pms.hasPermission('admin_news_update')")
	@Operation(summary = "[后台]更新公告")
	@PutMapping
	public R update(@Valid @RequestBody PubDTO pubDTO) {

		Long userId = SecurityUtils.getAdminUser().getUserId();
		String username = SecurityUtils.getAdminUser().getUsername();
		pubDTO.setAdminUserId(userId);
		pubDTO.setAdminUserName(username);
		pubDTO.setUpdateTime(new Date());
		int update = pubService.update(pubDTO);
		if (update <= 0) {
			return R.fail();
		}
		return R.ok();
	}


	@PreAuthorize("@pms.hasPermission('admin_news_updateStatus')")
	@Operation(summary = "[后台]公告上线/下线")
	@Parameters({
			@Parameter(name = "id", description = "公告ID", required = true),
			@Parameter(name = "status", description = "状态", required = true)
	})
	@PostMapping("/updateStatus")
	public R updateStatus(Long id, Integer status) {
		return pubService.updateStatus(id, status);
	}


	@PreAuthorize("@pms.hasPermission('admin_news_add')")
	@Operation(summary = "[后台]保存公告")
	@PostMapping
	public R save(@Valid @RequestBody PubDTO pubDTO) {
		Long userId = SecurityUtils.getAdminUser().getUserId();
		String username = SecurityUtils.getAdminUser().getUsername();
		pubDTO.setAdminUserId(userId);
		pubDTO.setAdminUserName(username);
		pubDTO.setCreateTime(new Date());
		Long result = pubService.save(pubDTO);
		if (result <= 0) {
			return R.fail();
		}
		return R.ok();
	}


	@PreAuthorize("@pms.hasPermission('admin_news_delete')")
	@Operation(summary = "[后台]删除公告")
	@Parameter(name = "id", description = "公告ID", required = true)
	@DeleteMapping("/{id}")
	public R delete(@PathVariable Long id) {
		int result = pubService.deletePub(id);
		if (result <= 0) {
			return R.fail();
		}
		return R.ok();
	}


	@PreAuthorize("@pms.hasPermission('admin_news_get')")
	@Operation(summary = "[后台]获取公告详情")
	@Parameter(name = "id", description = "公告ID", required = true)
	@GetMapping("/{id}")
	public R<PubDTO> get(@PathVariable Long id) {
		return pubService.getPubById(id, SecurityUtils.getAdminUser().getUserId(), ReceiverEnum.PLATFORM);
	}

}
