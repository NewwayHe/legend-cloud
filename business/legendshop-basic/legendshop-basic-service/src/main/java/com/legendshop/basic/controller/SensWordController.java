/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.SensWordDTO;
import com.legendshop.basic.query.SensWordQuery;
import com.legendshop.basic.service.SensWordService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 敏感字过滤表(SensWord)表控制层
 *
 * @author legendshop
 * @since 2021-12-22 14:41:11
 */
@Tag(name = "敏感字过滤表管理")
@RestController
@RequestMapping(value = "/sensWord", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class SensWordController {

	/**
	 * 敏感字过滤表(SensWord)服务对象
	 */
	private final SensWordService sensWordService;


	@GetMapping("/page")
	@Operation(summary = "【后台】分页查询敏感字过滤表")
	public R<PageSupport<SensWordDTO>> getRolePage(SensWordQuery sensWordQuery) {
		return R.ok(sensWordService.querySensWordPage(sensWordQuery));
	}

	/**
	 * 保存敏感字过滤表
	 *
	 * @param sensWordDTO
	 * @return
	 */
	@PostMapping
	@SystemLog("保存敏感字过滤表")
	@Operation(summary = "【后台】保存敏感字过滤表")
	public R save(@Valid @RequestBody SensWordDTO sensWordDTO) {
		String username = SecurityUtils.getAdminUser().getUsername();
		sensWordDTO.setCreator(username);
		String bySensWord = sensWordService.getBySensWord(sensWordDTO);
		if (ObjectUtil.isNotNull(bySensWord)) {
			return R.fail("当前敏感字已经存在！");
		}
		return R.ok(sensWordService.saveSensWord(sensWordDTO));
	}


	/**
	 * 更新敏感字过滤表
	 *
	 * @param sensWordDTO
	 * @return
	 */
	@PutMapping
	@SystemLog("更新敏感字过滤表")
	@Operation(summary = "【后台】更新敏感字过滤表")
	public R update(@Valid @RequestBody SensWordDTO sensWordDTO) {
		String bySensWord = sensWordService.getBySensWord(sensWordDTO);
		if (ObjectUtil.isNotNull(bySensWord)) {
			return R.fail("当前敏感字已经存在！");
		}
		return R.ok(sensWordService.updateSensWord(sensWordDTO));
	}

	/**
	 * 根据id查询敏感字过滤表
	 *
	 * @param id
	 * @return
	 */
	@GetMapping
	@Operation(summary = "【后台】根据id查询敏感字过滤表")
	public R<SensWordDTO> getById(@PathVariable("id") Long id) {
		return R.ok(sensWordService.getById(id));
	}

	/**
	 * 根据id删除敏感字过滤表
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@SystemLog("根据id删除敏感字过滤表")
	@Operation(summary = "【后台】根据id删除敏感字过滤表")
	public R deleteById(@PathVariable Long id) {
		return R.ok(sensWordService.deleteById(id));
	}

}
