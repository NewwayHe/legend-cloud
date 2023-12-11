/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.UserTokenUtil;
import com.legendshop.shop.dto.AdvertiseCountDTO;
import com.legendshop.shop.dto.AdvertiseDTO;
import com.legendshop.shop.dto.AdvertiseStausCountDTO;
import com.legendshop.shop.excel.AdvertiseCountExportDTO;
import com.legendshop.shop.query.AdvertiseQuery;
import com.legendshop.shop.service.AdvertiseCountService;
import com.legendshop.shop.service.AdvertiseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * (Advertise)表控制层
 *
 * @author legendshop
 * @since 2022-04-27 15:23:37
 */
@AllArgsConstructor
@RestController
public class AdvertiseController {

	/**
	 * (Advertise)服务对象
	 */
	@Autowired
	private AdvertiseService advertiseService;
	@Autowired
	private AdvertiseCountService advertiseCountService;
	@Autowired
	private final UserTokenUtil userTokenUtil;

	/**
	 * 根据id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/advertise/{id}")
	@Operation(summary = "【商家】根据id查询")
	public R<AdvertiseDTO> getById(@PathVariable("id") Long id) {
		return R.ok(advertiseService.getById(id));
	}

	/**
	 * 查询弹窗广告
	 *
	 * @param
	 * @return
	 */
	@GetMapping("/pop/query/queryPop")
	@Operation(summary = "用户端查询弹窗广告")
	public R<List<AdvertiseDTO>> queryAdvertise(AdvertiseQuery query, HttpServletRequest request) {
		query.setTime(DateUtil.date());
		//H5 APP MINI MP
		String source = request.getHeader(RequestHeaderConstant.SOURCE_KEY);
		if (StrUtil.isEmpty(source)) {
			source = VisitSourceEnum.APP.name();
		}


		if ("H5".equals(source)
				|| "APP".equals(source)
				|| "MP".equals(source)
				|| "MINI".equals(source)) {
			query.setSourceList(Collections.singletonList("APP"));
		} else {
			query.setSourceList(Collections.singletonList("PC"));
		}
		return R.ok(advertiseService.queryAdvertise(query));
	}

	/**
	 * 根据id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/advertise/{id}")
	@SystemLog("根据id删除")
	@Operation(summary = "【商家】根据id删除")
	public R deleteById(@PathVariable("id") Long id) {
		return R.ok(advertiseService.deleteById(id));
	}

	/**
	 * 保存
	 *
	 * @param advertiseDTO
	 * @return
	 */
	@PostMapping("/advertise")
	@SystemLog("保存")
	@Operation(summary = "【商家】保存")
	public R save(@Valid @RequestBody AdvertiseDTO advertiseDTO) {
		if (ObjectUtil.isNotEmpty(advertiseDTO.getEveryDayCount())) {
			advertiseDTO.setCount(advertiseDTO.getEveryDayCount());
		}

		advertiseDTO.setCreateTime(DateUtil.date());
		return R.ok(advertiseService.saveAdvertise(advertiseDTO));
	}

	/**
	 * 更新
	 *
	 * @param advertiseDTO
	 * @return
	 */
	@PostMapping("/advertise/updateAdvertise")
	@SystemLog("更新")
	@Operation(summary = "【后台】更新")
	public R update(@Valid @RequestBody AdvertiseDTO advertiseDTO) {
		return R.ok(advertiseService.update(advertiseDTO));
	}


