/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.console.auth.server.authentication;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.enums.AuthTypeEnum;
import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.common.security.enums.ClientIdEnum;
import com.legendshop.common.security.exception.ErrorAuthTypeException;
import com.legendshop.common.security.utils.CryptographicSignatureUtil;
import com.legendshop.console.auth.server.constant.CustomizeOauth2ParameterNames;
import com.legendshop.console.auth.server.constant.Oauth2Constant;
import com.legendshop.console.auth.server.service.LegendShopUserDetailsService;
import com.legendshop.console.auth.server.service.impl.LegendShopPreAuthenticationChecks;
import com.legendshop.console.auth.server.utils.RequestLimiterUtil;
import com.legendshop.user.dto.BasisLoginParamsDTO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 聚合登录认证提供者
 *
 * @author legendshop
 */
public class LegendshopAuthenticationProvider implements AuthenticationProvider {

	@Resource
	private LegendShopUserDetailsService userDetailsService;
	private RequestLimiterUtil requestLimiterUtil;
	private final OAuth2AuthorizationService authorizationService;
	private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
	private final Log logger = LogFactory.getLog(getClass());

	private final UserDetailsChecker detailsChecker = new LegendShopPreAuthenticationChecks();

	public LegendshopAuthenticationProvider(OAuth2AuthorizationService authorizationService,
											OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator, RequestLimiterUtil limiterUtil) {
		Assert.notNull(authorizationService, "authorizationService cannot be null");
		Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
		this.authorizationService = authorizationService;
		this.tokenGenerator = tokenGenerator;
		this.requestLimiterUtil = limiterUtil;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		LegendshopGrantAuthenticationToken legendshopGrantAuthenticationToken =
				(LegendshopGrantAuthenticationToken) authentication;

		// 参数校验
		BasisLoginParamsDTO loginParams = paramVerify(legendshopGrantAuthenticationToken);
		String principal = loginParams.getPrincipal();
		// 查询登录用户
		UserDetails userDetails;
		try {
			userDetails = this.userDetailsService.loadUserByUsername(loginParams);
		} catch (Exception e) {
			this.requestLimiterUtil.fail(principal);
			throw e;
		}
		this.detailsChecker.check(userDetails);


		// 注入用户扩展信息
		legendshopGrantAuthenticationToken.setDetails(userDetails);
		Map<String, Object> additionalParameters = legendshopGrantAuthenticationToken.getAdditionalParameters();
		//请求参数权限范围
		String requestScopesStr = (String) additionalParameters.get(OAuth2ParameterNames.SCOPE);
		//请求参数权限范围专场集合
		Set<String> requestScopeSet = Stream.of(requestScopesStr.split(" ")).collect(Collectors.toSet());

		// Ensure the client is authenticated
		OAuth2ClientAuthenticationToken clientPrincipal =
				getAuthenticatedClientElseThrowInvalidClient(legendshopGrantAuthenticationToken);
		RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

		//登录前，查看当前帐号是否有登录，如果有登录，会把上一次登录的有效AccessToken清理。（目前只踢移动端用户，商家，平台后台先不限制）
		if (!ClientIdEnum.SHOP.getType().equals(registeredClient.getClientId())
				&& !ClientIdEnum.ADMIN.getType().equals(registeredClient.getClientId())) {
		}

		//授权类型
		AuthorizationGrantType authorizationGrantType = legendshopGrantAuthenticationToken.getGrantType();
		if (!registeredClient.getAuthorizationGrantTypes().contains(authorizationGrantType)) {
			throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
		}

		//由于在上面已验证过用户名、密码，现在构建一个已认证的对象UsernamePasswordAuthenticationToken
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken.authenticated(userDetails, clientPrincipal, userDetails.getAuthorities());

		// Initialize the DefaultOAuth2TokenContext
		DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
				.registeredClient(registeredClient)
				.principal(usernamePasswordAuthenticationToken)
				.authorizationServerContext(AuthorizationServerContextHolder.getContext())
				.authorizationGrantType(authorizationGrantType)
				.authorizedScopes(requestScopeSet)
				.authorizationGrant(legendshopGrantAuthenticationToken);

		// Initialize the OAuth2Authorization
		OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
				//.principalName(clientPrincipal.getName())
				.principalName(userDetails.getUsername())
				.authorizedScopes(requestScopeSet)
				.attribute(Principal.class.getName(), usernamePasswordAuthenticationToken)
				.authorizationGrantType(authorizationGrantType);

		// ----- Access token -----
		OAuth2TokenContext tokenContext = tokenContextBuilder
				.tokenType(OAuth2TokenType.ACCESS_TOKEN)
				.put(CustomizeOauth2ParameterNames.USER_KEY, Objects.isNull(additionalParameters.get(CustomizeOauth2ParameterNames.USER_KEY)) ? "" : additionalParameters.get(CustomizeOauth2ParameterNames.USER_KEY))
				.build();

		OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
		if (generatedAccessToken == null) {
			OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
					"The token generator failed to generate the access token.", null);
			throw new OAuth2AuthenticationException(error);
		}

		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Generated access token");
		}

		OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
				generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
				generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
		if (generatedAccessToken instanceof ClaimAccessor) {
			authorizationBuilder.token(accessToken, (metadata) ->
					metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()));
		} else {
			authorizationBuilder.accessToken(accessToken);
		}

		// ----- Refresh token -----
		OAuth2RefreshToken refreshToken = null;
		if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
				// Do not issue refresh token to public client
				!clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

			tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
			OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
			if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
				OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
						"The token generator failed to generate the refresh token.", null);
				throw new OAuth2AuthenticationException(error);
			}

			if (this.logger.isTraceEnabled()) {
				this.logger.trace("Generated refresh token");
			}

			refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
			authorizationBuilder.refreshToken(refreshToken);
		}
		//保存认证信息
		OAuth2Authorization authorization = authorizationBuilder.build();
		this.authorizationService.save(authorization);

		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Saved authorization");
		}

		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Authenticated token request");
		}

		this.requestLimiterUtil.success(principal);

		// 认证成功返回扩展参数
		Map<String, Object> claims = authorization.getAccessToken().getClaims();
		return new OAuth2AccessTokenAuthenticationToken(
				registeredClient, clientPrincipal, accessToken, refreshToken, Objects.requireNonNull(claims));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return LegendshopGrantAuthenticationToken.class.isAssignableFrom(authentication);
	}

	/**
	 * 校验ClientToken
	 *
	 * @param authentication
	 * @return
	 */
	private static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
		OAuth2ClientAuthenticationToken clientPrincipal = null;
		if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
			clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
		}
		if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
			return clientPrincipal;
		}
		throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
	}


	/**
	 * *校验参数
	 *
	 * @param authenticationToken
	 * @return
	 */
	private BasisLoginParamsDTO paramVerify(LegendshopGrantAuthenticationToken authenticationToken) {

		String principal = (String) authenticationToken.getAdditionalParameters().get("principal");
		String credentials = (String) authenticationToken.getAdditionalParameters().get("credentials");
		String thirdPartyIdentifier = (String) authenticationToken.getAdditionalParameters().get("third_party_identifier");
		String authType = (String) authenticationToken.getAdditionalParameters().get("auth_type");
		String userType = (String) authenticationToken.getAdditionalParameters().get("user_type");
		String extended = (String) authenticationToken.getAdditionalParameters().get("extended");
		String userKey = (String) authenticationToken.getAdditionalParameters().get("user_key");
		if (StringUtils.isBlank(authType)) {
			throw new ErrorAuthTypeException("错误的认证类型");
		}
		if (StringUtils.isBlank(userType)) {
			throw new ErrorAuthTypeException("错误的用户认证类型");
		}
		if (StringUtils.isBlank(principal)) {
			throw new AccessDeniedException("用户登录标识不能为空");
		}
		if (StringUtils.isBlank(credentials)) {
			throw new AccessDeniedException("用户登录凭证不能为空");
		}
		AuthTypeEnum authTypeEnum;
		UserTypeEnum userTypeEnum;
		try {
			authTypeEnum = AuthTypeEnum.codeValue(authType);
			userTypeEnum = UserTypeEnum.codeValue(userType);
		} catch (Exception e) {
			throw new AccessDeniedException(e.getMessage());
		}

		// 限制请求次数
		if (authTypeEnum.equals(AuthTypeEnum.PASSWORD)) {
			// 解密
			principal = CryptographicSignatureUtil.decrypt(principal);
			credentials = CryptographicSignatureUtil.decrypt(credentials);
			// 请求次数
			R<Integer> limiter = this.requestLimiterUtil.limiter(principal);
			if (Oauth2Constant.MAX_COUNT.equals(limiter.getData())) {
				// 锁定用户
				this.userDetailsService.userLock(authenticationToken);
			}
			if (limiter.getSuccess() == null || Oauth2Constant.MAX_COUNT.equals(limiter.getData()) || !limiter.getSuccess()) {
				throw new AccessDeniedException("密码错误" + Oauth2Constant.MAX_COUNT + "次，账户已锁定！");
			}
		}
		return new BasisLoginParamsDTO(principal,
				credentials, thirdPartyIdentifier, authTypeEnum, userTypeEnum, extended, "", "", userKey);
	}
}
