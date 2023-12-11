/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.user;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.dto.VitLogDTO;
import com.legendshop.product.enums.VitLogPageEnum;
import com.legendshop.product.query.VitLogQuery;
import com.legendshop.product.service.VitLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/p/vit/log", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "用户端浏览历史")
public class UserVitLogController {


	@Autowired
	private VitLogService vitLogService;

	/**
	 * 获取用户浏览历史
	 *
	 * @param vitLogQuery
	 * @return
	 */
	@Operation(summary = "【用户】获取用户浏览历史")
	@GetMapping("/page")
	public R<PageSupport<VitLogDTO>> queryVitListPage(VitLogQuery vitLogQuery) {
		Long userId = SecurityUtils.getUser().getUserId();
		vitLogQuery.setUserId(userId);
		vitLogQuery.setPage(VitLogPageEnum.PRODUCT_PAGE.value());
		vitLogQuery.setUserDelFlag(false);
		PageSupport<VitLogDTO> pageSupport = this.vitLogService.queryVitListPageByUser(vitLogQuery);
		return R.ok(pageSupport);
	}

}
