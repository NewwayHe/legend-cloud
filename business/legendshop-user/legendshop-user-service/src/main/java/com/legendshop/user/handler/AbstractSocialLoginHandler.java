/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.handler;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.legendshop.basic.api.WxApi;
import com.legendshop.basic.enums.ThirdPartyAuthTypeEnum;
import com.legendshop.basic.properties.CommonProperties;
import com.legendshop.common.core.config.sys.params.WxMpConfig;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.SecurityConstants;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.common.core.properties.EnvironmentProperties;
import com.legendshop.common.security.enums.LegendshopSecurityErrorEnum;
import com.legendshop.user.bo.PassportBO;
import com.legendshop.user.dto.*;
import com.legendshop.user.service.OrdinaryUserService;
import com.legendshop.user.service.PassportService;
import com.legendshop.user.service.ShopUserService;
import com.legendshop.user.service.SocialLoginHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.legendshop.user.utils.VerifyCodeUtil.TEMP_CERTIFICATE_KEY;

/**
 * 抽象类社交登录
 *
 * @author legendshop
 */
@Slf4j
public abstract class AbstractSocialLoginHandler implements SocialLoginHandler {

	@Autowired
	protected ObjectMapper objectMapper;


	/**
	 * 授权请求来源，用于子类覆写
	 */
	@Setter
	private String source;

	@Autowired
	protected WxApi wxApi;

	@Autowired
	protected ShopUserService shopUserService;

	@Autowired
	protected PassportService passportService;

	@Autowired
	protected StringRedisTemplate redisTemplate;

	@Autowired
	protected OrdinaryUserService ordinaryUserService;

	@Autowired
	protected CommonProperties commonProperties;

	@Autowired
	protected EnvironmentProperties environmentProperties;

	@Override
	public String authUrl() {
		return "https://legendshop.cn/index";
	}

	@Override
	public R<Boolean> updateUserOpenId(BasisLoginParamsDTO loginParams) {
		return R.ok(true);
	}

	protected abstract R<Void> verify(BasisLoginParamsDTO loginParams);

	protected abstract R<UserInfo> identify(BasisLoginParamsDTO loginParams);

	/**
	 * 处理方法
	 *
	 * @param loginParams 登录参数
	 * @return userInfo
	 * @throws UsernameNotFoundException, BadCredentialsException, NotBindUserException
	 */
	@Override
	public R<UserInfo> handle(BasisLoginParamsDTO loginParams) throws UsernameNotFoundException, BadCredentialsException {

		// 参数校验
		log.info("开始进行参数校验， loginParams：{}", loginParams);
		R<Void> verify = verify(loginParams);
		if (!verify.getSuccess()) {
			log.info("参数校验失败， loginParams：{}", loginParams);
			return R.fail(verify.getMsg());
		}
		log.info("参数校验成功， loginParams：{}", loginParams);
		// 处理账号、获取信息
		return identify(loginParams);
	}

	/**
	 * 微信绑定处理
	 * 通过openId获取用户
	 * 微信授权只限普通用户
	 *
	 * @param wxUserInfo the 微信用户标识 token
	 * @return userInfo
	 */
	public R<UserInfo> info(WxUserInfo wxUserInfo) {
		if (null == wxUserInfo || StringUtils.isBlank(wxUserInfo.getOpenid())) {
			throw new BadCredentialsException("微信授权失败，请重新授权！");
		}
		PassportDTO passportDTO = this.passportService.getPassByIdentifier(wxUserInfo.getOpenid());
		if (null != passportDTO) {
			OrdinaryUserDTO ordinaryUserDTO = this.ordinaryUserService.getById(passportDTO.getUserId());
			if (null != ordinaryUserDTO) {
				return this.ordinaryUserService.getUserInfo(ordinaryUserDTO.getUsername());
			}
		}
		String code = RandomUtil.randomString(32);
		String key = TEMP_CERTIFICATE_KEY + code;
		this.redisTemplate.opsForValue().set(key, wxUserInfo.getOpenid(), 30, TimeUnit.MINUTES);
		wxUserInfo.setSource(VisitSourceEnum.valueOf(getSource()));
		this.passportService.saveToWxUserInfo(wxUserInfo);
		log.info("用户未绑定, code: {}", code);
		return R.fail(LegendshopSecurityErrorEnum.NOT_BIND_USER.name());
	}

