/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.data.dto.BaiduViewDTO;
import com.legendshop.data.query.BaiduViewQuery;
import com.legendshop.data.service.BaiduViewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 百度统计
 *
 * @author legendshop
 * @create: 2021-06-19 17:28
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/baidu/view", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "数据服务")
public class AdminBaiduViewController {

	@Autowired
	private BaiduViewService baiduViewService;

	@GetMapping("/flow/pic")
	@Operation(summary = "流量统计折线图", description = "")
	public R<List<BaiduViewDTO>> getFlowPic(BaiduViewQuery query) {
		List<BaiduViewDTO> businessData = baiduViewService.getFlowPic(query);
		query.setOrder("asc");
		query.setProp("archive_time");
		return R.ok(businessData);
	}

	@GetMapping("/flow/page")
	@Operation(summary = "流量统计分页", description = "")
	public R<PageSupport<BaiduViewDTO>> getFlowPage(BaiduViewQuery query) {
		PageSupport<BaiduViewDTO> businessData = baiduViewService.getFlowPage(query);
		return R.ok(businessData);
	}

	@GetMapping("/flow/excel")
	@ExportExcel(name = "访问统计", sheet = "访问统计")
	@Operation(summary = "流量统计导出", description = "")
	public List<BaiduViewDTO> getFlowExcel(BaiduViewQuery query) {
		return baiduViewService.getFlowPic(query);
	}
}
