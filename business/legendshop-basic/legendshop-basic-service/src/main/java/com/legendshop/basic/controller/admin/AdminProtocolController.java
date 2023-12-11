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
import com.legendshop.basic.dto.AdminProtocolDTO;
import com.legendshop.basic.dto.ProtocolDTO;
import com.legendshop.basic.query.ProtocolQuery;
import com.legendshop.basic.service.ProtocolService;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统协议管理控制器.
 *
 * @author legendshop
 */
@Tag(name = "协议管理")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin/protocol")
public class AdminProtocolController {


	private final ProtocolService protocolService;


	@PreAuthorize("@pms.hasPermission('admin_protocol_query')")
	@Operation(summary = "【后台】获取协议列表")
	@GetMapping("/query")
	public R<PageSupport<ProtocolDTO>> query(ProtocolQuery protocolQuery) {
		PageSupport<ProtocolDTO> ps = protocolService.queryPageList(protocolQuery);
		return R.ok(ps);
	}


	@PreAuthorize("@pms.hasPermission('admin_protocol_update_url')")
	@Operation(summary = "【后台】更新协议链接")
	@PostMapping(value = "/update/url")
	public R<Void> updateUrl(AdminProtocolDTO adminProtocolDTO) {
		return protocolService.updateUrl(adminProtocolDTO);
	}

	@GetMapping("/queryById")
	@Operation(summary = "【后台】查看协议链接")
	public R<ProtocolDTO> queryById(Long id) {
		return protocolService.getById(id);
	}


}
