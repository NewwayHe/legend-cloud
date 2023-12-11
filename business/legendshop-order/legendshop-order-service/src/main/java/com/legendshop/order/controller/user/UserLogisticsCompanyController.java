/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.controller.user;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.order.dto.LogisticsCompanyDTO;
import com.legendshop.order.service.LogisticsCompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 物流公司(LsLogisticsCompany)表控制层
 *
 * @author legendshop
 * @since 2022-04-25 16:15:12
 */
@RestController
@RequestMapping(value = "/p/logistics/company", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "物流公司管理")
public class UserLogisticsCompanyController {

	@Resource
	private LogisticsCompanyService logisticsCompanyService;

	@Operation(summary = "【商家】获取全部物流公司列表", description = "")
	@GetMapping("/all/list")
	public R<List<LogisticsCompanyDTO>> queryAll() {
		List<LogisticsCompanyDTO> list = logisticsCompanyService.queryAll(SecurityUtils.getShopUser().getShopId());
		return R.ok(list);
	}
}

