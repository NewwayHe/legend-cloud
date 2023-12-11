/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.config.sys.params.WxMiniConfig;
import com.legendshop.common.core.config.sys.params.WxMpConfig;
import com.legendshop.common.core.config.sys.params.WxPayConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 微信配置服务
 *
 * @author legendshop
 */
@FeignClient(contextId = "wxApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface WxApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	@GetMapping(value = PREFIX + "/wx/getWebConfig")
	R<WxConfig> getWebConfig();

	@GetMapping(value = PREFIX + "/wx/getMiniConfig")
	R<WxMiniConfig> getMiniConfig();

	@GetMapping(value = PREFIX + "/wx/getAppConfig")
	R<WxConfig> getAppConfig();

	@GetMapping(value = PREFIX + "/wx/getPayConfig")
	R<WxPayConfig> getPayConfig();

	@GetMapping(value = PREFIX + "/wx/getMpConfig")
	R<WxMpConfig> getMpConfig();

	/**
	 * 根据微信审核敏感字
	 *
	 * @param content 审核内容
	 * @return
	 */
	@PostMapping(value = PREFIX + "/wx/checkContent")
	R<Void> checkContent(@RequestBody String content);
}