	/**
	 * 微信绑定处理
	 * 通过openId获取用户
	 * 微信授权只限普通用户
	 *
	 * @return wxUserInfo
	 */
	public WxUserInfo weChatInfo(String url) {

		//https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html
		//第二步：通过code换取网页授权access_token
		String result = HttpUtil.get(url);
		log.debug("微信服务端响应报文结果:{}", result);

		WxUserInfo wxUserInfo = null;
		try {
			wxUserInfo = this.objectMapper.readValue(result, WxUserInfo.class);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("序列化微信返回信息失败！ response:{}", result);
		}
		if (null == wxUserInfo || StringUtils.isBlank(wxUserInfo.getOpenid())) {
			log.error("微信授权失败, 响应报文结果: {}", result);
			throw new BadCredentialsException("微信授权失败，请重新授权！");
		}
		return wxUserInfo;
	}

	/**
	 * 用户授权后，微信公众号可以获取用户的基本信息
	 *
	 * @param wxUserInfo
	 * @return
	 */
	public WxUserInfo weChatInfoExtend(WxUserInfo wxUserInfo) {
		//第四步：拉取用户信息(需scope为 snsapi_userinfo)
		//如果网页授权作用域为snsapi_userinfo，则此时开发者可以通过access_token和openid拉取用户信息了。
		if ("snsapi_userinfo".equals(getConfig().getScope())) {
			String userInfoUrl = String.format(SecurityConstants.WX_USER_INFO_SNS_URL, wxUserInfo.getAccess_token(), wxUserInfo.getOpenid());
			String userInfoResult = HttpUtil.get(userInfoUrl);
			try {
				log.info("获取用户扩展数据，{}", userInfoResult);
				wxUserInfo = this.objectMapper.readValue(userInfoResult, WxUserInfo.class);
			} catch (IOException e) {
				e.printStackTrace();
				log.error("序列化微信返回信息失败！ response:{}", userInfoResult);
			}
		}

		return wxUserInfo;
	}

