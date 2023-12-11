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
import com.legendshop.basic.dto.SocialDetailsDTO;
import com.legendshop.basic.query.SocialDetailsQuery;
import com.legendshop.basic.service.SocialDetailsService;
import com.legendshop.common.core.constant.R;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台配置用户社交登录
 *
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/admin/social/detail", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminSocialDetailsController {

	private final SocialDetailsService socialDetailsService;

	/**
	 * 分页查询登录配置
	 *
	 * @param socialDetailsQuery
	 * @return
	 */
	@GetMapping("/page")
	public R<PageSupport<SocialDetailsDTO>> page(SocialDetailsQuery socialDetailsQuery) {
		return R.ok(socialDetailsService.page(socialDetailsQuery));
	}
}