	/**
	 * 更新广告投放数据
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/advertise/advertise/put")
	@Operation(summary = "更新广告投放数据")
	public R updatePutAdvertise(Long id, HttpServletRequest request) {


		AdvertiseDTO byId = advertiseService.getById(id);
		if (ObjectUtil.isNotEmpty(byId)) {
			AdvertiseCountDTO advertiseCountDTO = new AdvertiseCountDTO();
			advertiseCountDTO.setUserKey(request.getHeader(RequestHeaderConstant.USER_KEY));
			advertiseCountDTO.setSource(request.getHeader(RequestHeaderConstant.SOURCE_KEY));
			if (ObjectUtil.isEmpty(advertiseCountDTO.getSource())) {
				throw new BusinessException("没有请求来源！");
			}
			if ("H5".equals(advertiseCountDTO.getSource())
					|| "APP".equals(advertiseCountDTO.getSource())
					|| "MP".equals(advertiseCountDTO.getSource())
					|| "MINI".equals(advertiseCountDTO.getSource())) {
				advertiseCountDTO.setSource("APP");
			} else {
				advertiseCountDTO.setSource("PC");
			}

			advertiseCountDTO.setUserId(userTokenUtil.getUserId(request));
			advertiseCountDTO.setAdvertiseId(byId.getId());
			DateTime time = DateUtil.offsetDay(new Date(), 0);
			advertiseCountDTO.setCreateTime(time);
			advertiseService.updatePut(advertiseCountDTO);

		}
		return R.ok();
	}

	/**
	 * 查询所有弹窗广告
	 *
	 * @param
	 * @return
	 */
	@GetMapping("/advertise/advertise/advertisePage")
	@Operation(summary = "平台端广告列表")
	public R<PageSupport<AdvertiseDTO>> queryAdvertisePage(AdvertiseQuery query) {
		return R.ok(this.advertiseService.queryAdvertisePage(query));
	}

	/**
	 * 查询所有弹窗广告数量
	 *
	 * @param
	 * @return
	 */
	@GetMapping("/advertise/advertise/advertiseCount")
	@Operation(summary = "平台端广告数量统计")
	public R<List<AdvertiseStausCountDTO>> queryadvertiseCount(AdvertiseQuery query) {
		return R.ok(this.advertiseService.queryadvertiseCount(query));
	}

	/**
	 * 查询弹窗广告
	 *
	 * @param
	 * @return
	 */
	@GetMapping("/advertise/advertise/queryAdvertiseDataReport")
	@Operation(summary = "平台端广告报表列表")
	public R<PageSupport<AdvertiseCountDTO>> queryAdvertiseDataReport(AdvertiseQuery query) {
		return R.ok(this.advertiseService.queryAdvertiseDataReport(query));
	}

	/**
	 * 广告投放数据报表
	 *
	 * @param
	 * @return
	 */
	@GetMapping("/advertise/advertise/advertiseDataReport")
	@Operation(summary = "广告投放数据报表")
	public R<List<AdvertiseCountDTO>> advertiseDataReport(AdvertiseQuery query) {
		return R.ok(advertiseCountService.queryAdvertiseCountReport(query));
	}

	/**
	 * 更新广告投放数据
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/advertise/advertise/clock")
	@Operation(summary = "更新广告点击数据")
	public R updateClockAdvertise(Long id, HttpServletRequest request) {
		AdvertiseDTO byId = advertiseService.getById(id);

		if (ObjectUtil.isNotEmpty(byId)) {
			AdvertiseCountDTO advertiseCountDTO = new AdvertiseCountDTO();
			advertiseCountDTO.setSource(request.getHeader(RequestHeaderConstant.SOURCE_KEY));
			if ("H5".equals(advertiseCountDTO.getSource())
					|| "APP".equals(advertiseCountDTO.getSource())
					|| "MP".equals(advertiseCountDTO.getSource())
					|| "MINI".equals(advertiseCountDTO.getSource())) {
				advertiseCountDTO.setSource("APP");
			} else {
				advertiseCountDTO.setSource("PC");
			}
			if (ObjectUtil.isEmpty(advertiseCountDTO.getSource())) {
				throw new BusinessException("没有请求来源！");
			}
			advertiseCountDTO.setUserId(userTokenUtil.getUserId(request));
			DateTime time = DateUtil.offsetDay(new Date(), 0);
			advertiseCountDTO.setCreateTime(time);
			advertiseCountDTO.setAdvertiseId(byId.getId());
			advertiseService.updateClock(advertiseCountDTO);
		}
		return R.ok();
	}

	@Operation(summary = "【后台】导出Excel广告数据统计")
	@GetMapping("/advertise/advertise/excel/advertiseDataReportExcel")
	@ExportExcel(name = "广告数据统计", sheet = "广告数据统计")
	public List<AdvertiseCountExportDTO> advertiseDataReportExcel(AdvertiseQuery query) {
		return advertiseCountService.queryAdvertiseCountReportExport(query);
	}
}
