/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.captcha.endpoint;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.legendshop.common.core.constant.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码默认的controller
 *
 * @author legendshop
 */
@RestController
@RequestMapping("/captcha")
@AllArgsConstructor
public class CaptchaEndpoint {
	private final CaptchaService captchaService;

	@PostMapping("/get")
	public R<Object> get(@RequestBody CaptchaVO captchaVO) {
		ResponseModel responseModel = captchaService.get(captchaVO);
		return R.ok(responseModel.getRepData());
	}

	@PostMapping("/check")
	public R check(@RequestBody CaptchaVO captchaVO) {
		ResponseModel check = captchaService.check(captchaVO);
		if (check.isSuccess()) {
			return R.ok(check.getRepData());
		} else {
			return R.fail("验证码不正确");
		}
	}

	@PostMapping("/verify")
	public R verify(@RequestBody CaptchaVO captchaVO) {
		ResponseModel verification = captchaService.verification(captchaVO);
		if (verification.isSuccess()) {
			return R.ok(verification.getRepData());
		} else {
			return R.fail("二次校验验证码失败");
		}
	}
}