	/**
	 * 微信绑定
	 * * @return wxUserInfo
	 */
	public R<UserInfo> wxUserBind(WxAuthorizationUserDTO authorizationUserDTO, WxUserInfo wxUserInfo) {
		// 获取微信绑定基本参数
		String openId = authorizationUserDTO.getOpenId();
		String mobile = authorizationUserDTO.getPhoneNumber();
		VisitSourceEnum source = authorizationUserDTO.getSource();
		String ip = authorizationUserDTO.getIp();

		// openId不存在无法认证用户
		if (StringUtils.isBlank(openId)) {
			log.info("获取不到openId, 绑定手机号:{}", mobile);
			return R.fail(LegendshopSecurityErrorEnum.THIRD_PARTY_AUTH_ERROR.name());
		}
		log.info("获取openId, 绑定手机号:{}", mobile);

		// 登录授权来源不明确
		if (null == source) {
			log.info("获取不到登录授权来源, 绑定手机号:{}", mobile);
			return R.fail(LegendshopSecurityErrorEnum.ERROR_AUTH_TYPE.name());
		}
		log.info("获取登录授权来源, 绑定手机号:{}", mobile);

		// 用户是否曾经授权
		PassportDTO passportDTO = this.passportService.getPassByIdentifier(openId);
		//找不到OPENID，
		if (null == passportDTO) {
			wxUserInfo.setSource(source);
			log.info("用户至今没有授权, 初始化微信授权信息, openid:{}, 手机号:{}", wxUserInfo.getOpenid(), mobile);
			passportDTO = this.passportService.saveToWxUserInfo(wxUserInfo).getData();
		}

		// 是否已经绑定用户
		Long userId = passportDTO.getUserId();
		if (null != userId) {
			log.info("已经绑定用户, 更新用户授权信息, 用户id:{}, 来源:{}, openId:{}", userId, source.name(), openId);
			// 更新用户授权信息
			this.passportService.updateAuthInfo(new PassportBO(userId, ThirdPartyAuthTypeEnum.WE_CHAT.name(), source.name(), openId));
			passportDTO.setNickName(wxUserInfo.getNickname());
			passportDTO.setHeadPortraitUrl(wxUserInfo.getHeadimgurl());
			this.passportService.update(passportDTO);
			// 已经绑定，登录成功
			return this.ordinaryUserService.getUserInfo(userId.toString());
		}

		// 手机号为空，返回绑定key，等待前端再次请求并绑定
		if (StringUtils.isBlank(mobile)) {
			log.info("手机号为空，返回绑定key，等待前端再次请求并绑定, 用户id:{}, 来源:{}, openId:{}", userId, source.name(), openId);
			String code = RandomUtil.randomNumbers(32);
			String key = TEMP_CERTIFICATE_KEY + code;
			this.redisTemplate.opsForValue().set(key, openId, 30, TimeUnit.MINUTES);
			return R.fail(HttpStatus.CREATED.value(), code);
		}

		// 手机号不为空，判断用户是否存在
		boolean exist = this.ordinaryUserService.isMobileExist(mobile);
		if (!exist) {
			// 手机号不存在，用户第一次授权
			log.info("手机号不为空, 手机号不存在，用户第一次授权, 手机号：{}, 来源:{}", mobile, source.name());
			OrdinaryUserDTO userDTO = new OrdinaryUserDTO();
			userDTO.setMobile(mobile);
			userDTO.setRegIp(ip);
			userDTO.setSource(source);
			userDTO.setNickName(passportDTO.getNickName());
			userDTO.setVisitorId(authorizationUserDTO.getVisitorId());
			userDTO.setAvatar(passportDTO.getHeadPortraitUrl());
			userId = (Long) this.ordinaryUserService.save(userDTO).getData();
			log.info("手机号不为空, 手机号不存在，用户第一次授权, 保存用户信息， 用户id:{}, 手机号：{}, 来源:{}", userId, mobile, source.name());
			passportDTO.setUserId(userId);
			this.passportService.update(passportDTO);
			log.info("手机号不为空, 手机号不存在，用户第一次授权, 更新通行证， 用户id:{}, 手机号：{}, 来源:{}", userId, mobile, source.name());
			return this.ordinaryUserService.getUserInfo(userId.toString());
		}

		// 手机号存在，判断是否绑定了其他用户
		OrdinaryUserDTO userDTO = this.ordinaryUserService.getByMobile(mobile);
		log.info("手机号存在, 已经绑定其他用户， 用户id:{}, 手机号：{}", userDTO.getId(), userDTO.getMobile());
		// 更新用户授权信息
		R<PassportBO> passportResult = this.passportService.updateAuthInfo(new PassportBO(userDTO.getId(), ThirdPartyAuthTypeEnum.WE_CHAT.name(), source.name(), openId));

		// 用户信息错误
		if (!passportResult.getSuccess() || null == passportResult.getData()) {
			log.info("手机号存在, 用户信息错误， passportResult:{}", passportResult.getData());
			return R.fail(LegendshopSecurityErrorEnum.EXIST_BIND_ACCOUNT.name());
		}
		log.info("手机号存在, 更新用户授权信息， 用户id:{}, 来源：{}", passportResult.getData().getUserId(), passportResult.getData().getSource());

		return this.ordinaryUserService.getUserInfo(passportResult.getData().getUserId().toString());
	}

	private WxMpConfig getConfig() {
		return wxApi.getMpConfig().getData();
	}

	public String getSource() {
		return source;
	}
}
