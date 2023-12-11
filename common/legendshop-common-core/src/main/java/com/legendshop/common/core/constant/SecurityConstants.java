/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.constant;

/**
 * 认证的常量
 *
 * @author legendshop
 */
public interface SecurityConstants {

	String AUTHORIZATION_HEADER = "Authorization";

	String TOKEN_PREFIX = "Bearer ";

	String BASIC_PREFIX = "Basic ";

	String FEIGN_PREFIX = "FeignClient";


	/**
	 * 刷新
	 */
	String REFRESH_TOKEN = "refresh_token";
	/**
	 * The client fields.
	 */
	String CLIENT_FIELDS = "client_id, CONCAT('{noop}',client_secret) as client_secret, resource_ids, scope, "
			+ "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
			+ "refresh_token_validity, additional_information, autoapprove";

	/**
	 * JdbcClientDetailsService 查询语句.
	 */
	String BASE_FIND_STATEMENT = "select " + CLIENT_FIELDS + " from ls_oauth_client_details";

	/**
	 * 默认的查询语句.
	 */
	String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";

	/**
	 * 按条件client_id 查询.
	 */
	String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";

	/**
	 * redis prefix.
	 */
	String LEGENDSHOP_PREFIX = "ls_";

	/**
	 * The oauth prefix.
	 */
	String OAUTH_PREFIX = "oauth:";

	/**
	 * OAUTH URL
	 */
	String OAUTH_TOKEN_URL = "/oauth/token";


	/**
	 * 手机号码登录
	 */
	String SMS_TOKEN_URL = "/mobile/token/sms";

	/**
	 * 内部
	 */
	String FROM_IN = "Y";

	/**
	 * 标志
	 */
	String FROM = "from";

	/**
	 * 资源服务器默认bean名称
	 */

	String RESOURCE_SERVER_CONFIGURER = "resourceServerConfigurerAdapter";


	/**
	 * {bcrypt} 加密的特征码
	 */
	String BCRYPT = "{bcrypt}";

	/**
	 * 角色前缀
	 */
	String ROLE = "ROLE_";


	/**
	 * 用户基本信息
	 */
	String USER_TYPE = "user_type";

	/**
	 * 客户端模式
	 */
	String CLIENT_CREDENTIALS = "client_credentials";


	/**
	 * 第三方登录的url
	 */
	String SOCIAL_TOKEN_URL = "/social/token";


	/**
	 * PC授权地址模板
	 */
	String WX_PC_AUTHORIZATION_CODE_URL = "https://open.weixin.qq.com/connect/qrconnect" +
			"?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect";

	/**
	 * 手机授权地址模板
	 */
	String H5_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize" +
			"?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";


	/**
	 * 调用微信服务器获取openId
	 */
	String WX_AUTHORIZATION_CODE_URL = "https://api.weixin.qq.com/sns/oauth2/access_token"
			+ "?appId=%s&secret=%s&code=%s&grant_type=authorization_code";

	/**
	 * 微信公众平台获取用户信息
	 */
	String WX_USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";


	/**
	 * 获取用户基本信息(UnionID机制)
	 */
	String WX_USER_INFO_SNS_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

	/**
	 * 调用微信小程序获取openId
	 */
	String WX_MINI_AUTHORIZATION_CODE_URL = "https://api.weixin.qq.com/sns/jscode2session"
			+ "?appId=%s&secret=%s&js_code=%s&grant_type=authorization_code";
}
