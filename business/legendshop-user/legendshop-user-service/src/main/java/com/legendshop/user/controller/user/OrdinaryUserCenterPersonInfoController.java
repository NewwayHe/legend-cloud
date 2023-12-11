/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.controller.user;

import com.legendshop.basic.api.WxApi;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.dto.BaseUserDetail;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.common.security.utils.UserTokenUtil;
import com.legendshop.user.bo.UserSecureBO;
import com.legendshop.user.dto.*;
import com.legendshop.user.service.BaseUserService;
import com.legendshop.user.service.OrdinaryUserService;
import com.legendshop.user.service.UserCenterPersonInfoService;
import com.legendshop.user.service.UserDetailService;
import com.legendshop.user.util.ValidatedMobileGroup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 个人资料控制器
 *
 * @author legendshop
 */
@Slf4j
@RestController
@RequestMapping(value = "/p/user/center/info", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "用户端个人资料")
public class OrdinaryUserCenterPersonInfoController {

	@Resource(name = "ordinaryUserCenterPersonInfoServiceImpl")
	private UserCenterPersonInfoService userCenterPersonInfoService;

	@Autowired
	private OrdinaryUserService ordinaryUserService;

	@Autowired
	private UserDetailService userDetailService;

	@Autowired
	private BaseUserService baseUserService;

	@Autowired
	private WxApi wxApi;

	@Autowired
	private UserTokenUtil userTokenUtil;


	@GetMapping()
	@Operation(summary = "【用户】获取用户信息")
	public R<MyPersonInfoDTO> getMyPersonInfo(HttpServletRequest request) {
		Long userId = SecurityUtils.getUserId();
		if (userId != null) {
			MyPersonInfoDTO info = userCenterPersonInfoService.getUserCenterPersonInfo(userId);
			return R.ok(info);
		} else {
			return R.ok(new MyPersonInfoDTO());
		}
	}

	@PostMapping(value = "/nick/name")
	@Operation(summary = "【用户】更改昵称")
	@Parameter(name = "nickName", description = "昵称", required = true)
	public R<Void> updateNickName(@RequestParam String nickName) {
		Assert.hasLength(nickName, "昵称不能为空！");
		//敏感字审核
		R<Void> result = wxApi.checkContent(nickName);
		if (!result.success()) {
			return result;
		}
		Long userId = SecurityUtils.getUserId();
		userCenterPersonInfoService.updateNickNameByUserId(userId, nickName);
		return R.ok();
	}

	@PostMapping("/sex")
	@Operation(summary = "【用户】修改性别")
	@Parameter(name = "sex", description = "性别", required = true)
	public R updateSex(@RequestParam String sex) {
		userCenterPersonInfoService.updateSexByUserId(SecurityUtils.getUserId(), sex);
		return R.ok();
	}

	@Operation(summary = "【用户】获取性别信息")
	@GetMapping("/secure")
	public R<UserSecureBO> getUserSecureByUserId() {
		Long userId = SecurityUtils.getUserId();
		UserSecureBO secureBO = userCenterPersonInfoService.getUserSecureByUserId(userId);
		return R.ok(secureBO);
	}

	@Operation(summary = "【用户】修改绑定手机号")
	@PostMapping("/updateMobilePhone")
	public R updateMobilePhone(@Validated({ValidatedMobileGroup.class}) @RequestBody VerifyOrdinaryUserDTO dto) {
		Long userId = SecurityUtils.getUserId();
		Assert.notNull(userId, "身份过期，请重新登录！");
		dto.setId(userId);
		dto.setUserType(UserTypeEnum.codeValue(SecurityUtils.getUserType()));
		return this.ordinaryUserService.updateMobilePhone(dto);
	}

	@Operation(summary = "【用户】修改头像")
	@Parameter(name = "avatar", description = "头像路径", required = true)
	@PostMapping("/updateAvatar")
	public R<Void> updateAvatar(@RequestParam String avatar) {
		Long userId = SecurityUtils.getUserId();
		Assert.notNull(userId, "身份过期，请重新登录！");
		return this.ordinaryUserService.updateAvatar(userId, avatar);
	}

	@Operation(summary = "【用户】修改支付密码")
	@PostMapping("/updatePayPassword")
	public R updatePayPassword(@RequestBody VerifyUserDetailDTO detailDTO) {
		BaseUserDetail baseUser = SecurityUtils.getBaseUser();
		Assert.notNull(baseUser, "身份过期，请重新登录！");
		String mobile = this.baseUserService.getMobile(new BaseUserQuery(baseUser.getUserId(), baseUser.getUserType()));
		detailDTO.setMobile(mobile);
		detailDTO.setUserId(baseUser.getUserId());
		return this.userDetailService.updatePayPassword(detailDTO);
	}

	@GetMapping(value = "/mobile/info")
	@SystemLog("【用户】获取移动端用户中心")
	@Operation(summary = "【用户】获取移动端用户中心:[ 商品、商家收藏，足迹，优惠卷 ]")
	public R<MobileUserCenterDTO> mobileInfo() {
		Long userId = SecurityUtils.getUserId();
		Assert.notNull(userId, "身份过期，请重新登录！");
		return this.userDetailService.mobileInfo(userId);
	}

	@PostMapping(value = "/update/weChatNumber")
	@Operation(summary = "【用户】修改微信号")
	@Parameter(name = "weChatNumber", description = "微信号", required = true)
	public R<Boolean> updateWeChatNumber(@RequestParam(value = "weChatNumber") String weChatNumber) {
		return R.ok(this.userDetailService.updateWeChatNumber(SecurityUtils.getUserId(), weChatNumber));
	}

	@PostMapping(value = "/update/email")
	@Operation(summary = "【用户】修改电子邮件")
	@Parameter(name = "email", description = "电子邮件", required = true)
	public R updateEmail(@Validated @Pattern(regexp = "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", message = "邮箱格式不正确~") @RequestParam(value = "email") String email) {
		return this.userDetailService.updateEmail(SecurityUtils.getUserId(), email);
	}
}
