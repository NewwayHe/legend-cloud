/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.dto.SysParamsDTO;
import com.legendshop.basic.enums.SysParamGroupEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.basic.enums.SysParamTypeEnum;
import com.legendshop.basic.query.SysParamPageQuery;
import com.legendshop.basic.query.SysParamQuery;
import com.legendshop.basic.service.SysParamsService;
import com.legendshop.basic.vo.EnumVO;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * (SysParams)表控制层
 *
 * @author legendshop
 * @since 2020-08-28 12:00:46
 */
@Tag(name = "主配置")
@RestController
@RequestMapping(value = "/admin/sys/params", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminSysParamsController {

	/**
	 * (SysParams)服务对象
	 */
	@Autowired
	private SysParamsService sysParamsService;

	@Operation(summary = "【后台】根据id查询主配置")
	@Parameter(name = "id", description = "id", required = true)
	@GetMapping("/{id}")
	public R<SysParamsDTO> getById(@PathVariable("id") Long id) {
		return R.ok(sysParamsService.getById(id));
	}


	@Operation(summary = "【后台】根据id删除主配置")
	@Parameter(name = "id", description = "id", required = true)
	@DeleteMapping("/{id}")
	public R deleteById(@PathVariable("id") Long id) {
		return R.ok(sysParamsService.deleteById(id));
	}


	@Operation(summary = "【后台】保存消息推送主配置")
	@PostMapping("/for/push")
	public R saveForPush(@Valid @RequestBody SysParamsDTO sysParamsDTO) {
		sysParamsDTO.setUpdateTime(new Date());
		sysParamsDTO.setGroupBy(SysParamGroupEnum.PUSH.name());
		sysParamsDTO.setType(SysParamTypeEnum.BUSINESS.value());
		sysParamsService.savePushSys(sysParamsDTO);
		return R.ok();
	}

	@PreAuthorize("@pms.hasPermission('admin_sys_params_page')")
	@Operation(summary = "【后台】查询主配置列表")
	@GetMapping("/page")
	public R<PageSupport<SysParamsDTO>> queryPage(SysParamQuery sysParamQuery) {
		PageSupport<SysParamsDTO> pageList = sysParamsService.queryPageList(sysParamQuery);
		return R.ok(pageList);
	}


	@Operation(summary = "【后台】根据名称查询主配置")
	@GetMapping("/name")
	public R<SysParamsDTO> getSysParamByName(@RequestParam SysParamNameEnum sysParamName) {
		SysParamsDTO name = sysParamsService.getByName(sysParamName.name());
		return R.ok(name);
	}


	@Operation(summary = "【后台】获取系统配置分组")
	@GetMapping("/group/name/list")
	public R<List<EnumVO>> getSysParamEnumGroupList() {
		List<EnumVO> voList = new ArrayList<>(SysParamGroupEnum.values().length);
		for (SysParamGroupEnum value : SysParamGroupEnum.values()) {
			EnumVO vo = new EnumVO(value.name(), null, value.getDesc());
			voList.add(vo);
		}
		return R.ok(voList);
	}


	@Operation(summary = "【后台】根据名称集合获取主配置集合")
	@GetMapping("/name/list")
	public R<List<SysParamsDTO>> getSysParamByNames(@RequestParam List<SysParamNameEnum> sysParamNameEnums) {
		List<SysParamsDTO> names = sysParamsService.getByNames(sysParamNameEnums);
		return R.ok(names);
	}


	@PreAuthorize("@pms.hasPermission('admin_sys_params_group_list')")
	@Operation(summary = "【后台】根据名称集合获取主配置集合")
	@GetMapping("/group/list")
	public R<PageSupport<SysParamsDTO>> getSysParamByGroup(SysParamPageQuery query) {
		return R.ok(this.sysParamsService.getByGroupPage(query));
	}

	@PreAuthorize("@pms.hasPermission('admin_sys_params_getItemList')")
	@Operation(summary = "【后台】根据主配置名称查询配置项")
	@GetMapping("/item/list")
	public R<List<SysParamItemDTO>> getItemList(@RequestParam SysParamNameEnum sysParamName) {
		List<SysParamItemDTO> list = sysParamsService.getSysParamItemsByParamName(sysParamName.name());
		return R.ok(list);
	}
}
