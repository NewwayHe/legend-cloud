/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.api.VitLogApi;
import com.legendshop.product.dto.VitLogDTO;
import com.legendshop.product.query.VitLogQuery;
import com.legendshop.user.bo.UserBasicInformationBO;
import com.legendshop.user.dto.CustomerBillDTO;
import com.legendshop.user.dto.UserInformationDTO;
import com.legendshop.user.query.CustomerBillQuery;
import com.legendshop.user.service.CustomerBillService;
import com.legendshop.user.service.UserDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "普通用户管理")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/admin/user/detail", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUserDetailController {

	private final VitLogApi vitLogApi;
	private final UserDetailService userDetailService;
	private final CustomerBillService customerBillService;


	@GetMapping("/{userId}")
	@PreAuthorize("@pms.hasPermission('admin_user_userDetail_get')")
	@Operation(summary = "【后台】查询用户详情")
	@Parameter(name = "userId", description = "用户Id", required = true)
	public R<UserBasicInformationBO> queryUserDetail(@PathVariable Long userId) {
		return this.userDetailService.getUserBasicInformationBOByUserId(userId);
	}


	/**
	 * 会员账单
	 */
	@GetMapping("/customerBill")
	@PreAuthorize("@pms.hasPermission('admin_user_detail_customerBill')")
	@Operation(summary = "【后台】查询用户账单记录")
	public R<PageSupport<CustomerBillDTO>> customerBillPage(CustomerBillQuery query) {
		if (null == query.getOwnerId()) {
			return R.fail("查询用户不能为空");
		}
		return R.ok(this.customerBillService.queryPage(query));
	}

	/**
	 * 会员浏览历史
	 */
	@GetMapping("/visitLogPage")
	@PreAuthorize("@pms.hasPermission('admin_user_userDetail_visitLogPage')")
	@Operation(summary = "【后台】查询用户浏览历史")
	public R<PageSupport<VitLogDTO>> visitLogPage(VitLogQuery vitLogQuery) {
		if (null == vitLogQuery.getUserId()) {
			return R.fail("查询用户不能为空");
		}
		return vitLogApi.queryVitListPage(vitLogQuery);
	}


	/**
	 * 根据用户id 获取 昵称 手机号
	 */
	@GetMapping("/getPhoneAndNickNameByIds")
	@Operation(summary = "【后台】根据用户id 获取 昵称 手机号")
	public R<List<UserInformationDTO>> getPhoneAndNickNameByIds(@RequestParam(value = "ids", required = true) List<Long> ids) {
		return userDetailService.getPhoneAndNickNameByIds(ids);
	}


}
