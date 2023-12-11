/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.legendshop.basic.bo.MsgSecCheckBO;
import com.legendshop.basic.dto.MsgSendParamDTO;
import com.legendshop.basic.dto.WeChatParamDTO;
import com.legendshop.basic.dto.WechatSceneDTO;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.basic.properties.CommonProperties;
import com.legendshop.basic.service.SensWordService;
import com.legendshop.basic.service.SysParamsService;
import com.legendshop.basic.service.WechatSceneService;
import com.legendshop.basic.service.WxService;
import com.legendshop.common.core.config.sys.params.WxConfig;
import com.legendshop.common.core.config.sys.params.WxMiniConfig;
import com.legendshop.common.core.config.sys.params.WxMpConfig;
import com.legendshop.common.core.config.sys.params.WxPayConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.StringConstant;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.common.data.cache.util.StringRedisUtil;
import com.legendshop.common.wechat.WechatSDKClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.legendshop.common.core.constant.CacheConstants.WXMINI_ACCESS_TOKEN;

/**
 * @author legendshop
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WxServiceImpl implements WxService {

	private final CommonProperties commonProperties;

	private final StringRedisUtil redisUtil;

	private final SysParamsService sysParamsService;

	private final WechatSceneService wechatSceneService;

	private final StringRedisUtil stringRedisUtil;

	private final SensWordService sensWordService;

	@Override
	public R<WxJsapiSignature> createJsapiSignature(String url) {
		try {
			return R.ok(WechatSDKClient.commonWxService().createJsapiSignature(url));
		} catch (WxErrorException e) {
			e.printStackTrace();
			return R.fail("获取失败！");
		}
	}

	@Override
	@SneakyThrows
	public String getMiniToken() {
		return WechatSDKClient.commonWxService().getAccessToken();
	}

	@Override
	public String getPushApiToken() {

		String token = redisUtil.getString("wx_ms_push_token");
		if (StrUtil.isNotBlank(token)) {
			return token;
		}
		// 获取推送接口的token
		WxMpConfig mpConfig = getMpConfig();
		Map<String, Object> paramMap = new HashMap(16);
		String url = mpConfig.getWxPushTokenUrl();
		paramMap.put("appid", mpConfig.getAppId());
		paramMap.put("secret", mpConfig.getSecret());
		paramMap.put("grant_type", "client_credential");
		log.info("请求微信公众号接口token,{}", paramMap);
		String s = HttpUtil.get(url, paramMap);
		JSONObject jsonObject = JSONUtil.parseObj(s);
		log.info("请求结果，{}", jsonObject);
		Integer errcode = (Integer) jsonObject.get("errcode");
		if (null != errcode && 0 != errcode) {
			log.error("获取微信公众号接口token失败,失败信息：{}", jsonObject.getStr("errmsg"));
			return null;
		}
		token = (String) jsonObject.get("access_token");
		Integer value = (Integer) jsonObject.get("expires_in");
		Long expires = Long.valueOf(value);
		redisUtil.setString("wx_ms_push_token", token, expires - 200);
		log.info("token存入redis,key:wx_ms_push_token,value : {}", token);
		return token;
	}

	@Override
	public WxMpConfig getMpConfig() {
		return sysParamsService.getConfigDtoByParamName(SysParamNameEnum.WX_MP.name(), WxMpConfig.class);
	}


	@Override
	public WxConfig getWebConfig() {
		return sysParamsService.getConfigDtoByParamName(SysParamNameEnum.WX_WEB.name(), WxConfig.class);
	}

	@Override
	public WxMiniConfig getMiniConfig() {
		return sysParamsService.getNotCacheConfigByName(SysParamNameEnum.WX_MINI_PRO.name(), WxMiniConfig.class);
	}

	@Override
	public WxConfig getAppConfig() {
		return sysParamsService.getConfigDtoByParamName(SysParamNameEnum.WX_APP.name(), WxConfig.class);
	}

	@Override
	public WxPayConfig getPayConfig() {
		return sysParamsService.getConfigDtoByParamName(SysParamNameEnum.WX_PAY.name(), WxPayConfig.class);
	}

	@Override
	@SneakyThrows
	public String getMiniQrCode(String scene, String page, Boolean flag) {
		if (!flag) {
			return getUnlimit(scene, page);
		}
		WechatSceneDTO wechatSceneDTO = wechatSceneService.getBySceneAndPage(scene, page);
		String md5 = DigestUtil.md5Hex(scene + StringConstant.UNDERLINE + page);
		String base64 = "";
		if (ObjectUtil.isNotNull(wechatSceneDTO) && md5.equals(wechatSceneDTO.getMd5())) {
			base64 = getUnlimit(md5, page);
			return base64;
		}
		wechatSceneDTO = new WechatSceneDTO();
		wechatSceneDTO.setScene(scene);
		wechatSceneDTO.setPage(page);
		wechatSceneDTO.setMd5(md5);
		base64 = getUnlimit(md5, page);
		wechatSceneService.save(wechatSceneDTO);
		log.info(">>>> 获取小程序码成功");
		return base64;
	}

	@SneakyThrows
	private String getUnlimit(String scene, String page) {
		WxMaQrcodeService qrcodeService = WechatSDKClient.commonWxService().getQrcodeService();
		File file = qrcodeService.createWxaCodeUnlimit(scene, page);
		BufferedInputStream inputStream = FileUtil.getInputStream(file);
		String base64 = Base64.encode(inputStream);
		return "data:image/jpeg;base64," + base64;
	}

	@Override
	public void sendMsg(WeChatParamDTO weChatParamDTO) {
		WxMpConfig mpConfig = getMpConfig();
		String pushApiToken = getPushApiToken();
		if (StrUtil.isBlank(pushApiToken)) {
			log.error("发送微信公众号推送失败，token为空~");
			return;
		}
		String url = mpConfig.getWxPushSendUrl() + "?access_token=" + pushApiToken;
		List<MsgSendParamDTO> dataDTOList = weChatParamDTO.getDataDTOList();
		JSONObject sendData = new JSONObject();
		sendData.set("template_id", weChatParamDTO.getTemplateId());
		JSONObject data = new JSONObject();
		for (MsgSendParamDTO d : dataDTOList) {
			JSONObject jsonObject = JSONUtil.createObj();
			jsonObject.set("value", d.getValue());
			jsonObject.set("color", d.getColor());
			data.set(d.getMsgSendParam().value(), jsonObject);
		}
		sendData.set("data", data);

		String jumpUrl = weChatParamDTO.getUrl();
		List<MsgSendParamDTO> urlParamList = weChatParamDTO.getUrlParamList();
		if (StrUtil.isNotBlank(jumpUrl) && CollUtil.isNotEmpty(urlParamList)) {
			UrlBuilder urlBuilder = UrlBuilder.ofHttp(jumpUrl, StandardCharsets.UTF_8);
			for (MsgSendParamDTO msgSendParamDTO : urlParamList) {
				urlBuilder.addQuery(msgSendParamDTO.getMsgSendParam().value(), msgSendParamDTO.getValue());
			}
			jumpUrl = urlBuilder.build();
		}
		sendData.set("url", jumpUrl);
		List<String> tousers = weChatParamDTO.getTouser();
		tousers.forEach(t -> {
			sendData.set("touser", t);
			log.info("微信消息推送url {}", url);
			log.info("请求体 {}", sendData);
			String body = HttpRequest.post(url).header(Header.CONTENT_TYPE, "application/json").body(JSONUtil.toJsonStr(sendData)).execute().body();
			log.info("请求返回内容{}", body);
			JSONObject jsonObject = JSONUtil.parseObj(body);
			Integer errcode = (Integer) jsonObject.get("errcode");
			if (0 != errcode) {
				if (ObjectUtil.isNotNull(jsonObject.get("errmsg"))) {
					log.error("微信公众号推送消息失败,失败信息：{}", jsonObject.getStr("errmsg"));
				}
//					throw new BusinessException(jsonObject.getStr("errmsg"));
			}
		});
	}

	@Override
	public String getMiniAccessToken() {
		String accessToken = stringRedisUtil.getString(WXMINI_ACCESS_TOKEN);
		if (StringUtils.isNotBlank(accessToken)) {
			return accessToken;
		}
		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + this.getMiniConfig().getAppId() + "&secret=" + this.getMiniConfig().getSecret();
		String body = HttpRequest.get(url).execute().body();
		JSONObject jsonObject = JSONUtil.parseObj(body);
		accessToken = (String) jsonObject.get("access_token");
		Integer expiresIn = (Integer) jsonObject.get("expires_in");
		if (StrUtil.isBlank(accessToken)) {
			throw new BusinessException((String) jsonObject.get("errmsg"));
		}
		stringRedisUtil.setString(WXMINI_ACCESS_TOKEN, accessToken, 90L, TimeUnit.MINUTES);
		return accessToken;
	}

	@Override
	public String refreshMiniAccessToken() {
		this.stringRedisUtil.delKey(WXMINI_ACCESS_TOKEN);
		return this.getMiniAccessToken();
	}

	@Override
	public R<Void> checkContent(String content) {
		Boolean wxSecCheck = this.getMiniConfig().getWxSecCheck();
		// 如果没有启用微信敏感词过滤，则直接返回成功
		if (null != wxSecCheck && !wxSecCheck) {
			return R.ok();
		}
		//获取access_token
		String accessToken = this.getMiniAccessToken();
		MsgSecCheckBO msgSecCheckBO = checkContent(content, accessToken);
		//accessToken过期，再查一次accessToken
		if (40001 == msgSecCheckBO.getErrCode()) {
			accessToken = this.refreshMiniAccessToken();
			msgSecCheckBO = checkContent(content, accessToken);
		}
		if (40001 == msgSecCheckBO.getErrCode()) {
			return R.fail("内容审核失败，请联系系统管理员！");
		}

		//内容违规，发布失败
		if (87014 == msgSecCheckBO.getErrCode()) {
			//errCode=87014
			return R.fail("内容含有敏感字");
		}

		return R.ok();
	}

	private MsgSecCheckBO checkContent(String content, String accessToken) {
		//请求微信小程序敏感字审核
		String url = "https://api.weixin.qq.com/wxa/msg_sec_check?access_token=" + accessToken;
		JSONObject sendData = new JSONObject();
		sendData.set("content", content);
		String result = HttpRequest.post(url).header(Header.CONTENT_TYPE, "application/json").body(JSONUtil.toJsonStr(sendData)).execute().body();
		JSONObject jsonObject = JSONUtil.parseObj(result);
		MsgSecCheckBO msgSecCheckBO = new MsgSecCheckBO((Integer) jsonObject.get("errcode"), (String) jsonObject.get("errmsg"));
		log.info("文字审核结果：" + msgSecCheckBO.getErrCode() + ":" + msgSecCheckBO.getErrMsg());
		return msgSecCheckBO;
	}

	@Override
	public R<Void> imgSecCheck(MultipartFile file) throws IOException {
		WxMiniConfig miniConfig = this.getMiniConfig();
		Boolean wxSecCheck = miniConfig.getWxSecCheck();
		// 如果没有启用微信敏感词过滤，则直接返回成功
		if (null != wxSecCheck && !wxSecCheck) {
			return R.ok();
		}

		String wxImageSuffix = miniConfig.getWxImageSuffix();
		if (StrUtil.isNotBlank(wxImageSuffix)) {
			List<String> suffixList = JSONUtil.toList(JSONUtil.parseArray(wxImageSuffix), String.class);
			// 如果不在敏感图片校验内的文件上传时，则直接返回成功
			if (suffixList != null && suffixList.stream().noneMatch(s -> file.getOriginalFilename().toLowerCase().endsWith(s))) {
				return R.ok();
			}
		}

		//获取access_token
		String accessToken = this.getMiniAccessToken();

		MsgSecCheckBO msgSecCheckBO = imgSecCheck(file, accessToken);

		//accessToken过期，再查一次accessToken
		if (40001 == msgSecCheckBO.getErrCode()) {
			accessToken = this.refreshMiniAccessToken();
			msgSecCheckBO = imgSecCheck(file, accessToken);
		}
		if (40001 == msgSecCheckBO.getErrCode()) {
			return R.fail("内容审核失败，请联系系统管理员！");
		}
		if (41005 == msgSecCheckBO.getErrCode()) {
			return R.fail("没传成功");
		}
		//内容违规，发布失败
		if (87014 == msgSecCheckBO.getErrCode()) {
			//errCode=87014
			return R.fail("图片违规");
		}
		return R.ok();
	}

	private MsgSecCheckBO imgSecCheck(MultipartFile file, String accessToken) throws IOException {
		//请求微信小程序敏感图片审核
		String url = "https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + accessToken;
		//MultipartFile转化为byte[]
		InputStream inputStream = file.getInputStream();
		byte[] byt = new byte[inputStream.available()];
		inputStream.read(byt);

		String result = HttpRequest.post(url).form("media", byt, file.getOriginalFilename()).execute().body();
		JSONObject jsonObject = JSONUtil.parseObj(result);
		MsgSecCheckBO msgSecCheckBO = new MsgSecCheckBO((Integer) jsonObject.get("errcode"), (String) jsonObject.get("errmsg"));
		log.info("图片审核结果：" + msgSecCheckBO.getErrCode() + ":" + msgSecCheckBO.getErrMsg());
		return msgSecCheckBO;
	}

}
