/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import com.legendshop.basic.dto.WeChatParamDTO;
import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.config.sys.params.WxMiniConfig;
import com.legendshop.common.core.config.sys.params.WxMpConfig;
import com.legendshop.common.core.config.sys.params.WxPayConfig;
import com.legendshop.common.core.constant.R;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author legendshop
 */
public interface WxService {

	R<WxJsapiSignature> createJsapiSignature(String url);

	/**
	 * 获取推送接口的token
	 *
	 * @return
	 */
	String getPushApiToken();

	String getMiniToken();

	WxMpConfig getMpConfig();

	WxPayConfig getPayConfig();

	WxConfig getWebConfig();

	WxMiniConfig getMiniConfig();

	WxConfig getAppConfig();


	/**
	 * 公众号推送消息
	 *
	 * @param weChatParamDTO
	 */
	void sendMsg(WeChatParamDTO weChatParamDTO);

	/**
	 * 获得带参的小程序二维码
	 *
	 * @param scene 需要小程序附带的参数
	 * @return
	 */
	String getMiniQrCode(String scene, String page, Boolean flag);

	/**
	 * 获得微信小程序access_token
	 *
	 * @return accessToken
	 */
	String getMiniAccessToken();

	String refreshMiniAccessToken();

	/**
	 * 微信小程序(内容安全)检查一段文本是否含有违法违规内容(敏感字审核)
	 *
	 * @param content 审核内容文字
	 * @return
	 */
	R<Void> checkContent(String content);

	/**
	 * 微信小程序(内容安全)校验一张图片是否含有违法违规内容(敏感图审核)
	 *
	 * @param file 审核图片
	 * @return
	 */
	R<Void> imgSecCheck(MultipartFile file) throws IOException;

}
