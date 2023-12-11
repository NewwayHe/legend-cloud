/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.user;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.UserTokenUtil;
import com.legendshop.shop.dto.PubDTO;
import com.legendshop.shop.enums.PubTypeEnum;
import com.legendshop.shop.enums.ReceiverEnum;
import com.legendshop.shop.query.PubQuery;
import com.legendshop.shop.service.PubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户公告控制器
 *
 * @author legendshop
 */
@Tag(name = "商城公告")
@RestController
public class UserPubController {


	@Autowired
	private PubService pubService;

	@Autowired
	private UserTokenUtil userTokenUtil;


	@Operation(summary = "[用户]获取商城公告详情")
	@Parameter(name = "pubId", description = "公告ID", required = true)
	@GetMapping("/p/pub/info")
	public R<PubDTO> pubInfo(@RequestParam Long pubId, HttpServletRequest request) {
		return pubService.getPubById(pubId, userTokenUtil.getUserId(request), ReceiverEnum.USER);
	}


	@Operation(summary = "[用户]获取买家公告列表")
	@Parameter(name = "curPageNO", description = "当前页码", required = true)
	@GetMapping("/p/query/user/pub/list")
	public R<PageSupport<PubDTO>> queryUserPubList(PubQuery pubQuery, HttpServletRequest request) {
		Long userId = userTokenUtil.getUserId(request);
		pubQuery.setType(PubTypeEnum.PUB_BUYERS.value());
		pubQuery.setUserId(userId);
		pubQuery.setReceiverType(ReceiverEnum.USER.getValue());
		PageSupport<PubDTO> result = pubService.queryPubPageListByType(pubQuery);
		return R.ok(result);
	}

	@Deprecated
	@Operation(summary = "[用户]获取最新一条买家公告")
	@GetMapping("/p/get/newest/user/pub")
	public R<PubDTO> getNewestUserPub() {
		return R.ok(pubService.getNewestPubByType(PubTypeEnum.PUB_BUYERS.value()));
	}

}
