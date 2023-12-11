/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller;

import cn.hutool.core.collection.CollUtil;
import com.legendshop.basic.properties.SensWordProperties;
import com.legendshop.basic.service.SensWordService;
import com.legendshop.basic.service.WechatSceneService;
import com.legendshop.basic.service.WxService;
import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.config.sys.params.WxMiniConfig;
import com.legendshop.common.core.config.sys.params.WxMpConfig;
import com.legendshop.common.core.config.sys.params.WxPayConfig;
import com.legendshop.common.core.constant.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author legendshop
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/wx")
public class WxController {

	final WxService wxService;
	final WechatSceneService wechatSceneService;
	final SensWordProperties sensWordProperties;
	final SensWordService sensWordService;

	@GetMapping(value = "/getWebConfig")
	public R<WxConfig> getWebConfig() {
		return R.ok(this.wxService.getWebConfig());
	}

	@GetMapping(value = "/getMpConfig")
	public R<WxMpConfig> getMpConfig() {
		return R.ok(this.wxService.getMpConfig());
	}

	@GetMapping(value = "/getAppConfig")
	public R<WxConfig> getAppConfig() {
		return R.ok(this.wxService.getAppConfig());
	}

	@GetMapping(value = "/getPayConfig")
	public R<WxPayConfig> getPayConfig() {
		return R.ok(this.wxService.getPayConfig());
	}

	@GetMapping(value = "/getMiniConfig")
	public R<WxMiniConfig> getMiniConfig() {
		return R.ok(this.wxService.getMiniConfig());
	}

	@GetMapping(value = "/getMiniQrCode")
	public R<String> getMiniCode(@RequestParam(value = "scene") String scene, @RequestParam(value = "page") String page, @RequestParam(defaultValue = "false") Boolean flag) {
		return R.ok(this.wxService.getMiniQrCode(scene, page, flag));
	}


	@GetMapping(value = "/getJsapiSignature")
	public R<WxJsapiSignature> getJsapiSignature(@RequestParam(value = "url") String url) {
		return this.wxService.createJsapiSignature(url);
	}

	@GetMapping(value = "weChatConfigPush")
	public R<Void> weChatConfigPush(@RequestParam String msg) {
		return R.ok();
	}

	@GetMapping(value = "/getWechatScene")
	public R<String> getWechatScene(@RequestParam(value = "md5") String md5) {
		return R.ok(this.wechatSceneService.getWechatScene(md5));
	}


	@PostMapping(value = "/checkContent")
	public R<Void> checkContent(@RequestBody String content) {
		//调用微信敏感词验证
		R<Void> wxResult = this.wxService.checkContent(content);

		//如果微信通过，进行默认敏感词验证
		if (wxResult.getSuccess()) {
			if (null == sensWordProperties.getEnable() || sensWordProperties.getEnable()) {
				log.info("进行默认敏感词验证!");
				//进行默认敏感词验证
				Set<String> findWords = sensWordService.checkSensitiveWords(content);
				if (CollUtil.isNotEmpty(findWords) && findWords.size() > 0) {
					return R.fail(-1, "您回复的内容含有敏感词 " + findWords + ", 请更正再提交！");
				}
			}
		}
		return wxResult;
	}
}
