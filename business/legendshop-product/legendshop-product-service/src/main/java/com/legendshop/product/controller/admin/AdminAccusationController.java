/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.admin;

import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.dto.AdminUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.AccusationBO;
import com.legendshop.product.dto.AccusationDTO;
import com.legendshop.product.dto.AccusationUserTypeDTO;
import com.legendshop.product.dto.AccusationbatchHandleDTO;
import com.legendshop.product.enums.AccusationUserTypeEnum;
import com.legendshop.product.query.AccusationReportQuery;
import com.legendshop.product.service.AccusationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 商品举报管理
 *
 * @author legendshop
 */
@Tag(name = "商品举报管理")
@RestController
@RequestMapping(value = "/admin/accusation", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminAccusationController {

	@Autowired
	private AccusationService accusationService;

	/**
	 * 平台保存违规信息下架商品
	 *
	 * @param accusationDTO
	 * @return
	 */
	@PostMapping
	@SystemLog("平台保存违规信息下架商品")
	@PreAuthorize("@pms.hasPermission('admin_product_accusation_add')")
	@Operation(summary = "【后台】平台保存违规信息下架商品")
	public R save(@Valid @RequestBody AccusationDTO accusationDTO) {
		AdminUserDetail adminUserDetail = SecurityUtils.getAdminUser();
		AccusationUserTypeDTO userTypeDTO = new AccusationUserTypeDTO();
		userTypeDTO.setUserId(adminUserDetail.getUserId());
		userTypeDTO.setUserName(adminUserDetail.getUsername());
		userTypeDTO.setUserType(AccusationUserTypeEnum.ADMIN.value());

		return accusationService.saveAccusationAndOffLine(accusationDTO, userTypeDTO);
	}

	/**
	 * 更新
	 *
	 * @param d
	 * @return
	 */
	@PutMapping
	@SystemLog("更新商品举报")
	@PreAuthorize("@pms.hasPermission('admin_product_accusation_update')")
	@Operation(summary = "【后台】更新商品举报")
	public R<Boolean> update(@Valid @RequestBody AccusationDTO d) {
		if (StrUtil.isBlank(d.getTypeName())) {
			return R.fail("举报类型名称不能为空");
		}
		return R.ok(accusationService.update(d));
	}


	/**
	 * 查询举报详情
	 */
	@GetMapping("/{id}")
	@Parameter(name = "id", description = "举报ID", required = true)
	@PreAuthorize("@pms.hasPermission('admin_product_accusation_get')")
	@Operation(summary = "【后台】查询举报详情")
	public R<AccusationBO> getById(@PathVariable Long id) {
		return R.ok(accusationService.getAccusation(id));
	}

	/**
	 * 根据id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@Parameter(name = "id", description = "举报ID", required = true)
	@SystemLog("删除商品举报")
	@PreAuthorize("@pms.hasPermission('admin_product_accusation_del')")
	@Operation(summary = "【后台】根据id删除举报")
	public R<Boolean> deleteById(@PathVariable Long id) {
		return R.ok(accusationService.deleteById(id));
	}

	/**
	 * 批量处理举报
	 *
	 * @param accusationDTO
	 * @return
	 */
	@PutMapping("/batchHandle")
	@SystemLog("批量处理举报")
	@Operation(summary = "【后台】批量处理举报")
	@PreAuthorize("@pms.hasPermission('admin_product_accusation_batchHandle')")
	public R batchHandle(@RequestBody @Valid AccusationbatchHandleDTO accusationDTO) {
		accusationDTO.setHandler(SecurityUtils.getUserName());
		return accusationService.batchHandle(accusationDTO);
	}

	@GetMapping("/page")
	@Operation(summary = "【后台】商品举报分页查询")
	@PreAuthorize("@pms.hasPermission('admin_product_accusation_page')")
	public R<PageSupport<AccusationBO>> page(AccusationReportQuery query) {
		return R.ok(accusationService.queryAccusation(query));
	}

}
